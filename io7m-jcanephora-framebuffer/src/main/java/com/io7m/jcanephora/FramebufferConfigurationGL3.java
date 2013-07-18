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

package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Indeterminate;

/**
 * Instructions to create an OpenGL 3.0 compatible framebuffer.
 */

public interface FramebufferConfigurationGL3
{
  /**
   * Retrieve the height of the resulting framebuffer.
   */

  int getHeight();

  /**
   * Retrieve the width of the resulting framebuffer.
   */

  int getWidth();

  /**
   * Construct a framebuffer based on the current configuration.
   * 
   * @throws ConstraintError
   *           Iff <code>gi == null</code>.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  @Nonnull
    <G extends GLFramebuffersGL3 & GLTextures2DStaticCommon & GLTexturesCubeStaticCommon & GLRenderbuffersGL3>
    Indeterminate<Framebuffer, FramebufferStatus>
    make(
      @Nonnull G gl)
      throws GLException,
        ConstraintError;

  /**
   * <p>
   * Request the best available RGBA color renderbuffer in the resulting
   * framebuffer, attached at attachment point <code>point</code>, with
   * fragment shader output (draw buffer) <code>buffer</code> mapped to use
   * the resulting attachment.
   * </p>
   * 
   * @throws ConstraintError
   *           If any of the following hold:
   *           <ul>
   *           <li>Any of the parameters are <code>null</code></li>
   *           <li>Another color attachment at a different attachment point is
   *           already mapped to <code>buffer</code></li>
   *           </ul>
   */

  void requestBestRGBAColorRenderbuffer(
    @Nonnull FramebufferColorAttachmentPoint point,
    @Nonnull FramebufferDrawBuffer buffer)
    throws ConstraintError;

  /**
   * <p>
   * Request the best available RGBA color 2D texture in the resulting
   * framebuffer, attached at attachment point <code>point</code>, with
   * fragment shader output (draw buffer) <code>buffer</code> mapped to use
   * the resulting attachment.
   * </p>
   * 
   * @throws ConstraintError
   *           If any of the following hold:
   *           <ul>
   *           <li>Any of the parameters are <code>null</code></li>
   *           <li>Another color attachment at a different attachment point is
   *           already mapped to <code>buffer</code></li>
   *           </ul>
   */

  void requestBestRGBAColorTexture2D(
    @Nonnull FramebufferColorAttachmentPoint point,
    @Nonnull FramebufferDrawBuffer buffer,
    @Nonnull TextureWrapS texture_wrap_s,
    @Nonnull TextureWrapT texture_wrap_t,
    @Nonnull TextureFilterMinification texture_min_filter,
    @Nonnull TextureFilterMagnification texture_mag_filter)
    throws ConstraintError;

  /**
   * <p>
   * Request the best available RGBA color cube-map texture in the resulting
   * framebuffer, attached at attachment point <code>point</code>, with
   * fragment shader output (draw buffer) <code>buffer</code> mapped to use
   * the resulting attachment.
   * </p>
   * 
   * @throws ConstraintError
   *           If any of the following hold:
   *           <ul>
   *           <li>Any of the parameters are <code>null</code></li>
   *           <li>Another color attachment at a different attachment point is
   *           already mapped to <code>buffer</code></li>
   *           <li>
   *           The framebuffer width is not equal to its height (cube maps
   *           must be square)</li>
   *           </ul>
   */

  void requestBestRGBAColorTextureCube(
    @Nonnull FramebufferColorAttachmentPoint point,
    @Nonnull FramebufferDrawBuffer buffer,
    @Nonnull TextureWrapR texture_wrap_r,
    @Nonnull TextureWrapS texture_wrap_s,
    @Nonnull TextureWrapT texture_wrap_t,
    @Nonnull TextureFilterMinification texture_min_filter,
    @Nonnull TextureFilterMagnification texture_mag_filter)
    throws ConstraintError;

  /**
   * <p>
   * Request the best available RGB color renderbuffer in the resulting
   * framebuffer, attached at attachment point <code>point</code>, with
   * fragment shader output (draw buffer) <code>buffer</code> mapped to use
   * the resulting attachment.
   * </p>
   * 
   * @throws ConstraintError
   *           If any of the following hold:
   *           <ul>
   *           <li>Any of the parameters are <code>null</code></li>
   *           <li>Another color attachment at a different attachment point is
   *           already mapped to <code>buffer</code></li>
   *           </ul>
   */

  void requestBestRGBColorRenderbuffer(
    @Nonnull FramebufferColorAttachmentPoint point,
    @Nonnull FramebufferDrawBuffer buffer)
    throws ConstraintError;

  /**
   * <p>
   * Request the best available RGB color 2D texture in the resulting
   * framebuffer, attached at attachment point <code>point</code>, with
   * fragment shader output (draw buffer) <code>buffer</code> mapped to use
   * the resulting attachment.
   * </p>
   * 
   * @throws ConstraintError
   *           If any of the following hold:
   *           <ul>
   *           <li>Any of the parameters are <code>null</code></li>
   *           <li>Another color attachment at a different attachment point is
   *           already mapped to <code>buffer</code></li>
   *           </ul>
   */

  void requestBestRGBColorTexture2D(
    @Nonnull FramebufferColorAttachmentPoint point,
    @Nonnull FramebufferDrawBuffer buffer,
    @Nonnull TextureWrapS texture_wrap_s,
    @Nonnull TextureWrapT texture_wrap_t,
    @Nonnull TextureFilterMinification texture_min_filter,
    @Nonnull TextureFilterMagnification texture_mag_filter)
    throws ConstraintError;

  /**
   * <p>
   * Request the best available RGB color cube-map texture in the resulting
   * framebuffer, attached at attachment point <code>point</code>, with
   * fragment shader output (draw buffer) <code>buffer</code> mapped to use
   * the resulting attachment.
   * </p>
   * 
   * @throws ConstraintError
   *           If any of the following hold:
   *           <ul>
   *           <li>Any of the parameters are <code>null</code></li>
   *           <li>Another color attachment at a different attachment point is
   *           already mapped to <code>buffer</code></li>
   *           <li>
   *           The framebuffer width is not equal to its height (cube maps
   *           must be square)</li>
   *           </ul>
   */

  void requestBestRGBColorTextureCube(
    @Nonnull FramebufferColorAttachmentPoint point,
    @Nonnull FramebufferDrawBuffer buffer,
    @Nonnull TextureWrapR texture_wrap_r,
    @Nonnull TextureWrapS texture_wrap_s,
    @Nonnull TextureWrapT texture_wrap_t,
    @Nonnull TextureFilterMinification texture_min_filter,
    @Nonnull TextureFilterMagnification texture_mag_filter)
    throws ConstraintError;

  /**
   * <p>
   * Request a depth renderbuffer in the resulting framebuffer.
   * </p>
   */

  void requestDepthRenderbuffer();

  /**
   * <p>
   * Remove the request to create any color attachments, if any.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff <code>point == null</code>.
   */

  void requestNoColor()
    throws ConstraintError;

  /**
   * <p>
   * Remove the request to create a color attachment at attachment point
   * <code>point</code>, if any.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff <code>point == null</code>.
   */

  void requestNoColorAt(
    @Nonnull FramebufferColorAttachmentPoint point)
    throws ConstraintError;

  /**
   * <p>
   * Request no depth buffer in the resulting framebuffer.
   * </p>
   * <p>
   * Due to limitations in OpenGL drivers, requesting a depth buffer will also
   * request a stencil buffer, regardless of whether or not one was desired.
   * </p>
   */

  void requestNoDepth();

  /**
   * <p>
   * Request no stencil buffer in the resulting framebuffer.
   * </p>
   */

  void requestNoStencil();

  /**
   * <p>
   * Request to share the color attachment at <code>source_point</code> of
   * <code>source</code>, attaching it at <code>point</code> to the resulting
   * framebuffer. Fragment shader output (draw buffer) <code>buffer</code> is
   * mapped to use the resulting attachment.
   * </p>
   * 
   * @param point
   *          The point on the resulting framebuffer at which to attach the
   *          shared color attachment
   * @param buffer
   *          The fragment shader (draw buffer) to which to map to the
   *          resulting attachment
   * @param source
   *          The source framebuffer
   * @param source_point
   *          The attachment point on the source framebuffer
   * @throws ConstraintError
   *           If any of the following hold:
   *           <ul>
   *           <li>Any of the parameters are <code>null</code></li>
   *           <li>If <code>source</code> has no color attachment at
   *           <code>source_point</code></li>
   *           </ul>
   */

  void requestSharedColor(
    @Nonnull FramebufferColorAttachmentPoint point,
    @Nonnull FramebufferDrawBuffer buffer,
    @Nonnull FramebufferUsable source,
    @Nonnull FramebufferColorAttachmentPoint source_point)
    throws ConstraintError;

  /**
   * <p>
   * Request that the resulting framebuffer share the depth attachment of
   * <code>framebuffer</code>.
   * </p>
   * <p>
   * Note that on most systems, requesting a shared depth attachment will also
   * result in a shared stencil attachment.
   * </p>
   * 
   * @param framebuffer
   *          The source framebuffer
   * @throws ConstraintError
   *           If any of the following hold:
   *           <ul>
   *           <li>Any of the parameters are <code>null</code></li>
   *           <li>If <code>framebuffer</code> has no depth attachment</li>
   *           </ul>
   */

  void requestSharedDepth(
    @Nonnull Framebuffer framebuffer)
    throws ConstraintError;

  /**
   * <p>
   * Request that the resulting framebuffer share the stencil attachment of
   * <code>framebuffer</code>.
   * </p>
   * <p>
   * Note that on most systems, requesting a shared stencil attachment will
   * also result in a shared depth attachment.
   * </p>
   * 
   * @param framebuffer
   *          The source framebuffer
   * @throws ConstraintError
   *           If any of the following hold:
   *           <ul>
   *           <li>Any of the parameters are <code>null</code></li>
   *           <li>If <code>framebuffer</code> has no stencil attachment</li>
   *           </ul>
   */

  void requestSharedStencil(
    @Nonnull Framebuffer framebuffer)
    throws ConstraintError;

  /**
   * <p>
   * Request a stencil renderbuffer in the resulting framebuffer.
   * </p>
   * <p>
   * Due to limitations in OpenGL drivers, requesting a stencil buffer will
   * also request a depth buffer, regardless of whether or not one was
   * desired.
   * </p>
   */

  void requestStencilRenderbuffer();

  /**
   * Set the height of the resulting framebuffer.
   * 
   * @throws ConstraintError
   *           Iff <code>0 &lt; height &lt;= Integer.MAX_VALUE</code>
   */

  void setHeight(
    int height)
    throws ConstraintError;

  /**
   * Set the width of the resulting framebuffer.
   * 
   * @throws ConstraintError
   *           Iff <code>0 &lt; width &lt;= Integer.MAX_VALUE</code>
   */

  void setWidth(
    int width)
    throws ConstraintError;
}
