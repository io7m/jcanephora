package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts_ES2.ColorBufferContract;
import com.io7m.jlog.Log;

public final class LWJGLES2ColorBufferTest extends ColorBufferContract
{
  @Override public Log getLog()
  {
    return LWJGLTestLog.getLog();
  }

  @Override public boolean isGLSupported()
  {
    return LWJGLTestDisplay.isOpenGLES2Supported();
  }

  @Override public GLImplementation makeNewGLImplementation()
    throws GLException,
      ConstraintError,
      GLUnsupportedException
  {
    return LWJGLTestDisplay.makeImplementationWithOpenGLES2();
  }
}
