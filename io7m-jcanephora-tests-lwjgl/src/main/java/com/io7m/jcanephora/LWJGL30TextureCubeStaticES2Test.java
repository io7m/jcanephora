package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts_ES2.TextureCubeStaticES2Contract;
import com.io7m.jlog.Log;

public final class LWJGL30TextureCubeStaticES2Test extends
  TextureCubeStaticES2Contract
{
  @Override public Log getLog()
  {
    return LWJGLTestLog.getLog();
  }

  @Override public boolean isGLSupported()
  {
    return LWJGLTestDisplay.isOpenGL3Supported();
  }

  @Override public GLInterfaceES2 makeNewGL()
    throws GLException,
      ConstraintError
  {
    return LWJGLTestDisplay.makeES2WithOpenGL3();
  }
}
