/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
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

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.CubeMapFaceLH;
import com.io7m.jcanephora.FramebufferColorAttachmentPoint;
import com.io7m.jcanephora.FramebufferDrawBuffer;
import com.io7m.jcanephora.JCGLRuntimeException;
import com.io7m.jcanephora.JCGLVersion;
import com.io7m.jcanephora.RenderableColorKind;
import com.io7m.jcanephora.RenderableDepthStencilKind;
import com.io7m.jcanephora.RenderbufferFormat;
import com.io7m.jcanephora.RenderbufferUsableType;
import com.io7m.jcanephora.Texture2DStaticUsableType;
import com.io7m.jcanephora.TextureCubeStaticUsable;
import com.io7m.jcanephora.jogl.FramebufferReference;

/**
 * Simplified interface to the framebuffer functionality available on OpenGL
 * 3.* implementations.
 */

public interface JCGLFramebuffersGL3 extends
  JCGLFramebuffersGLES2,
  JCGLFramebuffersReadGL3
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
   * @see RenderbufferFormat#isColorRenderable()
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li>Any of the parameters are <code>null</code></li>
   *           <li><code>renderbuffer.resourceIsDeleted()</code></li>
   *           <li><code>framebuffer.resourceIsDeleted()</code></li>
   *           <li><code>framebufferIsBoundDraw(framebuffer) == false</code></li>
   *           <li><code>renderbuffer</code> is not a color-renderable
   *           format</code></li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL exception occurs.
   */

  void framebufferDrawAttachColorRenderbufferAt(
    final  FramebufferReference framebuffer,
    final  FramebufferColorAttachmentPoint point,
    final  RenderbufferUsableType<RenderableColorKind> renderbuffer)
    throws JCGLRuntimeException,
      ConstraintError;

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
   * @see TextureTypeMeta#isColorRenderable2D(TextureType, JCGLVersion,
   *      JCGLNamedExtensions)
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li>Any of the parameters are <code>null</code></li>
   *           <li><code>texture.resourceIsDeleted()</code></li>
   *           <li><code>framebuffer.resourceIsDeleted()</code></li>
   *           <li><code>framebufferIsBoundDraw(framebuffer) == false</code></li>
   *           <li><code>texture</code> is not a color-renderable
   *           format</code></li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL exception occurs.
   */

  void framebufferDrawAttachColorTexture2DAt(
    final  FramebufferReference framebuffer,
    final  FramebufferColorAttachmentPoint point,
    final  Texture2DStaticUsableType texture)
    throws JCGLRuntimeException,
      ConstraintError;

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
   * @see TextureTypeMeta#isColorRenderable2D(TextureType, JCGLVersion,
   *      JCGLNamedExtensions)
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li>Any of the parameters are <code>null</code></li>
   *           <li><code>texture.resourceIsDeleted()</code></li>
   *           <li><code>framebuffer.resourceIsDeleted()</code></li>
   *           <li><code>framebufferIsBoundDraw(framebuffer) == false</code></li>
   *           <li><code>texture</code> is not a color-renderable
   *           format</code></li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL exception occurs.
   */

  void framebufferDrawAttachColorTextureCubeAt(
    final  FramebufferReference framebuffer,
    final  FramebufferColorAttachmentPoint point,
    final  TextureCubeStaticUsable texture,
    final  CubeMapFaceLH face)
    throws JCGLRuntimeException,
      ConstraintError;

  /**
   * <p>
   * Attach the given depth/stencil renderbuffer <code>renderbuffer</code> to
   * the framebuffer <code>framebuffer</code>.
   * </p>
   * <p>
   * The function will replace any existing depth/stencil attachment.
   * </p>
   * 
   * @see RenderbufferFormat#isDepthRenderable()
   * @see RenderbufferFormat#isStencilRenderable()
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li>Any of the parameters are <code>null</code></li>
   *           <li><code>renderbuffer.resourceIsDeleted()</code></li>
   *           <li><code>framebuffer.resourceIsDeleted()</code></li>
   *           <li><code>framebufferIsBoundDraw(framebuffer) == false</code></li>
   *           <li><code>renderbuffer</code> is not a depth-renderable and
   *           stencil-renderable format</code></li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL exception occurs.
   */

  void framebufferDrawAttachDepthStencilRenderbuffer(
    final  FramebufferReference framebuffer,
    final  RenderbufferUsableType<RenderableDepthStencilKind> renderbuffer)
    throws JCGLRuntimeException,
      ConstraintError;

  /**
   * <p>
   * For all draw buffers (keys) given in <code>mappings</code>, the color
   * attachment for the draw buffer is set to the associated value. All other
   * draw buffers are set to discard color data.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li>Any of the parameters are <code>null</code></li>
   *           <li><code>framebuffer.resourceIsDeleted()</code></li>
   *           <li><code>framebufferIsBoundDraw(framebuffer) == false</code></li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL exception occurs.
   */

    void
    framebufferDrawSetBuffers(
      final  FramebufferReference framebuffer,
      final  Map<FramebufferDrawBuffer, FramebufferColorAttachmentPoint> mappings)
      throws JCGLRuntimeException,
        ConstraintError;
}
