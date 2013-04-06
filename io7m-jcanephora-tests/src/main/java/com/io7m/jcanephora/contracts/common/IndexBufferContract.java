package com.io7m.jcanephora.contracts.common;

import junit.framework.Assert;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.ArrayBuffer;
import com.io7m.jcanephora.ArrayBufferAttribute;
import com.io7m.jcanephora.ArrayBufferDescriptor;
import com.io7m.jcanephora.GLArrayBuffers;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLIndexBuffers;
import com.io7m.jcanephora.GLScalarType;
import com.io7m.jcanephora.GLUnsignedType;
import com.io7m.jcanephora.GLUnsupportedException;
import com.io7m.jcanephora.IndexBuffer;
import com.io7m.jcanephora.IndexBufferWritableData;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.UsageHint;

public abstract class IndexBufferContract implements TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract GLArrayBuffers getGLArrayBuffers(
    TestContext tc);

  public abstract GLIndexBuffers getGLIndexBuffers(
    TestContext tc);

  /**
   * Allocating an index buffer from a deleted array buffer fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testIndexBufferAllocateArrayDeleted()
      throws ConstraintError,
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final GLIndexBuffers gi = this.getGLIndexBuffers(tc);

    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
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
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final GLIndexBuffers gi = this.getGLIndexBuffers(tc);

    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    IndexBuffer i = null;
    ArrayBuffer a = null;

    try {
      a = ga.arrayBufferAllocate(255, d, UsageHint.USAGE_STATIC_DRAW);
      i = gi.indexBufferAllocate(a, 4);
      Assert.assertEquals(GLUnsignedType.TYPE_UNSIGNED_BYTE, i.getType());
      Assert.assertEquals(1, i.getElementSizeBytes());
      Assert.assertEquals(4, i.getRange().getInterval());
      Assert.assertEquals(4, i.getSizeBytes());
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
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final GLIndexBuffers gi = this.getGLIndexBuffers(tc);

    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    IndexBuffer i = null;
    ArrayBuffer a = null;

    try {
      a = ga.arrayBufferAllocate(65536, d, UsageHint.USAGE_STATIC_DRAW);
      i = gi.indexBufferAllocate(a, 2);
      Assert.assertEquals(GLUnsignedType.TYPE_UNSIGNED_INT, i.getType());
      Assert.assertEquals(4, i.getElementSizeBytes());
      Assert.assertEquals(2, i.getRange().getInterval());
      Assert.assertEquals(8, i.getSizeBytes());
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
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLIndexBuffers gi = this.getGLIndexBuffers(tc);

    gi.indexBufferAllocate(null, 1);
  }

  /**
   * An allocated index buffer has the correct number of indices and index
   * type.
   */

  @Test public final void testIndexBufferAllocateShort()
    throws ConstraintError,
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final GLIndexBuffers gi = this.getGLIndexBuffers(tc);

    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    IndexBuffer i = null;
    ArrayBuffer a = null;

    try {
      a = ga.arrayBufferAllocate(256, d, UsageHint.USAGE_STATIC_DRAW);
      i = gi.indexBufferAllocate(a, 4);
      Assert.assertEquals(GLUnsignedType.TYPE_UNSIGNED_SHORT, i.getType());
      Assert.assertEquals(2, i.getElementSizeBytes());
      Assert.assertEquals(4, i.getRange().getInterval());
      Assert.assertEquals(8, i.getSizeBytes());
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
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLIndexBuffers gi = this.getGLIndexBuffers(tc);

    gi.indexBufferAllocateType(null, 1);
  }

  /**
   * Allocating zero elements fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testIndexBufferAllocateTypeZeroElements()
      throws ConstraintError,
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLIndexBuffers gi = this.getGLIndexBuffers(tc);

    gi.indexBufferAllocateType(GLUnsignedType.TYPE_UNSIGNED_BYTE, 0);
  }

  /**
   * Allocating zero elements fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testIndexBufferAllocateZeroElements()
      throws ConstraintError,
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final GLIndexBuffers gi = this.getGLIndexBuffers(tc);

    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
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
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final GLIndexBuffers gi = this.getGLIndexBuffers(tc);

    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
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
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLIndexBuffers gi = this.getGLIndexBuffers(tc);

    gi.indexBufferDelete(null);
  }

  /**
   * Updating an index buffer works.
   */

  @Test public final void testIndexBufferUpdate()
    throws ConstraintError,
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final GLIndexBuffers gi = this.getGLIndexBuffers(tc);

    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    IndexBuffer i = null;
    ArrayBuffer a = null;

    try {
      a = ga.arrayBufferAllocate(255, d, UsageHint.USAGE_STATIC_DRAW);
      i = gi.indexBufferAllocate(a, 4);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    final IndexBufferWritableData data = new IndexBufferWritableData(i);
    gi.indexBufferUpdate(i, data);
  }

  /**
   * Updating a deleted index buffer fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testIndexBufferUpdateDeleted()
      throws ConstraintError,
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final GLIndexBuffers gi = this.getGLIndexBuffers(tc);

    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
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
    gi.indexBufferUpdate(i, data);
  }

  /**
   * Updating a null index buffer fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testIndexBufferUpdateNull()
      throws ConstraintError,
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final GLIndexBuffers gi = this.getGLIndexBuffers(tc);

    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    IndexBuffer i = null;
    ArrayBuffer a = null;

    try {
      a = ga.arrayBufferAllocate(255, d, UsageHint.USAGE_STATIC_DRAW);
      i = gi.indexBufferAllocate(a, 4);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    final IndexBufferWritableData data = new IndexBufferWritableData(i);
    gi.indexBufferUpdate(null, data);
  }

  /**
   * Updating an index buffer with null data fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testIndexBufferUpdateNullData()
      throws ConstraintError,
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final GLIndexBuffers gi = this.getGLIndexBuffers(tc);

    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    IndexBuffer i = null;
    ArrayBuffer a = null;

    try {
      a = ga.arrayBufferAllocate(255, d, UsageHint.USAGE_STATIC_DRAW);
      i = gi.indexBufferAllocate(a, 4);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    gi.indexBufferUpdate(i, null);
  }
}
