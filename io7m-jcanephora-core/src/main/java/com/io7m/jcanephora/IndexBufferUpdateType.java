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
 * The type of index buffer updates.
 * </p>
 * <p>
 * An index buffer update is essentially a mutable region of memory that will
 * be used to replace part of (or the entirety of) an index buffer.
 * </p>
 */

public interface IndexBufferUpdateType
{
  /**
   * @return A cursor that points to elements of the index buffer. The cursor
   *         interface allows constant time access to any element and also
   *         minimizes the number of checks performed for each access.
   */

  CursorWritableIndexType getCursor();

  /**
   * @return The index buffer to which this update will be applied.
   */

  IndexBufferUsableType getIndexBuffer();
}
