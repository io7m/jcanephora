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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.FragmentShader;
import com.io7m.jcanephora.JCGLCompileException;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLInterfaceCommon;
import com.io7m.jcanephora.JCGLMeta;
import com.io7m.jcanephora.JCGLShaders;
import com.io7m.jcanephora.JCGLType;
import com.io7m.jcanephora.JCGLUnsupportedException;
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
import com.io7m.jtensors.VectorI3I;
import com.io7m.jtensors.VectorI4F;
import com.io7m.jtensors.VectorI4I;
import com.io7m.jtensors.VectorReadable2F;
import com.io7m.jtensors.VectorReadable3F;
import com.io7m.jtensors.VectorReadable4F;
import com.io7m.jvvfs.FSCapabilityAll;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathVirtual;

public abstract class ProgramContract implements TestContract
{
  static List<String> readLines(
    final FSCapabilityAll filesystem,
    final PathVirtual path)
    throws FilesystemError,
      ConstraintError,
      IOException
  {
    final BufferedReader reader =
      new BufferedReader(new InputStreamReader(filesystem.openFile(path)));
    final ArrayList<String> lines = new ArrayList<String>();
    for (;;) {
      final String line = reader.readLine();
      if (line == null) {
        break;
      }
      lines.add(line + "\n");
    }
    reader.close();
    return lines;
  }

  private final static
    <G extends JCGLShaders & JCGLMeta>
    Program
    makeLargeShader(
      final TestContext tc,
      final G gl)
      throws ConstraintError,
        JCGLCompileException
  {
    final Program p = new Program("program", tc.getLog());

    final PathVirtual path = tc.getShaderPath();
    p.addVertexShader(PathVirtual.ofString(path + "/large.v"));
    p.addFragmentShader(PathVirtual.ofString(path + "/texture.f"));
    p.compile(tc.getFilesystem(), gl);

    return p;
  }

  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  /**
   * Deleting a fragment shader twice fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testFragmentShaderDeleteTwice()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        FilesystemError,
        JCGLCompileException,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();

    final FragmentShader fr =
      gl.fragmentShaderCompile(
        "frag",
        ProgramContract.readLines(
          fs,
          tc.getShaderPath().appendName("simple.f")));

    gl.fragmentShaderDelete(fr);
    gl.fragmentShaderDelete(fr);
  }

  /**
   * Activating/deactivating a program works.
   */

  @Test public final void testProgramActivation()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();

    final Program p = new Program("program", tc.getLog());
    p.addVertexShader(PathVirtual.ofString(tc.getShaderPath() + "/simple.v"));
    p
      .addFragmentShader(PathVirtual.ofString(tc.getShaderPath()
        + "/simple.f"));
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
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();

    final Program p = new Program("program", tc.getLog());
    p.addFragmentShader(PathVirtual.ofString("/nonexistent"));
  }

  /**
   * Adding a shader causes the program to require compilation.
   */

  @SuppressWarnings("boxing") @Test public final
    void
    testProgramAddFragmentShaderPreservesCompilationRequirement()
      throws ConstraintError,
        FilesystemError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();

    final Program p = new Program("program", tc.getLog());
    {
      final boolean r = p.requiresCompilation(fs, gl);
      Assert.assertEquals(true, r);
    }
    p.addFragmentShader(PathVirtual.ofString("/nonexistent"));
    {
      final boolean r = p.requiresCompilation(fs, gl);
      Assert.assertEquals(true, r);
    }
  }

  @Test public final void testProgramAddFragmentShaderRequiresCompilation()
    throws FilesystemError,
      ConstraintError,
      JCGLCompileException,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();

    final Program p = new Program("program", tc.getLog());
    final PathVirtual path = tc.getShaderPath();

    p.addVertexShader(PathVirtual.ofString(path + "/attribute0.v"));
    p.addFragmentShader(PathVirtual.ofString(path + "/simple.f"));
    p.compile(fs, gl);
    Assert.assertFalse(p.requiresCompilation(fs, gl));

    p.addFragmentShader(PathVirtual.ofString(path + "/func.f"));
    Assert.assertTrue(p.requiresCompilation(fs, gl));
  }

  /**
   * Adding a shader twice does nothing.
   * 
   */

  @Test public final void testProgramAddFragmentShaderTwice()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();

    final Program p = new Program("program", tc.getLog());
    p.addFragmentShader(PathVirtual.ofString("/nonexistent"));
    p.addFragmentShader(PathVirtual.ofString("/nonexistent"));
  }

  /**
   * Adding a nonexistent shader does not fail.
   * 
   */

  @Test public final void testProgramAddVertexShaderNonexistent()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();

    final Program p = new Program("program", tc.getLog());
    p.addVertexShader(PathVirtual.ofString("/nonexistent"));
  }

  /**
   * Adding a shader to an uncompiled program preserves the compilation
   * requirement.
   * 
   */

  @SuppressWarnings("boxing") @Test public final
    void
    testProgramAddVertexShaderPreservesCompilationRequirement()
      throws ConstraintError,
        FilesystemError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();

    final Program p = new Program("program", tc.getLog());
    {
      final boolean r = p.requiresCompilation(fs, gl);
      Assert.assertEquals(true, r);
    }
    p.addVertexShader(PathVirtual.ofString("/nonexistent"));
    {
      final boolean r = p.requiresCompilation(fs, gl);
      Assert.assertEquals(true, r);
    }
  }

  @Test public final void testProgramAddVertexShaderRequiresCompilation()
    throws FilesystemError,
      ConstraintError,
      JCGLCompileException,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();

    final Program p = new Program("program", tc.getLog());
    final PathVirtual path = tc.getShaderPath();

    p.addVertexShader(PathVirtual.ofString(path + "/simple.v"));
    p.addFragmentShader(PathVirtual.ofString(path + "/simple.f"));
    p.compile(fs, gl);
    Assert.assertFalse(p.requiresCompilation(fs, gl));

    p.addVertexShader(PathVirtual.ofString(path + "/func.v"));
    Assert.assertTrue(p.requiresCompilation(fs, gl));
  }

  /**
   * Adding a shader twice does nothing.
   * 
   */

  @Test public final void testProgramAddVertexShaderTwice()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();

    final Program p = new Program("program", tc.getLog());
    p.addVertexShader(PathVirtual.ofString("/nonexistent"));
    p.addVertexShader(PathVirtual.ofString("/nonexistent"));
  }

  @Test public final void testProgramAttributeFloat()
    throws FilesystemError,
      ConstraintError,
      JCGLCompileException,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();

    final FSCapabilityAll fs = tc.getFilesystem();

    final Program p = new Program("program", tc.getLog());
    final PathVirtual path = tc.getShaderPath();

    p.addVertexShader(PathVirtual.ofString(path + "/attribute0.v"));
    p.addFragmentShader(PathVirtual.ofString(path + "/simple.f"));
    p.compile(fs, gl);
    Assert.assertFalse(p.requiresCompilation(fs, gl));

    final ProgramAttribute a = p.getAttribute("vertex");
    Assert.assertTrue(a != null);
    assert a != null;
    Assert.assertEquals(p.getID(), a.getProgram());

    Assert.assertEquals(JCGLType.TYPE_FLOAT_VECTOR_4, a.getType());
    Assert.assertEquals("vertex", a.getName());
  }

  /**
   * Check that the number of available vertex attributes is sane.
   * 
   */

  @Test public final void testProgramAttributes()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    Assert.assertTrue(gl.programGetMaximumActiveAttributes() >= 8);
  }

  @Test public final void testProgramCompileDeleteCompile()
    throws ConstraintError,
      JCGLCompileException,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();

    final FSCapabilityAll fs = tc.getFilesystem();

    final PathVirtual path = tc.getShaderPath();

    final Program p = new Program("program", tc.getLog());
    p.addVertexShader(PathVirtual.ofString(path + "/simple.v"));
    p.addFragmentShader(PathVirtual.ofString(path + "/simple.f"));
    p.compile(fs, gl);
    p.activate(gl);
    p.deactivate(gl);
    p.delete(gl);

    final Program q = new Program("program", tc.getLog());
    q.addVertexShader(PathVirtual.ofString(path + "/simple.v"));
    q.addFragmentShader(PathVirtual.ofString(path + "/simple.f"));
    q.compile(fs, gl);
    q.activate(gl);
    q.deactivate(gl);
    q.delete(gl);
  }

  /**
   * A nonexistent shader causes a compilation error.
   * 
   */

  @Test(expected = JCGLCompileException.class) public final
    void
    testProgramCompileFragmentShaderNonexistent()
      throws ConstraintError,
        JCGLCompileException,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();

    final FSCapabilityAll fs = tc.getFilesystem();

    final PathVirtual path = tc.getShaderPath();
    final Program p = new Program("program", tc.getLog());
    p.addFragmentShader(PathVirtual.ofString("/nonexistent"));
    p.addVertexShader(PathVirtual.ofString(path + "/simple.v"));

    try {
      p.compile(fs, gl);
    } catch (final JCGLCompileException e) {
      Assert.assertTrue(e.getMessage().endsWith(
        "file not found '/nonexistent'"));
      throw e;
    }
  }

  /**
   * Compiling an invalid fragment shader fails.
   * 
   */

  @Test(expected = JCGLCompileException.class) public final
    void
    testProgramCompileInvalidFragment()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();

    final FSCapabilityAll fs = tc.getFilesystem();

    final PathVirtual path = tc.getShaderPath();
    final Program p = new Program("program", tc.getLog());
    p.addFragmentShader(PathVirtual.ofString(path + "/invalid.f"));
    p.compile(fs, gl);
  }

  /**
   * Compiling an invalid vertex shader fails.
   * 
   */

  @Test(expected = JCGLCompileException.class) public final
    void
    testProgramCompileInvalidVertex()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();

    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual path = tc.getShaderPath();

    final Program p = new Program("program", tc.getLog());
    p.addVertexShader(PathVirtual.ofString(path + "/invalid.v"));
    p.compile(fs, gl);
  }

  /**
   * Compiling a program without a fragment shader fails (in order to comply
   * with GLSL ES 1.0 restrictions).
   * 
   */

  @Test(expected = JCGLCompileException.class) public final
    void
    testProgramCompileNoFragmentShader()
      throws ConstraintError,
        JCGLCompileException,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();

    final FSCapabilityAll fs = tc.getFilesystem();

    final PathVirtual path = tc.getShaderPath();
    final Program p = new Program("program", tc.getLog());
    p.addVertexShader(PathVirtual.ofString(path + "/simple.v"));

    try {
      p.compile(fs, gl);
    } catch (final JCGLCompileException e) {
      Assert.assertTrue(e.getMessage().endsWith(
        "at least one fragment shader is required"));
      throw e;
    }
  }

  /**
   * Compiling a program without a vertex shader fails (in order to comply
   * with GLSL ES 1.0 restrictions).
   * 
   */

  @Test(expected = JCGLCompileException.class) public final
    void
    testProgramCompileNoVertexShader()
      throws ConstraintError,
        JCGLCompileException,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();

    final FSCapabilityAll fs = tc.getFilesystem();

    final PathVirtual path = tc.getShaderPath();
    final Program p = new Program("program", tc.getLog());
    p.addFragmentShader(PathVirtual.ofString(path + "/simple.f"));

    try {
      p.compile(fs, gl);
    } catch (final JCGLCompileException e) {
      Assert.assertTrue(e.getMessage().endsWith(
        "at least one vertex shader is required"));
      throw e;
    }
  }

  @Test public final void testProgramCompileTwiceRedundant()
    throws FilesystemError,
      ConstraintError,
      JCGLCompileException,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();

    final FSCapabilityAll fs = tc.getFilesystem();

    final PathVirtual path = tc.getShaderPath();
    final Program p = new Program("program", tc.getLog());
    p.addVertexShader(PathVirtual.ofString(path + "/simple.v"));
    p.addFragmentShader(PathVirtual.ofString(path + "/simple.f"));

    p.compile(fs, gl);
    Assert.assertFalse(p.requiresCompilation(fs, gl));
    p.compile(fs, gl);
    Assert.assertFalse(p.requiresCompilation(fs, gl));
  }

  /**
   * A nonexistent shader causes a compilation error.
   * 
   */

  @Test(expected = JCGLCompileException.class) public final
    void
    testProgramCompileVertexShaderNonexistent()
      throws ConstraintError,
        JCGLCompileException,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();

    final FSCapabilityAll fs = tc.getFilesystem();

    final PathVirtual path = tc.getShaderPath();
    final Program p = new Program("program", tc.getLog());
    p.addVertexShader(PathVirtual.ofString("/nonexistent"));
    p.addFragmentShader(PathVirtual.ofString(path + "/simple.f"));

    try {
      p.compile(fs, gl);
    } catch (final JCGLCompileException e) {
      Assert.assertTrue(e.getMessage().endsWith(
        "file not found '/nonexistent'"));
      throw e;
    }
  }

  /**
   * Creating a program works.
   */

  @SuppressWarnings("boxing") @Test public final void testProgramCreate()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final Program p = new Program("program", tc.getLog());
    Assert.assertEquals("program", p.getName());
    Assert.assertEquals(false, p.isActive(gl));
    p.delete(gl);
  }

  /**
   * New program requires compilation.
   * 
   */

  @SuppressWarnings("boxing") @Test public final
    void
    testProgramCreatedRequiresCompilation()
      throws ConstraintError,
        FilesystemError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();

    final Program p = new Program("program", tc.getLog());
    final boolean r = p.requiresCompilation(fs, gl);
    Assert.assertEquals(true, r);
  }

  /**
   * Creating a program with a null name fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramCreateNull()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    gl.programCreate(null);
  }

  /**
   * Deactivating a program that's not active does nothing.
   */

  @Test public final void testProgramDeactivateNotActive()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();

    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual path = tc.getShaderPath();

    final Program p = new Program("program", tc.getLog());
    p.addVertexShader(PathVirtual.ofString(path + "/simple.v"));
    p.addFragmentShader(PathVirtual.ofString(path + "/simple.f"));
    p.compile(fs, gl);
    p.deactivate(gl);
  }

  /**
   * Deactivating a program that's not yet compiled does nothing.
   */

  @Test public final void testProgramDeactivateNotCompiled()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final Program p = new Program("program", tc.getLog());
    p.deactivate(gl);
  }

  /**
   * Deleting a deleted program fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramDeleteDeleted()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final ProgramReference p = gl.programCreate("program");

    Assert.assertFalse(p.resourceIsDeleted());
    gl.programDelete(p);
    Assert.assertTrue(p.resourceIsDeleted());
    gl.programDelete(p);
  }

  /**
   * Deleting a null program fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramDeleteNull()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    gl.programDelete(null);
  }

  /**
   * A change in modification time for a fragment shader requires
   * recompilation.
   * 
   */

  @Test public final void testProgramFragmentShaderTimeRequiresCompilation()
    throws FilesystemError,
      ConstraintError,
      JCGLCompileException,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();

    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual path = tc.getShaderPath();

    final Program p = new Program("program", tc.getLog());
    p.addVertexShader(PathVirtual.ofString(path + "/simple.v"));
    p.addFragmentShader(PathVirtual.ofString(path + "/simple.f"));
    p.compile(fs, gl);
    Assert.assertFalse(p.requiresCompilation(fs, gl));

    final Calendar t = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    t.setTimeInMillis(0);
    fs.updateModificationTime(path.appendName("simple.f"), t);

    Assert.assertTrue(p.requiresCompilation(fs, gl));
  }

  /**
   * Linking a program with inconsistent varying parameters fails.
   * 
   */

  @Test(expected = JCGLCompileException.class) public final
    void
    testProgramLinkInconsistentVarying()
      throws ConstraintError,
        JCGLCompileException,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual path = tc.getShaderPath();

    ProgramReference pr = null;

    try {
      pr = gl.programCreate("program");
      final VertexShader v =
        gl.vertexShaderCompile(
          "vertex",
          ProgramContract.readLines(fs, path.appendName("varying0.v")));
      final FragmentShader f =
        gl.fragmentShaderCompile(
          "frag",
          ProgramContract.readLines(fs, path.appendName("varying1.f")));
      gl.fragmentShaderAttach(pr, f);
      gl.vertexShaderAttach(pr, v);
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    gl.programLink(pr);
  }

  /**
   * Activating a program that's not yet compiled raises a ConstraintError.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramNotCompiledActivate()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final Program p = new Program("program", tc.getLog());
    p.activate(gl);
  }

  /**
   * Retrieving an attribute for a program that's not yet compiled returns
   * null.
   * 
   */

  @Test public final void testProgramNotCompiledAttribute()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final Program p = new Program("program", tc.getLog());
    final ProgramAttribute a = p.getAttribute("something");
    Assert.assertEquals(null, a);
  }

  /**
   * Retrieving the ID of a program that's not yet compiled returns null.
   * 
   */

  @Test public final void testProgramNotCompiledIDNull()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final Program p = new Program("program", tc.getLog());
    final ProgramReference r = p.getID();
    Assert.assertEquals(null, r);
  }

  /**
   * Retrieving a uniform for a program that's not yet compiled returns null.
   * 
   */

  @Test public final void testProgramNotCompiledUniform()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final Program p = new Program("program", tc.getLog());
    final ProgramUniform u = p.getUniform("something");
    Assert.assertEquals(null, u);
  }

  /**
   * Adding a deleted fragment shader fails.
   * 
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramReferenceAddFragmentShaderDeleted()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        FilesystemError,
        JCGLCompileException,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual path = tc.getShaderPath();

    final ProgramReference pr = gl.programCreate("program");
    final FragmentShader fr =
      gl.fragmentShaderCompile(
        "frag",
        ProgramContract.readLines(fs, path.appendName("simple.f")));

    gl.fragmentShaderDelete(fr);
    gl.fragmentShaderAttach(pr, fr);
  }

  /**
   * Adding a fragment shader to a deleted program fails.
   * 
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramReferenceAddFragmentShaderDeletedProgram()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        FilesystemError,
        JCGLCompileException,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual path = tc.getShaderPath();

    final ProgramReference pr = gl.programCreate("program");
    final FragmentShader fr =
      gl.fragmentShaderCompile(
        "frag",
        ProgramContract.readLines(fs, path.appendName("simple.f")));

    gl.fragmentShaderDelete(fr);
    gl.fragmentShaderAttach(pr, fr);
  }

  /**
   * Adding a deleted vertex shader fails.
   * 
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramReferenceAddVertexShaderDeleted()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        FilesystemError,
        JCGLCompileException,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual path = tc.getShaderPath();

    final ProgramReference pr = gl.programCreate("program");
    final VertexShader vr =
      gl.vertexShaderCompile(
        "vertex",
        ProgramContract.readLines(fs, path.appendName("simple.v")));

    gl.vertexShaderDelete(vr);
    gl.vertexShaderAttach(pr, vr);
  }

  /**
   * Adding a vertex shader to a deleted program fails.
   * 
   */

  @Test(expected = ConstraintError.class) public final
    void
    testProgramReferenceAddVertexShaderDeletedProgram()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        FilesystemError,
        JCGLCompileException,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual path = tc.getShaderPath();

    final ProgramReference pr = gl.programCreate("program");
    final VertexShader vr =
      gl.vertexShaderCompile(
        "vertex",
        ProgramContract.readLines(fs, path.appendName("simple.v")));

    gl.vertexShaderDelete(vr);
    gl.vertexShaderAttach(pr, vr);
  }

  /**
   * Creating a program works.
   */

  @Test public final void testProgramReferenceCreate()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    ProgramReference p = null;

    try {
      p = gl.programCreate("program");
      Assert.assertEquals("program", p.getName());
    } finally {
      if (p != null) {
        gl.programDelete(p);
      }
    }
  }

  /**
   * Removing a shader that's not added does nothing.
   */

  @Test public final void testProgramRemoveFragmentShaderNone()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final Program p = new Program("program", tc.getLog());
    p.removeFragmentShader(PathVirtual.ofString("/nonexistent"), gl);
  }

  /**
   * Removing a shader that's not compiled does nothing.
   */

  @Test public final void testProgramRemoveFragmentShaderNotCompiled()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final Program p = new Program("program", tc.getLog());
    p.addFragmentShader(PathVirtual.ofString("/nonexistent"));
    p.removeFragmentShader(PathVirtual.ofString("/nonexistent"), gl);
  }

  /**
   * Removing a shader that's not added does nothing.
   */

  @Test public final void testProgramRemoveVertexShaderNone()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final Program p = new Program("program", tc.getLog());
    p.removeVertexShader(PathVirtual.ofString("/nonexistent"), gl);
  }

  /**
   * Removing a shader that's not compiled does nothing.
   */

  @Test public final void testProgramRemoveVertexShaderNotCompiled()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final Program p = new Program("program", tc.getLog());
    p.addVertexShader(PathVirtual.ofString("/nonexistent"));
    p.removeVertexShader(PathVirtual.ofString("/nonexistent"), gl);
  }

  @Test public final void testProgramUniformFloat()
    throws FilesystemError,
      ConstraintError,
      JCGLCompileException,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();

    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual path = tc.getShaderPath();

    final Program p = new Program("program", tc.getLog());
    p.addVertexShader(PathVirtual.ofString(path + "/simple.v"));
    p.addFragmentShader(PathVirtual.ofString(path + "/uniform0.f"));
    p.compile(fs, gl);
    Assert.assertFalse(p.requiresCompilation(fs, gl));

    final ProgramUniform u = p.getUniform("alpha");
    Assert.assertTrue(u != null);
    assert u != null;
    Assert.assertEquals(p.getID(), u.getProgram());

    Assert.assertEquals(JCGLType.TYPE_FLOAT, u.getType());
    Assert.assertEquals("alpha", u.getName());
  }

  /**
   * Program uniforms work.
   * 
   */

  @Test public final void testProgramUniforms()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError,
      JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final Program p = ProgramContract.makeLargeShader(tc, gl);
    p.activate(gl);

    {
      final ProgramUniform u = p.getUniform("float_0");
      Assert.assertEquals(JCGLType.TYPE_FLOAT, u.getType());
      gl.programPutUniformFloat(u, 1.0f);
    }

    {
      final ProgramUniform u = p.getUniform("mat3_0");
      Assert.assertEquals(JCGLType.TYPE_FLOAT_MATRIX_3, u.getType());
      final MatrixM3x3F m = new MatrixM3x3F();
      gl.programPutUniformMatrix3x3f(u, m);
    }

    {
      final ProgramUniform u = p.getUniform("mat4_0");
      Assert.assertEquals(JCGLType.TYPE_FLOAT_MATRIX_4, u.getType());
      final MatrixM4x4F m = new MatrixM4x4F();
      gl.programPutUniformMatrix4x4f(u, m);
    }

    {
      final ProgramUniform u = p.getUniform("vec2_0");
      Assert.assertEquals(JCGLType.TYPE_FLOAT_VECTOR_2, u.getType());
      final VectorI2F v = new VectorI2F(1.0f, 2.0f);
      gl.programPutUniformVector2f(u, v);
    }

    {
      final ProgramUniform u = p.getUniform("vec2_1");
      Assert.assertEquals(JCGLType.TYPE_INTEGER_VECTOR_2, u.getType());
      final VectorI2I v = new VectorI2I(1, 2);
      gl.programPutUniformVector2i(u, v);
    }

    {
      final ProgramUniform u = p.getUniform("vec3_0");
      Assert.assertEquals(JCGLType.TYPE_FLOAT_VECTOR_3, u.getType());
      final VectorI3F v = new VectorI3F(1.0f, 2.0f, 3.0f);
      gl.programPutUniformVector3f(u, v);
    }

    {
      final ProgramUniform u = p.getUniform("vec4_0");
      Assert.assertEquals(JCGLType.TYPE_FLOAT_VECTOR_4, u.getType());
      final VectorI4F v = new VectorI4F(1.0f, 2.0f, 3.0f, 4.0f);
      gl.programPutUniformVector4f(u, v);
    }

    {
      final TextureUnit[] units = gl.textureGetUnits();
      final ProgramUniform u = p.getUniform("sampler");
      Assert.assertEquals(JCGLType.TYPE_SAMPLER_2D, u.getType());
      gl.programPutUniformTextureUnit(u, units[0]);
    }
  }

  /**
   * Null program uniforms fail.
   * 
   */

  @Test public final void testProgramUniformsNullFails()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError,
      JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final Program p = ProgramContract.makeLargeShader(tc, gl);
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

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformTypeWrongFloat()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();

    final Program p = ProgramContract.makeLargeShader(tc, gl);
    p.activate(gl);

    {
      final ProgramUniform u = p.getUniform("float_0");
      Assert.assertEquals(JCGLType.TYPE_FLOAT, u.getType());
      final VectorI2F v = new VectorI2F(1.0f, 2.0f);
      gl.programPutUniformVector2f(u, v);
    }
  }

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformTypeWrongFloatNot()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();

    final Program p = ProgramContract.makeLargeShader(tc, gl);
    p.activate(gl);

    {
      final ProgramUniform u = p.getUniform("vec2_0");
      Assert.assertEquals(JCGLType.TYPE_FLOAT_VECTOR_2, u.getType());
      gl.programPutUniformFloat(u, 1.0f);
    }
  }

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformTypeWrongMatrix3x3()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();

    final Program p = ProgramContract.makeLargeShader(tc, gl);
    p.activate(gl);

    {
      final ProgramUniform u = p.getUniform("float_0");
      Assert.assertEquals(JCGLType.TYPE_FLOAT, u.getType());
      final MatrixM3x3F m = new MatrixM3x3F();
      gl.programPutUniformMatrix3x3f(u, m);
    }
  }

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformTypeWrongMatrix4x4()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();

    final Program p = ProgramContract.makeLargeShader(tc, gl);
    p.activate(gl);

    {
      final ProgramUniform u = p.getUniform("float_0");
      Assert.assertEquals(JCGLType.TYPE_FLOAT, u.getType());
      final MatrixM4x4F m = new MatrixM4x4F();
      gl.programPutUniformMatrix4x4f(u, m);
    }
  }

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformTypeWrongTextureUnit()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final Program p = ProgramContract.makeLargeShader(tc, gl);
    p.activate(gl);

    {
      final ProgramUniform u = p.getUniform("float_0");
      Assert.assertEquals(JCGLType.TYPE_FLOAT, u.getType());
      final TextureUnit[] units = gl.textureGetUnits();
      gl.programPutUniformTextureUnit(u, units[0]);
    }
  }

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformTypeWrongVector2f()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();

    final Program p = ProgramContract.makeLargeShader(tc, gl);
    p.activate(gl);

    {
      final ProgramUniform u = p.getUniform("float_0");
      Assert.assertEquals(JCGLType.TYPE_FLOAT, u.getType());
      final VectorReadable2F v = new VectorI2F(1.0f, 1.0f);
      gl.programPutUniformVector2f(u, v);
    }
  }

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformTypeWrongVector2i()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();

    final Program p = ProgramContract.makeLargeShader(tc, gl);
    p.activate(gl);

    {
      final ProgramUniform u = p.getUniform("float_0");
      Assert.assertEquals(JCGLType.TYPE_FLOAT, u.getType());
      final VectorI2I v = new VectorI2I(1, 2);
      gl.programPutUniformVector2i(u, v);
    }
  }

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformTypeWrongVector3i()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();

    final Program p = ProgramContract.makeLargeShader(tc, gl);
    p.activate(gl);

    {
      final ProgramUniform u = p.getUniform("float_0");
      Assert.assertEquals(JCGLType.TYPE_FLOAT, u.getType());
      final VectorI3I v = new VectorI3I(1, 2, 3);
      gl.programPutUniformVector3i(u, v);
    }
  }

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformTypeWrongVector4i()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();

    final Program p = ProgramContract.makeLargeShader(tc, gl);
    p.activate(gl);

    {
      final ProgramUniform u = p.getUniform("float_0");
      Assert.assertEquals(JCGLType.TYPE_FLOAT, u.getType());
      final VectorI4I v = new VectorI4I(1, 2, 3, 4);
      gl.programPutUniformVector4i(u, v);
    }
  }

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformTypeWrongVector3f()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();

    final Program p = ProgramContract.makeLargeShader(tc, gl);
    p.activate(gl);

    {
      final ProgramUniform u = p.getUniform("float_0");
      Assert.assertEquals(JCGLType.TYPE_FLOAT, u.getType());
      final VectorReadable3F v = new VectorI3F(1.0f, 1.0f, 1.0f);
      gl.programPutUniformVector3f(u, v);
    }
  }

  @Test(expected = ConstraintError.class) public final
    void
    testProgramUniformTypeWrongVector4f()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final Program p = ProgramContract.makeLargeShader(tc, gl);
    p.activate(gl);

    {
      final ProgramUniform u = p.getUniform("float_0");
      Assert.assertEquals(JCGLType.TYPE_FLOAT, u.getType());
      final VectorReadable4F v = new VectorI4F(1.0f, 1.0f, 1.0f, 1.0f);
      gl.programPutUniformVector4f(u, v);
    }
  }

  /**
   * A change in modification time for a vertex shader requires recompilation.
   */

  @Test public final void testProgramVertexShaderTimeRequiresCompilation()
    throws FilesystemError,
      ConstraintError,
      JCGLCompileException,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual path = tc.getShaderPath();

    final Program p = new Program("program", tc.getLog());
    p.addVertexShader(PathVirtual.ofString(path + "/simple.v"));
    p.addFragmentShader(PathVirtual.ofString(path + "/simple.f"));
    p.compile(fs, gl);
    Assert.assertFalse(p.requiresCompilation(fs, gl));

    final Calendar t = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    t.setTimeInMillis(0);
    fs.updateModificationTime(path.appendName("simple.v"), t);
    Assert.assertTrue(p.requiresCompilation(fs, gl));
  }

  /**
   * Structs in GLSL programs are reported predictably.
   */

  @Test public final void testShaderStruct()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      FilesystemError,
      JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual path = tc.getShaderPath();

    final Program p = new Program("program", tc.getLog());
    p.addVertexShader(PathVirtual.ofString(path + "/simple.v"));
    p.addFragmentShader(PathVirtual.ofString(path + "/struct.f"));
    p.compile(fs, gl);
    Assert.assertFalse(p.requiresCompilation(fs, gl));

    {
      final ProgramUniform u = p.getUniform("color.red");
      Assert.assertNotNull(u);
      Assert.assertEquals(JCGLType.TYPE_FLOAT, u.getType());
    }

    {
      final ProgramUniform u = p.getUniform("color.green");
      Assert.assertNotNull(u);
      Assert.assertEquals(JCGLType.TYPE_FLOAT, u.getType());
    }

    {
      final ProgramUniform u = p.getUniform("color.blue");
      Assert.assertNotNull(u);
      Assert.assertEquals(JCGLType.TYPE_FLOAT, u.getType());
    }
  }

  /**
   * Deleting a vertex shader twice fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testVertexShaderDeleteTwice()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        FilesystemError,
        JCGLCompileException,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual path = tc.getShaderPath();

    final VertexShader vr =
      gl.vertexShaderCompile(
        "vertex",
        ProgramContract.readLines(fs, path.appendName("simple.v")));

    gl.vertexShaderDelete(vr);
    gl.vertexShaderDelete(vr);
  }
}
