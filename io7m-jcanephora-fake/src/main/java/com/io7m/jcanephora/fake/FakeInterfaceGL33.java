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
import com.io7m.jcanephora.core.api.JCGLCullingType;
import com.io7m.jcanephora.core.api.JCGLDepthBuffersType;
import com.io7m.jcanephora.core.api.JCGLDrawType;
import com.io7m.jcanephora.core.api.JCGLFramebuffersType;
import com.io7m.jcanephora.core.api.JCGLIndexBuffersType;
import com.io7m.jcanephora.core.api.JCGLInterfaceGL33Type;
import com.io7m.jcanephora.core.api.JCGLPolygonModesType;
import com.io7m.jcanephora.core.api.JCGLShadersType;
import com.io7m.jcanephora.core.api.JCGLTexturesType;
import com.io7m.junreachable.UnimplementedCodeException;

final class FakeInterfaceGL33 implements JCGLInterfaceGL33Type
{
  private final FakeArrayBuffers array_buffers;
  private final FakeArrayObjects array_objects;
  private final FakeShaders      shaders;
  private final FakeIndexBuffers index_buffers;
  private final FakeDraw         draw;
  private final FakeClear        clear;
  private final FakeTextures     textures;
  private final FakeBlending     blending;
  private final FakePolygonMode  poly;
  private final FakeCulling      culling;

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
    this.blending = new FakeBlending(c);
    this.poly = new FakePolygonMode(c);
    this.culling = new FakeCulling(c);
  }

  @Override public JCGLArrayBuffersType getArrayBuffers()
  {
    return this.array_buffers;
  }

  @Override public JCGLArrayObjectsType getArrayObjects()
  {
    return this.array_objects;
  }

  @Override public JCGLBlendingType getBlending()
  {
    return this.blending;
  }

  @Override public JCGLDepthBuffersType getDepthBuffers()
  {
    throw new UnimplementedCodeException();
  }

  @Override public JCGLShadersType getShaders()
  {
    return this.shaders;
  }

  @Override public JCGLIndexBuffersType getIndexBuffers()
  {
    return this.index_buffers;
  }

  @Override public JCGLDrawType getDraw()
  {
    return this.draw;
  }

  @Override public JCGLClearType getClear()
  {
    return this.clear;
  }

  @Override public JCGLTexturesType getTextures()
  {
    return this.textures;
  }

  @Override public JCGLFramebuffersType getFramebuffers()
  {
    throw new UnimplementedCodeException();
  }

  @Override public JCGLPolygonModesType getPolygonModes()
  {
    return this.poly;
  }

  @Override public JCGLCullingType getCulling()
  {
    return this.culling;
  }
}
