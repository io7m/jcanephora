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

import java.util.List;

import com.io7m.jcanephora.CubeMapFaceLH;
import com.io7m.jcanephora.FramebufferColorAttachmentPoint;
import com.io7m.jcanephora.FramebufferDrawBuffer;
import com.io7m.jcanephora.FramebufferStatus;
import com.io7m.jcanephora.FramebufferType;
import com.io7m.jcanephora.FramebufferUsableType;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.RenderableColorKind;
import com.io7m.jcanephora.RenderableDepthKind;
import com.io7m.jcanephora.RenderableStencilKind;
import com.io7m.jcanephora.RenderbufferUsableType;
import com.io7m.jcanephora.Texture2DStaticUsableType;
import com.io7m.jcanephora.TextureCubeStaticUsableType;

/**
 * Simplified interface to the subset of framebuffer functionality available
 * on all OpenGL implementations.
 */

public interface JCGLFramebuffersCommonType
{
  /**
   * <p>
   * Allocate a new framebuffer.
   * </p>
   * 
   * @throws JCGLExceptionRuntime
   *           Iff an OpenGL exception occurs.
   * @return A freshly allocated framebuffer.
   */

  FramebufferType framebufferAllocate()
    throws JCGLExceptionRuntime;

  /**
   * <p>
   * Delete the framebuffer <code>framebuffer</code>.
   * </p>
   * 
   * @param framebuffer
   *          The framebuffer.
   * @throws JCGLExceptionRuntime
   *           Iff an OpenGL exception occurs.
   */

  void framebufferDelete(
    final FramebufferType framebuffer)
    throws JCGLExceptionRuntime;

  /**
   * @return <code>true</code> iff any application-created draw framebuffer is
   *         currently bound.
   * 
   * @throws JCGLExceptionRuntime
   *           Iff an OpenGL exception occurs.
   */

  boolean framebufferDrawAnyIsBound()
    throws JCGLExceptionRuntime;

  /**
   * <p>
   * Attach the given color renderbuffer <code>renderbuffer</code> to the
   * framebuffer <code>framebuffer</code>.
   * </p>
   * <p>
   * The function will replace any existing color attachment at attachment
   * point <code>0</code>. On ES2 implementations, the only available
   * attachment point is <code>0</code>, so this will obviously replace the
   * only attached color buffer (if any).
   * </p>
   * 
   * @param framebuffer
   *          The framebuffer.
   * @param renderbuffer
   *          The renderbuffer.
   * @see com.io7m.jcanephora.RenderbufferFormat#isColorRenderable()
   * @throws JCGLExceptionRuntime
   *           Iff an OpenGL exception occurs.
   */

  void framebufferDrawAttachColorRenderbuffer(
    final FramebufferType framebuffer,
    final RenderbufferUsableType<RenderableColorKind> renderbuffer)
    throws JCGLExceptionRuntime;

  /**
   * <p>
   * Attach the given color texture <code>texture</code> to the framebuffer
   * <code>framebuffer</code>.
   * </p>
   * <p>
   * The function will replace any existing color attachment at attachment
   * point <code>0</code>. On ES2 implementations, the only available
   * attachment point is <code>0</code>, so this will obviously replace the
   * only attached color buffer (if any).
   * </p>
   * 
   * @param framebuffer
   *          The framebuffer.
   * @param texture
   *          The texture.
   * @see TextureTypeMeta#isColorRenderable2D(com.io7m.jcanephora.TextureType,
   *      com.io7m.jcanephora.JCGLVersion, JCGLNamedExtensions)
   * @throws JCGLExceptionRuntime
   *           Iff an OpenGL exception occurs.
   */

  void framebufferDrawAttachColorTexture2D(
    final FramebufferType framebuffer,
    final Texture2DStaticUsableType texture)
    throws JCGLExceptionRuntime;

  /**
   * <p>
   * Attach the face <code>face</code> of the given color cube-map texture
   * <code>texture</code> to the framebuffer <code>framebuffer</code>.
   * </p>
   * <p>
   * The function will replace any existing color attachment at attachment
   * point <code>0</code>. On ES2 implementations, the only available
   * attachment point is <code>0</code>, so this will obviously replace the
   * only attached color buffer (if any).
   * </p>
   * 
   * @param framebuffer
   *          The framebuffer.
   * @param texture
   *          The texture.
   * @param face
   *          The face of the cube, assuming a left-handed coordinate system.
   * @see com.io7m.jcanephora.TextureTypeMeta#isColorRenderable2D(com.io7m.jcanephora.TextureType,
   *      com.io7m.jcanephora.JCGLVersion, JCGLNamedExtensions)
   * @throws JCGLExceptionRuntime
   *           Iff an OpenGL exception occurs.
   */

  void framebufferDrawAttachColorTextureCube(
    final FramebufferType framebuffer,
    final TextureCubeStaticUsableType texture,
    final CubeMapFaceLH face)
    throws JCGLExceptionRuntime;

  /**
   * <p>
   * Attach the given depth renderbuffer <code>renderbuffer</code> to the
   * framebuffer <code>framebuffer</code>.
   * </p>
   * <p>
   * The function will replace any existing depth attachment.
   * </p>
   * 
   * @param framebuffer
   *          The framebuffer.
   * @param renderbuffer
   *          The renderbuffer.
   * @see com.io7m.jcanephora.RenderbufferFormat#isDepthRenderable()
   * @throws JCGLExceptionRuntime
   *           Iff an OpenGL exception occurs.
   */

  void framebufferDrawAttachDepthRenderbuffer(
    final FramebufferType framebuffer,
    final RenderbufferUsableType<RenderableDepthKind> renderbuffer)
    throws JCGLExceptionRuntime;

  /**
   * <p>
   * Attach the given depth texture <code>texture</code> to the framebuffer
   * <code>framebuffer</code>.
   * </p>
   * <p>
   * The function will replace any existing depth attachment.
   * </p>
   * 
   * @param framebuffer
   *          The framebuffer.
   * @param texture
   *          The texture.
   * @see com.io7m.jcanephora.TextureTypeMeta#isDepthRenderable2D(com.io7m.jcanephora.TextureType,
   *      JCGLNamedExtensions)
   * @throws JCGLExceptionRuntime
   *           Iff an OpenGL exception occurs.
   */

  void framebufferDrawAttachDepthTexture2D(
    final FramebufferType framebuffer,
    final Texture2DStaticUsableType texture)
    throws JCGLExceptionRuntime;

  /**
   * <p>
   * Attach the given stencil renderbuffer <code>renderbuffer</code> to the
   * framebuffer <code>framebuffer</code>.
   * </p>
   * <p>
   * The function will replace any existing stencil attachment.
   * </p>
   * 
   * @param framebuffer
   *          The framebuffer.
   * @param renderbuffer
   *          The renderbuffer.
   * @see com.io7m.jcanephora.RenderbufferFormat#isStencilRenderable()
   * @throws JCGLExceptionRuntime
   *           Iff an OpenGL exception occurs.
   */

  void framebufferDrawAttachStencilRenderbuffer(
    final FramebufferType framebuffer,
    final RenderbufferUsableType<RenderableStencilKind> renderbuffer)
    throws JCGLExceptionRuntime;

  /**
   * <p>
   * Bind the given framebuffer <code>framebuffer</code> to the draw target.
   * </p>
   * 
   * @param framebuffer
   *          The framebuffer.
   * @throws JCGLExceptionRuntime
   *           Iff an OpenGL exception occurs.
   */

  void framebufferDrawBind(
    final FramebufferUsableType framebuffer)
    throws JCGLExceptionRuntime;

  /**
   * @return <code>true</code> iff <code>framebuffer</code> is currently bound
   *         to the draw target.
   * 
   * @param framebuffer
   *          The framebuffer.
   * @throws JCGLExceptionRuntime
   *           Iff an OpenGL exception occurs.
   */

  boolean framebufferDrawIsBound(
    final FramebufferUsableType framebuffer)
    throws JCGLExceptionRuntime;

  /**
   * <p>
   * Unbind the current framebuffer from the draw target.
   * </p>
   * 
   * @throws JCGLExceptionRuntime
   *           Iff an OpenGL exception occurs.
   */

  void framebufferDrawUnbind()
    throws JCGLExceptionRuntime;

  /**
   * <p>
   * Determine the validity of the framebuffer <code>framebuffer</code>.
   * </p>
   * 
   * @return The status of the framebuffer.
   * @param framebuffer
   *          The framebuffer.
   * @throws JCGLExceptionRuntime
   *           Iff an OpenGL exception occurs.
   */

  FramebufferStatus framebufferDrawValidate(
    final FramebufferUsableType framebuffer)
    throws JCGLExceptionRuntime;

  /**
   * 
   * <p>
   * On ES2 implementations, the returned list will be a single item (as ES2
   * only allows single color attachments for framebuffers).
   * </p>
   * 
   * @throws JCGLExceptionRuntime
   *           Iff an OpenGL exception occurs.
   * @return The available set of color attachment points for framebuffers.
   */

  List<FramebufferColorAttachmentPoint> framebufferGetColorAttachmentPoints()
    throws JCGLExceptionRuntime;

  /**
   * <p>
   * On ES2 implementations, the returned list will be a single item (as ES2
   * only allows a single color draw buffer).
   * </p>
   * 
   * @return The available set of draw buffers framebuffers.
   * 
   * 
   * @throws JCGLExceptionRuntime
   *           Iff an OpenGL exception occurs.
   */

  List<FramebufferDrawBuffer> framebufferGetDrawBuffers()
    throws JCGLExceptionRuntime;
}
