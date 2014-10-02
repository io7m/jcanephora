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

import com.io7m.jcanephora.CubeMapFaceLH;
import com.io7m.jcanephora.RenderableColorKind;
import com.io7m.jcanephora.RenderableDepthKind;
import com.io7m.jcanephora.RenderableStencilKind;
import com.io7m.jcanephora.RenderbufferUsableType;
import com.io7m.jcanephora.Texture2DStaticUsableType;
import com.io7m.jcanephora.TextureCubeStaticUsableType;

/**
 * The type of mutable builders for framebuffers.
 */

public interface JCGLFramebufferBuilderType extends
  JCGLFramebufferBuilderReadableType
{
  /**
   * <p>
   * Attach the given color renderbuffer <code>renderbuffer</code> to the
   * framebuffer that will be created.
   * </p>
   * <p>
   * The function will replace any existing color attachment at attachment
   * point <code>0</code>. On ES2 implementations, the only available
   * attachment point is <code>0</code>, so this will obviously replace the
   * only attached color buffer (if any).
   * </p>
   *
   * @param r
   *          The renderbuffer.
   * @see com.io7m.jcanephora.RenderbufferFormat#isColorRenderable()
   */

  void attachColorRenderbuffer(
    final RenderbufferUsableType<RenderableColorKind> r);

  /**
   * <p>
   * Attach the given color texture <code>texture</code> to the framebuffer
   * that will be created.
   * </p>
   * <p>
   * The function will replace any existing color attachment at attachment
   * point <code>0</code>. On ES2 implementations, the only available
   * attachment point is <code>0</code>, so this will obviously replace the
   * only attached color buffer (if any).
   * </p>
   *
   * @param t
   *          The texture.
   * @see com.io7m.jcanephora.TextureFormatMeta#isColorRenderable2D(com.io7m.jcanephora.TextureFormat,
   *      com.io7m.jcanephora.JCGLVersion, JCGLNamedExtensionsType)
   */

  void attachColorTexture2D(
    final Texture2DStaticUsableType t);

  /**
   * <p>
   * Attach the face <code>face</code> of the given color cube-map texture
   * <code>texture</code> to the framebuffer that will be created.
   * </p>
   * <p>
   * The function will replace any existing color attachment at attachment
   * point <code>0</code>. On ES2 implementations, the only available
   * attachment point is <code>0</code>, so this will obviously replace the
   * only attached color buffer (if any).
   * </p>
   *
   * @param texture
   *          The texture.
   * @param face
   *          The face of the cube, assuming a left-handed coordinate system.
   * @see com.io7m.jcanephora.TextureFormatMeta#isColorRenderable2D(com.io7m.jcanephora.TextureFormat,
   *      com.io7m.jcanephora.JCGLVersion, JCGLNamedExtensionsType)
   */

  void attachColorTextureCube(
    final TextureCubeStaticUsableType texture,
    final CubeMapFaceLH face);

  /**
   * <p>
   * Attach the given depth renderbuffer <code>renderbuffer</code> to the
   * framebuffer that will be created.
   * </p>
   * <p>
   * The function will replace any existing depth attachment. This includes
   * individual depth and combined depth/stencil attachments.
   * </p>
   *
   * @param r
   *          The renderbuffer.
   * @see com.io7m.jcanephora.RenderbufferFormat#isDepthRenderable()
   */

  void attachDepthRenderbuffer(
    final RenderbufferUsableType<RenderableDepthKind> r);

  /**
   * <p>
   * Attach the given depth texture <code>texture</code> to the framebuffer
   * that will be created.
   * </p>
   * <p>
   * The function will replace any existing depth attachment. This includes
   * individual depth and combined depth/stencil attachments.
   * </p>
   *
   * @param t
   *          The texture.
   * @see com.io7m.jcanephora.TextureFormatMeta#isDepthRenderable2D(com.io7m.jcanephora.TextureFormat,
   *      JCGLNamedExtensionsType)
   */

  void attachDepthTexture2D(
    final Texture2DStaticUsableType t);

  /**
   * <p>
   * Attach the given stencil renderbuffer <code>renderbuffer</code> to the
   * framebuffer that will be created.
   * </p>
   * <p>
   * The function will replace any existing stencil attachment. This includes
   * individual depth and combined depth/stencil attachments.
   * </p>
   *
   * @param r
   *          The renderbuffer.
   * @see com.io7m.jcanephora.RenderbufferFormat#isStencilRenderable()
   */

  void attachStencilRenderbuffer(
    final RenderbufferUsableType<RenderableStencilKind> r);
}
