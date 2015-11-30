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
 * <p>Exception class representing an error caused by an invalid framebuffer
 * configuration.</p>
 */

public final class JCGLExceptionFramebufferInvalid extends JCGLException
{
  private final JCGLFramebufferStatus status;

  /**
   * Construct an exception with the given status code.
   *
   * @param s The status code
   */

  public JCGLExceptionFramebufferInvalid(
    final JCGLFramebufferStatus s)
  {
    super(JCGLExceptionFramebufferInvalid.makeMessage(s));
    this.status = NullCheck.notNull(s, "Status");
  }

  private static String makeMessage(
    final JCGLFramebufferStatus s)
  {
    NullCheck.notNull(s, "Status");
    final String rs =
      String.format("Invalid framebuffer configuration: %s", s);
    assert rs != null;
    return rs;
  }

  /**
   * @return The framebuffer status
   */

  public JCGLFramebufferStatus getStatus()
  {
    return this.status;
  }
}
