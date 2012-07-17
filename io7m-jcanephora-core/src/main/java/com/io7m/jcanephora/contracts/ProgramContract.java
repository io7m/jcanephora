package com.io7m.jcanephora.contracts;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLCompileException;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jcanephora.GLType;
import com.io7m.jcanephora.Program;
import com.io7m.jcanephora.ProgramAttribute;
import com.io7m.jcanephora.ProgramReference;
import com.io7m.jcanephora.ProgramUniform;
import com.io7m.jvvfs.FilesystemAPI;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathVirtual;

public abstract class ProgramContract implements
  GLTestContract,
  FilesystemTestContract
{
  /**
   * Adding a nonexistent shader does not fail.
   */

  @Test public final void testProgramAddFragmentShaderNonexistent()
    throws ConstraintError
  {
    final Program p = new Program("program", this.getLog());
    p.addFragmentShader(new PathVirtual("/nonexistent"));
  }

  /**
   * Adding a shader causes the program to require compilation.
   * 
   * @throws GLException
   */

  @Test public final
    void
    testProgramAddFragmentShaderPreservesCompilationRequirement()
      throws ConstraintError,
        FilesystemError,
        GLException
  {
    final Program p = new Program("program", this.getLog());
    {
      final boolean r = p.requiresCompilation(this.getFS(), this.getGL());
      Assert.assertEquals(true, r);
    }
    p.addFragmentShader(new PathVirtual("/nonexistent"));
    {
      final boolean r = p.requiresCompilation(this.getFS(), this.getGL());
      Assert.assertEquals(true, r);
    }
  }

  @Test public final void testProgramAddFragmentShaderRequiresCompilation()
    throws FilesystemError,
      ConstraintError,
      GLCompileException,
      GLException
  {
    final GLInterface gl = this.getGL();
    final FilesystemAPI fs = this.getFS();
    fs.mount("test_lwjgl30.zip", "/");

    final Program p = new Program("program", this.getLog());
    p.addVertexShader(new PathVirtual("/shaders/attribute0.v"));
    p.compile(fs, gl);
    Assert.assertFalse(p.requiresCompilation(fs, gl));

    p.addFragmentShader(new PathVirtual("/shaders/simple.f"));
    Assert.assertTrue(p.requiresCompilation(fs, gl));
  }

  /**
   * Adding a shader twice does nothing.
   */

  @Test public final void testProgramAddFragmentShaderTwice()
    throws ConstraintError
  {
    final Program p = new Program("program", this.getLog());
    p.addFragmentShader(new PathVirtual("/nonexistent"));
    p.addFragmentShader(new PathVirtual("/nonexistent"));
  }

  /**
   * Adding a nonexistent shader does not fail.
   */

  @Test public final void testProgramAddVertexShaderNonexistent()
    throws ConstraintError
  {
    final Program p = new Program("program", this.getLog());
    p.addVertexShader(new PathVirtual("/nonexistent"));
  }

  /**
   * Adding a shader to an uncompiled program preserves the compilation
   * requirement.
   * 
   * @throws GLException
   */

  @Test public final
    void
    testProgramAddVertexShaderPreservesCompilationRequirement()
      throws ConstraintError,
        FilesystemError,
        GLException
  {
    final Program p = new Program("program", this.getLog());
    {
      final boolean r = p.requiresCompilation(this.getFS(), this.getGL());
      Assert.assertEquals(true, r);
    }
    p.addVertexShader(new PathVirtual("/nonexistent"));
    {
      final boolean r = p.requiresCompilation(this.getFS(), this.getGL());
      Assert.assertEquals(true, r);
    }
  }

  @Test public final void testProgramAddVertexShaderRequiresCompilation()
    throws FilesystemError,
      ConstraintError,
      GLCompileException,
      GLException
  {
    final GLInterface gl = this.getGL();
    final FilesystemAPI fs = this.getFS();
    fs.mount("test_lwjgl30.zip", "/");

    final Program p = new Program("program", this.getLog());
    p.addFragmentShader(new PathVirtual("/shaders/simple.f"));
    p.compile(fs, gl);
    Assert.assertFalse(p.requiresCompilation(fs, gl));

    p.addVertexShader(new PathVirtual("/shaders/attribute0.v"));
    Assert.assertTrue(p.requiresCompilation(fs, gl));
  }

  /**
   * Adding a shader twice does nothing.
   */

  @Test public final void testProgramAddVertexShaderTwice()
    throws ConstraintError
  {
    final Program p = new Program("program", this.getLog());
    p.addVertexShader(new PathVirtual("/nonexistent"));
    p.addVertexShader(new PathVirtual("/nonexistent"));
  }

  @Test public final void testProgramAttributeFloat()
    throws FilesystemError,
      ConstraintError,
      GLCompileException,
      GLException
  {
    final GLInterface gl = this.getGL();
    final FilesystemAPI fs = this.getFS();
    fs.mount("test_lwjgl30.zip", "/");

    final Program p = new Program("program", this.getLog());
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

  @Test public final void testProgramCompileFragmentRemovesRequirement()
    throws ConstraintError,
      GLCompileException,
      FilesystemError,
      GLException
  {
    final FilesystemAPI fs = this.getFS();
    fs.mount("test_lwjgl30.zip", "/");

    final Program p = new Program("program", this.getLog());
    p.addFragmentShader(new PathVirtual("/shaders/simple.f"));
    p.compile(fs, this.getGL());
    Assert.assertFalse(p.requiresCompilation(fs, this.getGL()));
    p.delete(this.getGL());
  }

  /**
   * A nonexistent shader causes a compilation error.
   * 
   * @throws GLException
   */

  @Test(expected = GLCompileException.class) public final
    void
    testProgramCompileFragmentShaderNonexistent()
      throws ConstraintError,
        GLCompileException,
        GLException
  {
    final Program p = new Program("program", this.getLog());
    p.addFragmentShader(new PathVirtual("/nonexistent"));
    try {
      p.compile(this.getFS(), this.getGL());
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

  @Test(expected = GLCompileException.class) public final
    void
    testProgramCompileNothing()
      throws ConstraintError,
        FilesystemError,
        GLCompileException,
        GLException
  {
    final FilesystemAPI fs = this.getFS();
    fs.mount("test_lwjgl30.zip", "/");

    final GLInterface gl = this.getGL();
    final Program p = new Program("program", this.getLog());

    try {
      p.compile(fs, gl);
    } catch (final GLCompileException e) {
      Assert.assertTrue(e.getMessage().endsWith("empty program"));
      throw e;
    }
  }

  @Test public final void testProgramCompileTwiceRedundant()
    throws FilesystemError,
      ConstraintError,
      GLCompileException,
      GLException
  {
    final GLInterface gl = this.getGL();
    final FilesystemAPI fs = this.getFS();
    fs.mount("test_lwjgl30.zip", "/");

    final Program p = new Program("program", this.getLog());
    p.addVertexShader(new PathVirtual("/shaders/simple.v"));
    p.addFragmentShader(new PathVirtual("/shaders/simple.f"));

    p.compile(fs, gl);
    Assert.assertFalse(p.requiresCompilation(fs, gl));
    p.compile(fs, gl);
    Assert.assertFalse(p.requiresCompilation(fs, gl));
  }

  @Test public final void testProgramCompileVertexRemovesRequirement()
    throws ConstraintError,
      GLCompileException,
      FilesystemError,
      GLException
  {
    final FilesystemAPI fs = this.getFS();
    fs.mount("test_lwjgl30.zip", "/");

    final Program p = new Program("program", this.getLog());
    p.addVertexShader(new PathVirtual("/shaders/simple.v"));
    p.compile(fs, this.getGL());
    Assert.assertFalse(p.requiresCompilation(fs, this.getGL()));
    p.delete(this.getGL());
  }

  /**
   * A nonexistent shader causes a compilation error.
   * 
   * @throws GLException
   */

  @Test(expected = GLCompileException.class) public final
    void
    testProgramCompileVertexShaderNonexistent()
      throws ConstraintError,
        GLCompileException,
        GLException
  {
    final Program p = new Program("program", this.getLog());
    p.addVertexShader(new PathVirtual("/nonexistent"));
    try {
      p.compile(this.getFS(), this.getGL());
    } catch (final GLCompileException e) {
      Assert.assertTrue(e.getMessage().endsWith(
        "file not found '/nonexistent'"));
      throw e;
    }
  }

  /**
   * Creating a program works.
   */

  @Test public final void testProgramCreate()
    throws ConstraintError,
      GLException
  {
    final GLInterface gl = this.getGL();
    final Program p = new Program("program", this.getLog());
    Assert.assertEquals("program", p.getName());
    Assert.assertEquals(false, p.isActive(gl));
    p.delete(this.getGL());
  }

  /**
   * New program requires compilation.
   * 
   * @throws GLException
   */

  @Test public final void testProgramCreatedRequiresCompilation()
    throws ConstraintError,
      FilesystemError,
      GLException
  {
    final Program p = new Program("program", this.getLog());
    final boolean r = p.requiresCompilation(this.getFS(), this.getGL());
    Assert.assertEquals(true, r);
  }

  /**
   * Creating a program with a null name fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramCreateNull()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.getGL();
    gl.programCreate(null);
  }

  /**
   * Deactivating a program that's not active does nothing.
   */

  @Test public final void testProgramDeactivateNotActive()
    throws ConstraintError,
      GLException,
      GLCompileException,
      FilesystemError
  {
    final FilesystemAPI fs = this.getFS();
    fs.mount("test_lwjgl30.zip", "/");

    final GLInterface gl = this.getGL();
    final Program p = new Program("program", this.getLog());
    p.addVertexShader(new PathVirtual("/shaders/simple.v"));
    p.compile(fs, gl);
    p.deactivate(gl);
  }

  /**
   * Deactivating a program that's not yet compiled does nothing.
   */

  @Test public final void testProgramDeactivateNotCompiled()
    throws ConstraintError,
      GLException
  {
    final Program p = new Program("program", this.getLog());
    p.deactivate(this.getGL());
  }

  /**
   * Deleting a null program fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramDeleteNull()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.getGL();
    gl.programDelete(null);
  }

  /**
   * A change in modification time for a fragment shader requires
   * recompilation.
   * 
   * @throws GLException
   */

  @Test public final void testProgramFragmentShaderTimeRequiresCompilation()
    throws FilesystemError,
      ConstraintError,
      GLCompileException,
      GLException
  {
    final GLInterface gl = this.getGL();
    final FilesystemAPI fs = this.getFS();
    fs.mount("test_lwjgl30.zip", "/");

    final Program p = new Program("program", this.getLog());
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

  @Test(expected = ConstraintError.class) public final
    void
    testProgramNotCompiledActivate()
      throws ConstraintError,
        GLException
  {
    final GLInterface gl = this.getGL();
    final Program p = new Program("program", this.getLog());
    p.activate(gl);
  }

  /**
   * Retrieving an attribute for a program that's not yet compiled returns
   * null.
   */

  @Test public final void testProgramNotCompiledAttribute()
    throws ConstraintError
  {
    final Program p = new Program("program", this.getLog());
    final ProgramAttribute a = p.getAttribute("something");
    Assert.assertEquals(null, a);
  }

  /**
   * Retrieving the ID of a program that's not yet compiled returns null.
   */

  @Test public final void testProgramNotCompiledIDNull()
    throws ConstraintError
  {
    final Program p = new Program("program", this.getLog());
    final ProgramReference r = p.getID();
    Assert.assertEquals(null, r);
  }

  /**
   * Retrieving a uniform for a program that's not yet compiled returns null.
   */

  @Test public final void testProgramNotCompiledUniform()
    throws ConstraintError
  {
    final Program p = new Program("program", this.getLog());
    final ProgramUniform u = p.getUniform("something");
    Assert.assertEquals(null, u);
  }

  /**
   * Creating a program works.
   */

  @Test public final void testProgramReferenceCreate()
    throws ConstraintError,
      GLException
  {
    final GLInterface gl = this.getGL();
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

  @Test public final void testProgramRemoveFragmentShaderNone()
    throws ConstraintError,
      GLException
  {
    final Program p = new Program("program", this.getLog());
    p.removeFragmentShader(new PathVirtual("/nonexistent"), this.getGL());
  }

  /**
   * Removing a shader that's not compiled does nothing.
   */

  @Test public final void testProgramRemoveFragmentShaderNotCompiled()
    throws ConstraintError,
      GLException
  {
    final Program p = new Program("program", this.getLog());
    p.addFragmentShader(new PathVirtual("/nonexistent"));
    p.removeFragmentShader(new PathVirtual("/nonexistent"), this.getGL());
  }

  /**
   * Removing a shader that's not added does nothing.
   */

  @Test public final void testProgramRemoveVertexShaderNone()
    throws ConstraintError,
      GLException
  {
    final Program p = new Program("program", this.getLog());
    p.removeVertexShader(new PathVirtual("/nonexistent"), this.getGL());
  }

  /**
   * Removing a shader that's not compiled does nothing.
   */

  @Test public final void testProgramRemoveVertexShaderNotCompiled()
    throws ConstraintError,
      GLException
  {
    final Program p = new Program("program", this.getLog());
    p.addVertexShader(new PathVirtual("/nonexistent"));
    p.removeVertexShader(new PathVirtual("/nonexistent"), this.getGL());
  }

  @Test public final void testProgramUniformFloat()
    throws FilesystemError,
      ConstraintError,
      GLCompileException,
      GLException
  {
    final GLInterface gl = this.getGL();
    final FilesystemAPI fs = this.getFS();
    fs.mount("test_lwjgl30.zip", "/");

    final Program p = new Program("program", this.getLog());
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

  @Test public final void testProgramVertexShaderTimeRequiresCompilation()
    throws FilesystemError,
      ConstraintError,
      GLCompileException,
      GLException
  {
    final GLInterface gl = this.getGL();
    final FilesystemAPI fs = this.getFS();
    fs.mount("test_lwjgl30.zip", "/");

    final Program p = new Program("program", this.getLog());
    p.addVertexShader(new PathVirtual("/shaders/simple.v"));
    p.compile(fs, gl);
    Assert.assertFalse(p.requiresCompilation(fs, gl));

    fs.unmount("/");
    fs.mount("test_lwjgl30_newer.zip", "/");
    Assert.assertTrue(p.requiresCompilation(fs, gl));
  }
}
