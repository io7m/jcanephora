package com.io7m.jcanephora.contracts;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.ArrayBuffer;
import com.io7m.jcanephora.ArrayBufferAttribute;
import com.io7m.jcanephora.ArrayBufferDescriptor;
import com.io7m.jcanephora.ArrayBufferWritableMap;
import com.io7m.jcanephora.GLCompileException;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jcanephora.GLScalarType;
import com.io7m.jcanephora.Program;
import com.io7m.jcanephora.ProgramAttribute;
import com.io7m.jvvfs.FilesystemAPI;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathVirtual;

public abstract class ArrayBufferContract implements
  GLTestContract,
  FilesystemTestContract
{
  /**
   * An allocated buffer has the correct number of elements and element size.
   */

  @Test public final void testArrayBufferAllocate()
    throws ConstraintError,
      GLException
  {
    final GLInterface gl = this.makeNewGL();
    ArrayBuffer a = null;

    try {
      final ArrayBufferDescriptor d =
        new ArrayBufferDescriptor(
          new ArrayBufferAttribute[] { new ArrayBufferAttribute(
            "position",
            GLScalarType.TYPE_FLOAT,
            3) });

      a = gl.arrayBufferAllocate(10, d);
      Assert.assertEquals(12, a.getElementSizeBytes());
      Assert.assertEquals(10, a.getRange().getInterval());
      Assert.assertEquals(120, a.getSizeBytes());
      Assert.assertEquals(d, a.getDescriptor());
    } finally {
      if (a != null) {
        a.resourceDelete(gl);
      }
    }
  }

  /**
   * Allocating zero elements fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferAllocateZeroElements()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.makeNewGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_FLOAT,
          3) });

    gl.arrayBufferAllocate(0, d);
  }

  /**
   * Binding a deleted buffer fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferBindDeleted()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.makeNewGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a = gl.arrayBufferAllocate(10, d);

    gl.arrayBufferBind(a);
    Assert.assertTrue(gl.arrayBufferIsBound(a));
    gl.arrayBufferUnbind();
    Assert.assertFalse(gl.arrayBufferIsBound(a));
    a.resourceDelete(gl);
    gl.arrayBufferBind(a);
  }

  /**
   * Buffer binding/unbinding works.
   */

  @Test public final void testArrayBufferBinding()
    throws ConstraintError,
      GLException
  {
    final GLInterface gl = this.makeNewGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a = gl.arrayBufferAllocate(10, d);

    gl.arrayBufferBind(a);
    Assert.assertTrue(gl.arrayBufferIsBound(a));
    gl.arrayBufferUnbind();
    Assert.assertFalse(gl.arrayBufferIsBound(a));
  }

  /**
   * Attempting to bind a vertex attribute with a deleted array fails.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferBindVertexAttributeDeletedArray()
      throws GLException,
        ConstraintError
  {
    final GLInterface gl = this.makeNewGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a = gl.arrayBufferAllocate(10, d);
    gl.arrayBufferDelete(a);
    gl.arrayBufferBindVertexAttribute(a, null, null);
  }

  /**
   * Attempting to bind a vertex attribute with a null array fails.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferBindVertexAttributeNullArray()
      throws GLException,
        ConstraintError
  {
    final GLInterface gl = this.makeNewGL();
    gl.arrayBufferBindVertexAttribute(null, null, null);
  }

  /**
   * Attempting to bind a vertex attribute with a null attribute fails.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferBindVertexAttributeNullAttribute()
      throws GLException,
        ConstraintError
  {
    final GLInterface gl = this.makeNewGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a = gl.arrayBufferAllocate(10, d);
    gl.arrayBufferBindVertexAttribute(a, null, null);
  }

  /**
   * Attempting to bind a vertex attribute with a null program attribute
   * fails.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferBindVertexAttributeNullProgramAttribute()
      throws GLException,
        ConstraintError
  {
    final GLInterface gl = this.makeNewGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a = gl.arrayBufferAllocate(10, d);
    gl.arrayBufferBindVertexAttribute(a, d.getAttribute("position"), null);
  }

  /**
   * Binding a vertex attribute works.
   * 
   * @throws GLException
   * @throws ConstraintError
   * @throws FilesystemError
   * @throws IOException
   * @throws GLCompileException
   */

  @Test public final void testArrayBufferBindVertexAttributeOK()
    throws GLException,
      ConstraintError,
      GLCompileException,
      IOException,
      FilesystemError
  {
    final GLInterface gl = this.makeNewGL();
    final FilesystemAPI fs = this.makeNewFS();
    fs.mount("jcanephora.zip", new PathVirtual("/"));

    final Program pr = new Program("program", this.getLog());
    pr.addVertexShader(new PathVirtual("/shaders/position.v"));
    pr.compile(fs, gl);

    final ProgramAttribute pa = pr.getAttribute("position");
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_FLOAT,
          3) });
    final ArrayBuffer a = gl.arrayBufferAllocate(10, d);

    gl.arrayBufferBind(a);
    gl.arrayBufferBindVertexAttribute(a, d.getAttribute("position"), pa);
  }

  /**
   * Binding a vertex attribute that doesn't belong to the given array buffer
   * fails.
   * 
   * @throws GLException
   * @throws ConstraintError
   * @throws FilesystemError
   * @throws IOException
   * @throws GLCompileException
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferBindVertexAttributeWrongArray()
      throws GLException,
        ConstraintError,
        GLCompileException,
        IOException,
        FilesystemError
  {
    final GLInterface gl = this.makeNewGL();
    final FilesystemAPI fs = this.makeNewFS();
    fs.mount("jcanephora.zip", new PathVirtual("/"));

    final Program pr = new Program("program", this.getLog());
    pr.addVertexShader(new PathVirtual("/shaders/position.v"));
    pr.compile(fs, gl);

    final ProgramAttribute pa = pr.getAttribute("position");
    final ArrayBufferDescriptor d0 =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_FLOAT,
          3) });
    final ArrayBufferDescriptor d1 =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_FLOAT,
          3) });

    final ArrayBuffer a = gl.arrayBufferAllocate(10, d1);

    gl.arrayBufferBind(a);
    gl.arrayBufferBindVertexAttribute(a, d0.getAttribute("position"), pa);
  }

  /**
   * Binding a vertex attribute that doesn't have the same type as the program
   * attribute fails.
   * 
   * @throws GLException
   * @throws ConstraintError
   * @throws FilesystemError
   * @throws IOException
   * @throws GLCompileException
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferBindVertexAttributeWrongType()
      throws GLException,
        ConstraintError,
        GLCompileException,
        IOException,
        FilesystemError
  {
    final GLInterface gl = this.makeNewGL();
    final FilesystemAPI fs = this.makeNewFS();
    fs.mount("jcanephora.zip", new PathVirtual("/"));

    final Program pr = new Program("program", this.getLog());
    pr.addVertexShader(new PathVirtual("/shaders/position.v"));
    pr.compile(fs, gl);

    final ProgramAttribute pa = pr.getAttribute("position");
    final ArrayBufferDescriptor d0 =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_INT,
          3) });

    final ArrayBuffer a = gl.arrayBufferAllocate(10, d0);

    gl.arrayBufferBind(a);
    gl.arrayBufferBindVertexAttribute(a, d0.getAttribute("position"), pa);
  }

  /**
   * Checking if a deleted buffer is bound fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferDeletedIsBound()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.makeNewGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a = gl.arrayBufferAllocate(10, d);
    a.resourceDelete(gl);
    gl.arrayBufferIsBound(a);
  }

  /**
   * Deleting a buffer twice fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferDeleteDouble()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.makeNewGL();
    ArrayBuffer a = null;

    try {
      final ArrayBufferDescriptor d =
        new ArrayBufferDescriptor(
          new ArrayBufferAttribute[] { new ArrayBufferAttribute(
            "position",
            GLScalarType.TYPE_FLOAT,
            3) });

      a = gl.arrayBufferAllocate(10, d);
      gl.arrayBufferDelete(a);
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    gl.arrayBufferDelete(a);
  }

  /**
   * Deleting a null buffer twice fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferDeleteNull()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.makeNewGL();
    gl.arrayBufferDelete(null);
  }

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferElementOffset()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.makeNewGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(new ArrayBufferAttribute[] {
        new ArrayBufferAttribute("position", GLScalarType.TYPE_SHORT, 3),
        new ArrayBufferAttribute("normal", GLScalarType.TYPE_SHORT, 3),
        new ArrayBufferAttribute("color", GLScalarType.TYPE_SHORT, 3), });
    ArrayBuffer a = null;

    try {
      a = gl.arrayBufferAllocate(3, d);
      gl.arrayBufferMapWrite(a);
      Assert.assertEquals(0, a.getElementOffset(0));
      Assert.assertEquals(18, a.getElementOffset(1));
      Assert.assertEquals(36, a.getElementOffset(2));
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    assert a != null;
    a.getElementOffset(3);
  }

  /**
   * Mapping a buffer works.
   */

  @Test public final void testArrayBufferMap()
    throws ConstraintError,
      GLException
  {
    final GLInterface gl = this.makeNewGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a = gl.arrayBufferAllocate(10, d);

    try {
      try {
        final ArrayBufferWritableMap b = gl.arrayBufferMapWrite(a);
        final ShortBuffer s = b.getByteBuffer().asShortBuffer();
        for (int index = 0; index < 10; ++index) {
          s.put(index, (short) index);
        }
      } finally {
        gl.arrayBufferUnmap(a);
      }

      try {
        final ByteBuffer b = gl.arrayBufferMapRead(a);
        final ShortBuffer s = b.asShortBuffer();
        for (int index = 0; index < 10; ++index) {
          Assert.assertEquals(index, s.get(index));
        }
      } finally {
        gl.arrayBufferUnmap(a);
      }
    } finally {
      if (a != null) {
        a.resourceDelete(gl);
      }
    }
  }

  /**
   * A mapped buffer has the correct bounds.
   */

  @Test(expected = IndexOutOfBoundsException.class) public final
    void
    testArrayBufferMapReadBounds()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.makeNewGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a = gl.arrayBufferAllocate(10, d);

    try {
      try {
        final ByteBuffer b = gl.arrayBufferMapRead(a);
        b.get(20);
      } finally {
        gl.arrayBufferUnmap(a);
      }
    } finally {
      if (a != null) {
        a.resourceDelete(gl);
      }
    }
  }

  /**
   * Mapping a deleted buffer read-only fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferMapReadDeleted()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.makeNewGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a = gl.arrayBufferAllocate(10, d);

    a.resourceDelete(gl);
    gl.arrayBufferMapRead(a);
  }

  /**
   * Mapping a buffer twice fails.
   */

  @Test(expected = GLException.class) public final
    void
    testArrayBufferMapReadDouble()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.makeNewGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    ArrayBuffer a = null;

    try {
      a = gl.arrayBufferAllocate(10, d);
    } catch (final GLException e) {
      Assert.fail(e.getMessage());
    }

    try {
      gl.arrayBufferMapRead(a);
      gl.arrayBufferMapRead(a);
    } catch (final GLException e) {
      Assert.assertTrue(gl.errorCodeIsInvalidOperation(e.getCode()));
      throw e;
    } finally {
      if (a != null) {
        a.resourceDelete(gl);
      }
    }
  }

  /**
   * Mapping a null buffer fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferMapReadNull()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.makeNewGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    gl.arrayBufferAllocate(10, d);
    gl.arrayBufferMapRead(null);
  }

  /**
   * A mapped buffer has the correct bounds.
   */

  @Test(expected = IndexOutOfBoundsException.class) public final
    void
    testArrayBufferMapWriteBounds()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.makeNewGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a = gl.arrayBufferAllocate(10, d);

    try {
      try {
        final ArrayBufferWritableMap map = gl.arrayBufferMapWrite(a);
        final ByteBuffer b = map.getByteBuffer();
        b.put(20, (byte) 0xff);
      } finally {
        gl.arrayBufferUnmap(a);
      }
    } finally {
      if (a != null) {
        a.resourceDelete(gl);
      }
    }
  }

  /**
   * Mapping a deleted buffer write-only fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferMapWriteDeleted()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.makeNewGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a = gl.arrayBufferAllocate(10, d);

    a.resourceDelete(gl);
    gl.arrayBufferMapWrite(a);
  }

  /**
   * Mapping a null buffer fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferMapWriteNull()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.makeNewGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    gl.arrayBufferAllocate(10, d);
    gl.arrayBufferMapWrite(null);
  }

  /**
   * Checking if a null buffer is bound fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferNullIsBound()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.makeNewGL();
    gl.arrayBufferIsBound(null);
  }

  /**
   * Unbinding a vertex attribute for a deleted array fails.
   * 
   * @throws GLException
   * @throws ConstraintError
   * @throws GLCompileException
   * @throws IOException
   * @throws FilesystemError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferUnbindVertexAttributeDeleted()
      throws GLException,
        ConstraintError,
        GLCompileException,
        IOException,
        FilesystemError
  {
    final GLInterface gl = this.makeNewGL();
    final FilesystemAPI fs = this.makeNewFS();
    fs.mount("jcanephora.zip", new PathVirtual("/"));

    final Program pr = new Program("program", this.getLog());
    pr.addVertexShader(new PathVirtual("/shaders/position.v"));
    pr.compile(fs, gl);

    final ProgramAttribute pa = pr.getAttribute("position");
    final ArrayBufferDescriptor d0 =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_FLOAT,
          3) });

    final ArrayBuffer a = gl.arrayBufferAllocate(10, d0);

    gl.arrayBufferBind(a);
    gl.arrayBufferBindVertexAttribute(a, d0.getAttribute("position"), pa);
    a.resourceDelete(gl);
    gl.arrayBufferUnbindVertexAttribute(a, d0.getAttribute("position"), pa);
  }

  /**
   * Unbinding a vertex attribute with a null attribute fails.
   * 
   * @throws GLException
   * @throws ConstraintError
   * @throws FilesystemError
   * @throws GLCompileException
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferUnbindVertexAttributeNull()
      throws GLException,
        ConstraintError,
        FilesystemError,
        GLCompileException
  {
    final GLInterface gl = this.makeNewGL();
    final FilesystemAPI fs = this.makeNewFS();
    fs.mount("jcanephora.zip", new PathVirtual("/"));

    final Program pr = new Program("program", this.getLog());
    pr.addVertexShader(new PathVirtual("/shaders/position.v"));
    pr.compile(fs, gl);

    final ProgramAttribute pa = pr.getAttribute("position");
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_FLOAT,
          3) });
    final ArrayBuffer a = gl.arrayBufferAllocate(10, d);

    gl.arrayBufferBind(a);
    gl.arrayBufferBindVertexAttribute(a, d.getAttribute("position"), pa);
    gl.arrayBufferUnbindVertexAttribute(a, null, pa);
  }

  /**
   * Unbinding a vertex attribute with a null array fails.
   * 
   * @throws GLException
   * @throws ConstraintError
   * @throws FilesystemError
   * @throws GLCompileException
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferUnbindVertexAttributeNullArray()
      throws GLException,
        ConstraintError,
        FilesystemError,
        GLCompileException
  {
    final GLInterface gl = this.makeNewGL();
    final FilesystemAPI fs = this.makeNewFS();
    fs.mount("jcanephora.zip", new PathVirtual("/"));

    final Program pr = new Program("program", this.getLog());
    pr.addVertexShader(new PathVirtual("/shaders/position.v"));
    pr.compile(fs, gl);

    final ProgramAttribute pa = pr.getAttribute("position");
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_FLOAT,
          3) });
    final ArrayBuffer a = gl.arrayBufferAllocate(10, d);

    gl.arrayBufferBind(a);
    gl.arrayBufferBindVertexAttribute(a, d.getAttribute("position"), pa);
    gl.arrayBufferUnbindVertexAttribute(null, d.getAttribute("position"), pa);
  }

  /**
   * Unbinding a vertex attribute with a null program attribute fails.
   * 
   * @throws GLException
   * @throws ConstraintError
   * @throws FilesystemError
   * @throws GLCompileException
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferUnbindVertexAttributeNullProgram()
      throws GLException,
        ConstraintError,
        FilesystemError,
        GLCompileException
  {
    final GLInterface gl = this.makeNewGL();
    final FilesystemAPI fs = this.makeNewFS();
    fs.mount("jcanephora.zip", new PathVirtual("/"));

    final Program pr = new Program("program", this.getLog());
    pr.addVertexShader(new PathVirtual("/shaders/position.v"));
    pr.compile(fs, gl);

    final ProgramAttribute pa = pr.getAttribute("position");
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_FLOAT,
          3) });
    final ArrayBuffer a = gl.arrayBufferAllocate(10, d);

    gl.arrayBufferBind(a);
    gl.arrayBufferBindVertexAttribute(a, d.getAttribute("position"), pa);
    gl.arrayBufferUnbindVertexAttribute(a, d.getAttribute("position"), null);
  }

  /**
   * Unbinding a bound vertex attribute works.
   * 
   * @throws ConstraintError
   * @throws GLException
   * @throws FilesystemError
   * @throws GLCompileException
   */

  @Test public final void testArrayBufferUnbindVertexAttributeOK()
    throws GLException,
      ConstraintError,
      FilesystemError,
      GLCompileException
  {
    final GLInterface gl = this.makeNewGL();
    final FilesystemAPI fs = this.makeNewFS();
    fs.mount("jcanephora.zip", new PathVirtual("/"));

    final Program pr = new Program("program", this.getLog());
    pr.addVertexShader(new PathVirtual("/shaders/position.v"));
    pr.compile(fs, gl);

    final ProgramAttribute pa = pr.getAttribute("position");
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_FLOAT,
          3) });
    final ArrayBuffer a = gl.arrayBufferAllocate(10, d);

    gl.arrayBufferBind(a);
    gl.arrayBufferBindVertexAttribute(a, d.getAttribute("position"), pa);
    gl.arrayBufferUnbindVertexAttribute(a, d.getAttribute("position"), pa);
  }

  /**
   * Unbinding a vertex attribute with an unbound array fails.
   * 
   * @throws GLException
   * @throws ConstraintError
   * @throws FilesystemError
   * @throws GLCompileException
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferUnbindVertexAttributeUnbound()
      throws GLException,
        ConstraintError,
        FilesystemError,
        GLCompileException
  {
    final GLInterface gl = this.makeNewGL();
    final FilesystemAPI fs = this.makeNewFS();
    fs.mount("jcanephora.zip", new PathVirtual("/"));

    final Program pr = new Program("program", this.getLog());
    pr.addVertexShader(new PathVirtual("/shaders/position.v"));
    pr.compile(fs, gl);

    final ProgramAttribute pa = pr.getAttribute("position");
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_FLOAT,
          3) });
    final ArrayBuffer a = gl.arrayBufferAllocate(10, d);

    gl.arrayBufferBind(a);
    gl.arrayBufferBindVertexAttribute(a, d.getAttribute("position"), pa);
    gl.arrayBufferUnbind();
    gl.arrayBufferUnbindVertexAttribute(a, d.getAttribute("position"), pa);
  }

  /**
   * Unbinding a vertex attribute that does not belong to the given array
   * fails.
   * 
   * @throws GLException
   * @throws ConstraintError
   * @throws GLCompileException
   * @throws IOException
   * @throws FilesystemError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferUnbindVertexAttributeWrongArray()
      throws GLException,
        ConstraintError,
        GLCompileException,
        IOException,
        FilesystemError
  {
    final GLInterface gl = this.makeNewGL();
    final FilesystemAPI fs = this.makeNewFS();
    fs.mount("jcanephora.zip", new PathVirtual("/"));

    final Program pr = new Program("program", this.getLog());
    pr.addVertexShader(new PathVirtual("/shaders/position.v"));
    pr.compile(fs, gl);

    final ProgramAttribute pa = pr.getAttribute("position");
    final ArrayBufferDescriptor d0 =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_FLOAT,
          3) });
    final ArrayBufferDescriptor d1 =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_FLOAT,
          3) });

    final ArrayBuffer a = gl.arrayBufferAllocate(10, d0);

    gl.arrayBufferBind(a);
    gl.arrayBufferBindVertexAttribute(a, d0.getAttribute("position"), pa);
    gl.arrayBufferUnbindVertexAttribute(a, d1.getAttribute("position"), pa);
  }

  /**
   * Unmapping a deleted buffer fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferUnmapDeleted()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.makeNewGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a = gl.arrayBufferAllocate(10, d);

    gl.arrayBufferMapWrite(a);
    a.resourceDelete(gl);
    gl.arrayBufferUnmap(a);
  }

  /**
   * Unmapping a buffer twice fails.
   */

  @Test(expected = GLException.class) public final
    void
    testArrayBufferUnmapDouble()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.makeNewGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(
        new ArrayBufferAttribute[] { new ArrayBufferAttribute(
          "position",
          GLScalarType.TYPE_SHORT,
          1) });
    ArrayBuffer a = null;

    try {
      a = gl.arrayBufferAllocate(10, d);
      gl.arrayBufferMapWrite(a);
    } catch (final GLException e) {
      Assert.fail(e.getMessage());
    }

    try {
      gl.arrayBufferUnmap(a);
      gl.arrayBufferUnmap(a);
    } catch (final GLException e) {
      Assert.assertTrue(gl.errorCodeIsInvalidOperation(e.getCode()));
      throw e;
    } finally {
      if (a != null) {
        a.resourceDelete(gl);
      }
    }
  }
}
