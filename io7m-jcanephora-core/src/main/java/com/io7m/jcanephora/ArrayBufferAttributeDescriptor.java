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

/**
 * <p>
 * Immutable attribute descriptor for an array buffer.
 * </p>
 * <p>
 * If array buffers are considered as arrays of records, this type represents
 * a single field of the type of the record used.
 * </p>
 */

@Immutable public final class ArrayBufferAttributeDescriptor
{
  private final int                     elements;
  private final @Nonnull String         name;
  private final @Nonnull JCGLScalarType type;

  public ArrayBufferAttributeDescriptor(
    final @Nonnull String name,
    final @Nonnull JCGLScalarType type,
    final int elements)
    throws ConstraintError
  {
    this.name = Constraints.constrainNotNull(name, "Element name");
    this.type = Constraints.constrainNotNull(type, "Element type");
    this.elements =
      Constraints.constrainRange(
        elements,
        1,
        Integer.MAX_VALUE,
        "Element size");
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
    final ArrayBufferAttributeDescriptor other =
      (ArrayBufferAttributeDescriptor) obj;
    if (this.elements != other.elements) {
      return false;
    }
    if (!this.name.equals(other.name)) {
      return false;
    }
    if (this.type != other.type) {
      return false;
    }
    return true;
  }

  /**
   * Retrieve the number of elements in the attribute.
   */

  public int getElements()
  {
    return this.elements;
  }

  /**
   * Retrieve the name of the attribute.
   */

  public @Nonnull String getName()
  {
    return this.name;
  }

  /**
   * Retrieve the type of the attribute.
   */

  public @Nonnull JCGLScalarType getType()
  {
    return this.type;
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + this.elements;
    result = (prime * result) + this.name.hashCode();
    result = (prime * result) + this.type.hashCode();
    return result;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[ArrayBufferAttributeDescriptor ");
    builder.append(this.name);
    builder.append(" ");
    builder.append(this.type);
    builder.append(" ");
    builder.append(this.elements);
    builder.append("]");
    return builder.toString();
  }
}
