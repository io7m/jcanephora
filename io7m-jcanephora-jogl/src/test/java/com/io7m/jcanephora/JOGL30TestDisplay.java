package com.io7m.jcanephora;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLPbuffer;
import javax.media.opengl.GLProfile;

import com.io7m.jaux.Constraints.ConstraintError;

public final class JOGL30TestDisplay
{
  private static GLPbuffer createOffscreenDisplay(
    final int width,
    final int height)
  {
    final GLProfile pro = GLProfile.get(GLProfile.GL2GL3);
    final GLCapabilities cap = new GLCapabilities(pro);

    final GLDrawableFactory f = GLDrawableFactory.getFactory(pro);
    final GLPbuffer pb =
      f.createGLPbuffer(null, cap, null, width, height, null);

    return pb;
  }

  private static GLContext context;
  private static GLPbuffer buffer;

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
