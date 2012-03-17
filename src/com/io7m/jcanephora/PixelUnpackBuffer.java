package com.io7m.jcanephora;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * An immutable reference to an allocated pixel buffer.
 * 
 * <p>
 * A pixel buffer is conceptually an array of elements. Each element has a
 * fixed number of values. Therefore:
 * </p>
 * <p>
 * <code>PixelUnpackBuffer(x, 10, GLScalarType.TYPE_BYTE, 4);</code>
 * </p>
 * <p>
 * The above defines a pixel buffer containing 10, four-byte elements (the
 * typical representation for RGBA pixel data).
 * </p>
 */

@Immutable public final class PixelUnpackBuffer implements Buffer, GLResource
{
  private final int                   location;
  private final long                  elements;
  private final @Nonnull GLScalarType type;
  private final long                  type_elements;

  PixelUnpackBuffer(
    final int location,
    final long elements,
    final @Nonnull GLScalarType type,
    final long type_elements)
    throws ConstraintError
  {
    this.location =
      Constraints.constrainRange(
        location,
        0,
        Integer.MAX_VALUE,
        "Buffer OpenGL location");
    this.elements =
      Constraints.constrainRange(elements, 1, Integer.MAX_VALUE, "Elements");
    this.type = Constraints.constrainNotNull(type, "Element type");
    this.type_elements =
      Constraints.constrainRange(
        type_elements,
        1,
        Integer.MAX_VALUE,
        "Element value count");
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */

  @Override public void delete(
    final @Nonnull GLInterface gl)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    gl.deletePixelUnpackBuffer(this);
  }

  /**
   * Retrieve the number of elements allocated in the buffer.
   */

  @Override public long getElements()
  {
    return this.elements;
  }

  /**
   * Retrieve the size in bytes of a single element in the buffer.
   */

  @Override public long getElementSizeBytes()
  {
    return GLScalarTypeMeta.getSizeBytes(this.type) * this.type_elements;
  }

  /**
   * Retrieve the type of the values in each element.
   */

  public @Nonnull GLScalarType getElementType()
  {
    return this.type;
  }

  /**
   * Returns the number of values in a given element.
   */

  public long getElementValues()
  {
    return this.type_elements;
  }

  /**
   * Return the raw OpenGL 'location' of the buffer.
   */

  @Override public int getLocation()
  {
    return this.location;
  }

  /**
   * Return the total size in bytes of the allocated buffer.
   */

  @Override public long getSizeBytes()
  {
    return this.getElementSizeBytes() * this.elements;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[PixelUnpackBuffer ");
    builder.append(this.location);
    builder.append(" ");
    builder.append(this.elements);
    builder.append(" (");
    builder.append(this.type);
    builder.append(", ");
    builder.append(this.type_elements);
    builder.append(")]");

    return builder.toString();
  }
}
