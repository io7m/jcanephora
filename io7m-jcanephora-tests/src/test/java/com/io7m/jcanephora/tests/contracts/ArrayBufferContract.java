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

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jcanephora.ArrayAttributeDescriptor;
import com.io7m.jcanephora.ArrayAttributeType;
import com.io7m.jcanephora.ArrayBufferType;
import com.io7m.jcanephora.ArrayBufferUpdateUnmapped;
import com.io7m.jcanephora.ArrayBufferUpdateUnmappedType;
import com.io7m.jcanephora.ArrayBufferUsableType;
import com.io7m.jcanephora.ArrayDescriptor;
import com.io7m.jcanephora.ArrayDescriptorBuilderType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionDeleted;
import com.io7m.jcanephora.JCGLExceptionWrongContext;
import com.io7m.jcanephora.JCGLScalarType;
import com.io7m.jcanephora.UsageHint;
import com.io7m.jcanephora.api.JCGLArrayBuffersType;
import com.io7m.jcanephora.api.JCGLShadersCommonType;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.TestUtilities;
import com.io7m.jcanephora.tests.types.UnimplementedArray;
import com.io7m.jnull.NullCheckException;
import com.io7m.jranges.RangeCheckException;
import com.io7m.jranges.RangeInclusiveL;
import com.io7m.junreachable.UnreachableCodeException;

public abstract class ArrayBufferContract implements TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract JCGLArrayBuffersType getGLArrayBuffers(
    final TestContext context);

  public abstract JCGLShadersCommonType getGLPrograms(
    final TestContext context);

  @Test public final void testArrayBufferAllocate()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType gl = this.getGLArrayBuffers(tc);

    ArrayBufferType a = null;

    try {
      final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
      b.addAttribute(ArrayAttributeDescriptor.newAttribute(
        "position",
        JCGLScalarType.TYPE_FLOAT,
        3));
      final ArrayDescriptor d = b.build();

      a = gl.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
      Assert.assertEquals(12, a.bufferGetElementSizeBytes());
      Assert.assertEquals(10, a.bufferGetRange().getInterval());
      Assert.assertEquals(120, a.resourceGetSizeBytes());
      Assert.assertEquals(d, a.arrayGetDescriptor());
    } finally {
      if (a != null) {
        gl.arrayBufferDelete(a);
      }
    }
  }

  @Test(expected = NullCheckException.class) public final
    void
    testArrayBufferAllocateNull_0()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType gl = this.getGLArrayBuffers(tc);

    gl.arrayBufferAllocate(
      0,
      (ArrayDescriptor) TestUtilities.actuallyNull(),
      UsageHint.USAGE_STATIC_DRAW);
  }

  @Test(expected = NullCheckException.class) public final
    void
    testArrayBufferAllocateNull_1()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType gl = this.getGLArrayBuffers(tc);

    final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
    b.addAttribute(ArrayAttributeDescriptor.newAttribute(
      "position",
      JCGLScalarType.TYPE_FLOAT,
      3));
    final ArrayDescriptor d = b.build();

    gl.arrayBufferAllocate(0, d, (UsageHint) TestUtilities.actuallyNull());
  }

  @Test(expected = RangeCheckException.class) public final
    void
    testArrayBufferAllocateZeroElements()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType gl = this.getGLArrayBuffers(tc);

    final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
    b.addAttribute(ArrayAttributeDescriptor.newAttribute(
      "position",
      JCGLScalarType.TYPE_FLOAT,
      3));
    final ArrayDescriptor d = b.build();

    gl.arrayBufferAllocate(0, d, UsageHint.USAGE_STATIC_DRAW);
  }

  @Test public final void testArrayBufferAttributes()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType gl = this.getGLArrayBuffers(tc);

    final ArrayDescriptorBuilderType ab = ArrayDescriptor.newBuilder();
    ab.addAttribute(ArrayAttributeDescriptor.newAttribute(
      "position",
      JCGLScalarType.TYPE_SHORT,
      3));
    ab.addAttribute(ArrayAttributeDescriptor.newAttribute(
      "normal",
      JCGLScalarType.TYPE_SHORT,
      3));
    ab.addAttribute(ArrayAttributeDescriptor.newAttribute(
      "color",
      JCGLScalarType.TYPE_SHORT,
      3));
    final ArrayDescriptor ad = ab.build();
    Assert.assertEquals(0, ad.getAttributeOffset("position"));
    Assert.assertEquals(6, ad.getAttributeOffset("normal"));
    Assert.assertEquals(12, ad.getAttributeOffset("color"));
    Assert.assertEquals(18, ad.getElementSizeBytes());

    final ArrayBufferType a =
      gl.arrayBufferAllocate(3, ad, UsageHint.USAGE_STATIC_DRAW);

    {
      final ArrayAttributeType aa = a.arrayGetAttribute("position");
      Assert.assertEquals(a, aa.getArray());
      Assert.assertEquals("position", aa.getDescriptor().getName());
    }

    {
      final ArrayAttributeType aa = a.arrayGetAttribute("normal");
      Assert.assertEquals(a, aa.getArray());
      Assert.assertEquals("normal", aa.getDescriptor().getName());
    }

    {
      final ArrayAttributeType aa = a.arrayGetAttribute("color");
      Assert.assertEquals(a, aa.getArray());
      Assert.assertEquals("color", aa.getDescriptor().getName());
    }
  }

  @Test(expected = JCGLExceptionDeleted.class) public final
    void
    testArrayBufferBindDeleted()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType gl = this.getGLArrayBuffers(tc);
    final ArrayBufferType a;

    try {
      final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
      b.addAttribute(ArrayAttributeDescriptor.newAttribute(
        "position",
        JCGLScalarType.TYPE_FLOAT,
        3));
      final ArrayDescriptor d = b.build();

      a = gl.arrayBufferAllocate(1, d, UsageHint.USAGE_STATIC_DRAW);
      gl.arrayBufferDelete(a);
      Assert.assertTrue(a.resourceIsDeleted());
    } catch (final Throwable x) {
      throw new UnreachableCodeException(x);
    }

    gl.arrayBufferBind(a);
  }

  @Test(expected = NullCheckException.class) public final
    void
    testArrayBufferBindNull()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType gl = this.getGLArrayBuffers(tc);

    gl.arrayBufferBind((ArrayBufferUsableType) TestUtilities.actuallyNull());
  }

  @Test public final void testArrayBufferBindOK()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType gl = this.getGLArrayBuffers(tc);

    final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
    b.addAttribute(ArrayAttributeDescriptor.newAttribute(
      "position",
      JCGLScalarType.TYPE_FLOAT,
      3));
    final ArrayDescriptor d = b.build();

    final ArrayBufferType a =
      gl.arrayBufferAllocate(1, d, UsageHint.USAGE_STATIC_DRAW);

    gl.arrayBufferBind(a);
    Assert.assertTrue(gl.arrayBufferIsBound(a));
    gl.arrayBufferUnbind();
    Assert.assertFalse(gl.arrayBufferIsBound(a));
  }

  @Test(expected = JCGLExceptionWrongContext.class) public final
    void
    testArrayBufferBindWrongContext()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType gl = this.getGLArrayBuffers(tc);
    final ArrayBufferType a = new UnimplementedArray();

    gl.arrayBufferBind(a);
  }

  @Test(expected = JCGLExceptionDeleted.class) public final
    void
    testArrayBufferDeleteDeleted()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType gl = this.getGLArrayBuffers(tc);
    final ArrayBufferType a;

    try {
      final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
      b.addAttribute(ArrayAttributeDescriptor.newAttribute(
        "position",
        JCGLScalarType.TYPE_FLOAT,
        3));
      final ArrayDescriptor d = b.build();

      a = gl.arrayBufferAllocate(1, d, UsageHint.USAGE_STATIC_DRAW);
      gl.arrayBufferDelete(a);
      Assert.assertTrue(a.resourceIsDeleted());
    } catch (final Throwable x) {
      throw new UnreachableCodeException(x);
    }

    gl.arrayBufferDelete(a);
  }

  @Test(expected = NullCheckException.class) public final
    void
    testArrayBufferDeleteNull()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType gl = this.getGLArrayBuffers(tc);

    gl.arrayBufferDelete((ArrayBufferType) TestUtilities.actuallyNull());
  }

  @Test(expected = JCGLExceptionWrongContext.class) public final
    void
    testArrayBufferDeleteWrongContext()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType gl = this.getGLArrayBuffers(tc);
    final ArrayBufferType a = new UnimplementedArray();

    gl.arrayBufferDelete(a);
  }

  @Test(expected = JCGLExceptionDeleted.class) public final
    void
    testArrayBufferIsBoundDeleted()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType gl = this.getGLArrayBuffers(tc);
    final ArrayBufferType a;

    try {
      final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
      b.addAttribute(ArrayAttributeDescriptor.newAttribute(
        "position",
        JCGLScalarType.TYPE_FLOAT,
        3));
      final ArrayDescriptor d = b.build();

      a = gl.arrayBufferAllocate(1, d, UsageHint.USAGE_STATIC_DRAW);
      gl.arrayBufferDelete(a);
    } catch (final Throwable x) {
      throw new UnreachableCodeException(x);
    }

    gl.arrayBufferIsBound(a);
  }

  @Test(expected = NullCheckException.class) public final
    void
    testArrayBufferIsBoundNull()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType gl = this.getGLArrayBuffers(tc);

    gl.arrayBufferIsBound((ArrayBufferUsableType) TestUtilities
      .actuallyNull());
  }

  @Test(expected = JCGLExceptionWrongContext.class) public final
    void
    testArrayBufferIsBoundWrongContext()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType gl = this.getGLArrayBuffers(tc);
    final ArrayBufferType a = new UnimplementedArray();

    gl.arrayBufferIsBound(a);
  }

  @Test(expected = JCGLExceptionDeleted.class) public final
    void
    testArrayBufferUpdateDeleted_0()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType gl = this.getGLArrayBuffers(tc);

    ArrayBufferType a = null;
    ArrayBufferUpdateUnmappedType u = null;

    try {
      final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
      b.addAttribute(ArrayAttributeDescriptor.newAttribute(
        "position",
        JCGLScalarType.TYPE_FLOAT,
        3));
      final ArrayDescriptor d = b.build();

      a = gl.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
      u = ArrayBufferUpdateUnmapped.newUpdateReplacingAll(a);

      gl.arrayBufferDelete(a);
    } catch (final Throwable x) {
      throw new UnreachableCodeException(x);
    }

    gl.arrayBufferUpdate(u);
  }

  @Test(expected = JCGLExceptionDeleted.class) public final
    void
    testArrayBufferUpdateDeleted_1()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType gl = this.getGLArrayBuffers(tc);

    ArrayBufferType a = null;

    try {
      final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
      b.addAttribute(ArrayAttributeDescriptor.newAttribute(
        "position",
        JCGLScalarType.TYPE_FLOAT,
        3));
      final ArrayDescriptor d = b.build();

      a = gl.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
      gl.arrayBufferDelete(a);
    } catch (final Throwable x) {
      throw new UnreachableCodeException(x);
    }

    ArrayBufferUpdateUnmapped.newUpdateReplacingAll(a);
  }

  @Test(expected = NullCheckException.class) public final
    void
    testArrayBufferUpdateNull_0()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType gl = this.getGLArrayBuffers(tc);

    gl.arrayBufferUpdate((ArrayBufferUpdateUnmappedType) TestUtilities
      .actuallyNull());
  }

  @Test(expected = NullCheckException.class) public final
    void
    testArrayBufferUpdateNull_1()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType gl = this.getGLArrayBuffers(tc);

    ArrayBufferType a = null;

    try {
      final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
      b.addAttribute(ArrayAttributeDescriptor.newAttribute(
        "position",
        JCGLScalarType.TYPE_FLOAT,
        3));
      final ArrayDescriptor d = b.build();

      a = gl.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
    } catch (final Throwable x) {
      throw new UnreachableCodeException(x);
    }

    ArrayBufferUpdateUnmapped.newUpdateReplacingRange(
      a,
      (RangeInclusiveL) TestUtilities.actuallyNull());
  }

  @Test public final void testArrayBufferUpdateOK_0()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType gl = this.getGLArrayBuffers(tc);

    ArrayBufferType a = null;
    ArrayBufferUpdateUnmappedType u;

    try {
      final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
      b.addAttribute(ArrayAttributeDescriptor.newAttribute(
        "position",
        JCGLScalarType.TYPE_FLOAT,
        3));
      final ArrayDescriptor d = b.build();

      a = gl.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
      u = ArrayBufferUpdateUnmapped.newUpdateReplacingAll(a);
    } catch (final Throwable x) {
      throw new UnreachableCodeException(x);
    }

    gl.arrayBufferUpdate(u);
  }

  //
  // /**
  // * Array buffer complete updates work.
  // */
  //
  // @Test public final void testArrayBufferUpdateComplete()
  // throws ConstraintError,
  // JCGLRuntimeException,
  // JCGLUnsupportedException
  // {
  // final TestContext tc = this.newTestContext();
  // final JCGLArrayBuffersType gl = this.getGLArrayBuffers(tc);
  //
  // ArrayBuffer a = null;
  //
  // try {
  // final ArrayList<ArrayBufferAttributeDescriptor> abs =
  // new ArrayList<ArrayBufferAttributeDescriptor>();
  // abs.add(new ArrayBufferAttributeDescriptor(
  // "position",
  // JCGLScalarType.TYPE_FLOAT,
  // 3));
  // final ArrayBufferTypeDescriptor d = new ArrayBufferTypeDescriptor(abs);
  //
  // a = gl.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
  // } catch (final Throwable e) {
  // Assert.fail(e.getMessage());
  // }
  //
  // final ArrayBufferWritableData data = new ArrayBufferWritableData(a);
  // gl.arrayBufferBind(a);
  // gl.arrayBufferUpdate(data);
  // }
  //
  // /**
  // * Array buffer updates with a deleted buffer bound fail.
  // */
  //
  // @Test(expected = ConstraintError.class) public final
  // void
  // testArrayBufferUpdateDeletedFails()
  // throws ConstraintError,
  // JCGLRuntimeException,
  // JCGLUnsupportedException
  // {
  // final TestContext tc = this.newTestContext();
  // final JCGLArrayBuffersType gl = this.getGLArrayBuffers(tc);
  //
  // ArrayBuffer a = null;
  //
  // try {
  // final ArrayList<ArrayBufferAttributeDescriptor> abs =
  // new ArrayList<ArrayBufferAttributeDescriptor>();
  // abs.add(new ArrayBufferAttributeDescriptor(
  // "position",
  // JCGLScalarType.TYPE_FLOAT,
  // 3));
  // final ArrayBufferTypeDescriptor d = new ArrayBufferTypeDescriptor(abs);
  //
  // a = gl.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
  // } catch (final Throwable e) {
  // Assert.fail(e.getMessage());
  // }
  //
  // final ArrayBufferWritableData data = new ArrayBufferWritableData(a);
  // gl.arrayBufferBind(a);
  // gl.arrayBufferDelete(a);
  // gl.arrayBufferUpdate(data);
  // }
  //
  // /**
  // * Array buffer partial updates work.
  // */
  //
  // @Test public final void testArrayBufferUpdatePartial()
  // throws ConstraintError,
  // JCGLRuntimeException,
  // JCGLUnsupportedException
  // {
  // final TestContext tc = this.newTestContext();
  // final JCGLArrayBuffersType gl = this.getGLArrayBuffers(tc);
  //
  // ArrayBuffer a = null;
  //
  // try {
  // final ArrayList<ArrayBufferAttributeDescriptor> abs =
  // new ArrayList<ArrayBufferAttributeDescriptor>();
  // abs.add(new ArrayBufferAttributeDescriptor(
  // "position",
  // JCGLScalarType.TYPE_FLOAT,
  // 3));
  // final ArrayBufferTypeDescriptor d = new ArrayBufferTypeDescriptor(abs);
  //
  // a = gl.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
  // } catch (final Throwable e) {
  // Assert.fail(e.getMessage());
  // }
  //
  // final ArrayBufferWritableData data =
  // new ArrayBufferWritableData(a, new RangeInclusive(2, 8));
  // gl.arrayBufferBind(a);
  // gl.arrayBufferUpdate(data);
  // }
  //
  // /**
  // * Array buffer updates without a bound buffer fail.
  // */
  //
  // @Test(expected = ConstraintError.class) public final
  // void
  // testArrayBufferUpdateUnboundFails()
  // throws ConstraintError,
  // JCGLRuntimeException,
  // JCGLUnsupportedException
  // {
  // final TestContext tc = this.newTestContext();
  // final JCGLArrayBuffersType gl = this.getGLArrayBuffers(tc);
  //
  // ArrayBuffer a = null;
  //
  // try {
  // final ArrayList<ArrayBufferAttributeDescriptor> abs =
  // new ArrayList<ArrayBufferAttributeDescriptor>();
  // abs.add(new ArrayBufferAttributeDescriptor(
  // "position",
  // JCGLScalarType.TYPE_FLOAT,
  // 3));
  // final ArrayBufferTypeDescriptor d = new ArrayBufferTypeDescriptor(abs);
  //
  // a = gl.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
  // } catch (final Throwable e) {
  // Assert.fail(e.getMessage());
  // }
  //
  // final ArrayBufferWritableData data = new ArrayBufferWritableData(a);
  // gl.arrayBufferUnbind();
  // gl.arrayBufferUpdate(data);
  // }
  //
  // /**
  // * Array buffer updates with the wrong buffer bound fail.
  // */
  //
  // @Test(expected = ConstraintError.class) public final
  // void
  // testArrayBufferUpdateWrongBindingFails()
  // throws ConstraintError,
  // JCGLRuntimeException,
  // JCGLUnsupportedException,
  // JCGLUnsupportedException
  // {
  // final TestContext tc = this.newTestContext();
  // final JCGLArrayBuffersType gl = this.getGLArrayBuffers(tc);
  //
  // ArrayBuffer a = null;
  // ArrayBuffer b = null;
  //
  // try {
  // final ArrayList<ArrayBufferAttributeDescriptor> abs =
  // new ArrayList<ArrayBufferAttributeDescriptor>();
  // abs.add(new ArrayBufferAttributeDescriptor(
  // "position",
  // JCGLScalarType.TYPE_FLOAT,
  // 3));
  // final ArrayBufferTypeDescriptor d = new ArrayBufferTypeDescriptor(abs);
  //
  // a = gl.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
  // b = gl.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
  // } catch (final Throwable e) {
  // Assert.fail(e.getMessage());
  // }
  //
  // final ArrayBufferWritableData data = new ArrayBufferWritableData(a);
  // gl.arrayBufferBind(b);
  // gl.arrayBufferUpdate(data);
  // }
}
