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

package com.io7m.jcanephora.contracts.checkedexec;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import javax.annotation.Nonnull;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.FragmentShader;
import com.io7m.jcanephora.JCGLCompileException;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLInterfaceCommon;
import com.io7m.jcanephora.ProgramReference;
import com.io7m.jcanephora.ShaderUtilities;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.VertexShader;
import com.io7m.jcanephora.checkedexec.JCCEExecutionCallable;
import com.io7m.jcanephora.contracts.TestContract;
import com.io7m.jvvfs.FSCapabilityRead;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathVirtual;

public abstract class JCCEExecutionCallableContract implements TestContract
{
  static final class Counter implements Callable<Void>
  {
    public int calls = 0;

    @Override public Void call()
      throws Exception
    {
      this.calls++;
      return null;
    }
  }

  private static @Nonnull ProgramReference makeProgram(
    final @Nonnull TestContext tc,
    final @Nonnull JCGLInterfaceCommon gl)
  {
    try {
      final PathVirtual vsp = tc.getShaderPath().appendName("simple.v");
      final PathVirtual fsp = tc.getShaderPath().appendName("simple.f");

      final List<String> vs_lines =
        JCCEExecutionCallableContract.readLines(tc.getFilesystem(), vsp);
      final List<String> fs_lines =
        JCCEExecutionCallableContract.readLines(tc.getFilesystem(), fsp);

      final VertexShader v = gl.vertexShaderCompile("v", vs_lines);
      final FragmentShader f = gl.fragmentShaderCompile("f", fs_lines);
      return gl.programCreateCommon("p", v, f);
    } catch (final ConstraintError e) {
      throw new AssertionError(e);
    } catch (final FilesystemError e) {
      throw new AssertionError(e);
    } catch (final IOException e) {
      throw new AssertionError(e);
    } catch (final JCGLCompileException e) {
      throw new AssertionError(e);
    } catch (final JCGLException e) {
      throw new AssertionError(e);
    }
  }

  private static @Nonnull List<String> readLines(
    final @Nonnull FSCapabilityRead filesystem,
    final @Nonnull PathVirtual path)
    throws FilesystemError,
      ConstraintError,
      IOException
  {
    return ShaderUtilities.readLines(filesystem.openFile(path));
  }

  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  /**
   * The given callable is executed.
   */

  @SuppressWarnings({}) @Test public final void testCallable()
    throws Exception
  {
    JCCEExecutionCallable e = null;

    final Counter c = new Counter();

    try {
      final TestContext tc = this.newTestContext();
      final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
      final ProgramReference p =
        JCCEExecutionCallableContract.makeProgram(tc, gl);
      e = new JCCEExecutionCallable(p);
      e.execSetCallable(c);
      e.execPrepare(gl);
      e.execRun(gl);
    } catch (final Throwable x) {
      throw new AssertionError(x);
    }

    Assert.assertTrue(c.calls == 1);
  }

  /**
   * Failing to set a callable fails.
   */

  @SuppressWarnings({}) @Test(expected = ConstraintError.class) public final
    void
    testCallableUnset()
      throws ConstraintError,
        Exception
  {
    JCCEExecutionCallable e = null;
    JCGLInterfaceCommon gl = null;

    try {
      final TestContext tc = this.newTestContext();
      gl = tc.getGLImplementation().getGLCommon();
      final ProgramReference p =
        JCCEExecutionCallableContract.makeProgram(tc, gl);
      e = new JCCEExecutionCallable(p);
      e.execPrepare(gl);
    } catch (final Throwable x) {
      throw new AssertionError(x);
    }

    e.execRun(gl);
  }

  /**
   * Passing a null callable fails.
   */

  @SuppressWarnings({}) @Test(expected = ConstraintError.class) public final
    void
    testNullCallable()
      throws ConstraintError
  {
    JCCEExecutionCallable e = null;

    try {
      final TestContext tc = this.newTestContext();
      final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
      final ProgramReference p =
        JCCEExecutionCallableContract.makeProgram(tc, gl);
      e = new JCCEExecutionCallable(p);

    } catch (final Throwable x) {
      throw new AssertionError(x);
    }

    e.execSetCallable(null);
  }

  /**
   * Constructing an execution with a null program fails.
   */

  @SuppressWarnings({ "unused", "static-method" }) @Test(
    expected = ConstraintError.class) public final void testNullProgram()
    throws ConstraintError
  {
    new JCCEExecutionCallable(null);
  }
}
