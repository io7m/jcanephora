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

import com.io7m.jranges.RangeInclusiveL;
import com.io7m.junreachable.UnreachableCodeException;

/**
 * Generic byte buffer cursor pointing to indices of a given OpenGL unsigned
 * type.
 */

final class ByteBufferCursorReadableIndex extends BufferCursor implements
  CursorReadableIndexType
{
  private final ByteBuffer       target_data;
  private final JCGLUnsignedType type;

  ByteBufferCursorReadableIndex(
    final ByteBuffer in_data,
    final RangeInclusiveL range,
    final JCGLUnsignedType in_type)
  {
    super(range, 0, in_type.getSizeBytes());
    this.target_data = in_data;
    this.type = in_type;
  }

  @Override public int getIndex()
  {
    this.checkValid();

    final int offset = (int) this.getByteOffset();

    switch (this.type) {
      case TYPE_UNSIGNED_BYTE:
      {
        final int value = this.target_data.get(offset);
        this.next();
        return value & 0xff;
      }
      case TYPE_UNSIGNED_INT:
      {
        final int value = this.target_data.getInt(offset);
        this.next();
        return value;
      }
      case TYPE_UNSIGNED_SHORT:
      {
        final int value = this.target_data.getShort(offset);
        this.next();
        return value & 0xffff;
      }
    }

    throw new UnreachableCodeException();
  }
}
