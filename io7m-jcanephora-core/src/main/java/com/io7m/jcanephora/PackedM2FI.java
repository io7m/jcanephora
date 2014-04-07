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

/**
 * A single mutable packed value consisting of a floating point component, and
 * an integer component.
 */

public final class PackedM2FI
{
  public static @Nonnull PackedM2FI newPacked2FI(
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
    final PackedM2FI other = (PackedM2FI) obj;
    if (Float.floatToIntBits(this.f) != Float.floatToIntBits(other.f)) {
      return false;
    }
    if (this.i != other.i) {
      return false;
    }
    return true;
  }

  public float getFirst()
  {
    return this.f;
  }

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

  public void setFirst(
    final float f1)
  {
    this.f = f1;
  }

  public void setSecond(
    final int i1)
  {
    this.i = i1;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[PackedM2FI ");
    builder.append(this.f);
    builder.append(" ");
    builder.append(this.i);
    builder.append("]");
    return builder.toString();
  }
}
