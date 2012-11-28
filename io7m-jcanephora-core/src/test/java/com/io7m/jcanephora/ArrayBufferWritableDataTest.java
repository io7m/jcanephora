package com.io7m.jcanephora;

import java.nio.FloatBuffer;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;

public class ArrayBufferWritableDataTest
{
  static void dumpFloatData(
    final FloatBuffer data)
  {
    final int size = data.capacity();

    System.out.print(data + ": ");
    for (int index = 0; index < size; ++index) {
      final float x = data.get(index);
      System.out.print(String.format("%f ", Float.valueOf(x)));
    }
    System.out.println();
  }

  private static ArrayBuffer makeArray4Color4()
    throws ConstraintError
  {
    final ArrayBufferAttribute[] ab = new ArrayBufferAttribute[1];
    ab[0] = new ArrayBufferAttribute("color", GLScalarType.TYPE_FLOAT, 4);
    final ArrayBufferDescriptor type = new ArrayBufferDescriptor(ab);
    final ArrayBuffer array = new ArrayBuffer(1, 4, type);
    return array;
  }

  /**
   * Getting a cursor of the wrong number of elements fails.
   */

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testArrayDataCursor4fNot4()
      throws ConstraintError
  {
    ArrayBufferWritableData data = null;

    try {
      final ArrayBufferAttribute[] ab = new ArrayBufferAttribute[1];
      ab[0] = new ArrayBufferAttribute("color", GLScalarType.TYPE_FLOAT, 3);
      final ArrayBufferDescriptor type = new ArrayBufferDescriptor(ab);
      final ArrayBuffer array = new ArrayBuffer(1, 4, type);
      data = new ArrayBufferWritableData(array);
    } catch (final ConstraintError e) {
      Assert.fail(e.getMessage());
    }

    assert data != null;
    data.getCursor4f("color");
  }

  /**
   * Getting a cursor of the wrong type fails.
   */

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testArrayDataCursor4fNotF()
      throws ConstraintError
  {
    ArrayBufferWritableData data = null;

    try {
      final ArrayBufferAttribute[] ab = new ArrayBufferAttribute[1];
      ab[0] = new ArrayBufferAttribute("color", GLScalarType.TYPE_INT, 4);
      final ArrayBufferDescriptor type = new ArrayBufferDescriptor(ab);
      final ArrayBuffer array = new ArrayBuffer(1, 4, type);
      data = new ArrayBufferWritableData(array);
    } catch (final ConstraintError e) {
      Assert.fail(e.getMessage());
    }

    assert data != null;
    data.getCursor4f("color");
  }

  /**
   * Trying to write beyond the end of the array fails.
   */

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testArrayDataCursor4fOverflow()
      throws ConstraintError
  {
    CursorWritable4f cursor = null;

    try {
      final ArrayBuffer array =
        ArrayBufferWritableDataTest.makeArray4Color4();
      final ArrayBufferWritableData data = new ArrayBufferWritableData(array);
      cursor = data.getCursor4f("color");

      cursor.put4f(0, 1, 2, 3);
      cursor.put4f(4, 5, 6, 7);
      cursor.put4f(8, 9, 10, 11);
      cursor.put4f(12, 13, 14, 15);
    } catch (final ConstraintError e) {
      Assert.fail(e.getMessage());
    }

    assert cursor != null;
    cursor.put4f(1000, 2000, 3000, 4000);
  }

  /**
   * Partial updates work.
   */

  @SuppressWarnings("static-method") @Test public void testArrayDataPartial()
    throws ConstraintError
  {
    final ArrayBuffer array = ArrayBufferWritableDataTest.makeArray4Color4();
    final ArrayBufferWritableData data =
      new ArrayBufferWritableData(array, new RangeInclusive(1, 2));
    final CursorWritable4f cursor = data.getCursor4f("color");
    final FloatBuffer fb = data.target_data.asFloatBuffer();

    Assert.assertEquals(4 * 4 * 4, array.getSizeBytes());
    Assert.assertEquals(2 * 4 * 4, data.getTargetDataSize());
    Assert.assertEquals(4 * 4, data.getTargetDataOffset());

    cursor.put4f(4, 5, 6, 7);
    cursor.put4f(8, 9, 10, 11);

    Assert.assertEquals(0, data.target_data.position());

    {
      final float x = fb.get(0);
      final float y = fb.get(1);
      final float z = fb.get(2);
      final float w = fb.get(3);
      Assert.assertTrue(x == 4.0f);
      Assert.assertTrue(y == 5.0f);
      Assert.assertTrue(z == 6.0f);
      Assert.assertTrue(w == 7.0f);
    }

    {
      final float x = fb.get(4);
      final float y = fb.get(5);
      final float z = fb.get(6);
      final float w = fb.get(7);
      Assert.assertTrue(x == 8.0f);
      Assert.assertTrue(y == 9.0f);
      Assert.assertTrue(z == 10.0f);
      Assert.assertTrue(w == 11.0f);
    }

    Assert.assertEquals(0, data.target_data.position());
  }

  /**
   * An invalid range for the ArrayWritableData constructor fails.
   */

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testArrayDataPartialIllegalCase0()
      throws ConstraintError
  {
    final ArrayBuffer array = ArrayBufferWritableDataTest.makeArray4Color4();
    @SuppressWarnings("unused") final ArrayBufferWritableData data =
      new ArrayBufferWritableData(array, new RangeInclusive(0, 5));
  }

  /**
   * An invalid range for the ArrayWritableData constructor fails.
   */

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testArrayDataPartialIllegalCase1()
      throws ConstraintError
  {
    final ArrayBuffer array = ArrayBufferWritableDataTest.makeArray4Color4();
    @SuppressWarnings("unused") final ArrayBufferWritableData data =
      new ArrayBufferWritableData(array, new RangeInclusive(-1, 3));
  }

  /**
   * Complete updates work.
   */

  @SuppressWarnings("static-method") @Test public void testArrayDataSimple()
    throws ConstraintError
  {
    final ArrayBuffer array = ArrayBufferWritableDataTest.makeArray4Color4();
    final ArrayBufferWritableData data = new ArrayBufferWritableData(array);
    final CursorWritable4f cursor = data.getCursor4f("color");
    final FloatBuffer fb = data.target_data.asFloatBuffer();

    Assert.assertEquals(4 * 4 * 4, array.getSizeBytes());
    Assert.assertEquals(4 * 4 * 4, data.getTargetDataSize());
    Assert.assertEquals(0, data.getTargetDataOffset());

    cursor.put4f(0, 1, 2, 3);
    cursor.put4f(4, 5, 6, 7);
    cursor.put4f(8, 9, 10, 11);
    cursor.put4f(12, 13, 14, 15);

    Assert.assertEquals(0, data.target_data.position());

    {
      final float x = fb.get(0);
      final float y = fb.get(1);
      final float z = fb.get(2);
      final float w = fb.get(3);
      Assert.assertTrue(x == 0.0f);
      Assert.assertTrue(y == 1.0f);
      Assert.assertTrue(z == 2.0f);
      Assert.assertTrue(w == 3.0f);
    }

    {
      final float x = fb.get(4);
      final float y = fb.get(5);
      final float z = fb.get(6);
      final float w = fb.get(7);
      Assert.assertTrue(x == 4.0f);
      Assert.assertTrue(y == 5.0f);
      Assert.assertTrue(z == 6.0f);
      Assert.assertTrue(w == 7.0f);
    }

    {
      final float x = fb.get(8);
      final float y = fb.get(9);
      final float z = fb.get(10);
      final float w = fb.get(11);
      Assert.assertTrue(x == 8.0f);
      Assert.assertTrue(y == 9.0f);
      Assert.assertTrue(z == 10.0f);
      Assert.assertTrue(w == 11.0f);
    }

    {
      final float x = fb.get(12);
      final float y = fb.get(13);
      final float z = fb.get(14);
      final float w = fb.get(15);
      Assert.assertTrue(x == 12.0f);
      Assert.assertTrue(y == 13.0f);
      Assert.assertTrue(z == 14.0f);
      Assert.assertTrue(w == 15.0f);
    }

    Assert.assertEquals(0, data.target_data.position());
  }
}
