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

import java.nio.ByteBuffer;

import com.io7m.jranges.RangeCheck;
import com.io7m.jranges.RangeInclusiveL;
import com.io7m.junreachable.UnreachableCodeException;

/**
 * Generic byte buffer cursor pointing to indices of a given OpenGL unsigned
 * type.
 */

final class ByteBufferCursorWritableIndex extends BufferCursor implements
  CursorWritableIndexType
{
  private final ByteBuffer       target_data;
  private final JCGLUnsignedType type;

  ByteBufferCursorWritableIndex(
    final ByteBuffer target_data1,
    final RangeInclusiveL range,
    final JCGLUnsignedType type1)
  {
    super(range, 0, type1.getSizeBytes());
    this.target_data = target_data1;
    this.type = type1;
  }

  @Override public void putIndex(
    final int value)
  {
    this.checkValid();
    final int offset = (int) this.getByteOffset();

    switch (this.type) {
      case TYPE_UNSIGNED_BYTE:
      {
        RangeCheck.checkGreaterEqual(value, "Value", 0, "Lower bound");
        RangeCheck.checkLessEqual(value, "Value", 0xff, "Upper bound");

        this.target_data.put(offset, (byte) value);
        this.next();
        return;
      }
      case TYPE_UNSIGNED_INT:
      {
        this.target_data.putInt(offset, value);
        this.next();
        return;
      }
      case TYPE_UNSIGNED_SHORT:
      {
        RangeCheck.checkGreaterEqual(value, "Value", 0, "Lower bound");
        RangeCheck.checkLessEqual(value, "Value", 0xffff, "Upper bound");

        this.target_data.putShort(offset, (short) value);
        this.next();
        return;
      }
    }

    throw new UnreachableCodeException();
  }
}
