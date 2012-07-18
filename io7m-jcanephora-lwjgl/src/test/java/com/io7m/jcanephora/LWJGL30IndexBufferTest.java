package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts.IndexBufferContract;
import com.io7m.jlog.Log;

public final class LWJGL30IndexBufferTest extends IndexBufferContract
{
  @Override public GLInterface getGL()
    throws GLException,
      ConstraintError
  {
    return LWJGL30TestDisplay.getGL();
  }

  @Override public Log getLog()
  {
    return LWJGL30TestLog.getLog();
  }
}
