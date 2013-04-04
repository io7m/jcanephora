package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Option.Some;
import com.io7m.jcanephora.contracts.common.DrawContract;

public final class LWJGL30DrawTest extends DrawContract
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

  @Override public @Nonnull GLDraw getGLDraw(
    final TestContext context)
  {
    final Some<GLInterfaceGL3> some =
      (Some<GLInterfaceGL3>) context.getGLImplementation().getGL3();
    return some.value;
  }

  @Override public @Nonnull GLIndexBuffers getGLIndexBuffers(
    final TestContext context)
  {
    final Some<GLInterfaceGL3> some =
      (Some<GLInterfaceGL3>) context.getGLImplementation().getGL3();
    return some.value;
  }
}
