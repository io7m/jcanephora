package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts_full.ArrayBufferWritableMapContract;
import com.io7m.jlog.Log;

public final class LWJGL30ArrayBufferWritableMap extends
  ArrayBufferWritableMapContract
{
  @Override public Log getLog()
  {
    return LWJGLTestLog.getLog();
  }

  @Override public boolean isGLSupported()
  {
    return LWJGLTestDisplay.isOpenGL3Supported();
  }

  @Override public GLInterface makeNewGL()
    throws GLException,
      ConstraintError
  {
    return LWJGLTestDisplay.makeFullWithOpenGL3();
  }
}
