package com.io7m.jcanephora;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.Pbuffer;
import org.lwjgl.opengl.PixelFormat;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;

public final class LWJGLTestDisplay
{
  private static Pbuffer buffer = null;

  private static final class Profile
  {
    Profile()
    {

    }

    int     version_major;
    int     version_minor;
    boolean version_es;
    int     width;
    int     height;
  }

  private static final Profile PROFILE_OPENGL_3_0;
  private static final Profile PROFILE_OPENGL_ES_2_0;
  private static final Profile PROFILE_OPENGL_3_1;

  static {
    PROFILE_OPENGL_3_0 = new Profile();
    LWJGLTestDisplay.PROFILE_OPENGL_3_0.version_major = 3;
    LWJGLTestDisplay.PROFILE_OPENGL_3_0.version_minor = 0;
    LWJGLTestDisplay.PROFILE_OPENGL_3_0.version_es = false;
    LWJGLTestDisplay.PROFILE_OPENGL_3_0.width = 640;
    LWJGLTestDisplay.PROFILE_OPENGL_3_0.height = 480;

    PROFILE_OPENGL_3_1 = new Profile();
    LWJGLTestDisplay.PROFILE_OPENGL_3_1.version_major = 3;
    LWJGLTestDisplay.PROFILE_OPENGL_3_1.version_minor = 1;
    LWJGLTestDisplay.PROFILE_OPENGL_3_1.version_es = false;
    LWJGLTestDisplay.PROFILE_OPENGL_3_1.width = 640;
    LWJGLTestDisplay.PROFILE_OPENGL_3_1.height = 480;

    PROFILE_OPENGL_ES_2_0 = new Profile();
    LWJGLTestDisplay.PROFILE_OPENGL_ES_2_0.version_major = 2;
    LWJGLTestDisplay.PROFILE_OPENGL_ES_2_0.version_minor = 0;
    LWJGLTestDisplay.PROFILE_OPENGL_ES_2_0.version_es = true;
    LWJGLTestDisplay.PROFILE_OPENGL_ES_2_0.width = 640;
    LWJGLTestDisplay.PROFILE_OPENGL_ES_2_0.height = 480;
  }

  public static boolean isOpenGL3Supported()
  {
    // XXX: Surely not!
    return true;
  }

  public static boolean isOpenGLES2Supported()
  {
    /**
     * TODO: OpenGL ES2 support seems to be completely broken as of LWJGL
     * 2.8.4 - most functions erroneously raise a "function not supported"
     * error.
     */

    return false;
  }

  public static GLImplementation makeImplementationWithOpenGL3()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    LWJGLTestDisplay.openContext(LWJGLTestDisplay.PROFILE_OPENGL_3_0);
    return new GLImplementationLWJGL(LWJGLTestLog.getLog());
  }

  public static GLImplementation makeImplementationWithOpenGLES2()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    LWJGLTestDisplay.openContext(LWJGLTestDisplay.PROFILE_OPENGL_ES_2_0);
    return new GLImplementationLWJGL(LWJGLTestLog.getLog());
  }

  private static Pbuffer openContext(
    final Profile want)
  {
    if (LWJGLTestDisplay.buffer != null) {
      LWJGLTestDisplay.buffer.destroy();
    }

    LWJGLTestDisplay.buffer = LWJGLTestDisplay.createOffscreenDisplay(want);
    return LWJGLTestDisplay.buffer;
  }

  static Pbuffer createOffscreenDisplay(
    final Profile want)
  {
    try {
      final PixelFormat pixel_format = new PixelFormat(8, 24, 8);
      final ContextAttribs attribs =
        new ContextAttribs(want.version_major, want.version_minor);

      ContextAttribs attribs_w;
      if (want.version_es) {
        attribs_w = attribs.withProfileES(want.version_es);
      } else {
        attribs_w = attribs;
      }

      final Pbuffer pbuffer =
        new Pbuffer(
          want.width,
          want.height,
          pixel_format,
          null,
          null,
          attribs_w);
      pbuffer.makeCurrent();
      return pbuffer;
    } catch (final LWJGLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    throw new UnreachableCodeException();
  }

  static void destroyDisplay()
  {
    Display.destroy();
  }

  static void destroyOffscreenDisplay(
    final Pbuffer disp)
  {
    disp.destroy();
  }

  private LWJGLTestDisplay()
  {
    throw new UnreachableCodeException();
  }
}
