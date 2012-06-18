package com.io7m.jcanephora;

import java.nio.ByteBuffer;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Cursor type, pointing to elements of type Vector3f.
 * 
 * The cursor is used to point to specific attributes in a mapped ArrayBuffer.
 * Intuitively, this can be thought of as a cursor that seeks through an array
 * of records and will only point to a specific field in each record element.
 */

public final class ArrayBufferCursorWritable3f implements CursorWritable3f
{
  private final @Nonnull ArrayBufferWritableMap map;
  private long                                  index = 0;
  private final long                            index_max;
  private long                                  index_byte_current;
  private final long                            offset_bytes;
  private final @Nonnull ArrayBufferDescriptor  descriptor;

  ArrayBufferCursorWritable3f(
    final int offset_bytes,
    final @Nonnull ArrayBufferWritableMap map,
    final @Nonnull ArrayBufferDescriptor d)
  {
    this.map = map;
    this.index_max = map.getArrayBuffer().getElements() - 1L;
    this.offset_bytes = offset_bytes;
    this.index_byte_current = offset_bytes;
    this.descriptor = d;
  }

  /**
   * Return <code>true</code> iff there are more elements available of the
   * cursor's attribute in the map.
   */

  @Override public boolean hasNext()
  {
    return (this.index + 1) <= this.index_max;
  }

  /**
   * Point the cursor at the next available element of the cursor's attribute.
   * 
   * @throws ConstraintError
   *           Iff there are no more elements in the map.
   */

  @Override public void next()
    throws ConstraintError
  {
    Constraints.constrainArbitrary(this.hasNext(), "Next element exists");

    this.index += 1;
    this.index_byte_current += this.descriptor.getSize();
  }

  /**
   * Put the values <code>x, y, z</code> at the current cursor location and
   * seek the cursor to the next element iff there is one.
   */

  @Override public void put3f(
    final float x,
    final float y,
    final float z)
  {
    final ByteBuffer b = this.map.getByteBuffer();
    b.putFloat((int) this.index_byte_current, x);
    b.putFloat((int) (this.index_byte_current + 4), y);
    b.putFloat((int) (this.index_byte_current + 8), z);

    try {
      if (this.hasNext()) {
        this.next();
      }
    } catch (final ConstraintError e) {
      /* UNREACHABLE */
      throw new AssertionError("unreachable code: report this bug!");
    }
  }

  /**
   * Seek to the given element.
   */

  @Override public void seekTo(
    final long to)
    throws ConstraintError
  {
    this.index = Constraints.constrainRange(to, 0, this.index_max);
    this.index_byte_current =
      (this.index * this.descriptor.getSize()) + this.offset_bytes;
  }
}
