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

import com.io7m.jaux.UnreachableCodeException;

/**
 * The six faces of a cube map with a left-handed coordinate system (the
 * default for OpenGL cube maps).
 */

public enum CubeMapFaceLH
{
  CUBE_MAP_LH_NEGATIVE_X,
  CUBE_MAP_LH_NEGATIVE_Y,
  CUBE_MAP_LH_NEGATIVE_Z,
  CUBE_MAP_LH_POSITIVE_X,
  CUBE_MAP_LH_POSITIVE_Y,
  CUBE_MAP_LH_POSITIVE_Z;

  /**
   * For the cube map face <code>face</code> in a coordinate system consistent
   * with OpenGL's "world" coordinates, return the corresponding face in
   * OpenGL's left handed cube map coordinate system.
   */

  public static @Nonnull CubeMapFaceLH fromRH(
    final @Nonnull CubeMapFaceRH face)
  {
    switch (face) {
      case CUBE_MAP_RH_NEGATIVE_X:
        return CUBE_MAP_LH_NEGATIVE_X;
      case CUBE_MAP_RH_POSITIVE_X:
        return CUBE_MAP_LH_POSITIVE_X;

      case CUBE_MAP_RH_NEGATIVE_Y:
        return CUBE_MAP_LH_POSITIVE_Y;
      case CUBE_MAP_RH_POSITIVE_Y:
        return CUBE_MAP_LH_NEGATIVE_Y;

      case CUBE_MAP_RH_NEGATIVE_Z:
        return CUBE_MAP_LH_POSITIVE_Z;
      case CUBE_MAP_RH_POSITIVE_Z:
        return CUBE_MAP_LH_NEGATIVE_Z;
    }

    throw new UnreachableCodeException();
  }

  public int order()
  {
    switch (this) {
      case CUBE_MAP_LH_POSITIVE_X:
        return 0;
      case CUBE_MAP_LH_NEGATIVE_X:
        return 1;
      case CUBE_MAP_LH_POSITIVE_Y:
        return 2;
      case CUBE_MAP_LH_NEGATIVE_Y:
        return 3;
      case CUBE_MAP_LH_POSITIVE_Z:
        return 4;
      case CUBE_MAP_LH_NEGATIVE_Z:
        return 5;
    }

    throw new UnreachableCodeException();
  }
}
