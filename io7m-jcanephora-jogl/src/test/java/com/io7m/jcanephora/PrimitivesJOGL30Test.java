package com.io7m.jcanephora;

import javax.media.opengl.GLContext;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PrimitivesJOGL30Test
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

  @SuppressWarnings("static-method") @Test public void testModeBijection()
  {
    for (final Primitives p : Primitives.values()) {
      Assert
        .assertEquals(GLInterfaceJOGL30.primitiveFromGL(GLInterfaceJOGL30
          .primitiveToGL(p)), p);
    }
  }

  @SuppressWarnings("static-method") @Test(expected = AssertionError.class) public
    void
    testNonsense()
  {
    GLInterfaceJOGL30.polygonModeFromGL(-1);
  }
}
