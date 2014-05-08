/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.jogl;

import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2ES3;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawable;

import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.api.JCGLFramebuffersCommonType;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;

final class JOGLStencilGL2GL3 extends JOGLStencilAbstract
{
  private final JCGLFramebuffersCommonType framebuffers;
  private final JOGLIntegerCacheType       icache;

  JOGLStencilGL2GL3(
    final GL in_gl,
    final JOGLIntegerCacheType in_icache,
    final JCGLFramebuffersCommonType in_framebuffers,
    final LogUsableType in_log)
  {
    super(in_gl, in_log);
    this.framebuffers = NullCheck.notNull(in_framebuffers, "Framebuffers");
    this.icache = NullCheck.notNull(in_icache, "Integer cache");
  }

  @Override public int stencilBufferGetBits()
    throws JCGLException
  {
    final GL g = this.getGL();

    /**
     * If a framebuffer is bound, check to see if there's a stencil
     * attachment.
     */

    if (this.framebuffers.framebufferDrawAnyIsBound()) {

      {
        final IntBuffer ix = this.icache.getIntegerCache();
        g.glGetFramebufferAttachmentParameteriv(
          GL2ES3.GL_DRAW_FRAMEBUFFER,
          GL.GL_STENCIL_ATTACHMENT,
          GL.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE,
          ix);
        JOGLErrors.check(g);
        if (ix.get(0) == GL.GL_NONE) {
          return 0;
        }
      }

      /**
       * If there's a stencil attachment, check the size of it.
       */

      {
        final IntBuffer ix = this.icache.getIntegerCache();
        g.glGetFramebufferAttachmentParameteriv(
          GL2ES3.GL_DRAW_FRAMEBUFFER,
          GL.GL_STENCIL_ATTACHMENT,
          GL2ES3.GL_FRAMEBUFFER_ATTACHMENT_STENCIL_SIZE,
          ix);
        JOGLErrors.check(g);
        return ix.get(0);
      }
    }

    /**
     * Otherwise, check the capabilities of the OpenGL context for the
     * capabilities of the default framebuffer.
     */

    final GLContext context = g.getContext();
    final GLDrawable drawable = context.getGLDrawable();
    return drawable.getChosenGLCapabilities().getStencilBits();
  }

}
