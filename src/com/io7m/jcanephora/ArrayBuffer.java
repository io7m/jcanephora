package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * A reference to an allocated array buffer.
 */

public final class ArrayBuffer implements Buffer, GLResource
{
  private final int                            value;
  private final long                           elements;
  private final @Nonnull ArrayBufferDescriptor descriptor;

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
    this.elements =
      Constraints.constrainRange(
        elements,
        1,
        Integer.MAX_VALUE,
        "Buffer elements");
    this.descriptor =
      Constraints.constrainNotNull(descriptor, "Buffer descriptor");
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
    gl.deleteArrayBuffer(this);
  }

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
   *           Iff <code>0 <= index < this.getElements() == false</code>
   */

  public long getElementOffset(
    final int index)
    throws ConstraintError
  {
    return Constraints.constrainRange(index, 0, this.elements - 1)
      * this.getElementSizeBytes();
  }

  @Override public long getElements()
  {
    return this.elements;
  }

  @Override public long getElementSizeBytes()
  {
    return this.descriptor.getSize();
  }

  @Override public int getLocation()
  {
    return this.value;
  }

  @Override public long getSizeBytes()
  {
    return this.descriptor.getSize() * this.elements;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[ArrayBufferID ");
    builder.append(this.getLocation());
    builder.append(" ");
    builder.append(this.getElements());
    builder.append(" ");
    builder.append(this.getSizeBytes());
    builder.append("]");

    return builder.toString();
  }
}
