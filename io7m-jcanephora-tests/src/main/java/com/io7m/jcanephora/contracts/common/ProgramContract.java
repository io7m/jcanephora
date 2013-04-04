package com.io7m.jcanephora.contracts.common;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.FragmentShader;
import com.io7m.jcanephora.GLCompileException;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLMeta;
import com.io7m.jcanephora.GLShaders;
import com.io7m.jcanephora.GLTextureUnits;
import com.io7m.jcanephora.GLType;
import com.io7m.jcanephora.GLType.Type;
import com.io7m.jcanephora.GLUnsupportedException;
import com.io7m.jcanephora.Program;
import com.io7m.jcanephora.ProgramAttribute;
import com.io7m.jcanephora.ProgramReference;
import com.io7m.jcanephora.ProgramUniform;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.TextureUnit;
import com.io7m.jcanephora.VertexShader;
import com.io7m.jtensors.MatrixM3x3F;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorI2F;
import com.io7m.jtensors.VectorI2I;
import com.io7m.jtensors.VectorI3F;
import com.io7m.jtensors.VectorI4F;
import com.io7m.jtensors.VectorReadable2F;
import com.io7m.jtensors.VectorReadable3F;
import com.io7m.jtensors.VectorReadable4F;
import com.io7m.jvvfs.FilesystemAPI;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathVirtual;

public abstract class ProgramContract implements TestContract
{
  private final static Program makeLargeShader(
    final TestContext tc,
    final GLShaders gs,
    final GLMeta gm)
    throws ConstraintError,
      GLCompileException
  {
    final Program p = new Program("program", tc.getLog());

    final PathVirtual path = tc.getShaderPath();
    p.addVertexShader(new PathVirtual(path + "/large.v"));
    p.addFragmentShader(new PathVirtual(path + "/texture.f"));
    p.compile(tc.getFilesystem(), gs, gm);

    return p;
  }

  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract GLShaders getGLShaders(
    TestContext tc);

  public abstract GLMeta getGLMeta(
    TestContext tc);

  public abstract GLTextureUnits getGLTextureUnits(
    TestContext tc);

  /**
   * Deleting a fragment shader twice fails.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   * @throws FilesystemError
   * @throws IOException
   * @throws GLCompileException
   */

  @Test(expected = ConstraintError.class) public final
    void
    testFragmentShaderDeleteTwice()
      throws ConstraintError,
        GLException,
        GLUnsupportedException,
        FilesystemError,
        GLCompileException,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final FilesystemAPI fs = tc.getFilesystem();

    final FragmentShader fr =
      gs.fragmentShaderCompile(
        "frag",
        fs.openFile(tc.getShaderPath() + "/simple.f"));

    gs.fragmentShaderDelete(fr);
    gs.fragmentShaderDelete(fr);
  }

  /**
   * Activating/deactivating a program works.
   */

  @Test public final void testProgramActivation()
    throws ConstraintError,
      GLException,
      GLUnsupportedException,
      GLCompileException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final GLMeta gm = this.getGLMeta(tc);
    final FilesystemAPI fs = tc.getFilesystem();

    final Program p = new Program("program", tc.getLog());
    p.addVertexShader(new PathVirtual(tc.getShaderPath() + "/simple.v"));
    p.addFragmentShader(new PathVirtual(tc.getShaderPath() + "/simple.f"));
    p.compile(fs, gs, gm);

    Assert.assertFalse(p.isActive(gs));
    p.activate(gs);
    Assert.assertTrue(p.isActive(gs));
    p.deactivate(gs);
    Assert.assertFalse(p.isActive(gs));
  }

  /**
   * Adding a nonexistent shader does not fail.
   * 
   * @throws GLUnsupportedException
   * @throws GLException
   */

  @Test public final void testProgramAddFragmentShaderNonexistent()
    throws ConstraintError,
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();

    final Program p = new Program("program", tc.getLog());
    p.addFragmentShader(new PathVirtual("/nonexistent"));
  }

  /**
   * Adding a shader causes the program to require compilation.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   */

  @Test public final
    void
    testProgramAddFragmentShaderPreservesCompilationRequirement()
      throws ConstraintError,
        FilesystemError,
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final FilesystemAPI fs = tc.getFilesystem();

    final Program p = new Program("program", tc.getLog());
    {
      final boolean r = p.requiresCompilation(fs, gs);
      Assert.assertEquals(true, r);
    }
    p.addFragmentShader(new PathVirtual("/nonexistent"));
    {
      final boolean r = p.requiresCompilation(fs, gs);
      Assert.assertEquals(true, r);
    }
  }

  @Test public final void testProgramAddFragmentShaderRequiresCompilation()
    throws FilesystemError,
      ConstraintError,
      GLCompileException,
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final GLMeta gm = this.getGLMeta(tc);
    final FilesystemAPI fs = tc.getFilesystem();

    final Program p = new Program("program", tc.getLog());
    final PathVirtual path = tc.getShaderPath();

    p.addVertexShader(new PathVirtual(path + "/attribute0.v"));
    p.addFragmentShader(new PathVirtual(path + "/simple.f"));
    p.compile(fs, gs, gm);
    Assert.assertFalse(p.requiresCompilation(fs, gs));

    p.addFragmentShader(new PathVirtual(path + "/func.f"));
    Assert.assertTrue(p.requiresCompilation(fs, gs));
  }

  /**
   * Adding a shader twice does nothing.
   * 
   * @throws GLUnsupportedException
   * @throws GLException
   */

  @Test public final void testProgramAddFragmentShaderTwice()
    throws ConstraintError,
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();

    final Program p = new Program("program", tc.getLog());
    p.addFragmentShader(new PathVirtual("/nonexistent"));
    p.addFragmentShader(new PathVirtual("/nonexistent"));
  }

  /**
   * Adding a nonexistent shader does not fail.
   * 
   * @throws GLUnsupportedException
   * @throws GLException
   */

  @Test public final void testProgramAddVertexShaderNonexistent()
    throws ConstraintError,
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();

    final Program p = new Program("program", tc.getLog());
    p.addVertexShader(new PathVirtual("/nonexistent"));
  }

  /**
   * Adding a shader to an uncompiled program preserves the compilation
   * requirement.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   */

  @Test public final
    void
    testProgramAddVertexShaderPreservesCompilationRequirement()
      throws ConstraintError,
        FilesystemError,
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final FilesystemAPI fs = tc.getFilesystem();

    final Program p = new Program("program", tc.getLog());
    {
      final boolean r = p.requiresCompilation(fs, gs);
      Assert.assertEquals(true, r);
    }
    p.addVertexShader(new PathVirtual("/nonexistent"));
    {
      final boolean r = p.requiresCompilation(fs, gs);
      Assert.assertEquals(true, r);
    }
  }

  @Test public final void testProgramAddVertexShaderRequiresCompilation()
    throws FilesystemError,
      ConstraintError,
      GLCompileException,
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final GLMeta gm = this.getGLMeta(tc);
    final FilesystemAPI fs = tc.getFilesystem();

    final Program p = new Program("program", tc.getLog());
    final PathVirtual path = tc.getShaderPath();

    p.addVertexShader(new PathVirtual(path + "/simple.v"));
    p.addFragmentShader(new PathVirtual(path + "/simple.f"));
    p.compile(fs, gs, gm);
    Assert.assertFalse(p.requiresCompilation(fs, gs));

    p.addVertexShader(new PathVirtual(path + "/func.v"));
    Assert.assertTrue(p.requiresCompilation(fs, gs));
  }

  /**
   * Adding a shader twice does nothing.
   * 
   * @throws GLUnsupportedException
   * @throws GLException
   */

  @Test public final void testProgramAddVertexShaderTwice()
    throws ConstraintError,
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();

    final Program p = new Program("program", tc.getLog());
    p.addVertexShader(new PathVirtual("/nonexistent"));
    p.addVertexShader(new PathVirtual("/nonexistent"));
  }

  @Test public final void testProgramAttributeFloat()
    throws FilesystemError,
      ConstraintError,
      GLCompileException,
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final GLMeta gm = this.getGLMeta(tc);
    final FilesystemAPI fs = tc.getFilesystem();

    final Program p = new Program("program", tc.getLog());
    final PathVirtual path = tc.getShaderPath();

    p.addVertexShader(new PathVirtual(path + "/attribute0.v"));
    p.addFragmentShader(new PathVirtual(path + "/simple.f"));
    p.compile(fs, gs, gm);
    Assert.assertFalse(p.requiresCompilation(fs, gs));

    final ProgramAttribute a = p.getAttribute("vertex");
    Assert.assertTrue(a != null);
    assert a != null;
    Assert.assertEquals(p.getID(), a.getProgram());

    Assert.assertEquals(GLType.Type.TYPE_FLOAT_VECTOR_4, a.getType());
    Assert.assertEquals("vertex", a.getName());
  }

  /**
   * Check that the number of available vertex attributes is sane.
   * 
   * @throws ConstraintError
   * @throws GLException
   *           , GLUnsupportedException
   */

  @Test public final void testProgramAttributes()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    Assert.assertTrue(gs.programGetMaximumActiveAttributes() >= 8);
  }

  @Test public final void testProgramCompileDeleteCompile()
    throws ConstraintError,
      GLCompileException,
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final GLMeta gm = this.getGLMeta(tc);
    final FilesystemAPI fs = tc.getFilesystem();

    final PathVirtual path = tc.getShaderPath();

    final Program p = new Program("program", tc.getLog());
    p.addVertexShader(new PathVirtual(path + "/simple.v"));
    p.addFragmentShader(new PathVirtual(path + "/simple.f"));
    p.compile(fs, gs, gm);
    p.activate(gs);
    p.deactivate(gs);
    p.delete(gs);

    final Program q = new Program("program", tc.getLog());
    q.addVertexShader(new PathVirtual(path + "/simple.v"));
    q.addFragmentShader(new PathVirtual(path + "/simple.f"));
    q.compile(fs, gs, gm);
    q.activate(gs);
    q.deactivate(gs);
    q.delete(gs);
  }

  /**
   * A nonexistent shader causes a compilation error.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   */

  @Test(expected = GLCompileException.class) public final
    void
    testProgramCompileFragmentShaderNonexistent()
      throws ConstraintError,
        GLCompileException,
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final GLMeta gm = this.getGLMeta(tc);
    final FilesystemAPI fs = tc.getFilesystem();

    final PathVirtual path = tc.getShaderPath();
    final Program p = new Program("program", tc.getLog());
    p.addFragmentShader(new PathVirtual("/nonexistent"));
    p.addVertexShader(new PathVirtual(path + "/simple.v"));

    try {
      p.compile(fs, gs, gm);
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
   *           , GLUnsupportedException
   * @throws FilesystemError
   * @throws GLCompileException
   */

  @Test(expected = GLCompileException.class) public final
    void
    testProgramCompileInvalidFragment()
      throws ConstraintError,
        GLException,
        GLUnsupportedException,
        FilesystemError,
        GLCompileException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final GLMeta gm = this.getGLMeta(tc);
    final FilesystemAPI fs = tc.getFilesystem();

    final PathVirtual path = tc.getShaderPath();
    final Program p = new Program("program", tc.getLog());
    p.addFragmentShader(new PathVirtual(path + "/invalid.f"));
    p.compile(fs, gs, gm);
  }

  /**
   * Compiling an invalid vertex shader fails.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   * @throws FilesystemError
   * @throws GLCompileException
   */

  @Test(expected = GLCompileException.class) public final
    void
    testProgramCompileInvalidVertex()
      throws ConstraintError,
        GLException,
        GLUnsupportedException,
        FilesystemError,
        GLCompileException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final GLMeta gm = this.getGLMeta(tc);
    final FilesystemAPI fs = tc.getFilesystem();
    final PathVirtual path = tc.getShaderPath();

    final Program p = new Program("program", tc.getLog());
    p.addVertexShader(new PathVirtual(path + "/invalid.v"));
    p.compile(fs, gs, gm);
  }

  /**
   * Compiling a program without a fragment shader fails (in order to comply
   * with GLSL ES 1.0 restrictions).
   * 
   * @throws GLException
   *           , GLUnsupportedException
   */

  @Test(expected = GLCompileException.class) public final
    void
    testProgramCompileNoFragmentShader()
      throws ConstraintError,
        GLCompileException,
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final GLMeta gm = this.getGLMeta(tc);
    final FilesystemAPI fs = tc.getFilesystem();

    final PathVirtual path = tc.getShaderPath();
    final Program p = new Program("program", tc.getLog());
    p.addVertexShader(new PathVirtual(path + "/simple.v"));

    try {
      p.compile(fs, gs, gm);
    } catch (final GLCompileException e) {
      Assert.assertTrue(e.getMessage().endsWith(
        "at least one fragment shader is required"));
      throw e;
    }
  }

  /**
   * Compiling a program without a vertex shader fails (in order to comply
   * with GLSL ES 1.0 restrictions).
   * 
   * @throws GLException
   *           , GLUnsupportedException
   */

  @Test(expected = GLCompileException.class) public final
    void
    testProgramCompileNoVertexShader()
      throws ConstraintError,
        GLCompileException,
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final GLMeta gm = this.getGLMeta(tc);
    final FilesystemAPI fs = tc.getFilesystem();

    final PathVirtual path = tc.getShaderPath();
    final Program p = new Program("program", tc.getLog());
    p.addFragmentShader(new PathVirtual(path + "/simple.f"));

    try {
      p.compile(fs, gs, gm);
    } catch (final GLCompileException e) {
      Assert.assertTrue(e.getMessage().endsWith(
        "at least one vertex shader is required"));
      throw e;
    }
  }

  @Test public final void testProgramCompileTwiceRedundant()
    throws FilesystemError,
      ConstraintError,
      GLCompileException,
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final GLMeta gm = this.getGLMeta(tc);
    final FilesystemAPI fs = tc.getFilesystem();

    final PathVirtual path = tc.getShaderPath();
    final Program p = new Program("program", tc.getLog());
    p.addVertexShader(new PathVirtual(path + "/simple.v"));
    p.addFragmentShader(new PathVirtual(path + "/simple.f"));

    p.compile(fs, gs, gm);
    Assert.assertFalse(p.requiresCompilation(fs, gs));
    p.compile(fs, gs, gm);
    Assert.assertFalse(p.requiresCompilation(fs, gs));
  }

  /**
   * A nonexistent shader causes a compilation error.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   */

  @Test(expected = GLCompileException.class) public final
    void
    testProgramCompileVertexShaderNonexistent()
      throws ConstraintError,
        GLCompileException,
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final GLMeta gm = this.getGLMeta(tc);
    final FilesystemAPI fs = tc.getFilesystem();

    final PathVirtual path = tc.getShaderPath();
    final Program p = new Program("program", tc.getLog());
    p.addVertexShader(new PathVirtual("/nonexistent"));
    p.addFragmentShader(new PathVirtual(path + "/simple.f"));

    try {
      p.compile(fs, gs, gm);
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
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final Program p = new Program("program", tc.getLog());
    Assert.assertEquals("program", p.getName());
    Assert.assertEquals(false, p.isActive(gs));
    p.delete(gs);
  }

  /**
   * New program requires compilation.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   */

  @Test public final void testProgramCreatedRequiresCompilation()
    throws ConstraintError,
      FilesystemError,
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final FilesystemAPI fs = tc.getFilesystem();

    final Program p = new Program("program", tc.getLog());
    final boolean r = p.requiresCompilation(fs, gs);
    Assert.assertEquals(true, r);
  }

  /**
   * Creating a program with a null name fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramCreateNull()
      throws ConstraintError,
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    gs.programCreate(null);
  }

  /**
   * Deactivating a program that's not active does nothing.
   */

  @Test public final void testProgramDeactivateNotActive()
    throws ConstraintError,
      GLException,
      GLUnsupportedException,
      GLCompileException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final GLMeta gm = this.getGLMeta(tc);
    final FilesystemAPI fs = tc.getFilesystem();
    final PathVirtual path = tc.getShaderPath();

    final Program p = new Program("program", tc.getLog());
    p.addVertexShader(new PathVirtual(path + "/simple.v"));
    p.addFragmentShader(new PathVirtual(path + "/simple.f"));
    p.compile(fs, gs, gm);
    p.deactivate(gs);
  }

  /**
   * Deactivating a program that's not yet compiled does nothing.
   */

  @Test public final void testProgramDeactivateNotCompiled()
    throws ConstraintError,
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final Program p = new Program("program", tc.getLog());
    p.deactivate(gs);
  }

  /**
   * Deleting a deleted program fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramDeleteDeleted()
      throws ConstraintError,
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final ProgramReference p = gs.programCreate("program");

    gs.programDelete(p);
    gs.programDelete(p);
  }

  /**
   * Deleting a null program fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramDeleteNull()
      throws ConstraintError,
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    gs.programDelete(null);
  }

  /**
   * A change in modification time for a fragment shader requires
   * recompilation.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   */

  @Test public final void testProgramFragmentShaderTimeRequiresCompilation()
    throws FilesystemError,
      ConstraintError,
      GLCompileException,
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final GLMeta gm = this.getGLMeta(tc);
    final FilesystemAPI fs = tc.getFilesystem();
    final PathVirtual path = tc.getShaderPath();

    final Program p = new Program("program", tc.getLog());
    p.addVertexShader(new PathVirtual(path + "/simple.v"));
    p.addFragmentShader(new PathVirtual(path + "/simple.f"));
    p.compile(fs, gs, gm);
    Assert.assertFalse(p.requiresCompilation(fs, gs));

    fs.touch(path + "/simple.f", 0);
    Assert.assertTrue(p.requiresCompilation(fs, gs));
  }

  /**
   * Linking a program with inconsistent varying parameters fails.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   * @throws IOException
   */

  @Test(expected = GLCompileException.class) public final
    void
    testProgramLinkInconsistentVarying()
      throws ConstraintError,
        GLCompileException,
        GLException,
        GLUnsupportedException,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final FilesystemAPI fs = tc.getFilesystem();
    final PathVirtual path = tc.getShaderPath();

    ProgramReference pr = null;

    try {
      pr = gs.programCreate("program");
      final VertexShader v =
        gs.vertexShaderCompile("vertex", fs.openFile(path + "/varying0.v"));
      final FragmentShader f =
        gs.fragmentShaderCompile("frag", fs.openFile(path + "/varying1.f"));
      gs.fragmentShaderAttach(pr, f);
      gs.vertexShaderAttach(pr, v);
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    gs.programLink(pr);
  }

  /**
   * Activating a program that's not yet compiled raises a ConstraintError.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramNotCompiledActivate()
      throws ConstraintError,
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final Program p = new Program("program", tc.getLog());
    p.activate(gs);
  }

  /**
   * Retrieving an attribute for a program that's not yet compiled returns
   * null.
   * 
   * @throws GLUnsupportedException
   * @throws GLException
   */

  @Test public final void testProgramNotCompiledAttribute()
    throws ConstraintError,
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final Program p = new Program("program", tc.getLog());
    final ProgramAttribute a = p.getAttribute("something");
    Assert.assertEquals(null, a);
  }

  /**
   * Retrieving the ID of a program that's not yet compiled returns null.
   * 
   * @throws GLUnsupportedException
   * @throws GLException
   */

  @Test public final void testProgramNotCompiledIDNull()
    throws ConstraintError,
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final Program p = new Program("program", tc.getLog());
    final ProgramReference r = p.getID();
    Assert.assertEquals(null, r);
  }

  /**
   * Retrieving a uniform for a program that's not yet compiled returns null.
   * 
   * @throws GLUnsupportedException
   * @throws GLException
   */

  @Test public final void testProgramNotCompiledUniform()
    throws ConstraintError,
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final Program p = new Program("program", tc.getLog());
    final ProgramUniform u = p.getUniform("something");
    Assert.assertEquals(null, u);
  }

  /**
   * Adding a deleted fragment shader fails.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   * @throws FilesystemError
   * @throws IOException
   * @throws GLCompileException
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramReferenceAddFragmentShaderDeleted()
      throws ConstraintError,
        GLException,
        GLUnsupportedException,
        FilesystemError,
        GLCompileException,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final FilesystemAPI fs = tc.getFilesystem();
    final PathVirtual path = tc.getShaderPath();

    final ProgramReference pr = gs.programCreate("program");
    final FragmentShader fr =
      gs.fragmentShaderCompile("frag", fs.openFile(path + "/simple.f"));

    gs.fragmentShaderDelete(fr);
    gs.fragmentShaderAttach(pr, fr);
  }

  /**
   * Adding a fragment shader to a deleted program fails.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   * @throws FilesystemError
   * @throws IOException
   * @throws GLCompileException
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramReferenceAddFragmentShaderDeletedProgram()
      throws ConstraintError,
        GLException,
        GLUnsupportedException,
        FilesystemError,
        GLCompileException,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final FilesystemAPI fs = tc.getFilesystem();
    final PathVirtual path = tc.getShaderPath();

    final ProgramReference pr = gs.programCreate("program");
    final FragmentShader fr =
      gs.fragmentShaderCompile("frag", fs.openFile(path + "/simple.f"));

    gs.fragmentShaderDelete(fr);
    gs.fragmentShaderAttach(pr, fr);
  }

  /**
   * Adding a deleted vertex shader fails.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   * @throws FilesystemError
   * @throws IOException
   * @throws GLCompileException
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramReferenceAddVertexShaderDeleted()
      throws ConstraintError,
        GLException,
        GLUnsupportedException,
        FilesystemError,
        GLCompileException,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final FilesystemAPI fs = tc.getFilesystem();
    final PathVirtual path = tc.getShaderPath();

    final ProgramReference pr = gs.programCreate("program");
    final VertexShader vr =
      gs.vertexShaderCompile("vertex", fs.openFile(path + "/simple.v"));

    gs.vertexShaderDelete(vr);
    gs.vertexShaderAttach(pr, vr);
  }

  /**
   * Adding a vertex shader to a deleted program fails.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   * @throws FilesystemError
   * @throws IOException
   * @throws GLCompileException
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramReferenceAddVertexShaderDeletedProgram()
      throws ConstraintError,
        GLException,
        GLUnsupportedException,
        FilesystemError,
        GLCompileException,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final FilesystemAPI fs = tc.getFilesystem();
    final PathVirtual path = tc.getShaderPath();

    final ProgramReference pr = gs.programCreate("program");
    final VertexShader vr =
      gs.vertexShaderCompile("vertex", fs.openFile(path + "/simple.v"));

    gs.vertexShaderDelete(vr);
    gs.vertexShaderAttach(pr, vr);
  }

  /**
   * Creating a program works.
   */

  @Test public final void testProgramReferenceCreate()
    throws ConstraintError,
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    ProgramReference p = null;

    try {
      p = gs.programCreate("program");
      Assert.assertEquals("program", p.getName());
    } finally {
      if (p != null) {
        gs.programDelete(p);
      }
    }
  }

  /**
   * Removing a shader that's not added does nothing.
   */

  @Test public final void testProgramRemoveFragmentShaderNone()
    throws ConstraintError,
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final Program p = new Program("program", tc.getLog());
    p.removeFragmentShader(new PathVirtual("/nonexistent"), gs);
  }

  /**
   * Removing a shader that's not compiled does nothing.
   */

  @Test public final void testProgramRemoveFragmentShaderNotCompiled()
    throws ConstraintError,
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final Program p = new Program("program", tc.getLog());
    p.addFragmentShader(new PathVirtual("/nonexistent"));
    p.removeFragmentShader(new PathVirtual("/nonexistent"), gs);
  }

  /**
   * Removing a shader that's not added does nothing.
   */

  @Test public final void testProgramRemoveVertexShaderNone()
    throws ConstraintError,
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final Program p = new Program("program", tc.getLog());
    p.removeVertexShader(new PathVirtual("/nonexistent"), gs);
  }

  /**
   * Removing a shader that's not compiled does nothing.
   */

  @Test public final void testProgramRemoveVertexShaderNotCompiled()
    throws ConstraintError,
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final Program p = new Program("program", tc.getLog());
    p.addVertexShader(new PathVirtual("/nonexistent"));
    p.removeVertexShader(new PathVirtual("/nonexistent"), gs);
  }

  @Test public final void testProgramUniformFloat()
    throws FilesystemError,
      ConstraintError,
      GLCompileException,
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final GLMeta gm = this.getGLMeta(tc);
    final FilesystemAPI fs = tc.getFilesystem();
    final PathVirtual path = tc.getShaderPath();

    final Program p = new Program("program", tc.getLog());
    p.addVertexShader(new PathVirtual(path + "/simple.v"));
    p.addFragmentShader(new PathVirtual(path + "/uniform0.f"));
    p.compile(fs, gs, gm);
    Assert.assertFalse(p.requiresCompilation(fs, gs));

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
   *           , GLUnsupportedException
   * @throws FilesystemError
   * @throws GLCompileException
   */

  @Test public final void testProgramUniforms()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      FilesystemError,
      GLCompileException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final GLMeta gm = this.getGLMeta(tc);
    final GLTextureUnits gt = this.getGLTextureUnits(tc);
    final Program p = ProgramContract.makeLargeShader(tc, gs, gm);
    p.activate(gs);

    {
      final ProgramUniform u = p.getUniform("float_0");
      Assert.assertEquals(Type.TYPE_FLOAT, u.getType());
      gs.programPutUniformFloat(u, 1.0f);
    }

    {
      final ProgramUniform u = p.getUniform("mat3_0");
      Assert.assertEquals(Type.TYPE_FLOAT_MATRIX_3, u.getType());
      final MatrixM3x3F m = new MatrixM3x3F();
      gs.programPutUniformMatrix3x3f(u, m);
    }

    {
      final ProgramUniform u = p.getUniform("mat4_0");
      Assert.assertEquals(Type.TYPE_FLOAT_MATRIX_4, u.getType());
      final MatrixM4x4F m = new MatrixM4x4F();
      gs.programPutUniformMatrix4x4f(u, m);
    }

    {
      final ProgramUniform u = p.getUniform("vec2_0");
      Assert.assertEquals(Type.TYPE_FLOAT_VECTOR_2, u.getType());
      final VectorI2F v = new VectorI2F(1.0f, 2.0f);
      gs.programPutUniformVector2f(u, v);
    }

    {
      final ProgramUniform u = p.getUniform("vec2_1");
      Assert.assertEquals(Type.TYPE_INTEGER_VECTOR_2, u.getType());
      final VectorI2I v = new VectorI2I(1, 2);
      gs.programPutUniformVector2i(u, v);
    }

    {
      final ProgramUniform u = p.getUniform("vec3_0");
      Assert.assertEquals(Type.TYPE_FLOAT_VECTOR_3, u.getType());
      final VectorI3F v = new VectorI3F(1.0f, 2.0f, 3.0f);
      gs.programPutUniformVector3f(u, v);
    }

    {
      final ProgramUniform u = p.getUniform("vec4_0");
      Assert.assertEquals(Type.TYPE_FLOAT_VECTOR_4, u.getType());
      final VectorI4F v = new VectorI4F(1.0f, 2.0f, 3.0f, 4.0f);
      gs.programPutUniformVector4f(u, v);
    }

    {
      final TextureUnit[] units = gt.textureGetUnits();
      final ProgramUniform u = p.getUniform("sampler");
      Assert.assertEquals(Type.TYPE_SAMPLER_2D, u.getType());
      gs.programPutUniformTextureUnit(u, units[0]);
    }
  }

  /**
   * Null program uniforms fail.
   * 
   * @throws ConstraintError
   * @throws GLException
   *           , GLUnsupportedException
   * @throws FilesystemError
   * @throws GLCompileException
   */

  @Test public final void testProgramUniformsNullFails()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      FilesystemError,
      GLCompileException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final GLMeta gm = this.getGLMeta(tc);
    final GLTextureUnits gt = this.getGLTextureUnits(tc);
    final Program p = ProgramContract.makeLargeShader(tc, gs, gm);
    p.activate(gs);

    try {
      gs.programPutUniformFloat(null, 1.0f);
    } catch (final ConstraintError e) {
      // Ok.
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    try {
      final MatrixM3x3F m = new MatrixM3x3F();
      gs.programPutUniformMatrix3x3f(null, m);
    } catch (final ConstraintError e) {
      // Ok.
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    try {
      final MatrixM4x4F m = new MatrixM4x4F();
      gs.programPutUniformMatrix4x4f(null, m);
    } catch (final ConstraintError e) {
      // Ok.
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    try {
      final VectorI2F v = new VectorI2F(1.0f, 2.0f);
      gs.programPutUniformVector2f(null, v);
    } catch (final ConstraintError e) {
      // Ok.
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    try {
      final VectorI2I v = new VectorI2I(1, 2);
      gs.programPutUniformVector2i(null, v);
    } catch (final ConstraintError e) {
      // Ok.
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    try {
      final VectorI3F v = new VectorI3F(1.0f, 2.0f, 3.0f);
      gs.programPutUniformVector3f(null, v);
    } catch (final ConstraintError e) {
      // Ok.
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    try {
      final VectorI4F v = new VectorI4F(1.0f, 2.0f, 3.0f, 4.0f);
      gs.programPutUniformVector4f(null, v);
    } catch (final ConstraintError e) {
      // Ok.
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    try {
      final TextureUnit[] units = gt.textureGetUnits();
      gs.programPutUniformTextureUnit(null, units[0]);
    } catch (final ConstraintError e) {
      // Ok.
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformTypeWrongFloat()
      throws GLException,
        GLUnsupportedException,
        ConstraintError,
        GLCompileException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final GLMeta gm = this.getGLMeta(tc);
    final Program p = ProgramContract.makeLargeShader(tc, gs, gm);
    p.activate(gs);

    {
      final ProgramUniform u = p.getUniform("float_0");
      Assert.assertEquals(Type.TYPE_FLOAT, u.getType());
      final VectorI2F v = new VectorI2F(1.0f, 2.0f);
      gs.programPutUniformVector2f(u, v);
    }
  }

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformTypeWrongFloatNot()
      throws GLException,
        GLUnsupportedException,
        ConstraintError,
        GLCompileException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final GLMeta gm = this.getGLMeta(tc);
    final Program p = ProgramContract.makeLargeShader(tc, gs, gm);
    p.activate(gs);

    {
      final ProgramUniform u = p.getUniform("vec2_0");
      Assert.assertEquals(Type.TYPE_FLOAT_VECTOR_2, u.getType());
      gs.programPutUniformFloat(u, 1.0f);
    }
  }

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformTypeWrongMatrix3x3()
      throws GLException,
        GLUnsupportedException,
        ConstraintError,
        GLCompileException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final GLMeta gm = this.getGLMeta(tc);
    final Program p = ProgramContract.makeLargeShader(tc, gs, gm);
    p.activate(gs);

    {
      final ProgramUniform u = p.getUniform("float_0");
      Assert.assertEquals(Type.TYPE_FLOAT, u.getType());
      final MatrixM3x3F m = new MatrixM3x3F();
      gs.programPutUniformMatrix3x3f(u, m);
    }
  }

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformTypeWrongMatrix4x4()
      throws GLException,
        GLUnsupportedException,
        ConstraintError,
        GLCompileException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final GLMeta gm = this.getGLMeta(tc);
    final Program p = ProgramContract.makeLargeShader(tc, gs, gm);
    p.activate(gs);

    {
      final ProgramUniform u = p.getUniform("float_0");
      Assert.assertEquals(Type.TYPE_FLOAT, u.getType());
      final MatrixM4x4F m = new MatrixM4x4F();
      gs.programPutUniformMatrix4x4f(u, m);
    }
  }

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformTypeWrongTextureUnit()
      throws GLException,
        GLUnsupportedException,
        ConstraintError,
        GLCompileException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final GLMeta gm = this.getGLMeta(tc);
    final GLTextureUnits gt = this.getGLTextureUnits(tc);
    final Program p = ProgramContract.makeLargeShader(tc, gs, gm);
    p.activate(gs);

    {
      final ProgramUniform u = p.getUniform("float_0");
      Assert.assertEquals(Type.TYPE_FLOAT, u.getType());
      final TextureUnit[] units = gt.textureGetUnits();
      gs.programPutUniformTextureUnit(u, units[0]);
    }
  }

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformTypeWrongVector2f()
      throws GLException,
        GLUnsupportedException,
        ConstraintError,
        GLCompileException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final GLMeta gm = this.getGLMeta(tc);
    final Program p = ProgramContract.makeLargeShader(tc, gs, gm);
    p.activate(gs);

    {
      final ProgramUniform u = p.getUniform("float_0");
      Assert.assertEquals(Type.TYPE_FLOAT, u.getType());
      final VectorReadable2F v = new VectorI2F(1.0f, 1.0f);
      gs.programPutUniformVector2f(u, v);
    }
  }

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformTypeWrongVector2i()
      throws GLException,
        GLUnsupportedException,
        ConstraintError,
        GLCompileException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final GLMeta gm = this.getGLMeta(tc);
    final Program p = ProgramContract.makeLargeShader(tc, gs, gm);
    p.activate(gs);

    {
      final ProgramUniform u = p.getUniform("float_0");
      Assert.assertEquals(Type.TYPE_FLOAT, u.getType());
      final VectorI2I v = new VectorI2I(1, 2);
      gs.programPutUniformVector2i(u, v);
    }
  }

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformTypeWrongVector3f()
      throws GLException,
        GLUnsupportedException,
        ConstraintError,
        GLCompileException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final GLMeta gm = this.getGLMeta(tc);
    final Program p = ProgramContract.makeLargeShader(tc, gs, gm);
    p.activate(gs);

    {
      final ProgramUniform u = p.getUniform("float_0");
      Assert.assertEquals(Type.TYPE_FLOAT, u.getType());
      final VectorReadable3F v = new VectorI3F(1.0f, 1.0f, 1.0f);
      gs.programPutUniformVector3f(u, v);
    }
  }

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformTypeWrongVector4f()
      throws GLException,
        GLUnsupportedException,
        ConstraintError,
        GLCompileException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final GLMeta gm = this.getGLMeta(tc);
    final Program p = ProgramContract.makeLargeShader(tc, gs, gm);
    p.activate(gs);

    {
      final ProgramUniform u = p.getUniform("float_0");
      Assert.assertEquals(Type.TYPE_FLOAT, u.getType());
      final VectorReadable4F v = new VectorI4F(1.0f, 1.0f, 1.0f, 1.0f);
      gs.programPutUniformVector4f(u, v);
    }
  }

  /**
   * A change in modification time for a vertex shader requires recompilation.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   */

  @Test public final void testProgramVertexShaderTimeRequiresCompilation()
    throws FilesystemError,
      ConstraintError,
      GLCompileException,
      GLException,
      GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final GLMeta gm = this.getGLMeta(tc);
    final FilesystemAPI fs = tc.getFilesystem();

    final PathVirtual path = tc.getShaderPath();

    final Program p = new Program("program", tc.getLog());
    p.addVertexShader(new PathVirtual(path + "/simple.v"));
    p.addFragmentShader(new PathVirtual(path + "/simple.f"));
    p.compile(fs, gs, gm);
    Assert.assertFalse(p.requiresCompilation(fs, gs));

    fs.touch(path + "/simple.v", 0);
    Assert.assertTrue(p.requiresCompilation(fs, gs));
  }

  /**
   * Deleting a vertex shader twice fails.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   * @throws FilesystemError
   * @throws IOException
   * @throws GLCompileException
   */

  @Test(expected = ConstraintError.class) public final
    void
    testVertexShaderDeleteTwice()
      throws ConstraintError,
        GLException,
        GLUnsupportedException,
        FilesystemError,
        GLCompileException,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final GLShaders gs = this.getGLShaders(tc);
    final FilesystemAPI fs = tc.getFilesystem();
    final PathVirtual path = tc.getShaderPath();

    final VertexShader vr =
      gs.vertexShaderCompile("vertex", fs.openFile(path + "/simple.v"));

    gs.vertexShaderDelete(vr);
    gs.vertexShaderDelete(vr);
  }
}
