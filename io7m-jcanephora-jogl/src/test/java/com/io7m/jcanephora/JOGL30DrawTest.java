package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts_embedded.DrawContract;
import com.io7m.jlog.Log;

public final class JOGL30DrawTest extends DrawContract
{
  @Override public Log getLog()
  {
    return JOGL30TestLog.getLog();
  }

  @Override public GLInterfaceEmbedded makeNewGL()
    throws GLException,
      ConstraintError
  {
    return JOGL30TestDisplay.makeFreshGL();
  }
}
