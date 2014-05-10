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

import com.io7m.jnull.Nullable;

/**
 * A single mutable packed value consisting of a floating point component, and
 * an integer component.
 */

public final class PackedM2FI
{
  /**
   * Construct a new packed value.
   * 
   * @param x
   *          The first (floating-point) component.
   * @param y
   *          The second (integer) component.
   * @return A new packed value.
   */

  public static PackedM2FI newPacked2FI(
    final float x,
    final int y)
  {
    return new PackedM2FI(x, y);
  }

  private float f;
  private int   i;

  private PackedM2FI(
    final float f1,
    final int i1)
  {
    this.f = f1;
    this.i = i1;
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
    final PackedM2FI other = (PackedM2FI) obj;
    if (Float.floatToIntBits(this.f) != Float.floatToIntBits(other.f)) {
      return false;
    }
    if (this.i != other.i) {
      return false;
    }
    return true;
  }

  /**
   * @return The first (floating-point) component.
   */

  public float getFirst()
  {
    return this.f;
  }

  /**
   * @return The second (integer) component.
   */

  public int getSecond()
  {
    return this.i;
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + Float.floatToIntBits(this.f);
    result = (prime * result) + this.i;
    return result;
  }

  /**
   * Set the first (floating-point) component.
   * 
   * @param x
   *          The new value
   */

  public void setFirst(
    final float x)
  {
    this.f = x;
  }

  /**
   * Set the second (integer) component.
   * 
   * @param x
   *          The new value
   */

  public void setSecond(
    final int x)
  {
    this.i = x;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[PackedM2FI ");
    builder.append(this.f);
    builder.append(" ");
    builder.append(this.i);
    builder.append("]");
    final String r = builder.toString();
    assert r != null;
    return r;
  }
}
