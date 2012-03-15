package com.io7m.jcanephora;

import javax.annotation.Nonnull;

/**
 * Information about scalar OpenGL types.
 */

public final class GLScalarTypeMeta
{
  /**
   * Return the size in bytes of the given type.
   */

  public static int getSizeBytes(
    final @Nonnull GLScalarType type)
  {
    switch (type) {
      case TYPE_BYTE:
        return 1;
      case TYPE_DOUBLE:
        return 8;
      case TYPE_FLOAT:
        return 4;
      case TYPE_INT:
        return 4;
      case TYPE_SHORT:
        return 2;
      case TYPE_UNSIGNED_BYTE:
        return 1;
      case TYPE_UNSIGNED_INT:
        return 4;
      case TYPE_UNSIGNED_SHORT:
        return 2;
    }

    /* UNREACHABLE */
    throw new AssertionError("unreachable code: report this bug!");
  }
}
