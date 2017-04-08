/*
 * Copyright © 2016 <code@io7m.com> http://io7m.com
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

final class LWJGL3InterfaceGL33 implements JCGLInterfaceGL33Type
{
  private final LWJGL3Viewports viewports;
  private final LWJGL3Scissor scissor;
  private final LWJGL3Culling culling;
  private final LWJGL3Blending blending;
  private final LWJGL3PolygonMode polygon_modes;
  private final LWJGL3Clear clear;
  private final LWJGL3ColorBufferMasking color_mask;
  private final LWJGL3ArrayBuffers array_buffers;
  private final LWJGL3IndexBuffers index_buffers;
  private final LWJGL3ArrayObjects array_objects;
  private final LWJGL3Draw draw;
  private final LWJGL3Shaders shaders;
  private final LWJGL3Textures textures;
  private final LWJGL3Framebuffers framebuffers;
  private final LWJGL3DepthBuffers depth_buffers;
  private final LWJGL3StencilBuffers stencil_buffers;
  private final LWJGL3Timers timers;

  LWJGL3InterfaceGL33(
    final LWJGL3Context c)
    throws JCGLExceptionNonCompliant
  {
    NullCheck.notNull(c, "Context");

    this.array_buffers = new LWJGL3ArrayBuffers(c);
    this.index_buffers = new LWJGL3IndexBuffers(c);
    this.array_objects =
      new LWJGL3ArrayObjects(c, this.array_buffers, this.index_buffers);
    this.draw = new LWJGL3Draw(c, this.array_objects, this.index_buffers);
    this.blending = new LWJGL3Blending(c);
    this.color_mask = new LWJGL3ColorBufferMasking(c);
    this.clear = new LWJGL3Clear(c);
    this.culling = new LWJGL3Culling(c);
    this.textures = new LWJGL3Textures(c);
    this.framebuffers = new LWJGL3Framebuffers(c, this.textures);
    this.depth_buffers = new LWJGL3DepthBuffers(c, this.framebuffers);
    this.polygon_modes = new LWJGL3PolygonMode(c);
    this.shaders = new LWJGL3Shaders(c);
    this.scissor = new LWJGL3Scissor(c);
    this.stencil_buffers = new LWJGL3StencilBuffers(c, this.framebuffers);
    this.timers = new LWJGL3Timers(c);
    this.viewports = new LWJGL3Viewports(c);
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
    return this.polygon_modes;
  }

  @Override
  public JCGLCullingType culling()
  {
    return this.culling;
  }

  @Override
  public JCGLColorBufferMaskingType colorBufferMasking()
  {
    return this.color_mask;
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
    return this.stencil_buffers;
  }

  @Override
  public JCGLTimersType timers()
  {
    return this.timers;
  }
}
