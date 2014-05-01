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

package com.io7m.jcanephora;

import com.io7m.jequality.annotations.EqualityStructural;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;
import com.io7m.jranges.RangeInclusiveL;

/**
 * A description of a two dimensional area, given in terms of a pair of
 * {@link RangeInclusive}.
 */

@EqualityStructural public final class AreaInclusive
{
  private final RangeInclusiveL range_x;
  private final RangeInclusiveL range_y;

  /**
   * Construct an inclusive area.
   * 
   * @param in_range_x
   *          The range on the X axis
   * @param in_range_y
   *          The range on the Y axis
   */

  public AreaInclusive(
    final RangeInclusiveL in_range_x,
    final RangeInclusiveL in_range_y)
  {
    this.range_x = NullCheck.notNull(in_range_x, "Range X");
    this.range_y = NullCheck.notNull(in_range_y, "Range Y");
  }

  @Override public boolean equals(
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
   * @return The range of valid indices on the X axis for this area.
   */

  public RangeInclusiveL getRangeX()
  {
    return this.range_x;
  }

  /**
   * @return The range of valid indices on the Y axis for this area.
   */

  public RangeInclusiveL getRangeY()
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
   * @param other
   *          The including area
   * @return <code>true</code> if this area is included within the given area.
   */

  public boolean isIncludedIn(
    final AreaInclusive other)
  {
    NullCheck.notNull(other, "Other");
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
    final String r = builder.toString();
    assert r != null;
    return r;
  }
}
