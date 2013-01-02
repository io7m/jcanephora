package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts_full.IndexBufferMapContract;
import com.io7m.jvvfs.PathVirtual;

public final class LWJGL30IndexBufferMapTest extends IndexBufferMapContract
{
  @Override public PathVirtual getShaderPath()
    throws ConstraintError
  {
    return LWJGLTestContext.GLSL_110_SHADER_PATH;
  }

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
