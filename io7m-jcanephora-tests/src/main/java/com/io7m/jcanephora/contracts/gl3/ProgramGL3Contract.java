package com.io7m.jcanephora.contracts.gl3;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLCompileException;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLMeta;
import com.io7m.jcanephora.GLShaders;
import com.io7m.jcanephora.GLUnsupportedException;
import com.io7m.jcanephora.Program;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.contracts.common.ProgramContract;
import com.io7m.jvvfs.FilesystemAPI;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathVirtual;

public abstract class ProgramGL3Contract extends ProgramContract
{
  @Test public final void testProgramCompileFragmentRemovesRequirement()
    throws ConstraintError,
      GLCompileException,
      FilesystemError,
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
    p.addFragmentShader(new PathVirtual(path + "/func.f"));
    p.compile(fs, gs, gm);
    Assert.assertFalse(p.requiresCompilation(fs, gs));
    p.delete(gs);
  }

  @Test public final void testProgramCompileVertexRemovesRequirement()
    throws ConstraintError,
      GLCompileException,
      FilesystemError,
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
    p.addVertexShader(new PathVirtual(path + "/func.v"));
    p.compile(fs, gs, gm);
    Assert.assertFalse(p.requiresCompilation(fs, gs));
    p.delete(gs);
  }
}
