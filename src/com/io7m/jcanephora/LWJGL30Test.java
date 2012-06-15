package com.io7m.jcanephora;

import org.junit.Test;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.Pbuffer;

public class LWJGL30Test
{
  @Test public void testNormalDisplay()
  {
    LWJGL30.createDisplay("LWJGL30", 640, 480);
    Display.destroy();
  }

  @Test public void testOffscreenDisplay()
  {
    final Pbuffer buffer = LWJGL30.createOffscreenDisplay(640, 480);
    LWJGL30.destroyOffscreenDisplay(buffer);
  }
}
