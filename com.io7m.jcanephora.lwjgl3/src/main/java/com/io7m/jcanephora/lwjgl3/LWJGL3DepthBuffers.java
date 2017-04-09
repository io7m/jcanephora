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

package com.io7m.jcanephora.lwjgl3;

import com.io7m.jcanephora.core.JCGLDepthFunction;
import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLExceptionNoDepthBuffer;
import com.io7m.jcanephora.core.api.JCGLDepthBuffersType;
import com.io7m.jnull.NullCheck;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class LWJGL3DepthBuffers implements JCGLDepthBuffersType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(LWJGL3DepthBuffers.class);
  }

  private final LWJGL3Framebuffers framebuffers;
  private boolean enable_test;
  private boolean enable_write;
  private boolean clamp;
  private JCGLDepthFunction func;

  LWJGL3DepthBuffers(
    final LWJGL3Context c,
    final LWJGL3Framebuffers f)
  {
    NullCheck.notNull(c, "Context");
    this.framebuffers = NullCheck.notNull(f, "Framebuffers");

    /*
     * Configure baseline defaults.
     */

    GL11.glDisable(GL11.GL_DEPTH_TEST);
    GL11.glDepthMask(true);
    GL11.glDisable(GL32.GL_DEPTH_CLAMP);

    this.enable_test = false;
    this.enable_write = true;
    this.clamp = false;

    LWJGL3ErrorChecking.checkErrors();
  }

  @Override
  public void depthBufferClear(final float depth)
    throws JCGLException, JCGLExceptionNoDepthBuffer
  {
    this.checkDepthBits();
    GL11.glClearDepth((double) depth);
    GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
  }

  private void checkDepthBits()
  {
    if (this.getDepthBits() == 0) {
      throw new JCGLExceptionNoDepthBuffer("No depth buffer is available");
    }
  }

  @Override
  public int depthBufferGetBits()
    throws JCGLException
  {
    return this.getDepthBits();
  }

  private int getDepthBits()
  {
    final LWJGL3Framebuffer fb = this.framebuffers.getBindDraw();
    if (fb != null) {
      return fb.framebufferDepthBits();
    }

    return GL11.glGetInteger(GL11.GL_DEPTH_BITS);
  }

  @Override
  public void depthBufferTestDisable()
    throws JCGLException
  {
    this.checkDepthBits();

    if (this.enable_test) {
      GL11.glDisable(GL11.GL_DEPTH_TEST);
      this.enable_test = false;
    } else {
      LOG.trace("redundant depth test disable ignored");
    }
  }

  @Override
  public void depthBufferTestEnable(
    final JCGLDepthFunction in_func)
    throws JCGLException
  {
    NullCheck.notNull(in_func, "Depth function");
    this.checkDepthBits();

    if (!this.enable_test) {
      GL11.glEnable(GL11.GL_DEPTH_TEST);
      this.enable_test = true;
    } else {
      LOG.trace("redundant depth test enable ignored");
    }

    if (this.func != in_func) {
      final int d = LWJGL3TypeConversions.depthFunctionToGL(in_func);
      GL11.glDepthFunc(d);
      this.func = in_func;
    } else {
      LOG.trace("redundant depth function change ignored");
    }
  }

  @Override
  public boolean depthBufferTestIsEnabled()
    throws JCGLException
  {
    this.checkDepthBits();
    return this.enable_test;
  }

  @Override
  public void depthBufferWriteDisable()
    throws JCGLException
  {
    this.checkDepthBits();

    if (this.enable_write) {
      GL11.glDepthMask(false);
      this.enable_write = false;
    } else {
      LOG.trace("redundant depth write disable ignored");
    }
  }

  @Override
  public void depthBufferWriteEnable()
    throws JCGLException
  {
    this.checkDepthBits();

    if (!this.enable_write) {
      GL11.glDepthMask(true);
      this.enable_write = true;
    } else {
      LOG.trace("redundant depth write enable ignored");
    }
  }

  @Override
  public boolean depthBufferWriteIsEnabled()
    throws JCGLException
  {
    this.checkDepthBits();
    return this.enable_write;
  }

  @Override
  public void depthClampingDisable()
    throws JCGLException, JCGLExceptionNoDepthBuffer
  {
    this.checkDepthBits();

    if (this.clamp) {
      GL11.glDisable(GL32.GL_DEPTH_CLAMP);
      this.clamp = false;
    } else {
      LOG.trace("redundant depth clamp disable ignored");
    }
  }

  @Override
  public void depthClampingEnable()
    throws JCGLException, JCGLExceptionNoDepthBuffer
  {
    this.checkDepthBits();

    if (!this.clamp) {
      GL11.glEnable(GL32.GL_DEPTH_CLAMP);
      this.clamp = true;
    } else {
      LOG.trace("redundant depth clamp enable ignored");
    }
  }

  @Override
  public boolean depthClampingIsEnabled()
    throws JCGLException, JCGLExceptionNoDepthBuffer
  {
    this.checkDepthBits();
    return this.clamp;
  }
}
