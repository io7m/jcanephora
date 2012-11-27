package com.io7m.jcanephora;

import java.nio.ByteBuffer;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Generic byte buffer cursor pointing to elements containing three floats.
 */

final class ByteBufferCursorWritable3f extends BufferCursor implements
  CursorWritable3f
{
  private final @Nonnull ByteBuffer target_data;

  ByteBufferCursorWritable3f(
    final @Nonnull ByteBuffer target_data,
    final long attribute_offset,
    final long element_first,
    final long element_last,
    final long element_size)
  {
    super(attribute_offset, element_first, element_last, element_size);
    this.target_data = target_data;
  }

  /**
   * Put the values <code>x, y, z, w</code> at the current cursor location and
   * seek the cursor to the next element iff there is one.
   */

  @Override public void put3f(
    final float x,
    final float y,
    final float z)
    throws ConstraintError
  {
    Constraints.constrainArbitrary(this.canWrite(), "Cursor is within range");

    final int byte_current = (int) this.getByteOffset();
    this.target_data.putFloat(byte_current + 0, x);
    this.target_data.putFloat(byte_current + 4, y);
    this.target_data.putFloat(byte_current + 8, z);
    this.next();
  }
}
