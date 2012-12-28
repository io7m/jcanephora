package com.io7m.jcanephora;

import org.lwjgl.opengl.Pbuffer;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;

public final class LWJGLTestDisplay
{
  private static Pbuffer buffer = null;

  public static boolean isOpenGL3Supported()
  {
    // XXX: Surely not!
    return true;
  }

  public static boolean isOpenGLES2Supported()
  {
    // XXX: Surely not!
    return false;
  }

  public static GLInterfaceES2 makeES2WithOpenGL3()
    throws GLException,
      ConstraintError
  {
    LWJGLTestDisplay.openContext();
    return new GLInterface_LWJGL30(LWJGLTestLog.getLog());
  }

  public static GLInterfaceES2 makeES2WithOpenGLES2()
    throws GLException,
      ConstraintError
  {
    LWJGLTestDisplay.openContext();
    return new GLInterfaceES2_LWJGL_ES2(LWJGLTestLog.getLog());
  }

  public static GLInterface makeFullWithOpenGL3()
    throws GLException,
      ConstraintError
  {
    LWJGLTestDisplay.openContext();
    return new GLInterface_LWJGL30(LWJGLTestLog.getLog());
  }

  private static Pbuffer openContext()
  {
    if (LWJGLTestDisplay.buffer != null) {
      LWJGLTestDisplay.buffer.destroy();
    }

    LWJGLTestDisplay.buffer = LWJGL30.createOffscreenDisplay(640, 480);
    return LWJGLTestDisplay.buffer;
  }

  private LWJGLTestDisplay()
  {
    throw new UnreachableCodeException();
  }
}
