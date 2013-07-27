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

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;

public class ArrayBufferAttributeTest
{
  /**
   * Creating an attribute with a null array fails.
   */

  @SuppressWarnings({ "static-method", "unused" }) @Test(
    expected = ConstraintError.class) public void testNullArray()
    throws ConstraintError
  {
    ArrayBufferAttributeDescriptor descriptor = null;

    try {
      descriptor =
        new ArrayBufferAttributeDescriptor(
          "position",
          JCGLScalarType.TYPE_FLOAT,
          3);
    } catch (final ConstraintError e) {
      Assert.fail(e.getMessage());
    }

    new ArrayBufferAttribute(null, descriptor);
  }

  /**
   * Creating an attribute with a null descriptor fails.
   */

  @SuppressWarnings({ "static-method", "unused" }) @Test(
    expected = ConstraintError.class) public void testNullDescriptor()
    throws ConstraintError
  {
    ArrayBuffer array = null;
    ArrayBufferTypeDescriptor type_descriptor = null;

    try {
      type_descriptor =
        new ArrayBufferTypeDescriptor(
          new ArrayBufferAttributeDescriptor[] { new ArrayBufferAttributeDescriptor(
            "position",
            JCGLScalarType.TYPE_FLOAT,
            3) });
      array = new ArrayBuffer(1, 1, type_descriptor);
    } catch (final ConstraintError e) {
      Assert.fail(e.getMessage());
    }

    new ArrayBufferAttribute(array, null);
  }

  /**
   * Identities.
   */

  @SuppressWarnings({ "static-method" }) @Test public void testIdentities()
  {
    ArrayBuffer array = null;
    ArrayBufferTypeDescriptor type_descriptor = null;
    ArrayBufferAttributeDescriptor descriptor = null;
    ArrayBufferAttribute attribute = null;

    try {
      type_descriptor =
        new ArrayBufferTypeDescriptor(
          new ArrayBufferAttributeDescriptor[] { new ArrayBufferAttributeDescriptor(
            "position",
            JCGLScalarType.TYPE_FLOAT,
            3) });
      descriptor = type_descriptor.getAttribute("position");

      array = new ArrayBuffer(1, 1, type_descriptor);
      attribute = new ArrayBufferAttribute(array, descriptor);
    } catch (final ConstraintError e) {
      Assert.fail(e.getMessage());
    }

    assert attribute != null;
    Assert.assertTrue(attribute.getArray() == array);
    Assert.assertTrue(attribute.getDescriptor() == descriptor);
  }

  /**
   * equals, hashCode, and toString.
   */

  @SuppressWarnings({ "static-method" }) @Test public
    void
    testEqualsHashcodeToString()
  {
    ArrayBuffer array1 = null;
    ArrayBuffer array0 = null;
    ArrayBufferTypeDescriptor type_descriptor = null;
    ArrayBufferAttributeDescriptor attribute_desc0 = null;
    ArrayBufferAttributeDescriptor attribute_desc1 = null;
    ArrayBufferAttribute array0_attribute0 = null;
    ArrayBufferAttribute array1_attribute0 = null;
    ArrayBufferAttribute array0_attribute1 = null;
    ArrayBufferAttribute array0_attribute2 = null;

    try {
      type_descriptor =
        new ArrayBufferTypeDescriptor(new ArrayBufferAttributeDescriptor[] {
          new ArrayBufferAttributeDescriptor(
            "position",
            JCGLScalarType.TYPE_FLOAT,
            3),
          new ArrayBufferAttributeDescriptor(
            "normal",
            JCGLScalarType.TYPE_FLOAT,
            3) });

      attribute_desc0 = type_descriptor.getAttribute("position");
      attribute_desc1 = type_descriptor.getAttribute("normal");

      array0 = new ArrayBuffer(1, 1, type_descriptor);
      array0_attribute0 = new ArrayBufferAttribute(array0, attribute_desc0);
      array0_attribute1 = new ArrayBufferAttribute(array0, attribute_desc1);
      array0_attribute2 = new ArrayBufferAttribute(array0, attribute_desc0);

      array1 = new ArrayBuffer(2, 1, type_descriptor);
      array1_attribute0 = new ArrayBufferAttribute(array1, attribute_desc0);
    } catch (final ConstraintError e) {
      Assert.fail(e.getMessage());
    }

    assert array0_attribute0 != null;
    assert array0_attribute1 != null;
    assert array0_attribute2 != null;

    Assert.assertTrue(array0_attribute0.equals(array0_attribute0));
    Assert.assertFalse(array0_attribute0.equals(null));
    Assert.assertFalse(array0_attribute0.equals(Integer.valueOf(23)));
    Assert.assertFalse(array0_attribute0.equals(Integer.valueOf(23)));
    Assert.assertFalse(array0_attribute0.equals(array1_attribute0));
    Assert.assertFalse(array0_attribute0.equals(array0_attribute1));
    Assert.assertTrue(array0_attribute0.equals(array0_attribute2));

    Assert.assertFalse(array0_attribute0.hashCode() == array0_attribute1
      .hashCode());

    Assert.assertTrue(array0_attribute0.toString().equals(
      array0_attribute0.toString()));
    Assert.assertFalse(array0_attribute0.toString().equals(
      array0_attribute1.toString()));
    Assert.assertTrue(array0_attribute0.toString().equals(
      array0_attribute2.toString()));
  }
}
