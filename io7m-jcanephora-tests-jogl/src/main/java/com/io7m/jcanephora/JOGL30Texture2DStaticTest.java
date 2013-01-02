package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts_full.Texture2DStaticContract;
import com.io7m.jlog.Log;

public final class JOGL30Texture2DStaticTest extends Texture2DStaticContract
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
      GLUnsupportedException,
      ConstraintError,
      GLUnsupportedException
  {
    return JOGLTestDisplay.makeImplementationWithOpenGL3();
  }
}
