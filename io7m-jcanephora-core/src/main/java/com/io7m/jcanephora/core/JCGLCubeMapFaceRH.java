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

package com.io7m.jcanephora.core;

import com.io7m.jnull.NullCheck;
import com.io7m.junreachable.UnreachableCodeException;

/**
 * The six faces of a cube map with a right-handed coordinate system (see the
 * documentation for details on how to use a right-handed coordinate system).
 */

public enum JCGLCubeMapFaceRH
{
  /**
   * The negative X face of a right-handed cube map.
   */

  CUBE_MAP_RH_NEGATIVE_X,

  /**
   * The negative Y face of a right-handed cube map.
   */

  CUBE_MAP_RH_NEGATIVE_Y,

  /**
   * The negative Z face of a right-handed cube map.
   */

  CUBE_MAP_RH_NEGATIVE_Z,

  /**
   * The positive X face of a right-handed cube map.
   */

  CUBE_MAP_RH_POSITIVE_X,

  /**
   * The positive Y face of a right-handed cube map.
   */

  CUBE_MAP_RH_POSITIVE_Y,

  /**
   * The positive Z face of a right-handed cube map.
   */

  CUBE_MAP_RH_POSITIVE_Z;

  /**
   * For the cube map face {@code face} in OpenGL's left handed cube map
   * coordinate system, return the corresponding face in a right-handed
   * coordinate system consistent with that of OpenGL's "world" coordinates.
   *
   * @param face The left-handed face.
   *
   * @return The corresponding right-handed face.
   */

  public static JCGLCubeMapFaceRH fromLH(
    final JCGLCubeMapFaceLH face)
  {
    NullCheck.notNull(face, "Face");

    switch (face) {
      case CUBE_MAP_LH_NEGATIVE_X:
        return JCGLCubeMapFaceRH.CUBE_MAP_RH_NEGATIVE_X;
      case CUBE_MAP_LH_POSITIVE_X:
        return JCGLCubeMapFaceRH.CUBE_MAP_RH_POSITIVE_X;

      case CUBE_MAP_LH_NEGATIVE_Y:
        return JCGLCubeMapFaceRH.CUBE_MAP_RH_POSITIVE_Y;
      case CUBE_MAP_LH_POSITIVE_Y:
        return JCGLCubeMapFaceRH.CUBE_MAP_RH_NEGATIVE_Y;

      case CUBE_MAP_LH_NEGATIVE_Z:
        return JCGLCubeMapFaceRH.CUBE_MAP_RH_POSITIVE_Z;
      case CUBE_MAP_LH_POSITIVE_Z:
        return JCGLCubeMapFaceRH.CUBE_MAP_RH_NEGATIVE_Z;
    }

    throw new UnreachableCodeException();
  }
}
