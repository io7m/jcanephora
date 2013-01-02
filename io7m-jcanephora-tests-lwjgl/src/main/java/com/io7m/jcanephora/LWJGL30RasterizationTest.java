package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jlog.Log;

public final class LWJGL30RasterizationTest extends
  com.io7m.jcanephora.contracts_full.RasterizationContract
{
  @Override public Log getLog()
  {
    return LWJGLTestLog.getLog();
  }

  @Override public boolean isGLSupported()
  {
    return LWJGLTestDisplay.isOpenGL3Supported();
  }

  @Override public GLImplementation makeNewGLImplementation()
    throws GLException,
      GLUnsupportedException,
      ConstraintError,
      GLUnsupportedException
  {
    return LWJGLTestDisplay.makeImplementationWithOpenGL3();
  }
}
