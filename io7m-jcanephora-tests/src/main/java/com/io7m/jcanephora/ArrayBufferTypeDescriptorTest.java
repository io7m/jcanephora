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

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;

public class ArrayBufferTypeDescriptorTest
{
  /**
   * Trying to pass an attribute without any elements fails.
   */

  @SuppressWarnings({ "unused", "static-method" }) @Test(
    expected = ConstraintError.class) public
    void
    testDescriptorAttributeElementsZero()
      throws ConstraintError
  {
    final ArrayList<ArrayBufferAttributeDescriptor> abs =
      new ArrayList<ArrayBufferAttributeDescriptor>();
    abs.add(new ArrayBufferAttributeDescriptor(
      "position",
      JCGLScalarType.TYPE_FLOAT,
      0));
    new ArrayBufferTypeDescriptor(abs);
  }

  /**
   * Trying to pass an attribute without a name fails.
   */

  @SuppressWarnings({ "unused", "static-method" }) @Test(
    expected = ConstraintError.class) public
    void
    testDescriptorAttributeNameNull()
      throws ConstraintError
  {
    final ArrayList<ArrayBufferAttributeDescriptor> abs =
      new ArrayList<ArrayBufferAttributeDescriptor>();
    abs.add(new ArrayBufferAttributeDescriptor(
      null,
      JCGLScalarType.TYPE_FLOAT,
      3));
    new ArrayBufferTypeDescriptor(abs);
  }

  /**
   * Trying to pass two attributes with the same name fails.
   */

  @SuppressWarnings({ "unused", "static-method" }) @Test(
    expected = ConstraintError.class) public
    void
    testDescriptorAttributeNamesNotUnique()
      throws ConstraintError
  {
    ArrayBufferAttributeDescriptor ap = null;
    ArrayBufferAttributeDescriptor an = null;

    try {
      ap =
        new ArrayBufferAttributeDescriptor(
          "position",
          JCGLScalarType.TYPE_FLOAT,
          3);
      an =
        new ArrayBufferAttributeDescriptor(
          "position",
          JCGLScalarType.TYPE_INT,
          4);
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    final ArrayList<ArrayBufferAttributeDescriptor> abs =
      new ArrayList<ArrayBufferAttributeDescriptor>();
    abs.add(ap);
    abs.add(an);
    new ArrayBufferTypeDescriptor(abs);
  }

  /**
   * Sizes, offsets, and types are correct.
   */

  @SuppressWarnings("static-method") @Test public
    void
    testDescriptorAttributeOffsetsSizes()
      throws ConstraintError
  {
    final ArrayBufferAttributeDescriptor ap =
      new ArrayBufferAttributeDescriptor(
        "position",
        JCGLScalarType.TYPE_FLOAT,
        3);
    final ArrayBufferAttributeDescriptor an =
      new ArrayBufferAttributeDescriptor(
        "normal",
        JCGLScalarType.TYPE_FLOAT,
        3);
    final ArrayBufferAttributeDescriptor au =
      new ArrayBufferAttributeDescriptor("uv", JCGLScalarType.TYPE_FLOAT, 2);

    final ArrayList<ArrayBufferAttributeDescriptor> abs =
      new ArrayList<ArrayBufferAttributeDescriptor>();
    abs.add(ap);
    abs.add(an);
    abs.add(au);

    final ArrayBufferTypeDescriptor a = new ArrayBufferTypeDescriptor(abs);

    Assert.assertEquals(0, a.getAttributeOffset("position"));
    Assert.assertEquals(
      JCGLScalarType.TYPE_FLOAT,
      a.getAttributeType("position"));
    Assert.assertEquals(3, a.getAttributeElements("position"));
    Assert.assertEquals(0, a.getElementOffset("position", 0));
    Assert.assertEquals(4, a.getElementOffset("position", 1));
    Assert.assertEquals(8, a.getElementOffset("position", 2));

    Assert.assertEquals(3 * 4, a.getAttributeOffset("normal"));
    Assert.assertEquals(
      JCGLScalarType.TYPE_FLOAT,
      a.getAttributeType("normal"));
    Assert.assertEquals(3, a.getAttributeElements("normal"));
    Assert.assertEquals((3 * 4) + 0, a.getElementOffset("normal", 0));
    Assert.assertEquals((3 * 4) + 4, a.getElementOffset("normal", 1));
    Assert.assertEquals((3 * 4) + 8, a.getElementOffset("normal", 2));

    Assert.assertEquals(6 * 4, a.getAttributeOffset("uv"));
    Assert.assertEquals(JCGLScalarType.TYPE_FLOAT, a.getAttributeType("uv"));
    Assert.assertEquals(2, a.getAttributeElements("uv"));
    Assert.assertEquals((6 * 4) + 0, a.getElementOffset("uv", 0));
    Assert.assertEquals((6 * 4) + 4, a.getElementOffset("uv", 1));

    Assert.assertEquals(8 * 4, a.getSize());
  }

  /**
   * Trying to pass an null array of attributes fails.
   */

  @SuppressWarnings({ "unused", "static-method" }) @Test(
    expected = ConstraintError.class) public
    void
    testDescriptorAttributesNull()
      throws ConstraintError
  {
    new ArrayBufferTypeDescriptor(null);
  }

  /**
   * Trying to pass an array of null attributes fails.
   */

  @SuppressWarnings({ "unused", "static-method" }) @Test(
    expected = ConstraintError.class) public
    void
    testDescriptorAttributesNullElements()
      throws ConstraintError
  {
    final ArrayList<ArrayBufferAttributeDescriptor> abs =
      new ArrayList<ArrayBufferAttributeDescriptor>();
    abs.add(null);
    new ArrayBufferTypeDescriptor(abs);
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testDescriptorElementOffsetOutOfRange()
      throws ConstraintError
  {
    ArrayBufferTypeDescriptor a = null;

    try {
      final ArrayList<ArrayBufferAttributeDescriptor> abs =
        new ArrayList<ArrayBufferAttributeDescriptor>();

      final ArrayBufferAttributeDescriptor ap =
        new ArrayBufferAttributeDescriptor(
          "position",
          JCGLScalarType.TYPE_FLOAT,
          3);

      abs.add(ap);

      a = new ArrayBufferTypeDescriptor(abs);
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    assert a != null;
    a.getElementOffset("position", 3);
  }

  /**
   * Trying to declare an array buffer descriptor without any elements fails.
   */

  @SuppressWarnings({ "unused", "static-method" }) @Test(
    expected = ConstraintError.class) public void testDescriptorNone()
    throws ConstraintError
  {
    final ArrayList<ArrayBufferAttributeDescriptor> abs =
      new ArrayList<ArrayBufferAttributeDescriptor>();
    new ArrayBufferTypeDescriptor(abs);
  }

  @SuppressWarnings({ "static-method" }) @Test public void testEquals()
    throws ConstraintError
  {
    final ArrayList<ArrayBufferAttributeDescriptor> abs =
      new ArrayList<ArrayBufferAttributeDescriptor>();
    abs.add(new ArrayBufferAttributeDescriptor(
      "position",
      JCGLScalarType.TYPE_FLOAT,
      3));
    final ArrayBufferTypeDescriptor as0 = new ArrayBufferTypeDescriptor(abs);

    abs.clear();
    abs.add(new ArrayBufferAttributeDescriptor(
      "position",
      JCGLScalarType.TYPE_FLOAT,
      3));
    final ArrayBufferTypeDescriptor as1 = new ArrayBufferTypeDescriptor(abs);

    abs.clear();
    abs.add(new ArrayBufferAttributeDescriptor(
      "normal",
      JCGLScalarType.TYPE_FLOAT,
      3));
    final ArrayBufferTypeDescriptor as2 = new ArrayBufferTypeDescriptor(abs);

    Assert.assertTrue(as0.equals(as0));
    Assert.assertTrue(as0.equals(as1));
    Assert.assertFalse(as0.equals(null));
    Assert.assertFalse(as0.equals(Integer.valueOf(23)));
    Assert.assertFalse(as0.equals(as2));
  }

  @SuppressWarnings({ "static-method" }) @Test public void testHashcode()
    throws ConstraintError
  {
    final ArrayList<ArrayBufferAttributeDescriptor> abs =
      new ArrayList<ArrayBufferAttributeDescriptor>();
    abs.add(new ArrayBufferAttributeDescriptor(
      "position",
      JCGLScalarType.TYPE_FLOAT,
      3));
    final ArrayBufferTypeDescriptor as0 = new ArrayBufferTypeDescriptor(abs);

    abs.clear();
    abs.add(new ArrayBufferAttributeDescriptor(
      "position",
      JCGLScalarType.TYPE_FLOAT,
      3));
    final ArrayBufferTypeDescriptor as1 = new ArrayBufferTypeDescriptor(abs);

    abs.clear();
    abs.add(new ArrayBufferAttributeDescriptor(
      "normal",
      JCGLScalarType.TYPE_FLOAT,
      3));
    final ArrayBufferTypeDescriptor as2 = new ArrayBufferTypeDescriptor(abs);

    Assert.assertTrue(as0.hashCode() == (as0.hashCode()));
    Assert.assertTrue(as0.hashCode() == (as1.hashCode()));
    Assert.assertFalse(as0.hashCode() == (as2.hashCode()));
  }

  @SuppressWarnings({ "static-method" }) @Test public void testToString()
    throws ConstraintError
  {
    final ArrayList<ArrayBufferAttributeDescriptor> abs =
      new ArrayList<ArrayBufferAttributeDescriptor>();
    abs.add(new ArrayBufferAttributeDescriptor(
      "position",
      JCGLScalarType.TYPE_FLOAT,
      3));
    final ArrayBufferTypeDescriptor as0 = new ArrayBufferTypeDescriptor(abs);

    abs.clear();
    abs.add(new ArrayBufferAttributeDescriptor(
      "position",
      JCGLScalarType.TYPE_FLOAT,
      3));
    final ArrayBufferTypeDescriptor as1 = new ArrayBufferTypeDescriptor(abs);

    abs.clear();
    abs.add(new ArrayBufferAttributeDescriptor(
      "normal",
      JCGLScalarType.TYPE_FLOAT,
      3));
    final ArrayBufferTypeDescriptor as2 = new ArrayBufferTypeDescriptor(abs);

    Assert.assertTrue(as0.toString().equals(as0.toString()));
    Assert.assertTrue(as0.toString().equals(as1.toString()));
    Assert.assertFalse(as0.toString().equals(as2.toString()));
  }
}
