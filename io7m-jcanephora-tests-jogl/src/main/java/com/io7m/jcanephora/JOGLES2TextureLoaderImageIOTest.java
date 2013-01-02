package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

public final class JOGLES2TextureLoaderImageIOTest extends
  TextureLoaderImageIOTest
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
