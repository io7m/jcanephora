package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts_embedded.ScissorContract;
import com.io7m.jlog.Log;

public final class JOGL30ScissorTest extends ScissorContract
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
