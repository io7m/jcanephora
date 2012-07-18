package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts.ViewportContract;
import com.io7m.jlog.Log;

public final class JOGL30ViewportTest extends ViewportContract
{
  @Override public GLInterface getGL()
    throws GLException,
      ConstraintError
  {
    return JOGL30TestDisplay.makeFreshGL();
  }

  @Override public Log getLog()
  {
    return JOGL30TestLog.getLog();
  }
}
