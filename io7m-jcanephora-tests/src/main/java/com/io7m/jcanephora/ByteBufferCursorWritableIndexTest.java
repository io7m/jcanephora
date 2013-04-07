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
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;

public class ByteBufferCursorWritableIndexTest
{
  @SuppressWarnings("static-method") @Test public void testWriteByte()
    throws ConstraintError
  {
    final int element_count = 4;
    final GLUnsignedType type = GLUnsignedType.TYPE_UNSIGNED_BYTE;
    final int element_size = type.getSizeBytes();

    final ByteBuffer data =
      ByteBuffer.allocate(element_count * element_size).order(
        ByteOrder.nativeOrder());

    final ByteBufferCursorWritableIndex c =
      new ByteBufferCursorWritableIndex(data, new RangeInclusive(
        0,
        element_count - 1), type);

    c.putIndex(5);
    c.putIndex(7);
    c.putIndex(9);
    c.putIndex(11);

    Assert.assertTrue(5 == data.get(0));
    Assert.assertTrue(7 == data.get(1));
    Assert.assertTrue(9 == data.get(2));
    Assert.assertTrue(11 == data.get(3));
  }

  @SuppressWarnings("static-method") @Test public void testWriteInt()
    throws ConstraintError
  {
    final int element_count = 4;
    final GLUnsignedType type = GLUnsignedType.TYPE_UNSIGNED_INT;
    final int element_size = type.getSizeBytes();

    final ByteBuffer data =
      ByteBuffer.allocate(element_count * element_size).order(
        ByteOrder.nativeOrder());

    final ByteBufferCursorWritableIndex c =
      new ByteBufferCursorWritableIndex(data, new RangeInclusive(
        0,
        element_count - 1), type);

    c.putIndex(5);
    c.putIndex(7);
    c.putIndex(9);
    c.putIndex(11);

    final IntBuffer ib = data.asIntBuffer();
    Assert.assertTrue(5 == ib.get(0));
    Assert.assertTrue(7 == ib.get(1));
    Assert.assertTrue(9 == ib.get(2));
    Assert.assertTrue(11 == ib.get(3));
  }

  @SuppressWarnings("static-method") @Test public void testWriteShort()
    throws ConstraintError
  {
    final int element_count = 4;
    final GLUnsignedType type = GLUnsignedType.TYPE_UNSIGNED_SHORT;
    final int element_size = type.getSizeBytes();

    final ByteBuffer data =
      ByteBuffer.allocate(element_count * element_size).order(
        ByteOrder.nativeOrder());

    final ByteBufferCursorWritableIndex c =
      new ByteBufferCursorWritableIndex(data, new RangeInclusive(
        0,
        element_count - 1), type);

    c.putIndex(5);
    c.putIndex(7);
    c.putIndex(9);
    c.putIndex(11);

    final ShortBuffer sb = data.asShortBuffer();
    Assert.assertTrue(5 == sb.get(0));
    Assert.assertTrue(7 == sb.get(1));
    Assert.assertTrue(9 == sb.get(2));
    Assert.assertTrue(11 == sb.get(3));
  }
}
