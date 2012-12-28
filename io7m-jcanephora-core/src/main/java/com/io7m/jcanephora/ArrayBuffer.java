/*
 * Copyright © 2012 http://io7m.com
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

@Immutable public final class ArrayBuffer extends Deletable implements
  Buffer,
  GLResource
{
  private final int                            value;
  private final @Nonnull ArrayBufferDescriptor descriptor;
  private boolean                              deleted;
  private final RangeInclusive                 range;

  ArrayBuffer(
    final int value,
    final long elements,
    final @Nonnull ArrayBufferDescriptor descriptor)
    throws ConstraintError
  {
    this.value =
      Constraints.constrainRange(
        value,
        0,
        Integer.MAX_VALUE,
        "Buffer ID value");

    this.range =
      new RangeInclusive(0, Constraints.constrainRange(
        elements,
        1,
        Integer.MAX_VALUE,
        "Buffer elements") - 1);

    this.descriptor =
      Constraints.constrainNotNull(descriptor, "Buffer descriptor");
    this.deleted = false;
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

  /**
   * Retrieve the descriptor that describes the types of values in the array.
   */

  public @Nonnull ArrayBufferDescriptor getDescriptor()
  {
    return this.descriptor;
  }

  /**
   * Return the offset in bytes of the element at <code>index</code>.
   * 
   * @param index
   *          The index.
   * @throws ConstraintError
   *           Iff
   *           <code>0 <= index < this.getRange().getUpper() == false</code>
   */

  public long getElementOffset(
    final int index)
    throws ConstraintError
  {
    return Constraints.constrainRange(index, 0, this.range.getUpper())
      * this.getElementSizeBytes();
  }

  /**
   * Retrieve the size in bytes of a single element in the buffer.
   */

  @Override public long getElementSizeBytes()
  {
    return this.descriptor.getSize();
  }

  @Override public int getGLName()
  {
    return this.value;
  }

  @Override public @Nonnull RangeInclusive getRange()
  {
    return this.range;
  }

  /**
   * Return the total size in bytes of the allocated buffer.
   */

  @Override public long getSizeBytes()
  {
    return this.descriptor.getSize() * this.range.getInterval();
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + this.value;
    return result;
  }

  @Override public void resourceDelete(
    final @Nonnull GLInterfaceES2 gl)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    gl.arrayBufferDelete(this);
  }

  @Override public boolean resourceIsDeleted()
  {
    return this.deleted;
  }

  @Override void setDeleted()
  {
    this.deleted = true;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[ArrayBufferID ");
    builder.append(this.getGLName());
    builder.append(" ");
    builder.append(this.range);
    builder.append(" ");
    builder.append(this.getSizeBytes());
    builder.append("]");

    return builder.toString();
  }
}
