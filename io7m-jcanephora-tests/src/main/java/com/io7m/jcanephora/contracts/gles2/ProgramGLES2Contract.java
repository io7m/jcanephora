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
import com.io7m.jcanephora.JCGLCompileException;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLInterfaceCommon;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.Program;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.contracts.TestContract;
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
   * @throws JCGLException
   * @throws JCGLUnsupportedException
   * @throws FilesystemError
   * @throws IOException
   * @throws JCGLCompileException
   */

  @Test(expected = JCGLCompileException.class) public final
    void
    testTwoFragmentShaders()
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

    final Program p = new Program("program", tc.getLog());
    p.addVertexShader(tc.getShaderPath().appendName("simple.v"));
    p.addFragmentShader(tc.getShaderPath().appendName("simple.f"));
    p.addFragmentShader(tc.getShaderPath().appendName("func.f"));
    p.compile(fs, gl);
  }

  /**
   * Attaching two vertex shaders on ES2, fails.
   * 
   * @throws JCGLException
   * @throws JCGLUnsupportedException
   * @throws FilesystemError
   * @throws IOException
   * @throws JCGLCompileException
   */

  @Test(expected = JCGLCompileException.class) public final
    void
    testTwoVertexShaders()
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

    final Program p = new Program("program", tc.getLog());
    p.addVertexShader(tc.getShaderPath().appendName("simple.v"));
    p.addVertexShader(tc.getShaderPath().appendName("func.v"));
    p.addFragmentShader(tc.getShaderPath().appendName("simple.f"));
    p.compile(fs, gl);
  }
}
