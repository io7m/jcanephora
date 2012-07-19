package com.io7m.jcanephora;

import javax.media.opengl.GLContext;

import com.io7m.jaux.Constraints.ConstraintError;

public final class JOGL30TestDisplay
{
  private static GLContext context = null;

  private static GLContext getContext()
  {
    if (JOGL30TestDisplay.context != null) {
      JOGL30TestDisplay.context.destroy();

      /**
       * XXX: Tell the JVM to attempt garbage collection. This seems to
       * partially mitigate an X11-specific bug where new contexts cannot be
       * created ("Maximum number of clients reached").
       */

      System.gc();
    }

    JOGL30TestDisplay.context = JOGL30.createOffscreenDisplay(640, 480);
    return JOGL30TestDisplay.context;
  }

  public static GLInterface makeFreshGL()
    throws GLException,
      ConstraintError
  {
    return new GLInterfaceJOGL30(
      JOGL30TestDisplay.getContext(),
      JOGL30TestLog.getLog());
  }

  private JOGL30TestDisplay()
  {

  }
}
