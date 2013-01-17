package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts_full.Texture2DStaticContract;

public final class LWJGL30Texture2DStaticTest extends Texture2DStaticContract
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