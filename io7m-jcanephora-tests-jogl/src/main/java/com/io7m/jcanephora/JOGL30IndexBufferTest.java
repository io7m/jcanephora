package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Option.Some;
import com.io7m.jcanephora.contracts.common.IndexBufferContract;

public final class JOGL30IndexBufferTest extends IndexBufferContract
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

  @Override public GLIndexBuffers getGLIndexBuffers(
    final TestContext tc)
  {
    final Some<GLInterfaceGL3> some =
      (Some<GLInterfaceGL3>) tc.getGLImplementation().getGL3();
    return some.value;
  }

  @Override public GLArrayBuffers getGLArrayBuffers(
    final TestContext tc)
  {
    final Some<GLInterfaceGL3> some =
      (Some<GLInterfaceGL3>) tc.getGLImplementation().getGL3();
    return some.value;
  }
}
