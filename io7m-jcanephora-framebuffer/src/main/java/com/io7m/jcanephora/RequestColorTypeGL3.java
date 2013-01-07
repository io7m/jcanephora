/*
 * Copyright © 2012 http://io7m.com
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

import com.io7m.jaux.UnreachableCodeException;

/**
 * The types of color buffers that can be explicitly requested in a strictly
 * GL3-compatible program.
 */

public enum RequestColorTypeGL3
{
  /**
   * @see TextureType#TEXTURE_TYPE_RGBA_8888_4BPP
   */

  REQUEST_GL3_COLOR_RGBA8888,

  /**
   * @see TextureType#TEXTURE_TYPE_RGB_888_3BPP
   */

  REQUEST_GL3_COLOR_RGB888,

  /**
   * @see TextureType#TEXTURE_TYPE_RGBA_4444_2BPP
   */

  REQUEST_GL3_COLOR_RGBA4444,

  /**
   * @see TextureType#TEXTURE_TYPE_RGBA_5551_2BPP
   */

  REQUEST_GL3_COLOR_RGBA5551,

  /**
   * @see TextureType#TEXTURE_TYPE_RGB_565_2BPP
   */

  REQUEST_GL3_COLOR_RGB565;

  /**
   * Retrieve the equivalent renderbuffer type for the given request.
   */

  public @Nonnull RenderbufferType equivalentRenderbufferType()
  {
    switch (this) {
      case REQUEST_GL3_COLOR_RGB565:
        return RenderbufferType.RENDERBUFFER_COLOR_RGB_565;
      case REQUEST_GL3_COLOR_RGBA4444:
        return RenderbufferType.RENDERBUFFER_COLOR_RGBA_4444;
      case REQUEST_GL3_COLOR_RGBA5551:
        return RenderbufferType.RENDERBUFFER_COLOR_RGBA_5551;
      case REQUEST_GL3_COLOR_RGB888:
        return RenderbufferType.RENDERBUFFER_COLOR_RGB_888;
      case REQUEST_GL3_COLOR_RGBA8888:
        return RenderbufferType.RENDERBUFFER_COLOR_RGBA_8888;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Retrieve the equivalent texture type for the given request.
   */

  public @Nonnull TextureType equivalentTextureType()
  {
    switch (this) {
      case REQUEST_GL3_COLOR_RGB565:
        return TextureType.TEXTURE_TYPE_RGB_565_2BPP;
      case REQUEST_GL3_COLOR_RGBA4444:
        return TextureType.TEXTURE_TYPE_RGBA_4444_2BPP;
      case REQUEST_GL3_COLOR_RGBA5551:
        return TextureType.TEXTURE_TYPE_RGBA_5551_2BPP;
      case REQUEST_GL3_COLOR_RGB888:
        return TextureType.TEXTURE_TYPE_RGB_888_3BPP;
      case REQUEST_GL3_COLOR_RGBA8888:
        return TextureType.TEXTURE_TYPE_RGBA_8888_4BPP;
    }

    throw new UnreachableCodeException();
  }
}
