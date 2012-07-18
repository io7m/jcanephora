package com.io7m.jcanephora;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.Pbuffer;

import com.io7m.jaux.Constraints.ConstraintError;

public final class LWJGL30TestDisplay
{
  private static Pbuffer buffer;

  public static GLInterface getGL()
    throws GLException,
      ConstraintError
  {
    LWJGL30TestDisplay.openContext();
    return new GLInterfaceLWJGL30(LWJGL30TestLog.getLog());
  }

  private static Pbuffer openContext()
  {
    if (LWJGL30TestDisplay.buffer != null) {
      Display.destroy();
    }

    LWJGL30TestDisplay.buffer = LWJGL30.createOffscreenDisplay(640, 480);
    return LWJGL30TestDisplay.buffer;
  }
}
