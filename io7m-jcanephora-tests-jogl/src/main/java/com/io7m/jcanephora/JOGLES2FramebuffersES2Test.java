package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Option.Some;
import com.io7m.jcanephora.contracts.gles2.FramebuffersES2Contract;

public final class JOGLES2FramebuffersES2Test extends FramebuffersES2Contract
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

  @Override public GLFramebuffersCommon getGLFramebuffers(
    final TestContext tc)
  {
    final Some<GLInterfaceGLES2> some =
      (Some<GLInterfaceGLES2>) tc.getGLImplementation().getGLES2();
    return some.value;
  }

  @Override public GLRenderbuffersCommon getGLRenderbuffers(
    final TestContext tc)
  {
    final Some<GLInterfaceGLES2> some =
      (Some<GLInterfaceGLES2>) tc.getGLImplementation().getGLES2();
    return some.value;
  }

  @Override public GLTexturesCubeStaticCommon getGLTexturesCube(
    final TestContext tc)
  {
    final Some<GLInterfaceGLES2> some =
      (Some<GLInterfaceGLES2>) tc.getGLImplementation().getGLES2();
    return some.value;
  }

  @Override public GLTextures2DStaticCommon getGLTextures2D(
    final TestContext tc)
  {
    final Some<GLInterfaceGLES2> some =
      (Some<GLInterfaceGLES2>) tc.getGLImplementation().getGLES2();
    return some.value;
  }
}
