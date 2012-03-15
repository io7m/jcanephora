package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * A reference to an allocated index buffer.
 */

public final class IndexBuffer implements Buffer, GLResource
{
  private final int            value;
  private final long           elements;
  private final GLUnsignedType type;

  IndexBuffer(
    final int value,
    final long elements,
    final GLUnsignedType type)
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
    this.type = Constraints.constrainNotNull(type, "GL type");
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
    gl.deleteIndexBuffer(this);
  }

  @Override public long getElements()
  {
    return this.elements;
  }

  @Override public long getElementSizeBytes()
  {
    switch (this.type) {
      case TYPE_UNSIGNED_BYTE:
        return 1;
      case TYPE_UNSIGNED_INT:
        return 4;
      case TYPE_UNSIGNED_SHORT:
        return 2;
    }

    throw new AssertionError();
  }

  @Override public int getLocation()
  {
    return this.value;
  }

  @Override public long getSizeBytes()
  {
    return this.getElementSizeBytes() * this.elements;
  }

  public @Nonnull GLUnsignedType getType()
  {
    return this.type;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[ArrayBufferID ");
    builder.append(this.value);
    builder.append(" ");
    builder.append(this.elements);
    builder.append(" ");
    builder.append(this.getElementSizeBytes());
    builder.append("]");

    return builder.toString();
  }
}
