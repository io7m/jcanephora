package com.io7m.jcanephora;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;

/**
 * An immutable reference to an allocated index buffer.
 */

@Immutable public final class IndexBuffer extends Deletable implements
  Buffer,
  GLResource
{
  private final int            value;
  private final long           elements;
  private final GLUnsignedType type;
  private boolean              deleted;

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
    this.deleted = false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */

  /**
   * Retrieve the number of elements in the buffer.
   */

  @Override public long getElements()
  {
    return this.elements;
  }

  /**
   * Retrieve the size in bytes of each element.
   */

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

    throw new UnreachableCodeException();
  }

  /**
   * Retrieve the raw OpenGL 'location' of the buffer.
   */

  @Override public int getLocation()
  {
    return this.value;
  }

  /**
   * Retrieve the total size in bytes of the allocated buffer.
   */

  @Override public long getSizeBytes()
  {
    return this.getElementSizeBytes() * this.elements;
  }

  /**
   * Retrieve the type of the elements in the buffer.
   */

  public @Nonnull GLUnsignedType getType()
  {
    return this.type;
  }

  @Override public void resourceDelete(
    final @Nonnull GLInterfaceEmbedded gl)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    gl.indexBufferDelete(this);
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
    builder.append(this.value);
    builder.append(" ");
    builder.append(this.elements);
    builder.append(" ");
    builder.append(this.getElementSizeBytes());
    builder.append("]");

    return builder.toString();
  }
}
