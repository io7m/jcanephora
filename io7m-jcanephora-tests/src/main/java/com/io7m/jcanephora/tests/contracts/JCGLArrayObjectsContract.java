/*
 * Copyright © 2015 <code@io7m.com> http://io7m.com
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

import com.io7m.jcanephora.core.JCGLArrayBufferType;
import com.io7m.jcanephora.core.JCGLArrayObjectBuilderType;
import com.io7m.jcanephora.core.JCGLArrayObjectType;
import com.io7m.jcanephora.core.JCGLArrayVertexAttributeFloatingPointType;
import com.io7m.jcanephora.core.JCGLArrayVertexAttributeIntegralType;
import com.io7m.jcanephora.core.JCGLArrayVertexAttributeType;
import com.io7m.jcanephora.core.JCGLExceptionDeleted;
import com.io7m.jcanephora.core.JCGLExceptionWrongContext;
import com.io7m.jcanephora.core.JCGLScalarIntegralType;
import com.io7m.jcanephora.core.JCGLScalarType;
import com.io7m.jcanephora.core.JCGLUsageHint;
import com.io7m.jcanephora.core.api.JCGLArrayBuffersType;
import com.io7m.jcanephora.core.api.JCGLArrayObjectsType;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeCheckException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Optional;

/**
 * Array objects contracts.
 */

// CHECKSTYLE_JAVADOC:OFF

public abstract class JCGLArrayObjectsContract extends JCGLContract
{
  @Rule public final ExpectedException expected = ExpectedException.none();

  protected abstract Interfaces getInterfaces(String name);

  @Test public final void testArrayGetBadIndex()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLArrayObjectsType go = i.getArrayObjects();
    final JCGLArrayObjectBuilderType b = go.arrayObjectNewBuilder();
    Assert.assertTrue(b.getMaximumVertexAttributes() >= 16);

    this.expected.expect(RangeCheckException.class);
    b.getAttributeAt(-1);
  }

  @Test public final void testArrayIntegralReplacesFloating()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLArrayBuffersType ga = i.getArrayBuffers();
    final JCGLArrayObjectsType go = i.getArrayObjects();

    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);
    final JCGLArrayObjectBuilderType b = go.arrayObjectNewBuilder();

    b.setAttributeFloatingPoint(
      0, a, 4, JCGLScalarType.TYPE_FLOAT, 16, 0L, false);

    {
      final Optional<JCGLArrayVertexAttributeType> at_opt = b.getAttributeAt(0);
      Assert.assertTrue(at_opt.isPresent());
      final JCGLArrayVertexAttributeType at_raw = at_opt.get();
      Assert.assertTrue(
        at_raw instanceof JCGLArrayVertexAttributeFloatingPointType);
      final JCGLArrayVertexAttributeFloatingPointType at =
        (JCGLArrayVertexAttributeFloatingPointType) at_raw;

      Assert.assertEquals(0L, (long) at.getIndex());
    }

    for (int index = 1; index < b.getMaximumVertexAttributes(); ++index) {
      final Optional<JCGLArrayVertexAttributeType> at_opt =
        b.getAttributeAt(index);
      Assert.assertFalse(at_opt.isPresent());
    }

    b.setAttributeIntegral(
      0, a, 2, JCGLScalarIntegralType.TYPE_INT, 24, 8L);

    {
      final Optional<JCGLArrayVertexAttributeType> at_opt = b.getAttributeAt(0);
      Assert.assertTrue(at_opt.isPresent());
      final JCGLArrayVertexAttributeType at_raw = at_opt.get();
      Assert.assertTrue(
        at_raw instanceof JCGLArrayVertexAttributeIntegralType);
      final JCGLArrayVertexAttributeIntegralType at =
        (JCGLArrayVertexAttributeIntegralType) at_raw;

      Assert.assertEquals(0L, (long) at.getIndex());
    }

    for (int index = 1; index < b.getMaximumVertexAttributes(); ++index) {
      final Optional<JCGLArrayVertexAttributeType> at_opt =
        b.getAttributeAt(index);
      Assert.assertFalse(at_opt.isPresent());
    }
  }

  @Test public final void testArrayFloatingReplacesIntegral()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLArrayBuffersType ga = i.getArrayBuffers();
    final JCGLArrayObjectsType go = i.getArrayObjects();

    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);
    final JCGLArrayObjectBuilderType b = go.arrayObjectNewBuilder();

    b.setAttributeIntegral(
      0, a, 2, JCGLScalarIntegralType.TYPE_INT, 24, 8L);

    {
      final Optional<JCGLArrayVertexAttributeType> at_opt = b.getAttributeAt(0);
      Assert.assertTrue(at_opt.isPresent());
      final JCGLArrayVertexAttributeType at_raw = at_opt.get();
      Assert.assertTrue(
        at_raw instanceof JCGLArrayVertexAttributeIntegralType);
      final JCGLArrayVertexAttributeIntegralType at =
        (JCGLArrayVertexAttributeIntegralType) at_raw;

      Assert.assertEquals(0L, (long) at.getIndex());
    }

    for (int index = 1; index < b.getMaximumVertexAttributes(); ++index) {
      final Optional<JCGLArrayVertexAttributeType> at_opt =
        b.getAttributeAt(index);
      Assert.assertFalse(at_opt.isPresent());
    }

    b.setAttributeFloatingPoint(
      0, a, 4, JCGLScalarType.TYPE_FLOAT, 16, 0L, false);

    {
      final Optional<JCGLArrayVertexAttributeType> at_opt = b.getAttributeAt(0);
      Assert.assertTrue(at_opt.isPresent());
      final JCGLArrayVertexAttributeType at_raw = at_opt.get();
      Assert.assertTrue(
        at_raw instanceof JCGLArrayVertexAttributeFloatingPointType);
      final JCGLArrayVertexAttributeFloatingPointType at =
        (JCGLArrayVertexAttributeFloatingPointType) at_raw;

      Assert.assertEquals(0L, (long) at.getIndex());
    }

    for (int index = 1; index < b.getMaximumVertexAttributes(); ++index) {
      final Optional<JCGLArrayVertexAttributeType> at_opt =
        b.getAttributeAt(index);
      Assert.assertFalse(at_opt.isPresent());
    }
  }

  @Test public final void testArrayIdentities()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLArrayBuffersType ga = i.getArrayBuffers();
    final JCGLArrayObjectsType go = i.getArrayObjects();

    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);
    final JCGLArrayObjectBuilderType b = go.arrayObjectNewBuilder();
    Assert.assertTrue(b.getMaximumVertexAttributes() >= 16);

    b.setAttributeFloatingPoint(
      0, a, 4, JCGLScalarType.TYPE_FLOAT, 16, 0L, false);
    b.setAttributeFloatingPoint(
      1, a, 3, JCGLScalarType.TYPE_INT, 20, 4L, true);
    b.setAttributeIntegral(
      2, a, 2, JCGLScalarIntegralType.TYPE_INT, 24, 8L);

    {
      final Optional<JCGLArrayVertexAttributeType> at_opt = b.getAttributeAt(0);
      Assert.assertTrue(at_opt.isPresent());
      final JCGLArrayVertexAttributeType at_raw = at_opt.get();
      Assert.assertTrue(
        at_raw instanceof JCGLArrayVertexAttributeFloatingPointType);
      final JCGLArrayVertexAttributeFloatingPointType at =
        (JCGLArrayVertexAttributeFloatingPointType) at_raw;

      Assert.assertEquals(0L, (long) at.getIndex());
    }

    {
      final Optional<JCGLArrayVertexAttributeType> at_opt = b.getAttributeAt(1);
      Assert.assertTrue(at_opt.isPresent());
      final JCGLArrayVertexAttributeType at_raw = at_opt.get();
      Assert.assertTrue(
        at_raw instanceof JCGLArrayVertexAttributeFloatingPointType);
      final JCGLArrayVertexAttributeFloatingPointType at =
        (JCGLArrayVertexAttributeFloatingPointType) at_raw;

      Assert.assertEquals(1L, (long) at.getIndex());
    }

    {
      final Optional<JCGLArrayVertexAttributeType> at_opt = b.getAttributeAt(2);
      Assert.assertTrue(at_opt.isPresent());
      final JCGLArrayVertexAttributeType at_raw = at_opt.get();
      Assert.assertTrue(
        at_raw instanceof JCGLArrayVertexAttributeIntegralType);
      final JCGLArrayVertexAttributeIntegralType at =
        (JCGLArrayVertexAttributeIntegralType) at_raw;

      Assert.assertEquals(2L, (long) at.getIndex());
    }

    {
      final Optional<JCGLArrayVertexAttributeType> at_opt = b.getAttributeAt(3);
      Assert.assertFalse(at_opt.isPresent());
    }

    b.reset();

    {
      for (int index = 0; index < b.getMaximumVertexAttributes(); ++index) {
        final Optional<JCGLArrayVertexAttributeType> at_opt =
          b.getAttributeAt(index);
        Assert.assertFalse(at_opt.isPresent());
      }
    }
  }

  @Test public final void testFloatingArrayDeleted()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLArrayBuffersType ga = i.getArrayBuffers();
    final JCGLArrayObjectsType go = i.getArrayObjects();

    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);
    final JCGLArrayObjectBuilderType b = go.arrayObjectNewBuilder();
    Assert.assertTrue(b.getMaximumVertexAttributes() >= 16);

    ga.arrayBufferDelete(a);

    this.expected.expect(JCGLExceptionDeleted.class);
    b.setAttributeFloatingPoint(
      0, a, 4, JCGLScalarType.TYPE_FLOAT, 16, 0L, false);
  }

  @Test public final void testFloatingArrayWrongContext()
  {
    final Interfaces i_main = this.getInterfaces("main");
    final JCGLArrayBuffersType ga_main = i_main.getArrayBuffers();
    final JCGLContextType c_main = i_main.getContext();
    final Interfaces i_alt = this.getInterfaces("alt");
    final JCGLArrayObjectsType go_alt = i_alt.getArrayObjects();
    final JCGLContextType c_alt = i_alt.getContext();

    c_main.contextMakeCurrent();
    final JCGLArrayBufferType a =
      ga_main.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);

    c_alt.contextMakeCurrent();
    final JCGLArrayObjectBuilderType b = go_alt.arrayObjectNewBuilder();
    Assert.assertTrue(b.getMaximumVertexAttributes() >= 16);

    this.expected.expect(JCGLExceptionWrongContext.class);
    b.setAttributeFloatingPoint(
      0, a, 4, JCGLScalarType.TYPE_FLOAT, 16, 0L, false);
  }

  @Test public final void testFloatingArrayBadIndex()
  {
    final Interfaces i_main = this.getInterfaces("main");
    final JCGLArrayBuffersType ga_main = i_main.getArrayBuffers();
    final JCGLArrayObjectsType go_main = i_main.getArrayObjects();

    final JCGLArrayBufferType a =
      ga_main.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);

    final JCGLArrayObjectBuilderType b = go_main.arrayObjectNewBuilder();
    Assert.assertTrue(b.getMaximumVertexAttributes() >= 16);

    this.expected.expect(RangeCheckException.class);
    b.setAttributeFloatingPoint(
      -1, a, 4, JCGLScalarType.TYPE_FLOAT, 16, 0L, false);
  }

  @Test public final void testFloatingArrayBadElements0()
  {
    final Interfaces i_main = this.getInterfaces("main");
    final JCGLArrayBuffersType ga_main = i_main.getArrayBuffers();
    final JCGLArrayObjectsType go_main = i_main.getArrayObjects();

    final JCGLArrayBufferType a =
      ga_main.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);

    final JCGLArrayObjectBuilderType b = go_main.arrayObjectNewBuilder();
    Assert.assertTrue(b.getMaximumVertexAttributes() >= 16);

    this.expected.expect(RangeCheckException.class);
    b.setAttributeFloatingPoint(
      0, a, -1, JCGLScalarType.TYPE_FLOAT, 16, 0L, false);
  }

  @Test public final void testFloatingArrayBadElements1()
  {
    final Interfaces i_main = this.getInterfaces("main");
    final JCGLArrayBuffersType ga_main = i_main.getArrayBuffers();
    final JCGLArrayObjectsType go_main = i_main.getArrayObjects();

    final JCGLArrayBufferType a =
      ga_main.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);

    final JCGLArrayObjectBuilderType b = go_main.arrayObjectNewBuilder();
    Assert.assertTrue(b.getMaximumVertexAttributes() >= 16);

    this.expected.expect(RangeCheckException.class);
    b.setAttributeFloatingPoint(
      0, a, 5, JCGLScalarType.TYPE_FLOAT, 16, 0L, false);
  }

  @Test public final void testFloatingArrayBadStride()
  {
    final Interfaces i_main = this.getInterfaces("main");
    final JCGLArrayBuffersType ga_main = i_main.getArrayBuffers();
    final JCGLArrayObjectsType go_main = i_main.getArrayObjects();

    final JCGLArrayBufferType a =
      ga_main.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);

    final JCGLArrayObjectBuilderType b = go_main.arrayObjectNewBuilder();
    Assert.assertTrue(b.getMaximumVertexAttributes() >= 16);

    this.expected.expect(RangeCheckException.class);
    b.setAttributeFloatingPoint(
      0, a, 4, JCGLScalarType.TYPE_FLOAT, -1, 0L, false);
  }

  @Test public final void testFloatingArrayBadOffset()
  {
    final Interfaces i_main = this.getInterfaces("main");
    final JCGLArrayBuffersType ga_main = i_main.getArrayBuffers();
    final JCGLArrayObjectsType go_main = i_main.getArrayObjects();

    final JCGLArrayBufferType a =
      ga_main.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);

    final JCGLArrayObjectBuilderType b = go_main.arrayObjectNewBuilder();
    Assert.assertTrue(b.getMaximumVertexAttributes() >= 16);

    this.expected.expect(RangeCheckException.class);
    b.setAttributeFloatingPoint(
      0, a, 4, JCGLScalarType.TYPE_FLOAT, 16, 100L, false);
  }

  @Test public final void testIntegralArrayDeleted()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLArrayBuffersType ga = i.getArrayBuffers();
    final JCGLArrayObjectsType go = i.getArrayObjects();

    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);
    final JCGLArrayObjectBuilderType b = go.arrayObjectNewBuilder();
    Assert.assertTrue(b.getMaximumVertexAttributes() >= 16);

    ga.arrayBufferDelete(a);

    this.expected.expect(JCGLExceptionDeleted.class);
    b.setAttributeIntegral(
      0, a, 4, JCGLScalarIntegralType.TYPE_INT, 16, 0L);
  }

  @Test public final void testIntegralArrayWrongContext()
  {
    final Interfaces i_main = this.getInterfaces("main");
    final JCGLArrayBuffersType ga_main = i_main.getArrayBuffers();
    final JCGLContextType c_main = i_main.getContext();
    final Interfaces i_alt = this.getInterfaces("alt");
    final JCGLArrayObjectsType go_alt = i_alt.getArrayObjects();
    final JCGLContextType c_alt = i_alt.getContext();

    c_main.contextMakeCurrent();
    final JCGLArrayBufferType a =
      ga_main.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);

    c_alt.contextMakeCurrent();
    final JCGLArrayObjectBuilderType b = go_alt.arrayObjectNewBuilder();
    Assert.assertTrue(b.getMaximumVertexAttributes() >= 16);

    this.expected.expect(JCGLExceptionWrongContext.class);
    b.setAttributeIntegral(
      0, a, 4, JCGLScalarIntegralType.TYPE_INT, 16, 0L);
  }

  @Test public final void testIntegralArrayBadIndex()
  {
    final Interfaces i_main = this.getInterfaces("main");
    final JCGLArrayBuffersType ga_main = i_main.getArrayBuffers();
    final JCGLArrayObjectsType go_main = i_main.getArrayObjects();

    final JCGLArrayBufferType a =
      ga_main.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);

    final JCGLArrayObjectBuilderType b = go_main.arrayObjectNewBuilder();
    Assert.assertTrue(b.getMaximumVertexAttributes() >= 16);

    this.expected.expect(RangeCheckException.class);
    b.setAttributeIntegral(
      -1, a, 4, JCGLScalarIntegralType.TYPE_INT, 16, 0L);
  }

  @Test public final void testIntegralArrayBadElements0()
  {
    final Interfaces i_main = this.getInterfaces("main");
    final JCGLArrayBuffersType ga_main = i_main.getArrayBuffers();
    final JCGLArrayObjectsType go_main = i_main.getArrayObjects();

    final JCGLArrayBufferType a =
      ga_main.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);

    final JCGLArrayObjectBuilderType b = go_main.arrayObjectNewBuilder();
    Assert.assertTrue(b.getMaximumVertexAttributes() >= 16);

    this.expected.expect(RangeCheckException.class);
    b.setAttributeIntegral(
      0, a, -1, JCGLScalarIntegralType.TYPE_INT, 16, 0L);
  }

  @Test public final void testIntegralArrayBadElements1()
  {
    final Interfaces i_main = this.getInterfaces("main");
    final JCGLArrayBuffersType ga_main = i_main.getArrayBuffers();
    final JCGLArrayObjectsType go_main = i_main.getArrayObjects();

    final JCGLArrayBufferType a =
      ga_main.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);

    final JCGLArrayObjectBuilderType b = go_main.arrayObjectNewBuilder();
    Assert.assertTrue(b.getMaximumVertexAttributes() >= 16);

    this.expected.expect(RangeCheckException.class);
    b.setAttributeIntegral(
      0, a, 5, JCGLScalarIntegralType.TYPE_INT, 16, 0L);
  }

  @Test public final void testIntegralArrayBadStride()
  {
    final Interfaces i_main = this.getInterfaces("main");
    final JCGLArrayBuffersType ga_main = i_main.getArrayBuffers();
    final JCGLArrayObjectsType go_main = i_main.getArrayObjects();

    final JCGLArrayBufferType a =
      ga_main.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);

    final JCGLArrayObjectBuilderType b = go_main.arrayObjectNewBuilder();
    Assert.assertTrue(b.getMaximumVertexAttributes() >= 16);

    this.expected.expect(RangeCheckException.class);
    b.setAttributeIntegral(
      0, a, 4, JCGLScalarIntegralType.TYPE_INT, -1, 0L);
  }

  @Test public final void testIntegralArrayBadOffset()
  {
    final Interfaces i_main = this.getInterfaces("main");
    final JCGLArrayBuffersType ga_main = i_main.getArrayBuffers();
    final JCGLArrayObjectsType go_main = i_main.getArrayObjects();

    final JCGLArrayBufferType a =
      ga_main.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);

    final JCGLArrayObjectBuilderType b = go_main.arrayObjectNewBuilder();
    Assert.assertTrue(b.getMaximumVertexAttributes() >= 16);

    this.expected.expect(RangeCheckException.class);
    b.setAttributeIntegral(
      0, a, 4, JCGLScalarIntegralType.TYPE_INT, 16, 100L);
  }

  @Test public final void testArrayAllocate()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLArrayBuffersType ga = i.getArrayBuffers();
    final JCGLArrayObjectsType go = i.getArrayObjects();

    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);
    final JCGLArrayObjectBuilderType b = go.arrayObjectNewBuilder();
    Assert.assertTrue(b.getMaximumVertexAttributes() >= 16);

    b.setAttributeFloatingPoint(
      0, a, 4, JCGLScalarType.TYPE_FLOAT, 16, 0L, false);
    b.setAttributeFloatingPoint(
      1, a, 3, JCGLScalarType.TYPE_INT, 20, 4L, true);
    b.setAttributeIntegral(
      4, a, 2, JCGLScalarIntegralType.TYPE_INT, 24, 8L);

    final JCGLArrayObjectType ai = go.arrayObjectAllocate(b);
    Assert.assertFalse(ai.isDeleted());
    Assert.assertEquals(
      (long) b.getMaximumVertexAttributes(),
      (long) ai.getMaximumVertexAttributes());

    for (int index = 0; index < ai.getMaximumVertexAttributes(); ++index) {
      Assert.assertEquals(b.getAttributeAt(index), ai.getAttributeAt(index));
    }
  }

  @Test public final void testArrayBindIdentity()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLArrayBuffersType ga = i.getArrayBuffers();
    final JCGLArrayObjectsType go = i.getArrayObjects();

    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);
    final JCGLArrayObjectBuilderType b = go.arrayObjectNewBuilder();
    Assert.assertTrue(b.getMaximumVertexAttributes() >= 16);

    b.setAttributeFloatingPoint(
      0, a, 4, JCGLScalarType.TYPE_FLOAT, 16, 0L, false);

    final JCGLArrayObjectType ai = go.arrayObjectAllocate(b);
    Assert.assertFalse(go.arrayObjectGetCurrentlyBound().isPresent());

    go.arrayObjectBind(ai);
    Assert.assertEquals(go.arrayObjectGetCurrentlyBound(), Optional.of(ai));

    go.arrayObjectUnbind();
    Assert.assertFalse(go.arrayObjectGetCurrentlyBound().isPresent());
  }

  @Test public final void testArrayBindDeleted()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLArrayBuffersType ga = i.getArrayBuffers();
    final JCGLArrayObjectsType go = i.getArrayObjects();

    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);
    final JCGLArrayObjectBuilderType b = go.arrayObjectNewBuilder();
    Assert.assertTrue(b.getMaximumVertexAttributes() >= 16);

    b.setAttributeFloatingPoint(
      0, a, 4, JCGLScalarType.TYPE_FLOAT, 16, 0L, false);

    final JCGLArrayObjectType ai = go.arrayObjectAllocate(b);
    Assert.assertFalse(go.arrayObjectGetCurrentlyBound().isPresent());

    go.arrayObjectDelete(ai);

    this.expected.expect(JCGLExceptionDeleted.class);
    go.arrayObjectBind(ai);
  }

  @Test public final void testArrayBindDeleteBind()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLArrayBuffersType ga = i.getArrayBuffers();
    final JCGLArrayObjectsType go = i.getArrayObjects();

    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);
    final JCGLArrayObjectBuilderType b = go.arrayObjectNewBuilder();
    Assert.assertTrue(b.getMaximumVertexAttributes() >= 16);

    b.setAttributeFloatingPoint(
      0, a, 4, JCGLScalarType.TYPE_FLOAT, 16, 0L, false);

    final JCGLArrayObjectType ai = go.arrayObjectAllocate(b);
    go.arrayObjectBind(ai);
    Assert.assertEquals(Optional.of(ai), go.arrayObjectGetCurrentlyBound());

    go.arrayObjectDelete(ai);
    Assert.assertFalse(go.arrayObjectGetCurrentlyBound().isPresent());
  }

  @Test public final void testArrayBindDeleteBindPreserves()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLArrayBuffersType ga = i.getArrayBuffers();
    final JCGLArrayObjectsType go = i.getArrayObjects();

    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);
    final JCGLArrayObjectBuilderType b = go.arrayObjectNewBuilder();
    Assert.assertTrue(b.getMaximumVertexAttributes() >= 16);

    b.setAttributeFloatingPoint(
      0, a, 4, JCGLScalarType.TYPE_FLOAT, 16, 0L, false);

    final JCGLArrayObjectType a0 = go.arrayObjectAllocate(b);
    final JCGLArrayObjectType a1 = go.arrayObjectAllocate(b);
    go.arrayObjectBind(a0);
    Assert.assertEquals(Optional.of(a0), go.arrayObjectGetCurrentlyBound());

    go.arrayObjectDelete(a1);
    Assert.assertEquals(Optional.of(a0), go.arrayObjectGetCurrentlyBound());
  }

  @Test public final void testArrayDeleteIdentity()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLArrayBuffersType ga = i.getArrayBuffers();
    final JCGLArrayObjectsType go = i.getArrayObjects();

    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);
    final JCGLArrayObjectBuilderType b = go.arrayObjectNewBuilder();
    Assert.assertTrue(b.getMaximumVertexAttributes() >= 16);

    b.setAttributeFloatingPoint(
      0, a, 4, JCGLScalarType.TYPE_FLOAT, 16, 0L, false);

    final JCGLArrayObjectType ai = go.arrayObjectAllocate(b);
    go.arrayObjectDelete(ai);
    Assert.assertTrue(ai.isDeleted());
  }

  @Test public final void testArrayDeleteDeleted()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLArrayBuffersType ga = i.getArrayBuffers();
    final JCGLArrayObjectsType go = i.getArrayObjects();

    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);
    final JCGLArrayObjectBuilderType b = go.arrayObjectNewBuilder();
    Assert.assertTrue(b.getMaximumVertexAttributes() >= 16);

    b.setAttributeFloatingPoint(
      0, a, 4, JCGLScalarType.TYPE_FLOAT, 16, 0L, false);

    final JCGLArrayObjectType ai = go.arrayObjectAllocate(b);
    go.arrayObjectDelete(ai);

    this.expected.expect(JCGLExceptionDeleted.class);
    go.arrayObjectDelete(ai);
  }

  protected static final class Interfaces
  {
    private final JCGLContextType      context;
    private final JCGLArrayBuffersType array_buffers;
    private final JCGLArrayObjectsType array_objects;

    public Interfaces(
      final JCGLContextType in_context,
      final JCGLArrayBuffersType in_array_buffers,
      final JCGLArrayObjectsType in_array_objects)
    {
      this.context = NullCheck.notNull(in_context);
      this.array_buffers = NullCheck.notNull(in_array_buffers);
      this.array_objects = NullCheck.notNull(in_array_objects);
    }

    public JCGLContextType getContext()
    {
      return this.context;
    }

    public JCGLArrayBuffersType getArrayBuffers()
    {
      return this.array_buffers;
    }

    public JCGLArrayObjectsType getArrayObjects()
    {
      return this.array_objects;
    }
  }
}
