package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jlog.Log;

public final class JOGL30RasterizationTest extends
  com.io7m.jcanephora.contracts_full.RasterizationContract
{
  @Override public Log getLog()
  {
    return JOGL30TestLog.getLog();
  }

  @Override public boolean isFullGLSupported()
  {
    return JOGL30TestDisplay.isFullGLSupported();
  }

  @Override public GLInterface makeNewGL()
    throws GLException,
      ConstraintError
  {
    return JOGL30TestDisplay.makeFreshGLFull();
  }
}
