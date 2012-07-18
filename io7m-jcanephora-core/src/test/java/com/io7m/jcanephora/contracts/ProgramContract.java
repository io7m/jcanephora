package com.io7m.jcanephora.contracts;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.FragmentShader;
import com.io7m.jcanephora.GLCompileException;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jcanephora.GLType;
import com.io7m.jcanephora.GLType.Type;
import com.io7m.jcanephora.Program;
import com.io7m.jcanephora.ProgramAttribute;
import com.io7m.jcanephora.ProgramReference;
import com.io7m.jcanephora.ProgramUniform;
import com.io7m.jcanephora.TextureUnit;
import com.io7m.jtensors.MatrixM3x3F;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorI2F;
import com.io7m.jtensors.VectorI2I;
import com.io7m.jtensors.VectorI3F;
import com.io7m.jtensors.VectorI4F;
import com.io7m.jvvfs.FilesystemAPI;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathVirtual;

public abstract class ProgramContract implements
  GLTestContract,
  FilesystemTestContract
{
  /**
   * Activating/deactivating a program works.
   */

  @Test public final void testProgramActivation()
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

    Assert.assertFalse(p.isActive(gl));
    p.activate(gl);
    Assert.assertTrue(p.isActive(gl));
    p.deactivate(gl);
    Assert.assertFalse(p.isActive(gl));
  }

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
   * Adding a deleted fragment shader fails.
   * 
   * @throws GLException
   * @throws FilesystemError
   * @throws IOException
   * @throws GLCompileException
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramReferenceAddFragmentShaderDeleted()
      throws ConstraintError,
        GLException,
        FilesystemError,
        GLCompileException,
        IOException
  {
    final GLInterface gl = this.getGL();
    final FilesystemAPI fs = this.getFS();
    fs.mount("test_lwjgl30.zip", "/");

    final ProgramReference pr = gl.programCreate("program");
    final FragmentShader fr =
      gl.fragmentShaderCompile("frag", fs.openFile("/shaders/simple.f"));

    fr.resourceDelete(gl);
    gl.fragmentShaderAttach(pr, fr);
  }

  /**
   * Adding a fragment shader to a deleted program fails.
   * 
   * @throws GLException
   * @throws FilesystemError
   * @throws IOException
   * @throws GLCompileException
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramReferenceAddFragmentShaderDeletedProgram()
      throws ConstraintError,
        GLException,
        FilesystemError,
        GLCompileException,
        IOException
  {
    final GLInterface gl = this.getGL();
    final FilesystemAPI fs = this.getFS();
    fs.mount("test_lwjgl30.zip", "/");

    final ProgramReference pr = gl.programCreate("program");
    final FragmentShader fr =
      gl.fragmentShaderCompile("frag", fs.openFile("/shaders/simple.f"));

    pr.resourceDelete(gl);
    gl.fragmentShaderAttach(pr, fr);
  }

  /**
   * Deleting a fragment shader twice fails.
   * 
   * @throws GLException
   * @throws FilesystemError
   * @throws IOException
   * @throws GLCompileException
   */

  @Test(expected = ConstraintError.class) public final
    void
    testFragmentShaderDeleteTwice()
      throws ConstraintError,
        GLException,
        FilesystemError,
        GLCompileException,
        IOException
  {
    final GLInterface gl = this.getGL();
    final FilesystemAPI fs = this.getFS();
    fs.mount("test_lwjgl30.zip", "/");

    final FragmentShader fr =
      gl.fragmentShaderCompile("frag", fs.openFile("/shaders/simple.f"));

    fr.resourceDelete(gl);
    fr.resourceDelete(gl);
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
   * Compiling an invalid fragment shader fails.
   * 
   * @throws GLException
   * @throws FilesystemError
   * @throws GLCompileException
   */

  @Test(expected = GLCompileException.class) public final
    void
    testProgramCompileInvalidFragment()
      throws ConstraintError,
        GLException,
        FilesystemError,
        GLCompileException
  {
    final GLInterface gl = this.getGL();
    final FilesystemAPI fs = this.getFS();
    fs.mount("test_lwjgl30.zip", "/");
    final Program p = new Program("program", this.getLog());
    p.addFragmentShader(new PathVirtual("/shaders/invalid.f"));
    p.compile(fs, gl);
  }

  /**
   * Compiling an invalid vertex shader fails.
   * 
   * @throws GLException
   * @throws FilesystemError
   * @throws GLCompileException
   */

  @Test(expected = GLCompileException.class) public final
    void
    testProgramCompileInvalidVertex()
      throws ConstraintError,
        GLException,
        FilesystemError,
        GLCompileException
  {
    final GLInterface gl = this.getGL();
    final FilesystemAPI fs = this.getFS();
    fs.mount("test_lwjgl30.zip", "/");
    final Program p = new Program("program", this.getLog());
    p.addVertexShader(new PathVirtual("/shaders/invalid.v"));
    p.compile(fs, gl);
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
   * Program uniforms work.
   * 
   * @throws ConstraintError
   * @throws GLException
   * @throws FilesystemError
   * @throws GLCompileException
   */

  @Test public final void testProgramUniforms()
    throws GLException,
      ConstraintError,
      FilesystemError,
      GLCompileException
  {
    final GLInterface gl = this.getGL();
    final FilesystemAPI fs = this.getFS();
    fs.mount("test_lwjgl30.zip", "/");

    final Program p = new Program("program", this.getLog());
    p.addVertexShader(new PathVirtual("/shaders/large.v"));
    p.addFragmentShader(new PathVirtual("/shaders/texture.f"));
    p.compile(fs, gl);
    p.activate(gl);

    {
      final ProgramUniform u = p.getUniform("float_0");
      Assert.assertEquals(Type.TYPE_FLOAT, u.getType());
      gl.programPutUniformFloat(u, 1.0f);
    }

    {
      final ProgramUniform u = p.getUniform("mat3_0");
      Assert.assertEquals(Type.TYPE_FLOAT_MATRIX_3, u.getType());
      final MatrixM3x3F m = new MatrixM3x3F();
      gl.programPutUniformMatrix3x3f(u, m);
    }

    {
      final ProgramUniform u = p.getUniform("mat4_0");
      Assert.assertEquals(Type.TYPE_FLOAT_MATRIX_4, u.getType());
      final MatrixM4x4F m = new MatrixM4x4F();
      gl.programPutUniformMatrix4x4f(u, m);
    }

    {
      final ProgramUniform u = p.getUniform("vec2_0");
      Assert.assertEquals(Type.TYPE_FLOAT_VECTOR_2, u.getType());
      final VectorI2F v = new VectorI2F(1.0f, 2.0f);
      gl.programPutUniformVector2f(u, v);
    }

    {
      final ProgramUniform u = p.getUniform("vec2_1");
      Assert.assertEquals(Type.TYPE_INTEGER_VECTOR_2, u.getType());
      final VectorI2I v = new VectorI2I(1, 2);
      gl.programPutUniformVector2i(u, v);
    }

    {
      final ProgramUniform u = p.getUniform("vec3_0");
      Assert.assertEquals(Type.TYPE_FLOAT_VECTOR_3, u.getType());
      final VectorI3F v = new VectorI3F(1.0f, 2.0f, 3.0f);
      gl.programPutUniformVector3f(u, v);
    }

    {
      final ProgramUniform u = p.getUniform("vec4_0");
      Assert.assertEquals(Type.TYPE_FLOAT_VECTOR_4, u.getType());
      final VectorI4F v = new VectorI4F(1.0f, 2.0f, 3.0f, 4.0f);
      gl.programPutUniformVector4f(u, v);
    }

    {
      final TextureUnit[] units = gl.textureGetUnits();
      final ProgramUniform u = p.getUniform("sampler");
      Assert.assertEquals(Type.TYPE_SAMPLER_2D, u.getType());
      gl.programPutUniformTextureUnit(u, units[0]);
    }
  }

  /**
   * Null program uniforms fail.
   * 
   * @throws ConstraintError
   * @throws GLException
   * @throws FilesystemError
   * @throws GLCompileException
   */

  @Test public final void testProgramUniformsNullFails()
    throws GLException,
      ConstraintError,
      FilesystemError,
      GLCompileException
  {
    final GLInterface gl = this.getGL();
    final FilesystemAPI fs = this.getFS();
    fs.mount("test_lwjgl30.zip", "/");

    final Program p = new Program("program", this.getLog());
    p.addVertexShader(new PathVirtual("/shaders/large.v"));
    p.addFragmentShader(new PathVirtual("/shaders/texture.f"));
    p.compile(fs, gl);
    p.activate(gl);

    try {
      gl.programPutUniformFloat(null, 1.0f);
    } catch (final ConstraintError e) {
      // Ok.
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    try {
      final MatrixM3x3F m = new MatrixM3x3F();
      gl.programPutUniformMatrix3x3f(null, m);
    } catch (final ConstraintError e) {
      // Ok.
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    try {
      final MatrixM4x4F m = new MatrixM4x4F();
      gl.programPutUniformMatrix4x4f(null, m);
    } catch (final ConstraintError e) {
      // Ok.
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    try {
      final VectorI2F v = new VectorI2F(1.0f, 2.0f);
      gl.programPutUniformVector2f(null, v);
    } catch (final ConstraintError e) {
      // Ok.
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    try {
      final VectorI2I v = new VectorI2I(1, 2);
      gl.programPutUniformVector2i(null, v);
    } catch (final ConstraintError e) {
      // Ok.
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    try {
      final VectorI3F v = new VectorI3F(1.0f, 2.0f, 3.0f);
      gl.programPutUniformVector3f(null, v);
    } catch (final ConstraintError e) {
      // Ok.
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    try {
      final VectorI4F v = new VectorI4F(1.0f, 2.0f, 3.0f, 4.0f);
      gl.programPutUniformVector4f(null, v);
    } catch (final ConstraintError e) {
      // Ok.
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    try {
      final TextureUnit[] units = gl.textureGetUnits();
      gl.programPutUniformTextureUnit(null, units[0]);
    } catch (final ConstraintError e) {
      // Ok.
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }
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
