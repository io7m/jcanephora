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

package com.io7m.jcanephora.api;

import java.util.Map;

import com.io7m.jcanephora.CubeMapFaceLH;
import com.io7m.jcanephora.FramebufferColorAttachmentPointType;
import com.io7m.jcanephora.FramebufferDrawBufferType;
import com.io7m.jcanephora.FramebufferType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.RenderableColorKind;
import com.io7m.jcanephora.RenderableDepthStencilKind;
import com.io7m.jcanephora.RenderbufferUsableType;
import com.io7m.jcanephora.Texture2DStaticUsableType;
import com.io7m.jcanephora.TextureCubeStaticUsableType;

/**
 * Simplified interface to the framebuffer functionality available on OpenGL
 * 3.* implementations.
 */

public interface JCGLFramebuffersGL3Type extends
  JCGLFramebuffersGLES2Type,
  JCGLFramebuffersReadGL3Type
{
  /**
   * <p>
   * Attach the given color renderbuffer <code>renderbuffer</code> to the
   * framebuffer <code>framebuffer</code> at attachment point
   * <code>point</code>.
   * </p>
   * <p>
   * The function will replace any existing color attachment.
   * </p>
   * 
   * @param framebuffer
   *          The framebuffer.
   * @param point
   *          The attachment point.
   * @param renderbuffer
   *          The renderbuffer.
   * @see com.io7m.jcanephora.RenderbufferFormat#isColorRenderable()
   * 
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

  void framebufferDrawAttachColorRenderbufferAt(
    final FramebufferType framebuffer,
    final FramebufferColorAttachmentPointType point,
    final RenderbufferUsableType<RenderableColorKind> renderbuffer)
    throws JCGLException;

  /**
   * <p>
   * Attach the given color texture <code>texture</code> to the framebuffer
   * <code>framebuffer</code>, at attachment point <code>point</code>.
   * </p>
   * <p>
   * The function will replace any existing color attachment at attachment
   * point <code>point</code>.
   * </p>
   * 
   * @param framebuffer
   *          The framebuffer.
   * @param point
   *          The attachment point.
   * @param texture
   *          The texture.
   * @see com.io7m.jcanephora.TextureTypeMeta#isColorRenderable2D(com.io7m.jcanephora.TextureType,
   *      com.io7m.jcanephora.JCGLVersion, JCGLNamedExtensions)
   * 
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

  void framebufferDrawAttachColorTexture2DAt(
    final FramebufferType framebuffer,
    final FramebufferColorAttachmentPointType point,
    final Texture2DStaticUsableType texture)
    throws JCGLException;

  /**
   * <p>
   * Attach the face <code>face</code> of the given color cube-map texture
   * <code>texture</code> to the framebuffer <code>framebuffer</code>, at
   * attachment point <code>point</code>.
   * </p>
   * <p>
   * The function will replace any existing color attachment at attachment
   * point <code>point</code>.
   * </p>
   * 
   * @param framebuffer
   *          The framebuffer.
   * @param point
   *          The attachment point.
   * @param texture
   *          The texture.
   * @param face
   *          The cube face, assuming a left-hannded coordinate system.
   * @see com.io7m.jcanephora.TextureTypeMeta#isColorRenderable2D(com.io7m.jcanephora.TextureType,
   *      com.io7m.jcanephora.JCGLVersion, JCGLNamedExtensions)
   * 
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

  void framebufferDrawAttachColorTextureCubeAt(
    final FramebufferType framebuffer,
    final FramebufferColorAttachmentPointType point,
    final TextureCubeStaticUsableType texture,
    final CubeMapFaceLH face)
    throws JCGLException;

  /**
   * <p>
   * Attach the given depth/stencil renderbuffer <code>renderbuffer</code> to
   * the framebuffer <code>framebuffer</code>.
   * </p>
   * <p>
   * The function will replace any existing depth/stencil attachment.
   * </p>
   * 
   * @param framebuffer
   *          The framebuffer.
   * @param renderbuffer
   *          The renderbuffer.
   * @see com.io7m.jcanephora.RenderbufferFormat#isDepthRenderable()
   * @see com.io7m.jcanephora.RenderbufferFormat#isStencilRenderable()
   * 
   * 
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

  void framebufferDrawAttachDepthStencilRenderbuffer(
    final FramebufferType framebuffer,
    final RenderbufferUsableType<RenderableDepthStencilKind> renderbuffer)
    throws JCGLException;

  /**
   * <p>
   * For all draw buffers (keys) given in <code>mappings</code>, the color
   * attachment for the draw buffer is set to the associated value. All other
   * draw buffers are set to discard color data.
   * </p>
   * 
   * @param framebuffer
   *          The framebuffer.
   * @param mappings
   *          The attachment point mappings.
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

    void
    framebufferDrawSetBuffers(
      final FramebufferType framebuffer,
      final Map<FramebufferDrawBufferType, FramebufferColorAttachmentPointType> mappings)
      throws JCGLException;
}
