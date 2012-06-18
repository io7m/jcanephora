package com.io7m.jcanephora;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Immutable attribute descriptor for an array buffer.
 * 
 * If array buffers are considered as arrays of records, this type represents
 * a single field of the type of the record used.
 */

@Immutable public final class ArrayBufferAttribute
{
  private @Nonnull final String       name;
  private @Nonnull final GLScalarType type;
  private final int                   elements;

  public ArrayBufferAttribute(
    final @Nonnull String name,
    final @Nonnull GLScalarType type,
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

  /**
   * Retrieve the number of elements in the attribute.
   */

  int getElements()
  {
    return this.elements;
  }

  /**
   * Retrieve the name of the attribute.
   */

  @Nonnull String getName()
  {
    return this.name;
  }

  /**
   * Retrieve the type of the attribute.
   */

  @Nonnull GLScalarType getType()
  {
    return this.type;
  }
}
