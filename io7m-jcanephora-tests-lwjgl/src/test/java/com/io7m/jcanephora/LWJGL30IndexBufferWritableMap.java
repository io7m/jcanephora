package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts_full.IndexBufferMapContract;
import com.io7m.jlog.Log;

public final class LWJGL30IndexBufferWritableMap extends
  IndexBufferMapContract
{
  @Override public Log getLog()
  {
    return LWJGL30TestLog.getLog();
  }

  @Override public GLInterface makeNewGL()
    throws GLException,
      ConstraintError
  {
    return LWJGL30TestDisplay.makeFreshGLFull();
  }

  @Override public boolean isFullGLSupported()
  {
    return LWJGL30TestDisplay.isFullGLSupported();
  }
}
