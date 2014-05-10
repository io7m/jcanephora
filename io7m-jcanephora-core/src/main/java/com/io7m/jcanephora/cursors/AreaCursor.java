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

import com.io7m.jcanephora.AreaInclusive;
import com.io7m.jcanephora.SpatialCursorType;
import com.io7m.jequality.annotations.EqualityStructural;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;
import com.io7m.jranges.RangeCheck;
import com.io7m.jranges.RangeCheckException;
import com.io7m.jranges.RangeInclusiveL;

/**
 * <p>
 * Basic cursor implementation for seeking through an area.
 * </p>
 * <p>
 * The area is assumed to be comprised of elements of size
 * <code>element_bytes</code> bytes.
 * </p>
 * <p>
 * The cursor is instantiated with two areas: An "outer" area and an "inner"
 * area. The inner area is expected to be contained within the outer area (the
 * two may in fact be identical). The cursor is allowed to access elements in
 * the inner area, and will use the outer area to construct byte offsets.
 * </p>
 */

@EqualityStructural public abstract class AreaCursor implements
  SpatialCursorType
{
  private final AreaInclusive area_inner;
  private final AreaInclusive area_outer;
  private long                byte_offset;
  private boolean             can_write;
  private final long          element_bytes;
  private long                element_x;
  private long                element_y;
  private final long          row_byte_span;

  protected AreaCursor(
    final AreaInclusive in_area_outer,
    final AreaInclusive in_area_inner,
    final long in_element_bytes)
  {
    NullCheck.notNull(in_area_outer, "Area outer");
    NullCheck.notNull(in_area_inner, "Area inner");

    /**
     * It is the responsibility of subtypes to respect these preconditions.
     */

    /**
     * The inner area must be included in the outer area.
     */

    if (in_area_inner.isIncludedIn(in_area_outer) == false) {
      throw new RangeCheckException("Inner area is included in outer area");
    }

    /**
     * The lower bound of the outer area must be greater than or equal to
     * zero.
     */

    RangeCheck.checkGreaterEqual(
      in_area_outer.getRangeX().getLower(),
      "Outer X lower bound",
      0,
      "Minimum X lower bound");

    /**
     * The lower bound of the outer area must be greater than or equal to
     * zero.
     */

    RangeCheck.checkGreaterEqual(
      in_area_outer.getRangeY().getLower(),
      "Outer Y lower bound",
      0,
      "Minimum Y lower bound");

    /**
     * If the outer lower bound is not negative, and the inner bound is
     * included within the outer bound, then the inner bounds cannot be
     * negative.
     */

    /**
     * The Element size must be non-zero.
     */

    RangeCheck.checkGreater(
      in_element_bytes,
      "Element size",
      0,
      "Minimum element size");

    this.area_outer = in_area_outer;
    this.area_inner = in_area_inner;
    this.element_bytes = in_element_bytes;
    this.row_byte_span =
      in_area_outer.getRangeX().getInterval() * in_element_bytes;

    final long lo_x = in_area_inner.getRangeX().getLower();
    final long lo_y = in_area_inner.getRangeY().getLower();
    this.uncheckedSeek(lo_x, lo_y);
  }

  private void checkValid()
  {
    if (this.isValid() == false) {
      throw new IllegalStateException("Cursor is out of range");
    }
  }

  @Override public final boolean equals(
    final @Nullable Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    final AreaCursor other = (AreaCursor) obj;
    if (!this.area_inner.equals(other.area_inner)) {
      return false;
    }
    if (!this.area_outer.equals(other.area_outer)) {
      return false;
    }
    if (this.byte_offset != other.byte_offset) {
      return false;
    }
    if (this.element_bytes != other.element_bytes) {
      return false;
    }
    return true;
  }

  /**
   * @return The current byte offset.
   */

  public final long getByteOffset()
  {
    this.checkValid();
    return this.byte_offset;
  }

  /**
   * @return The number of bytes used by each element.
   */

  public final long getElementBytes()
  {
    return this.element_bytes;
  }

  @Override public final long getElementX()
  {
    this.checkValid();
    return this.element_x;
  }

  @Override public final long getElementY()
  {
    this.checkValid();
    return this.element_y;
  }

  @Override public final int hashCode()
  {
    final int p = 31;
    int r = 1;
    r = (p * r) + this.area_inner.hashCode();
    r = (p * r) + this.area_outer.hashCode();
    r = (p * r) + (int) (this.byte_offset ^ (this.byte_offset >>> 32));
    r = (p * r) + (int) (this.element_bytes ^ (this.element_bytes >>> 32));
    return r;
  }

  @Override public final boolean isValid()
  {
    return this.can_write;
  }

  @Override public final void next()
  {
    final RangeInclusiveL range_x = this.area_inner.getRangeX();

    long x = this.element_x;
    long y = this.element_y;

    if (x == range_x.getUpper()) {
      x = range_x.getLower();
      y = this.element_y + 1;
    } else {
      x = this.element_x + 1;
      y = this.element_y;
    }

    this.uncheckedSeek(x, y);
  }

  @Override public final void seekTo(
    final long x,
    final long y)
  {
    this.uncheckedSeek(x, y);
  }

  @Override public final String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[AreaCursor ");
    builder.append(this.area_outer);
    builder.append(" ");
    builder.append(this.area_inner);
    builder.append(" ");
    builder.append(this.element_bytes);
    builder.append(" ");
    builder.append(this.element_x);
    builder.append(" ");
    builder.append(this.element_y);
    builder.append(" ");
    builder.append(this.byte_offset);
    builder.append(" ");
    builder.append(this.row_byte_span);
    builder.append(" ");
    builder.append(this.can_write);
    builder.append("]");
    final String r = builder.toString();
    assert r != null;
    return r;
  }

  protected final void uncheckedSeek(
    final long x,
    final long y)
  {
    final RangeInclusiveL range_x = this.area_inner.getRangeX();
    final RangeInclusiveL range_y = this.area_inner.getRangeY();

    this.element_x = x;
    this.element_y = y;

    this.byte_offset =
      (this.element_y * this.row_byte_span)
        + (this.element_x * this.element_bytes);

    this.can_write =
      ((x >= range_x.getLower()) && (x <= range_x.getUpper()))
        && ((y >= range_y.getLower()) && (y <= range_y.getUpper()));
  }
}
