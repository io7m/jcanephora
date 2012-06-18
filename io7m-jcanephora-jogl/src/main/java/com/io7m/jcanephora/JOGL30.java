package com.io7m.jcanephora;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLPbuffer;
import javax.media.opengl.GLProfile;

import com.jogamp.newt.opengl.GLWindow;

final class JOGL30
{
  static GLWindow createDisplay(
    final String name,
    final int width,
    final int height)
  {
    final GLProfile pro = GLProfile.get(GLProfile.GL2GL3);
    final GLCapabilities cap = new GLCapabilities(pro);
    final GLWindow window = GLWindow.create(cap);

    window.setSize(width, height);
    window.setTitle(name);
    window.setVisible(true);

    return window;
  }

  static GLContext createOffscreenDisplay(
    final int width,
    final int height)
  {
    final GLProfile pro = GLProfile.get(GLProfile.GL2GL3);
    final GLCapabilities cap = new GLCapabilities(pro);

    final GLDrawableFactory f = GLDrawableFactory.getFactory(pro);
    final GLPbuffer pb =
      f.createGLPbuffer(null, cap, null, width, height, null);

    return pb.createContext(null);
  }

  static void destroyDisplay(
    final GLContext context)
  {
    context.destroy();
  }

  private JOGL30()
  {

  }
}
