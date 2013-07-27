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

public class ArrayBufferTypeTest
{
  /**
   * Creating a type with a null array fails.
   */

  @SuppressWarnings({ "static-method", "unused" }) @Test(
    expected = ConstraintError.class) public void testNullArray()
    throws ConstraintError
  {
    ArrayBufferTypeDescriptor descriptor = null;

    try {
      descriptor =
        new ArrayBufferTypeDescriptor(
          new ArrayBufferAttributeDescriptor[] { new ArrayBufferAttributeDescriptor(
            "position",
            JCGLScalarType.TYPE_FLOAT,
            3) });
    } catch (final ConstraintError e) {
      Assert.fail(e.getMessage());
    }

    new ArrayBufferType(null, descriptor);
  }

  /**
   * Creating a type with a null descriptor fails.
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

    new ArrayBufferType(array, null);
  }

  /**
   * Identities.
   */

  @SuppressWarnings({ "static-method" }) @Test public void testIdentities()
  {
    ArrayBuffer array = null;
    ArrayBufferTypeDescriptor type_descriptor = null;
    ArrayBufferType type = null;

    try {
      type_descriptor =
        new ArrayBufferTypeDescriptor(
          new ArrayBufferAttributeDescriptor[] { new ArrayBufferAttributeDescriptor(
            "position",
            JCGLScalarType.TYPE_FLOAT,
            3) });
      array = new ArrayBuffer(1, 1, type_descriptor);
      type = new ArrayBufferType(array, type_descriptor);
    } catch (final ConstraintError e) {
      Assert.fail(e.getMessage());
    }

    assert type != null;
    Assert.assertEquals(array, type.getArrayBuffer());
    Assert.assertEquals(type_descriptor, type.getTypeDescriptor());
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
    ArrayBufferTypeDescriptor type_descriptor0 = null;
    ArrayBufferTypeDescriptor type_descriptor1 = null;
    ArrayBufferType type0 = null;
    ArrayBufferType type1 = null;
    ArrayBufferType type2 = null;
    ArrayBufferType type3 = null;

    try {
      type_descriptor0 =
        new ArrayBufferTypeDescriptor(
          new ArrayBufferAttributeDescriptor[] { new ArrayBufferAttributeDescriptor(
            "position",
            JCGLScalarType.TYPE_FLOAT,
            3) });
      type_descriptor1 =
        new ArrayBufferTypeDescriptor(
          new ArrayBufferAttributeDescriptor[] { new ArrayBufferAttributeDescriptor(
            "normal",
            JCGLScalarType.TYPE_FLOAT,
            3) });
      array0 = new ArrayBuffer(1, 1, type_descriptor0);
      array1 = new ArrayBuffer(2, 1, type_descriptor0);

      type0 = new ArrayBufferType(array0, type_descriptor0);
      type1 = new ArrayBufferType(array0, type_descriptor1);
      type2 = new ArrayBufferType(array1, type_descriptor0);
      type3 = new ArrayBufferType(array0, type_descriptor0);

    } catch (final ConstraintError e) {
      Assert.fail(e.getMessage());
    }

    assert type0 != null;
    assert type1 != null;
    assert type2 != null;
    assert type3 != null;

    Assert.assertTrue(type0.equals(type0));
    Assert.assertFalse(type0.equals(null));
    Assert.assertFalse(type0.equals(Integer.valueOf(23)));
    Assert.assertFalse(type0.equals(Integer.valueOf(23)));
    Assert.assertFalse(type0.equals(type1));
    Assert.assertFalse(type0.equals(type2));
    Assert.assertTrue(type0.equals(type3));

    Assert.assertFalse(type0.hashCode() == type1.hashCode());

    Assert.assertTrue(type0.toString().equals(type0.toString()));
    Assert.assertFalse(type0.toString().equals(type1.toString()));
    Assert.assertTrue(type0.toString().equals(type3.toString()));
  }
}
