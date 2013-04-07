package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Option.Some;
import com.io7m.jcanephora.contracts.common.CullContract;

public final class JOGL21CullTest extends CullContract
{
  @Override public GLCull getGLCull(
    final TestContext context)
  {
    final Some<GLInterfaceGL3> some =
      (Some<GLInterfaceGL3>) context.getGLImplementation().getGL3();
    return some.value;
  }

  @Override public boolean isGLSupported()
  {
    return JOGLTestContext.isOpenGL21WithExtensionsSupported();
  }

  @Override public @Nonnull TestContext newTestContext()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    return JOGLTestContext.makeContextWithOpenGL2_1();
  }
}
