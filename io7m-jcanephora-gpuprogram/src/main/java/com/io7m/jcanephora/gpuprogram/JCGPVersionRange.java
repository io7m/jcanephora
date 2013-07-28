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

package com.io7m.jcanephora.gpuprogram;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.JCGLApiKind;
import com.io7m.jcanephora.JCGLVersionNumber;

/**
 * An inclusive version range.
 */

@Immutable public final class JCGPVersionRange<G extends JCGLApiKind>
{
  private final @Nonnull JCGLVersionNumber lower;
  private final @Nonnull JCGLVersionNumber upper;

  public JCGPVersionRange(
    final @Nonnull JCGLVersionNumber lower,
    final @Nonnull JCGLVersionNumber upper)
    throws ConstraintError
  {
    this.lower = Constraints.constrainNotNull(lower, "Lower bound");
    this.upper = Constraints.constrainNotNull(upper, "Upper bound");

    Constraints.constrainArbitrary(
      this.lower.compareTo(this.upper) <= 0,
      "Lower bound is less than or equal to the upper bound");
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
    final JCGPVersionRange<?> other = (JCGPVersionRange<?>) obj;
    if (!this.lower.equals(other.lower)) {
      return false;
    }
    if (!this.upper.equals(other.upper)) {
      return false;
    }
    return true;
  }

  /**
   * Retrieve the inclusive lower bound of this version range.
   */

  public @Nonnull JCGLVersionNumber getLowerBound()
  {
    return this.lower;
  }

  /**
   * Retrieve the inclusive upper bound of this version range.
   */

  public @Nonnull JCGLVersionNumber getUpperBound()
  {
    return this.upper;
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + this.lower.hashCode();
    result = (prime * result) + this.upper.hashCode();
    return result;
  }

  /**
   * Return <tt>true</tt> iff the version <tt>v</tt> is included in this
   * range.
   */

  public boolean includes(
    final @Nonnull JCGLVersionNumber v)
  {
    return (v.compareTo(this.lower) >= 0) && (v.compareTo(this.upper) <= 0);
  }

  public @Nonnull String toRangeNotation()
  {
    final StringBuilder b = new StringBuilder();
    b.append("[");
    b.append(this.lower.getVersionMajor());
    b.append(".");
    b.append(this.lower.getVersionMinor());
    b.append(".");
    b.append(this.lower.getVersionMicro());
    b.append(", ");
    b.append(this.upper.getVersionMajor());
    b.append(".");
    b.append(this.upper.getVersionMinor());
    b.append(".");
    b.append(this.upper.getVersionMicro());
    b.append("]");
    return b.toString();
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[JCGPVersionRange ");
    builder.append(this.lower);
    builder.append(" ");
    builder.append(this.upper);
    builder.append("]");
    return builder.toString();
  }
}
