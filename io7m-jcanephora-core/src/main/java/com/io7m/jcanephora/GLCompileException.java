/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
 * 
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

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
