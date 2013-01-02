package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.UnreachableCodeException;

/**
 * The types of color buffers that can be explicitly requested in a strictly
 * ES2-compatible program.
 */

public enum RequestColorTypeES2
{
  /**
   * @see TextureType#TEXTURE_TYPE_RGBA_4444_2BPP
   */

  REQUEST_ES2_COLOR_RGBA4444,

  /**
   * @see TextureType#TEXTURE_TYPE_RGBA_5551_2BPP
   */

  REQUEST_ES2_COLOR_RGBA5551,

  /**
   * @see TextureType#TEXTURE_TYPE_RGB_565_2BPP
   */

  REQUEST_ES2_COLOR_RGB565;

  /**
   * Retrieve the equivalent renderbuffer type for the given request.
   */

  public @Nonnull RenderbufferType equivalentRenderbufferType()
  {
    switch (this) {
      case REQUEST_ES2_COLOR_RGB565:
        return RenderbufferType.RENDERBUFFER_COLOR_RGB_565;
      case REQUEST_ES2_COLOR_RGBA4444:
        return RenderbufferType.RENDERBUFFER_COLOR_RGBA_4444;
      case REQUEST_ES2_COLOR_RGBA5551:
        return RenderbufferType.RENDERBUFFER_COLOR_RGBA_5551;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Retrieve the equivalent texture type for the given request.
   */

  public @Nonnull TextureType equivalentTextureType()
  {
    switch (this) {
      case REQUEST_ES2_COLOR_RGB565:
        return TextureType.TEXTURE_TYPE_RGB_565_2BPP;
      case REQUEST_ES2_COLOR_RGBA4444:
        return TextureType.TEXTURE_TYPE_RGBA_4444_2BPP;
      case REQUEST_ES2_COLOR_RGBA5551:
        return TextureType.TEXTURE_TYPE_RGBA_5551_2BPP;
    }

    throw new UnreachableCodeException();
  }
}
