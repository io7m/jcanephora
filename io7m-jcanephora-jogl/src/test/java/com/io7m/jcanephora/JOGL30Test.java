package com.io7m.jcanephora;

import javax.media.opengl.GL;
import javax.media.opengl.GLContext;

import junit.framework.Assert;

import org.junit.Test;

import com.jogamp.newt.opengl.GLWindow;

public final class JOGL30Test
{
  @SuppressWarnings("static-method") @Test public void testNormalDisplay()
  {
    final GLWindow k = JOGL30.createDisplay("JOGL30", 640, 480);
    JOGL30.destroyDisplay(k.getContext());
  }

  @SuppressWarnings("static-method") @Test public void testOffscreenDisplay()
  {
    final GLContext k = JOGL30.createOffscreenDisplay(640, 480);
    Assert.assertNotNull(k);

    final int r0 = k.makeCurrent();
    Assert.assertEquals(GLContext.CONTEXT_CURRENT_NEW, r0);

    final GL g = k.getGL();
    Assert.assertNotNull(g);

    g.glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
    g.glClear(GL.GL_COLOR_BUFFER_BIT);

    JOGL30.destroyDisplay(k);
  }
}
