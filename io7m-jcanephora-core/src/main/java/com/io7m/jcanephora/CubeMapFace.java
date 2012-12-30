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
