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

import com.io7m.jcanephora.CursorWritable2fType;
import com.io7m.jcanephora.cursors.ByteBufferCursorWritable2f;
import com.io7m.jranges.RangeInclusiveL;

@SuppressWarnings({ "static-method", "null" }) public class ByteBufferCursorWritable2fTest
{
  @Test public void testWrite()
  {
    final int element_size = 4 * 4;
    final int element_count = 4;
    final int attribute_offset = 2 * 4;

    final ByteBuffer data =
      ByteBuffer.allocate(element_count * element_size).order(
        ByteOrder.nativeOrder());
    final RangeInclusiveL range = new RangeInclusiveL(0, element_count - 1);
    final CursorWritable2fType c =
      ByteBufferCursorWritable2f.newCursor(
        data,
        range,
        attribute_offset,
        element_size);

    c.put2f(1.0f, 2.0f);
    c.put2f(3.0f, 4.0f);
    c.put2f(5.0f, 6.0f);
    c.put2f(7.0f, 8.0f);

    final FloatBuffer fb = data.asFloatBuffer();
    Assert.assertTrue(0.0f == fb.get(0));
    Assert.assertTrue(0.0f == fb.get(1));
    Assert.assertTrue(1.0f == fb.get(2));
    Assert.assertTrue(2.0f == fb.get(3));
    Assert.assertTrue(0.0f == fb.get(4));
    Assert.assertTrue(0.0f == fb.get(5));
    Assert.assertTrue(3.0f == fb.get(6));
    Assert.assertTrue(4.0f == fb.get(7));
    Assert.assertTrue(0.0f == fb.get(8));
    Assert.assertTrue(0.0f == fb.get(9));
    Assert.assertTrue(5.0f == fb.get(10));
    Assert.assertTrue(6.0f == fb.get(11));
    Assert.assertTrue(0.0f == fb.get(12));
    Assert.assertTrue(0.0f == fb.get(13));
    Assert.assertTrue(7.0f == fb.get(14));
    Assert.assertTrue(8.0f == fb.get(15));
  }

  @Test public void testWriteDouble()
  {
    final int element_size = 4 * 4;
    final int element_count = 4;
    final int attribute_offset = 2 * 4;

    final ByteBuffer data =
      ByteBuffer.allocate(element_count * element_size).order(
        ByteOrder.nativeOrder());
    final RangeInclusiveL range = new RangeInclusiveL(0, element_count - 1);
    final CursorWritable2fType c0 =
      ByteBufferCursorWritable2f.newCursor(
        data,
        range,
        attribute_offset,
        element_size);
    final CursorWritable2fType c1 =
      ByteBufferCursorWritable2f.newCursor(data, range, 0, element_size);

    c0.put2f(1.0f, 2.0f);
    c0.put2f(3.0f, 4.0f);
    c0.put2f(5.0f, 6.0f);
    c0.put2f(7.0f, 8.0f);

    c1.put2f(10.0f, 20.0f);
    c1.put2f(30.0f, 40.0f);
    c1.put2f(50.0f, 60.0f);
    c1.put2f(70.0f, 80.0f);

    final FloatBuffer fb = data.asFloatBuffer();
    Assert.assertTrue(10.0f == fb.get(0));
    Assert.assertTrue(20.0f == fb.get(1));
    Assert.assertTrue(1.0f == fb.get(2));
    Assert.assertTrue(2.0f == fb.get(3));
    Assert.assertTrue(30.0f == fb.get(4));
    Assert.assertTrue(40.0f == fb.get(5));
    Assert.assertTrue(3.0f == fb.get(6));
    Assert.assertTrue(4.0f == fb.get(7));
    Assert.assertTrue(50.0f == fb.get(8));
    Assert.assertTrue(60.0f == fb.get(9));
    Assert.assertTrue(5.0f == fb.get(10));
    Assert.assertTrue(6.0f == fb.get(11));
    Assert.assertTrue(70.0f == fb.get(12));
    Assert.assertTrue(80.0f == fb.get(13));
    Assert.assertTrue(7.0f == fb.get(14));
    Assert.assertTrue(8.0f == fb.get(15));
  }
}
