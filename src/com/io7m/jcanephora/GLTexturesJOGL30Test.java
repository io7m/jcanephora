package com.io7m.jcanephora;

import java.io.IOException;

import javax.media.opengl.GLContext;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;

public class GLTexturesJOGL30Test
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

  @Test public void testGetUnits()
    throws IOException,
      ConstraintError,
      GLException
  {
    final GLInterface gl = GLInterfaceJOGL30Util.getGL(this.context);
    final TextureUnit[] u = gl.textureGetUnits();
    Assert.assertTrue(u.length >= 2);
  }
}
