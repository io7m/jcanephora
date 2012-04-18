package com.io7m.jcanephora;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * A immutable reference to an allocated framebuffer.
 */

@Immutable public final class Framebuffer implements GLResource
{
  private final int value;

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
    gl.deleteFramebuffer(this);
  }

  /**
   * Return the raw OpenGL 'location' of the buffer.
   */

  public int getLocation()
  {
    return this.value;
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
