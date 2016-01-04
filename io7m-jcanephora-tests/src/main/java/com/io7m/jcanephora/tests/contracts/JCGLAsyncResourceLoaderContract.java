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

package com.io7m.jcanephora.tests.contracts;

import com.io7m.jcanephora.async.JCGLAsyncBufferPairType;
import com.io7m.jcanephora.async.JCGLAsyncResourceLoaderType;
import com.io7m.jcanephora.core.JCGLArrayBufferType;
import com.io7m.jcanephora.core.JCGLBufferUpdates;
import com.io7m.jcanephora.core.JCGLIndexBufferType;
import com.io7m.jcanephora.core.JCGLTexture2DType;
import com.io7m.jcanephora.core.JCGLTextureFilterMagnification;
import com.io7m.jcanephora.core.JCGLTextureFilterMinification;
import com.io7m.jcanephora.core.JCGLTextureFormat;
import com.io7m.jcanephora.core.JCGLTextureUpdates;
import com.io7m.jcanephora.core.JCGLTextureWrapS;
import com.io7m.jcanephora.core.JCGLTextureWrapT;
import com.io7m.jcanephora.core.JCGLUnsignedType;
import com.io7m.jcanephora.core.JCGLUsageHint;
import com.io7m.jcanephora.texture_loader.core.JCGLTLTextureDataProviderType;
import com.io7m.jcanephora.texture_loader.core.JCGLTLTextureDataType;
import com.io7m.jcanephora.texture_loader.core.JCGLTLTextureUpdateProviderType;
import com.io7m.jtensors.VectorWritable4DType;
import com.io7m.junreachable.UnreachableCodeException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Async resource loader contract.
 */

// CHECKSTYLE_JAVADOC:OFF

public abstract class JCGLAsyncResourceLoaderContract extends JCGLContract
{
  @Rule public ExpectedException expected = ExpectedException.none();

  protected abstract JCGLAsyncResourceLoaderType getLoader(String name);

  protected abstract JCGLTLTextureUpdateProviderType getUpdateProvider();

  protected abstract JCGLTLTextureDataProviderType getDataProvider();

  @Test
  public final void testLoadArrayIndexOK()
    throws Exception
  {
    final JCGLAsyncResourceLoaderType loader = this.getLoader("main");

    final CompletableFuture<JCGLAsyncBufferPairType> f =
      loader.loadArrayIndexBuffers(
        () -> "Data",
        (data) -> Long.valueOf(200L),
        JCGLUsageHint.USAGE_STATIC_DRAW,
        (data) -> Long.valueOf(100L),
        JCGLUnsignedType.TYPE_UNSIGNED_BYTE,
        JCGLUsageHint.USAGE_DYNAMIC_DRAW,
        (data, array) -> JCGLBufferUpdates.newUpdateReplacingAll(array),
        (data, index) -> JCGLBufferUpdates.newUpdateReplacingAll(index)
      );

    final JCGLAsyncBufferPairType p = f.get(30L, TimeUnit.SECONDS);
    final JCGLArrayBufferType a = p.getArrayBuffer();
    final JCGLIndexBufferType i = p.getIndexBuffer();

    Assert.assertEquals(200L, a.getRange().getInterval());
    Assert.assertEquals(JCGLUsageHint.USAGE_STATIC_DRAW, a.getUsageHint());

    Assert.assertEquals(100L, i.getRange().getInterval());
    Assert.assertEquals(JCGLUnsignedType.TYPE_UNSIGNED_BYTE, i.getType());
    Assert.assertEquals(JCGLUsageHint.USAGE_DYNAMIC_DRAW, i.getUsageHint());
  }

  @Test
  public final void testLoadArrayIndexIODataError()
    throws Exception
  {
    final JCGLAsyncResourceLoaderType loader = this.getLoader("main");

    final CompletableFuture<JCGLAsyncBufferPairType> f =
      loader.loadArrayIndexBuffers(
        () -> {
          throw new UncheckedIOException(new IOException("IO failure"));
        },
        (data) -> Long.valueOf(23L),
        JCGLUsageHint.USAGE_STATIC_DRAW,
        (data) -> Long.valueOf(42L),
        JCGLUnsignedType.TYPE_UNSIGNED_INT,
        JCGLUsageHint.USAGE_STATIC_DRAW,
        (data, array) -> JCGLBufferUpdates.newUpdateReplacingAll(array),
        (data, index) -> JCGLBufferUpdates.newUpdateReplacingAll(index)
      );

    this.expected.expect(ExecutionException.class);
    this.expected.expectMessage("IO failure");
    f.get(30L, TimeUnit.SECONDS);
  }

  @Test
  public final void testLoadArrayIndexIOArraySizeError()
    throws Exception
  {
    final JCGLAsyncResourceLoaderType loader = this.getLoader("main");

    final CompletableFuture<JCGLAsyncBufferPairType> f =
      loader.loadArrayIndexBuffers(
        () -> "Hello",
        (data) -> {
          throw new UncheckedIOException(new IOException("IO failure"));
        },
        JCGLUsageHint.USAGE_STATIC_DRAW,
        (data) -> Long.valueOf(42L),
        JCGLUnsignedType.TYPE_UNSIGNED_INT,
        JCGLUsageHint.USAGE_STATIC_DRAW,
        (data, array) -> JCGLBufferUpdates.newUpdateReplacingAll(array),
        (data, index) -> JCGLBufferUpdates.newUpdateReplacingAll(index)
      );

    this.expected.expect(ExecutionException.class);
    this.expected.expectMessage("IO failure");
    f.get(30L, TimeUnit.SECONDS);
  }

  @Test
  public final void testLoadArrayIndexIOIndexSizeError()
    throws Exception
  {
    final JCGLAsyncResourceLoaderType loader = this.getLoader("main");

    final CompletableFuture<JCGLAsyncBufferPairType> f =
      loader.loadArrayIndexBuffers(
        () -> "Hello",
        (data) -> Long.valueOf(42L),
        JCGLUsageHint.USAGE_STATIC_DRAW,
        (data) -> {
          throw new UncheckedIOException(new IOException("IO failure"));
        },
        JCGLUnsignedType.TYPE_UNSIGNED_INT,
        JCGLUsageHint.USAGE_STATIC_DRAW,
        (data, array) -> JCGLBufferUpdates.newUpdateReplacingAll(array),
        (data, index) -> JCGLBufferUpdates.newUpdateReplacingAll(index)
      );

    this.expected.expect(ExecutionException.class);
    this.expected.expectMessage("IO failure");
    f.get(30L, TimeUnit.SECONDS);
  }

  @Test
  public final void testLoadArrayIndexIOIndexUpdateError()
    throws Exception
  {
    final JCGLAsyncResourceLoaderType loader = this.getLoader("main");

    final CompletableFuture<JCGLAsyncBufferPairType> f =
      loader.loadArrayIndexBuffers(
        () -> "Hello",
        (data) -> Long.valueOf(42L),
        JCGLUsageHint.USAGE_STATIC_DRAW,
        (data) -> Long.valueOf(42L),
        JCGLUnsignedType.TYPE_UNSIGNED_INT,
        JCGLUsageHint.USAGE_STATIC_DRAW,
        (data, array) -> JCGLBufferUpdates.newUpdateReplacingAll(array),
        (data, index) -> {
          throw new UncheckedIOException(new IOException("IO failure"));
        }
      );

    this.expected.expect(ExecutionException.class);
    this.expected.expectMessage("IO failure");
    f.get(30L, TimeUnit.SECONDS);
  }

  @Test
  public final void testLoadArrayIndexIOArrayUpdateError()
    throws Exception
  {
    final JCGLAsyncResourceLoaderType loader = this.getLoader("main");

    final CompletableFuture<JCGLAsyncBufferPairType> f =
      loader.loadArrayIndexBuffers(
        () -> "Hello",
        (data) -> Long.valueOf(42L),
        JCGLUsageHint.USAGE_STATIC_DRAW,
        (data) -> {
          throw new UncheckedIOException(new IOException("IO failure"));
        },
        JCGLUnsignedType.TYPE_UNSIGNED_INT,
        JCGLUsageHint.USAGE_STATIC_DRAW,
        (data, array) -> JCGLBufferUpdates.newUpdateReplacingAll(array),
        (data, index) -> JCGLBufferUpdates.newUpdateReplacingAll(index)
      );

    this.expected.expect(ExecutionException.class);
    this.expected.expectMessage("IO failure");
    f.get(30L, TimeUnit.SECONDS);
  }

  @Test
  public final void testLoadTextureIOError()
    throws Exception
  {
    final JCGLAsyncResourceLoaderType loader = this.getLoader("main");

    final CompletableFuture<JCGLTexture2DType> f =
      loader.loadTexture(
        () -> {
          throw new UncheckedIOException(new IOException("IO failure"));
        },
        JCGLTextureFormat.TEXTURE_FORMAT_RGB_8_3BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST,
        (tex, data) -> JCGLTextureUpdates.newUpdateReplacingAll2D(tex)
      );

    this.expected.expect(ExecutionException.class);
    this.expected.expectMessage("IO failure");
    f.get(30L, TimeUnit.SECONDS);
  }

  @Test
  public final void testLoadTextureUpdateError()
    throws Exception
  {
    final JCGLAsyncResourceLoaderType loader = this.getLoader("main");

    final CompletableFuture<JCGLTexture2DType> f =
      loader.loadTexture(
        () -> new JCGLTLTextureDataType()
        {
          @Override
          public boolean isPremultipliedAlpha()
          {
            return false;
          }

          @Override
          public long getWidth()
          {
            return 32L;
          }

          @Override
          public long getHeight()
          {
            return 64L;
          }

          @Override
          public void getPixel(
            final int x,
            final int y,
            final VectorWritable4DType v)
          {
            v.set4D(0.0, 0.0, 0.0, 1.0);
          }
        },
        JCGLTextureFormat.TEXTURE_FORMAT_RGB_8_3BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST,
        (tex, data) -> {
          Assert.assertEquals(32L, tex.textureGetWidth());
          Assert.assertEquals(64L, tex.textureGetHeight());
          Assert.assertEquals(
            JCGLTextureFormat.TEXTURE_FORMAT_RGB_8_3BPP,
            tex.textureGetFormat());
          Assert.assertEquals(
            JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
            tex.textureGetWrapS());
          Assert.assertEquals(
            JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
            tex.textureGetWrapT());
          Assert.assertEquals(
            JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
            tex.textureGetMinificationFilter());
          Assert.assertEquals(
            JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST,
            tex.textureGetMagnificationFilter());
          throw new UncheckedIOException(new IOException("IO failure"));
        }
      );

    this.expected.expect(ExecutionException.class);
    this.expected.expectMessage("IO failure");
    f.get(30L, TimeUnit.SECONDS);
  }

  @Test
  public final void testLoadTextureOK()
    throws Exception
  {
    final Class<JCGLAsyncResourceLoaderContract> c =
      JCGLAsyncResourceLoaderContract.class;

    final JCGLAsyncResourceLoaderType loader =
      this.getLoader("main");
    final JCGLTLTextureDataProviderType data_prov =
      this.getDataProvider();
    final JCGLTLTextureUpdateProviderType update_prov =
      this.getUpdateProvider();

    final CompletableFuture<JCGLTexture2DType> f =
      loader.loadTexture(
        () -> {
          try {
            return data_prov.loadFromStream(
              c.getResourceAsStream("basn6a08.png"));
          } catch (final IOException e) {
            throw new UnreachableCodeException();
          }
        },
        JCGLTextureFormat.TEXTURE_FORMAT_RGB_8_3BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST,
        (tex, data) -> update_prov.getTextureUpdate(tex, data)
      );

    final JCGLTexture2DType tex = f.get(30L, TimeUnit.SECONDS);
    Assert.assertEquals(32L, tex.textureGetWidth());
    Assert.assertEquals(32L, tex.textureGetHeight());
    Assert.assertEquals(
      JCGLTextureFormat.TEXTURE_FORMAT_RGB_8_3BPP,
      tex.textureGetFormat());
    Assert.assertEquals(
      JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
      tex.textureGetWrapS());
    Assert.assertEquals(
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
      tex.textureGetWrapT());
    Assert.assertEquals(
      JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
      tex.textureGetMinificationFilter());
    Assert.assertEquals(
      JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST,
      tex.textureGetMagnificationFilter());
  }
}
