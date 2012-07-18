package com.io7m.jcanephora;

import org.lwjgl.opengl.Pbuffer;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jlog.Log;

public final class LWJGL30ContextCache
{
  private static Pbuffer buffer;

  public static GLInterface getGL()
    throws GLException,
      ConstraintError
  {
    LWJGL30ContextCache.openContext();
    return new GLInterfaceLWJGL30(LWJGL30TestLog.getLog());
  }

  private static Pbuffer openContext()
  {
    final Log log = LWJGL30TestLog.getLog();

    if (LWJGL30ContextCache.buffer == null) {
      log.debug("creating new offscreen display");
      LWJGL30ContextCache.buffer = LWJGL30.createOffscreenDisplay(640, 480);
    } else {
      log.debug("reusing existing offscreen display");
    }
    return LWJGL30ContextCache.buffer;
  }
}
