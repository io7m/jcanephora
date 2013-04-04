package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Option.Some;
import com.io7m.jcanephora.contracts.common.ScissorContract;

public final class LWJGL30ScissorTest extends ScissorContract
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

  @Override public GLScissor getGLScissor(
    final TestContext tc)
  {
    final Some<GLInterfaceGL3> some =
      (Some<GLInterfaceGL3>) tc.getGLImplementation().getGL3();
    return some.value;
  }
}
