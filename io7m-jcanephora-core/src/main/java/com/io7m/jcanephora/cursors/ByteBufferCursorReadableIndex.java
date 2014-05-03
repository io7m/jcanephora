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

package com.io7m.jcanephora.cursors;

import java.nio.ByteBuffer;

import com.io7m.jcanephora.CursorReadableIndexType;
import com.io7m.jcanephora.JCGLUnsignedType;
import com.io7m.jranges.RangeInclusiveL;
import com.io7m.junreachable.UnreachableCodeException;

/**
 * Generic byte buffer cursor pointing to indices of a given OpenGL unsigned
 * type.
 */

public final class ByteBufferCursorReadableIndex extends ByteBufferCursor implements
  CursorReadableIndexType
{
  /**
   * Construct a new cursor.
   * 
   * @param in_data
   *          The data.
   * @param range
   *          The range to be read.
   * @param in_type
   *          The type of elements.
   * @return A new cursor.
   */

  public static CursorReadableIndexType newCursor(
    final ByteBuffer in_data,
    final RangeInclusiveL range,
    final JCGLUnsignedType in_type)
  {
    return new ByteBufferCursorReadableIndex(in_data, range, in_type);
  }

  private final JCGLUnsignedType type;

  private ByteBufferCursorReadableIndex(
    final ByteBuffer in_data,
    final RangeInclusiveL range,
    final JCGLUnsignedType in_type)
  {
    super(in_data, range, 0, in_type.getSizeBytes());
    this.type = in_type;
  }

  @Override public int getIndex()
  {
    this.checkValid();

    final ByteBuffer b = this.getBuffer();
    final int offset = (int) this.getByteOffset();

    switch (this.type) {
      case TYPE_UNSIGNED_BYTE:
      {
        final int value = b.get(offset);
        this.next();
        return value & 0xff;
      }
      case TYPE_UNSIGNED_INT:
      {
        final int value = b.getInt(offset);
        this.next();
        return value;
      }
      case TYPE_UNSIGNED_SHORT:
      {
        final int value = b.getShort(offset);
        this.next();
        return value & 0xffff;
      }
    }

    throw new UnreachableCodeException();
  }
}
