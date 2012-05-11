package com.io7m.jcanephora;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * A immutable reference to an allocated array buffer.
 */

@Immutable public final class ArrayBuffer extends Deletable implements
  Buffer,
  GLResource
{
  private final int                            value;
  private final long                           elements;
  private final @Nonnull ArrayBufferDescriptor descriptor;
  private boolean                              deleted;

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
    this.deleted = false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
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
   *           Iff <code>0 <= index < this.getElements() == false</code>
   */

  public long getElementOffset(
    final int index)
    throws ConstraintError
  {
    return Constraints.constrainRange(index, 0, this.elements - 1)
      * this.getElementSizeBytes();
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
    return this.descriptor.getSize();
  }

  /**
   * Return the raw OpenGL 'location' of the buffer.
   */

  @Override public int getLocation()
  {
    return this.value;
  }

  /**
   * Return the total size in bytes of the allocated buffer.
   */

  @Override public long getSizeBytes()
  {
    return this.descriptor.getSize() * this.elements;
  }

  @Override public void resourceDelete(
    final @Nonnull GLInterface gl)
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
    builder.append(this.getLocation());
    builder.append(" ");
    builder.append(this.getElements());
    builder.append(" ");
    builder.append(this.getSizeBytes());
    builder.append("]");

    return builder.toString();
  }
}
