package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Option.Some;
import com.io7m.jcanephora.contracts.common.CullContract;

public final class LWJGLES2CullTest extends CullContract
{
  @Override public boolean isGLSupported()
  {
    return LWJGLTestContext.isOpenGLES2Supported();
  }

  @Override public @Nonnull TestContext newTestContext()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    return LWJGLTestContext.makeContextWithOpenGL_ES2();
  }

  @Override public GLCull getGLCull(
    final TestContext context)
  {
    final Some<GLInterfaceGLES2> some =
      (Some<GLInterfaceGLES2>) context.getGLImplementation().getGLES2();
    return some.value;
  }
}
