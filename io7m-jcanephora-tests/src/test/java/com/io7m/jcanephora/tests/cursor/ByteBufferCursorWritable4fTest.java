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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jcanephora.CursorWritable4fType;
import com.io7m.jcanephora.cursors.ByteBufferCursorWritable4f;
import com.io7m.jranges.RangeInclusiveL;

@SuppressWarnings({ "static-method", "null" }) public class ByteBufferCursorWritable4fTest
{
  @Test public void testWrite()
  {
    final int element_size = 8 * 4;
    final int element_count = 4;
    final int attribute_offset = 4 * 4;

    final ByteBuffer data =
      ByteBuffer.allocate(element_count * element_size).order(
        ByteOrder.nativeOrder());
    final RangeInclusiveL range = new RangeInclusiveL(0, element_count - 1);
    final CursorWritable4fType c =
      ByteBufferCursorWritable4f.newCursor(
        data,
        range,
        attribute_offset,
        element_size);

    c.put4f(1.0f, 2.0f, 3.0f, 4.0f);
    c.put4f(5.0f, 6.0f, 7.0f, 8.0f);
    c.put4f(9.0f, 10.0f, 11.0f, 12.0f);
    c.put4f(13.0f, 14.0f, 15.0f, 16.0f);

    final FloatBuffer fb = data.asFloatBuffer();
    Assert.assertTrue(0.0f == fb.get(0));
    Assert.assertTrue(0.0f == fb.get(1));
    Assert.assertTrue(0.0f == fb.get(2));
    Assert.assertTrue(0.0f == fb.get(3));

    Assert.assertTrue(1.0f == fb.get(4));
    Assert.assertTrue(2.0f == fb.get(5));
    Assert.assertTrue(3.0f == fb.get(6));
    Assert.assertTrue(4.0f == fb.get(7));

    Assert.assertTrue(0.0f == fb.get(8));
    Assert.assertTrue(0.0f == fb.get(9));
    Assert.assertTrue(0.0f == fb.get(10));
    Assert.assertTrue(0.0f == fb.get(11));

    Assert.assertTrue(5.0f == fb.get(12));
    Assert.assertTrue(6.0f == fb.get(13));
    Assert.assertTrue(7.0f == fb.get(14));
    Assert.assertTrue(8.0f == fb.get(15));

    Assert.assertTrue(0.0f == fb.get(16));
    Assert.assertTrue(0.0f == fb.get(17));
    Assert.assertTrue(0.0f == fb.get(18));
    Assert.assertTrue(0.0f == fb.get(19));

    Assert.assertTrue(9.0f == fb.get(20));
    Assert.assertTrue(10.0f == fb.get(21));
    Assert.assertTrue(11.0f == fb.get(22));
    Assert.assertTrue(12.0f == fb.get(23));

    Assert.assertTrue(0.0f == fb.get(24));
    Assert.assertTrue(0.0f == fb.get(25));
    Assert.assertTrue(0.0f == fb.get(26));
    Assert.assertTrue(0.0f == fb.get(27));

    Assert.assertTrue(13.0f == fb.get(28));
    Assert.assertTrue(14.0f == fb.get(29));
    Assert.assertTrue(15.0f == fb.get(30));
    Assert.assertTrue(16.0f == fb.get(31));
  }

  @Test public void testWriteDouble()
  {
    final int element_size = 8 * 4;
    final int element_count = 4;
    final int attribute_offset = 4 * 4;

    final ByteBuffer data =
      ByteBuffer.allocate(element_count * element_size).order(
        ByteOrder.nativeOrder());
    final RangeInclusiveL range = new RangeInclusiveL(0, element_count - 1);
    final CursorWritable4fType c0 =
      ByteBufferCursorWritable4f.newCursor(
        data,
        range,
        attribute_offset,
        element_size);
    final CursorWritable4fType c1 =
      ByteBufferCursorWritable4f.newCursor(data, range, 0, element_size);

    c0.put4f(1.0f, 2.0f, 3.0f, 4.0f);
    c0.put4f(5.0f, 6.0f, 7.0f, 8.0f);
    c0.put4f(9.0f, 10.0f, 11.0f, 12.0f);
    c0.put4f(13.0f, 14.0f, 15.0f, 16.0f);

    c1.put4f(10.0f, 20.0f, 30.0f, 40.0f);
    c1.put4f(50.0f, 60.0f, 70.0f, 80.0f);
    c1.put4f(90.0f, 100.0f, 110.0f, 120.0f);
    c1.put4f(130.0f, 140.0f, 150.0f, 160.0f);

    final FloatBuffer fb = data.asFloatBuffer();
    Assert.assertTrue(10.0f == fb.get(0));
    Assert.assertTrue(20.0f == fb.get(1));
    Assert.assertTrue(30.0f == fb.get(2));
    Assert.assertTrue(40.0f == fb.get(3));

    Assert.assertTrue(1.0f == fb.get(4));
    Assert.assertTrue(2.0f == fb.get(5));
    Assert.assertTrue(3.0f == fb.get(6));
    Assert.assertTrue(4.0f == fb.get(7));

    Assert.assertTrue(50.0f == fb.get(8));
    Assert.assertTrue(60.0f == fb.get(9));
    Assert.assertTrue(70.0f == fb.get(10));
    Assert.assertTrue(80.0f == fb.get(11));

    Assert.assertTrue(5.0f == fb.get(12));
    Assert.assertTrue(6.0f == fb.get(13));
    Assert.assertTrue(7.0f == fb.get(14));
    Assert.assertTrue(8.0f == fb.get(15));

    Assert.assertTrue(90.0f == fb.get(16));
    Assert.assertTrue(100.0f == fb.get(17));
    Assert.assertTrue(110.0f == fb.get(18));
    Assert.assertTrue(120.0f == fb.get(19));

    Assert.assertTrue(9.0f == fb.get(20));
    Assert.assertTrue(10.0f == fb.get(21));
    Assert.assertTrue(11.0f == fb.get(22));
    Assert.assertTrue(12.0f == fb.get(23));

    Assert.assertTrue(130.0f == fb.get(24));
    Assert.assertTrue(140.0f == fb.get(25));
    Assert.assertTrue(150.0f == fb.get(26));
    Assert.assertTrue(160.0f == fb.get(27));

    Assert.assertTrue(13.0f == fb.get(28));
    Assert.assertTrue(14.0f == fb.get(29));
    Assert.assertTrue(15.0f == fb.get(30));
    Assert.assertTrue(16.0f == fb.get(31));
  }
}
