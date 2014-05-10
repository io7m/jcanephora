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
import java.nio.FloatBuffer;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jcanephora.CursorReadable4fType;
import com.io7m.jcanephora.cursors.ByteBufferCursorReadable4f;
import com.io7m.jranges.RangeCheckException;
import com.io7m.jranges.RangeInclusiveL;
import com.io7m.jtensors.VectorM4F;
import com.io7m.jtensors.VectorWritable4FType;

@SuppressWarnings("static-method") public class ByteBufferCursorReadable4fTest
{
  @Test(expected = RangeCheckException.class) public
    void
    testOneByteTooSmall()
  {
    final ByteBuffer data =
      ByteBuffer.allocate(143).order(ByteOrder.nativeOrder());
    assert data != null;

    final CursorReadable4fType r =
      ByteBufferCursorReadable4f.newCursor(
        data,
        new RangeInclusiveL(0, 9),
        0,
        16);

    final VectorWritable4FType v = new VectorM4F();
    r.get4f(v);
  }

  @Test public void testWrite()
  {
    final int float_bytes = 4;
    final int element_components = 8;
    final int element_size = element_components * float_bytes;
    final int element_count = 4;
    final int attribute_offset = 4 * float_bytes;

    final ByteBuffer data =
      ByteBuffer.allocate(element_count * element_size).order(
        ByteOrder.nativeOrder());

    final FloatBuffer fb = data.asFloatBuffer();

    for (int index = 0; index < element_size; ++index) {
      fb.put(index, index);
    }

    for (int index = 0; index < element_size; index += element_components) {
      System.out.print("buffer " + index + " : ");
      System.out.print(fb.get(index));
      System.out.print(" ");
      System.out.print(fb.get(index + 1));
      System.out.print(" ");
      System.out.print(fb.get(index + 2));
      System.out.print(" ");
      System.out.print(fb.get(index + 3));
      System.out.print(" ");
      System.out.print(fb.get(index + 4));
      System.out.print(" ");
      System.out.print(fb.get(index + 5));
      System.out.print(" ");
      System.out.print(fb.get(index + 6));
      System.out.print(" ");
      System.out.print(fb.get(index + 7));
      System.out.print(" ");
      System.out.println();
    }

    final RangeInclusiveL range = new RangeInclusiveL(0, element_count - 1);
    final CursorReadable4fType r =
      ByteBufferCursorReadable4f.newCursor(
        data,
        range,
        attribute_offset,
        element_size);

    final VectorM4F v = new VectorM4F();

    float exp_x = 4.0f;
    float exp_y = 5.0f;
    float exp_z = 6.0f;
    float exp_w = 7.0f;
    for (int index = 0; index < element_count; ++index) {
      Assert.assertTrue(r.isValid());
      r.get4f(v);
      System.out.println(index + " 0 : " + v);
      System.out.println("exp_x : " + exp_x);
      System.out.println("exp_y : " + exp_y);
      System.out.println("exp_z : " + exp_z);
      System.out.println("exp_w : " + exp_w);
      Assert.assertEquals(exp_x, v.getXF(), 0.0);
      Assert.assertEquals(exp_y, v.getYF(), 0.0);
      Assert.assertEquals(exp_z, v.getZF(), 0.0);
      Assert.assertEquals(exp_w, v.getWF(), 0.0);
      exp_x += 8.0f;
      exp_y += 8.0f;
      exp_z += 8.0f;
      exp_w += 8.0f;
    }

    Assert.assertFalse(r.isValid());
    Assert.assertFalse(r.hasNext());
  }
}
