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
 * to specify a nonexistent attribute.
 * </p>
 * <p>
 * Note: This should arguably be an unchecked exception as run-time type
 * errors indicate programming mistakes and should not be caught. However, as
 * almost all functions already throw subtypes of {@link JCGLException}, the
 * presence of another subtype does not add any extra clutter to interfaces.
 * </p>
 */

public final class JCGLMissingAttributeException extends JCGLException
{
  private static final long serialVersionUID;

  static {
    serialVersionUID = -2399910787893514882L;
  }

  /**
   * Construct an exception with a useful error message.
   * 
   * @param name
   *          The name of the missing attribute.
   * @return An exception.
   */

  public static JCGLMissingAttributeException noSuchAttribute(
    final String name)
  {
    final String text = String.format("No such attribute '%s'", name);
    assert text != null;
    return new JCGLMissingAttributeException(text);
  }

  /**
   * Construct an error with the given message.
   * 
   * @param message
   *          The message.
   */

  public JCGLMissingAttributeException(
    final String message)
  {
    super(message);
  }
}
