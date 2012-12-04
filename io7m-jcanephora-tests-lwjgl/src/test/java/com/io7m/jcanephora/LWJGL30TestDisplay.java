package com.io7m.jcanephora;

import org.lwjgl.opengl.Pbuffer;

import com.io7m.jaux.Constraints.ConstraintError;

public final class LWJGL30TestDisplay
{
  private static Pbuffer buffer = null;

  public static GLInterfaceEmbedded makeFreshGLEmbedded()
    throws GLException,
      ConstraintError
  {
    LWJGL30TestDisplay.openContext();
    return new GLInterfaceEmbedded_LWJGL_ES2(LWJGL30TestLog.getLog());
  }

  public static GLInterface makeFreshGLFull()
    throws GLException,
      ConstraintError
  {
    LWJGL30TestDisplay.openContext();
    return new GLInterface_LWJGL30(LWJGL30TestLog.getLog());
  }

  public static boolean isFullGLSupported()
  {
    // XXX: Surely not!
    return true;
  }

  private static Pbuffer openContext()
  {
    if (LWJGL30TestDisplay.buffer != null) {
      LWJGL30TestDisplay.buffer.destroy();
    }

    LWJGL30TestDisplay.buffer = LWJGL30.createOffscreenDisplay(640, 480);
    return LWJGL30TestDisplay.buffer;
  }
}
