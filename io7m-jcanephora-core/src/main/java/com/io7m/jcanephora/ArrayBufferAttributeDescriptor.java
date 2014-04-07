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
import com.io7m.jaux.UnreachableCodeException;

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
    final @Nonnull String name1,
    final @Nonnull JCGLScalarType type1,
    final int elements1)
    throws ConstraintError
  {
    this.name = Constraints.constrainNotNull(name1, "Element name");
    this.type = Constraints.constrainNotNull(type1, "Element type");
    this.elements =
      Constraints.constrainRange(
        elements1,
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
   * Retrieve the type of the attribute as a {@link JCGLType}.
   */

  public @Nonnull JCGLType getJCGLType()
  {
    switch (this.type) {
      case TYPE_FLOAT:
      {
        switch (this.elements) {
          case 1:
            return JCGLType.TYPE_FLOAT;
          case 2:
            return JCGLType.TYPE_FLOAT_VECTOR_2;
          case 3:
            return JCGLType.TYPE_FLOAT_VECTOR_3;
          case 4:
            return JCGLType.TYPE_FLOAT_VECTOR_4;
        }
        throw new UnreachableCodeException();
      }
      case TYPE_INT:
      {
        switch (this.elements) {
          case 1:
            return JCGLType.TYPE_INTEGER;
          case 2:
            return JCGLType.TYPE_INTEGER_VECTOR_2;
          case 3:
            return JCGLType.TYPE_INTEGER_VECTOR_3;
          case 4:
            return JCGLType.TYPE_INTEGER_VECTOR_4;
        }
        throw new UnreachableCodeException();
      }
      case TYPE_BYTE:
      case TYPE_SHORT:
      case TYPE_UNSIGNED_BYTE:
      case TYPE_UNSIGNED_INT:
      case TYPE_UNSIGNED_SHORT:
        throw new UnreachableCodeException();
    }

    throw new UnreachableCodeException();
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
