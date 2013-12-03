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

import java.nio.FloatBuffer;
import java.util.ArrayList;

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

  private static ArrayBuffer makeArray4Color2()
    throws ConstraintError
  {
    final ArrayList<ArrayBufferAttributeDescriptor> abs =
      new ArrayList<ArrayBufferAttributeDescriptor>();
    abs.add(new ArrayBufferAttributeDescriptor(
      "color",
      JCGLScalarType.TYPE_FLOAT,
      2));

    final ArrayBufferTypeDescriptor type = new ArrayBufferTypeDescriptor(abs);
    final ArrayBuffer array = new ArrayBuffer(1, 4, type);
    return array;
  }

  private static ArrayBuffer makeArray4Color3()
    throws ConstraintError
  {
    final ArrayList<ArrayBufferAttributeDescriptor> abs =
      new ArrayList<ArrayBufferAttributeDescriptor>();
    abs.add(new ArrayBufferAttributeDescriptor(
      "color",
      JCGLScalarType.TYPE_FLOAT,
      3));
    final ArrayBufferTypeDescriptor type = new ArrayBufferTypeDescriptor(abs);
    final ArrayBuffer array = new ArrayBuffer(1, 4, type);
    return array;
  }

  private static ArrayBuffer makeArray4Color4()
    throws ConstraintError
  {
    final ArrayList<ArrayBufferAttributeDescriptor> abs =
      new ArrayList<ArrayBufferAttributeDescriptor>();
    abs.add(new ArrayBufferAttributeDescriptor(
      "color",
      JCGLScalarType.TYPE_FLOAT,
      4));
    final ArrayBufferTypeDescriptor type = new ArrayBufferTypeDescriptor(abs);
    final ArrayBuffer array = new ArrayBuffer(1, 4, type);
    return array;
  }

  /**
   * Getting a cursor of the wrong number of elements fails.
   */

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testArrayDataCursor2fNot2()
      throws ConstraintError
  {
    ArrayBufferWritableData data = null;

    try {
      final ArrayList<ArrayBufferAttributeDescriptor> abs =
        new ArrayList<ArrayBufferAttributeDescriptor>();
      abs.add(new ArrayBufferAttributeDescriptor(
        "color",
        JCGLScalarType.TYPE_FLOAT,
        4));

      final ArrayBufferTypeDescriptor type =
        new ArrayBufferTypeDescriptor(abs);
      final ArrayBuffer array = new ArrayBuffer(1, 4, type);
      data = new ArrayBufferWritableData(array);
    } catch (final ConstraintError e) {
      Assert.fail(e.getMessage());
    }

    assert data != null;
    data.getCursor2f("color");
  }

  /**
   * Getting a cursor of the wrong type fails.
   */

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testArrayDataCursor2fNotF()
      throws ConstraintError
  {
    ArrayBufferWritableData data = null;

    try {
      final ArrayList<ArrayBufferAttributeDescriptor> abs =
        new ArrayList<ArrayBufferAttributeDescriptor>();
      abs.add(new ArrayBufferAttributeDescriptor(
        "color",
        JCGLScalarType.TYPE_FLOAT,
        3));
      final ArrayBufferTypeDescriptor type =
        new ArrayBufferTypeDescriptor(abs);
      final ArrayBuffer array = new ArrayBuffer(1, 4, type);
      data = new ArrayBufferWritableData(array);
    } catch (final ConstraintError e) {
      Assert.fail(e.getMessage());
    }

    assert data != null;
    data.getCursor2f("color");
  }

  /**
   * Trying to write beyond the end of the array fails.
   */

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testArrayDataCursor2fOverflow()
      throws ConstraintError
  {
    CursorWritable2f cursor = null;

    try {
      final ArrayBuffer array =
        ArrayBufferWritableDataTest.makeArray4Color2();
      final ArrayBufferWritableData data = new ArrayBufferWritableData(array);
      cursor = data.getCursor2f("color");

      cursor.put2f(0, 1);
      cursor.put2f(2, 3);
      cursor.put2f(4, 5);
      cursor.put2f(6, 7);
    } catch (final ConstraintError e) {
      Assert.fail(e.getMessage());
    }

    assert cursor != null;
    cursor.put2f(1000, 2000);
  }

  /**
   * Getting a cursor of the wrong number of elements fails.
   */

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testArrayDataCursor3fNot3()
      throws ConstraintError
  {
    ArrayBufferWritableData data = null;

    try {
      final ArrayList<ArrayBufferAttributeDescriptor> abs =
        new ArrayList<ArrayBufferAttributeDescriptor>();
      abs.add(new ArrayBufferAttributeDescriptor(
        "color",
        JCGLScalarType.TYPE_FLOAT,
        4));
      final ArrayBufferTypeDescriptor type =
        new ArrayBufferTypeDescriptor(abs);
      final ArrayBuffer array = new ArrayBuffer(1, 4, type);
      data = new ArrayBufferWritableData(array);
    } catch (final ConstraintError e) {
      Assert.fail(e.getMessage());
    }

    assert data != null;
    data.getCursor3f("color");
  }

  /**
   * Getting a cursor of the wrong type fails.
   */

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testArrayDataCursor3fNotF()
      throws ConstraintError
  {
    ArrayBufferWritableData data = null;

    try {
      final ArrayList<ArrayBufferAttributeDescriptor> abs =
        new ArrayList<ArrayBufferAttributeDescriptor>();
      abs.add(new ArrayBufferAttributeDescriptor(
        "color",
        JCGLScalarType.TYPE_FLOAT,
        2));
      final ArrayBufferTypeDescriptor type =
        new ArrayBufferTypeDescriptor(abs);
      final ArrayBuffer array = new ArrayBuffer(1, 4, type);
      data = new ArrayBufferWritableData(array);
    } catch (final ConstraintError e) {
      Assert.fail(e.getMessage());
    }

    assert data != null;
    data.getCursor3f("color");
  }

  /**
   * Trying to write beyond the end of the array fails.
   */

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testArrayDataCursor3fOverflow()
      throws ConstraintError
  {
    CursorWritable3f cursor = null;

    try {
      final ArrayBuffer array =
        ArrayBufferWritableDataTest.makeArray4Color3();
      final ArrayBufferWritableData data = new ArrayBufferWritableData(array);
      cursor = data.getCursor3f("color");

      cursor.put3f(0, 1, 2);
      cursor.put3f(3, 4, 5);
      cursor.put3f(6, 7, 8);
      cursor.put3f(9, 10, 11);
    } catch (final ConstraintError e) {
      Assert.fail(e.getMessage());
    }

    assert cursor != null;
    cursor.put3f(1000, 2000, 3000);
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
      final ArrayList<ArrayBufferAttributeDescriptor> abs =
        new ArrayList<ArrayBufferAttributeDescriptor>();
      abs.add(new ArrayBufferAttributeDescriptor(
        "color",
        JCGLScalarType.TYPE_FLOAT,
        3));
      final ArrayBufferTypeDescriptor type =
        new ArrayBufferTypeDescriptor(abs);
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
      final ArrayList<ArrayBufferAttributeDescriptor> abs =
        new ArrayList<ArrayBufferAttributeDescriptor>();
      abs.add(new ArrayBufferAttributeDescriptor(
        "color",
        JCGLScalarType.TYPE_FLOAT,
        2));
      final ArrayBufferTypeDescriptor type =
        new ArrayBufferTypeDescriptor(abs);
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
    final FloatBuffer fb = data.getTargetData().asFloatBuffer();

    Assert.assertEquals(4 * 4 * 4, array.getSizeBytes());
    Assert.assertEquals(2 * 4 * 4, data.getTargetDataSize());
    Assert.assertEquals(4 * 4, data.getTargetDataOffset());

    cursor.put4f(4, 5, 6, 7);
    cursor.put4f(8, 9, 10, 11);

    Assert.assertEquals(0, data.getTargetData().position());

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

    Assert.assertEquals(0, data.getTargetData().position());
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
    final FloatBuffer fb = data.getTargetData().asFloatBuffer();

    Assert.assertEquals(4 * 4 * 4, array.getSizeBytes());
    Assert.assertEquals(4 * 4 * 4, data.getTargetDataSize());
    Assert.assertEquals(0, data.getTargetDataOffset());

    cursor.put4f(0, 1, 2, 3);
    cursor.put4f(4, 5, 6, 7);
    cursor.put4f(8, 9, 10, 11);
    cursor.put4f(12, 13, 14, 15);

    Assert.assertEquals(0, data.getTargetData().position());

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

    Assert.assertEquals(0, data.getTargetData().position());
  }
}
