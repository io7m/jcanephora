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

package com.io7m.jcanephora.contracts.gl2;

import javax.annotation.Nonnull;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLRenderbuffersGL2;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.Renderbuffer;
import com.io7m.jcanephora.RenderbufferType;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.contracts.RenderbufferContract;

public abstract class RenderbufferGL2Contract extends
  RenderbufferContract<JCGLRenderbuffersGL2>
{
  @Override public Renderbuffer<?> allocateAnything(
    final @Nonnull JCGLRenderbuffersGL2 gl)
    throws JCGLException,
      ConstraintError
  {
    return gl.renderbufferAllocateRGB888(128, 128);
  }

  /**
   * Allocating all of the renderbuffer types works.
   * 
   * @throws ConstraintError
   * @throws JCGLException
   */

  @Test public void testRenderbufferAllocate()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLRenderbuffersGL2 gr = this.getGLRenderbuffers(tc);

    final int width = 128;
    final int height = 128;

    for (final RenderbufferType t : RenderbufferType.values()) {
      Renderbuffer<?> rb = null;

      switch (t) {
        case RENDERBUFFER_DEPTH_24_STENCIL_8:
          rb = gr.renderbufferAllocateDepth24Stencil8(width, height);
          break;
        case RENDERBUFFER_COLOR_RGBA_8888:
          rb = gr.renderbufferAllocateRGBA8888(width, height);
          break;
        case RENDERBUFFER_COLOR_RGB_888:
          rb = gr.renderbufferAllocateRGB888(width, height);
          break;
        case RENDERBUFFER_COLOR_RGBA_4444:
        case RENDERBUFFER_COLOR_RGBA_5551:
        case RENDERBUFFER_COLOR_RGB_565:
        case RENDERBUFFER_DEPTH_16:
        case RENDERBUFFER_STENCIL_8:
          break;
      }

      if (rb != null) {
        Assert.assertFalse(rb.resourceIsDeleted());
        gr.renderbufferDelete(rb);
        Assert.assertTrue(rb.resourceIsDeleted());
      }
    }
  }
}