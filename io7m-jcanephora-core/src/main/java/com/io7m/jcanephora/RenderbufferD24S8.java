package com.io7m.jcanephora;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * A immutable reference to an allocated depth/stencil renderbuffer. The
 * buffer has 24 bits of precision for the depth buffer and 8 bits of
 * precision for the stencil.
 */

@Immutable public final class RenderbufferD24S8 extends Deletable implements
  GLResource,
  GLName
{
  private final int value;
  private final int width;
  private final int height;
  private boolean   deleted;

  RenderbufferD24S8(
    final int value,
    final int width,
    final int height)
    throws ConstraintError
  {
    this.value =
      Constraints.constrainRange(
        value,
        0,
        Integer.MAX_VALUE,
        "Buffer ID value");
    this.width = width;
    this.height = height;
    this.deleted = false;
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
    final RenderbufferD24S8 other = (RenderbufferD24S8) obj;
    if (this.value != other.value) {
      return false;
    }
    return true;
  }

  @Override public int getGLName()
  {
    return this.value;
  }

  /**
   * Retrieve the height of the buffer.
   */

  public int getHeight()
  {
    return this.height;
  }

  /**
   * Retrieve the width of the buffer.
   */

  public int getWidth()
  {
    return this.width;
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + this.value;
    return result;
  }

  @Override public void resourceDelete(
    final @Nonnull GLInterfaceEmbedded gl)
    throws ConstraintError,
      GLException
  {
    gl.renderbufferD24S8Delete(this);
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
    builder.append("[RenderbufferD24S8 ");
    builder.append(this.getGLName());
    builder.append("]");
    return builder.toString();
  }
}
