package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts_ES2.ViewportContract;
import com.io7m.jlog.Log;

public final class JOGL30ViewportTest extends ViewportContract
{
  @Override public Log getLog()
  {
    return JOGL30TestLog.getLog();
  }

  @Override public GLInterfaceES2 makeNewGL()
    throws GLException,
      ConstraintError
  {
    return JOGL30TestDisplay.makeFreshGLEmbedded();
  }
}
