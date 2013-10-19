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
import javax.annotation.concurrent.NotThreadSafe;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;

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

@NotThreadSafe class AreaCursor implements SpatialCursor
{
  private final @Nonnull AreaInclusive area_outer;
  private final @Nonnull AreaInclusive area_inner;
  private final long                   element_bytes;
  private long                         element_x;
  private long                         element_y;
  private long                         byte_offset;
  private final long                   row_byte_span;
  private boolean                      can_write;

  protected AreaCursor(
    final @Nonnull AreaInclusive area_outer,
    final @Nonnull AreaInclusive area_inner,
    final long element_bytes)
    throws ConstraintError
  {
    /**
     * These are not constraint errors because this class is not visible
     * outside of the jcanephora package: it is the responsibility of subtypes
     * to respect these preconditions.
     */

    if (area_inner.isIncludedIn(area_outer) == false) {
      throw new IllegalArgumentException(
        "Inner area is included in outer area");
    }
    if (0 > area_outer.getRangeX().getLower()) {
      throw new IllegalArgumentException("Outer X lower bound is negative");
    }
    if (0 > area_outer.getRangeY().getLower()) {
      throw new IllegalArgumentException("Outer Y lower bound is negative");
    }

    /**
     * If the outer lower bound is not negative, and the inner bound is
     * included within the outer bound, then the inner bounds cannot be
     * negative.
     */

    if (0 > element_bytes) {
      throw new IllegalArgumentException("element_bytes is negative");
    }

    this.area_outer = area_outer;
    this.area_inner = area_inner;
    this.element_bytes = element_bytes;
    this.row_byte_span = area_outer.getRangeX().getInterval() * element_bytes;

    this.uncheckedSeek(area_inner.getRangeX().getLower(), area_inner
      .getRangeY()
      .getLower());
  }

  @Override public final boolean isValid()
  {
    return this.can_write;
  }

  @Override public boolean equals(
    final Object obj)
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

  protected final long getByteOffset()
    throws ConstraintError
  {
    Constraints.constrainArbitrary(this.isValid(), "Cursor is in range");
    return this.byte_offset;
  }

  @Override public final long getElementX()
    throws ConstraintError
  {
    Constraints.constrainArbitrary(this.isValid(), "Cursor is in range");
    return this.element_x;
  }

  @Override public final long getElementY()
    throws ConstraintError
  {
    Constraints.constrainArbitrary(this.isValid(), "Cursor is in range");
    return this.element_y;
  }

  @Override public int hashCode()
  {
    final int p = 31;
    int r = 1;
    r = (p * r) + this.area_inner.hashCode();
    r = (p * r) + this.area_outer.hashCode();
    r = (p * r) + (int) (this.byte_offset ^ (this.byte_offset >>> 32));
    r = (p * r) + (int) (this.element_bytes ^ (this.element_bytes >>> 32));
    return r;
  }

  @Override public final void next()
  {
    final RangeInclusive range_x = this.area_inner.getRangeX();

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

  @Override public String toString()
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
    return builder.toString();
  }

  final void uncheckedSeek(
    final long x,
    final long y)
  {
    final RangeInclusive range_x = this.area_inner.getRangeX();
    final RangeInclusive range_y = this.area_inner.getRangeY();

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
