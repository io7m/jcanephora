package com.io7m.jcanephora;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;

public class GLMetaLWJGL30Test
{
  @Before public void setUp()
    throws Exception
  {
    LWJGL30.createDisplay("Meta", 1, 1);
  }

  @After public void tearDown()
    throws Exception
  {
    LWJGL30.destroyDisplay();
  }

  @Test public void testMetaStrings()
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
