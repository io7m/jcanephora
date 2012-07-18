package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts.PixelUnpackBufferMapContract;
import com.io7m.jlog.Log;

public final class LWJGL30PixelUnpackBufferMapTest extends
  PixelUnpackBufferMapContract
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
