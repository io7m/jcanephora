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
import com.io7m.jranges.RangeCheck;
import com.io7m.junreachable.UnreachableCodeException;

/**
 * <p>
 * Immutable attribute descriptor for an array buffer.
 * </p>
 * <p>
 * If array buffers are considered as arrays of records, this type represents
 * a single field of the type of the record used.
 * </p>
 */

@EqualityStructural public final class ArrayAttributeDescriptor
{
  private final int            components;
  private final String         name;
  private final JCGLScalarType type;

  /**
   * Construct a new attribute descriptor.
   * 
   * @param in_name
   *          The name of the attribute.
   * @param in_type
   *          The type of the components of the attribute.
   * @param in_components
   *          The number of components in the attribute.
   * @return A new attribute.
   */

  public static ArrayAttributeDescriptor newAttribute(
    final String in_name,
    final JCGLScalarType in_type,
    final int in_components)
  {
    return new ArrayAttributeDescriptor(in_name, in_type, in_components);
  }

  private ArrayAttributeDescriptor(
    final String in_name,
    final JCGLScalarType in_type,
    final int in_components)
  {
    this.name = NullCheck.notNull(in_name, "Element name");
    this.type = NullCheck.notNull(in_type, "Element type");
    this.components =
      (int) RangeCheck.checkIncludedIn(
        in_components,
        "Components",
        RangeCheck.POSITIVE_INTEGER,
        "Components");
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
    final ArrayAttributeDescriptor other = (ArrayAttributeDescriptor) obj;
    if (this.components != other.components) {
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
   * @return The number of components in the attribute.
   */

  public int getComponents()
  {
    return this.components;
  }

  /**
   * @return The type of the attribute as a {@link JCGLType}.
   */

  public JCGLType getJCGLType()
  {
    switch (this.type) {
      case TYPE_FLOAT:
      {
        switch (this.components) {
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
        switch (this.components) {
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
   * @return The name of the attribute.
   */

  public String getName()
  {
    return this.name;
  }

  /**
   * @return The type of the attribute.
   */

  public JCGLScalarType getType()
  {
    return this.type;
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + this.components;
    result = (prime * result) + this.name.hashCode();
    result = (prime * result) + this.type.hashCode();
    return result;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[ArrayAttributeDescriptor ");
    builder.append(this.name);
    builder.append(" ");
    builder.append(this.type);
    builder.append(" ");
    builder.append(this.components);
    builder.append("]");
    final String r = builder.toString();
    assert r != null;
    return r;
  }

  /**
   * Assert that the current attribute has exactly the given type and number
   * of components, throwing {@link JCGLTypingException} if it does not.
   * 
   * @param required_components
   *          The required number of components.
   * @param required_type
   *          The required type.
   * @throws JCGLTypingException
   *           If type checking fails.
   */

  @SuppressWarnings({ "boxing", "null" }) public void checkTypes(
    final int required_components,
    final JCGLScalarType required_type)
    throws JCGLTypingException
  {
    if (this.components != required_components) {
      throw new JCGLTypingException(String.format(
        "Attribute '%s' has %d components, but %d are required",
        this.name,
        this.components,
        required_components));
    }

    if (this.type != required_type) {
      throw new JCGLTypingException(
        String
          .format(
            "Attribute '%s' has components of type %s, but components of type %s are required",
            this.name,
            this.type,
            required_type));
    }
  }
}
