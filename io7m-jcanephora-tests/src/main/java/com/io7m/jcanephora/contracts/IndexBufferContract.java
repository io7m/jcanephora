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
import com.io7m.jcanephora.IndexBuffer;
import com.io7m.jcanephora.IndexBufferWritableData;
import com.io7m.jcanephora.JCGLArrayBuffers;
import com.io7m.jcanephora.JCGLRuntimeException;
import com.io7m.jcanephora.JCGLIndexBuffers;
import com.io7m.jcanephora.JCGLScalarType;
import com.io7m.jcanephora.JCGLUnsignedType;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.UsageHint;

public abstract class IndexBufferContract implements TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract JCGLArrayBuffers getGLArrayBuffers(
    TestContext tc);

  public abstract JCGLIndexBuffers getGLIndexBuffers(
    TestContext tc);

  /**
   * Allocating an index buffer from a deleted array buffer fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testIndexBufferAllocateArrayDeleted()
      throws ConstraintError,
        JCGLRuntimeException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLIndexBuffers gi = this.getGLIndexBuffers(tc);

    final ArrayList<ArrayBufferAttributeDescriptor> abs =
      new ArrayList<ArrayBufferAttributeDescriptor>();
    abs.add(new ArrayBufferAttributeDescriptor(
      "position",
      JCGLScalarType.TYPE_SHORT,
      1));
    final ArrayBufferTypeDescriptor d = new ArrayBufferTypeDescriptor(abs);

    final ArrayBuffer a =
      ga.arrayBufferAllocate(1, d, UsageHint.USAGE_STATIC_DRAW);
    ga.arrayBufferDelete(a);
    gi.indexBufferAllocate(a, 0);
  }

  /**
   * An allocated index buffer has the correct number of indices and index
   * type.
   */

  @Test public final void testIndexBufferAllocateByte()
    throws ConstraintError,
      JCGLRuntimeException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLIndexBuffers gi = this.getGLIndexBuffers(tc);

    final ArrayList<ArrayBufferAttributeDescriptor> abs =
      new ArrayList<ArrayBufferAttributeDescriptor>();
    abs.add(new ArrayBufferAttributeDescriptor(
      "position",
      JCGLScalarType.TYPE_SHORT,
      1));
    final ArrayBufferTypeDescriptor d = new ArrayBufferTypeDescriptor(abs);

    IndexBuffer i = null;
    ArrayBuffer a = null;

    try {
      a = ga.arrayBufferAllocate(255, d, UsageHint.USAGE_STATIC_DRAW);
      i = gi.indexBufferAllocate(a, 4);
      Assert.assertEquals(JCGLUnsignedType.TYPE_UNSIGNED_BYTE, i.getType());
      Assert.assertEquals(1, i.getElementSizeBytes());
      Assert.assertEquals(4, i.getRange().getInterval());
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
    throws ConstraintError,
      JCGLRuntimeException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLIndexBuffers gi = this.getGLIndexBuffers(tc);

    final ArrayList<ArrayBufferAttributeDescriptor> abs =
      new ArrayList<ArrayBufferAttributeDescriptor>();
    abs.add(new ArrayBufferAttributeDescriptor(
      "position",
      JCGLScalarType.TYPE_SHORT,
      1));
    final ArrayBufferTypeDescriptor d = new ArrayBufferTypeDescriptor(abs);

    IndexBuffer i = null;
    ArrayBuffer a = null;

    try {
      a = ga.arrayBufferAllocate(65536, d, UsageHint.USAGE_STATIC_DRAW);
      i = gi.indexBufferAllocate(a, 2);
      Assert.assertEquals(JCGLUnsignedType.TYPE_UNSIGNED_INT, i.getType());
      Assert.assertEquals(4, i.getElementSizeBytes());
      Assert.assertEquals(2, i.getRange().getInterval());
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

  @Test(expected = ConstraintError.class) public final
    void
    testIndexBufferAllocateNull()
      throws ConstraintError,
        JCGLRuntimeException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLIndexBuffers gi = this.getGLIndexBuffers(tc);

    gi.indexBufferAllocate(null, 1);
  }

  /**
   * An allocated index buffer has the correct number of indices and index
   * type.
   */

  @Test public final void testIndexBufferAllocateShort()
    throws ConstraintError,
      JCGLRuntimeException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLIndexBuffers gi = this.getGLIndexBuffers(tc);

    final ArrayList<ArrayBufferAttributeDescriptor> abs =
      new ArrayList<ArrayBufferAttributeDescriptor>();
    abs.add(new ArrayBufferAttributeDescriptor(
      "position",
      JCGLScalarType.TYPE_SHORT,
      1));
    final ArrayBufferTypeDescriptor d = new ArrayBufferTypeDescriptor(abs);

    IndexBuffer i = null;
    ArrayBuffer a = null;

    try {
      a = ga.arrayBufferAllocate(256, d, UsageHint.USAGE_STATIC_DRAW);
      i = gi.indexBufferAllocate(a, 4);
      Assert.assertEquals(JCGLUnsignedType.TYPE_UNSIGNED_SHORT, i.getType());
      Assert.assertEquals(2, i.getElementSizeBytes());
      Assert.assertEquals(4, i.getRange().getInterval());
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

  @Test(expected = ConstraintError.class) public final
    void
    testIndexBufferAllocateTypeNull()
      throws ConstraintError,
        JCGLRuntimeException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLIndexBuffers gi = this.getGLIndexBuffers(tc);

    gi.indexBufferAllocateType(null, 1);
  }

  /**
   * Allocating zero elements fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testIndexBufferAllocateTypeZeroElements()
      throws ConstraintError,
        JCGLRuntimeException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLIndexBuffers gi = this.getGLIndexBuffers(tc);

    gi.indexBufferAllocateType(JCGLUnsignedType.TYPE_UNSIGNED_BYTE, 0);
  }

  /**
   * Allocating zero elements fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testIndexBufferAllocateZeroElements()
      throws ConstraintError,
        JCGLRuntimeException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLIndexBuffers gi = this.getGLIndexBuffers(tc);

    final ArrayList<ArrayBufferAttributeDescriptor> abs =
      new ArrayList<ArrayBufferAttributeDescriptor>();
    abs.add(new ArrayBufferAttributeDescriptor(
      "position",
      JCGLScalarType.TYPE_SHORT,
      1));
    final ArrayBufferTypeDescriptor d = new ArrayBufferTypeDescriptor(abs);

    final ArrayBuffer a =
      ga.arrayBufferAllocate(1, d, UsageHint.USAGE_STATIC_DRAW);
    gi.indexBufferAllocate(a, 0);
  }

  /**
   * Deleting a buffer twice fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testIndexBufferDeleteDouble()
      throws ConstraintError,
        JCGLRuntimeException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLIndexBuffers gi = this.getGLIndexBuffers(tc);

    final ArrayList<ArrayBufferAttributeDescriptor> abs =
      new ArrayList<ArrayBufferAttributeDescriptor>();
    abs.add(new ArrayBufferAttributeDescriptor(
      "position",
      JCGLScalarType.TYPE_SHORT,
      1));
    final ArrayBufferTypeDescriptor d = new ArrayBufferTypeDescriptor(abs);

    IndexBuffer i = null;
    ArrayBuffer a = null;

    try {
      a = ga.arrayBufferAllocate(4, d, UsageHint.USAGE_STATIC_DRAW);
      i = gi.indexBufferAllocate(a, 10);
      gi.indexBufferDelete(i);
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    gi.indexBufferDelete(i);
  }

  /**
   * Deleting a null buffer twice fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testIndexBufferDeleteNull()
      throws ConstraintError,
        JCGLRuntimeException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLIndexBuffers gi = this.getGLIndexBuffers(tc);

    gi.indexBufferDelete(null);
  }

  /**
   * Updating an index buffer works.
   */

  @Test public final void testIndexBufferUpdate()
    throws ConstraintError,
      JCGLRuntimeException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLIndexBuffers gi = this.getGLIndexBuffers(tc);

    final ArrayList<ArrayBufferAttributeDescriptor> abs =
      new ArrayList<ArrayBufferAttributeDescriptor>();
    abs.add(new ArrayBufferAttributeDescriptor(
      "position",
      JCGLScalarType.TYPE_SHORT,
      1));
    final ArrayBufferTypeDescriptor d = new ArrayBufferTypeDescriptor(abs);

    IndexBuffer i = null;
    ArrayBuffer a = null;

    try {
      a = ga.arrayBufferAllocate(255, d, UsageHint.USAGE_STATIC_DRAW);
      i = gi.indexBufferAllocate(a, 4);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    final IndexBufferWritableData data = new IndexBufferWritableData(i);
    gi.indexBufferUpdate(data);
  }

  /**
   * Updating a deleted index buffer fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testIndexBufferUpdateDeleted()
      throws ConstraintError,
        JCGLRuntimeException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLIndexBuffers gi = this.getGLIndexBuffers(tc);

    final ArrayList<ArrayBufferAttributeDescriptor> abs =
      new ArrayList<ArrayBufferAttributeDescriptor>();
    abs.add(new ArrayBufferAttributeDescriptor(
      "position",
      JCGLScalarType.TYPE_SHORT,
      1));
    final ArrayBufferTypeDescriptor d = new ArrayBufferTypeDescriptor(abs);

    IndexBuffer i = null;
    ArrayBuffer a = null;

    try {
      a = ga.arrayBufferAllocate(255, d, UsageHint.USAGE_STATIC_DRAW);
      i = gi.indexBufferAllocate(a, 4);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    final IndexBufferWritableData data = new IndexBufferWritableData(i);
    gi.indexBufferDelete(i);
    gi.indexBufferUpdate(data);
  }

  /**
   * Updating an index buffer with null data fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testIndexBufferUpdateNullData()
      throws ConstraintError,
        JCGLRuntimeException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLIndexBuffers gi = this.getGLIndexBuffers(tc);

    gi.indexBufferUpdate(null);
  }
}
