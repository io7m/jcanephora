package com.io7m.jcanephora;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLOffscreenAutoDrawable;
import javax.media.opengl.GLProfile;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jlog.Log;

public final class JOGLTestDisplay
{
  private static GLContext               context;
  private static GLOffscreenAutoDrawable buffer;
  private static GLProfile               PROFILE_OPENGL_3;
  private static GLProfile               PROFILE_OPENGL_ES2;

  static {
    JOGLTestDisplay.PROFILE_OPENGL_3 = GLProfile.get(GLProfile.GL2);
    JOGLTestDisplay.PROFILE_OPENGL_ES2 = GLProfile.get(GLProfile.GLES2);
  }

  private static GLOffscreenAutoDrawable createOffscreenDrawable(
    final GLProfile profile,
    final int width,
    final int height)
  {
    final GLCapabilities cap = new GLCapabilities(profile);
    cap.setFBO(true);

    final GLDrawableFactory f = GLDrawableFactory.getFactory(profile);
    final GLOffscreenAutoDrawable k =
      f.createOffscreenAutoDrawable(null, cap, null, width, height, null);

    return k;
  }

  private static GLContext getContext(
    final GLProfile profile)
  {
    if (JOGLTestDisplay.buffer != null) {
      JOGLTestDisplay.context.release();
      JOGLTestDisplay.context.destroy();
      JOGLTestDisplay.buffer.destroy();
    }

    JOGLTestDisplay.buffer =
      JOGLTestDisplay.createOffscreenDrawable(profile, 640, 480);
    JOGLTestDisplay.context = JOGLTestDisplay.buffer.createContext(null);

    final int r = JOGLTestDisplay.context.makeCurrent();
    if (r == GLContext.CONTEXT_NOT_CURRENT) {
      throw new AssertionError("Could not make context current");
    }

    System.err.println("Context: " + JOGLTestDisplay.context);
    return JOGLTestDisplay.context;
  }

  public static boolean isOpenGL3Supported()
  {
    final GLContext ctx =
      JOGLTestDisplay.getContext(JOGLTestDisplay.PROFILE_OPENGL_3);
    return ctx.isGL3();
  }

  public static boolean isOpenGLES2Supported()
  {
    final GLContext ctx =
      JOGLTestDisplay.getContext(JOGLTestDisplay.PROFILE_OPENGL_ES2);
    return ctx.isGLES2();
  }

  public static GLInterfaceES2 makeES2WithOpenGL3()
    throws GLException,
      ConstraintError
  {
    final GLContext ctx =
      JOGLTestDisplay.getContext(JOGLTestDisplay.PROFILE_OPENGL_3);
    final Log log = JOGLTestLog.getLog();
    return new GLInterfaceES2_JOGL30(ctx, log);
  }

  public static GLInterfaceES2 makeES2WithOpenGLES2()
    throws GLException,
      ConstraintError
  {
    final GLContext ctx =
      JOGLTestDisplay.getContext(JOGLTestDisplay.PROFILE_OPENGL_ES2);
    final Log log = JOGLTestLog.getLog();
    return new GLInterfaceES2_JOGLES2(ctx, log);
  }

  public static GLInterface makeFullWithOpenGL3()
    throws GLException,
      ConstraintError
  {
    final GLContext ctx =
      JOGLTestDisplay.getContext(JOGLTestDisplay.PROFILE_OPENGL_3);
    final Log log = JOGLTestLog.getLog();
    return new GLInterface_JOGL30(ctx, log);
  }

  private JOGLTestDisplay()
  {

  }
}
