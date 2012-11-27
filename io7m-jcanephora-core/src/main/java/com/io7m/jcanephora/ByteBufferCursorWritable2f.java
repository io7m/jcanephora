package com.io7m.jcanephora;

import java.nio.ByteBuffer;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Generic byte buffer cursor pointing to elements containing two floats.
 */

final class ByteBufferCursorWritable2f extends BufferCursor implements
  CursorWritable2f
{
  private final @Nonnull ByteBuffer target_data;

  ByteBufferCursorWritable2f(
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
   * Put the values <code>x, y</code> at the current cursor location and seek
   * the cursor to the next element iff there is one.
   */

  @Override public void put2f(
    final float x,
    final float y)
    throws ConstraintError
  {
    Constraints.constrainArbitrary(this.canWrite(), "Cursor is within range");

    final int byte_current = (int) this.getByteOffset();
    this.target_data.putFloat(byte_current + 0, x);
    this.target_data.putFloat(byte_current + 4, y);
    this.next();
  }
}
