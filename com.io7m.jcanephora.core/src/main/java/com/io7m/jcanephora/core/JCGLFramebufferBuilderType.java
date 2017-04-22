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

package com.io7m.jcanephora.core;

/**
 * A mutable builder for configuring framebuffer objects.
 */

public interface JCGLFramebufferBuilderType
{
  /**
   * <p>Attach the given color texture {@code texture} to the framebuffer that
   * will be created, at attachment point {@code point}.</p>
   *
   * <p>The function will replace any existing color attachment at attachment
   * point {@code point}.</p>
   *
   * <p>The attachment will be associated with draw buffer {@code buffer}, so
   * any output written to {@code buffer} will end up in {@code texture}.</p>
   *
   * @param point   The attachment point
   * @param buffer  The draw buffer that will be associated with the color
   *                attachment
   * @param texture The texture
   */

  void attachColorTexture2DAt(
    JCGLFramebufferColorAttachmentPointType point,
    JCGLFramebufferDrawBufferType buffer,
    JCGLTexture2DUsableType texture);

  /**
   * <p>Attach the face {@code face} of the given color cube-map texture {@code
   * texture} to the framebuffer that will be created, at attachment point
   * {@code point}.</p>
   *
   * <p>The function will replace any existing color attachment at attachment
   * point {@code point}.</p>
   *
   * @param point   The attachment point
   * @param buffer  The draw buffer that will be associated with the color
   *                attachment
   * @param texture The texture
   * @param face    The cube face, assuming a left-hannded coordinate system
   *
   * @see JCGLTextureFormats#isColorRenderable2D(JCGLTextureFormat)
   */

  void attachColorTextureCubeAt(
    JCGLFramebufferColorAttachmentPointType point,
    JCGLFramebufferDrawBufferType buffer,
    JCGLTextureCubeUsableType texture,
    JCGLCubeMapFaceLH face);

  /**
   * <p>Attach the given depth texture {@code texture} to the framebuffer that
   * will be created.</p>
   *
   * <p>The function will replace any existing depth attachment. This includes
   * individual depth and combined depth/stencil attachments.</p>
   *
   * @param t The texture.
   */

  void attachDepthTexture2D(
    JCGLTexture2DUsableType t);

  /**
   * <p>Attach the given depth+stencil texture {@code texture} to the
   * framebuffer that will be created.</p>
   *
   * <p>The function will replace any existing depth attachment. This includes
   * individual depth and combined depth/stencil attachments.</p>
   *
   * @param t The texture.
   */

  void attachDepthStencilTexture2D(
    JCGLTexture2DUsableType t);

  /**
   * <p>Detach any existing depth attachment (including depth+stencil
   * attachments).</p>
   */

  void detachDepth();

  /**
   * <p>Detach any existing color attachment at {@code point}.</p>
   *
   * @param point The attachment point
   */

  void detachColorAttachment(
    JCGLFramebufferColorAttachmentPointType point);
}
