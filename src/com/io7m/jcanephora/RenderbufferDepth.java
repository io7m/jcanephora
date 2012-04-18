package com.io7m.jcanephora;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * A immutable reference to an allocated depth renderbuffer.
 */

@Immutable public final class RenderbufferDepth implements GLResource
{
  private final int value;
  private final int width;
  private final int height;

  RenderbufferDepth(
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
  }

  @Override public void delete(
    final @Nonnull GLInterface gl)
    throws ConstraintError,
      GLException
  {
    gl.deleteRenderbufferDepth(this);
  }

  public int getHeight()
  {
    return this.height;
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

  public int getWidth()
  {
    return this.width;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[RenderbufferDepth ");
    builder.append(this.getLocation());
    builder.append("]");
    return builder.toString();
  }
}
