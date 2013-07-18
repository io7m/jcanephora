/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import javax.media.opengl.GL;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GL2ES3;

import com.io7m.jaux.UnreachableCodeException;

/**
 * Getting the correct combinations of "format", "type", and "internalformat"
 * across different versions of OpenGL is complex. This file describes the
 * mappings between {@link TextureType} values and OpenGL's insane texture API
 * formats.
 */

final class JOGL_TextureSpecs
{
  @Immutable static class TextureSpec
  {
    final int format;
    final int type;
    final int internal_format;

    TextureSpec(
      final int format,
      final int type,
      final int internal_format)
    {
      this.format = format;
      this.type = type;
      this.internal_format = internal_format;
    }
  }

  static @Nonnull TextureSpec getGL3TextureSpec(
    final @Nonnull TextureType type)
  {
    int gl_format = -1;
    int gl_type = -1;
    int gl_internalformat = -1;

    /**
     * OpenGL 3.0 doesn't give a table of the valid combinations of texture
     * formats.
     */

    switch (type) {
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      {
        gl_format = GL2ES2.GL_DEPTH_COMPONENT;
        gl_type = GL.GL_UNSIGNED_SHORT;
        gl_internalformat = GL.GL_DEPTH_COMPONENT16;
        break;
      }
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      {
        gl_format = GL2ES2.GL_DEPTH_COMPONENT;
        gl_type = GL.GL_UNSIGNED_INT;
        gl_internalformat = GL.GL_DEPTH_COMPONENT24;
        break;
      }
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      {
        gl_format = GL2ES2.GL_DEPTH_COMPONENT;
        gl_type = GL.GL_FLOAT;
        gl_internalformat = GL2ES3.GL_DEPTH_COMPONENT32F;
        break;
      }
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      {
        gl_format = GL.GL_RGBA;
        gl_type = GL.GL_UNSIGNED_SHORT_4_4_4_4;
        gl_internalformat = GL.GL_RGBA4;
        break;
      }
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      {
        gl_format = GL.GL_RGBA;
        gl_type = GL.GL_UNSIGNED_SHORT_5_5_5_1;
        gl_internalformat = GL.GL_RGB5_A1;
        break;
      }
      case TEXTURE_TYPE_RGBA_8888_4BPP:
      {
        gl_format = GL.GL_RGBA;
        gl_type = GL.GL_UNSIGNED_BYTE;
        gl_internalformat = GL.GL_RGBA8;
        break;
      }
      case TEXTURE_TYPE_RGB_565_2BPP:
      {
        gl_format = GL.GL_RGB;
        gl_type = GL.GL_UNSIGNED_SHORT_5_6_5;
        gl_internalformat = GL.GL_RGB565;
        break;
      }
      case TEXTURE_TYPE_RGB_888_3BPP:
      {
        gl_format = GL.GL_RGB;
        gl_type = GL.GL_UNSIGNED_BYTE;
        gl_internalformat = GL.GL_RGB8;
        break;
      }
      case TEXTURE_TYPE_RG_88_2BPP:
      {
        gl_format = GL2ES2.GL_RG;
        gl_type = GL.GL_UNSIGNED_BYTE;
        gl_internalformat = GL2ES2.GL_RG8;
        break;
      }
      case TEXTURE_TYPE_R_8_1BPP:
      {
        gl_format = GL2ES2.GL_RED;
        gl_type = GL.GL_UNSIGNED_BYTE;
        gl_internalformat = GL2ES2.GL_R8;
        break;
      }
    }

    assert gl_format != -1;
    assert gl_type != -1;
    assert gl_internalformat != -1;
    return new TextureSpec(gl_format, gl_type, gl_internalformat);
  }

  static @Nonnull TextureSpec getGLES2TextureSpec(
    final @Nonnull TextureType type)
  {
    int gl_format = -1;
    int gl_type = -1;
    int gl_internalformat = -1;

    /**
     * Note that the ES2 spec states that "format" must always match
     * "internalformat".
     */

    switch (type) {
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      case TEXTURE_TYPE_RG_88_2BPP:
      case TEXTURE_TYPE_R_8_1BPP:
      {
        // Not available in ES2.
        throw new UnreachableCodeException();
      }
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      {
        gl_format = GL.GL_RGBA;
        gl_type = GL.GL_UNSIGNED_SHORT_4_4_4_4;
        gl_internalformat = GL.GL_RGBA;
        break;
      }
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      {
        gl_format = GL.GL_RGBA;
        gl_type = GL.GL_UNSIGNED_SHORT_5_5_5_1;
        gl_internalformat = GL.GL_RGBA;
        break;
      }
      case TEXTURE_TYPE_RGBA_8888_4BPP:
      {
        gl_format = GL.GL_RGBA;
        gl_type = GL.GL_UNSIGNED_BYTE;
        gl_internalformat = GL.GL_RGBA;
        break;
      }
      case TEXTURE_TYPE_RGB_565_2BPP:
      {
        gl_format = GL.GL_RGB;
        gl_type = GL.GL_UNSIGNED_SHORT_5_6_5;
        gl_internalformat = GL.GL_RGB;
        break;
      }
      case TEXTURE_TYPE_RGB_888_3BPP:
      {
        gl_format = GL.GL_RGB;
        gl_type = GL.GL_UNSIGNED_BYTE;
        gl_internalformat = GL.GL_RGB;
        break;
      }
    }

    assert gl_format != -1;
    assert gl_type != -1;
    assert gl_internalformat != -1;
    assert gl_format == gl_internalformat;
    return new TextureSpec(gl_format, gl_type, gl_internalformat);
  }

  static @Nonnull TextureSpec getGLES3TextureSpec(
    final @Nonnull TextureType type)
  {
    int gl_format = -1;
    int gl_type = -1;
    int gl_internalformat = -1;

    /**
     * See the ES 3.0 spec, pages 109-110 for these mappings.
     */

    switch (type) {
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      {
        gl_format = GL2ES2.GL_DEPTH_COMPONENT;
        gl_type = GL.GL_UNSIGNED_SHORT;
        gl_internalformat = GL.GL_DEPTH_COMPONENT16;
        break;
      }
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      {
        gl_format = GL2ES2.GL_DEPTH_COMPONENT;
        gl_type = GL.GL_UNSIGNED_INT;
        gl_internalformat = GL.GL_DEPTH_COMPONENT24;
        break;
      }
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      {
        gl_format = GL2ES2.GL_DEPTH_COMPONENT;
        gl_type = GL.GL_FLOAT;
        gl_internalformat = GL2ES3.GL_DEPTH_COMPONENT32F;
        break;
      }
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      {
        gl_format = GL.GL_RGBA;
        gl_type = GL.GL_UNSIGNED_SHORT_4_4_4_4;
        gl_internalformat = GL.GL_RGBA4;
        break;
      }
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      {
        gl_format = GL.GL_RGBA;
        gl_type = GL.GL_UNSIGNED_SHORT_5_5_5_1;
        gl_internalformat = GL.GL_RGB5_A1;
        break;
      }
      case TEXTURE_TYPE_RGBA_8888_4BPP:
      {
        gl_format = GL.GL_RGBA;
        gl_type = GL.GL_UNSIGNED_BYTE;
        gl_internalformat = GL.GL_RGBA8;
        break;
      }
      case TEXTURE_TYPE_RGB_565_2BPP:
      {
        gl_format = GL.GL_RGB;
        gl_type = GL.GL_UNSIGNED_SHORT_5_6_5;
        gl_internalformat = GL.GL_RGB565;
        break;
      }
      case TEXTURE_TYPE_RGB_888_3BPP:
      {
        gl_format = GL.GL_RGB;
        gl_type = GL.GL_UNSIGNED_BYTE;
        gl_internalformat = GL.GL_RGB8;
        break;
      }
      case TEXTURE_TYPE_RG_88_2BPP:
      {
        gl_format = GL2ES2.GL_RG;
        gl_type = GL.GL_UNSIGNED_BYTE;
        gl_internalformat = GL2ES2.GL_RG8;
        break;
      }
      case TEXTURE_TYPE_R_8_1BPP:
      {
        gl_format = GL2ES2.GL_RED;
        gl_type = GL.GL_UNSIGNED_BYTE;
        gl_internalformat = GL2ES2.GL_R8;
        break;
      }
    }

    assert gl_format != -1;
    assert gl_type != -1;
    assert gl_internalformat != -1;
    return new TextureSpec(gl_format, gl_type, gl_internalformat);
  }
}
