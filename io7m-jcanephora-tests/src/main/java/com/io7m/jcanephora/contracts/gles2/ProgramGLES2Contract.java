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
import com.io7m.jvvfs.FSCapabilityAll;
import com.io7m.jvvfs.FilesystemError;

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
    final FSCapabilityAll fs = tc.getFilesystem();

    final Program p = new Program("program", tc.getLog());
    p.addVertexShader(tc.getShaderPath().appendName("simple.v"));
    p.addFragmentShader(tc.getShaderPath().appendName("simple.f"));
    p.addFragmentShader(tc.getShaderPath().appendName("func.f"));
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
    final FSCapabilityAll fs = tc.getFilesystem();

    final Program p = new Program("program", tc.getLog());
    p.addVertexShader(tc.getShaderPath().appendName("simple.v"));
    p.addVertexShader(tc.getShaderPath().appendName("func.v"));
    p.addFragmentShader(tc.getShaderPath().appendName("simple.f"));
    p.compile(fs, gl);
  }
}
