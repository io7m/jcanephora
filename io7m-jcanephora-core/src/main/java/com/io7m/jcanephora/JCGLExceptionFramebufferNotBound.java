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
 * <p>
 * Exception class representing an error caused by the programmer attempting
 * to perform operations with a framebuffer that is not bound.
 * </p>
 * <p>
 * Note: This should arguably be an unchecked exception as run-time type
 * errors indicate programming mistakes and should not be caught. However, as
 * almost all functions already throw subtypes of {@link JCGLException}, the
 * presence of another subtype does not add any extra clutter to interfaces.
 * </p>
 */

public final class JCGLExceptionFramebufferNotBound extends JCGLException
{
  private static final long serialVersionUID;

  static {
    serialVersionUID = 7797977371517659070L;
  }

  /**
   * Construct an exception explaining that the given framebuffer is not
   * bound.
   * 
   * @param framebuffer
   *          The framebuffer.
   * @return An exception.
   */

  public static JCGLExceptionFramebufferNotBound notBound(
    final FramebufferUsableType framebuffer)
  {
    final String s =
      String.format("Framebuffer %s is not bound", framebuffer);
    assert s != null;
    return new JCGLExceptionFramebufferNotBound(s);
  }

  /**
   * Construct an error with the given message.
   * 
   * @param message
   *          The message.
   */

  public JCGLExceptionFramebufferNotBound(
    final String message)
  {
    super(message);
  }
}
