package com.io7m.jcanephora.examples;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLOffscreenAutoDrawable;
import javax.media.opengl.GLProfile;

import com.jogamp.newt.opengl.GLWindow;

public final class JOGL30
{
  public static GLWindow createDisplay(
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

  public static GLContext createOffscreenDisplay(
    final int width,
    final int height)
  {
    final GLProfile pro = GLProfile.get(GLProfile.GL2GL3);
    final GLCapabilities cap = new GLCapabilities(pro);
    cap.setFBO(true);

    final GLDrawableFactory f = GLDrawableFactory.getFactory(pro);
    final GLOffscreenAutoDrawable k =
      f.createOffscreenAutoDrawable(null, cap, null, width, height, null);

    return k.createContext(null);
  }

  public static void destroyDisplay(
    final GLContext context)
  {
    context.destroy();
  }

  private JOGL30()
  {

  }
}
