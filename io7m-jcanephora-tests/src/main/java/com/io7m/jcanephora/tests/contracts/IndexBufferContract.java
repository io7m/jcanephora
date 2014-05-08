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
import com.io7m.jcanephora.ArrayBufferType;
import com.io7m.jcanephora.ArrayBufferUsableType;
import com.io7m.jcanephora.ArrayDescriptor;
import com.io7m.jcanephora.ArrayDescriptorBuilderType;
import com.io7m.jcanephora.IndexBufferType;
import com.io7m.jcanephora.IndexBufferUpdateUnmapped;
import com.io7m.jcanephora.IndexBufferUpdateUnmappedType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionAttributeDuplicate;
import com.io7m.jcanephora.JCGLExceptionDeleted;
import com.io7m.jcanephora.JCGLScalarType;
import com.io7m.jcanephora.JCGLUnsignedType;
import com.io7m.jcanephora.UsageHint;
import com.io7m.jcanephora.api.JCGLArrayBuffersType;
import com.io7m.jcanephora.api.JCGLIndexBuffersType;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.TestUtilities;
import com.io7m.jnull.NullCheckException;
import com.io7m.jranges.RangeCheckException;
import com.io7m.junreachable.UnreachableCodeException;

@SuppressWarnings("null") public abstract class IndexBufferContract implements
  TestContract
{
  private static ArrayDescriptor makeDescriptor()
  {
    try {
      final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
      b.addAttribute(ArrayAttributeDescriptor.newAttribute(
        "position",
        JCGLScalarType.TYPE_SHORT,
        1));
      final ArrayDescriptor d = b.build();
      return d;
    } catch (final JCGLExceptionAttributeDuplicate x) {
      throw new UnreachableCodeException(x);
    }
  }

  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract JCGLArrayBuffersType getGLArrayBuffers(
    TestContext tc);

  public abstract JCGLIndexBuffersType getGLIndexBuffers(
    TestContext tc);

  /**
   * Allocating an index buffer from a deleted array buffer fails.
   */

  @Test(expected = JCGLExceptionDeleted.class) public final
    void
    testIndexBufferAllocateArrayDeleted()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType ga = this.getGLArrayBuffers(tc);
    final JCGLIndexBuffersType gi = this.getGLIndexBuffers(tc);

    final ArrayDescriptor d = IndexBufferContract.makeDescriptor();

    final ArrayBufferType a =
      ga.arrayBufferAllocate(1, d, UsageHint.USAGE_STATIC_DRAW);
    ga.arrayBufferDelete(a);
    gi.indexBufferAllocate(a, 0, UsageHint.USAGE_STATIC_DRAW);
  }

  /**
   * An allocated index buffer has the correct number of indices and index
   * type.
   */

  @Test public final void testIndexBufferAllocateByte()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType ga = this.getGLArrayBuffers(tc);
    final JCGLIndexBuffersType gi = this.getGLIndexBuffers(tc);

    final ArrayDescriptor d = IndexBufferContract.makeDescriptor();

    IndexBufferType i = null;
    ArrayBufferType a = null;

    try {
      a = ga.arrayBufferAllocate(255, d, UsageHint.USAGE_STATIC_DRAW);
      i = gi.indexBufferAllocate(a, 4, UsageHint.USAGE_STATIC_DRAW);
      Assert.assertEquals(
        JCGLUnsignedType.TYPE_UNSIGNED_BYTE,
        i.indexGetType());
      Assert.assertEquals(1, i.bufferGetElementSizeBytes());
      Assert.assertEquals(4, i.bufferGetRange().getInterval());
      Assert.assertEquals(4, i.resourceGetSizeBytes());
    } finally {
      if (a != null) {
        ga.arrayBufferDelete(a);
      }
      if (i != null) {
        gi.indexBufferDelete(i);
      }
    }
  }

  /**
   * An allocated index buffer has the correct number of indices and index
   * type.
   */

  @Test public final void testIndexBufferAllocateInt()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType ga = this.getGLArrayBuffers(tc);
    final JCGLIndexBuffersType gi = this.getGLIndexBuffers(tc);

    final ArrayDescriptor d = IndexBufferContract.makeDescriptor();

    IndexBufferType i = null;
    ArrayBufferType a = null;

    try {
      a = ga.arrayBufferAllocate(65536, d, UsageHint.USAGE_STATIC_DRAW);
      i = gi.indexBufferAllocate(a, 2, UsageHint.USAGE_STATIC_DRAW);
      Assert.assertEquals(
        JCGLUnsignedType.TYPE_UNSIGNED_INT,
        i.indexGetType());
      Assert.assertEquals(4, i.bufferGetElementSizeBytes());
      Assert.assertEquals(2, i.bufferGetRange().getInterval());
      Assert.assertEquals(8, i.resourceGetSizeBytes());
    } finally {
      if (a != null) {
        ga.arrayBufferDelete(a);
      }
      if (i != null) {
        gi.indexBufferDelete(i);
      }
    }
  }

  /**
   * Passing null to allocateIndexBuffer fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testIndexBufferAllocateNull()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLIndexBuffersType gi = this.getGLIndexBuffers(tc);

    gi.indexBufferAllocate(
      (ArrayBufferUsableType) TestUtilities.actuallyNull(),
      1,
      UsageHint.USAGE_STATIC_DRAW);
  }

  /**
   * An allocated index buffer has the correct number of indices and index
   * type.
   */

  @Test public final void testIndexBufferAllocateShort()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType ga = this.getGLArrayBuffers(tc);
    final JCGLIndexBuffersType gi = this.getGLIndexBuffers(tc);

    final ArrayDescriptor d = IndexBufferContract.makeDescriptor();

    IndexBufferType i = null;
    ArrayBufferType a = null;

    try {
      a = ga.arrayBufferAllocate(256, d, UsageHint.USAGE_STATIC_DRAW);
      i = gi.indexBufferAllocate(a, 4, UsageHint.USAGE_STATIC_DRAW);
      Assert.assertEquals(
        JCGLUnsignedType.TYPE_UNSIGNED_SHORT,
        i.indexGetType());
      Assert.assertEquals(2, i.bufferGetElementSizeBytes());
      Assert.assertEquals(4, i.bufferGetRange().getInterval());
      Assert.assertEquals(8, i.resourceGetSizeBytes());
    } finally {
      if (a != null) {
        ga.arrayBufferDelete(a);
      }
      if (i != null) {
        gi.indexBufferDelete(i);
      }
    }
  }

  /**
   * Allocating with a null type fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testIndexBufferAllocateTypeNull()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLIndexBuffersType gi = this.getGLIndexBuffers(tc);

    gi.indexBufferAllocateType(
      (JCGLUnsignedType) TestUtilities.actuallyNull(),
      1,
      UsageHint.USAGE_STATIC_DRAW);
  }

  /**
   * Allocating zero elements fails.
   */

  @Test(expected = RangeCheckException.class) public final
    void
    testIndexBufferAllocateTypeZeroElements()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLIndexBuffersType gi = this.getGLIndexBuffers(tc);

    gi.indexBufferAllocateType(
      JCGLUnsignedType.TYPE_UNSIGNED_BYTE,
      0,
      UsageHint.USAGE_STATIC_DRAW);
  }

  /**
   * Allocating zero elements fails.
   */

  @Test(expected = RangeCheckException.class) public final
    void
    testIndexBufferAllocateZeroElements()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType ga = this.getGLArrayBuffers(tc);
    final JCGLIndexBuffersType gi = this.getGLIndexBuffers(tc);

    final ArrayDescriptor d = IndexBufferContract.makeDescriptor();

    final ArrayBufferType a =
      ga.arrayBufferAllocate(1, d, UsageHint.USAGE_STATIC_DRAW);
    gi.indexBufferAllocate(a, 0, UsageHint.USAGE_STATIC_DRAW);
  }

  /**
   * Deleting a buffer twice fails.
   */

  @Test(expected = JCGLExceptionDeleted.class) public final
    void
    testIndexBufferDeleteDouble()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType ga = this.getGLArrayBuffers(tc);
    final JCGLIndexBuffersType gi = this.getGLIndexBuffers(tc);

    final ArrayDescriptor d = IndexBufferContract.makeDescriptor();

    IndexBufferType i;
    ArrayBufferType a = null;

    try {
      a = ga.arrayBufferAllocate(4, d, UsageHint.USAGE_STATIC_DRAW);
      i = gi.indexBufferAllocate(a, 10, UsageHint.USAGE_STATIC_DRAW);
      gi.indexBufferDelete(i);
    } catch (final Exception e) {
      throw new UnreachableCodeException(e);
    }

    gi.indexBufferDelete(i);
  }

  /**
   * Deleting a null buffer fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testIndexBufferDeleteNull()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLIndexBuffersType gi = this.getGLIndexBuffers(tc);

    gi.indexBufferDelete((IndexBufferType) TestUtilities.actuallyNull());
  }

  /**
   * Updating an index buffer works.
   */

  @Test public final void testIndexBufferUpdate()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType ga = this.getGLArrayBuffers(tc);
    final JCGLIndexBuffersType gi = this.getGLIndexBuffers(tc);
    final ArrayDescriptor d = IndexBufferContract.makeDescriptor();

    IndexBufferType i;
    ArrayBufferType a = null;

    try {
      a = ga.arrayBufferAllocate(255, d, UsageHint.USAGE_STATIC_DRAW);
      i = gi.indexBufferAllocate(a, 4, UsageHint.USAGE_STATIC_DRAW);
    } catch (final Throwable e) {
      throw new UnreachableCodeException(e);
    }

    final IndexBufferUpdateUnmappedType data =
      IndexBufferUpdateUnmapped.newReplacing(i);
    gi.indexBufferUpdate(data);
  }

  /**
   * Updating a deleted index buffer fails.
   */

  @Test(expected = JCGLExceptionDeleted.class) public final
    void
    testIndexBufferUpdateDeleted()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType ga = this.getGLArrayBuffers(tc);
    final JCGLIndexBuffersType gi = this.getGLIndexBuffers(tc);
    final ArrayDescriptor d = IndexBufferContract.makeDescriptor();

    IndexBufferType i = null;
    ArrayBufferType a = null;

    try {
      a = ga.arrayBufferAllocate(255, d, UsageHint.USAGE_STATIC_DRAW);
      i = gi.indexBufferAllocate(a, 4, UsageHint.USAGE_STATIC_DRAW);
    } catch (final Throwable e) {
      throw new UnreachableCodeException(e);
    }

    final IndexBufferUpdateUnmappedType data =
      IndexBufferUpdateUnmapped.newReplacing(i);
    gi.indexBufferDelete(i);
    gi.indexBufferUpdate(data);
  }

  /**
   * Updating an index buffer with null data fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testIndexBufferUpdateNullData()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLIndexBuffersType gi = this.getGLIndexBuffers(tc);

    gi.indexBufferUpdate((IndexBufferUpdateUnmappedType) TestUtilities
      .actuallyNull());
  }
}
