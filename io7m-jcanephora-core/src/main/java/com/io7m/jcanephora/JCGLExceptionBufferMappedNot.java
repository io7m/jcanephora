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
 * to map a buffer object more than once.
 * </p>
 * <p>
 * Note: This should arguably be an unchecked exception as run-time type
 * errors indicate programming mistakes and should not be caught. However, as
 * almost all functions already throw subtypes of {@link JCGLException}, the
 * presence of another subtype does not add any extra clutter to interfaces.
 * </p>
 */

public final class JCGLExceptionBufferMappedNot extends JCGLException
{
  private static final long serialVersionUID;

  static {
    serialVersionUID = 6533386605615175581L;
  }

  /**
   * Construct a new exception explaining that the given array is not mapped.
   * 
   * @param array
   *          The array.
   * @return An exception.
   */

  public static JCGLExceptionBufferMappedNot ofArray(
    final ArrayBufferUsableType array)
  {
    final StringBuilder m = new StringBuilder();
    m.append("The array ");
    m.append(array);
    m.append(" is not mapped.");
    final String s = m.toString();
    assert s != null;
    return new JCGLExceptionBufferMappedNot(s);
  }

  /**
   * Construct a new exception explaining that the given index buffer is not
   * mapped.
   * 
   * @param index
   *          The index buffer.
   * @return An exception.
   */

  public static JCGLExceptionBufferMappedNot ofIndex(
    final IndexBufferUsableType index)
  {
    final StringBuilder m = new StringBuilder();
    m.append("The index buffer ");
    m.append(index);
    m.append(" is not mapped.");
    final String s = m.toString();
    assert s != null;
    return new JCGLExceptionBufferMappedNot(s);
  }

  /**
   * Construct an error with the given message.
   * 
   * @param message
   *          The message.
   */

  public JCGLExceptionBufferMappedNot(
    final String message)
  {
    super(message);
  }
}
