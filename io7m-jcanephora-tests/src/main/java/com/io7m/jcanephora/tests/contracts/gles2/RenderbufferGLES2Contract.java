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

package com.io7m.jcanephora.tests.contracts.gles2;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.RenderbufferFormat;
import com.io7m.jcanephora.RenderbufferType;
import com.io7m.jcanephora.api.JCGLRenderbuffersGLES2Type;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.contracts.RenderbufferContract;
import com.io7m.junreachable.UnreachableCodeException;

public abstract class RenderbufferGLES2Contract extends
  RenderbufferContract<JCGLRenderbuffersGLES2Type>
{
  @Override public final RenderbufferType<?> allocateAnything(
    final JCGLRenderbuffersGLES2Type r)
  {
    try {
      return r.renderbufferAllocateRGB565(128, 128);
    } catch (final JCGLExceptionRuntime e) {
      throw new UnreachableCodeException(e);
    }
  }

  /**
   * Allocating all of the renderbuffer types works.
   */

  @Test public void testRenderbufferAllocate()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLRenderbuffersGLES2Type gr = this.getGLRenderbuffers(tc);

    final int width = 128;
    final int height = 128;

    for (final RenderbufferFormat t : RenderbufferFormat.getGLES2Types()) {
      RenderbufferType<?> rb = null;

      switch (t) {
        case RENDERBUFFER_COLOR_RGBA_4444:
          rb = gr.renderbufferAllocateRGBA4444(width, height);
          break;
        case RENDERBUFFER_COLOR_RGBA_5551:
          rb = gr.renderbufferAllocateRGBA5551(width, height);
          break;
        case RENDERBUFFER_COLOR_RGB_565:
          rb = gr.renderbufferAllocateRGB565(width, height);
          break;
        case RENDERBUFFER_DEPTH_16:
          rb = gr.renderbufferAllocateDepth16(width, height);
          break;
        case RENDERBUFFER_STENCIL_8:
          rb = gr.renderbufferAllocateStencil8(width, height);
          break;
        case RENDERBUFFER_DEPTH_24:
        case RENDERBUFFER_DEPTH_24_STENCIL_8:
        case RENDERBUFFER_COLOR_RGBA_8888:
        case RENDERBUFFER_COLOR_RGB_888:
          throw new UnreachableCodeException();
      }

      assert rb != null;
      Assert.assertEquals(t, rb.renderbufferGetFormat());
      Assert.assertFalse(rb.resourceIsDeleted());
      gr.renderbufferDelete(rb);
      Assert.assertTrue(rb.resourceIsDeleted());
    }
  }
}
