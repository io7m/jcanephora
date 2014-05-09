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

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jcanephora.ArrayAttributeDescriptor;
import com.io7m.jcanephora.ArrayBufferType;
import com.io7m.jcanephora.ArrayBufferUpdateUnmapped;
import com.io7m.jcanephora.ArrayBufferUpdateUnmappedType;
import com.io7m.jcanephora.ArrayDescriptor;
import com.io7m.jcanephora.ArrayDescriptorBuilderType;
import com.io7m.jcanephora.CursorWritable2fType;
import com.io7m.jcanephora.CursorWritable3fType;
import com.io7m.jcanephora.CursorWritable4fType;
import com.io7m.jcanephora.JCGLExceptionAttributeDuplicate;
import com.io7m.jcanephora.JCGLExceptionAttributeMissing;
import com.io7m.jcanephora.JCGLExceptionDeleted;
import com.io7m.jcanephora.JCGLExceptionTypeError;
import com.io7m.jcanephora.JCGLScalarType;
import com.io7m.jcanephora.UsageHint;
import com.io7m.jcanephora.tests.types.PseudoArray;
import com.io7m.jnull.NullCheckException;
import com.io7m.jranges.RangeCheckException;
import com.io7m.jranges.RangeInclusiveL;
import com.io7m.junreachable.UnreachableCodeException;

@SuppressWarnings({ "null", "static-method" }) public final class ArrayBufferUpdateUnmappedTest
{
  private PseudoArray makePseudoArray()
  {
    try {
      final ArrayDescriptorBuilderType ab = ArrayDescriptor.newBuilder();
      ab.addAttribute(ArrayAttributeDescriptor.newAttribute(
        "p",
        JCGLScalarType.TYPE_FLOAT,
        3));
      ab.addAttribute(ArrayAttributeDescriptor.newAttribute(
        "n",
        JCGLScalarType.TYPE_INT,
        3));
      ab.addAttribute(ArrayAttributeDescriptor.newAttribute(
        "u",
        JCGLScalarType.TYPE_FLOAT,
        2));
      ab.addAttribute(ArrayAttributeDescriptor.newAttribute(
        "t",
        JCGLScalarType.TYPE_FLOAT,
        4));

      final ArrayDescriptor ad = ab.build();
      final RangeInclusiveL arange = new RangeInclusiveL(0, 9);

      final PseudoArray a =
        new PseudoArray(ad, arange, UsageHint.USAGE_STATIC_DRAW);

      return a;
    } catch (final JCGLExceptionAttributeDuplicate x) {
      throw new UnreachableCodeException(x);
    }
  }

  @Test(expected = JCGLExceptionTypeError.class) public final
    void
    testArrayBufferCursor2f_0()
      throws RangeCheckException,
        JCGLExceptionDeleted,
        JCGLExceptionAttributeMissing,
        JCGLExceptionTypeError
  {
    final PseudoArray a = this.makePseudoArray();
    final RangeInclusiveL range = new RangeInclusiveL(1, 9);
    final ArrayBufferUpdateUnmappedType u =
      ArrayBufferUpdateUnmapped.newUpdateReplacingRange(a, range);

    u.getCursor2f("p");
  }

  @Test(expected = JCGLExceptionAttributeMissing.class) public final
    void
    testArrayBufferCursor2f_1()
      throws RangeCheckException,
        JCGLExceptionDeleted,
        JCGLExceptionAttributeMissing,
        JCGLExceptionTypeError
  {
    final PseudoArray a = this.makePseudoArray();
    final RangeInclusiveL range = new RangeInclusiveL(1, 9);
    final ArrayBufferUpdateUnmappedType u =
      ArrayBufferUpdateUnmapped.newUpdateReplacingRange(a, range);

    u.getCursor2f("q");
  }

  @Test public final void testArrayBufferCursor2f_2()
    throws RangeCheckException,
      JCGLExceptionDeleted,
      JCGLExceptionAttributeMissing,
      JCGLExceptionTypeError
  {
    final PseudoArray a = this.makePseudoArray();
    final RangeInclusiveL range = new RangeInclusiveL(1, 9);
    final ArrayBufferUpdateUnmappedType u =
      ArrayBufferUpdateUnmapped.newUpdateReplacingRange(a, range);

    final CursorWritable2fType c = u.getCursor2f("u");
    c.put2f(0.0f, 1.0f);
  }

  @Test(expected = JCGLExceptionTypeError.class) public final
    void
    testArrayBufferCursor3f_0()
      throws RangeCheckException,
        JCGLExceptionDeleted,
        JCGLExceptionAttributeMissing,
        JCGLExceptionTypeError
  {
    final PseudoArray a = this.makePseudoArray();
    final RangeInclusiveL range = new RangeInclusiveL(1, 9);
    final ArrayBufferUpdateUnmappedType u =
      ArrayBufferUpdateUnmapped.newUpdateReplacingRange(a, range);

    u.getCursor3f("u");
  }

  @Test(expected = JCGLExceptionAttributeMissing.class) public final
    void
    testArrayBufferCursor3f_1()
      throws RangeCheckException,
        JCGLExceptionDeleted,
        JCGLExceptionAttributeMissing,
        JCGLExceptionTypeError
  {
    final PseudoArray a = this.makePseudoArray();
    final RangeInclusiveL range = new RangeInclusiveL(1, 9);
    final ArrayBufferUpdateUnmappedType u =
      ArrayBufferUpdateUnmapped.newUpdateReplacingRange(a, range);

    u.getCursor3f("q");
  }

  @Test public final void testArrayBufferCursor3f_2()
    throws RangeCheckException,
      JCGLExceptionDeleted,
      JCGLExceptionAttributeMissing,
      JCGLExceptionTypeError
  {
    final PseudoArray a = this.makePseudoArray();
    final RangeInclusiveL range = new RangeInclusiveL(1, 9);
    final ArrayBufferUpdateUnmappedType u =
      ArrayBufferUpdateUnmapped.newUpdateReplacingRange(a, range);

    final CursorWritable3fType c = u.getCursor3f("p");
    c.put3f(0.0f, 1.0f, 2.0f);
  }

  @Test(expected = JCGLExceptionTypeError.class) public final
    void
    testArrayBufferCursor4f_0()
      throws RangeCheckException,
        JCGLExceptionDeleted,
        JCGLExceptionAttributeMissing,
        JCGLExceptionTypeError
  {
    final PseudoArray a = this.makePseudoArray();
    final RangeInclusiveL range = new RangeInclusiveL(1, 9);
    final ArrayBufferUpdateUnmappedType u =
      ArrayBufferUpdateUnmapped.newUpdateReplacingRange(a, range);

    u.getCursor4f("u");
  }

  @Test(expected = JCGLExceptionAttributeMissing.class) public final
    void
    testArrayBufferCursor4f_1()
      throws RangeCheckException,
        JCGLExceptionDeleted,
        JCGLExceptionAttributeMissing,
        JCGLExceptionTypeError
  {
    final PseudoArray a = this.makePseudoArray();
    final RangeInclusiveL range = new RangeInclusiveL(1, 9);
    final ArrayBufferUpdateUnmappedType u =
      ArrayBufferUpdateUnmapped.newUpdateReplacingRange(a, range);

    u.getCursor4f("q");
  }

  @Test public final void testArrayBufferCursor4f_2()
    throws RangeCheckException,
      JCGLExceptionDeleted,
      JCGLExceptionAttributeMissing,
      JCGLExceptionTypeError
  {
    final PseudoArray a = this.makePseudoArray();
    final RangeInclusiveL range = new RangeInclusiveL(1, 9);
    final ArrayBufferUpdateUnmappedType u =
      ArrayBufferUpdateUnmapped.newUpdateReplacingRange(a, range);

    final CursorWritable4fType c = u.getCursor4f("t");
    c.put4f(0.0f, 1.0f, 2.0f, 3.0f);
  }

  @Test(expected = JCGLExceptionDeleted.class) public final
    void
    testArrayBufferUpdateDeleted_0()
      throws RangeCheckException,
        JCGLExceptionDeleted
  {
    final PseudoArray a = this.makePseudoArray();
    final RangeInclusiveL range = new RangeInclusiveL(0, 9);

    a.setDeleted(true);
    ArrayBufferUpdateUnmapped.newUpdateReplacingRange(a, range);
  }

  @Test(expected = NullCheckException.class) public final
    void
    testArrayBufferUpdateNull_0()
      throws JCGLExceptionDeleted
  {
    ArrayBufferUpdateUnmapped
      .newUpdateReplacingAll((ArrayBufferType) TestUtilities.actuallyNull());
  }

  @Test(expected = NullCheckException.class) public final
    void
    testArrayBufferUpdateNull_1()
      throws RangeCheckException,
        JCGLExceptionDeleted
  {
    final RangeInclusiveL range = new RangeInclusiveL(0, 0);
    ArrayBufferUpdateUnmapped.newUpdateReplacingRange(
      (ArrayBufferType) TestUtilities.actuallyNull(),
      range);
  }

  @Test public final void testArrayBufferUpdateOK_0()
    throws RangeCheckException,
      JCGLExceptionDeleted
  {
    final PseudoArray a = this.makePseudoArray();
    final ArrayDescriptor ad = a.arrayGetDescriptor();

    final RangeInclusiveL range = new RangeInclusiveL(1, 9);

    final ArrayBufferUpdateUnmappedType u =
      ArrayBufferUpdateUnmapped.newUpdateReplacingRange(a, range);

    Assert.assertEquals(a, u.getArrayBuffer());
    Assert.assertEquals(ad.getElementSizeBytes(), u.getTargetDataOffset());
    Assert.assertEquals(ad.getElementSizeBytes() * 9, u.getTargetDataSize());

    final ByteBuffer b = u.getTargetData();
    Assert.assertEquals(ad.getElementSizeBytes() * 9, b.capacity());
  }

  @Test public final void testArrayBufferUpdateOK_1()
    throws RangeCheckException,
      JCGLExceptionDeleted
  {
    final PseudoArray a = this.makePseudoArray();
    final ArrayDescriptor ad = a.arrayGetDescriptor();

    final ArrayBufferUpdateUnmappedType u =
      ArrayBufferUpdateUnmapped.newUpdateReplacingAll(a);

    Assert.assertEquals(a, u.getArrayBuffer());
    Assert.assertEquals(0, u.getTargetDataOffset());
    Assert.assertEquals(ad.getElementSizeBytes() * 10, u.getTargetDataSize());

    final ByteBuffer b = u.getTargetData();
    Assert.assertEquals(ad.getElementSizeBytes() * 10, b.capacity());
  }

  @Test(expected = RangeCheckException.class) public final
    void
    testArrayBufferUpdateOutOfRange_0()
      throws RangeCheckException,
        JCGLExceptionDeleted
  {
    final PseudoArray a = this.makePseudoArray();
    final RangeInclusiveL range = new RangeInclusiveL(0, 10);
    ArrayBufferUpdateUnmapped.newUpdateReplacingRange(a, range);
  }
}
