package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts_ES2.DepthBuffersContract;

public final class LWJGL30DepthBuffersTest extends DepthBuffersContract
{
  @Override public @Nonnull TestContext getTestContext()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    return LWJGLTestContext.makeContextWithOpenGL3_X();
  }

  @Override public boolean isGLSupported()
  {
    return LWJGLTestContext.isOpenGL3Supported();
  }
}
