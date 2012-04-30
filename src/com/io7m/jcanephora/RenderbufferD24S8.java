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

@Immutable public final class RenderbufferD24S8 implements GLResource
{
  private final int value;
  private final int width;
  private final int height;

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
  }

  public int getHeight()
  {
    return this.height;
  }

  /**
   * Return the raw OpenGL 'location' of the buffer.
   */

  public int getLocation()
  {
    return this.value;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */

  public int getWidth()
  {
    return this.width;
  }

  @Override public void resourceDelete(
    final @Nonnull GLInterface gl)
    throws ConstraintError,
      GLException
  {
    gl.renderbufferD24S8Delete(this);
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[RenderbufferD24S8 ");
    builder.append(this.getLocation());
    builder.append("]");
    return builder.toString();
  }
}
