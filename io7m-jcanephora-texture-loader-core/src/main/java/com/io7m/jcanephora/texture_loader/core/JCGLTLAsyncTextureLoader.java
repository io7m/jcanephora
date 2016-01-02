/*
 * Copyright © 2015 <code@io7m.com> http://io7m.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.io7m.jcanephora.texture_loader.core;

import com.io7m.jcanephora.core.JCGLExceptionConcurrency;
import com.io7m.jcanephora.core.JCGLExceptionContextNotCurrent;
import com.io7m.jcanephora.core.JCGLExceptionDeleted;
import com.io7m.jcanephora.core.JCGLTexture2DType;
import com.io7m.jcanephora.core.JCGLTexture2DUpdateType;
import com.io7m.jcanephora.core.JCGLTexture2DUsableType;
import com.io7m.jcanephora.core.JCGLTextureFilterMagnification;
import com.io7m.jcanephora.core.JCGLTextureFilterMinification;
import com.io7m.jcanephora.core.JCGLTextureFormat;
import com.io7m.jcanephora.core.JCGLTextureUnitType;
import com.io7m.jcanephora.core.JCGLTextureWrapS;
import com.io7m.jcanephora.core.JCGLTextureWrapT;
import com.io7m.jcanephora.core.api.JCGLContextUsableType;
import com.io7m.jcanephora.core.api.JCGLInterfaceGL33Type;
import com.io7m.jcanephora.core.api.JCGLTexturesType;
import com.io7m.jnull.NullCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The default implementation of the {@link JCGLTLAsyncTextureLoaderType}
 * interface.
 */

public final class JCGLTLAsyncTextureLoader implements
  JCGLTLAsyncTextureLoaderType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(JCGLTLAsyncTextureLoader.class);
  }

  private final ExecutorService                 exec_gl;
  private final Executor                        exec_io;
  private final JCGLTLTextureDataProviderType   data_prov;
  private final JCGLTLTextureUpdateProviderType update_prov;
  private final JCGLTexturesType                textures;
  private final List<JCGLTextureUnitType>       units;
  private final AtomicLong                      ids;
  private final JCGLContextUsableType           context;
  private final AtomicBoolean                   deleted;

  private JCGLTLAsyncTextureLoader(
    final JCGLTLTextureDataProviderType in_data_prov,
    final JCGLTLTextureUpdateProviderType in_update_prov,
    final ExecutorService in_exec_gl,
    final Executor in_exec_io,
    final JCGLContextUsableType in_context,
    final JCGLInterfaceGL33Type in_g)
  {
    this.data_prov = NullCheck.notNull(in_data_prov);
    this.update_prov = NullCheck.notNull(in_update_prov);
    this.exec_gl = NullCheck.notNull(in_exec_gl);
    this.exec_io = NullCheck.notNull(in_exec_io);
    this.context = NullCheck.notNull(in_context);
    this.textures = in_g.getTextures();
    this.units = this.textures.textureGetUnits();
    this.ids = new AtomicLong(0L);
    this.deleted = new AtomicBoolean(false);
  }

  /**
   * Construct a new loader.
   *
   * @param in_data_prov   A texture data provider
   * @param in_update_prov A texture update provider
   * @param in_context     An OpenGL context
   * @param in_exec_io     An executor containing threads for scheduling I/O
   *                       operations
   *
   * @return A new texture loader
   */

  public static JCGLTLAsyncTextureLoaderType newLoader(
    final JCGLTLTextureDataProviderType in_data_prov,
    final JCGLTLTextureUpdateProviderType in_update_prov,
    final JCGLContextUsableType in_context,
    final Executor in_exec_io)
  {
    try {
      JCGLTLAsyncTextureLoader.LOG.debug("creating texture loader");
      if (in_context.contextIsCurrent()) {
        in_context.contextReleaseCurrent();
      }

      /**
       * Spawn a single thread that services OpenGL calls...
       */

      final ExecutorService e = Executors.newFixedThreadPool(
        1, r -> {
          final Thread t = new Thread(r);
          t.setName("jcanephora-async-texture-loader-gl");
          t.setDaemon(true);
          return t;
        });

      /**
       * ... and then make the given context current upon that thread.
       */

      final JCGLInterfaceGL33Type g = e.submit(() -> {
        JCGLTLAsyncTextureLoader.LOG.debug(
          "making context current on GL thread");
        in_context.contextMakeCurrent();
        return in_context.contextGetGL33();
      }).get();

      return new JCGLTLAsyncTextureLoader(
        in_data_prov, in_update_prov, e, in_exec_io, in_context, g);

    } catch (final InterruptedException e) {
      throw new JCGLExceptionConcurrency(e);
    } catch (final ExecutionException e) {
      throw new JCGLExceptionConcurrency(e.getCause());
    }
  }

  @Override
  public AsyncTexture loadTextureFromStream(
    final InputStream is,
    final JCGLTexture2DUsableType t_default,
    final JCGLTexture2DUsableType t_error,
    final JCGLTextureFormat format,
    final JCGLTextureWrapS wrap_s,
    final JCGLTextureWrapT wrap_t,
    final JCGLTextureFilterMinification mini,
    final JCGLTextureFilterMagnification mag)
  {
    NullCheck.notNull(is);
    NullCheck.notNull(t_default);
    NullCheck.notNull(t_error);
    NullCheck.notNull(format);
    NullCheck.notNull(wrap_s);
    NullCheck.notNull(wrap_t);
    NullCheck.notNull(mini);
    NullCheck.notNull(mag);

    if (this.deleted.get()) {
      throw new JCGLExceptionDeleted("Texture loader has been shut down");
    }

    final JCGLTextureUnitType unit = this.units.get(0);
    final long id = this.ids.get();
    final Long idb = Long.valueOf(id);

    /**
     * A function that, when evaluated, yields texture data. Executed
     * on one of the I/O threads.
     */

    final Supplier<JCGLTLTextureDataType> read_texture = () -> {
      try {
        JCGLTLAsyncTextureLoader.LOG.debug(
          "[{}] loading texture from stream {}", idb, is);
        final JCGLTLTextureDataType data = this.data_prov.loadFromStream(is);
        is.close();
        return data;
      } catch (final IOException e) {
        JCGLTLAsyncTextureLoader.LOG.error(
          "[{}] failed to load texture from stream: ", idb, e);
        throw new UncheckedIOException(e);
      }
    };

    /**
     * A function that takes texture data and yields a texture. Executed
     * on the GL thread.
     */

    final Function<JCGLTLTextureDataType, JCGLTexture2DType> upload_texture =
      d -> {
        if (!this.context.contextIsCurrent()) {
          throw new JCGLExceptionContextNotCurrent(
            "Context is not current on this thread");
        }

        JCGLTLAsyncTextureLoader.LOG.debug("[{}] allocating texture", idb);
        final long w = d.getWidth();
        final long h = d.getHeight();
        final JCGLTexture2DType t = this.textures.texture2DAllocate(
          unit, w, h, format, wrap_s, wrap_t, mini, mag);

        JCGLTLAsyncTextureLoader.LOG.debug("[{}] populating texture", idb);
        final JCGLTexture2DUpdateType u =
          this.update_prov.getTextureUpdate(t, d);
        this.textures.texture2DUpdate(unit, u);

        JCGLTLAsyncTextureLoader.LOG.debug("[{}] loaded texture", idb);
        return t;
      };

    /**
     * A function that, when evaluated, saves either the given non-null
     * texture or the given non-null exception.
     */

    final AtomicReference<JCGLTexture2DType> result = new AtomicReference<>();
    final AtomicReference<Throwable> error = new AtomicReference<>();
    final BiConsumer<JCGLTexture2DType, Throwable> save_result =
      (r_texture, r_error) -> {
        if (r_texture != null) {
          result.set(r_texture);
        }
        if (r_error != null) {
          error.set(r_error);
        }
      };

    /**
     * Construct the pipeline.
     */

    final CompletableFuture<JCGLTexture2DType> f_complete =
      CompletableFuture.supplyAsync(read_texture, this.exec_io)
        .thenApplyAsync(upload_texture, this.exec_gl)
        .whenComplete(save_result);

    return new AsyncTexture(
      idb,
      this.textures,
      t_default,
      t_error,
      f_complete,
      result,
      error,
      this.exec_gl);
  }

  @Override
  public boolean shutDownSynchronously(
    final long t,
    final TimeUnit unit)
    throws InterruptedException
  {
    if (this.deleted.get()) {
      return true;
    }

    try {
      this.deleted.set(true);

      JCGLTLAsyncTextureLoader.LOG.debug("shutting down texture loader");
      JCGLTLAsyncTextureLoader.LOG.debug("shutting down GL thread");
      this.exec_gl.submit(() -> {
        JCGLTLAsyncTextureLoader.LOG.trace("releasing context");
        this.context.contextReleaseCurrent();
      }).get(t, unit);
      this.exec_gl.shutdown();
      return this.exec_gl.awaitTermination(t, unit);
    } catch (final ExecutionException e) {
      throw new JCGLExceptionConcurrency(e);
    } catch (final TimeoutException e) {
      throw new JCGLExceptionConcurrency(e);
    }
  }

  @Override
  public boolean isDeleted()
  {
    return this.deleted.get();
  }

  private static final class AsyncTexture
    implements JCGLTLSoftAsyncTexture2DType
  {
    private final JCGLTexture2DUsableType              texture_default;
    private final JCGLTexture2DUsableType              texture_error;
    private final CompletableFuture<JCGLTexture2DType> future;
    private final AtomicReference<JCGLTexture2DType>   result;
    private final AtomicReference<Throwable>           error;
    private final AtomicBoolean                        deleted;
    private final Executor                             exec;
    private final JCGLTexturesType                     textures;
    private final Long                                 id;

    private AsyncTexture(
      final Long in_id,
      final JCGLTexturesType in_textures,
      final JCGLTexture2DUsableType in_texture_default,
      final JCGLTexture2DUsableType in_texture_error,
      final CompletableFuture<JCGLTexture2DType> in_future,
      final AtomicReference<JCGLTexture2DType> in_result,
      final AtomicReference<Throwable> in_error,
      final Executor in_exec)
    {
      this.id = NullCheck.notNull(in_id);
      this.textures = NullCheck.notNull(in_textures);
      this.texture_default = NullCheck.notNull(in_texture_default);
      this.texture_error = NullCheck.notNull(in_texture_error);
      this.future = NullCheck.notNull(in_future);
      this.result = NullCheck.notNull(in_result);
      this.error = NullCheck.notNull(in_error);
      this.exec = NullCheck.notNull(in_exec);
      this.deleted = new AtomicBoolean(false);
    }

    @Override
    public JCGLTexture2DUsableType get()
    {
      final Throwable e = this.error.get();
      if (e != null) {
        return this.texture_error;
      }
      final JCGLTexture2DType r = this.result.get();
      if (r != null) {
        return r;
      }
      return this.texture_default;
    }

    @Override
    public void cancel()
    {
      this.future.cancel(false);
    }

    @Override
    public Future<Void> delete()
    {
      return this.future.thenAcceptAsync(t -> {
        JCGLTLAsyncTextureLoader.LOG.debug("[{}] deleting texture", this.id);
        if (t != null && !t.isDeleted()) {
          this.textures.texture2DDelete(t);
          this.deleted.set(true);
        } else {
          JCGLTLAsyncTextureLoader.LOG.debug(
            "[{}] texture already deleted or unavailable", this.id);
        }
      }, this.exec);
    }

    @Override
    public Future<JCGLTexture2DType> future()
    {
      return this.future;
    }

    @Override
    public boolean isDeleted()
    {
      return this.deleted.get();
    }
  }
}
