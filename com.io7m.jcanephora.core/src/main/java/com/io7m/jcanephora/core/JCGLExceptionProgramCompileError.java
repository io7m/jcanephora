/*
 * Copyright © 2015 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.core;

import com.io7m.jnull.NullCheck;

/**
 * An exception indicating the failure to compile a given shading program.
 */

public final class JCGLExceptionProgramCompileError extends JCGLException
{
  private static final long serialVersionUID = 1L;
  private final String name;
  private final String message;

  /**
   * Construct an exception.
   *
   * @param in_name    The program name
   * @param in_message The message
   */

  public JCGLExceptionProgramCompileError(
    final String in_name,
    final String in_message)
  {
    super(makeMessage(in_name, in_message));
    this.name = NullCheck.notNull(in_name, "Name");
    this.message = NullCheck.notNull(in_message, "Message");
  }

  private static String makeMessage(
    final String file,
    final String message)
  {
    NullCheck.notNull(file, "File");
    NullCheck.notNull(message, "Message");

    final StringBuilder builder = new StringBuilder(128);
    builder.append("Compilation error: ");
    builder.append(file);
    builder.append(":");
    builder.append(System.lineSeparator());
    builder.append(message);
    return builder.toString();
  }

  /**
   * @return The program name
   */

  public String getProgramName()
  {
    return this.name;
  }

  /**
   * @return The program compilation error
   */

  public String getProgramCompilationError()
  {
    return this.message;
  }
}
