package com.io7m.jcanephora;

import javax.annotation.Nonnull;

/**
 * A read-only interface to the {@link Renderbuffer} type that allows use of
 * the type but not mutation and/or deletion of the contents.
 */

public interface RenderbufferUsable<K extends RenderbufferKind> extends
  GLName,
  GLResourceUsable
{
  /**
   * Retrieve the height of the buffer.
   */

  public int getHeight();

  /**
   * Retrieve the type of the renderbuffer.
   */

  public @Nonnull RenderbufferType getType();

  /**
   * Retrieve the width of the buffer.
   */

  public int getWidth();
}
