package com.io7m.jcanephora;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lwjgl.opengl.Pbuffer;

import com.io7m.jaux.Constraints.ConstraintError;

public class GLMetaLWJGL30Test
{
  private Pbuffer buffer;

  @Before public void setUp()
    throws Exception
  {
    this.buffer = LWJGL30.createOffscreenDisplay(640, 480);
  }

  @After public void tearDown()
    throws Exception
  {
    LWJGL30.destroyOffscreenDisplay(this.buffer);
  }

  @SuppressWarnings("static-method") @Test public void testMetaStrings()
    throws GLException,
      IOException,
      ConstraintError
  {
    final GLInterface gl = GLInterfaceLWJGL30Util.getGL();

    final String vn = gl.metaGetVendor();
    final String vr = gl.metaGetVersion();
    final String r = gl.metaGetRenderer();

    Assert.assertNotNull(vn);
    Assert.assertNotNull(vr);
    Assert.assertNotNull(r);
  }
}
