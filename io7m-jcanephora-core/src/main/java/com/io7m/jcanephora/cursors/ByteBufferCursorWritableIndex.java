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

import com.io7m.jcanephora.CursorWritableIndexType;
import com.io7m.jcanephora.JCGLUnsignedType;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeCheck;
import com.io7m.jranges.RangeInclusiveL;
import com.io7m.junreachable.UnreachableCodeException;

/**
 * Generic byte buffer cursor pointing to indices of a given OpenGL unsigned
 * type.
 */

public final class ByteBufferCursorWritableIndex extends ByteBufferCursor implements
  CursorWritableIndexType
{
  /**
   * Construct a new cursor capable of writing to indices of the given type
   * within the specified range of the given data.
   *
   * @param data
   *          The data.
   * @param range
   *          The range within the data.
   * @param type
   *          The type of indices.
   * @return A new writable cursor.
   */

  public static CursorWritableIndexType newCursor(
    final ByteBuffer data,
    final RangeInclusiveL range,
    final JCGLUnsignedType type)
  {
    return new ByteBufferCursorWritableIndex(data, range, type);
  }

  private final JCGLUnsignedType type;

  private ByteBufferCursorWritableIndex(
    final ByteBuffer in_target_data,
    final RangeInclusiveL range,
    final JCGLUnsignedType in_type)
  {
    super(in_target_data, range, 0, in_type.getSizeBytes());
    this.type = NullCheck.notNull(in_type, "Type");
  }

  @Override public void putIndex(
    final long value)
  {
    this.checkValid();

    final ByteBuffer b = this.getBuffer();
    final int offset = (int) this.getByteOffset();

    switch (this.type) {
      case TYPE_UNSIGNED_BYTE:
      {
        RangeCheck.checkGreaterEqual(value, "Value", 0, "Lower bound");
        RangeCheck.checkLessEqual(value, "Value", 0xff, "Upper bound");

        b.put(offset, (byte) value);
        this.next();
        return;
      }
      case TYPE_UNSIGNED_INT:
      {
        b.putInt(offset, (int) value);
        this.next();
        return;
      }
      case TYPE_UNSIGNED_SHORT:
      {
        RangeCheck.checkGreaterEqual(value, "Value", 0, "Lower bound");
        RangeCheck.checkLessEqual(value, "Value", 0xffff, "Upper bound");

        b.putShort(offset, (short) value);
        this.next();
        return;
      }
    }

    throw new UnreachableCodeException();
  }
}
