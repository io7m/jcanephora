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
 * Functions providing information about texture types.
 */

public final class TextureTypeMeta
{
  public static int bytesPerPixel(
    final @Nonnull TextureType type)
  {
    switch (type) {
      case TEXTURE_TYPE_R_8_1BPP:
        return 1;
      case TEXTURE_TYPE_RG_88_2BPP:
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGB_565_2BPP:
        return 2;
      case TEXTURE_TYPE_RGB_888_3BPP:
        return 3;
      case TEXTURE_TYPE_RGBA_8888_4BPP:
        return 4;
    }

    throw new UnreachableCodeException();
  }

  public static int components(
    final @Nonnull TextureType type)
  {
    switch (type) {
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGBA_8888_4BPP:
        return 4;
      case TEXTURE_TYPE_RGB_565_2BPP:
      case TEXTURE_TYPE_RGB_888_3BPP:
        return 3;
      case TEXTURE_TYPE_RG_88_2BPP:
        return 2;
      case TEXTURE_TYPE_R_8_1BPP:
        return 1;
    }

    throw new UnreachableCodeException();
  }

  private TextureTypeMeta()
  {
    throw new UnreachableCodeException();
  }
}
