package com.io7m.jcanephora;

import java.nio.ByteBuffer;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Cursor type, pointing to elements of type Vector4b.
 * 
 * The cursor is used to point to specific attributes in a mapped
 * PixelUnpackBuffer. Intuitively, this can be thought of as a cursor that
 * seeks through an array of records and will only point to the start of each
 * record.
 */

public final class PixelUnpackBufferCursorWritable4b implements
  CursorWritable4b
{
  private final @Nonnull PixelUnpackBufferWritableMap map;
  private long                                        index              = 0;
  private final long                                  index_max;
  private long                                        index_byte_current = 0;

  PixelUnpackBufferCursorWritable4b(
    final @Nonnull PixelUnpackBufferWritableMap map)
  {
    this.map = map;
    this.index_max = map.getPixelUnpackBuffer().getElements() - 1L;
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
    this.index_byte_current += 4;
  }

  /**
   * Seek to the given element.
   */

  @Override public void seekTo(
    final long to)
    throws ConstraintError
  {
    this.index = Constraints.constrainRange(to, 0, this.index_max);
    this.index_byte_current = this.index * 4;
  }

  /**
   * Put the values <code>s, t, u, v</code> at the current cursor location and
   * seek the cursor to the next element iff there is one.
   */

  @Override public void put4b(
    final byte s,
    final byte t,
    final byte u,
    final byte v)
  {
    final ByteBuffer b = this.map.getByteBuffer();
    b.put((int) (this.index_byte_current + 0), s);
    b.put((int) (this.index_byte_current + 1), t);
    b.put((int) (this.index_byte_current + 2), u);
    b.put((int) (this.index_byte_current + 3), v);

    try {
      if (this.hasNext()) {
        this.next();
      }
    } catch (final ConstraintError e) {
      /* UNREACHABLE */
      throw new AssertionError("unreachable code: report this bug!");
    }
  }

}
