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
import java.nio.FloatBuffer;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;
import com.io7m.jtensors.VectorM4F;

public class ByteBufferCursorReadable4fTest
{
  @SuppressWarnings("static-method") @Test public void testWrite()
    throws ConstraintError
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

    final ByteBufferCursorReadable4f r =
      new ByteBufferCursorReadable4f(data, new RangeInclusive(
        0,
        element_count - 1), attribute_offset, element_size);

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
      Assert.assertEquals(exp_x, v.x, 0.0);
      Assert.assertEquals(exp_y, v.y, 0.0);
      Assert.assertEquals(exp_z, v.z, 0.0);
      Assert.assertEquals(exp_w, v.w, 0.0);
      exp_x += 8.0f;
      exp_y += 8.0f;
      exp_z += 8.0f;
      exp_w += 8.0f;
    }

    Assert.assertFalse(r.isValid());
    Assert.assertFalse(r.hasNext());
  }
}
