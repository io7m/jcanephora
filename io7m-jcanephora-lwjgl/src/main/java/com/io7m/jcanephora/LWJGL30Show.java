package com.io7m.jcanephora;

import java.io.IOException;
import java.util.Properties;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.PropertyUtils;
import com.io7m.jlog.Log;

public final class LWJGL30Show
{
  public static void main(
    final String args[])
    throws IOException,
      ConstraintError,
      LWJGLException,
      GLException
  {
    Display.setDisplayMode(new DisplayMode(1, 1));
    Display.setTitle("LWJGL30Show");
    final PixelFormat format = new PixelFormat();
    final ContextAttribs attributes =
      new ContextAttribs(3, 0).withForwardCompatible(true);
    Display.create(format, attributes);

    try {
      final Properties properties =
        PropertyUtils.loadFromFile("tests.properties");
      final Log log = new Log(properties, "com.io7m", "example");
      final GLInterface gl = new GLInterfaceLWJGL30(log);

      System.out.println("GL-Vendor:   " + gl.metaGetVendor());
      System.out.println("GL-Version:  " + gl.metaGetVersion());
      System.out.println("GL-Renderer: " + gl.metaGetRenderer());
    } finally {
      Display.destroy();
    }
  }
}
