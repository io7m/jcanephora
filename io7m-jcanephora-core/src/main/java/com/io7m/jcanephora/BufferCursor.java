package com.io7m.jcanephora;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Basic cursor implementation for seeking through a buffer.
 * 
 * The buffer is assumed to be comprised of elements of size
 * <code>element_size</code> bytes.
 * 
 * The cursor is assumed to be pointing to attributes that are
 * <code>attribute_offset</code> bytes from the start of each element.
 * 
 * The cursor can address elements from the inclusive range
 * <code>[element_first .. element_last]</code>.
 * 
 * As this class is not visible outside of the <code>jcanephora</code>
 * package, the class assumes that sane values are given to the constructor.
 */

class BufferCursor implements Cursor
{
  private final long attribute_offset;
  private final long element_first;
  private final long element_last;
  private final long element_size;

  private long       element_current;
  private long       byte_current;

  BufferCursor(
    final long attribute_offset,
    final long element_first,
    final long element_last,
    final long element_size)
  {
    this.attribute_offset = attribute_offset;
    this.element_first = element_first;
    this.element_last = element_last;
    this.element_size = element_size;

    this.element_current = element_first;
    this.byte_current = (element_first * element_size) + attribute_offset;
  }

  /**
   * Return the current byte offset of the cursor.
   */

  protected final long getByteOffset()
  {
    return this.byte_current;
  }

  /**
   * Return the current element of the cursor.
   */

  protected final long getElement()
  {
    return this.element_current;
  }

  /**
   * Return <code>true</code> iff there are more elements available.
   */

  @Override public boolean hasNext()
  {
    return (this.element_current + 1) <= this.element_last;
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
    this.uncheckedSeek(this.element_current + 1);
  }

  /**
   * Seek to the given element.
   */

  @Override public final void seekTo(
    final long element)
    throws ConstraintError
  {
    Constraints
      .constrainRange(element, this.element_first, this.element_last);
    this.uncheckedSeek(element);
  }

  private void uncheckedSeek(
    final long element)
  {
    this.element_current = element;
    this.byte_current =
      (this.element_current * this.element_size) + this.attribute_offset;
  }
}
