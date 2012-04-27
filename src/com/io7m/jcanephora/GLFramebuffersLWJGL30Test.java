package com.io7m.jcanephora;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;

public class GLFramebuffersLWJGL30Test
{
  @Before public void setUp()
    throws Exception
  {
    LWJGL30.createDisplay("GLFramebuffersLWJGL30", 1, 1);
  }

  @After public void tearDown()
    throws Exception
  {
    LWJGL30.destroyDisplay();
  }

  @Test(expected = ConstraintError.class) public void testDepthEnableNone()
    throws GLException,
      ConstraintError
  {
    GLInterface g = null;
    Framebuffer fb = null;

    try {
      g = GLInterfaceLWJGL30Util.getGL();
      fb = g.allocateFramebuffer();
      final Texture2DRGBAStatic cb =
        g.allocateTextureRGBAStatic(
          "framebuffer",
          128,
          128,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          TextureFilter.TEXTURE_FILTER_NEAREST);

      g
        .attachFramebufferStorage(
          fb,
          new FramebufferAttachment[] { new FramebufferAttachment.FramebufferColorAttachment(
            cb,
            0) });
      g.bindFramebuffer(fb);
    } catch (final IOException e) {
      Assert.fail(e.getMessage());
    } catch (final ConstraintError e) {
      Assert.fail(e.getMessage());
    } catch (final GLException e) {
      Assert.fail(e.getMessage());
    }

    assert g != null;
    g.enableDepthTest(DepthFunction.DEPTH_LESS_THAN_OR_EQUAL);
  }
}
