/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.tests.contracts.gl3;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;

import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.TextureCubeStaticType;
import com.io7m.jcanephora.TextureCubeStaticUpdate;
import com.io7m.jcanephora.TextureCubeStaticUpdateType;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureFormat;
import com.io7m.jcanephora.TextureFormatMeta;
import com.io7m.jcanephora.TextureWrapR;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.api.JCGLTexturesCubeStaticGL3Type;
import com.io7m.jcanephora.tests.contracts.TextureCubeStaticUpdateContract;

@SuppressWarnings("null") public abstract class TextureCubeStaticUpdateGL3Contract extends
  TextureCubeStaticUpdateContract<JCGLTexturesCubeStaticGL3Type>
{
  @Override public TextureFormat getSafeDefaultFormat()
  {
    return TextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP;
  }

  @Override public Set<TextureFormat> getSupportedTexturesWithComponents(
    final int count)
  {
    final EnumSet<TextureFormat> s =
      EnumSet.copyOf(TextureFormatMeta.getTexturesCubeRequiredByGL3());
    final Iterator<TextureFormat> i = s.iterator();
    while (i.hasNext()) {
      final TextureFormat t = i.next();
      if (t.getComponentCount() != count) {
        i.remove();
      }
    }
    return s;
  }

  @Override public
    Set<TextureFormat>
    getSupportedTexturesWithNotEqualComponents(
      final int count)
  {
    final EnumSet<TextureFormat> s =
      EnumSet.copyOf(TextureFormatMeta.getTexturesCubeRequiredByGL3());
    final Iterator<TextureFormat> i = s.iterator();
    while (i.hasNext()) {
      final TextureFormat t = i.next();
      if (t.getComponentCount() == count) {
        i.remove();
      }
    }
    return s;
  }

  @Override public TextureCubeStaticType getTexture(
    final JCGLTexturesCubeStaticGL3Type g,
    final TextureFormat t)
  {
    try {
      switch (t) {
        case TEXTURE_FORMAT_RGBA_1010102_4BPP:
        {
          return g.textureCubeStaticAllocateRGBA1010102(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
        }
        case TEXTURE_FORMAT_RGB_565_2BPP:
        case TEXTURE_FORMAT_RGBA_4444_2BPP:
        case TEXTURE_FORMAT_RGBA_5551_2BPP:
        {
          throw new UnreachableCodeException(new AssertionError(t.toString()));
        }

        case TEXTURE_FORMAT_RGBA_8U_4BPP:
        {
          return g.textureCubeStaticAllocateRGBA8U(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
        }
        case TEXTURE_FORMAT_RGBA_8I_4BPP:
        {
          return g.textureCubeStaticAllocateRGBA8I(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

        }
        case TEXTURE_FORMAT_RGBA_16U_8BPP:
        {
          return g.textureCubeStaticAllocateRGBA16U(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

        }
        case TEXTURE_FORMAT_RGBA_16I_8BPP:
        {
          return g.textureCubeStaticAllocateRGBA16I(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

        }
        case TEXTURE_FORMAT_RGBA_16_8BPP:
        {
          return g.textureCubeStaticAllocateRGBA16(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
        }
        case TEXTURE_FORMAT_RGBA_16F_8BPP:
        {
          return g.textureCubeStaticAllocateRGBA16f(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

        }
        case TEXTURE_FORMAT_RGBA_32U_16BPP:
        {
          return g.textureCubeStaticAllocateRGBA32U(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

        }
        case TEXTURE_FORMAT_RGBA_32I_16BPP:
        {
          return g.textureCubeStaticAllocateRGBA32I(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

        }

        case TEXTURE_FORMAT_RGB_8_3BPP:
        {
          return g.textureCubeStaticAllocateRGB8(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

        }
        case TEXTURE_FORMAT_RGB_8U_3BPP:
        {
          return g.textureCubeStaticAllocateRGB8U(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

        }
        case TEXTURE_FORMAT_RGB_8I_3BPP:
        {
          return g.textureCubeStaticAllocateRGB8I(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

        }
        case TEXTURE_FORMAT_RGB_16U_6BPP:
        {
          return g.textureCubeStaticAllocateRGB16U(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

        }
        case TEXTURE_FORMAT_RGB_16I_6BPP:
        {
          return g.textureCubeStaticAllocateRGB16I(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

        }
        case TEXTURE_FORMAT_RGB_16_6BPP:
        {
          return g.textureCubeStaticAllocateRGB16(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
        }
        case TEXTURE_FORMAT_RGB_16F_6BPP:
        {
          return g.textureCubeStaticAllocateRGB16f(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

        }
        case TEXTURE_FORMAT_RGB_32U_12BPP:
        {
          return g.textureCubeStaticAllocateRGB32U(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

        }
        case TEXTURE_FORMAT_RGB_32I_12BPP:
        {
          return g.textureCubeStaticAllocateRGB32I(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

        }
        case TEXTURE_FORMAT_RGB_32F_12BPP:
        {
          return g.textureCubeStaticAllocateRGB32f(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
        }
        case TEXTURE_FORMAT_RG_8_2BPP:
        {
          return g.textureCubeStaticAllocateRG8(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

        }
        case TEXTURE_FORMAT_RG_8U_2BPP:
        {
          return g.textureCubeStaticAllocateRG8U(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

        }
        case TEXTURE_FORMAT_RG_8I_2BPP:
        {
          return g.textureCubeStaticAllocateRG8I(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

        }
        case TEXTURE_FORMAT_RG_16U_4BPP:
        {
          return g.textureCubeStaticAllocateRG16U(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

        }
        case TEXTURE_FORMAT_RG_16I_4BPP:
        {
          return g.textureCubeStaticAllocateRG16I(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
        }
        case TEXTURE_FORMAT_RG_16_4BPP:
        {
          return g.textureCubeStaticAllocateRG16(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
        }
        case TEXTURE_FORMAT_RG_16F_4BPP:
        {
          return g.textureCubeStaticAllocateRG16f(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

        }
        case TEXTURE_FORMAT_RG_32U_8BPP:
        {
          return g.textureCubeStaticAllocateRG32U(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

        }
        case TEXTURE_FORMAT_RG_32I_8BPP:
        {
          return g.textureCubeStaticAllocateRG32I(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

        }
        case TEXTURE_FORMAT_RG_32F_8BPP:
        {
          return g.textureCubeStaticAllocateRG32f(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

        }

        case TEXTURE_FORMAT_R_8_1BPP:
        {
          return g.textureCubeStaticAllocateR8(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

        }
        case TEXTURE_FORMAT_R_8U_1BPP:
        {
          return g.textureCubeStaticAllocateR8U(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

        }
        case TEXTURE_FORMAT_R_8I_1BPP:
        {
          return g.textureCubeStaticAllocateR8I(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

        }
        case TEXTURE_FORMAT_R_16U_2BPP:
        {
          return g.textureCubeStaticAllocateR16U(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

        }
        case TEXTURE_FORMAT_R_16I_2BPP:
        {
          return g.textureCubeStaticAllocateR16I(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
        }
        case TEXTURE_FORMAT_R_16_2BPP:
        {
          return g.textureCubeStaticAllocateR16(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
        }
        case TEXTURE_FORMAT_R_16F_2BPP:
        {
          return g.textureCubeStaticAllocateR16f(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

        }
        case TEXTURE_FORMAT_R_32U_4BPP:
        {
          return g.textureCubeStaticAllocateR32U(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

        }
        case TEXTURE_FORMAT_R_32I_4BPP:
        {
          return g.textureCubeStaticAllocateR32I(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

        }
        case TEXTURE_FORMAT_R_32F_4BPP:
        {
          return g.textureCubeStaticAllocateR32f(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

        }

        case TEXTURE_FORMAT_RGBA_32F_16BPP:
        {
          return g.textureCubeStaticAllocateRGBA32f(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

        }
        case TEXTURE_FORMAT_RGBA_8_4BPP:
        {
          return g.textureCubeStaticAllocateRGBA8(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

        }
        case TEXTURE_FORMAT_DEPTH_16_2BPP:
        {
          return g.textureCubeStaticAllocateDepth16(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
        }
        case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
        {
          return g.textureCubeStaticAllocateDepth24Stencil8(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
        }
        case TEXTURE_FORMAT_DEPTH_24_4BPP:
        {
          return g.textureCubeStaticAllocateDepth24(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
        }
        case TEXTURE_FORMAT_DEPTH_32F_4BPP:
        {
          return g.textureCubeStaticAllocateDepth32f(
            t.toString(),
            128,
            TextureWrapR.TEXTURE_WRAP_REPEAT,
            TextureWrapS.TEXTURE_WRAP_REPEAT,
            TextureWrapT.TEXTURE_WRAP_REPEAT,
            TextureFilterMinification.TEXTURE_FILTER_NEAREST,
            TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
        }
      }
    } catch (final JCGLExceptionRuntime e) {
      throw new UnreachableCodeException(e);
    } catch (final JCGLException e) {
      throw new UnreachableCodeException(e);
    }

    throw new UnreachableCodeException();
  }

  @Override public TextureCubeStaticUpdateType getTextureUpdate(
    final TextureCubeStaticType t)
  {
    return TextureCubeStaticUpdate.newReplacingAll(t);
  }
}
