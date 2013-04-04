package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Option.Some;
import com.io7m.jcanephora.contracts.common.MetaContract;

public final class JOGL30MetaTest extends MetaContract
{
  @Override public boolean isGLSupported()
  {
    return JOGLTestContext.isOpenGL3Supported();
  }

  @Override public @Nonnull TestContext newTestContext()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    return JOGLTestContext.makeContextWithOpenGL3_X();
  }

  @Override public GLMeta getGLMeta(
    final TestContext tc)
  {
    final Some<GLInterfaceGL3> some =
      (Some<GLInterfaceGL3>) tc.getGLImplementation().getGL3();
    return some.value;
  }
}
