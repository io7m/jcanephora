/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.tests;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jcanephora.ArrayAttributeDescriptor;
import com.io7m.jcanephora.JCGLScalarType;
import com.io7m.jnull.NullCheckException;
import com.io7m.jranges.RangeCheckException;

@SuppressWarnings("static-method") public final class ArrayAttributeDescriptorTest
{
  @Test public void testEquals()
  {
    final ArrayAttributeDescriptor a0 =
      ArrayAttributeDescriptor.newAttribute(
        "position",
        JCGLScalarType.TYPE_FLOAT,
        3);
    final ArrayAttributeDescriptor a1 =
      ArrayAttributeDescriptor.newAttribute(
        "position",
        JCGLScalarType.TYPE_FLOAT,
        3);

    Assert.assertEquals(a0, a1);
  }

  @Test(expected = RangeCheckException.class) public
    void
    testZeroComponents()
  {
    ArrayAttributeDescriptor.newAttribute(
      "position",
      JCGLScalarType.TYPE_FLOAT,
      0);
  }

  @Test(expected = NullCheckException.class) public void testNullName()
  {
    ArrayAttributeDescriptor.newAttribute(
      (String) TestUtilities.actuallyNull(),
      JCGLScalarType.TYPE_FLOAT,
      4);
  }

  @Test(expected = NullCheckException.class) public void testNullType()
  {
    ArrayAttributeDescriptor.newAttribute(
      "name",
      (JCGLScalarType) TestUtilities.actuallyNull(),
      4);
  }
}
