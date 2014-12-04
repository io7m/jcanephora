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

import javax.media.opengl.GL;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawable;

import com.io7m.jcanephora.FaceSelection;
import com.io7m.jcanephora.FramebufferUsableType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionNoStencilBuffer;
import com.io7m.jcanephora.StencilFunction;
import com.io7m.jcanephora.StencilOperation;
import com.io7m.jcanephora.api.JCGLFramebuffersCommonType;
import com.io7m.jcanephora.api.JCGLStencilBufferType;
import com.io7m.jfunctional.None;
import com.io7m.jfunctional.OptionType;
import com.io7m.jfunctional.OptionVisitorType;
import com.io7m.jfunctional.Some;
import com.io7m.jnull.NullCheck;

final class JOGLStencil implements JCGLStencilBufferType
{
  private final JCGLFramebuffersCommonType framebuffers;
  private final GL                         gl;

  JOGLStencil(
    final GL in_gl,
    final JCGLFramebuffersCommonType in_fb)
  {
    this.gl = NullCheck.notNull(in_gl, "GL");
    this.framebuffers = NullCheck.notNull(in_fb, "Framebuffers");
  }

  protected void checkStencilBuffer()
    throws JCGLException
  {
    final int bits = this.stencilBufferGetBits();
    if (bits == 0) {
      throw new JCGLExceptionNoStencilBuffer("No stencil buffer available");
    }
  }

  @Override public void stencilBufferClear(
    final int stencil)
    throws JCGLException,
      JCGLExceptionNoStencilBuffer
  {
    this.checkStencilBuffer();
    this.gl.glClearStencil(stencil);
    this.gl.glClear(GL.GL_STENCIL_BUFFER_BIT);
  }

  @Override public void stencilBufferDisable()
    throws JCGLException,
      JCGLExceptionNoStencilBuffer
  {
    this.checkStencilBuffer();
    this.gl.glDisable(GL.GL_STENCIL_TEST);
  }

  @Override public void stencilBufferEnable()
    throws JCGLException,
      JCGLExceptionNoStencilBuffer
  {
    this.checkStencilBuffer();
    this.gl.glEnable(GL.GL_STENCIL_TEST);
  }

  @Override public void stencilBufferFunction(
    final FaceSelection faces,
    final StencilFunction function,
    final int reference,
    final int mask)
    throws JCGLException,
      JCGLExceptionNoStencilBuffer
  {
    NullCheck.notNull(faces, "Faces");
    NullCheck.notNull(function, "Function");

    this.checkStencilBuffer();

    final GL2ES2 g2 = this.gl.getGL2ES2();
    final int func = JOGLTypeConversions.stencilFunctionToGL(function);
    g2.glStencilFuncSeparate(
      JOGLTypeConversions.faceSelectionToGL(faces),
      func,
      reference,
      mask);
  }

  @Override public int stencilBufferGetBits()
    throws JCGLException
  {
    final GL g = this.gl;
    final OptionType<FramebufferUsableType> opt =
      this.framebuffers.framebufferDrawGetBound();

    return opt.accept(
      new OptionVisitorType<FramebufferUsableType, Integer>() {
        @Override public Integer none(
          final None<FramebufferUsableType> none)
        {
          /**
           * Check the capabilities of the OpenGL context for the capabilities
           * of the default framebuffer.
           */

          final GLContext context = g.getContext();
          final GLDrawable drawable = context.getGLDrawable();
          final int b = drawable.getChosenGLCapabilities().getStencilBits();
          return Integer.valueOf(b);
        }

        @Override public Integer some(
          final Some<FramebufferUsableType> s)
        {
          return Integer.valueOf(s.get().framebufferGetStencilBits());
        }
      }).intValue();
  }

  @Override public boolean stencilBufferIsEnabled()
    throws JCGLException,
      JCGLExceptionNoStencilBuffer
  {
    this.checkStencilBuffer();
    final boolean e = this.gl.glIsEnabled(GL.GL_STENCIL_TEST);
    return e;
  }

  @Override public void stencilBufferMask(
    final FaceSelection faces,
    final int mask)
    throws JCGLException,
      JCGLExceptionNoStencilBuffer
  {
    NullCheck.notNull(faces, "Faces");

    this.checkStencilBuffer();

    final GL2ES2 g2 = this.gl.getGL2ES2();
    g2.glStencilMaskSeparate(
      JOGLTypeConversions.faceSelectionToGL(faces),
      mask);
  }

  @Override public void stencilBufferOperation(
    final FaceSelection faces,
    final StencilOperation stencil_fail,
    final StencilOperation depth_fail,
    final StencilOperation pass)
    throws JCGLException
  {
    NullCheck.notNull(faces, "Face selection");
    NullCheck.notNull(stencil_fail, "Stencil fail operation");
    NullCheck.notNull(depth_fail, "Depth fail operation");
    NullCheck.notNull(pass, "Pass operation");

    final GL2ES2 g2 = this.gl.getGL2ES2();
    final int sfail = JOGLTypeConversions.stencilOperationToGL(stencil_fail);
    final int dfail = JOGLTypeConversions.stencilOperationToGL(depth_fail);
    final int dpass = JOGLTypeConversions.stencilOperationToGL(pass);
    g2.glStencilOpSeparate(
      JOGLTypeConversions.faceSelectionToGL(faces),
      sfail,
      dfail,
      dpass);
  }
}
