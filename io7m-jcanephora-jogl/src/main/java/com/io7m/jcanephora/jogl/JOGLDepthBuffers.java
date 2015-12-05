/*
 * Copyright © 2015 <code@io7m.com> http://io7m.com
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

import com.io7m.jcanephora.core.JCGLDepthFunction;
import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLExceptionNoDepthBuffer;
import com.io7m.jcanephora.core.api.JCGLDepthBuffersType;
import com.io7m.jnull.NullCheck;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLDrawable;

final class JOGLDepthBuffers implements JCGLDepthBuffersType
{
  private final JOGLFramebuffers framebuffers;
  private final GL3              gl;
  private       boolean          enable_test;
  private       boolean          enable_write;
  private       boolean          clamp;

  JOGLDepthBuffers(
    final JOGLContext c,
    final JOGLFramebuffers f)
  {
    NullCheck.notNull(c);
    this.framebuffers = NullCheck.notNull(f);
    this.gl = c.getGL3();

    /**
     * Configure baseline defaults.
     */

    this.gl.glDisable(GL.GL_DEPTH_TEST);
    this.gl.glDepthMask(true);
    this.gl.glDisable(GL3.GL_DEPTH_CLAMP);

    this.enable_test = false;
    this.enable_write = true;
    this.clamp = false;

    JOGLErrorChecking.checkErrors(this.gl);
  }

  @Override public void depthBufferClear(final float depth)
    throws JCGLException, JCGLExceptionNoDepthBuffer
  {
    this.checkDepthBits();
    this.gl.glClearDepth((double) depth);
    this.gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
  }

  private void checkDepthBits()
  {
    if (this.getDepthBits() == 0) {
      throw new JCGLExceptionNoDepthBuffer("No depth buffer is available");
    }
  }

  @Override public int depthBufferGetBits()
    throws JCGLException
  {
    return this.getDepthBits();
  }

  private int getDepthBits()
  {
    final JOGLFramebuffer fb = this.framebuffers.getBindDraw();
    if (fb != null) {
      return fb.framebufferGetDepthBits();
    }

    /**
     * Check the capabilities of the OpenGL context for the capabilities
     * of the default framebuffer.
     */

    final GLContext c = this.gl.getContext();
    final GLDrawable drawable = c.getGLDrawable();
    return drawable.getChosenGLCapabilities().getDepthBits();
  }

  @Override public void depthBufferTestDisable()
    throws JCGLException
  {
    this.checkDepthBits();
    this.gl.glDisable(GL.GL_DEPTH_TEST);
    this.enable_test = false;
  }

  @Override public void depthBufferTestEnable(
    final JCGLDepthFunction in_func)
    throws JCGLException
  {
    NullCheck.notNull(in_func, "Depth function");
    this.checkDepthBits();

    final int d = JOGLTypeConversions.depthFunctionToGL(in_func);
    this.gl.glEnable(GL.GL_DEPTH_TEST);
    this.gl.glDepthFunc(d);
    this.enable_test = true;
  }

  @Override public boolean depthBufferTestIsEnabled()
    throws JCGLException
  {
    this.checkDepthBits();
    return this.enable_test;
  }

  @Override public void depthBufferWriteDisable()
    throws JCGLException
  {
    this.checkDepthBits();
    this.gl.glDepthMask(false);
    this.enable_write = false;
  }

  @Override public void depthBufferWriteEnable()
    throws JCGLException
  {
    this.checkDepthBits();
    this.gl.glDepthMask(true);
    this.enable_write = true;
  }

  @Override public boolean depthBufferWriteIsEnabled()
    throws JCGLException
  {
    this.checkDepthBits();
    return this.enable_write;
  }

  @Override public void depthClampingDisable()
    throws JCGLException, JCGLExceptionNoDepthBuffer
  {
    this.checkDepthBits();

    if (this.clamp) {
      this.gl.glDisable(GL3.GL_DEPTH_CLAMP);
      this.clamp = false;
    }
  }

  @Override public void depthClampingEnable()
    throws JCGLException, JCGLExceptionNoDepthBuffer
  {
    this.checkDepthBits();

    if (!this.clamp) {
      this.gl.glEnable(GL3.GL_DEPTH_CLAMP);
      this.clamp = true;
    }
  }

  @Override public boolean depthClampingIsEnabled()
    throws JCGLException, JCGLExceptionNoDepthBuffer
  {
    this.checkDepthBits();
    return this.clamp;
  }
}
