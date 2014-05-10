/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.cursors;

import com.io7m.jcanephora.CursorType;
import com.io7m.jequality.annotations.EqualityStructural;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeCheck;
import com.io7m.jranges.RangeInclusiveL;

/**
 * <p>
 * Basic cursor implementation for seeking through a buffer.
 * </p>
 * <p>
 * The buffer is assumed to be comprised of elements of size
 * <code>element_size</code> bytes.
 * </p>
 * <p>
 * The cursor is assumed to be pointing to attributes that are
 * <code>attribute_offset</code> bytes from the start of each element.
 * </p>
 * <p>
 * The cursor can address elements from the given inclusive range and will
 * reject attempts to write outside of this range.
 * </p>
 */

@EqualityStructural public abstract class BufferCursor implements CursorType
{
  private final long            attribute_offset;
  private long                  byte_current;
  private boolean               can_write;
  private long                  element_current;
  private final long            element_size;
  private final RangeInclusiveL range;

  protected BufferCursor(
    final RangeInclusiveL in_range,
    final long in_attribute_offset,
    final long in_element_size)
  {
    NullCheck.notNull(in_range, "Range");

    /**
     * It is the responsibility of subtypes to respect these preconditions.
     */

    /**
     * Lower bound must not be negative.
     */

    RangeCheck.checkGreaterEqual(
      in_range.getLower(),
      "Lower bound",
      0L,
      "Minimum lower bound");

    /**
     * Element size must be greater than zero.
     */

    RangeCheck.checkGreater(
      in_element_size,
      "Element size",
      0L,
      "Minimum element size");

    /**
     * Attribute offset must be greater than zero.
     */

    RangeCheck.checkGreaterEqual(
      in_attribute_offset,
      "Attribute offset",
      0L,
      "Minimum attribute offset");

    /**
     * Attribute offset must be less than the element size.
     */

    RangeCheck.checkLess(
      in_attribute_offset,
      "Attribute offset",
      in_element_size,
      "Element size");

    this.attribute_offset = in_attribute_offset;
    this.range = in_range;
    this.element_size = in_element_size;
    this.uncheckedSeek(in_range.getLower());
  }

  protected final void checkValid()
  {
    if (this.isValid() == false) {
      throw new IllegalStateException("Cursor is out of range");
    }
  }

  /**
   * @return The current byte offset of the cursor.
   */

  public final long getByteOffset()
  {
    this.checkValid();
    return this.byte_current;
  }

  /**
   * @return The current element of the cursor.
   */

  public final long getElement()
  {
    this.checkValid();
    return this.element_current;
  }

  @Override public final boolean hasNext()
  {
    return (this.element_current + 1) <= this.range.getUpper();
  }

  @Override public final boolean isValid()
  {
    return this.can_write;
  }

  @Override public final void next()
  {
    this.uncheckedSeek(this.element_current + 1);
  }

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
