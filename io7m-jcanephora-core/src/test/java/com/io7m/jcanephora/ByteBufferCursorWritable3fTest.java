package com.io7m.jcanephora;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;

public class ByteBufferCursorWritable3fTest
{
  @SuppressWarnings("static-method") @Test public void testWrite()
    throws ConstraintError
  {
    final int element_size = 6 * 4;
    final int element_count = 4;
    final int attribute_offset = 3 * 4;

    final ByteBuffer data =
      ByteBuffer.allocate(element_count * element_size).order(
        ByteOrder.nativeOrder());
    final ByteBufferCursorWritable3f c =
      new ByteBufferCursorWritable3f(
        data,
        attribute_offset,
        0,
        element_count - 1,
        element_size);

    c.put3f(1.0f, 2.0f, 3.0f);
    c.put3f(4.0f, 5.0f, 6.0f);
    c.put3f(7.0f, 8.0f, 9.0f);
    c.put3f(10.0f, 11.0f, 12.0f);

    final FloatBuffer fb = data.asFloatBuffer();
    Assert.assertTrue(0.0f == fb.get(0));
    Assert.assertTrue(0.0f == fb.get(1));
    Assert.assertTrue(0.0f == fb.get(2));

    Assert.assertTrue(1.0f == fb.get(3));
    Assert.assertTrue(2.0f == fb.get(4));
    Assert.assertTrue(3.0f == fb.get(5));

    Assert.assertTrue(0.0f == fb.get(6));
    Assert.assertTrue(0.0f == fb.get(7));
    Assert.assertTrue(0.0f == fb.get(8));

    Assert.assertTrue(4.0f == fb.get(9));
    Assert.assertTrue(5.0f == fb.get(10));
    Assert.assertTrue(6.0f == fb.get(11));

    Assert.assertTrue(0.0f == fb.get(12));
    Assert.assertTrue(0.0f == fb.get(13));
    Assert.assertTrue(0.0f == fb.get(14));

    Assert.assertTrue(7.0f == fb.get(15));
    Assert.assertTrue(8.0f == fb.get(16));
    Assert.assertTrue(9.0f == fb.get(17));

    Assert.assertTrue(0.0f == fb.get(18));
    Assert.assertTrue(0.0f == fb.get(19));
    Assert.assertTrue(0.0f == fb.get(20));

    Assert.assertTrue(10.0f == fb.get(21));
    Assert.assertTrue(11.0f == fb.get(22));
    Assert.assertTrue(12.0f == fb.get(23));
  }

  @SuppressWarnings("static-method") @Test public void testWriteDouble()
    throws ConstraintError
  {
    final int element_size = 6 * 4;
    final int element_count = 4;
    final int attribute_offset = 3 * 4;

    final ByteBuffer data =
      ByteBuffer.allocate(element_count * element_size).order(
        ByteOrder.nativeOrder());
    final ByteBufferCursorWritable3f c0 =
      new ByteBufferCursorWritable3f(
        data,
        attribute_offset,
        0,
        element_count - 1,
        element_size);
    final ByteBufferCursorWritable3f c1 =
      new ByteBufferCursorWritable3f(
        data,
        0,
        0,
        element_count - 1,
        element_size);

    c0.put3f(1.0f, 2.0f, 3.0f);
    c0.put3f(4.0f, 5.0f, 6.0f);
    c0.put3f(7.0f, 8.0f, 9.0f);
    c0.put3f(10.0f, 11.0f, 12.0f);

    c1.put3f(10.0f, 20.0f, 30.0f);
    c1.put3f(40.0f, 50.0f, 60.0f);
    c1.put3f(70.0f, 80.0f, 90.0f);
    c1.put3f(100.0f, 110.0f, 120.0f);

    final FloatBuffer fb = data.asFloatBuffer();
    Assert.assertTrue(10.0f == fb.get(0));
    Assert.assertTrue(20.0f == fb.get(1));
    Assert.assertTrue(30.0f == fb.get(2));

    Assert.assertTrue(1.0f == fb.get(3));
    Assert.assertTrue(2.0f == fb.get(4));
    Assert.assertTrue(3.0f == fb.get(5));

    Assert.assertTrue(40.0f == fb.get(6));
    Assert.assertTrue(50.0f == fb.get(7));
    Assert.assertTrue(60.0f == fb.get(8));

    Assert.assertTrue(4.0f == fb.get(9));
    Assert.assertTrue(5.0f == fb.get(10));
    Assert.assertTrue(6.0f == fb.get(11));

    Assert.assertTrue(70.0f == fb.get(12));
    Assert.assertTrue(80.0f == fb.get(13));
    Assert.assertTrue(90.0f == fb.get(14));

    Assert.assertTrue(7.0f == fb.get(15));
    Assert.assertTrue(8.0f == fb.get(16));
    Assert.assertTrue(9.0f == fb.get(17));

    Assert.assertTrue(100.0f == fb.get(18));
    Assert.assertTrue(110.0f == fb.get(19));
    Assert.assertTrue(120.0f == fb.get(20));

    Assert.assertTrue(10.0f == fb.get(21));
    Assert.assertTrue(11.0f == fb.get(22));
    Assert.assertTrue(12.0f == fb.get(23));
  }
}
