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
import com.io7m.jcanephora.ArrayDescriptor;
import com.io7m.jcanephora.ArrayDescriptorBuilderType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionAttributeDuplicate;
import com.io7m.jcanephora.JCGLScalarType;
import com.io7m.jnull.NullCheckException;
import com.io7m.jranges.RangeCheckException;

@SuppressWarnings("static-method") public final class ArrayDescriptorTest
{
  @Test(expected = RangeCheckException.class) public void testEmpty()
  {
    final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
    b.build();
  }

  @Test(expected = NullCheckException.class) public void testNullBuilder_0()
    throws JCGLException
  {
    final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
    b.addAttribute((ArrayAttributeDescriptor) TestUtilities.actuallyNull());
  }

  @Test(expected = JCGLExceptionAttributeDuplicate.class) public
    void
    testDuplicate_0()
      throws JCGLException
  {
    final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
    b.addAttribute(ArrayAttributeDescriptor.newAttribute(
      "p",
      JCGLScalarType.TYPE_FLOAT,
      4));
    b.addAttribute(ArrayAttributeDescriptor.newAttribute(
      "p",
      JCGLScalarType.TYPE_FLOAT,
      4));
  }

  @Test public void testResults_0()
    throws JCGLException
  {
    final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
    b.addAttribute(ArrayAttributeDescriptor.newAttribute(
      "p",
      JCGLScalarType.TYPE_FLOAT,
      4));
    b.addAttribute(ArrayAttributeDescriptor.newAttribute(
      "q",
      JCGLScalarType.TYPE_FLOAT,
      4));
    b.addAttribute(ArrayAttributeDescriptor.newAttribute(
      "r",
      JCGLScalarType.TYPE_FLOAT,
      4));
    final ArrayDescriptor d = b.build();

    Assert.assertEquals(0, d.getAttributeOffset("p"));
    Assert.assertEquals(4 * 4, d.getAttributeOffset("q"));
    Assert.assertEquals((4 * 4) + (4 * 4), d.getAttributeOffset("r"));

    Assert.assertEquals(3 * (4 * 4), d.getElementSizeBytes());
  }
}
