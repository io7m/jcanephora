package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts_ES2.ScissorContract;
import com.io7m.jlog.Log;

public final class JOGLES2ScissorTest extends ScissorContract
{
  @Override public Log getLog()
  {
    return JOGLTestLog.getLog();
  }

  @Override public boolean isGLSupported()
  {
    return JOGLTestDisplay.isOpenGLES2Supported();
  }

  @Override public GLInterfaceES2 makeNewGL()
    throws GLException,
      ConstraintError
  {
    return JOGLTestDisplay.makeES2WithOpenGLES2();
  }
}
