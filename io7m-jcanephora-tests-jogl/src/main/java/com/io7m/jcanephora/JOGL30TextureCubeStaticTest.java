package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts_full.TextureCubeStaticContract;
import com.io7m.jvvfs.PathVirtual;

public final class JOGL30TextureCubeStaticTest extends
  TextureCubeStaticContract
{
  @Override public PathVirtual getShaderPath()
    throws ConstraintError
  {
    return JOGLTestContext.GLSL_110_SHADER_PATH;
  }

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
