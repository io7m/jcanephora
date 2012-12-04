package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts_full.BlendContract;
import com.io7m.jlog.Log;

public final class JOGL30BlendFullTest extends BlendContract
{
  @Override public Log getLog()
  {
    return JOGL30TestLog.getLog();
  }

  @Override public GLInterface makeNewGL()
    throws GLException,
      ConstraintError
  {
    return JOGL30TestDisplay.makeFreshGLFull();
  }

  @Override public boolean isFullGLSupported()
  {
    return JOGL30TestDisplay.isFullGLSupported();
  }
}
