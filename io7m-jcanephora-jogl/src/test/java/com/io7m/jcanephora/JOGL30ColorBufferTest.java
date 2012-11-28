package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts_embedded.ColorBufferContract;
import com.io7m.jlog.Log;

public final class JOGL30ColorBufferTest extends ColorBufferContract
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
