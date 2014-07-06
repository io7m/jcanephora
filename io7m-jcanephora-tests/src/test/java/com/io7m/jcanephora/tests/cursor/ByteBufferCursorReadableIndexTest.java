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

package com.io7m.jcanephora.tests.cursor;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jcanephora.CursorReadableIndexType;
import com.io7m.jcanephora.CursorWritableIndexType;
import com.io7m.jcanephora.JCGLUnsignedType;
import com.io7m.jcanephora.cursors.ByteBufferCursorReadableIndex;
import com.io7m.jcanephora.cursors.ByteBufferCursorWritableIndex;
import com.io7m.jranges.RangeInclusiveL;

@SuppressWarnings("static-method") public class ByteBufferCursorReadableIndexTest
{
  @Test public void testReadWriteByteIdentity()
  {
    final int element_count = 4;
    final JCGLUnsignedType type = JCGLUnsignedType.TYPE_UNSIGNED_BYTE;
    final int element_size = type.getSizeBytes();

    final ByteBuffer data =
      ByteBuffer.allocate(element_count * element_size).order(
        ByteOrder.nativeOrder());
    assert data != null;

    final RangeInclusiveL range = new RangeInclusiveL(0, element_count - 1);
    final CursorWritableIndexType cw =
      ByteBufferCursorWritableIndex.newCursor(data, range, type);
    final CursorReadableIndexType cr =
      ByteBufferCursorReadableIndex.newCursor(data, range, type);

    cw.putIndex(5);
    cw.putIndex(7);
    cw.putIndex(9);
    cw.putIndex(11);

    Assert.assertTrue(5 == cr.getIndex());
    Assert.assertTrue(7 == cr.getIndex());
    Assert.assertTrue(9 == cr.getIndex());
    Assert.assertTrue(11 == cr.getIndex());
  }

  @Test public void testReadWriteIntIdentity()
  {
    final int element_count = 4;
    final JCGLUnsignedType type = JCGLUnsignedType.TYPE_UNSIGNED_INT;
    final int element_size = type.getSizeBytes();

    final ByteBuffer data =
      ByteBuffer.allocate(element_count * element_size).order(
        ByteOrder.nativeOrder());
    assert data != null;

    final RangeInclusiveL range = new RangeInclusiveL(0, element_count - 1);
    final CursorWritableIndexType cw =
      ByteBufferCursorWritableIndex.newCursor(data, range, type);
    final CursorReadableIndexType cr =
      ByteBufferCursorReadableIndex.newCursor(data, range, type);

    cw.putIndex(5);
    cw.putIndex(7);
    cw.putIndex(9);
    cw.putIndex(0xffffffffL);

    Assert.assertTrue(5 == cr.getIndex());
    Assert.assertTrue(7 == cr.getIndex());
    Assert.assertTrue(9 == cr.getIndex());
    Assert.assertEquals(0xffffffffL, 0xffffffff & cr.getIndex());
  }

  @Test public void testReadWriteShortIdentity()
  {
    final int element_count = 4;
    final JCGLUnsignedType type = JCGLUnsignedType.TYPE_UNSIGNED_SHORT;
    final int element_size = type.getSizeBytes();

    final ByteBuffer data =
      ByteBuffer.allocate(element_count * element_size).order(
        ByteOrder.nativeOrder());
    assert data != null;

    final RangeInclusiveL range = new RangeInclusiveL(0, element_count - 1);
    final CursorWritableIndexType cw =
      ByteBufferCursorWritableIndex.newCursor(data, range, type);
    final CursorReadableIndexType cr =
      ByteBufferCursorReadableIndex.newCursor(data, range, type);

    cw.putIndex(5);
    cw.putIndex(7);
    cw.putIndex(9);
    cw.putIndex(11);

    Assert.assertTrue(5 == cr.getIndex());
    Assert.assertTrue(7 == cr.getIndex());
    Assert.assertTrue(9 == cr.getIndex());
    Assert.assertTrue(11 == cr.getIndex());
  }
}
