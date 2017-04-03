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
 * An exception indicating that the package cannot execute on the current
 * context version.
 */

public final class JCGLExceptionUnsupported extends JCGLExceptionChecked
{
  private static final long serialVersionUID = 1L;
  private final JCGLVersionNumber expected;
  private final JCGLVersionNumber received;

  /**
   * Construct an exception.
   *
   * @param v_exp The expected (required) version
   * @param v_got The actual version received
   */

  public JCGLExceptionUnsupported(
    final JCGLVersionNumber v_exp,
    final JCGLVersionNumber v_got)
  {
    super("Unsupported OpenGL version");
    this.expected = NullCheck.notNull(v_exp);
    this.received = NullCheck.notNull(v_got);
  }

  /**
   * @return The expected (required) OpenGL version
   */

  public JCGLVersionNumber getExpected()
  {
    return this.expected;
  }

  /**
   * @return The received OpenGL version
   */

  public JCGLVersionNumber getReceived()
  {
    return this.received;
  }
}
