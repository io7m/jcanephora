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

public class ArrayBufferAttributeDescriptorTest
{
  /**
   * Creating an attribute with no elements fails.
   */

  @SuppressWarnings({ "unused", "static-method" }) @Test(
    expected = ConstraintError.class) public void testAttributeElementsZero()
    throws ConstraintError
  {
    new ArrayBufferAttributeDescriptor(
      "position",
      JCGLScalarType.TYPE_FLOAT,
      0);
  }

  /**
   * Creating an attribute without a name fails.
   */

  @SuppressWarnings({ "unused", "static-method" }) @Test(
    expected = ConstraintError.class) public void testAttributeNameNull()
    throws ConstraintError
  {
    new ArrayBufferAttributeDescriptor(null, JCGLScalarType.TYPE_FLOAT, 2);
  }

  /**
   * equals, hashCode, toString.
   */

  @SuppressWarnings({ "static-method" }) @Test public
    void
    testEqualsHashcodeToString()
  {
    ArrayBufferAttributeDescriptor a0 = null;
    ArrayBufferAttributeDescriptor a1 = null;
    ArrayBufferAttributeDescriptor a2 = null;
    ArrayBufferAttributeDescriptor a3 = null;
    ArrayBufferAttributeDescriptor a4 = null;

    try {
      a0 =
        new ArrayBufferAttributeDescriptor(
          "position",
          JCGLScalarType.TYPE_FLOAT,
          3);
      a1 =
        new ArrayBufferAttributeDescriptor(
          "position",
          JCGLScalarType.TYPE_FLOAT,
          3);
      a2 =
        new ArrayBufferAttributeDescriptor(
          "normal",
          JCGLScalarType.TYPE_FLOAT,
          3);
      a3 =
        new ArrayBufferAttributeDescriptor(
          "position",
          JCGLScalarType.TYPE_INT,
          3);
      a4 =
        new ArrayBufferAttributeDescriptor(
          "position",
          JCGLScalarType.TYPE_FLOAT,
          4);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert a0 != null;
    assert a1 != null;
    assert a2 != null;
    assert a3 != null;
    assert a4 != null;

    Assert.assertTrue(a0.equals(a0));
    Assert.assertFalse(a0.equals(null));
    Assert.assertFalse(a0.equals(Integer.valueOf(23)));
    Assert.assertTrue(a0.equals(a1));
    Assert.assertFalse(a0.equals(a2));
    Assert.assertFalse(a0.equals(a3));
    Assert.assertFalse(a0.equals(a4));

    Assert.assertTrue(a0.hashCode() == a0.hashCode());
    Assert.assertTrue(a0.hashCode() == a1.hashCode());
    Assert.assertFalse(a0.hashCode() == a2.hashCode());
    Assert.assertFalse(a0.hashCode() == a3.hashCode());
    Assert.assertFalse(a0.hashCode() == a4.hashCode());

    Assert.assertTrue(a0.toString().equals(a0.toString()));
    Assert.assertFalse(a0.toString().equals(null));
    Assert.assertTrue(a0.toString().equals(a1.toString()));
    Assert.assertFalse(a0.toString().equals(a2.toString()));
    Assert.assertFalse(a0.toString().equals(a3.toString()));
    Assert.assertFalse(a0.toString().equals(a4.toString()));
  }

  /**
   * Identities.
   */

  @SuppressWarnings("static-method") @Test public void testIdentities()
  {
    ArrayBufferAttributeDescriptor a0 = null;

    try {
      a0 =
        new ArrayBufferAttributeDescriptor(
          "position",
          JCGLScalarType.TYPE_FLOAT,
          3);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert a0 != null;
    Assert.assertEquals(a0.getName(), "position");
    Assert.assertEquals(a0.getType(), JCGLScalarType.TYPE_FLOAT);
    Assert.assertEquals(a0.getElements(), 3);
  }
}
