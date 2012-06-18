package com.io7m.jcanephora;

import java.io.IOException;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lwjgl.opengl.Pbuffer;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.PropertyUtils;
import com.io7m.jlog.Log;
import com.io7m.jvvfs.Filesystem;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathReal;
import com.io7m.jvvfs.PathVirtual;

public class ProgramLWJGL30Test
{
  private static Filesystem getFS()
    throws FilesystemError,
      IOException,
      ConstraintError
  {
    return new Filesystem(ProgramLWJGL30Test.getLog(), new PathReal(
      "test-archives"));
  }

  private static GLInterface getGL()
    throws IOException,
      ConstraintError,
      GLException
  {
    return new GLInterfaceLWJGL30(ProgramLWJGL30Test.getLog());
  }

  private static Log getLog()
    throws IOException,
      ConstraintError
  {
    final Properties properties =
      PropertyUtils.loadFromFile("tests.properties");
    return new Log(properties, "com.io7m", "example");
  }

  private Pbuffer buffer;

  @Before public void setUp()
    throws Exception
  {
    this.buffer = LWJGL30.createOffscreenDisplay(640, 480);
  }

  @After public void tearDown()
    throws Exception
  {
    LWJGL30.destroyOffscreenDisplay(this.buffer);
  }

  /**
   * Adding a nonexistent shader does not fail.
   */

  @Test public void testProgramAddFragmentShaderNonexistent()
    throws IOException,
      ConstraintError
  {
    final Program p = new Program("program", ProgramLWJGL30Test.getLog());
    p.addFragmentShader(new PathVirtual("/nonexistent"));
  }

  /**
   * Adding a shader causes the program to require compilation.
   * 
   * @throws GLException
   */

  @Test public
    void
    testProgramAddFragmentShaderPreservesCompilationRequirement()
      throws IOException,
        ConstraintError,
        FilesystemError,
        GLException
  {
    final Program p = new Program("program", ProgramLWJGL30Test.getLog());
    {
      final boolean r =
        p.requiresCompilation(
          ProgramLWJGL30Test.getFS(),
          ProgramLWJGL30Test.getGL());
      Assert.assertEquals(true, r);
    }
    p.addFragmentShader(new PathVirtual("/nonexistent"));
    {
      final boolean r =
        p.requiresCompilation(
          ProgramLWJGL30Test.getFS(),
          ProgramLWJGL30Test.getGL());
      Assert.assertEquals(true, r);
    }
  }

  @Test public void testProgramAddFragmentShaderRequiresCompilation()
    throws FilesystemError,
      IOException,
      ConstraintError,
      GLCompileException,
      GLException
  {
    final GLInterface gl = ProgramLWJGL30Test.getGL();
    final Filesystem fs = ProgramLWJGL30Test.getFS();
    fs.mount("test_lwjgl30.zip", "/");

    final Program p = new Program("program", ProgramLWJGL30Test.getLog());
    p.addVertexShader(new PathVirtual("/shaders/attribute0.v"));
    p.compile(fs, gl);
    Assert.assertFalse(p.requiresCompilation(fs, gl));

    p.addFragmentShader(new PathVirtual("/shaders/simple.f"));
    Assert.assertTrue(p.requiresCompilation(fs, gl));
  }

  /**
   * Adding a shader twice does nothing.
   */

  @Test public void testProgramAddFragmentShaderTwice()
    throws IOException,
      ConstraintError
  {
    final Program p = new Program("program", ProgramLWJGL30Test.getLog());
    p.addFragmentShader(new PathVirtual("/nonexistent"));
    p.addFragmentShader(new PathVirtual("/nonexistent"));
  }

  /**
   * Adding a nonexistent shader does not fail.
   */

  @Test public void testProgramAddVertexShaderNonexistent()
    throws IOException,
      ConstraintError
  {
    final Program p = new Program("program", ProgramLWJGL30Test.getLog());
    p.addVertexShader(new PathVirtual("/nonexistent"));
  }

  /**
   * Adding a shader to an uncompiled program preserves the compilation
   * requirement.
   * 
   * @throws GLException
   */

  @Test public
    void
    testProgramAddVertexShaderPreservesCompilationRequirement()
      throws IOException,
        ConstraintError,
        FilesystemError,
        GLException
  {
    final Program p = new Program("program", ProgramLWJGL30Test.getLog());
    {
      final boolean r =
        p.requiresCompilation(
          ProgramLWJGL30Test.getFS(),
          ProgramLWJGL30Test.getGL());
      Assert.assertEquals(true, r);
    }
    p.addVertexShader(new PathVirtual("/nonexistent"));
    {
      final boolean r =
        p.requiresCompilation(
          ProgramLWJGL30Test.getFS(),
          ProgramLWJGL30Test.getGL());
      Assert.assertEquals(true, r);
    }
  }

  @Test public void testProgramAddVertexShaderRequiresCompilation()
    throws FilesystemError,
      IOException,
      ConstraintError,
      GLCompileException,
      GLException
  {
    final GLInterface gl = ProgramLWJGL30Test.getGL();
    final Filesystem fs = ProgramLWJGL30Test.getFS();
    fs.mount("test_lwjgl30.zip", "/");

    final Program p = new Program("program", ProgramLWJGL30Test.getLog());
    p.addFragmentShader(new PathVirtual("/shaders/simple.f"));
    p.compile(fs, gl);
    Assert.assertFalse(p.requiresCompilation(fs, gl));

    p.addVertexShader(new PathVirtual("/shaders/attribute0.v"));
    Assert.assertTrue(p.requiresCompilation(fs, gl));
  }

  /**
   * Adding a shader twice does nothing.
   */

  @Test public void testProgramAddVertexShaderTwice()
    throws IOException,
      ConstraintError
  {
    final Program p = new Program("program", ProgramLWJGL30Test.getLog());
    p.addVertexShader(new PathVirtual("/nonexistent"));
    p.addVertexShader(new PathVirtual("/nonexistent"));
  }

  @Test public void testProgramAttributeFloat()
    throws FilesystemError,
      IOException,
      ConstraintError,
      GLCompileException,
      GLException
  {
    final GLInterface gl = ProgramLWJGL30Test.getGL();
    final Filesystem fs = ProgramLWJGL30Test.getFS();
    fs.mount("test_lwjgl30.zip", "/");

    final Program p = new Program("program", ProgramLWJGL30Test.getLog());
    p.addVertexShader(new PathVirtual("/shaders/attribute0.v"));
    p.compile(fs, gl);
    Assert.assertFalse(p.requiresCompilation(fs, gl));

    final ProgramAttribute a = p.getAttribute("vertex");
    Assert.assertTrue(a != null);
    assert a != null;
    Assert.assertEquals(p.getID(), a.getProgram());

    Assert.assertEquals(GLType.Type.TYPE_FLOAT_VECTOR_4, a.getType());
    Assert.assertEquals("vertex", a.getName());
  }

  @Test public void testProgramCompileFragmentRemovesRequirement()
    throws IOException,
      ConstraintError,
      GLCompileException,
      FilesystemError,
      GLException
  {
    final Filesystem fs = ProgramLWJGL30Test.getFS();
    fs.mount("test_lwjgl30.zip", "/");

    final Program p = new Program("program", ProgramLWJGL30Test.getLog());
    p.addFragmentShader(new PathVirtual("/shaders/simple.f"));
    p.compile(fs, ProgramLWJGL30Test.getGL());
    Assert.assertFalse(p.requiresCompilation(fs, ProgramLWJGL30Test.getGL()));
    p.delete(ProgramLWJGL30Test.getGL());
  }

  /**
   * A nonexistent shader causes a compilation error.
   * 
   * @throws GLException
   */

  @Test(expected = GLCompileException.class) public
    void
    testProgramCompileFragmentShaderNonexistent()
      throws IOException,
        ConstraintError,
        GLCompileException,
        FilesystemError,
        GLException
  {
    final Program p = new Program("program", ProgramLWJGL30Test.getLog());
    p.addFragmentShader(new PathVirtual("/nonexistent"));
    try {
      p.compile(ProgramLWJGL30Test.getFS(), ProgramLWJGL30Test.getGL());
    } catch (final GLCompileException e) {
      Assert.assertTrue(e.getMessage().endsWith(
        "file not found '/nonexistent'"));
      throw e;
    }
  }

  /**
   * Compiling an empty program is illegal (some implementations don't accept
   * empty shading programs, notably the Mesa "software" implementation).
   * 
   * @throws GLException
   */

  @Test(expected = GLCompileException.class) public
    void
    testProgramCompileNothing()
      throws IOException,
        ConstraintError,
        FilesystemError,
        GLCompileException,
        GLException
  {
    final Filesystem fs = ProgramLWJGL30Test.getFS();
    fs.mount("test_lwjgl30.zip", "/");

    final GLInterface gl = ProgramLWJGL30Test.getGL();
    final Program p = new Program("program", ProgramLWJGL30Test.getLog());

    try {
      p.compile(fs, gl);
    } catch (final GLCompileException e) {
      Assert.assertTrue(e.getMessage().endsWith("empty program"));
      throw e;
    }
  }

  @Test public void testProgramCompileTwiceRedundant()
    throws FilesystemError,
      IOException,
      ConstraintError,
      GLCompileException,
      GLException
  {
    final GLInterface gl = ProgramLWJGL30Test.getGL();
    final Filesystem fs = ProgramLWJGL30Test.getFS();
    fs.mount("test_lwjgl30.zip", "/");

    final Program p = new Program("program", ProgramLWJGL30Test.getLog());
    p.addVertexShader(new PathVirtual("/shaders/simple.v"));
    p.addFragmentShader(new PathVirtual("/shaders/simple.f"));

    p.compile(fs, gl);
    Assert.assertFalse(p.requiresCompilation(fs, gl));
    p.compile(fs, gl);
    Assert.assertFalse(p.requiresCompilation(fs, gl));
  }

  @Test public void testProgramCompileVertexRemovesRequirement()
    throws IOException,
      ConstraintError,
      GLCompileException,
      FilesystemError,
      GLException
  {
    final Filesystem fs = ProgramLWJGL30Test.getFS();
    fs.mount("test_lwjgl30.zip", "/");

    final Program p = new Program("program", ProgramLWJGL30Test.getLog());
    p.addVertexShader(new PathVirtual("/shaders/simple.v"));
    p.compile(fs, ProgramLWJGL30Test.getGL());
    Assert.assertFalse(p.requiresCompilation(fs, ProgramLWJGL30Test.getGL()));
    p.delete(ProgramLWJGL30Test.getGL());
  }

  /**
   * A nonexistent shader causes a compilation error.
   * 
   * @throws GLException
   */

  @Test(expected = GLCompileException.class) public
    void
    testProgramCompileVertexShaderNonexistent()
      throws IOException,
        ConstraintError,
        GLCompileException,
        FilesystemError,
        GLException
  {
    final Program p = new Program("program", ProgramLWJGL30Test.getLog());
    p.addVertexShader(new PathVirtual("/nonexistent"));
    try {
      p.compile(ProgramLWJGL30Test.getFS(), ProgramLWJGL30Test.getGL());
    } catch (final GLCompileException e) {
      Assert.assertTrue(e.getMessage().endsWith(
        "file not found '/nonexistent'"));
      throw e;
    }
  }

  /**
   * Creating a program works.
   */

  @Test public void testProgramCreate()
    throws IOException,
      ConstraintError,
      GLException
  {
    final GLInterface gl = ProgramLWJGL30Test.getGL();
    final Program p = new Program("program", ProgramLWJGL30Test.getLog());
    Assert.assertEquals("program", p.getName());
    Assert.assertEquals(false, p.isActive(gl));
    p.delete(ProgramLWJGL30Test.getGL());
  }

  /**
   * New program requires compilation.
   * 
   * @throws GLException
   */

  @Test public void testProgramCreatedRequiresCompilation()
    throws IOException,
      ConstraintError,
      FilesystemError,
      GLException
  {
    final Program p = new Program("program", ProgramLWJGL30Test.getLog());
    final boolean r =
      p.requiresCompilation(
        ProgramLWJGL30Test.getFS(),
        ProgramLWJGL30Test.getGL());
    Assert.assertEquals(true, r);
  }

  /**
   * Creating a program with a null name fails.
   */

  @Test(expected = ConstraintError.class) public void testProgramCreateNull()
    throws IOException,
      ConstraintError,
      GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    gl.programCreate(null);
  }

  /**
   * Deactivating a program that's not active does nothing.
   */

  @Test public void testProgramDeactivateNotActive()
    throws IOException,
      ConstraintError,
      GLException,
      GLCompileException,
      FilesystemError
  {
    final Filesystem fs = ProgramLWJGL30Test.getFS();
    fs.mount("test_lwjgl30.zip", "/");

    final GLInterface gl = ProgramLWJGL30Test.getGL();
    final Program p = new Program("program", ProgramLWJGL30Test.getLog());
    p.addVertexShader(new PathVirtual("/shaders/simple.v"));
    p.compile(fs, gl);
    p.deactivate(gl);
  }

  /**
   * Deactivating a program that's not yet compiled does nothing.
   */

  @Test public void testProgramDeactivateNotCompiled()
    throws IOException,
      ConstraintError,
      GLException
  {
    final Program p = new Program("program", ProgramLWJGL30Test.getLog());
    p.deactivate(ProgramLWJGL30Test.getGL());
  }

  /**
   * Deleting a null program fails.
   */

  @Test(expected = ConstraintError.class) public void testProgramDeleteNull()
    throws IOException,
      ConstraintError,
      GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    gl.programDelete(null);
  }

  /**
   * A change in modification time for a fragment shader requires
   * recompilation.
   * 
   * @throws GLException
   */

  @Test public void testProgramFragmentShaderTimeRequiresCompilation()
    throws FilesystemError,
      IOException,
      ConstraintError,
      GLCompileException,
      GLException
  {
    final GLInterface gl = ProgramLWJGL30Test.getGL();
    final Filesystem fs = ProgramLWJGL30Test.getFS();
    fs.mount("test_lwjgl30.zip", "/");

    final Program p = new Program("program", ProgramLWJGL30Test.getLog());
    p.addFragmentShader(new PathVirtual("/shaders/simple.f"));
    p.compile(fs, gl);
    Assert.assertFalse(p.requiresCompilation(fs, gl));

    fs.unmount("/");
    fs.mount("test_lwjgl30_newer.zip", "/");
    Assert.assertTrue(p.requiresCompilation(fs, gl));
  }

  /**
   * Activating a program that's not yet compiled raises a ConstraintError.
   */

  @Test(expected = ConstraintError.class) public
    void
    testProgramNotCompiledActivate()
      throws IOException,
        ConstraintError,
        GLException
  {
    final GLInterface gl = ProgramLWJGL30Test.getGL();
    final Program p = new Program("program", ProgramLWJGL30Test.getLog());
    p.activate(gl);
  }

  /**
   * Retrieving an attribute for a program that's not yet compiled returns
   * null.
   */

  @Test public void testProgramNotCompiledAttribute()
    throws IOException,
      ConstraintError
  {
    final Program p = new Program("program", ProgramLWJGL30Test.getLog());
    final ProgramAttribute a = p.getAttribute("something");
    Assert.assertEquals(null, a);
  }

  /**
   * Retrieving the ID of a program that's not yet compiled returns null.
   */

  @Test public void testProgramNotCompiledIDNull()
    throws IOException,
      ConstraintError
  {
    final Program p = new Program("program", ProgramLWJGL30Test.getLog());
    final ProgramReference r = p.getID();
    Assert.assertEquals(null, r);
  }

  /**
   * Retrieving a uniform for a program that's not yet compiled returns null.
   */

  @Test public void testProgramNotCompiledUniform()
    throws IOException,
      ConstraintError
  {
    final Program p = new Program("program", ProgramLWJGL30Test.getLog());
    final ProgramUniform u = p.getUniform("something");
    Assert.assertEquals(null, u);
  }

  /**
   * Creating a program works.
   */

  @Test public void testProgramReferenceCreate()
    throws IOException,
      ConstraintError,
      GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    ProgramReference p = null;

    try {
      p = gl.programCreate("program");
      Assert.assertEquals("program", p.getName());
    } finally {
      if (p != null) {
        p.resourceDelete(gl);
      }
    }
  }

  /**
   * Removing a shader that's not added does nothing.
   */

  @Test public void testProgramRemoveFragmentShaderNone()
    throws IOException,
      ConstraintError,
      GLException
  {
    final Program p = new Program("program", ProgramLWJGL30Test.getLog());
    p.removeFragmentShader(
      new PathVirtual("/nonexistent"),
      ProgramLWJGL30Test.getGL());
  }

  /**
   * Removing a shader that's not compiled does nothing.
   */

  @Test public void testProgramRemoveFragmentShaderNotCompiled()
    throws IOException,
      ConstraintError,
      GLException
  {
    final Program p = new Program("program", ProgramLWJGL30Test.getLog());
    p.addFragmentShader(new PathVirtual("/nonexistent"));
    p.removeFragmentShader(
      new PathVirtual("/nonexistent"),
      ProgramLWJGL30Test.getGL());
  }

  /**
   * Removing a shader that's not added does nothing.
   */

  @Test public void testProgramRemoveVertexShaderNone()
    throws IOException,
      ConstraintError,
      GLException
  {
    final Program p = new Program("program", ProgramLWJGL30Test.getLog());
    p.removeVertexShader(
      new PathVirtual("/nonexistent"),
      ProgramLWJGL30Test.getGL());
  }

  /**
   * Removing a shader that's not compiled does nothing.
   */

  @Test public void testProgramRemoveVertexShaderNotCompiled()
    throws IOException,
      ConstraintError,
      GLException
  {
    final Program p = new Program("program", ProgramLWJGL30Test.getLog());
    p.addVertexShader(new PathVirtual("/nonexistent"));
    p.removeVertexShader(
      new PathVirtual("/nonexistent"),
      ProgramLWJGL30Test.getGL());
  }

  @Test public void testProgramUniformFloat()
    throws FilesystemError,
      IOException,
      ConstraintError,
      GLCompileException,
      GLException
  {
    final GLInterface gl = ProgramLWJGL30Test.getGL();
    final Filesystem fs = ProgramLWJGL30Test.getFS();
    fs.mount("test_lwjgl30.zip", "/");

    final Program p = new Program("program", ProgramLWJGL30Test.getLog());
    p.addFragmentShader(new PathVirtual("/shaders/uniform0.f"));
    p.compile(fs, gl);
    Assert.assertFalse(p.requiresCompilation(fs, gl));

    final ProgramUniform u = p.getUniform("alpha");
    Assert.assertTrue(u != null);
    assert u != null;
    Assert.assertEquals(p.getID(), u.getProgram());

    Assert.assertEquals(GLType.Type.TYPE_FLOAT, u.getType());
    Assert.assertEquals("alpha", u.getName());
  }

  /**
   * A change in modification time for a vertex shader requires recompilation.
   * 
   * @throws GLException
   */

  @Test public void testProgramVertexShaderTimeRequiresCompilation()
    throws FilesystemError,
      IOException,
      ConstraintError,
      GLCompileException,
      GLException
  {
    final GLInterface gl = ProgramLWJGL30Test.getGL();
    final Filesystem fs = ProgramLWJGL30Test.getFS();
    fs.mount("test_lwjgl30.zip", "/");

    final Program p = new Program("program", ProgramLWJGL30Test.getLog());
    p.addVertexShader(new PathVirtual("/shaders/simple.v"));
    p.compile(fs, gl);
    Assert.assertFalse(p.requiresCompilation(fs, gl));

    fs.unmount("/");
    fs.mount("test_lwjgl30_newer.zip", "/");
    Assert.assertTrue(p.requiresCompilation(fs, gl));
  }
}
