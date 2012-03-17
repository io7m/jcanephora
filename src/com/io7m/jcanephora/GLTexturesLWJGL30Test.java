package com.io7m.jcanephora;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;

public class GLTexturesLWJGL30Test
{
  @Before public void setUp()
    throws Exception
  {
    LWJGL30.createDisplay("Textures", 1, 1);
  }

  @After public void tearDown()
    throws Exception
  {
    LWJGL30.destroyDisplay();
  }

  @Test public void testGetUnits()
    throws IOException,
      ConstraintError,
      GLException
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();
    final TextureUnit[] u = gl.getTextureUnits();
    Assert.assertTrue(u.length > 0);
  }
}
