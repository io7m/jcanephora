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

package com.io7m.jcanephora.gpuprogram;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.JCGLType;

public class JCGPFragmentShaderOutputTest
{
  @SuppressWarnings("static-method") @Test public void testEquals()
    throws ConstraintError
  {
    final JCGPFragmentShaderOutput i0 =
      JCGPFragmentShaderOutput.make(JCGLType.TYPE_FLOAT_VECTOR_3, "name", 0);
    final JCGPFragmentShaderOutput i1 =
      JCGPFragmentShaderOutput.make(JCGLType.TYPE_FLOAT_VECTOR_4, "name", 0);
    final JCGPFragmentShaderOutput i2 =
      JCGPFragmentShaderOutput.make(
        JCGLType.TYPE_FLOAT_VECTOR_3,
        "name-other",
        0);
    final JCGPFragmentShaderOutput i3 =
      JCGPFragmentShaderOutput.make(
        JCGLType.TYPE_FLOAT_VECTOR_4,
        "name-other",
        0);
    final JCGPFragmentShaderOutput i4 =
      JCGPFragmentShaderOutput.make(JCGLType.TYPE_FLOAT_VECTOR_3, "name", 0);
    final JCGPFragmentShaderOutput i5 =
      JCGPFragmentShaderOutput.make(JCGLType.TYPE_FLOAT_VECTOR_3, "name", 1);

    Assert.assertTrue(i0.equals(i0));
    Assert.assertFalse(i0.equals(null));
    Assert.assertFalse(i0.equals(Integer.valueOf(23)));
    Assert.assertFalse(i0.equals(i1));
    Assert.assertFalse(i0.equals(i2));
    Assert.assertFalse(i0.equals(i3));
    Assert.assertFalse(i0.equals(i5));
    Assert.assertTrue(i0.equals(i4));
  }

  @SuppressWarnings("static-method") @Test public void testHashCode()
    throws ConstraintError
  {
    final JCGPFragmentShaderOutput i0 =
      JCGPFragmentShaderOutput.make(JCGLType.TYPE_FLOAT_VECTOR_3, "name", 0);
    final JCGPFragmentShaderOutput i1 =
      JCGPFragmentShaderOutput.make(JCGLType.TYPE_FLOAT_VECTOR_4, "name", 0);
    final JCGPFragmentShaderOutput i2 =
      JCGPFragmentShaderOutput.make(
        JCGLType.TYPE_FLOAT_VECTOR_3,
        "name-other",
        0);
    final JCGPFragmentShaderOutput i3 =
      JCGPFragmentShaderOutput.make(
        JCGLType.TYPE_FLOAT_VECTOR_4,
        "name-other",
        0);
    final JCGPFragmentShaderOutput i4 =
      JCGPFragmentShaderOutput.make(JCGLType.TYPE_FLOAT_VECTOR_3, "name", 0);

    Assert.assertTrue(i0.hashCode() == (i0.hashCode()));
    Assert.assertFalse(i0.hashCode() == (i1.hashCode()));
    Assert.assertFalse(i0.hashCode() == (i2.hashCode()));
    Assert.assertFalse(i0.hashCode() == (i3.hashCode()));
    Assert.assertTrue(i0.hashCode() == (i4.hashCode()));
  }

  @SuppressWarnings("static-method") @Test public void testIdentities()
    throws ConstraintError
  {
    final JCGPFragmentShaderOutput i0 =
      JCGPFragmentShaderOutput.make(JCGLType.TYPE_FLOAT_VECTOR_3, "name", 0);

    Assert.assertEquals("name", i0.getName());
    Assert.assertEquals(JCGLType.TYPE_FLOAT_VECTOR_3, i0.getType());
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testNullName()
      throws ConstraintError
  {
    JCGPFragmentShaderOutput.make(JCGLType.TYPE_FLOAT_VECTOR_3, null, 0);
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testNullType()
      throws ConstraintError
  {
    JCGPFragmentShaderOutput.make(null, "something", 0);
  }

  @SuppressWarnings("static-method") @Test public void testToString()
    throws ConstraintError
  {
    final JCGPFragmentShaderOutput i0 =
      JCGPFragmentShaderOutput.make(JCGLType.TYPE_FLOAT_VECTOR_3, "name", 0);
    final JCGPFragmentShaderOutput i1 =
      JCGPFragmentShaderOutput.make(JCGLType.TYPE_FLOAT_VECTOR_4, "name", 0);
    final JCGPFragmentShaderOutput i2 =
      JCGPFragmentShaderOutput.make(
        JCGLType.TYPE_FLOAT_VECTOR_3,
        "name-other",
        0);
    final JCGPFragmentShaderOutput i3 =
      JCGPFragmentShaderOutput.make(
        JCGLType.TYPE_FLOAT_VECTOR_4,
        "name-other",
        0);
    final JCGPFragmentShaderOutput i4 =
      JCGPFragmentShaderOutput.make(JCGLType.TYPE_FLOAT_VECTOR_3, "name", 0);

    Assert.assertTrue(i0.toString().equals(i0.toString()));
    Assert.assertFalse(i0.toString().equals(i1.toString()));
    Assert.assertFalse(i0.toString().equals(i2.toString()));
    Assert.assertFalse(i0.toString().equals(i3.toString()));
    Assert.assertTrue(i0.toString().equals(i4.toString()));
  }
}
