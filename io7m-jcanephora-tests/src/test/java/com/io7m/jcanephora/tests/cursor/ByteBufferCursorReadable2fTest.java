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

import com.io7m.jcanephora.CursorReadable2fType;
import com.io7m.jcanephora.cursors.ByteBufferCursorReadable2f;
import com.io7m.jranges.RangeCheckException;
import com.io7m.jranges.RangeInclusiveL;
import com.io7m.jtensors.VectorM2F;
import com.io7m.jtensors.VectorWritable2FType;

@SuppressWarnings("static-method") public class ByteBufferCursorReadable2fTest
{
  @Test(expected = RangeCheckException.class) public
    void
    testOneByteTooSmall()
  {
    final ByteBuffer data =
      ByteBuffer.allocate(71).order(ByteOrder.nativeOrder());
    assert data != null;

    final CursorReadable2fType r =
      ByteBufferCursorReadable2f.newCursor(
        data,
        new RangeInclusiveL(0, 9),
        0,
        8);

    final VectorWritable2FType v = new VectorM2F();
    r.get2f(v);
  }

  @Test public void testWrite()
  {
    final int float_bytes = 4;
    final int element_components = 4;
    final int element_size = element_components * float_bytes;
    final int element_count = 4;
    final int attribute_offset = 2 * float_bytes;

    final ByteBuffer data =
      ByteBuffer.allocate(element_count * element_size).order(
        ByteOrder.nativeOrder());

    final FloatBuffer fb = data.asFloatBuffer();

    for (int index = 0; index < element_size; ++index) {
      fb.put(index, index);
    }

    for (int index = 0; index < element_size; index += 4) {
      System.out.print("buffer " + index + " : ");
      System.out.print(fb.get(index));
      System.out.print(" ");
      System.out.print(fb.get(index + 1));
      System.out.print(" ");
      System.out.print(fb.get(index + 2));
      System.out.print(" ");
      System.out.print(fb.get(index + 3));
      System.out.print(" ");
      System.out.println();
    }

    final RangeInclusiveL range = new RangeInclusiveL(0, element_count - 1);
    final CursorReadable2fType r =
      ByteBufferCursorReadable2f.newCursor(
        data,
        range,
        attribute_offset,
        element_size);

    final VectorM2F v = new VectorM2F();
    float exp_x = 2.0f;
    float exp_y = 3.0f;
    for (int index = 0; index < element_count; ++index) {
      Assert.assertTrue(r.isValid());
      r.get2f(v);
      System.out.println(index + " 0 : " + v);
      System.out.println("exp_x : " + exp_x);
      System.out.println("exp_y : " + exp_y);
      Assert.assertEquals(exp_x, v.getXF(), 0.0);
      Assert.assertEquals(exp_y, v.getYF(), 0.0);
      exp_x += 4.0f;
      exp_y += 4.0f;
    }

    Assert.assertFalse(r.isValid());
    Assert.assertFalse(r.hasNext());
  }
}
