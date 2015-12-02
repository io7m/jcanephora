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
import com.io7m.jcanephora.core.api.JCGLShadersType;
import com.io7m.jcanephora.core.api.JCGLTexturesType;
import com.io7m.jnull.NullCheck;

final class JOGLInterfaceGL33 implements JCGLInterfaceGL33Type
{
  private final JOGLArrayBuffers       array_buffers;
  private final JOGLArrayObjects       array_objects;
  private final JOGLShaders            shaders;
  private final JOGLIndexBuffers       index_buffers;
  private final JOGLDraw               draw;
  private final JOGLClear              clear;
  private final JOGLTextures           textures;
  private final JOGLFramebuffers       framebuffers;
  private final JOGLDepthBuffers       depth_buffers;
  private final JOGLBlending           blending;
  private final JOGLPolygonMode        poly;
  private final JOGLCulling            culling;
  private final JOGLColorBufferMasking color_masking;

  JOGLInterfaceGL33(
    final JOGLContext c)
    throws JCGLExceptionNonCompliant
  {
    NullCheck.notNull(c);

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
    return this.depth_buffers;
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
    return this.framebuffers;
  }

  @Override public JCGLPolygonModesType getPolygonModes()
  {
    return this.poly;
  }

  @Override public JCGLCullingType getCulling()
  {
    return this.culling;
  }

  @Override public JCGLColorBufferMaskingType getColorBufferMasking()
  {
    return this.color_masking;
  }
}
