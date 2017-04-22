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

package com.io7m.jcanephora.texture.loader.core;

import com.io7m.jcanephora.core.JCGLTexture2DUpdateType;
import com.io7m.jcanephora.core.JCGLTexture2DUsableType;
import com.io7m.jcanephora.core.JCGLTextureCubeUpdateType;
import com.io7m.jcanephora.core.JCGLTextureCubeUsableType;
import com.io7m.jcanephora.core.JCGLTextureFormat;
import com.io7m.jcanephora.core.JCGLTextureUpdates;
import com.io7m.jcanephora.cursors.JCGLDepth16ByteBuffered;
import com.io7m.jcanephora.cursors.JCGLDepth16Type;
import com.io7m.jcanephora.cursors.JCGLDepth24S8ByteBuffered;
import com.io7m.jcanephora.cursors.JCGLDepth24S8Type;
import com.io7m.jcanephora.cursors.JCGLDepth32FByteBuffered;
import com.io7m.jcanephora.cursors.JCGLDepth32FType;
import com.io7m.jcanephora.cursors.JCGLR16ByteBuffered;
import com.io7m.jcanephora.cursors.JCGLR16FByteBuffered;
import com.io7m.jcanephora.cursors.JCGLR16FType;
import com.io7m.jcanephora.cursors.JCGLR16Type;
import com.io7m.jcanephora.cursors.JCGLR32FByteBuffered;
import com.io7m.jcanephora.cursors.JCGLR32FType;
import com.io7m.jcanephora.cursors.JCGLR8ByteBuffered;
import com.io7m.jcanephora.cursors.JCGLR8Type;
import com.io7m.jcanephora.cursors.JCGLRG16ByteBuffered;
import com.io7m.jcanephora.cursors.JCGLRG16FByteBuffered;
import com.io7m.jcanephora.cursors.JCGLRG16FType;
import com.io7m.jcanephora.cursors.JCGLRG16Type;
import com.io7m.jcanephora.cursors.JCGLRG32FByteBuffered;
import com.io7m.jcanephora.cursors.JCGLRG32FType;
import com.io7m.jcanephora.cursors.JCGLRG8ByteBuffered;
import com.io7m.jcanephora.cursors.JCGLRG8Type;
import com.io7m.jcanephora.cursors.JCGLRGB16ByteBuffered;
import com.io7m.jcanephora.cursors.JCGLRGB16FByteBuffered;
import com.io7m.jcanephora.cursors.JCGLRGB16FType;
import com.io7m.jcanephora.cursors.JCGLRGB16Type;
import com.io7m.jcanephora.cursors.JCGLRGB32FByteBuffered;
import com.io7m.jcanephora.cursors.JCGLRGB32FType;
import com.io7m.jcanephora.cursors.JCGLRGB8ByteBuffered;
import com.io7m.jcanephora.cursors.JCGLRGB8Type;
import com.io7m.jcanephora.cursors.JCGLRGBA16ByteBuffered;
import com.io7m.jcanephora.cursors.JCGLRGBA16FByteBuffered;
import com.io7m.jcanephora.cursors.JCGLRGBA16FType;
import com.io7m.jcanephora.cursors.JCGLRGBA16Type;
import com.io7m.jcanephora.cursors.JCGLRGBA32FByteBuffered;
import com.io7m.jcanephora.cursors.JCGLRGBA32FType;
import com.io7m.jcanephora.cursors.JCGLRGBA8ByteBuffered;
import com.io7m.jcanephora.cursors.JCGLRGBA8Type;
import com.io7m.jpra.runtime.java.JPRACursor2DByteBufferedChecked;
import com.io7m.jpra.runtime.java.JPRACursor2DType;
import com.io7m.jranges.RangeCheck;
import com.io7m.jtensors.storage.heap.VectorMutable4D;
import com.io7m.junreachable.UnimplementedCodeException;

import java.nio.ByteBuffer;

/**
 * The default implementation of the {@link JCGLTLTextureUpdateProviderType}
 * interface.
 */

public final class JCGLTLTextureUpdateProvider implements
  JCGLTLTextureUpdateProviderType
{
  private JCGLTLTextureUpdateProvider()
  {

  }

  private static void copyR(
    final JCGLTLTextureDataType data,
    final JPRACursor2DType<?> cursor,
    final Consumer1DType setter,
    final int width,
    final int height)
  {
    final VectorMutable4D v = new VectorMutable4D();
    for (int y = 0; y < height; ++y) {
      final int iy = (height - 1) - y;
      for (int x = 0; x < width; ++x) {
        cursor.setElementPosition(x, iy);
        data.pixel(x, y, v);
        setter.set1d(v.x());
      }
    }
  }

  private static void copyRG(
    final JCGLTLTextureDataType data,
    final JPRACursor2DType<?> cursor,
    final Consumer2DType setter,
    final int width,
    final int height)
  {
    final VectorMutable4D v = new VectorMutable4D();
    for (int y = 0; y < height; ++y) {
      final int iy = (height - 1) - y;
      for (int x = 0; x < width; ++x) {
        cursor.setElementPosition(x, iy);
        data.pixel(x, y, v);
        setter.set2d(v.x(), v.y());
      }
    }
  }

  private static void copyRGB(
    final JCGLTLTextureDataType data,
    final JPRACursor2DType<?> cursor,
    final Consumer3DType setter,
    final int width,
    final int height)
  {
    final VectorMutable4D v = new VectorMutable4D();
    for (int y = 0; y < height; ++y) {
      final int iy = (height - 1) - y;
      for (int x = 0; x < width; ++x) {
        cursor.setElementPosition(x, iy);
        data.pixel(x, y, v);
        setter.set3d(v.x(), v.y(), v.z());
      }
    }
  }

  private static void copyRGBA(
    final JCGLTLTextureDataType data,
    final JPRACursor2DType<?> cursor,
    final Consumer4DType setter,
    final int width,
    final int height)
  {
    final VectorMutable4D v = new VectorMutable4D();
    for (int y = 0; y < height; ++y) {
      final int iy = (height - 1) - y;
      for (int x = 0; x < width; ++x) {
        cursor.setElementPosition(x, iy);
        data.pixel(x, y, v);
        setter.set4d(v.x(), v.y(), v.z(), v.w());
      }
    }
  }

  /**
   * @return A new texture update provider
   */

  public static JCGLTLTextureUpdateProviderType newProvider()
  {
    return new JCGLTLTextureUpdateProvider();
  }

  private static void populate(
    final JCGLTextureFormat format,
    final JCGLTLTextureDataType data,
    final ByteBuffer d,
    final int tw,
    final int th)
  {
    switch (format) {
      case TEXTURE_FORMAT_DEPTH_16_2BPP: {
        final JPRACursor2DType<JCGLDepth16Type> c =
          JPRACursor2DByteBufferedChecked.newCursor(
            d, tw, th, JCGLDepth16ByteBuffered::newValueWithOffset);
        final JCGLDepth16Type view = c.getElementView();
        copyR(data, c, view::set, tw, th);
        break;
      }

      case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP: {
        final JPRACursor2DType<JCGLDepth24S8Type> c =
          JPRACursor2DByteBufferedChecked.newCursor(
            d, tw, th, JCGLDepth24S8ByteBuffered::newValueWithOffset);
        final JCGLDepth24S8Type view = c.getElementView();
        copyR(data, c, view::set, tw, th);
        break;
      }

      case TEXTURE_FORMAT_DEPTH_32F_4BPP: {
        final JPRACursor2DType<JCGLDepth32FType> c =
          JPRACursor2DByteBufferedChecked.newCursor(
            d, tw, th, JCGLDepth32FByteBuffered::newValueWithOffset);
        final JCGLDepth32FType view = c.getElementView();
        copyR(data, c, (r) -> view.setDepth((float) r), tw, th);
        break;
      }

      case TEXTURE_FORMAT_R_16_2BPP: {
        final JPRACursor2DType<JCGLR16Type> c =
          JPRACursor2DByteBufferedChecked.newCursor(
            d, tw, th, JCGLR16ByteBuffered::newValueWithOffset);
        final JCGLR16Type view = c.getElementView();
        copyR(data, c, view::setR, tw, th);
        break;
      }

      case TEXTURE_FORMAT_R_16F_2BPP: {
        final JPRACursor2DType<JCGLR16FType> c =
          JPRACursor2DByteBufferedChecked.newCursor(
            d, tw, th, JCGLR16FByteBuffered::newValueWithOffset);
        final JCGLR16FType view = c.getElementView();
        copyR(data, c, view::setR, tw, th);
        break;
      }

      case TEXTURE_FORMAT_R_32F_4BPP: {
        final JPRACursor2DType<JCGLR32FType> c =
          JPRACursor2DByteBufferedChecked.newCursor(
            d, tw, th, JCGLR32FByteBuffered::newValueWithOffset);
        final JCGLR32FType view = c.getElementView();
        copyR(
          data, c, (r) -> view.setR((float) r), tw, th);
        break;
      }

      case TEXTURE_FORMAT_R_8_1BPP: {
        final JPRACursor2DType<JCGLR8Type> c =
          JPRACursor2DByteBufferedChecked.newCursor(
            d, tw, th, JCGLR8ByteBuffered::newValueWithOffset);
        final JCGLR8Type view = c.getElementView();
        copyR(data, c, view::setR, tw, th);
        break;
      }

      case TEXTURE_FORMAT_RG_16_4BPP: {
        final JPRACursor2DType<JCGLRG16Type> c =
          JPRACursor2DByteBufferedChecked.newCursor(
            d, tw, th, JCGLRG16ByteBuffered::newValueWithOffset);
        final JCGLRG16Type view = c.getElementView();
        copyRG(data, c, (r, g) -> {
          view.setR(r);
          view.setG(g);
        }, tw, th);
        break;
      }

      case TEXTURE_FORMAT_RG_16F_4BPP: {
        final JPRACursor2DType<JCGLRG16FType> c =
          JPRACursor2DByteBufferedChecked.newCursor(
            d, tw, th, JCGLRG16FByteBuffered::newValueWithOffset);
        final JCGLRG16FType view = c.getElementView();
        copyRG(data, c, (r, g) -> {
          view.setR(r);
          view.setG(g);
        }, tw, th);
        break;
      }

      case TEXTURE_FORMAT_RG_32F_8BPP: {
        final JPRACursor2DType<JCGLRG32FType> c =
          JPRACursor2DByteBufferedChecked.newCursor(
            d, tw, th, JCGLRG32FByteBuffered::newValueWithOffset);
        final JCGLRG32FType view = c.getElementView();
        copyRG(data, c, (r, g) -> {
          view.setR((float) r);
          view.setG((float) g);
        }, tw, th);
        break;
      }

      case TEXTURE_FORMAT_RG_8_2BPP: {
        final JPRACursor2DType<JCGLRG8Type> c =
          JPRACursor2DByteBufferedChecked.newCursor(
            d, tw, th, JCGLRG8ByteBuffered::newValueWithOffset);
        final JCGLRG8Type view = c.getElementView();
        copyRG(data, c, (r, g) -> {
          view.setR(r);
          view.setG(g);
        }, tw, th);
        break;
      }

      case TEXTURE_FORMAT_RGB_16_6BPP: {
        final JPRACursor2DType<JCGLRGB16Type> c =
          JPRACursor2DByteBufferedChecked.newCursor(
            d, tw, th, JCGLRGB16ByteBuffered::newValueWithOffset);
        final JCGLRGB16Type view = c.getElementView();
        copyRGB(data, c, (r, g, b) -> {
          view.setR(r);
          view.setG(g);
          view.setB(b);
        }, tw, th);
        break;
      }

      case TEXTURE_FORMAT_RGB_16F_6BPP: {
        final JPRACursor2DType<JCGLRGB16FType> c =
          JPRACursor2DByteBufferedChecked.newCursor(
            d, tw, th, JCGLRGB16FByteBuffered::newValueWithOffset);
        final JCGLRGB16FType view = c.getElementView();
        copyRGB(data, c, (r, g, b) -> {
          view.setR(r);
          view.setG(g);
          view.setB(b);
        }, tw, th);
        break;
      }

      case TEXTURE_FORMAT_RGB_32F_12BPP: {
        final JPRACursor2DType<JCGLRGB32FType> c =
          JPRACursor2DByteBufferedChecked.newCursor(
            d, tw, th, JCGLRGB32FByteBuffered::newValueWithOffset);
        final JCGLRGB32FType view = c.getElementView();
        copyRGB(data, c, (r, g, b) -> {
          view.setR((float) r);
          view.setG((float) g);
          view.setB((float) b);
        }, tw, th);
        break;
      }

      case TEXTURE_FORMAT_RGB_8_3BPP: {
        final JPRACursor2DType<JCGLRGB8Type> c =
          JPRACursor2DByteBufferedChecked.newCursor(
            d, tw, th, JCGLRGB8ByteBuffered::newValueWithOffset);
        final JCGLRGB8Type view = c.getElementView();
        copyRGB(data, c, (r, g, b) -> {
          view.setR(r);
          view.setG(g);
          view.setB(b);
        }, tw, th);
        break;
      }

      case TEXTURE_FORMAT_RGBA_16_8BPP: {
        final JPRACursor2DType<JCGLRGBA16Type> c =
          JPRACursor2DByteBufferedChecked.newCursor(
            d, tw, th, JCGLRGBA16ByteBuffered::newValueWithOffset);
        final JCGLRGBA16Type view = c.getElementView();
        copyRGBA(data, c, (r, g, b, a) -> {
          view.setR(r);
          view.setG(g);
          view.setB(b);
          view.setA(a);
        }, tw, th);
        break;
      }

      case TEXTURE_FORMAT_RGBA_16F_8BPP: {
        final JPRACursor2DType<JCGLRGBA16FType> c =
          JPRACursor2DByteBufferedChecked.newCursor(
            d, tw, th, JCGLRGBA16FByteBuffered::newValueWithOffset);
        final JCGLRGBA16FType view = c.getElementView();
        copyRGBA(data, c, (r, g, b, a) -> {
          view.setR(r);
          view.setG(g);
          view.setB(b);
          view.setA(a);
        }, tw, th);
        break;
      }

      case TEXTURE_FORMAT_RGBA_32F_16BPP: {
        final JPRACursor2DType<JCGLRGBA32FType> c =
          JPRACursor2DByteBufferedChecked.newCursor(
            d, tw, th, JCGLRGBA32FByteBuffered::newValueWithOffset);
        final JCGLRGBA32FType view = c.getElementView();
        copyRGBA(data, c, (r, g, b, a) -> {
          view.setR((float) r);
          view.setG((float) g);
          view.setB((float) b);
          view.setA((float) a);
        }, tw, th);
        break;
      }

      case TEXTURE_FORMAT_RGBA_8_4BPP: {
        final JPRACursor2DType<JCGLRGBA8Type> c =
          JPRACursor2DByteBufferedChecked.newCursor(
            d, tw, th, JCGLRGBA8ByteBuffered::newValueWithOffset);
        final JCGLRGBA8Type view = c.getElementView();
        copyRGBA(data, c, (r, g, b, a) -> {
          view.setR(r);
          view.setG(g);
          view.setB(b);
          view.setA(a);
        }, tw, th);
        break;
      }

      case TEXTURE_FORMAT_R_32I_4BPP:
      case TEXTURE_FORMAT_R_32U_4BPP:
      case TEXTURE_FORMAT_R_16I_2BPP:
      case TEXTURE_FORMAT_R_16U_2BPP:
      case TEXTURE_FORMAT_R_8I_1BPP:
      case TEXTURE_FORMAT_R_8U_1BPP:
      case TEXTURE_FORMAT_RG_16I_4BPP:
      case TEXTURE_FORMAT_RG_16U_4BPP:
      case TEXTURE_FORMAT_RG_32I_8BPP:
      case TEXTURE_FORMAT_RG_32U_8BPP:
      case TEXTURE_FORMAT_RG_8I_2BPP:
      case TEXTURE_FORMAT_RG_8U_2BPP:
      case TEXTURE_FORMAT_RGB_16I_6BPP:
      case TEXTURE_FORMAT_RGB_16U_6BPP:
      case TEXTURE_FORMAT_RGB_32I_12BPP:
      case TEXTURE_FORMAT_RGB_32U_12BPP:
      case TEXTURE_FORMAT_RGB_8I_3BPP:
      case TEXTURE_FORMAT_RGB_8U_3BPP:
      case TEXTURE_FORMAT_RGBA_1010102_4BPP:
      case TEXTURE_FORMAT_RGBA_16I_8BPP:
      case TEXTURE_FORMAT_RGBA_16U_8BPP:
      case TEXTURE_FORMAT_RGBA_32I_16BPP:
      case TEXTURE_FORMAT_RGBA_32U_16BPP:
      case TEXTURE_FORMAT_RGBA_8I_4BPP:
      case TEXTURE_FORMAT_RGBA_8U_4BPP:
      case TEXTURE_FORMAT_DEPTH_24_4BPP: {
        throw new UnimplementedCodeException();
      }
    }
  }

  @Override
  public JCGLTexture2DUpdateType createTextureUpdate2D(
    final JCGLTexture2DUsableType t,
    final JCGLTLTextureDataType data)
  {
    final JCGLTexture2DUpdateType u =
      JCGLTextureUpdates.newUpdateReplacingAll2D(t);

    final int tw = (int) t.width();
    final int th = (int) t.height();
    final long dw = data.width();
    final long dh = data.height();
    RangeCheck.checkGreaterEqualLong(
      (long) tw, "Texture width", dw, "Data width");
    RangeCheck.checkGreaterEqualLong(
      (long) th, "Texture height", dh, "Data height");

    populate(
      t.format(), data, u.data(), tw, th);
    return u;
  }

  @Override
  public JCGLTextureCubeUpdateType createTextureUpdateCube(
    final JCGLTextureCubeUsableType t,
    final JCGLTLTextureDataType data)
  {
    final JCGLTextureCubeUpdateType u =
      JCGLTextureUpdates.newUpdateReplacingAllCube(t);

    final int tw = (int) t.width();
    final long dw = data.width();
    RangeCheck.checkGreaterEqualLong(
      (long) tw, "Texture size", dw, "Data size");

    populate(
      t.format(), data, u.data(), tw, tw);
    return u;
  }

  private interface Consumer1DType
  {
    void set1d(double r);
  }

  private interface Consumer2DType
  {
    void set2d(
      double r,
      double g);
  }

  private interface Consumer3DType
  {
    void set3d(
      double r,
      double g,
      double b);
  }

  private interface Consumer4DType
  {
    void set4d(
      double r,
      double g,
      double b,
      double a);
  }
}
