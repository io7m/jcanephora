package com.io7m.jcanephora;

import javax.annotation.Nonnull;

/**
 * Exception class representing an error raised by the OpenGL implementation.
 */

public final class GLException extends Exception
{
  private static final long serialVersionUID = -6495367643829747178L;
  private final int         code;

  public GLException(
    final int code,
    final @Nonnull String message)
  {
    super(message);
    this.code = code;
  }

  public int getCode()
  {
    return this.code;
  }
}
