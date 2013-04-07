package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts.gl3.ProgramGL3Contract;

public final class JOGL21ProgramGL3Test extends ProgramGL3Contract
{
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
