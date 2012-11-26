package com.io7m.jcanephora;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * A immutable reference to an allocated framebuffer.
 */

@Immutable public final class Framebuffer extends Deletable implements
  GLResource
{
  private final int value;
  private boolean   deleted;

  Framebuffer(
    final int value)
    throws ConstraintError
  {
    this.value =
      Constraints.constrainRange(
        value,
        0,
        Integer.MAX_VALUE,
        "Buffer ID value");
    this.deleted = false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */

  /**
   * Return the raw OpenGL 'location' of the buffer.
   */

  public int getLocation()
  {
    return this.value;
  }

  @Override public void resourceDelete(
    final @Nonnull GLInterfaceEmbedded gl)
    throws ConstraintError,
      GLException
  {
    gl.framebufferDelete(this);
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
    builder.append("[Framebuffer ");
    builder.append(this.getLocation());
    builder.append("]");
    return builder.toString();
  }
}
