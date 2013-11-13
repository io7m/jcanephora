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
 * A immutable reference to an allocated array buffer.
 */

@Immutable public final class ArrayBuffer extends JCGLResourceDeletable implements
  Buffer,
  ArrayBufferUsable
{
  private final RangeInclusive           range;
  private final @Nonnull ArrayBufferType type;
  private final int                      value;

  ArrayBuffer(
    final int value,
    final long elements,
    final @Nonnull ArrayBufferTypeDescriptor descriptor)
    throws ConstraintError
  {
    this.value =
      Constraints.constrainRange(
        value,
        1,
        Integer.MAX_VALUE,
        "Buffer ID value");

    this.range =
      new RangeInclusive(0, Constraints.constrainRange(
        elements,
        1,
        Integer.MAX_VALUE,
        "Buffer elements") - 1);

    this.type =
      new ArrayBufferType(this, Constraints.constrainNotNull(
        descriptor,
        "Buffer descriptor"));
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
    final ArrayBuffer other = (ArrayBuffer) obj;
    if (this.value != other.value) {
      return false;
    }
    return true;
  }

  @Override public @Nonnull ArrayBufferAttribute getAttribute(
    final @Nonnull String name)
    throws ConstraintError
  {
    return this.type.getAttribute(name);
  }

  @Override public long getElementOffset(
    final int index)
    throws ConstraintError
  {
    return Constraints.constrainRange(index, 0, this.range.getUpper())
      * this.getElementSizeBytes();
  }

  @Override public long getElementSizeBytes()
  {
    return this.type.getTypeDescriptor().getSize();
  }

  @Override public int getGLName()
  {
    return this.value;
  }

  @Override public @Nonnull RangeInclusive getRange()
  {
    return this.range;
  }

  @Override public long getSizeBytes()
  {
    return this.type.getTypeDescriptor().getSize() * this.range.getInterval();
  }

  @Override public @Nonnull ArrayBufferType getType()
  {
    return this.type;
  }

  @Override public boolean hasAttribute(
    final @Nonnull String name)
    throws ConstraintError
  {
    return this.type.hasAttribute(name);
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + this.value;
    return result;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[ArrayBuffer ");
    builder.append(this.getGLName());
    builder.append(" ");
    builder.append(this.range);
    builder.append(" ");
    builder.append(this.getSizeBytes());
    builder.append("]");
    return builder.toString();
  }
}
