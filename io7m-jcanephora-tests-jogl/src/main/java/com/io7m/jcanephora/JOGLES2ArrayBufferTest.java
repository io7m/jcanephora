package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Option.Some;
import com.io7m.jcanephora.contracts.common.ArrayBufferContract;

public final class JOGLES2ArrayBufferTest extends ArrayBufferContract
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

  @Override public @Nonnull GLArrayBuffers getGLArrayBuffers(
    @Nonnull final TestContext context)
  {
    final Some<GLInterfaceGLES2> some =
      (Some<GLInterfaceGLES2>) context.getGLImplementation().getGLES2();
    return some.value;
  }

  @Override public @Nonnull GLShaders getGLPrograms(
    @Nonnull final TestContext context)
  {
    final Some<GLInterfaceGLES2> some =
      (Some<GLInterfaceGLES2>) context.getGLImplementation().getGLES2();
    return some.value;
  }
}
