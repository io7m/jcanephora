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

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;

public class IndexBufferWritableDataTest
{
  @SuppressWarnings("static-method") @Test public
    void
    testWriteCompleteBytes()
      throws ConstraintError
  {
    final IndexBuffer indices =
      new IndexBuffer(
        1,
        new RangeInclusive(0, 3),
        JCGLUnsignedType.TYPE_UNSIGNED_BYTE);
    final IndexBufferWritableData data = new IndexBufferWritableData(indices);

    Assert.assertEquals(0, data.getTargetDataOffset());
    Assert.assertEquals(4, data.getTargetDataSize());

    final CursorWritableIndex cursor = data.getCursor();

    cursor.putIndex(3);
    cursor.putIndex(5);
    cursor.putIndex(7);
    cursor.putIndex(11);

    final ByteBuffer buffer = data.getTargetData();
    Assert.assertTrue(3 == buffer.get(0));
    Assert.assertTrue(5 == buffer.get(1));
    Assert.assertTrue(7 == buffer.get(2));
    Assert.assertTrue(11 == buffer.get(3));
  }

  @SuppressWarnings("static-method") @Test public
    void
    testWriteCompleteInts()
      throws ConstraintError
  {
    final IndexBuffer indices =
      new IndexBuffer(
        1,
        new RangeInclusive(0, 3),
        JCGLUnsignedType.TYPE_UNSIGNED_INT);
    final IndexBufferWritableData data = new IndexBufferWritableData(indices);

    Assert.assertEquals(0, data.getTargetDataOffset());
    Assert.assertEquals(4 * 4, data.getTargetDataSize());

    final CursorWritableIndex cursor = data.getCursor();

    cursor.putIndex(3);
    cursor.putIndex(5);
    cursor.putIndex(7);
    cursor.putIndex(11);

    final ByteBuffer buffer = data.getTargetData();
    Assert.assertTrue(3 == buffer.getShort(0));
    Assert.assertTrue(5 == buffer.getShort(4));
    Assert.assertTrue(7 == buffer.getShort(8));
    Assert.assertTrue(11 == buffer.getShort(12));
  }

  @SuppressWarnings("static-method") @Test public
    void
    testWriteCompleteShorts()
      throws ConstraintError
  {
    final IndexBuffer indices =
      new IndexBuffer(
        1,
        new RangeInclusive(0, 3),
        JCGLUnsignedType.TYPE_UNSIGNED_SHORT);
    final IndexBufferWritableData data = new IndexBufferWritableData(indices);

    Assert.assertEquals(0, data.getTargetDataOffset());
    Assert.assertEquals(4 * 2, data.getTargetDataSize());

    final CursorWritableIndex cursor = data.getCursor();

    cursor.putIndex(3);
    cursor.putIndex(5);
    cursor.putIndex(7);
    cursor.putIndex(11);

    final ByteBuffer buffer = data.getTargetData();
    Assert.assertTrue(3 == buffer.getShort(0));
    Assert.assertTrue(5 == buffer.getShort(2));
    Assert.assertTrue(7 == buffer.getShort(4));
    Assert.assertTrue(11 == buffer.getShort(6));
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testWriteOverflowByte()
      throws ConstraintError
  {
    CursorWritableIndex cursor = null;

    try {
      final IndexBuffer indices =
        new IndexBuffer(
          1,
          new RangeInclusive(0, 7),
          JCGLUnsignedType.TYPE_UNSIGNED_BYTE);
      final IndexBufferWritableData data =
        new IndexBufferWritableData(indices);

      cursor = data.getCursor();
    } catch (final ConstraintError e) {
      Assert.fail(e.getMessage());
    }

    assert cursor != null;
    cursor.putIndex(0xff + 1);
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testWriteOverflowShort()
      throws ConstraintError
  {
    CursorWritableIndex cursor = null;

    try {
      final IndexBuffer indices =
        new IndexBuffer(
          1,
          new RangeInclusive(0, 7),
          JCGLUnsignedType.TYPE_UNSIGNED_SHORT);
      final IndexBufferWritableData data =
        new IndexBufferWritableData(indices);

      cursor = data.getCursor();
    } catch (final ConstraintError e) {
      Assert.fail(e.getMessage());
    }

    assert cursor != null;
    cursor.putIndex(0xffff + 1);
  }

  @SuppressWarnings("static-method") @Test public void testWriteRangeBytes()
    throws ConstraintError
  {
    final IndexBuffer indices =
      new IndexBuffer(
        1,
        new RangeInclusive(0, 7),
        JCGLUnsignedType.TYPE_UNSIGNED_BYTE);
    final IndexBufferWritableData data =
      new IndexBufferWritableData(indices, new RangeInclusive(2, 5));

    Assert.assertEquals(2, data.getTargetDataOffset());
    Assert.assertEquals(4, data.getTargetDataSize());

    final CursorWritableIndex cursor = data.getCursor();
    cursor.putIndex(3);
    cursor.putIndex(5);
    cursor.putIndex(7);
    cursor.putIndex(11);

    final ByteBuffer buffer = data.getTargetData();
    Assert.assertTrue(3 == buffer.get(0));
    Assert.assertTrue(5 == buffer.get(1));
    Assert.assertTrue(7 == buffer.get(2));
    Assert.assertTrue(11 == buffer.get(3));
  }

  @SuppressWarnings("static-method") @Test public void testWriteRangeInts()
    throws ConstraintError
  {
    final IndexBuffer indices =
      new IndexBuffer(
        1,
        new RangeInclusive(0, 7),
        JCGLUnsignedType.TYPE_UNSIGNED_INT);
    final IndexBufferWritableData data =
      new IndexBufferWritableData(indices, new RangeInclusive(2, 5));

    Assert.assertEquals(2 * 4, data.getTargetDataOffset());
    Assert.assertEquals(4 * 4, data.getTargetDataSize());

    final CursorWritableIndex cursor = data.getCursor();
    cursor.putIndex(3);
    cursor.putIndex(5);
    cursor.putIndex(7);
    cursor.putIndex(11);

    final ByteBuffer buffer = data.getTargetData();
    Assert.assertTrue(3 == buffer.getInt(0));
    Assert.assertTrue(5 == buffer.getInt(4));
    Assert.assertTrue(7 == buffer.getInt(8));
    Assert.assertTrue(11 == buffer.getInt(12));
  }

  @SuppressWarnings("static-method") @Test public void testWriteRangeShorts()
    throws ConstraintError
  {
    final IndexBuffer indices =
      new IndexBuffer(
        1,
        new RangeInclusive(0, 7),
        JCGLUnsignedType.TYPE_UNSIGNED_SHORT);
    final IndexBufferWritableData data =
      new IndexBufferWritableData(indices, new RangeInclusive(2, 5));

    Assert.assertEquals(2 * 2, data.getTargetDataOffset());
    Assert.assertEquals(2 * 4, data.getTargetDataSize());

    final CursorWritableIndex cursor = data.getCursor();
    cursor.putIndex(3);
    cursor.putIndex(5);
    cursor.putIndex(7);
    cursor.putIndex(11);

    final ByteBuffer buffer = data.getTargetData();
    Assert.assertTrue(3 == buffer.getShort(0));
    Assert.assertTrue(5 == buffer.getShort(2));
    Assert.assertTrue(7 == buffer.getShort(4));
    Assert.assertTrue(11 == buffer.getShort(6));
  }
}
