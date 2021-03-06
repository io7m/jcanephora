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

package com.io7m.jcanephora.jogl;

import com.io7m.jaffirm.core.Preconditions;
import com.io7m.jcanephora.core.JCGLPixelFormat;
import com.io7m.jcanephora.core.JCGLTextureFormat;
import com.io7m.jnull.NullCheck;
import com.io7m.junreachable.UnreachableCodeException;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2ES2;
import com.jogamp.opengl.GL2ES3;
import com.jogamp.opengl.GL2GL3;

import java.util.EnumMap;
import java.util.Map;

final class JOGLTextureSpecs
{
  private static final Map<JCGLTextureFormat, JOGLTextureSpec> SPECS;

  static {
    SPECS = makeTextureSpecs();
  }

  private JOGLTextureSpecs()
  {
    throw new UnreachableCodeException();
  }

  private static Map<JCGLTextureFormat, JOGLTextureSpec> makeTextureSpecs()
  {
    final Map<JCGLTextureFormat, JOGLTextureSpec> m =
      new EnumMap<>(JCGLTextureFormat.class);

    final JCGLTextureFormat[] values = JCGLTextureFormat.values();
    for (int index = 0; index < values.length; ++index) {
      final JCGLTextureFormat format = values[index];
      final JOGLTextureSpec spec = makeTextureSpec(format);

      Preconditions.checkPrecondition(
        format,
        !m.containsKey(format),
        ignored -> "Format must be unique");

      m.put(format, spec);
    }

    return m;
  }

  private static JOGLTextureSpec makeTextureSpec(
    final JCGLTextureFormat format)
  {
    JCGLPixelFormat ct = format.getComponentType();
    switch (ct) {
      case PIXEL_PACKED_UNSIGNED_INT_1010102:

        /*
         * 1010102 has to be re-mapped to unsigned bytes on GL3.
         */

        ct = JCGLPixelFormat.PIXEL_COMPONENT_UNSIGNED_BYTE;
        break;
      case PIXEL_COMPONENT_BYTE:
      case PIXEL_COMPONENT_FLOAT:
      case PIXEL_COMPONENT_INT:
      case PIXEL_COMPONENT_SHORT:
      case PIXEL_COMPONENT_UNSIGNED_BYTE:
      case PIXEL_COMPONENT_UNSIGNED_INT:
      case PIXEL_COMPONENT_UNSIGNED_SHORT:
      case PIXEL_PACKED_UNSIGNED_SHORT_4444:
      case PIXEL_PACKED_UNSIGNED_SHORT_5551:
      case PIXEL_PACKED_UNSIGNED_SHORT_565:
      case PIXEL_COMPONENT_HALF_FLOAT:
      case PIXEL_PACKED_UNSIGNED_INT_24_8:
        break;
    }

    final int gl_type = JOGLTypeConversions.pixelTypeToGL(ct);
    int gl_format = -1;
    int gl_internalformat = -1;

    /*
     * OpenGL 3.0 doesn't give a table of the valid combinations of texture
     * formats.
     */

    switch (format) {
      case TEXTURE_FORMAT_DEPTH_16_2BPP: {
        gl_format = GL2ES2.GL_DEPTH_COMPONENT;
        gl_internalformat = GL.GL_DEPTH_COMPONENT16;
        break;
      }
      case TEXTURE_FORMAT_DEPTH_24_4BPP: {
        gl_format = GL2ES2.GL_DEPTH_COMPONENT;
        gl_internalformat = GL.GL_DEPTH_COMPONENT24;
        break;
      }
      case TEXTURE_FORMAT_DEPTH_32F_4BPP: {
        gl_format = GL2ES2.GL_DEPTH_COMPONENT;
        gl_internalformat = GL2ES3.GL_DEPTH_COMPONENT32F;
        break;
      }
      //      case TEXTURE_FORMAT_RGBA_4444_2BPP: {
      //        gl_format = GL2ES2.GL_RGBA;
      //        gl_internalformat = GLES2.GL_RGBA4;
      //        break;
      //      }
      //      case TEXTURE_FORMAT_RGBA_5551_2BPP: {
      //        gl_format = GL2ES2.GL_RGBA;
      //        gl_internalformat = GLES2.GL_RGB5_A1;
      //        break;
      //      }
      //      case TEXTURE_FORMAT_RGB_565_2BPP: {
      //        gl_format = GL2ES2.GL_RGBA;
      //        gl_internalformat = GLES2.GL_RGB565;
      //        break;
      //      }
      case TEXTURE_FORMAT_RGBA_8_4BPP: {
        gl_format = GL.GL_RGBA;
        gl_internalformat = GL.GL_RGBA8;
        break;
      }
      case TEXTURE_FORMAT_RGB_8_3BPP: {
        gl_format = GL.GL_RGB;
        gl_internalformat = GL.GL_RGB8;
        break;
      }
      case TEXTURE_FORMAT_RG_8_2BPP: {
        gl_format = GL2ES2.GL_RG;
        gl_internalformat = GL2ES2.GL_RG8;
        break;
      }
      case TEXTURE_FORMAT_R_8_1BPP: {
        gl_format = GL2ES2.GL_RED;
        gl_internalformat = GL2ES2.GL_R8;
        break;
      }
      case TEXTURE_FORMAT_RGBA_32F_16BPP: {
        gl_format = GL.GL_RGBA;
        gl_internalformat = GL.GL_RGBA32F;
        break;
      }
      case TEXTURE_FORMAT_RGBA_1010102_4BPP: {
        gl_format = GL.GL_RGBA;
        gl_internalformat = GL.GL_RGB10_A2;
        break;
      }
      case TEXTURE_FORMAT_RGBA_16F_8BPP: {
        gl_format = GL.GL_RGBA;
        gl_internalformat = GL.GL_RGBA16F;
        break;
      }
      case TEXTURE_FORMAT_RGBA_16_8BPP: {
        gl_format = GL.GL_RGBA;
        gl_internalformat = GL2GL3.GL_RGBA16;
        break;
      }
      case TEXTURE_FORMAT_RGBA_32I_16BPP: {
        gl_format = GL2ES3.GL_RGBA_INTEGER;
        gl_internalformat = GL2ES3.GL_RGBA32I;
        break;
      }
      case TEXTURE_FORMAT_RGBA_16I_8BPP: {
        gl_format = GL2ES3.GL_RGBA_INTEGER;
        gl_internalformat = GL2ES3.GL_RGBA16I;
        break;
      }
      case TEXTURE_FORMAT_RGBA_16U_8BPP: {
        gl_format = GL2ES3.GL_RGBA_INTEGER;
        gl_internalformat = GL2ES3.GL_RGBA16UI;
        break;
      }
      case TEXTURE_FORMAT_RGBA_32U_16BPP: {
        gl_format = GL2ES3.GL_RGBA_INTEGER;
        gl_internalformat = GL2ES3.GL_RGBA32UI;
        break;
      }
      case TEXTURE_FORMAT_RGBA_8I_4BPP: {
        gl_format = GL2ES3.GL_RGBA_INTEGER;
        gl_internalformat = GL2ES3.GL_RGBA8I;
        break;
      }
      case TEXTURE_FORMAT_RGBA_8U_4BPP: {
        gl_format = GL2ES3.GL_RGBA_INTEGER;
        gl_internalformat = GL2ES3.GL_RGBA8UI;
        break;
      }
      case TEXTURE_FORMAT_RGB_16F_6BPP: {
        gl_format = GL.GL_RGB;
        gl_internalformat = GL.GL_RGB16F;
        break;
      }
      case TEXTURE_FORMAT_RGB_16I_6BPP: {
        gl_format = GL2ES3.GL_RGB_INTEGER;
        gl_internalformat = GL2ES3.GL_RGB16I;
        break;
      }
      case TEXTURE_FORMAT_RGB_16U_6BPP: {
        gl_format = GL2ES3.GL_RGB_INTEGER;
        gl_internalformat = GL2ES3.GL_RGB16UI;
        break;
      }
      case TEXTURE_FORMAT_RGB_16_6BPP: {
        gl_format = GL.GL_RGB;
        gl_internalformat = GL2GL3.GL_RGB16;
        break;
      }
      case TEXTURE_FORMAT_RGB_32F_12BPP: {
        gl_format = GL.GL_RGB;
        gl_internalformat = GL.GL_RGB32F;
        break;
      }
      case TEXTURE_FORMAT_RGB_32I_12BPP: {
        gl_format = GL2ES3.GL_RGB_INTEGER;
        gl_internalformat = GL2ES3.GL_RGB32I;
        break;
      }
      case TEXTURE_FORMAT_RGB_32U_12BPP: {
        gl_format = GL2ES3.GL_RGB_INTEGER;
        gl_internalformat = GL2ES3.GL_RGB32UI;
        break;
      }
      case TEXTURE_FORMAT_RGB_8I_3BPP: {
        gl_format = GL2ES3.GL_RGB_INTEGER;
        gl_internalformat = GL2ES3.GL_RGB8I;
        break;
      }
      case TEXTURE_FORMAT_RGB_8U_3BPP: {
        gl_format = GL2ES3.GL_RGB_INTEGER;
        gl_internalformat = GL2ES3.GL_RGB8UI;
        break;
      }
      case TEXTURE_FORMAT_RG_16F_4BPP: {
        gl_format = GL2ES2.GL_RG;
        gl_internalformat = GL2ES2.GL_RG16F;
        break;
      }
      case TEXTURE_FORMAT_RG_16I_4BPP: {
        gl_format = GL2ES3.GL_RG_INTEGER;
        gl_internalformat = GL2ES3.GL_RG16I;
        break;
      }
      case TEXTURE_FORMAT_RG_16U_4BPP: {
        gl_format = GL2ES3.GL_RG_INTEGER;
        gl_internalformat = GL2ES3.GL_RG16UI;
        break;
      }
      case TEXTURE_FORMAT_RG_16_4BPP: {
        gl_format = GL2ES2.GL_RG;
        gl_internalformat = GL2GL3.GL_RG16;
        break;
      }
      case TEXTURE_FORMAT_RG_32F_8BPP: {
        gl_format = GL2ES2.GL_RG;
        gl_internalformat = GL2ES2.GL_RG32F;
        break;
      }
      case TEXTURE_FORMAT_RG_32I_8BPP: {
        gl_format = GL2ES3.GL_RG_INTEGER;
        gl_internalformat = GL2ES3.GL_RG32I;
        break;
      }
      case TEXTURE_FORMAT_RG_32U_8BPP: {
        gl_format = GL2ES3.GL_RG_INTEGER;
        gl_internalformat = GL2ES3.GL_RG32UI;
        break;
      }
      case TEXTURE_FORMAT_RG_8I_2BPP: {
        gl_format = GL2ES3.GL_RG_INTEGER;
        gl_internalformat = GL2ES3.GL_RG8I;
        break;
      }
      case TEXTURE_FORMAT_RG_8U_2BPP: {
        gl_format = GL2ES3.GL_RG_INTEGER;
        gl_internalformat = GL2ES3.GL_RG8UI;
        break;
      }
      case TEXTURE_FORMAT_R_16F_2BPP: {
        gl_format = GL2ES2.GL_RED;
        gl_internalformat = GL2ES2.GL_R16F;
        break;
      }
      case TEXTURE_FORMAT_R_16I_2BPP: {
        gl_format = GL2ES3.GL_RED_INTEGER;
        gl_internalformat = GL2ES3.GL_R16I;
        break;
      }
      case TEXTURE_FORMAT_R_16U_2BPP: {
        gl_format = GL2ES3.GL_RED_INTEGER;
        gl_internalformat = GL2ES3.GL_R16UI;
        break;
      }
      case TEXTURE_FORMAT_R_16_2BPP: {
        gl_format = GL2ES2.GL_RED;
        gl_internalformat = GL2GL3.GL_R16;
        break;
      }
      case TEXTURE_FORMAT_R_32F_4BPP: {
        gl_format = GL2ES2.GL_RED;
        gl_internalformat = GL2ES2.GL_R32F;
        break;
      }
      case TEXTURE_FORMAT_R_32I_4BPP: {
        gl_format = GL2ES3.GL_RED_INTEGER;
        gl_internalformat = GL2ES3.GL_R32I;
        break;
      }
      case TEXTURE_FORMAT_R_32U_4BPP: {
        gl_format = GL2ES3.GL_RED_INTEGER;
        gl_internalformat = GL2ES3.GL_R32UI;
        break;
      }
      case TEXTURE_FORMAT_R_8I_1BPP: {
        gl_format = GL2ES3.GL_RED_INTEGER;
        gl_internalformat = GL2ES3.GL_R8I;
        break;
      }
      case TEXTURE_FORMAT_R_8U_1BPP: {
        gl_format = GL2ES3.GL_RED_INTEGER;
        gl_internalformat = GL2ES3.GL_R8UI;
        break;
      }
      case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP: {
        gl_format = GL.GL_DEPTH_STENCIL;
        gl_internalformat = GL.GL_DEPTH24_STENCIL8;
        break;
      }
    }

    assert gl_format != -1;
    assert gl_internalformat != -1;
    return new JOGLTextureSpec(gl_format, gl_type, gl_internalformat);
  }

  public static JOGLTextureSpec getTextureSpec(
    final JCGLTextureFormat format)
  {
    NullCheck.notNull(format, "Format");

    Preconditions.checkPrecondition(
      format,
      SPECS.containsKey(format),
      ignored -> "Format specification must be known");

    return SPECS.get(format);
  }
}
