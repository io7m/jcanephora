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

package com.io7m.jcanephora;

import com.io7m.jnull.NullCheck;
import com.io7m.junreachable.UnreachableCodeException;

/**
 * The six faces of a cube map with a left-handed coordinate system (the
 * default for OpenGL cube maps).
 */

public enum CubeMapFaceLH
{
  /**
   * The negative X face of a left-handed cube map.
   */

  CUBE_MAP_LH_NEGATIVE_X,

  /**
   * The negative Y face of a left-handed cube map.
   */

  CUBE_MAP_LH_NEGATIVE_Y,

  /**
   * The negative Z face of a left-handed cube map.
   */

  CUBE_MAP_LH_NEGATIVE_Z,

  /**
   * The positive X face of a left-handed cube map.
   */

  CUBE_MAP_LH_POSITIVE_X,

  /**
   * The positive Y face of a left-handed cube map.
   */

  CUBE_MAP_LH_POSITIVE_Y,

  /**
   * The positive Z face of a left-handed cube map.
   */

  CUBE_MAP_LH_POSITIVE_Z;

  /**
   * For the cube map face <code>face</code> in a coordinate system consistent
   * with OpenGL's "world" coordinates, return the corresponding face in
   * OpenGL's left handed cube map coordinate system.
   * 
   * @return The corresponding left-handed face.
   * @param face
   *          The right-handed face.
   */

  public static CubeMapFaceLH fromRH(
    final CubeMapFaceRH face)
  {
    NullCheck.notNull(face, "Face");

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

  /**
   * @return The ordering relation of faces.
   */

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
