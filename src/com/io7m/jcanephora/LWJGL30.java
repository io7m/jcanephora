package com.io7m.jcanephora;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public final class LWJGL30
{
  public static void createDisplay(
    final String name,
    final int width,
    final int height)
  {
    try {
      /*
       * XXX: As a temporary measure, do not specifically ask for an OpenGL
       * 3.0 context until a reliable means of obtaining one can be found.
       * Mesa 8.0.1 claims to support 3.0 and then fails to create a context,
       * whereas previous versions claimed only to support 2.1 but happily
       * created working 3.0 contexts anyway.
       */

      Display.setDisplayMode(new DisplayMode(width, height));
      Display.setTitle(name);
      Display.create();

      // Display.setDisplayMode(new DisplayMode(width, height));
      // Display.setTitle(name);
      // final PixelFormat format = new PixelFormat();
      // final ContextAttribs attributes =
      // new ContextAttribs(3, 0).withForwardCompatible(true);
      // Display.create(format, attributes);
    } catch (final LWJGLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  public static void destroyDisplay()
  {
    Display.destroy();
  }

  private LWJGL30()
  {
    // Empty.
  }
}
