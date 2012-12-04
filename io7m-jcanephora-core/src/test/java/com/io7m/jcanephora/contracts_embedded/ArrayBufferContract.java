package com.io7m.jcanephora.contracts_embedded;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;
import com.io7m.jcanephora.ArrayBuffer;
import com.io7m.jcanephora.ArrayBufferAttribute;
import com.io7m.jcanephora.ArrayBufferDescriptor;
import com.io7m.jcanephora.ArrayBufferWritableData;
import com.io7m.jcanephora.GLCompileException;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterfaceEmbedded;
import com.io7m.jcanephora.GLScalarType;
import com.io7m.jcanephora.Program;
import com.io7m.jcanephora.ProgramAttribute;
import com.io7m.jvvfs.FilesystemAPI;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathVirtual;

public abstract class ArrayBufferContract implements
  GLEmbeddedTestContract,
  FilesystemTestContract
{
  /**
   * An allocated buffer has the correct number of elements and element size.
   */

  @Test public final void testArrayBufferAllocate()
    throws ConstraintError,
      GLException
  {
    final GLInterfaceEmbedded gl = this.makeNewGL();
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
    final GLInterfaceEmbedded gl = this.makeNewGL();
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
    final GLInterfaceEmbedded gl = this.makeNewGL();
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
    final GLInterfaceEmbedded gl = this.makeNewGL();
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
    final GLInterfaceEmbedded gl = this.makeNewGL();
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
    final GLInterfaceEmbedded gl = this.makeNewGL();
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
    final GLInterfaceEmbedded gl = this.makeNewGL();
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
    final GLInterfaceEmbedded gl = this.makeNewGL();
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
    final GLInterfaceEmbedded gl = this.makeNewGL();
    final FilesystemAPI fs = this.makeNewFS();

    final Program pr = new Program("program", this.getLog());
    pr.addVertexShader(new PathVirtual(
      "/com/io7m/jcanephora/shaders/position.v"));
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
    final GLInterfaceEmbedded gl = this.makeNewGL();
    final FilesystemAPI fs = this.makeNewFS();

    final Program pr = new Program("program", this.getLog());
    pr.addVertexShader(new PathVirtual(
      "/com/io7m/jcanephora/shaders/position.v"));
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
    final GLInterfaceEmbedded gl = this.makeNewGL();
    final FilesystemAPI fs = this.makeNewFS();

    final Program pr = new Program("program", this.getLog());
    pr.addVertexShader(new PathVirtual(
      "/com/io7m/jcanephora/shaders/position.v"));
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
    final GLInterfaceEmbedded gl = this.makeNewGL();
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
    final GLInterfaceEmbedded gl = this.makeNewGL();
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
    final GLInterfaceEmbedded gl = this.makeNewGL();
    gl.arrayBufferDelete(null);
  }

  /**
   * Array buffer offsets are correct.
   * 
   * @throws ConstraintError
   * @throws GLException
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferElementOffset()
      throws ConstraintError,
        GLException
  {
    final GLInterfaceEmbedded gl = this.makeNewGL();
    final ArrayBufferDescriptor d =
      new ArrayBufferDescriptor(new ArrayBufferAttribute[] {
        new ArrayBufferAttribute("position", GLScalarType.TYPE_SHORT, 3),
        new ArrayBufferAttribute("normal", GLScalarType.TYPE_SHORT, 3),
        new ArrayBufferAttribute("color", GLScalarType.TYPE_SHORT, 3), });
    ArrayBuffer a = null;

    try {
      a = gl.arrayBufferAllocate(3, d);
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
   * Checking if a null buffer is bound fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferNullIsBound()
      throws ConstraintError,
        GLException
  {
    final GLInterfaceEmbedded gl = this.makeNewGL();
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
    final GLInterfaceEmbedded gl = this.makeNewGL();
    final FilesystemAPI fs = this.makeNewFS();

    final Program pr = new Program("program", this.getLog());
    pr.addVertexShader(new PathVirtual(
      "/com/io7m/jcanephora/shaders/position.v"));
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
    final GLInterfaceEmbedded gl = this.makeNewGL();
    final FilesystemAPI fs = this.makeNewFS();

    final Program pr = new Program("program", this.getLog());
    pr.addVertexShader(new PathVirtual(
      "/com/io7m/jcanephora/shaders/position.v"));
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
    final GLInterfaceEmbedded gl = this.makeNewGL();
    final FilesystemAPI fs = this.makeNewFS();

    final Program pr = new Program("program", this.getLog());
    pr.addVertexShader(new PathVirtual(
      "/com/io7m/jcanephora/shaders/position.v"));
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
    final GLInterfaceEmbedded gl = this.makeNewGL();
    final FilesystemAPI fs = this.makeNewFS();

    final Program pr = new Program("program", this.getLog());
    pr.addVertexShader(new PathVirtual(
      "/com/io7m/jcanephora/shaders/position.v"));
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
    final GLInterfaceEmbedded gl = this.makeNewGL();
    final FilesystemAPI fs = this.makeNewFS();

    final Program pr = new Program("program", this.getLog());
    pr.addVertexShader(new PathVirtual(
      "/com/io7m/jcanephora/shaders/position.v"));
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
    final GLInterfaceEmbedded gl = this.makeNewGL();
    final FilesystemAPI fs = this.makeNewFS();

    final Program pr = new Program("program", this.getLog());
    pr.addVertexShader(new PathVirtual(
      "/com/io7m/jcanephora/shaders/position.v"));
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
    final GLInterfaceEmbedded gl = this.makeNewGL();
    final FilesystemAPI fs = this.makeNewFS();

    final Program pr = new Program("program", this.getLog());
    pr.addVertexShader(new PathVirtual(
      "/com/io7m/jcanephora/shaders/position.v"));
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
   * Array buffer complete updates work.
   */

  @Test public final void testArrayBufferUpdateComplete()
    throws ConstraintError,
      GLException
  {
    final GLInterfaceEmbedded gl = this.makeNewGL();
    ArrayBuffer a = null;

    try {
      final ArrayBufferDescriptor d =
        new ArrayBufferDescriptor(
          new ArrayBufferAttribute[] { new ArrayBufferAttribute(
            "position",
            GLScalarType.TYPE_FLOAT,
            3) });

      a = gl.arrayBufferAllocate(10, d);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    final ArrayBufferWritableData data = new ArrayBufferWritableData(a);
    gl.arrayBufferBind(a);
    gl.arrayBufferUpdate(a, data);
  }

  /**
   * Array buffer updates with a deleted buffer bound fail.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferUpdateDeletedFails()
      throws ConstraintError,
        GLException
  {
    final GLInterfaceEmbedded gl = this.makeNewGL();
    ArrayBuffer a = null;

    try {
      final ArrayBufferDescriptor d =
        new ArrayBufferDescriptor(
          new ArrayBufferAttribute[] { new ArrayBufferAttribute(
            "position",
            GLScalarType.TYPE_FLOAT,
            3) });

      a = gl.arrayBufferAllocate(10, d);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    final ArrayBufferWritableData data = new ArrayBufferWritableData(a);
    gl.arrayBufferBind(a);
    gl.arrayBufferDelete(a);
    gl.arrayBufferUpdate(a, data);
  }

  /**
   * Array buffer partial updates work.
   */

  @Test public final void testArrayBufferUpdatePartial()
    throws ConstraintError,
      GLException
  {
    final GLInterfaceEmbedded gl = this.makeNewGL();
    ArrayBuffer a = null;

    try {
      final ArrayBufferDescriptor d =
        new ArrayBufferDescriptor(
          new ArrayBufferAttribute[] { new ArrayBufferAttribute(
            "position",
            GLScalarType.TYPE_FLOAT,
            3) });

      a = gl.arrayBufferAllocate(10, d);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    final ArrayBufferWritableData data =
      new ArrayBufferWritableData(a, new RangeInclusive(2, 8));
    gl.arrayBufferBind(a);
    gl.arrayBufferUpdate(a, data);
  }

  /**
   * Array buffer updates without a bound buffer fail.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferUpdateUnboundFails()
      throws ConstraintError,
        GLException
  {
    final GLInterfaceEmbedded gl = this.makeNewGL();
    ArrayBuffer a = null;

    try {
      final ArrayBufferDescriptor d =
        new ArrayBufferDescriptor(
          new ArrayBufferAttribute[] { new ArrayBufferAttribute(
            "position",
            GLScalarType.TYPE_FLOAT,
            3) });

      a = gl.arrayBufferAllocate(10, d);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    final ArrayBufferWritableData data = new ArrayBufferWritableData(a);
    gl.arrayBufferUnbind();
    gl.arrayBufferUpdate(a, data);
  }

  /**
   * Array buffer updates with the wrong buffer bound fail.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferUpdateWrongBindingFails()
      throws ConstraintError,
        GLException
  {
    final GLInterfaceEmbedded gl = this.makeNewGL();
    ArrayBuffer a = null;
    ArrayBuffer b = null;

    try {
      final ArrayBufferDescriptor d =
        new ArrayBufferDescriptor(
          new ArrayBufferAttribute[] { new ArrayBufferAttribute(
            "position",
            GLScalarType.TYPE_FLOAT,
            3) });

      a = gl.arrayBufferAllocate(10, d);
      b = gl.arrayBufferAllocate(10, d);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    final ArrayBufferWritableData data = new ArrayBufferWritableData(a);
    gl.arrayBufferBind(b);
    gl.arrayBufferUpdate(a, data);
  }
}
