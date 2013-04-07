/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

public final class RenderbufferTypeTest
{
  @SuppressWarnings("static-method") @Test public void testColorRenderable()
  {
    for (final RenderbufferType t : RenderbufferType.values()) {
      System.err.println("ColorRenderable: " + t);
      if ((t == RenderbufferType.RENDERBUFFER_COLOR_RGB_565)
        || (t == RenderbufferType.RENDERBUFFER_COLOR_RGBA_4444)
        || (t == RenderbufferType.RENDERBUFFER_COLOR_RGBA_5551)
        || (t == RenderbufferType.RENDERBUFFER_COLOR_RGBA_8888)
        || (t == RenderbufferType.RENDERBUFFER_COLOR_RGB_888)) {
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
