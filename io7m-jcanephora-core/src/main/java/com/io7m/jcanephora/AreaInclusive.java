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
import javax.annotation.concurrent.Immutable;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;

/**
 * A description of a two dimensional area, given in terms of a pair of
 * {@link RangeInclusive}.
 */

@Immutable public final class AreaInclusive
{
  private final @Nonnull RangeInclusive range_x;
  private final @Nonnull RangeInclusive range_y;

  public AreaInclusive(
    final @Nonnull RangeInclusive range_x,
    final @Nonnull RangeInclusive range_y)
    throws ConstraintError
  {
    this.range_x = Constraints.constrainNotNull(range_x, "Range X");
    this.range_y = Constraints.constrainNotNull(range_y, "Range Y");
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
    final AreaInclusive other = (AreaInclusive) obj;
    if (!this.range_x.equals(other.range_x)) {
      return false;
    }
    if (!this.range_y.equals(other.range_y)) {
      return false;
    }
    return true;
  }

  /**
   * Retrieve the range of valid indices on the X axis for this area.
   */

  public @Nonnull RangeInclusive getRangeX()
  {
    return this.range_x;
  }

  /**
   * Retrieve the range of valid indices on the Y axis for this area.
   */

  public @Nonnull RangeInclusive getRangeY()
  {
    return this.range_y;
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + this.range_x.hashCode();
    result = (prime * result) + this.range_y.hashCode();
    return result;
  }

  /**
   * Return <code>true</code> if this area is included within the given area.
   */

  public boolean isIncludedIn(
    final @Nonnull AreaInclusive other)
    throws ConstraintError
  {
    return this.range_x.isIncludedIn(other.range_x)
      && this.range_y.isIncludedIn(other.range_y);
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[Area ");
    builder.append(this.range_x);
    builder.append(" ");
    builder.append(this.range_y);
    builder.append("]");
    return builder.toString();
  }
}
