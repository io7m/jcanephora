package com.io7m.jcanephora;

import java.nio.ByteBuffer;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;

/**
 * Generic byte buffer cursor pointing to indices of a given OpenGL unsigned
 * type.
 */

final class ByteBufferCursorWritableIndex extends BufferCursor implements
  CursorWritableIndex
{
  private final @Nonnull ByteBuffer     target_data;
  private final @Nonnull GLUnsignedType type;

  ByteBufferCursorWritableIndex(
    final @Nonnull ByteBuffer target_data,
    final long element_first,
    final long element_last,
    final @Nonnull GLUnsignedType type)
  {
    super(0, element_first, element_last, GLUnsignedTypeMeta
      .getSizeBytes(type));
    this.target_data = target_data;
    this.type = type;
  }

  @Override public void putIndex(
    final int value)
    throws ConstraintError
  {
    Constraints.constrainArbitrary(this.canWrite(), "Cursor is within range");

    final int offset = (int) this.getByteOffset();

    switch (this.type) {
      case TYPE_UNSIGNED_BYTE:
      {
        this.target_data.put(offset, (byte) value);
        this.next();
        return;
      }
      case TYPE_UNSIGNED_INT:
      {
        this.target_data.putInt(offset, value);
        this.next();
        return;
      }
      case TYPE_UNSIGNED_SHORT:
      {
        this.target_data.putShort(offset, (short) value);
        this.next();
        return;
      }
    }

    throw new UnreachableCodeException();
  }
}
