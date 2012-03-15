package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * A reference to an allocated pixel buffer.
 */

public final class PixelUnpackBuffer implements Buffer, GLResource
{
  private final int  value;
  private final long elements;
  private final long element_size;

  PixelUnpackBuffer(
    final int value,
    final long elements,
    final long element_size)
    throws ConstraintError
  {
    this.value =
      Constraints.constrainRange(
        value,
        0,
        Integer.MAX_VALUE,
        "buffer ID value");
    this.elements =
      Constraints.constrainRange(elements, 1, Integer.MAX_VALUE, "elements");
    this.element_size =
      Constraints.constrainRange(
        element_size,
        1,
        Integer.MAX_VALUE,
        "element size");
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

  @Override public long getElements()
  {
    return this.elements;
  }

  @Override public long getElementSizeBytes()
  {
    return this.element_size;
  }

  @Override public int getLocation()
  {
    return this.value;
  }

  @Override public long getSizeBytes()
  {
    return this.element_size * this.elements;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[PixelUnpackBuffer ");
    builder.append(this.value);
    builder.append(" ");
    builder.append(this.elements);
    builder.append(" ");
    builder.append(this.element_size);
    builder.append("]");

    return builder.toString();
  }
}
