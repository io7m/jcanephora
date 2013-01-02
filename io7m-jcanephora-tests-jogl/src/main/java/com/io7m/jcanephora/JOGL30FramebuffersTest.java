package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts_ES2.FramebuffersContract;
import com.io7m.jlog.Log;

public final class JOGL30FramebuffersTest extends FramebuffersContract
{
  @Override public Log getLog()
  {
    return JOGLTestLog.getLog();
  }

  @Override public boolean isGLSupported()
  {
    return JOGLTestDisplay.isOpenGL3Supported();
  }

  @Override public GLImplementation makeNewGLImplementation()
    throws GLException,
      ConstraintError,
      GLUnsupportedException
  {
    return JOGLTestDisplay.makeImplementationWithOpenGL3();
  }
}
