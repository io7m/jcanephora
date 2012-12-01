package com.io7m.jcanephora;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLOffscreenAutoDrawable;
import javax.media.opengl.GLProfile;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Option;
import com.io7m.jlog.Log;

public final class JOGL30TestDisplay
{
  private static GLContext               context;
  private static GLOffscreenAutoDrawable buffer;

  private static GLOffscreenAutoDrawable createOffscreenDisplay(
    final int width,
    final int height)
  {
    final GLProfile pro = GLProfile.get(GLProfile.GL2GL3);
    final GLCapabilities cap = new GLCapabilities(pro);
    cap.setFBO(true);

    final GLDrawableFactory f = GLDrawableFactory.getFactory(pro);
    final GLOffscreenAutoDrawable k =
      f.createOffscreenAutoDrawable(null, cap, null, width, height, null);

    return k;
  }

  private static GLContext getContext()
  {
    if (JOGL30TestDisplay.buffer != null) {
      JOGL30TestDisplay.context.release();
      JOGL30TestDisplay.context.destroy();
      JOGL30TestDisplay.buffer.destroy();
    }

    JOGL30TestDisplay.buffer =
      JOGL30TestDisplay.createOffscreenDisplay(640, 480);
    JOGL30TestDisplay.context = JOGL30TestDisplay.buffer.createContext(null);

    final int r = JOGL30TestDisplay.context.makeCurrent();
    if (r == GLContext.CONTEXT_NOT_CURRENT) {
      throw new AssertionError("Could not make context current");
    }

    return JOGL30TestDisplay.context;
  }

  public static GLInterfaceEmbedded makeFreshGLEmbedded()
    throws GLException,
      ConstraintError
  {
    final GLContext ctx = JOGL30TestDisplay.getContext();
    final Log log = JOGL30TestLog.getLog();
    return new GLInterfaceEmbedded_JOGL_ES2(ctx, log);
  }

  public static Option<GLInterface> makeFreshGLFull()
    throws GLException,
      ConstraintError
  {
    final GLContext ctx = JOGL30TestDisplay.getContext();
    final Log log = JOGL30TestLog.getLog();

    if (ctx.isGL2GL3()) {
      return new Option.Some<GLInterface>(new GLInterface_JOGL30(ctx, log));
    }

    return new Option.None<GLInterface>();
  }

  private JOGL30TestDisplay()
  {

  }
}
