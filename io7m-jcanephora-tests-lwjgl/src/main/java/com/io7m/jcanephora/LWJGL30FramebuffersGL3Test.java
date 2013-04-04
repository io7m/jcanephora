package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Option.Some;
import com.io7m.jcanephora.contracts.gl3.FramebuffersGL3Contract;

public final class LWJGL30FramebuffersGL3Test extends FramebuffersGL3Contract
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

  @Override public GLInterfaceGL3 getGL3(
    final TestContext context)
  {
    final Some<GLInterfaceGL3> some =
      (Some<GLInterfaceGL3>) context.getGLImplementation().getGL3();
    return some.value;
  }
}
