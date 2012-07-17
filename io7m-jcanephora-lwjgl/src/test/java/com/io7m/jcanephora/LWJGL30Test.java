package com.io7m.jcanephora;

import org.junit.Test;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.Pbuffer;

public final class LWJGL30Test
{
  @SuppressWarnings("static-method") @Test public void testNormalDisplay()
  {
    LWJGL30.createDisplay("LWJGL30", 640, 480);
    Display.destroy();
  }

  @SuppressWarnings("static-method") @Test public void testOffscreenDisplay()
  {
    final Pbuffer buffer = LWJGL30.createOffscreenDisplay(640, 480);
    LWJGL30.destroyOffscreenDisplay(buffer);
  }
}
