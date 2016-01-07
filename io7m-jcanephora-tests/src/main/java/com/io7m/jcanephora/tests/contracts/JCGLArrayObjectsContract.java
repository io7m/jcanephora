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
import com.io7m.jcanephora.core.JCGLArrayObjectUsableType;
import com.io7m.jcanephora.core.JCGLArrayVertexAttributeFloatingPointType;
import com.io7m.jcanephora.core.JCGLArrayVertexAttributeIntegralType;
import com.io7m.jcanephora.core.JCGLArrayVertexAttributeType;
import com.io7m.jcanephora.core.JCGLExceptionDeleted;
import com.io7m.jcanephora.core.JCGLExceptionObjectNotDeletable;
import com.io7m.jcanephora.core.JCGLExceptionWrongContext;
import com.io7m.jcanephora.core.JCGLIndexBufferType;
import com.io7m.jcanephora.core.JCGLReferableType;
import com.io7m.jcanephora.core.JCGLReferenceContainerType;
import com.io7m.jcanephora.core.JCGLScalarIntegralType;
import com.io7m.jcanephora.core.JCGLScalarType;
import com.io7m.jcanephora.core.JCGLUnsignedType;
import com.io7m.jcanephora.core.JCGLUsageHint;
import com.io7m.jcanephora.core.api.JCGLArrayBuffersType;
import com.io7m.jcanephora.core.api.JCGLArrayObjectsType;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.core.api.JCGLIndexBuffersType;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeCheckException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Optional;
import java.util.Set;

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

    b.setAttributeFloatingPointWithDivisor(
      0, a, 4, JCGLScalarType.TYPE_FLOAT, 16, 0L, false, 23);
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
      Assert.assertEquals(23L, (long) at.getDivisor());
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
      Assert.assertEquals(0L, (long) at.getDivisor());
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
      Assert.assertEquals(0L, (long) at.getDivisor());
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

    Assert.assertFalse(c_main.contextIsCurrent());
    Assert.assertTrue(c_alt.contextIsCurrent());

    c_alt.contextReleaseCurrent();
    c_main.contextMakeCurrent();
    final JCGLArrayBufferType a =
      ga_main.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);

    c_main.contextReleaseCurrent();
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

    Assert.assertFalse(c_main.contextIsCurrent());
    Assert.assertTrue(c_alt.contextIsCurrent());

    c_alt.contextReleaseCurrent();
    c_main.contextMakeCurrent();
    final JCGLArrayBufferType a =
      ga_main.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);

    c_main.contextReleaseCurrent();
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

  @Test public final void testArrayAllocateIndexDeleted()
  {
    final Interfaces is = this.getInterfaces("main");
    final JCGLArrayBuffersType ga = is.getArrayBuffers();
    final JCGLIndexBuffersType gi = is.getIndexBuffers();
    final JCGLArrayObjectsType go = is.getArrayObjects();

    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);
    final JCGLIndexBufferType i = gi.indexBufferAllocate(
      100L,
      JCGLUnsignedType.TYPE_UNSIGNED_BYTE,
      JCGLUsageHint.USAGE_STATIC_DRAW);

    final JCGLArrayObjectBuilderType b = go.arrayObjectNewBuilder();
    b.setIndexBuffer(i);
    gi.indexBufferDelete(i);

    this.expected.expect(JCGLExceptionDeleted.class);
    go.arrayObjectAllocate(b);
  }

  @Test public final void testArrayAllocate()
  {
    final Interfaces is = this.getInterfaces("main");
    final JCGLArrayBuffersType ga = is.getArrayBuffers();
    final JCGLIndexBuffersType gi = is.getIndexBuffers();
    final JCGLArrayObjectsType go = is.getArrayObjects();

    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);
    final JCGLIndexBufferType i = gi.indexBufferAllocate(
      100L,
      JCGLUnsignedType.TYPE_UNSIGNED_BYTE,
      JCGLUsageHint.USAGE_STATIC_DRAW);

    final JCGLArrayObjectBuilderType b = go.arrayObjectNewBuilder();
    Assert.assertTrue(b.getMaximumVertexAttributes() >= 16);

    b.setAttributeFloatingPoint(
      0, a, 4, JCGLScalarType.TYPE_FLOAT, 16, 0L, false);
    b.setAttributeFloatingPoint(
      1, a, 3, JCGLScalarType.TYPE_INT, 20, 4L, true);
    b.setAttributeIntegral(
      4, a, 2, JCGLScalarIntegralType.TYPE_INT, 24, 8L);
    b.setIndexBuffer(i);

    final JCGLArrayObjectType ai = go.arrayObjectAllocate(b);
    Assert.assertFalse(ai.isDeleted());
    Assert.assertEquals(
      (long) b.getMaximumVertexAttributes(),
      (long) ai.getMaximumVertexAttributes());

    for (int index = 0; index < ai.getMaximumVertexAttributes(); ++index) {
      Assert.assertEquals(b.getAttributeAt(index), ai.getAttributeAt(index));
    }

    Assert.assertEquals(Optional.of(i), gi.indexBufferGetCurrentlyBound());
    Assert.assertEquals(Optional.of(i), ai.getIndexBufferBound());

    final Set<JCGLReferableType> ai_refs = ai.getReferences();
    Assert.assertEquals(2L, (long) ai_refs.size());
    Assert.assertTrue(ai_refs.contains(a));
    Assert.assertTrue(ai_refs.contains(i));

    final Set<JCGLReferenceContainerType> a_refs = a.getReferringContainers();
    Assert.assertEquals(1L, (long) a_refs.size());
    Assert.assertTrue(a_refs.contains(ai));

    final Set<JCGLReferenceContainerType> i_refs = i.getReferringContainers();
    Assert.assertEquals(2L, (long) i_refs.size());
    Assert.assertTrue(i_refs.contains(ai));
    Assert.assertTrue(i_refs.contains(go.arrayObjectGetDefault()));
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

    final JCGLArrayObjectType ao0 = go.arrayObjectAllocate(b);
    Assert.assertEquals(ao0, go.arrayObjectGetCurrentlyBound());
    final JCGLArrayObjectType ao1 = go.arrayObjectAllocate(b);
    Assert.assertEquals(ao1, go.arrayObjectGetCurrentlyBound());

    go.arrayObjectBind(ao0);
    Assert.assertEquals(ao0, go.arrayObjectGetCurrentlyBound());
    go.arrayObjectBind(ao1);
    Assert.assertEquals(ao1, go.arrayObjectGetCurrentlyBound());

    go.arrayObjectUnbind();
    Assert.assertEquals(
      go.arrayObjectGetDefault(), go.arrayObjectGetCurrentlyBound());
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
    Assert.assertEquals(ai, go.arrayObjectGetCurrentlyBound());

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
    Assert.assertEquals(ai, go.arrayObjectGetCurrentlyBound());

    go.arrayObjectDelete(ai);
    Assert.assertEquals(
      go.arrayObjectGetDefault(), go.arrayObjectGetCurrentlyBound());
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
    Assert.assertEquals(a0, go.arrayObjectGetCurrentlyBound());

    go.arrayObjectDelete(a1);
    Assert.assertEquals(a0, go.arrayObjectGetCurrentlyBound());
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

    final Set<JCGLReferableType> ai_refs = ai.getReferences();
    Assert.assertEquals(0L, (long) ai_refs.size());

    final Set<JCGLReferenceContainerType> a_refs = a.getReferringContainers();
    Assert.assertEquals(0L, (long) a_refs.size());
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

  @Test public final void testArrayDeleteDefault()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLArrayBuffersType ga = i.getArrayBuffers();
    final JCGLArrayObjectsType go = i.getArrayObjects();

    final JCGLArrayObjectUsableType unsafe = go.arrayObjectGetDefault();
    if (unsafe instanceof JCGLArrayObjectType) {
      this.expected.expect(JCGLExceptionObjectNotDeletable.class);
      go.arrayObjectDelete((JCGLArrayObjectType) unsafe);
    }
  }

  @Test public final void testArrayBufferDeletion()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLArrayBuffersType ga = i.getArrayBuffers();
    final JCGLArrayObjectsType go = i.getArrayObjects();

    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);
    ga.arrayBufferUnbind();

    final JCGLArrayObjectBuilderType b = go.arrayObjectNewBuilder();
    b.setAttributeIntegral(
      0, a, 1, JCGLScalarIntegralType.TYPE_UNSIGNED_BYTE, 0, 0L);

    final JCGLArrayObjectType ao = go.arrayObjectAllocate(b);
    Assert.assertEquals(ao, go.arrayObjectGetCurrentlyBound());

    ga.arrayBufferDelete(a);
    go.arrayObjectBind(ao);

    final Set<JCGLReferableType> refs = ao.getReferences();
    Assert.assertEquals(1L, (long) refs.size());
    Assert.assertTrue(refs.contains(a));
  }

  @Test public final void testArrayDeleteIndex()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLArrayBuffersType ga = i.getArrayBuffers();
    final JCGLArrayObjectsType go = i.getArrayObjects();
    final JCGLIndexBuffersType gi = i.getIndexBuffers();

    final JCGLIndexBufferType ib = gi.indexBufferAllocate(
      100L,
      JCGLUnsignedType.TYPE_UNSIGNED_BYTE,
      JCGLUsageHint.USAGE_STATIC_DRAW);

    final JCGLArrayObjectBuilderType b = go.arrayObjectNewBuilder();
    Assert.assertTrue(b.getMaximumVertexAttributes() >= 16);
    b.setIndexBuffer(ib);

    final JCGLArrayObjectType ai_0 = go.arrayObjectAllocate(b);
    final JCGLArrayObjectType ai_1 = go.arrayObjectAllocate(b);
    final JCGLArrayObjectType ai_2 = go.arrayObjectAllocate(b);

    Assert.assertEquals(go.arrayObjectGetCurrentlyBound(), ai_2);
    Assert.assertEquals(Optional.of(ib), ai_0.getIndexBufferBound());
    Assert.assertEquals(Optional.of(ib), ai_1.getIndexBufferBound());
    Assert.assertEquals(Optional.of(ib), ai_2.getIndexBufferBound());
    Assert.assertEquals(Optional.of(ib), gi.indexBufferGetCurrentlyBound());

    final Set<JCGLReferenceContainerType> ib_refs = ib.getReferringContainers();
    final Set<JCGLReferableType> ai0_refs = ai_0.getReferences();
    final Set<JCGLReferableType> ai1_refs = ai_1.getReferences();
    final Set<JCGLReferableType> ai2_refs = ai_2.getReferences();

    Assert.assertEquals(4L, (long) ib_refs.size());
    Assert.assertTrue(ib_refs.contains(go.arrayObjectGetDefault()));
    Assert.assertTrue(ib_refs.contains(ai_0));
    Assert.assertTrue(ib_refs.contains(ai_1));
    Assert.assertTrue(ib_refs.contains(ai_2));

    Assert.assertEquals(1L, (long) ai0_refs.size());
    Assert.assertTrue(ai0_refs.contains(ib));
    Assert.assertEquals(1L, (long) ai1_refs.size());
    Assert.assertTrue(ai1_refs.contains(ib));
    Assert.assertEquals(1L, (long) ai2_refs.size());
    Assert.assertTrue(ai2_refs.contains(ib));

    gi.indexBufferDelete(ib);

    Assert.assertEquals(0L, (long) ib_refs.size());
    Assert.assertEquals(0L, (long) ai0_refs.size());
    Assert.assertEquals(0L, (long) ai1_refs.size());
    Assert.assertEquals(0L, (long) ai2_refs.size());
  }

  protected static final class Interfaces
  {
    private final JCGLContextType      context;
    private final JCGLIndexBuffersType index_buffers;
    private final JCGLArrayBuffersType array_buffers;
    private final JCGLArrayObjectsType array_objects;

    public Interfaces(
      final JCGLContextType in_context,
      final JCGLArrayBuffersType in_array_buffers,
      final JCGLIndexBuffersType in_index_buffers,
      final JCGLArrayObjectsType in_array_objects)
    {
      this.context = NullCheck.notNull(in_context);
      this.array_buffers = NullCheck.notNull(in_array_buffers);
      this.array_objects = NullCheck.notNull(in_array_objects);
      this.index_buffers = NullCheck.notNull(in_index_buffers);
    }

    public JCGLContextType getContext()
    {
      return this.context;
    }

    public JCGLIndexBuffersType getIndexBuffers()
    {
      return this.index_buffers;
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
