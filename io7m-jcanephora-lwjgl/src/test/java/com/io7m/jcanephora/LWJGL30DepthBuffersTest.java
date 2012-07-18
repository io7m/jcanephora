package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts.DepthBuffersContract;
import com.io7m.jlog.Log;

public final class LWJGL30DepthBuffersTest extends DepthBuffersContract
{
  @Override public GLInterface getGL()
    throws GLException,
      ConstraintError
  {
    return LWJGL30ContextCache.getGL();
  }

  @Override public Log getLog()
  {
    return LWJGL30TestLog.getLog();
  }
}
