package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Option.Some;
import com.io7m.jcanephora.contracts.common.ArrayBufferContract;

public final class JOGL30ArrayBufferTest extends ArrayBufferContract
{
  @Override public @Nonnull GLArrayBuffers getGLArrayBuffers(
    @Nonnull final TestContext context)
  {
    final Some<GLInterfaceGL3> some =
      (Some<GLInterfaceGL3>) context.getGLImplementation().getGL3();
    return some.value;
  }

  @Override public @Nonnull GLShaders getGLPrograms(
    @Nonnull final TestContext context)
  {
    final Some<GLInterfaceGL3> some =
      (Some<GLInterfaceGL3>) context.getGLImplementation().getGL3();
    return some.value;
  }

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
}
