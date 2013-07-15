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

import com.io7m.jaux.UnreachableCodeException;

/**
 * The six faces of a cube map.
 */

public enum CubeMapFace
{
  CUBE_MAP_POSITIVE_X,
  CUBE_MAP_NEGATIVE_X,
  CUBE_MAP_POSITIVE_Y,
  CUBE_MAP_NEGATIVE_Y,
  CUBE_MAP_POSITIVE_Z,
  CUBE_MAP_NEGATIVE_Z;

  public int order()
  {
    switch (this) {
      case CUBE_MAP_POSITIVE_X:
        return 0;
      case CUBE_MAP_NEGATIVE_X:
        return 1;
      case CUBE_MAP_POSITIVE_Y:
        return 2;
      case CUBE_MAP_NEGATIVE_Y:
        return 3;
      case CUBE_MAP_POSITIVE_Z:
        return 4;
      case CUBE_MAP_NEGATIVE_Z:
        return 5;
    }

    throw new UnreachableCodeException();
  }
}
