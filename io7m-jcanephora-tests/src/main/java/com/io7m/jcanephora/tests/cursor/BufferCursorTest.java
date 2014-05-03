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

package com.io7m.jcanephora.tests.cursor;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jcanephora.cursors.BufferCursor;
import com.io7m.jranges.RangeCheckException;
import com.io7m.jranges.RangeInclusiveL;

@SuppressWarnings({ "static-method", "unused" }) public class BufferCursorTest
{
  private static final class TestCursor extends BufferCursor
  {
    protected TestCursor(
      final RangeInclusiveL in_range,
      final long in_attribute_offset,
      final long in_element_size)
    {
      super(in_range, in_attribute_offset, in_element_size);
    }
  }

  @Test public void testCanWrite()
  {
    final BufferCursor c = new TestCursor(new RangeInclusiveL(0, 3), 0, 4);

    Assert.assertEquals(0, c.getByteOffset());
    Assert.assertTrue(c.isValid());
    c.next();

    Assert.assertEquals(4, c.getByteOffset());
    Assert.assertTrue(c.isValid());
    c.next();

    Assert.assertEquals(8, c.getByteOffset());
    Assert.assertTrue(c.isValid());
    c.next();

    Assert.assertEquals(12, c.getByteOffset());
    Assert.assertTrue(c.isValid());
    Assert.assertFalse(c.hasNext());
  }

  @Test public void testCanWritePre()
  {
    final BufferCursor c = new TestCursor(new RangeInclusiveL(2, 3), 0, 4);

    Assert.assertEquals(8, c.getByteOffset());
    Assert.assertTrue(c.isValid());
    c.next();

    Assert.assertEquals(12, c.getByteOffset());
    Assert.assertTrue(c.isValid());
    Assert.assertFalse(c.hasNext());

    c.seekTo(0);
    Assert.assertTrue(c.hasNext());
    Assert.assertFalse(c.isValid());

    c.seekTo(1);
    Assert.assertTrue(c.hasNext());
    Assert.assertFalse(c.isValid());

    c.seekTo(2);
    Assert.assertTrue(c.hasNext());
    Assert.assertTrue(c.isValid());

    c.seekTo(3);
    Assert.assertFalse(c.hasNext());
    Assert.assertTrue(c.isValid());
  }

  @Test public void testHasNextBeyond()
  {
    final BufferCursor c = new TestCursor(new RangeInclusiveL(0, 0), 0, 4);
    Assert.assertFalse(c.hasNext());
  }

  @Test public void testInit0()
  {
    final BufferCursor c = new TestCursor(new RangeInclusiveL(0, 7), 0, 4);

    Assert.assertEquals(0, c.getByteOffset());
    Assert.assertEquals(0, c.getElement());
    Assert.assertTrue(c.hasNext());
    Assert.assertTrue(c.isValid());
  }

  @Test public void testInit1()
  {
    final BufferCursor c = new TestCursor(new RangeInclusiveL(1, 7), 0, 4);

    Assert.assertEquals(4, c.getByteOffset());
    Assert.assertEquals(1, c.getElement());
    Assert.assertTrue(c.hasNext());
    Assert.assertTrue(c.isValid());
  }

  @Test public void testNext()
  {
    final BufferCursor c = new TestCursor(new RangeInclusiveL(0, 7), 0, 4);

    for (int index = 0; index < 7; ++index) {
      Assert.assertTrue(c.hasNext());
      c.next();
    }
  }

  @Test public void testNextOffsets()
  {
    final BufferCursor c = new TestCursor(new RangeInclusiveL(0, 3), 0, 4);

    Assert.assertEquals(0, c.getByteOffset());
    c.next();
    Assert.assertEquals(4, c.getByteOffset());
    c.next();
    Assert.assertEquals(8, c.getByteOffset());
    c.next();
    Assert.assertEquals(12, c.getByteOffset());

    Assert.assertFalse(c.hasNext());
  }

  @Test(expected = RangeCheckException.class) public void testPrecondition0()
  {
    new TestCursor(new RangeInclusiveL(-1, 0), 0, 4);
  }

  @Test(expected = RangeCheckException.class) public void testPrecondition1()
  {
    new TestCursor(new RangeInclusiveL(0, 0), 0, 0);
  }

  @Test(expected = RangeCheckException.class) public void testPrecondition2()
  {
    new TestCursor(new RangeInclusiveL(0, 0), -1, 1);
  }

  @Test(expected = RangeCheckException.class) public void testPrecondition3()
  {
    new TestCursor(new RangeInclusiveL(0, 0), 2, 2);
  }

  @Test public void testSeekOffsets()
  {
    final BufferCursor c = new TestCursor(new RangeInclusiveL(0, 3), 0, 4);

    c.seekTo(3);
    Assert.assertEquals(12, c.getByteOffset());

    c.seekTo(1);
    Assert.assertEquals(4, c.getByteOffset());

    c.seekTo(2);
    Assert.assertEquals(8, c.getByteOffset());

    c.seekTo(0);
    Assert.assertEquals(0, c.getByteOffset());
  }
}
