package com.io7m.jcanephora;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;

public class ByteBufferCursorWritable2fTest
{
  @SuppressWarnings("static-method") @Test public void testWrite()
    throws ConstraintError
  {
    final int element_size = 4 * 4;
    final int element_count = 4;
    final int attribute_offset = 2 * 4;

    final ByteBuffer data =
      ByteBuffer.allocate(element_count * element_size).order(
        ByteOrder.nativeOrder());
    final ByteBufferCursorWritable2f c =
      new ByteBufferCursorWritable2f(data, new RangeInclusive(
        0,
        element_count - 1), attribute_offset, element_size);

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

  @SuppressWarnings("static-method") @Test public void testWriteDouble()
    throws ConstraintError
  {
    final int element_size = 4 * 4;
    final int element_count = 4;
    final int attribute_offset = 2 * 4;

    final ByteBuffer data =
      ByteBuffer.allocate(element_count * element_size).order(
        ByteOrder.nativeOrder());
    final ByteBufferCursorWritable2f c0 =
      new ByteBufferCursorWritable2f(data, new RangeInclusive(
        0,
        element_count - 1), attribute_offset, element_size);
    final ByteBufferCursorWritable2f c1 =
      new ByteBufferCursorWritable2f(data, new RangeInclusive(
        0,
        element_count - 1), 0, element_size);

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
