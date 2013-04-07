package com.io7m.jcanephora.contracts.gles2;

import java.io.IOException;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLCompileException;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterfaceCommon;
import com.io7m.jcanephora.GLUnsupportedException;
import com.io7m.jcanephora.Program;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.contracts.common.TestContract;
import com.io7m.jvvfs.FilesystemAPI;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathVirtual;

public abstract class ProgramGLES2Contract implements TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  /**
   * Attaching two fragment shaders on ES2, fails.
   * 
   * @throws GLException
   * @throws GLUnsupportedException
   * @throws FilesystemError
   * @throws IOException
   * @throws GLCompileException
   */

  @Test(expected = GLCompileException.class) public final
    void
    testTwoFragmentShaders()
      throws ConstraintError,
        GLException,
        GLUnsupportedException,
        FilesystemError,
        GLCompileException,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final GLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FilesystemAPI fs = tc.getFilesystem();

    final Program p = new Program("program", tc.getLog());
    p.addVertexShader(new PathVirtual(tc.getShaderPath() + "/simple.v"));
    p.addFragmentShader(new PathVirtual(tc.getShaderPath() + "/simple.f"));
    p.addFragmentShader(new PathVirtual(tc.getShaderPath() + "/func.f"));
    p.compile(fs, gl);
  }

  /**
   * Attaching two vertex shaders on ES2, fails.
   * 
   * @throws GLException
   * @throws GLUnsupportedException
   * @throws FilesystemError
   * @throws IOException
   * @throws GLCompileException
   */

  @Test(expected = GLCompileException.class) public final
    void
    testTwoVertexShaders()
      throws ConstraintError,
        GLException,
        GLUnsupportedException,
        FilesystemError,
        GLCompileException,
        IOException
  {
    final TestContext tc = this.newTestContext();
    final GLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FilesystemAPI fs = tc.getFilesystem();

    final Program p = new Program("program", tc.getLog());
    p.addVertexShader(new PathVirtual(tc.getShaderPath() + "/simple.v"));
    p.addVertexShader(new PathVirtual(tc.getShaderPath() + "/func.v"));
    p.addFragmentShader(new PathVirtual(tc.getShaderPath() + "/simple.f"));
    p.compile(fs, gl);
  }
}
