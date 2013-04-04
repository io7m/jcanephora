package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Option.Some;
import com.io7m.jcanephora.contracts.gles2.ProgramGLES2Contract;

public final class JOGLES2ProgramTest extends ProgramGLES2Contract
{
  @Override public boolean isGLSupported()
  {
    return JOGLTestContext.isOpenGLES2Supported();
  }

  @Override public @Nonnull TestContext newTestContext()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    return JOGLTestContext.makeContextWithOpenGL_ES2();
  }

  @Override public GLShaders getGLShaders(
    final TestContext tc)
  {
    final Some<GLInterfaceGLES2> some =
      (Some<GLInterfaceGLES2>) tc.getGLImplementation().getGLES2();
    return some.value;
  }

  @Override public GLTextureUnits getGLTextureUnits(
    final TestContext tc)
  {
    final Some<GLInterfaceGLES2> some =
      (Some<GLInterfaceGLES2>) tc.getGLImplementation().getGLES2();
    return some.value;
  }

  @Override public GLMeta getGLMeta(
    final TestContext tc)
  {
    final Some<GLInterfaceGLES2> some =
      (Some<GLInterfaceGLES2>) tc.getGLImplementation().getGLES2();
    return some.value;
  }
}
