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

package com.io7m.jcanephora.batchexec;

import com.io7m.jcanephora.JCGLException;

/**
 * The type of exceptions raised by executed programs.
 */

public final class JCGLExceptionExecution extends JCGLException
{
  private static final long serialVersionUID = -3942400021551593890L;

  /**
   * Construct an exception with the given message.
   * 
   * @param message
   *          The message.
   */

  public JCGLExceptionExecution(
    final String message)
  {
    super(message);
  }

  /**
   * Construct an exception with the given cause.
   * 
   * @param e
   *          The cause.
   */

  public JCGLExceptionExecution(
    final Throwable e)
  {
    super(e);
  }

  /**
   * Construct an exception with the given message and cause.
   * 
   * @param e
   *          The cause.
   * @param message
   *          The message.
   */

  public JCGLExceptionExecution(
    final Throwable e,
    final String message)
  {
    super(message, e);
  }
}
