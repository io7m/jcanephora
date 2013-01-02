package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts_ES2.ViewportContract;

public final class JOGLES2ViewportTest extends ViewportContract
{
  @Override public @Nonnull TestContext getTestContext()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    return JOGLTestContext.makeContextWithOpenGL_ES2();
  }

  @Override public boolean isGLSupported()
  {
    return JOGLTestContext.isOpenGLES2Supported();
  }
}
