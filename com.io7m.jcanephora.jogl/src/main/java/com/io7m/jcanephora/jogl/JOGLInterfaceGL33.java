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

import com.io7m.jcanephora.core.JCGLExceptionNonCompliant;
import com.io7m.jcanephora.core.api.JCGLArrayBuffersType;
import com.io7m.jcanephora.core.api.JCGLArrayObjectsType;
import com.io7m.jcanephora.core.api.JCGLBlendingType;
import com.io7m.jcanephora.core.api.JCGLClearType;
import com.io7m.jcanephora.core.api.JCGLColorBufferMaskingType;
import com.io7m.jcanephora.core.api.JCGLCullingType;
import com.io7m.jcanephora.core.api.JCGLDepthBuffersType;
import com.io7m.jcanephora.core.api.JCGLDrawType;
import com.io7m.jcanephora.core.api.JCGLFramebuffersType;
import com.io7m.jcanephora.core.api.JCGLIndexBuffersType;
import com.io7m.jcanephora.core.api.JCGLInterfaceGL33Type;
import com.io7m.jcanephora.core.api.JCGLPolygonModesType;
import com.io7m.jcanephora.core.api.JCGLScissorType;
import com.io7m.jcanephora.core.api.JCGLShadersType;
import com.io7m.jcanephora.core.api.JCGLStencilBuffersType;
import com.io7m.jcanephora.core.api.JCGLTexturesType;
import com.io7m.jcanephora.core.api.JCGLTimersType;
import com.io7m.jcanephora.core.api.JCGLViewportsType;
import com.io7m.jnull.NullCheck;

final class JOGLInterfaceGL33 implements JCGLInterfaceGL33Type
{
  private final JOGLArrayBuffers array_buffers;
  private final JOGLArrayObjects array_objects;
  private final JOGLShaders shaders;
  private final JOGLIndexBuffers index_buffers;
  private final JOGLDraw draw;
  private final JOGLClear clear;
  private final JOGLTextures textures;
  private final JOGLFramebuffers framebuffers;
  private final JOGLDepthBuffers depth_buffers;
  private final JOGLBlending blending;
  private final JOGLPolygonMode poly;
  private final JOGLCulling culling;
  private final JOGLColorBufferMasking color_masking;
  private final JOGLViewports viewports;
  private final JOGLScissor scissor;
  private final JOGLStencilBuffers stencil;
  private final JOGLTimers timers;

  JOGLInterfaceGL33(
    final JOGLContext c)
    throws JCGLExceptionNonCompliant
  {
    NullCheck.notNull(c, "Context");

    this.array_buffers = new JOGLArrayBuffers(c);
    this.index_buffers = new JOGLIndexBuffers(c);
    this.array_objects =
      new JOGLArrayObjects(c, this.array_buffers, this.index_buffers);
    this.shaders = new JOGLShaders(c);
    this.draw = new JOGLDraw(c, this.array_objects, this.index_buffers);
    this.clear = new JOGLClear(c);
    this.textures = new JOGLTextures(c);
    this.framebuffers = new JOGLFramebuffers(c, this.textures);
    this.depth_buffers = new JOGLDepthBuffers(c, this.framebuffers);
    this.blending = new JOGLBlending(c);
    this.poly = new JOGLPolygonMode(c);
    this.culling = new JOGLCulling(c);
    this.color_masking = new JOGLColorBufferMasking(c);
    this.viewports = new JOGLViewports(c);
    this.scissor = new JOGLScissor(c);
    this.stencil = new JOGLStencilBuffers(c, this.framebuffers);
    this.timers = new JOGLTimers(c);
  }

  @Override
  public JCGLArrayBuffersType arrayBuffers()
  {
    return this.array_buffers;
  }

  @Override
  public JCGLArrayObjectsType arrayObjects()
  {
    return this.array_objects;
  }

  @Override
  public JCGLBlendingType blending()
  {
    return this.blending;
  }

  @Override
  public JCGLDepthBuffersType depthBuffers()
  {
    return this.depth_buffers;
  }

  @Override
  public JCGLShadersType shaders()
  {
    return this.shaders;
  }

  @Override
  public JCGLIndexBuffersType indexBuffers()
  {
    return this.index_buffers;
  }

  @Override
  public JCGLDrawType drawing()
  {
    return this.draw;
  }

  @Override
  public JCGLClearType clearing()
  {
    return this.clear;
  }

  @Override
  public JCGLTexturesType textures()
  {
    return this.textures;
  }

  @Override
  public JCGLFramebuffersType framebuffers()
  {
    return this.framebuffers;
  }

  @Override
  public JCGLPolygonModesType polygonModes()
  {
    return this.poly;
  }

  @Override
  public JCGLCullingType culling()
  {
    return this.culling;
  }

  @Override
  public JCGLColorBufferMaskingType colorBufferMasking()
  {
    return this.color_masking;
  }

  @Override
  public JCGLViewportsType viewports()
  {
    return this.viewports;
  }

  @Override
  public JCGLScissorType scissor()
  {
    return this.scissor;
  }

  @Override
  public JCGLStencilBuffersType stencilBuffers()
  {
    return this.stencil;
  }

  @Override
  public JCGLTimersType timers()
  {
    return this.timers;
  }
}
