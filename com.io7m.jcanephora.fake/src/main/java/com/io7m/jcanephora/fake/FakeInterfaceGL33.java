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

package com.io7m.jcanephora.fake;

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
import com.io7m.junreachable.UnimplementedCodeException;

final class FakeInterfaceGL33 implements JCGLInterfaceGL33Type
{
  private final FakeArrayBuffers array_buffers;
  private final FakeArrayObjects array_objects;
  private final FakeShaders shaders;
  private final FakeIndexBuffers index_buffers;
  private final FakeDraw draw;
  private final FakeClear clear;
  private final FakeTextures textures;
  private final FakeBlending blending;
  private final FakePolygonMode poly;
  private final FakeCulling culling;
  private final FakeColorBufferMasking color_masking;
  private final FakeViewports viewports;
  private final FakeScissor scissor;
  private final FakeFramebuffers framebuffers;
  private final FakeTimers timers;

  FakeInterfaceGL33(final FakeContext c)
    throws JCGLExceptionNonCompliant
  {
    this.array_buffers = new FakeArrayBuffers(c);
    this.index_buffers = new FakeIndexBuffers(c);
    this.array_objects =
      new FakeArrayObjects(c, this.array_buffers, this.index_buffers);
    this.shaders = new FakeShaders(c);
    this.draw = new FakeDraw(c, this.shaders, this.index_buffers);
    this.clear = new FakeClear(c);
    this.textures = new FakeTextures(c);
    this.framebuffers = new FakeFramebuffers(c, this.textures);
    this.blending = new FakeBlending(c);
    this.poly = new FakePolygonMode(c);
    this.culling = new FakeCulling(c);
    this.color_masking = new FakeColorBufferMasking(c);
    this.viewports = new FakeViewports(c);
    this.scissor = new FakeScissor(c);
    this.timers = new FakeTimers(c);
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
    throw new UnimplementedCodeException();
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
    throw new UnimplementedCodeException();
  }

  @Override
  public JCGLTimersType timers()
  {
    return this.timers;
  }
}
