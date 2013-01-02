package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

public final class JOGL30TextureLoaderImageIOTest extends
  TextureLoaderImageIOTest
{
  @Override public @Nonnull TestContext getTestContext()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    return JOGLTestContext.makeContextWithOpenGL3_X();
  }

  @Override public boolean isGLSupported()
  {
    return JOGLTestContext.isOpenGL3Supported();
  }
}
