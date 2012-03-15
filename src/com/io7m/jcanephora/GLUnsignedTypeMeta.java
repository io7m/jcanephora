package com.io7m.jcanephora;

/**
 * Information about unsigned types.
 */

public final class GLUnsignedTypeMeta
{
  public static int getSizeBytes(
    final GLUnsignedType type)
  {
    switch (type) {
      case TYPE_UNSIGNED_BYTE:
        return 1;
      case TYPE_UNSIGNED_INT:
        return 4;
      case TYPE_UNSIGNED_SHORT:
        return 2;
    }

    throw new AssertionError();
  }
}
