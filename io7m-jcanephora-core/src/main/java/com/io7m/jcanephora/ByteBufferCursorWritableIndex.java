/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
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

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;
import com.io7m.jaux.UnreachableCodeException;

/**
 * Generic byte buffer cursor pointing to indices of a given OpenGL unsigned
 * type.
 */

final class ByteBufferCursorWritableIndex extends BufferCursor implements
  CursorWritableIndex
{
  private final @Nonnull ByteBuffer       target_data;
  private final @Nonnull JCGLUnsignedType type;

  ByteBufferCursorWritableIndex(
    final @Nonnull ByteBuffer target_data,
    final @Nonnull RangeInclusive range,
    final @Nonnull JCGLUnsignedType type)
  {
    super(range, 0, type.getSizeBytes());
    this.target_data = target_data;
    this.type = type;
  }

  @Override public void putIndex(
    final int value)
    throws ConstraintError
  {
    Constraints.constrainArbitrary(this.canWrite(), "Cursor is within range");

    final int offset = (int) this.getByteOffset();

    switch (this.type) {
      case TYPE_UNSIGNED_BYTE:
      {
        Constraints.constrainRange(value, 0, 0xff);
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
        Constraints.constrainRange(value, 0, 0xffff);
        this.target_data.putShort(offset, (short) value);
        this.next();
        return;
      }
    }

    throw new UnreachableCodeException();
  }
}
