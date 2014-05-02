/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

/**
 * Exception type raised when an error occurs whilst compiling and/or linking
 * OpenGL shading programs.
 */

public final class JCGLExceptionCompileError extends JCGLException
{
  private static final long serialVersionUID = -4382015389191616062L;

  private static String makeMessage(
    final String file,
    final String message)
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("Compilation error: ");
    builder.append(file);
    builder.append(":\n");
    builder.append(message);
    final String r = builder.toString();
    assert r != null;
    return r;
  }

  private final String file;

  /**
   * Construct a compilation error with the given file and message.
   * 
   * @param in_file
   *          The file.
   * @param message
   *          The message.
   */

  public JCGLExceptionCompileError(
    final String in_file,
    final String message)
  {
    super(JCGLExceptionCompileError.makeMessage(in_file, message));
    this.file = in_file;
  }

  /**
   * Construct a compilation error with the given cause, file, and message.
   * 
   * @param e
   *          The cause.
   * @param in_file
   *          The file.
   * @param message
   *          The message.
   */

  public JCGLExceptionCompileError(
    final Throwable e,
    final String in_file,
    final String message)
  {
    super(JCGLExceptionCompileError.makeMessage(in_file, message), e);
    this.file = in_file;
  }

  /**
   * @return The name of the file in which the error occurred.
   */

  public String getFile()
  {
    return this.file;
  }
}
