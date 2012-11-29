package com.io7m.jcanephora.contracts_full;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Option;
import com.io7m.jaux.functional.Option.Type;
import com.io7m.jcanephora.ArrayBuffer;
import com.io7m.jcanephora.ArrayBufferAttribute;
import com.io7m.jcanephora.ArrayBufferDescriptor;
import com.io7m.jcanephora.CursorReadableIndex;
import com.io7m.jcanephora.CursorWritableIndex;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jcanephora.GLScalarType;
import com.io7m.jcanephora.GLUnsignedType;
import com.io7m.jcanephora.IndexBuffer;
import com.io7m.jcanephora.IndexBufferReadableMap;
import com.io7m.jcanephora.IndexBufferWritableMap;

public abstract class IndexBufferMapContract implements GLTestContract
{
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
        ConstraintError
  {
    final Option<GLInterface> og = this.makeNewGL();
    if (og.type == Type.OPTION_NONE) {
      throw new ConstraintError("Unsupported");
    }
    final GLInterface gl = ((Option.Some<GLInterface>) og).value;
    final IndexBuffer ib =
      gl.indexBufferAllocateType(GLUnsignedType.TYPE_UNSIGNED_BYTE, 25);

    ib.resourceDelete(gl);
    gl.indexBufferMapRead(ib);
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
        ConstraintError
  {
    final Option<GLInterface> og = this.makeNewGL();
    if (og.type == Type.OPTION_NONE) {
      throw new ConstraintError("Unsupported");
    }
    final GLInterface gl = ((Option.Some<GLInterface>) og).value;
    final IndexBuffer ib =
      gl.indexBufferAllocateType(GLUnsignedType.TYPE_UNSIGNED_BYTE, 25);

    ib.resourceDelete(gl);
    gl.indexBufferMapWrite(ib);
  }

  /**
   * An index buffer of less than 256 elements has type 'unsigned byte' and
   * reading/writing is correct.
   */

  @Test public final void testReadWriteByte()
    throws GLException,
      ConstraintError
  {
    final Option<GLInterface> og = this.makeNewGL();
    if (og.type == Type.OPTION_NONE) {
      return;
    }
    final GLInterface gl = ((Option.Some<GLInterface>) og).value;
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_BYTE,
          1) });
    final ArrayBuffer a = gl.arrayBufferAllocate(255, d);
    final IndexBuffer ib = gl.indexBufferAllocate(a, 1);

    Assert.assertTrue(ib.getType() == GLUnsignedType.TYPE_UNSIGNED_BYTE);

    {
      final IndexBufferWritableMap map = gl.indexBufferMapWrite(ib);
      Assert.assertEquals(ib, map.getIndexBuffer());
      final CursorWritableIndex cursor = map.getCursor();
      for (int index = 0; index < ib.getRange().getInterval(); ++index) {
        cursor.putIndex(index);
      }
    }

    gl.indexBufferUnmap(ib);

    {
      final IndexBufferReadableMap map = gl.indexBufferMapRead(ib);
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
      ConstraintError
  {
    final Option<GLInterface> og = this.makeNewGL();
    if (og.type == Type.OPTION_NONE) {
      return;
    }
    final GLInterface gl = ((Option.Some<GLInterface>) og).value;
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_BYTE,
          1) });
    final ArrayBuffer a = gl.arrayBufferAllocate(70000, d);
    final IndexBuffer ib = gl.indexBufferAllocate(a, 1);

    Assert.assertTrue(ib.getType() == GLUnsignedType.TYPE_UNSIGNED_INT);

    {
      final IndexBufferWritableMap map = gl.indexBufferMapWrite(ib);
      Assert.assertEquals(ib, map.getIndexBuffer());
      final CursorWritableIndex cursor = map.getCursor();
      for (int index = 0; index < ib.getRange().getInterval(); ++index) {
        cursor.putIndex(index);
      }
    }

    gl.indexBufferUnmap(ib);

    {
      final IndexBufferReadableMap map = gl.indexBufferMapRead(ib);
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
      ConstraintError
  {
    final Option<GLInterface> og = this.makeNewGL();
    if (og.type == Type.OPTION_NONE) {
      return;
    }
    final GLInterface gl = ((Option.Some<GLInterface>) og).value;
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_BYTE,
          1) });
    final ArrayBuffer a = gl.arrayBufferAllocate(65535, d);
    final IndexBuffer ib = gl.indexBufferAllocate(a, 1);

    Assert.assertTrue(ib.getType() == GLUnsignedType.TYPE_UNSIGNED_SHORT);

    {
      final IndexBufferWritableMap map = gl.indexBufferMapWrite(ib);
      Assert.assertEquals(ib, map.getIndexBuffer());
      final CursorWritableIndex cursor = map.getCursor();
      for (int index = 0; index < ib.getRange().getInterval(); ++index) {
        cursor.putIndex(index);
      }
    }

    gl.indexBufferUnmap(ib);

    {
      final IndexBufferReadableMap map = gl.indexBufferMapRead(ib);
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
        ConstraintError
  {
    final Option<GLInterface> og = this.makeNewGL();
    if (og.type == Type.OPTION_NONE) {
      throw new ConstraintError("Unsupported");
    }
    final GLInterface gl = ((Option.Some<GLInterface>) og).value;
    final IndexBuffer ib =
      gl.indexBufferAllocateType(GLUnsignedType.TYPE_UNSIGNED_BYTE, 25);

    gl.indexBufferMapRead(ib);
    ib.resourceDelete(gl);
    gl.indexBufferUnmap(ib);
  }
}
