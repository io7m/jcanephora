package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts.RenderbuffersContract;
import com.io7m.jlog.Log;

public final class LWJGL30RenderbuffersTest extends RenderbuffersContract
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
