package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts.IndexBufferMapContract;
import com.io7m.jlog.Log;

public final class JOGL30IndexBufferMapTest extends IndexBufferMapContract
{
  @Override public GLInterface getGL()
    throws GLException,
      ConstraintError
  {
    return JOGL30ContextCache.getGL();
  }

  @Override public Log getLog()
  {
    return JOGL30TestLog.getLog();
  }
}