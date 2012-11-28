package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts_full.ArrayBufferWritableMapContract;
import com.io7m.jlog.Log;

public final class JOGL30ArrayBufferWritableMapTest extends
  ArrayBufferWritableMapContract
{
  @Override public Log getLog()
  {
    return JOGL30TestLog.getLog();
  }

  @Override public GLInterface makeNewGL()
    throws GLException,
      ConstraintError
  {
    return JOGL30TestDisplay.makeFreshGL();
  }
}
