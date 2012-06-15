package com.io7m.jcanephora;

import java.io.IOException;

import javax.media.opengl.GLContext;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;

public class GLMetaJOGL30Test
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

  @Test public void testMetaStrings()
    throws GLException,
      IOException,
      ConstraintError
  {
    final GLInterface gl = GLInterfaceJOGL30Util.getGL(this.context);

    final String vn = gl.metaGetVendor();
    final String vr = gl.metaGetVersion();
    final String r = gl.metaGetRenderer();

    Assert.assertNotNull(vn);
    Assert.assertNotNull(vr);
    Assert.assertNotNull(r);
  }
}
