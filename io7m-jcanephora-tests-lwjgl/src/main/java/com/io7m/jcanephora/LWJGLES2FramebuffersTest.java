package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts_ES2.FramebuffersContract;

public final class LWJGLES2FramebuffersTest extends FramebuffersContract
{
  @Override public @Nonnull TestContext getTestContext()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    return LWJGLTestContext.makeContextWithOpenGL_ES2();
  }

  @Override public boolean isGLSupported()
  {
    return LWJGLTestContext.isOpenGLES2Supported();
  }
}
