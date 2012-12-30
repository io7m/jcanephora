package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

public final class RenderbufferTypeTest
{
  @SuppressWarnings("static-method") @Test public void testColorRenderable()
  {
    for (final RenderbufferType t : RenderbufferType.values()) {
      if ((t == RenderbufferType.RENDERBUFFER_COLOR_RGB_565)
        || (t == RenderbufferType.RENDERBUFFER_COLOR_RGBA_4444)
        || (t == RenderbufferType.RENDERBUFFER_COLOR_RGBA_5551)) {
        Assert.assertTrue(t.isColorRenderable());
      } else {
        Assert.assertFalse(t.isColorRenderable());
      }
    }
  }

  @SuppressWarnings("static-method") @Test public void testDepthRenderable()
  {
    for (final RenderbufferType t : RenderbufferType.values()) {
      if ((t == RenderbufferType.RENDERBUFFER_DEPTH_24_STENCIL_8)
        || (t == RenderbufferType.RENDERBUFFER_DEPTH_16)) {
        Assert.assertTrue(t.isDepthRenderable());
      } else {
        Assert.assertFalse(t.isDepthRenderable());
      }
    }
  }

  @SuppressWarnings("static-method") @Test public
    void
    testStencilRenderable()
  {
    for (final RenderbufferType t : RenderbufferType.values()) {
      if ((t == RenderbufferType.RENDERBUFFER_DEPTH_24_STENCIL_8)
        || (t == RenderbufferType.RENDERBUFFER_STENCIL_8)) {
        Assert.assertTrue(t.isStencilRenderable());
      } else {
        Assert.assertFalse(t.isStencilRenderable());
      }
    }
  }
}
