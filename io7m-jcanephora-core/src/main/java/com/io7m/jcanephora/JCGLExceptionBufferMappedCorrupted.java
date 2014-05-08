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
 * Exception class representing an error caused by a mapped buffer becoming
 * "corrupted" by external events prior to unmapping. See page 46 of the
 * OpenGL 3.3 specification:
 * </p>
 * <blockquote> "UnmapBuffer returns TRUE unless data values in the buffer's
 * data store have become corrupted during the period that the buffer was
 * mapped. Such corruption can be the result of a screen resolution change or
 * other window system-dependent event that causes system heaps such as those
 * for high-performance graphics memory to be discarded. GL implementations
 * must guarantee that such corruption can occur only during the periods that
 * a buffer's data store is mapped. If such corruption has occurred,
 * UnmapBuffer returns FALSE, and the contents of the buffer's data store
 * become undefined." </blockquote>
 * <p>
 * Note: This should arguably be an unchecked exception as run-time type
 * errors indicate programming mistakes and should not be caught. However, as
 * almost all functions already throw subtypes of {@link JCGLException}, the
 * presence of another subtype does not add any extra clutter to interfaces.
 * </p>
 */

public final class JCGLExceptionBufferMappedCorrupted extends JCGLException
{
  private static final long serialVersionUID;

  static {
    serialVersionUID = 6533386605615175581L;
  }

  /**
   * Construct an error with the given message.
   * 
   * @param message
   *          The message.
   */

  public JCGLExceptionBufferMappedCorrupted(
    final String message)
  {
    super(message);
  }
}
