package com.io7m.jcanephora;

import javax.annotation.Nonnull;

/**
 * Exception type raised when an error occurs whilst compiling and/or linking
 * OpenGL shading programs.
 */

public final class GLCompileException extends Exception
{
  private static final long serialVersionUID = -4382015389191616062L;

  private static @Nonnull String makeMessage(
    final @Nonnull String file,
    final @Nonnull String message)
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("Compilation error: ");
    builder.append(file);
    builder.append(":\n");
    builder.append(message);
    return builder.toString();
  }

  private final @Nonnull String file;

  public GLCompileException(
    final @Nonnull String file,
    final @Nonnull String message)
  {
    super(GLCompileException.makeMessage(file, message));
    this.file = file;
  }

  /**
   * Retrieve the name of the file in which the error occurred.
   */

  public @Nonnull String getFile()
  {
    return this.file;
  }
}
