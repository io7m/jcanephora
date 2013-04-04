package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

public final class LWJGL30RasterizationTest extends
  com.io7m.jcanephora.contracts.gl3.RasterizationContract
{
  @Override public boolean isGLSupported()
  {
    return LWJGLTestContext.isOpenGL3Supported();
  }

  @Override public @Nonnull TestContext newTestContext()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    return LWJGLTestContext.makeContextWithOpenGL3_X();
  }
}
