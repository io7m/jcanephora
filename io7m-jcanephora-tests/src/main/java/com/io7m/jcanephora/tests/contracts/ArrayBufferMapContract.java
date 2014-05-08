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

package com.io7m.jcanephora.tests.contracts;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jcanephora.ArrayAttributeDescriptor;
import com.io7m.jcanephora.ArrayBufferType;
import com.io7m.jcanephora.ArrayBufferUpdateMappedType;
import com.io7m.jcanephora.ArrayBufferUsableType;
import com.io7m.jcanephora.ArrayDescriptor;
import com.io7m.jcanephora.ArrayDescriptorBuilderType;
import com.io7m.jcanephora.CursorWritable2fType;
import com.io7m.jcanephora.CursorWritable3fType;
import com.io7m.jcanephora.CursorWritable4fType;
import com.io7m.jcanephora.JCGLErrorCodesType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionDeleted;
import com.io7m.jcanephora.JCGLExceptionBufferMappedMultiple;
import com.io7m.jcanephora.JCGLExceptionBufferMappedNot;
import com.io7m.jcanephora.JCGLExceptionAttributeMissing;
import com.io7m.jcanephora.JCGLExceptionWrongContext;
import com.io7m.jcanephora.JCGLScalarType;
import com.io7m.jcanephora.UsageHint;
import com.io7m.jcanephora.api.JCGLArrayBuffersMappedType;
import com.io7m.jcanephora.api.JCGLArrayBuffersType;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.TestUtilities;
import com.io7m.jcanephora.tests.types.UnimplementedArray;
import com.io7m.jnull.NullCheckException;
import com.io7m.junreachable.UnreachableCodeException;

public abstract class ArrayBufferMapContract implements TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract JCGLArrayBuffersType getGLArrayBuffers(
    final TestContext tc);

  public abstract JCGLArrayBuffersMappedType getGLArrayBuffersMapped(
    final TestContext tc);

  public abstract JCGLErrorCodesType getGLErrorCodes(
    TestContext tc);

  @Test(expected = JCGLExceptionDeleted.class) public
    void
    testArrayIsMappedDeleted_0()
      throws JCGLExceptionBufferMappedMultiple,
        JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMappedType gm = this.getGLArrayBuffersMapped(tc);

    ArrayBufferType a = null;

    try {
      final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
      b.addAttribute(ArrayAttributeDescriptor.newAttribute(
        "position",
        JCGLScalarType.TYPE_FLOAT,
        3));
      final ArrayDescriptor d = b.build();

      a = ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
      ga.arrayBufferDelete(a);
    } catch (final Throwable x) {
      throw new UnreachableCodeException(x);
    }

    gm.arrayBufferIsMapped(a);
  }

  @Test(expected = NullCheckException.class) public
    void
    testArrayIsMappedNull_0()
      throws JCGLExceptionBufferMappedMultiple,
        JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersMappedType gm = this.getGLArrayBuffersMapped(tc);

    gm.arrayBufferIsMapped((ArrayBufferUsableType) TestUtilities
      .actuallyNull());
  }

  @Test(expected = JCGLExceptionWrongContext.class) public
    void
    testArrayIsMappedWrongContext_0()
      throws JCGLExceptionBufferMappedMultiple,
        JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersMappedType gm = this.getGLArrayBuffersMapped(tc);
    final ArrayBufferType a = new UnimplementedArray();
    gm.arrayBufferIsMapped(a);
  }

  @Test(expected = JCGLExceptionDeleted.class) public
    void
    testArrayMapReadDeleted_0()
      throws JCGLExceptionBufferMappedMultiple,
        JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMappedType gm = this.getGLArrayBuffersMapped(tc);

    ArrayBufferType a = null;

    try {
      final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
      b.addAttribute(ArrayAttributeDescriptor.newAttribute(
        "position",
        JCGLScalarType.TYPE_FLOAT,
        3));
      final ArrayDescriptor d = b.build();

      a = ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
      ga.arrayBufferDelete(a);
    } catch (final Throwable x) {
      throw new UnreachableCodeException(x);
    }

    gm.arrayBufferMapReadUntyped(a);
  }

  @Test(expected = JCGLExceptionBufferMappedMultiple.class) public
    void
    testArrayMapReadMultiple_0()
      throws JCGLExceptionBufferMappedMultiple,
        JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMappedType gm = this.getGLArrayBuffersMapped(tc);

    ArrayBufferType a = null;

    try {
      final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
      b.addAttribute(ArrayAttributeDescriptor.newAttribute(
        "position",
        JCGLScalarType.TYPE_FLOAT,
        3));
      final ArrayDescriptor d = b.build();

      a = ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
    } catch (final Throwable x) {
      throw new UnreachableCodeException(x);
    }

    gm.arrayBufferMapReadUntyped(a);
    gm.arrayBufferMapReadUntyped(a);
  }

  @Test(expected = JCGLExceptionBufferMappedNot.class) public
    void
    testArrayMapReadNot_0()
      throws JCGLExceptionBufferMappedMultiple,
        JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMappedType gm = this.getGLArrayBuffersMapped(tc);

    ArrayBufferType a = null;

    try {
      final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
      b.addAttribute(ArrayAttributeDescriptor.newAttribute(
        "position",
        JCGLScalarType.TYPE_FLOAT,
        3));
      final ArrayDescriptor d = b.build();

      a = ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
    } catch (final Throwable x) {
      throw new UnreachableCodeException(x);
    }

    gm.arrayBufferUnmap(a);
  }

  @Test public void testArrayMapUnmap_0()
    throws JCGLExceptionBufferMappedMultiple,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMappedType gm = this.getGLArrayBuffersMapped(tc);

    ArrayBufferType a = null;

    try {
      final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
      b.addAttribute(ArrayAttributeDescriptor.newAttribute(
        "position",
        JCGLScalarType.TYPE_FLOAT,
        3));
      final ArrayDescriptor d = b.build();

      a = ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
      gm.arrayBufferMapWrite(a);
      Assert.assertTrue(gm.arrayBufferIsMapped(a));
    } catch (final Throwable x) {
      throw new UnreachableCodeException(x);
    }

    gm.arrayBufferUnmap(a);
    Assert.assertFalse(gm.arrayBufferIsMapped(a));
  }

  @Test(expected = JCGLExceptionDeleted.class) public
    void
    testArrayMapUnmapDeleted_0()
      throws JCGLExceptionBufferMappedMultiple,
        JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMappedType gm = this.getGLArrayBuffersMapped(tc);

    ArrayBufferType a = null;

    try {
      final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
      b.addAttribute(ArrayAttributeDescriptor.newAttribute(
        "position",
        JCGLScalarType.TYPE_FLOAT,
        3));
      final ArrayDescriptor d = b.build();

      a = ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
      gm.arrayBufferMapWrite(a);
      Assert.assertTrue(gm.arrayBufferIsMapped(a));
      ga.arrayBufferDelete(a);
    } catch (final Throwable x) {
      throw new UnreachableCodeException(x);
    }

    gm.arrayBufferUnmap(a);
  }

  @Test(expected = JCGLExceptionDeleted.class) public
    void
    testArrayMapWriteDeleted_0()
      throws JCGLExceptionBufferMappedMultiple,
        JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMappedType gm = this.getGLArrayBuffersMapped(tc);

    ArrayBufferType a = null;

    try {
      final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
      b.addAttribute(ArrayAttributeDescriptor.newAttribute(
        "position",
        JCGLScalarType.TYPE_FLOAT,
        3));
      final ArrayDescriptor d = b.build();

      a = ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
      ga.arrayBufferDelete(a);
    } catch (final Throwable x) {
      throw new UnreachableCodeException(x);
    }

    gm.arrayBufferMapWrite(a);
  }

  @Test(expected = JCGLExceptionBufferMappedMultiple.class) public
    void
    testArrayMapWriteMultiple_0()
      throws JCGLExceptionBufferMappedMultiple,
        JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMappedType gm = this.getGLArrayBuffersMapped(tc);

    ArrayBufferType a = null;

    try {
      final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
      b.addAttribute(ArrayAttributeDescriptor.newAttribute(
        "position",
        JCGLScalarType.TYPE_FLOAT,
        3));
      final ArrayDescriptor d = b.build();

      a = ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
    } catch (final Throwable x) {
      throw new UnreachableCodeException(x);
    }

    gm.arrayBufferMapWrite(a);
    gm.arrayBufferMapWrite(a);
  }

  @Test(expected = JCGLExceptionAttributeMissing.class) public
    void
    testArrayMapWriteNonexistentAttribute_0()
      throws JCGLExceptionBufferMappedMultiple,
        JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMappedType gm = this.getGLArrayBuffersMapped(tc);

    ArrayBufferType a = null;

    final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
    b.addAttribute(ArrayAttributeDescriptor.newAttribute(
      "position",
      JCGLScalarType.TYPE_FLOAT,
      3));
    final ArrayDescriptor d = b.build();

    try {
      a = ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
    } catch (final Throwable x) {
      throw new UnreachableCodeException(x);
    }

    final ArrayBufferUpdateMappedType m = gm.arrayBufferMapWrite(a);
    m.getCursor3f("nonexistent");
  }

  @Test(expected = JCGLExceptionAttributeMissing.class) public
    void
    testArrayMapWriteNonexistentAttribute_1()
      throws JCGLExceptionBufferMappedMultiple,
        JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMappedType gm = this.getGLArrayBuffersMapped(tc);

    ArrayBufferType a = null;

    final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
    b.addAttribute(ArrayAttributeDescriptor.newAttribute(
      "position",
      JCGLScalarType.TYPE_FLOAT,
      3));
    final ArrayDescriptor d = b.build();

    try {
      a = ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
    } catch (final Throwable x) {
      throw new UnreachableCodeException(x);
    }

    final ArrayBufferUpdateMappedType m = gm.arrayBufferMapWrite(a);
    m.getCursor2f("nonexistent");
  }

  @Test(expected = JCGLExceptionAttributeMissing.class) public
    void
    testArrayMapWriteNonexistentAttribute_2()
      throws JCGLExceptionBufferMappedMultiple,
        JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMappedType gm = this.getGLArrayBuffersMapped(tc);

    ArrayBufferType a = null;

    final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
    b.addAttribute(ArrayAttributeDescriptor.newAttribute(
      "position",
      JCGLScalarType.TYPE_FLOAT,
      3));
    final ArrayDescriptor d = b.build();

    try {
      a = ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
    } catch (final Throwable x) {
      throw new UnreachableCodeException(x);
    }

    final ArrayBufferUpdateMappedType m = gm.arrayBufferMapWrite(a);
    m.getCursor4f("nonexistent");
  }

  @Test(expected = JCGLExceptionBufferMappedNot.class) public
    void
    testArrayMapWriteNot_0()
      throws JCGLExceptionBufferMappedMultiple,
        JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMappedType gm = this.getGLArrayBuffersMapped(tc);

    ArrayBufferType a = null;

    try {
      final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
      b.addAttribute(ArrayAttributeDescriptor.newAttribute(
        "position",
        JCGLScalarType.TYPE_FLOAT,
        3));
      final ArrayDescriptor d = b.build();

      a = ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
    } catch (final Throwable x) {
      throw new UnreachableCodeException(x);
    }

    gm.arrayBufferUnmap(a);
  }

  @Test public void testArrayMapWriteRead_0()
    throws JCGLExceptionBufferMappedMultiple,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMappedType gm = this.getGLArrayBuffersMapped(tc);

    ArrayBufferType a = null;

    final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
    final int components = 2;
    b.addAttribute(ArrayAttributeDescriptor.newAttribute(
      "position",
      JCGLScalarType.TYPE_FLOAT,
      components));
    final ArrayDescriptor d = b.build();

    try {
      a = ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
      final ArrayBufferUpdateMappedType m = gm.arrayBufferMapWrite(a);

      final CursorWritable2fType c = m.getCursor2f("position");

      int count = 0;
      for (int index = 0; index < 10; ++index) {
        c.put2f(count + 0, count + 1);
        count += components;
      }

      gm.arrayBufferUnmap(a);

    } catch (final Throwable x) {
      throw new UnreachableCodeException(x);
    }

    final ByteBuffer m = gm.arrayBufferMapReadUntyped(a);
    Assert.assertEquals(10 * d.getElementSizeBytes(), m.capacity());

    final FloatBuffer f = m.asFloatBuffer();

    for (int index = 0; index < f.capacity(); ++index) {
      Assert.assertEquals(index, f.get(index), 0.0f);
    }
  }

  @Test public void testArrayMapWriteRead_1()
    throws JCGLExceptionBufferMappedMultiple,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMappedType gm = this.getGLArrayBuffersMapped(tc);

    ArrayBufferType a = null;

    final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
    final int components = 3;
    b.addAttribute(ArrayAttributeDescriptor.newAttribute(
      "position",
      JCGLScalarType.TYPE_FLOAT,
      components));
    final ArrayDescriptor d = b.build();

    try {
      a = ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
      final ArrayBufferUpdateMappedType m = gm.arrayBufferMapWrite(a);

      final CursorWritable3fType c = m.getCursor3f("position");

      int count = 0;
      for (int index = 0; index < 10; ++index) {
        c.put3f(count + 0, count + 1, count + 2);
        count += components;
      }

      gm.arrayBufferUnmap(a);

    } catch (final Throwable x) {
      throw new UnreachableCodeException(x);
    }

    final ByteBuffer m = gm.arrayBufferMapReadUntyped(a);
    Assert.assertEquals(10 * d.getElementSizeBytes(), m.capacity());

    final FloatBuffer f = m.asFloatBuffer();

    for (int index = 0; index < f.capacity(); ++index) {
      Assert.assertEquals(index, f.get(index), 0.0f);
    }
  }

  @Test public void testArrayMapWriteRead_2()
    throws JCGLExceptionBufferMappedMultiple,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMappedType gm = this.getGLArrayBuffersMapped(tc);

    ArrayBufferType a = null;

    final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
    final int components = 4;
    b.addAttribute(ArrayAttributeDescriptor.newAttribute(
      "position",
      JCGLScalarType.TYPE_FLOAT,
      components));
    final ArrayDescriptor d = b.build();

    try {
      a = ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
      final ArrayBufferUpdateMappedType m = gm.arrayBufferMapWrite(a);

      final CursorWritable4fType c = m.getCursor4f("position");

      int count = 0;
      for (int index = 0; index < 10; ++index) {
        c.put4f(count + 0, count + 1, count + 2, count + 3);
        count += components;
      }

      gm.arrayBufferUnmap(a);

    } catch (final Throwable x) {
      throw new UnreachableCodeException(x);
    }

    final ByteBuffer m = gm.arrayBufferMapReadUntyped(a);
    Assert.assertEquals(10 * d.getElementSizeBytes(), m.capacity());

    final FloatBuffer f = m.asFloatBuffer();

    for (int index = 0; index < f.capacity(); ++index) {
      Assert.assertEquals(index, f.get(index), 0.0f);
    }
  }

  @Test(expected = JCGLExceptionWrongContext.class) public
    void
    testArrayMapWriteWrongContext_0()
      throws JCGLExceptionBufferMappedMultiple,
        JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersMappedType gm = this.getGLArrayBuffersMapped(tc);
    gm.arrayBufferMapWrite(new UnimplementedArray());
  }
}
