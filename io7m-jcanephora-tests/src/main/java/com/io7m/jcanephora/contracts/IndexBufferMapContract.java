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
package com.io7m.jcanephora.contracts;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.ArrayBuffer;
import com.io7m.jcanephora.ArrayBufferAttributeDescriptor;
import com.io7m.jcanephora.ArrayBufferTypeDescriptor;
import com.io7m.jcanephora.CursorReadableIndex;
import com.io7m.jcanephora.CursorWritableIndex;
import com.io7m.jcanephora.IndexBuffer;
import com.io7m.jcanephora.IndexBufferWritableMap;
import com.io7m.jcanephora.JCGLArrayBuffers;
import com.io7m.jcanephora.JCGLArrayBuffersMapped;
import com.io7m.jcanephora.JCGLErrorCodes;
import com.io7m.jcanephora.JCGLRuntimeException;
import com.io7m.jcanephora.JCGLIndexBuffers;
import com.io7m.jcanephora.JCGLIndexBuffersMapped;
import com.io7m.jcanephora.JCGLScalarType;
import com.io7m.jcanephora.JCGLUnsignedType;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.UsageHint;
import com.io7m.jcanephora.jogl.IndexBufferReadableMap;

public abstract class IndexBufferMapContract implements TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract JCGLArrayBuffers getGLArrayBuffers(
    final TestContext tc);

  public abstract JCGLArrayBuffersMapped getGLArrayBuffersMapped(
    final TestContext tc);

  public abstract JCGLErrorCodes getGLErrorCodes(
    TestContext tc);

  public abstract JCGLIndexBuffers getGLIndexBuffers(
    final TestContext tc);

  public abstract JCGLIndexBuffersMapped getGLIndexBuffersMapped(
    final TestContext tc);

  /**
   * Mapping a deleted buffer read-only fails.
   * 
   * @throws JCGLRuntimeException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testMapReadDeleted()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLIndexBuffers gi = this.getGLIndexBuffers(tc);
    final JCGLIndexBuffersMapped gm = this.getGLIndexBuffersMapped(tc);

    final IndexBuffer ib =
      gi.indexBufferAllocateType(JCGLUnsignedType.TYPE_UNSIGNED_BYTE, 25);

    gi.indexBufferDelete(ib);
    gm.indexBufferMapRead(ib);
  }

  /**
   * Mapping a deleted buffer write-only fails.
   * 
   * @throws JCGLRuntimeException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testMapWriteDeleted()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLIndexBuffers gi = this.getGLIndexBuffers(tc);
    final JCGLIndexBuffersMapped gm = this.getGLIndexBuffersMapped(tc);

    final IndexBuffer ib =
      gi.indexBufferAllocateType(JCGLUnsignedType.TYPE_UNSIGNED_BYTE, 25);

    gi.indexBufferDelete(ib);
    gm.indexBufferMapWrite(ib);
  }

  /**
   * An index buffer of less than 256 elements has type 'unsigned byte' and
   * reading/writing is correct.
   */

  @Test public final void testReadWriteByte()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLIndexBuffers gi = this.getGLIndexBuffers(tc);
    final JCGLIndexBuffersMapped gm = this.getGLIndexBuffersMapped(tc);

    final ArrayList<ArrayBufferAttributeDescriptor> abs =
      new ArrayList<ArrayBufferAttributeDescriptor>();
    abs.add(new ArrayBufferAttributeDescriptor(
      "position",
      JCGLScalarType.TYPE_BYTE,
      1));
    final ArrayBufferTypeDescriptor d = new ArrayBufferTypeDescriptor(abs);

    final ArrayBuffer a =
      ga.arrayBufferAllocate(255, d, UsageHint.USAGE_STATIC_DRAW);
    final IndexBuffer ib = gi.indexBufferAllocate(a, 1);

    Assert.assertTrue(ib.getType() == JCGLUnsignedType.TYPE_UNSIGNED_BYTE);

    {
      final IndexBufferWritableMap map = gm.indexBufferMapWrite(ib);
      Assert.assertEquals(ib, map.getIndexBuffer());
      final CursorWritableIndex cursor = map.getCursor();
      for (int index = 0; index < ib.getRange().getInterval(); ++index) {
        cursor.putIndex(index);
      }
    }

    gm.indexBufferUnmap(ib);

    {
      final IndexBufferReadableMap map = gm.indexBufferMapRead(ib);
      final CursorReadableIndex cursor = map.getCursor();
      for (int index = 0; index < ib.getRange().getInterval(); ++index) {
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
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLIndexBuffers gi = this.getGLIndexBuffers(tc);
    final JCGLIndexBuffersMapped gm = this.getGLIndexBuffersMapped(tc);

    final ArrayList<ArrayBufferAttributeDescriptor> abs =
      new ArrayList<ArrayBufferAttributeDescriptor>();
    abs.add(new ArrayBufferAttributeDescriptor(
      "position",
      JCGLScalarType.TYPE_BYTE,
      1));
    final ArrayBufferTypeDescriptor d = new ArrayBufferTypeDescriptor(abs);

    final ArrayBuffer a =
      ga.arrayBufferAllocate(70000, d, UsageHint.USAGE_STATIC_DRAW);
    final IndexBuffer ib = gi.indexBufferAllocate(a, 1);

    Assert.assertTrue(ib.getType() == JCGLUnsignedType.TYPE_UNSIGNED_INT);

    {
      final IndexBufferWritableMap map = gm.indexBufferMapWrite(ib);
      Assert.assertEquals(ib, map.getIndexBuffer());
      final CursorWritableIndex cursor = map.getCursor();
      for (int index = 0; index < ib.getRange().getInterval(); ++index) {
        cursor.putIndex(index);
      }
    }

    gm.indexBufferUnmap(ib);

    {
      final IndexBufferReadableMap map = gm.indexBufferMapRead(ib);
      final CursorReadableIndex cursor = map.getCursor();
      for (int index = 0; index < ib.getRange().getInterval(); ++index) {
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
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLIndexBuffers gi = this.getGLIndexBuffers(tc);
    final JCGLIndexBuffersMapped gm = this.getGLIndexBuffersMapped(tc);

    final ArrayList<ArrayBufferAttributeDescriptor> abs =
      new ArrayList<ArrayBufferAttributeDescriptor>();
    abs.add(new ArrayBufferAttributeDescriptor(
      "position",
      JCGLScalarType.TYPE_BYTE,
      1));
    final ArrayBufferTypeDescriptor d = new ArrayBufferTypeDescriptor(abs);

    final ArrayBuffer a =
      ga.arrayBufferAllocate(65535, d, UsageHint.USAGE_STATIC_DRAW);
    final IndexBuffer ib = gi.indexBufferAllocate(a, 1);

    Assert.assertTrue(ib.getType() == JCGLUnsignedType.TYPE_UNSIGNED_SHORT);

    {
      final IndexBufferWritableMap map = gm.indexBufferMapWrite(ib);
      Assert.assertEquals(ib, map.getIndexBuffer());
      final CursorWritableIndex cursor = map.getCursor();
      for (int index = 0; index < ib.getRange().getInterval(); ++index) {
        cursor.putIndex(index);
      }
    }

    gm.indexBufferUnmap(ib);

    {
      final IndexBufferReadableMap map = gm.indexBufferMapRead(ib);
      final CursorReadableIndex cursor = map.getCursor();
      for (int index = 0; index < ib.getRange().getInterval(); ++index) {
        final int value = cursor.getIndex();
        Assert.assertTrue(value == index);
      }
    }
  }

  /**
   * Unmapping a deleted buffer fails.
   * 
   * @throws JCGLRuntimeException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testUnmapDeleted()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLIndexBuffers gi = this.getGLIndexBuffers(tc);
    final JCGLIndexBuffersMapped gm = this.getGLIndexBuffersMapped(tc);

    final IndexBuffer ib =
      gi.indexBufferAllocateType(JCGLUnsignedType.TYPE_UNSIGNED_BYTE, 25);

    gm.indexBufferMapRead(ib);
    gi.indexBufferDelete(ib);
    gm.indexBufferUnmap(ib);
  }
}
