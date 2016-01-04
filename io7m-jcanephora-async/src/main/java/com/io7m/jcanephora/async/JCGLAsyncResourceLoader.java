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

package com.io7m.jcanephora.async;

import com.io7m.jcanephora.core.JCGLArrayBufferType;
import com.io7m.jcanephora.core.JCGLBufferUpdateType;
import com.io7m.jcanephora.core.JCGLIndexBufferType;
import com.io7m.jcanephora.core.JCGLTexture2DType;
import com.io7m.jcanephora.core.JCGLTexture2DUpdateType;
import com.io7m.jcanephora.core.JCGLTextureFilterMagnification;
import com.io7m.jcanephora.core.JCGLTextureFilterMinification;
import com.io7m.jcanephora.core.JCGLTextureFormat;
import com.io7m.jcanephora.core.JCGLTextureUnitType;
import com.io7m.jcanephora.core.JCGLTextureWrapS;
import com.io7m.jcanephora.core.JCGLTextureWrapT;
import com.io7m.jcanephora.core.JCGLUnsignedType;
import com.io7m.jcanephora.core.JCGLUsageHint;
import com.io7m.jcanephora.core.api.JCGLArrayBuffersType;
import com.io7m.jcanephora.core.api.JCGLIndexBuffersType;
import com.io7m.jcanephora.core.api.JCGLTexturesType;
import com.io7m.jcanephora.texture_loader.core.JCGLTLTextureDataType;
import com.io7m.jnull.NullCheck;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The default implementation of the {@link JCGLAsyncResourceLoaderType}
 * interface.
 */

public final class JCGLAsyncResourceLoader implements
  JCGLAsyncResourceLoaderType
{
  private final JCGLAsyncInterfaceGL33Type g;
  private final Executor                   exec_io;

  private JCGLAsyncResourceLoader(
    final Executor in_exec_io,
    final JCGLAsyncInterfaceGL33Type in_g)
  {
    this.exec_io = NullCheck.notNull(in_exec_io);
    this.g = NullCheck.notNull(in_g);
  }

  /**
   * Construct a new resource loader.
   *
   * @param in_exec_io An executor for I/O tasks
   * @param in_g       An async GL interface
   *
   * @return A new loader
   */

  public static JCGLAsyncResourceLoaderType newLoader(
    final Executor in_exec_io,
    final JCGLAsyncInterfaceGL33Type in_g)
  {
    return new JCGLAsyncResourceLoader(in_exec_io, in_g);
  }

  @Override
  public CompletableFuture<JCGLTexture2DType> loadTexture(
    final Supplier<JCGLTLTextureDataType> on_data,
    final JCGLTextureFormat format,
    final JCGLTextureWrapS wrap_s,
    final JCGLTextureWrapT wrap_t,
    final JCGLTextureFilterMinification min_filter,
    final JCGLTextureFilterMagnification mag_filter,
    final BiFunction<JCGLTexture2DType, JCGLTLTextureDataType,
      JCGLTexture2DUpdateType> on_populate)
  {
    NullCheck.notNull(on_data);
    NullCheck.notNull(format);
    NullCheck.notNull(wrap_s);
    NullCheck.notNull(wrap_t);
    NullCheck.notNull(min_filter);
    NullCheck.notNull(mag_filter);

    final CompletableFuture<JCGLTLTextureDataType> f_data =
      CompletableFuture.supplyAsync(on_data, this.exec_io);

    final CompletableFuture<JCGLTexture2DType> f_alloc =
      f_data.thenCompose(
        td -> this.g.evaluate(g33 -> {
          final JCGLTexturesType gt = g33.getTextures();
          final List<JCGLTextureUnitType> us = gt.textureGetUnits();
          final JCGLTextureUnitType u0 = us.get(0);
          final long w = td.getWidth();
          final long h = td.getHeight();
          return gt.texture2DAllocate(
            u0, w, h, format, wrap_s, wrap_t, min_filter, mag_filter);
        }));

    final CompletableFuture<JCGLTexture2DUpdateType> f_pop =
      f_alloc.thenCombine(f_data, on_populate);

    return f_pop.thenCompose(up -> this.g.evaluate(g33 -> {
      final JCGLTexturesType gt = g33.getTextures();
      final List<JCGLTextureUnitType> us = gt.textureGetUnits();
      final JCGLTextureUnitType u0 = us.get(0);
      gt.texture2DBind(u0, up.getTexture());
      gt.texture2DUpdate(u0, up);
      return (JCGLTexture2DType) up.getTexture();
    }));
  }

  @Override
  public <T> CompletableFuture<JCGLAsyncBufferPairType> loadArrayIndexBuffers(
    final Supplier<T> on_data,
    final Function<T, Long> on_array_size,
    final JCGLUsageHint array_usage,
    final Function<T, Long> on_index_size,
    final JCGLUnsignedType index_type,
    final JCGLUsageHint index_usage,
    final BiFunction<T, JCGLArrayBufferType,
      JCGLBufferUpdateType<JCGLArrayBufferType>> on_populate_array,
    final BiFunction<T, JCGLIndexBufferType,
      JCGLBufferUpdateType<JCGLIndexBufferType>> on_populate_index)
  {
    NullCheck.notNull(on_data);
    NullCheck.notNull(on_array_size);
    NullCheck.notNull(array_usage);
    NullCheck.notNull(on_index_size);
    NullCheck.notNull(index_type);
    NullCheck.notNull(index_usage);
    NullCheck.notNull(on_populate_array);
    NullCheck.notNull(on_populate_index);

    final CompletableFuture<T> f_data =
      CompletableFuture.supplyAsync(on_data, this.exec_io);
    final CompletableFuture<Long> f_array_size =
      f_data.thenApply(on_array_size);
    final CompletableFuture<Long> f_index_size =
      f_data.thenApply(on_index_size);

    final CompletableFuture<JCGLArrayBufferType> f_array_alloc =
      f_array_size.thenCompose(
        size -> this.g.evaluate(g33 -> {
          final long sz = size.longValue();
          final JCGLArrayBuffersType ga = g33.getArrayBuffers();
          ga.arrayBufferUnbind();
          return ga.arrayBufferAllocate(sz, array_usage);
        }));
    final CompletableFuture<JCGLIndexBufferType> f_index_alloc =
      f_index_size.thenCompose(
        size -> this.g.evaluate(g33 -> {
          final long sz = size.longValue();
          final JCGLIndexBuffersType gi = g33.getIndexBuffers();
          gi.indexBufferUnbind();
          return gi.indexBufferAllocate(sz, index_type, index_usage);
        }));

    final CompletableFuture<
      JCGLBufferUpdateType<JCGLArrayBufferType>> f_array_get_update =
      f_data.thenCombine(f_array_alloc, on_populate_array);
    final CompletableFuture<
      JCGLBufferUpdateType<JCGLIndexBufferType>> f_index_get_update =
      f_data.thenCombine(f_index_alloc, on_populate_index);

    final CompletableFuture<JCGLArrayBufferType> f_array_upload =
      f_array_get_update.thenCompose(
        up -> this.g.evaluate(g33 -> {
          final JCGLArrayBuffersType ga = g33.getArrayBuffers();
          ga.arrayBufferBind(up.getBuffer());
          ga.arrayBufferUpdate(up);
          ga.arrayBufferUnbind();
          return up.getBuffer();
        }));

    final CompletableFuture<JCGLIndexBufferType> f_index_upload =
      f_index_get_update.thenCompose(
        up -> this.g.evaluate(g33 -> {
          final JCGLIndexBuffersType gi = g33.getIndexBuffers();
          gi.indexBufferBind(up.getBuffer());
          gi.indexBufferUpdate(up);
          gi.indexBufferUnbind();
          return up.getBuffer();
        }));

    return f_array_upload.thenCombine(f_index_upload, BufferPair::new);
  }

  private static final class BufferPair implements JCGLAsyncBufferPairType
  {
    private final JCGLArrayBufferType array;
    private final JCGLIndexBufferType index;

    BufferPair(
      final JCGLArrayBufferType in_array,
      final JCGLIndexBufferType in_index)
    {
      this.array = NullCheck.notNull(in_array);
      this.index = NullCheck.notNull(in_index);
    }

    @Override
    public JCGLArrayBufferType getArrayBuffer()
    {
      return this.array;
    }

    @Override
    public JCGLIndexBufferType getIndexBuffer()
    {
      return this.index;
    }
  }
}
