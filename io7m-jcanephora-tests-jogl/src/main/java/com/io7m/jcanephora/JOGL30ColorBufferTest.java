package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts_ES2.ColorBufferContract;
import com.io7m.jlog.Log;

public final class JOGL30ColorBufferTest extends ColorBufferContract
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
