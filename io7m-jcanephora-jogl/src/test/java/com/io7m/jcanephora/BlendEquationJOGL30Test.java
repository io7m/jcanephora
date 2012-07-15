package com.io7m.jcanephora;

import javax.media.opengl.GLContext;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BlendEquationJOGL30Test
{
  private GLContext context;

  @Before public void setUp()
    throws Exception
  {
    this.context = JOGL30.createOffscreenDisplay(640, 480);
  }

  @After public void tearDown()
    throws Exception
  {
    JOGL30.destroyDisplay(this.context);
  }

  @SuppressWarnings("static-method") @Test public void testBijection()
  {
    for (final BlendEquation f : BlendEquation.values()) {
      Assert.assertEquals(GLInterfaceJOGL30
        .blendEquationFromGL(GLInterfaceJOGL30.blendEquationToGL(f)), f);
    }
  }

  @SuppressWarnings("static-method") @Test(expected = AssertionError.class) public void testNonsense()
  {
    GLInterfaceJOGL30.blendEquationFromGL(-1);
  }
}
