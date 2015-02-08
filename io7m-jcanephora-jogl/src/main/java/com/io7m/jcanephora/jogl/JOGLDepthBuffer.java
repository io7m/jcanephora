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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawable;

import com.io7m.jcanephora.DepthFunction;
import com.io7m.jcanephora.FramebufferUsableType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionNoDepthBuffer;
import com.io7m.jcanephora.api.JCGLDepthBufferType;
import com.io7m.jcanephora.api.JCGLDepthClampingType;
import com.io7m.jcanephora.api.JCGLFramebuffersCommonType;
import com.io7m.jfunctional.None;
import com.io7m.jfunctional.OptionType;
import com.io7m.jfunctional.OptionVisitorType;
import com.io7m.jfunctional.Some;
import com.io7m.jnull.NullCheck;

final class JOGLDepthBuffer implements
  JCGLDepthBufferType,
  JCGLDepthClampingType
{
  private boolean                          clamp;
  private final ByteBuffer                 depth_buffer_mask_cache;
  private final JCGLFramebuffersCommonType framebuffers;
  private final GL                         gl;

  JOGLDepthBuffer(
    final GL in_gl,
    final JCGLFramebuffersCommonType in_fb)
  {
    this.gl = NullCheck.notNull(in_gl, "GL");
    this.framebuffers = NullCheck.notNull(in_fb, "Framebuffers");

    final ByteBuffer bb = ByteBuffer.allocateDirect(4 * 4);
    bb.order(ByteOrder.nativeOrder());
    this.depth_buffer_mask_cache = bb;
    this.clamp = false;
  }

  private void checkDepthBuffer()
    throws JCGLException
  {
    final int bits = this.depthBufferGetBits();
    if (bits == 0) {
      throw new JCGLExceptionNoDepthBuffer("No depth buffer available");
    }
  }

  @Override public void depthBufferClear(
    final float depth)
    throws JCGLException
  {
    this.checkDepthBuffer();
    this.gl.glClearDepth(depth);
    this.gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
  }

  @Override public int depthBufferGetBits()
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
          final int b = drawable.getChosenGLCapabilities().getDepthBits();
          return Integer.valueOf(b);
        }

        @Override public Integer some(
          final Some<FramebufferUsableType> s)
        {
          return Integer.valueOf(s.get().framebufferGetDepthBits());
        }
      }).intValue();
  }

  @Override public void depthBufferTestDisable()
    throws JCGLException
  {
    this.checkDepthBuffer();
    this.gl.glDisable(GL.GL_DEPTH_TEST);
  }

  @Override public void depthBufferTestEnable(
    final DepthFunction function)
    throws JCGLException
  {
    NullCheck.notNull(function, "Depth function");
    this.checkDepthBuffer();

    final int d = JOGLTypeConversions.depthFunctionToGL(function);
    this.gl.glEnable(GL.GL_DEPTH_TEST);
    this.gl.glDepthFunc(d);
  }

  @Override public boolean depthBufferTestIsEnabled()
    throws JCGLException
  {
    this.checkDepthBuffer();
    final boolean e = this.gl.glIsEnabled(GL.GL_DEPTH_TEST);
    return e;
  }

  @Override public void depthBufferWriteDisable()
    throws JCGLException
  {
    this.checkDepthBuffer();
    this.gl.glDepthMask(false);
  }

  @Override public void depthBufferWriteEnable()
    throws JCGLException
  {
    this.checkDepthBuffer();
    this.gl.glDepthMask(true);
  }

  @Override public boolean depthBufferWriteIsEnabled()
    throws JCGLException
  {
    this.checkDepthBuffer();
    this.gl
      .glGetBooleanv(GL.GL_DEPTH_WRITEMASK, this.depth_buffer_mask_cache);

    final IntBuffer bi = this.depth_buffer_mask_cache.asIntBuffer();
    return bi.get(0) == 1;
  }

  @Override public void depthClampingDisable()
    throws JCGLException,
      JCGLExceptionNoDepthBuffer
  {
    this.checkDepthBuffer();
    if (this.clamp) {
      this.gl.glDisable(GL3.GL_DEPTH_CLAMP);
      this.clamp = false;
    }
  }

  @Override public void depthClampingEnable()
    throws JCGLException,
      JCGLExceptionNoDepthBuffer
  {
    this.checkDepthBuffer();
    if (this.clamp == false) {
      this.gl.glEnable(GL3.GL_DEPTH_CLAMP);
      this.clamp = true;
    }
  }

  @Override public boolean depthClampingIsEnabled()
    throws JCGLException,
      JCGLExceptionNoDepthBuffer
  {
    this.checkDepthBuffer();
    return this.clamp;
  }
}
