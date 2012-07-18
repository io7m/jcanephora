package com.io7m.jcanephora;

import javax.media.opengl.GLContext;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jlog.Log;

public final class JOGL30ContextCache
{
  private static GLContext context = null;

  public static GLInterface getGL()
    throws GLException,
      ConstraintError
  {
    return new GLInterfaceJOGL30(
      JOGL30ContextCache.getContext(),
      JOGL30TestLog.getLog());
  }

  private static GLContext getContext()
  {
    final Log log = JOGL30TestLog.getLog();

    if (JOGL30ContextCache.context == null) {
      log.debug("creating new offscreen display");
      JOGL30ContextCache.context = JOGL30.createOffscreenDisplay(640, 480);
    } else {
      log.debug("reusing existing offscreen display");
    }
    return JOGL30ContextCache.context;
  }

  private JOGL30ContextCache()
  {

  }
}
