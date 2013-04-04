package com.io7m.jcanephora.contracts.gl3;

import junit.framework.Assert;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.ArrayBuffer;
import com.io7m.jcanephora.ArrayBufferAttribute;
import com.io7m.jcanephora.ArrayBufferDescriptor;
import com.io7m.jcanephora.CursorReadableIndex;
import com.io7m.jcanephora.CursorWritableIndex;
import com.io7m.jcanephora.GLArrayBuffers;
import com.io7m.jcanephora.GLArrayBuffersMapped;
import com.io7m.jcanephora.GLErrorCodes;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLIndexBuffers;
import com.io7m.jcanephora.GLIndexBuffersMapped;
import com.io7m.jcanephora.GLScalarType;
import com.io7m.jcanephora.GLUnsignedType;
import com.io7m.jcanephora.GLUnsupportedException;
import com.io7m.jcanephora.IndexBuffer;
import com.io7m.jcanephora.IndexBufferReadableMap;
import com.io7m.jcanephora.IndexBufferWritableMap;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.UsageHint;
import com.io7m.jcanephora.contracts.common.TestContract;

public abstract class IndexBufferMapContract implements TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract GLArrayBuffers getGLArrayBuffers(
    final TestContext tc);

  public abstract GLArrayBuffersMapped getGLArrayBuffersMapped(
    final TestContext tc);

  public abstract GLIndexBuffers getGLIndexBuffers(
    final TestContext tc);

  public abstract GLIndexBuffersMapped getGLIndexBuffersMapped(
    final TestContext tc);

  public abstract GLErrorCodes getGLErrorCodes(
    TestContext tc);

  /**
   * Mapping a deleted buffer read-only fails.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testMapReadDeleted()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLIndexBuffers gi = this.getGLIndexBuffers(tc);
    final GLIndexBuffersMapped gm = this.getGLIndexBuffersMapped(tc);

    final IndexBuffer ib =
      gi.indexBufferAllocateType(GLUnsignedType.TYPE_UNSIGNED_BYTE, 25);

    gi.indexBufferDelete(ib);
    gm.indexBufferMapRead(ib);
  }

  /**
   * Mapping a deleted buffer write-only fails.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testMapWriteDeleted()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLIndexBuffers gi = this.getGLIndexBuffers(tc);
    final GLIndexBuffersMapped gm = this.getGLIndexBuffersMapped(tc);

    final IndexBuffer ib =
      gi.indexBufferAllocateType(GLUnsignedType.TYPE_UNSIGNED_BYTE, 25);

    gi.indexBufferDelete(ib);
    gm.indexBufferMapWrite(ib);
  }

  /**
   * An index buffer of less than 256 elements has type 'unsigned byte' and
   * reading/writing is correct.
   */

  @Test public final void testReadWriteByte()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final GLIndexBuffers gi = this.getGLIndexBuffers(tc);
    final GLIndexBuffersMapped gm = this.getGLIndexBuffersMapped(tc);

    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_BYTE,
          1) });
    final ArrayBuffer a =
      ga.arrayBufferAllocate(255, d, UsageHint.USAGE_STATIC_DRAW);
    final IndexBuffer ib = gi.indexBufferAllocate(a, 1);

    Assert.assertTrue(ib.getType() == GLUnsignedType.TYPE_UNSIGNED_BYTE);

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
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final GLIndexBuffers gi = this.getGLIndexBuffers(tc);
    final GLIndexBuffersMapped gm = this.getGLIndexBuffersMapped(tc);

    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_BYTE,
          1) });
    final ArrayBuffer a =
      ga.arrayBufferAllocate(70000, d, UsageHint.USAGE_STATIC_DRAW);
    final IndexBuffer ib = gi.indexBufferAllocate(a, 1);

    Assert.assertTrue(ib.getType() == GLUnsignedType.TYPE_UNSIGNED_INT);

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
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final GLIndexBuffers gi = this.getGLIndexBuffers(tc);
    final GLIndexBuffersMapped gm = this.getGLIndexBuffersMapped(tc);

    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_BYTE,
          1) });
    final ArrayBuffer a =
      ga.arrayBufferAllocate(65535, d, UsageHint.USAGE_STATIC_DRAW);
    final IndexBuffer ib = gi.indexBufferAllocate(a, 1);

    Assert.assertTrue(ib.getType() == GLUnsignedType.TYPE_UNSIGNED_SHORT);

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
   * @throws GLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testUnmapDeleted()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLIndexBuffers gi = this.getGLIndexBuffers(tc);
    final GLIndexBuffersMapped gm = this.getGLIndexBuffersMapped(tc);

    final IndexBuffer ib =
      gi.indexBufferAllocateType(GLUnsignedType.TYPE_UNSIGNED_BYTE, 25);

    gm.indexBufferMapRead(ib);
    gi.indexBufferDelete(ib);
    gm.indexBufferUnmap(ib);
  }
}
