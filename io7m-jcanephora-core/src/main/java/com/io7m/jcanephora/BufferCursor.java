/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;

/**
 * Basic cursor implementation for seeking through a buffer.
 * 
 * The buffer is assumed to be comprised of elements of size
 * <code>element_size</code> bytes.
 * 
 * The cursor is assumed to be pointing to attributes that are
 * <code>attribute_offset</code> bytes from the start of each element.
 * 
 * The cursor can address elements from the given inclusive range and will
 * reject attempts to write outside of this range.
 */

class BufferCursor implements Cursor
{
  private final long                    attribute_offset;
  private long                          byte_current;
  private boolean                       can_write;

  private long                          element_current;
  private final long                    element_size;
  private final @Nonnull RangeInclusive range;

  protected BufferCursor(
    final @Nonnull RangeInclusive range,
    final long attribute_offset,
    final long element_size)
  {
    /**
     * These are not constraint errors because this class is not visible
     * outside of the jcanephora package: it is the responsibility of subtypes
     * to respect these preconditions.
     */

    if (0 > range.getLower()) {
      throw new IllegalArgumentException("lower bound is negative");
    }
    if (0 > element_size) {
      throw new IllegalArgumentException("element_size is negative");
    }

    if (attribute_offset < 0) {
      throw new IllegalArgumentException("attribute_offset is negative");
    }
    if (attribute_offset >= element_size) {
      throw new IllegalArgumentException("attribute_offset >= element_size");
    }

    this.attribute_offset = attribute_offset;
    this.range = range;
    this.element_size = element_size;
    this.uncheckedSeek(range.getLower());
  }

  /**
   * Return the current byte offset of the cursor.
   * 
   * @throws ConstraintError
   *           Iff the cursor is out of range
   */

  protected final long getByteOffset()
    throws ConstraintError
  {
    Constraints.constrainArbitrary(this.isValid(), "Cursor is in range");
    return this.byte_current;
  }

  /**
   * Return the current element of the cursor.
   * 
   * @throws ConstraintError
   *           Iff the cursor is out of range
   */

  protected final long getElement()
    throws ConstraintError
  {
    Constraints.constrainArbitrary(this.isValid(), "Cursor is in range");
    return this.element_current;
  }

  /**
   * Return <code>true</code> iff there are more elements available.
   */

  @Override public final boolean hasNext()
  {
    return (this.element_current + 1) <= this.range.getUpper();
  }

  @Override public final boolean isValid()
  {
    return this.can_write;
  }

  /**
   * Point the cursor at the next available element of the cursor's attribute.
   */

  @Override public final void next()
  {
    this.uncheckedSeek(this.element_current + 1);
  }

  /**
   * Seek to the given element.
   */

  @Override public final void seekTo(
    final long element)
  {
    this.uncheckedSeek(element);
  }

  private void uncheckedSeek(
    final long element)
  {
    this.element_current = element;
    this.byte_current =
      (this.element_current * this.element_size) + this.attribute_offset;
    this.can_write =
      (this.range.getLower() <= this.element_current)
        && (this.element_current <= this.range.getUpper());
  }
}
