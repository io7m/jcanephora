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
import com.io7m.jcanephora.ArrayDescriptor;
import com.io7m.jcanephora.ArrayDescriptorBuilderType;
import com.io7m.jcanephora.CursorReadableIndexType;
import com.io7m.jcanephora.CursorWritableIndexType;
import com.io7m.jcanephora.IndexBufferReadMappedType;
import com.io7m.jcanephora.IndexBufferReadType;
import com.io7m.jcanephora.IndexBufferType;
import com.io7m.jcanephora.IndexBufferUpdateMappedType;
import com.io7m.jcanephora.IndexBufferUsableType;
import com.io7m.jcanephora.JCGLErrorCodesType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionAttributeDuplicate;
import com.io7m.jcanephora.JCGLExceptionBufferMappedMultiple;
import com.io7m.jcanephora.JCGLExceptionBufferMappedNot;
import com.io7m.jcanephora.JCGLExceptionDeleted;
import com.io7m.jcanephora.JCGLExceptionWrongContext;
import com.io7m.jcanephora.JCGLScalarType;
import com.io7m.jcanephora.JCGLUnsignedType;
import com.io7m.jcanephora.UsageHint;
import com.io7m.jcanephora.api.JCGLArrayBuffersMappedType;
import com.io7m.jcanephora.api.JCGLArrayBuffersType;
import com.io7m.jcanephora.api.JCGLIndexBuffersMappedType;
import com.io7m.jcanephora.api.JCGLIndexBuffersType;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.TestUtilities;
import com.io7m.jcanephora.tests.types.PseudoIndexBuffer;
import com.io7m.jnull.NullCheckException;
import com.io7m.jranges.RangeInclusiveL;
import com.io7m.junreachable.UnreachableCodeException;

@SuppressWarnings({ "null" }) public abstract class IndexBufferMapContract implements
  TestContract
{
  private static ArrayDescriptor makeDescriptor()
  {
    try {
      final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
      b.addAttribute(ArrayAttributeDescriptor.newAttribute(
        "position",
        JCGLScalarType.TYPE_BYTE,
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
    final TestContext tc);

  public abstract JCGLArrayBuffersMappedType getGLArrayBuffersMapped(
    final TestContext tc);

  public abstract JCGLErrorCodesType getGLErrorCodes(
    TestContext tc);

  public abstract JCGLIndexBuffersType getGLIndexBuffers(
    final TestContext tc);

  public abstract JCGLIndexBuffersMappedType getGLIndexBuffersMapped(
    final TestContext tc);

  /**
   * Mapping a deleted buffer read-only fails.
   */

  @Test(expected = JCGLExceptionDeleted.class) public final
    void
    testMapReadDeleted()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLIndexBuffersType gi = this.getGLIndexBuffers(tc);
    final JCGLIndexBuffersMappedType gm = this.getGLIndexBuffersMapped(tc);

    final IndexBufferType ib =
      gi.indexBufferAllocateType(
        JCGLUnsignedType.TYPE_UNSIGNED_BYTE,
        25,
        UsageHint.USAGE_STATIC_DRAW);

    gi.indexBufferDelete(ib);
    gm.indexBufferMapRead(ib);
  }

  /**
   * Mapping a buffer multiple times fails.
   */

  @Test(expected = JCGLExceptionBufferMappedMultiple.class) public final
    void
    testMapReadMultiple()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLIndexBuffersType gi = this.getGLIndexBuffers(tc);
    final JCGLIndexBuffersMappedType gm = this.getGLIndexBuffersMapped(tc);

    final IndexBufferType ib =
      gi.indexBufferAllocateType(
        JCGLUnsignedType.TYPE_UNSIGNED_BYTE,
        25,
        UsageHint.USAGE_STATIC_DRAW);

    gm.indexBufferMapRead(ib);
    gm.indexBufferMapRead(ib);
  }

  /**
   * Mapping a null buffer fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testMapReadNull()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLIndexBuffersMappedType gm = this.getGLIndexBuffersMapped(tc);

    gm.indexBufferMapRead((IndexBufferUsableType) TestUtilities
      .actuallyNull());
  }

  @Test(expected = JCGLExceptionWrongContext.class) public final
    void
    testMapReadWrongContext()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLIndexBuffersMappedType gm = this.getGLIndexBuffersMapped(tc);

    final PseudoIndexBuffer ib =
      new PseudoIndexBuffer(
        JCGLUnsignedType.TYPE_UNSIGNED_BYTE,
        new RangeInclusiveL(0, 0),
        UsageHint.USAGE_STATIC_DRAW);
    gm.indexBufferMapRead(ib);
  }

  /**
   * Mapping a deleted buffer write-only fails.
   */

  @Test(expected = JCGLExceptionDeleted.class) public final
    void
    testMapWriteDeleted()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLIndexBuffersType gi = this.getGLIndexBuffers(tc);
    final JCGLIndexBuffersMappedType gm = this.getGLIndexBuffersMapped(tc);

    final IndexBufferType ib =
      gi.indexBufferAllocateType(
        JCGLUnsignedType.TYPE_UNSIGNED_BYTE,
        25,
        UsageHint.USAGE_STATIC_DRAW);

    gi.indexBufferDelete(ib);
    gm.indexBufferMapWrite(ib);
  }

  /**
   * Mapping a null buffer fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testMapWriteNull()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLIndexBuffersMappedType gm = this.getGLIndexBuffersMapped(tc);

    gm.indexBufferMapWrite((IndexBufferType) TestUtilities.actuallyNull());
  }

  @Test(expected = JCGLExceptionWrongContext.class) public final
    void
    testMapWriteWrongContext()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLIndexBuffersMappedType gm = this.getGLIndexBuffersMapped(tc);

    final PseudoIndexBuffer ib =
      new PseudoIndexBuffer(
        JCGLUnsignedType.TYPE_UNSIGNED_BYTE,
        new RangeInclusiveL(0, 0),
        UsageHint.USAGE_STATIC_DRAW);
    gm.indexBufferMapWrite(ib);
  }

  /**
   * An index buffer of less than 256 elements has type 'unsigned byte' and
   * reading/writing is correct.
   */

  @Test public final void testReadWriteByte()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType ga = this.getGLArrayBuffers(tc);
    final JCGLIndexBuffersType gi = this.getGLIndexBuffers(tc);
    final JCGLIndexBuffersMappedType gm = this.getGLIndexBuffersMapped(tc);

    final ArrayDescriptor d = IndexBufferMapContract.makeDescriptor();
    final ArrayBufferType a =
      ga.arrayBufferAllocate(255, d, UsageHint.USAGE_STATIC_DRAW);
    final IndexBufferType ib =
      gi.indexBufferAllocate(a, 1, UsageHint.USAGE_STATIC_DRAW);

    Assert
      .assertTrue(ib.indexGetType() == JCGLUnsignedType.TYPE_UNSIGNED_BYTE);

    {
      final IndexBufferUpdateMappedType map = gm.indexBufferMapWrite(ib);
      Assert.assertEquals(ib, map.getIndexBuffer());
      final CursorWritableIndexType cursor = map.getCursor();
      for (int index = 0; index < ib.bufferGetRange().getInterval(); ++index) {
        cursor.putIndex(index);
      }
    }

    gm.indexBufferUnmap(ib);

    {
      final IndexBufferReadMappedType map = gm.indexBufferMapRead(ib);
      final CursorReadableIndexType cursor = map.getCursor();
      for (int index = 0; index < ib.bufferGetRange().getInterval(); ++index) {
        final int value = cursor.getIndex();
        Assert.assertTrue(value == index);
      }
    }
  }

  /**
   * An index buffer of less than 4294967296 elements has type 'unsigned
   * short' and reading/writing is correct.
   */

  @Test public final void testReadWriteInt()
    throws JCGLExceptionBufferMappedNot,
      JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType ga = this.getGLArrayBuffers(tc);
    final JCGLIndexBuffersType gi = this.getGLIndexBuffers(tc);
    final JCGLIndexBuffersMappedType gm = this.getGLIndexBuffersMapped(tc);

    final ArrayDescriptor d = IndexBufferMapContract.makeDescriptor();
    final ArrayBufferType a =
      ga.arrayBufferAllocate(70000, d, UsageHint.USAGE_STATIC_DRAW);
    final IndexBufferType ib =
      gi.indexBufferAllocate(a, 1, UsageHint.USAGE_STATIC_DRAW);

    Assert
      .assertTrue(ib.indexGetType() == JCGLUnsignedType.TYPE_UNSIGNED_INT);

    {
      final IndexBufferUpdateMappedType map = gm.indexBufferMapWrite(ib);
      Assert.assertEquals(ib, map.getIndexBuffer());
      final CursorWritableIndexType cursor = map.getCursor();
      for (int index = 0; index < ib.bufferGetRange().getInterval(); ++index) {
        cursor.putIndex(index);
      }
    }

    gm.indexBufferUnmap(ib);

    {
      final IndexBufferReadMappedType map = gm.indexBufferMapRead(ib);
      final CursorReadableIndexType cursor = map.getCursor();
      for (int index = 0; index < ib.bufferGetRange().getInterval(); ++index) {
        final int value = cursor.getIndex();
        Assert.assertTrue(value == index);
      }
    }
  }

  /**
   * An index buffer of less than 65536 elements has type 'unsigned short' and
   * reading/writing is correct.
   */

  @Test public final void testReadWriteShort()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffersType ga = this.getGLArrayBuffers(tc);
    final JCGLIndexBuffersType gi = this.getGLIndexBuffers(tc);
    final JCGLIndexBuffersMappedType gm = this.getGLIndexBuffersMapped(tc);

    final ArrayDescriptor d = IndexBufferMapContract.makeDescriptor();
    final ArrayBufferType a =
      ga.arrayBufferAllocate(65535, d, UsageHint.USAGE_STATIC_DRAW);
    final IndexBufferType ib =
      gi.indexBufferAllocate(a, 1, UsageHint.USAGE_STATIC_DRAW);

    Assert
      .assertTrue(ib.indexGetType() == JCGLUnsignedType.TYPE_UNSIGNED_SHORT);

    {
      final IndexBufferUpdateMappedType map = gm.indexBufferMapWrite(ib);
      Assert.assertEquals(ib, map.getIndexBuffer());
      final CursorWritableIndexType cursor = map.getCursor();
      for (int index = 0; index < ib.bufferGetRange().getInterval(); ++index) {
        cursor.putIndex(index);
      }
    }

    gm.indexBufferUnmap(ib);

    {
      final IndexBufferReadType map = gm.indexBufferMapRead(ib);
      final CursorReadableIndexType cursor = map.getCursor();
      for (int index = 0; index < ib.bufferGetRange().getInterval(); ++index) {
        final int value = cursor.getIndex();
        Assert.assertTrue(value == index);
      }
    }
  }

  /**
   * Unmapping a deleted buffer fails.
   */

  @Test(expected = JCGLExceptionDeleted.class) public final
    void
    testUnmapDeleted()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLIndexBuffersType gi = this.getGLIndexBuffers(tc);
    final JCGLIndexBuffersMappedType gm = this.getGLIndexBuffersMapped(tc);

    final IndexBufferType ib =
      gi.indexBufferAllocateType(
        JCGLUnsignedType.TYPE_UNSIGNED_BYTE,
        25,
        UsageHint.USAGE_STATIC_DRAW);

    gm.indexBufferMapRead(ib);
    gi.indexBufferDelete(ib);
    gm.indexBufferUnmap(ib);
  }

  /**
   * Unmapping a buffer that isn't mapped fails.
   */

  @Test(expected = JCGLExceptionBufferMappedNot.class) public final
    void
    testUnmapNot()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLIndexBuffersType gi = this.getGLIndexBuffers(tc);
    final JCGLIndexBuffersMappedType gm = this.getGLIndexBuffersMapped(tc);

    final IndexBufferType ib =
      gi.indexBufferAllocateType(
        JCGLUnsignedType.TYPE_UNSIGNED_BYTE,
        25,
        UsageHint.USAGE_STATIC_DRAW);

    gm.indexBufferUnmap(ib);
  }

  /**
   * Unmapping a null buffer fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testUnmapNull()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLIndexBuffersMappedType gm = this.getGLIndexBuffersMapped(tc);

    gm.indexBufferUnmap((IndexBufferType) TestUtilities.actuallyNull());
  }

  @Test(expected = JCGLExceptionWrongContext.class) public final
    void
    testUnmapWrongContext()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLIndexBuffersMappedType gm = this.getGLIndexBuffersMapped(tc);

    final PseudoIndexBuffer ib =
      new PseudoIndexBuffer(
        JCGLUnsignedType.TYPE_UNSIGNED_BYTE,
        new RangeInclusiveL(0, 0),
        UsageHint.USAGE_STATIC_DRAW);
    gm.indexBufferUnmap(ib);
  }
}
