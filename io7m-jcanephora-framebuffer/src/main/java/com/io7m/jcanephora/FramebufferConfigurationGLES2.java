/*
 * Copyright © 2012 http://io7m.com
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
 * Instructions to create an ES2 compatible framebuffer configuration (using
 * OpenGL 3.0 features if available, but guaranteed to work on both ES2 and
 * 3.0).
 */

public interface FramebufferConfigurationGLES2
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

  @Nonnull Indeterminate<Framebuffer, FramebufferStatus> make(
    @Nonnull GLImplementation gi)
    throws GLException,
      ConstraintError;

  /**
   * <p>
   * Request the best available RGBA color renderbuffer in the resulting
   * framebuffer.
   * </p>
   * <p>
   * Note that this function is able to request RGBA renderbuffers of a better
   * quality than those given in the OpenGL ES2 specification <i>when not
   * running on an ES2 implementation</i>. This prevents programs from
   * effectively downgrading all framebuffers to OpenGL ES2 quality.
   * </p>
   * <p>
   * Essentially, the function will pick the "best" quality ES2 type when
   * running on ES2, and will pick the "best" quality OpenGL 3.0 type when
   * running on OpenGL 3.0.
   * </p>
   */

  void requestBestRGBAColorRenderbuffer();

  /**
   * <p>
   * Request the best available RGBA color 2D texture in the resulting
   * framebuffer.
   * </p>
   * <p>
   * Note that this function is able to request RGBA textures of a better
   * quality than those given in the OpenGL ES2 specification <i>when not
   * running on an ES2 implementation</i>. This prevents programs from
   * effectively downgrading all textures to OpenGL ES2 quality.
   * </p>
   * <p>
   * Essentially, the function will pick the "best" quality ES2 type when
   * running on ES2, and will pick the "best" quality OpenGL 3.0 type when
   * running on OpenGL 3.0.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   */

  void requestBestRGBAColorTexture2D(
    @Nonnull TextureWrapS texture_wrap_s,
    @Nonnull TextureWrapT texture_wrap_t,
    @Nonnull TextureFilterMinification texture_min_filter,
    @Nonnull TextureFilterMagnification texture_mag_filter)
    throws ConstraintError;

  /**
   * <p>
   * Request the best available RGBA color cube-map texture in the resulting
   * framebuffer.
   * </p>
   * <p>
   * Note that this function is able to request RGBA textures of a better
   * quality than those given in the OpenGL ES2 specification <i>when not
   * running on an ES2 implementation</i>. This prevents programs from
   * effectively downgrading all textures to OpenGL ES2 quality.
   * </p>
   * <p>
   * Essentially, the function will pick the "best" quality ES2 type when
   * running on ES2, and will pick the "best" quality OpenGL 3.0 type when
   * running on OpenGL 3.0.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>, or if the
   *           framebuffer width is not equal to its height (cube maps must be
   *           square).
   */

  void requestBestRGBAColorTextureCube(
    @Nonnull TextureWrapR texture_wrap_r,
    @Nonnull TextureWrapS texture_wrap_s,
    @Nonnull TextureWrapT texture_wrap_t,
    @Nonnull TextureFilterMinification texture_min_filter,
    @Nonnull TextureFilterMagnification texture_mag_filter)
    throws ConstraintError;

  /**
   * <p>
   * Request the best available RGB color renderbuffer in the resulting
   * framebuffer.
   * </p>
   * <p>
   * Note that this function is able to request RGB renderbuffers of a better
   * quality than those given in the OpenGL ES2 specification <i>when not
   * running on an ES2 implementation</i>. This prevents programs from
   * effectively downgrading all framebuffers to OpenGL ES2 quality.
   * </p>
   * <p>
   * Essentially, the function will pick the "best" quality ES2 type when
   * running on ES2, and will pick the "best" quality OpenGL 3.0 type when
   * running on OpenGL 3.0.
   * </p>
   */

  void requestBestRGBColorRenderbuffer();

  /**
   * <p>
   * Request the best available RGB color 2D texture in the resulting
   * framebuffer.
   * </p>
   * <p>
   * Note that this function is able to request RGB textures of a better
   * quality than those given in the OpenGL ES2 specification <i>when not
   * running on an ES2 implementation</i>. This prevents programs from
   * effectively downgrading all textures to OpenGL ES2 quality.
   * </p>
   * <p>
   * Essentially, the function will pick the "best" quality ES2 type when
   * running on ES2, and will pick the "best" quality OpenGL 3.0 type when
   * running on OpenGL 3.0.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   */

  void requestBestRGBColorTexture2D(
    @Nonnull TextureWrapS texture_wrap_s,
    @Nonnull TextureWrapT texture_wrap_t,
    @Nonnull TextureFilterMinification texture_min_filter,
    @Nonnull TextureFilterMagnification texture_mag_filter)
    throws ConstraintError;

  /**
   * <p>
   * Request the best available RGB color cube-map texture in the resulting
   * framebuffer.
   * </p>
   * <p>
   * Note that this function is able to request RGB textures of a better
   * quality than those given in the OpenGL ES2 specification <i>when not
   * running on an ES2 implementation</i>. This prevents programs from
   * effectively downgrading all textures to OpenGL ES2 quality.
   * </p>
   * <p>
   * Essentially, the function will pick the "best" quality ES2 type when
   * running on ES2, and will pick the "best" quality OpenGL 3.0 type when
   * running on OpenGL 3.0.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>, or if the
   *           framebuffer width is not equal to its height (cube maps must be
   *           square).
   */

  void requestBestRGBColorTextureCube(
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
   * Request no depth buffer in the resulting framebuffer.
   * </p>
   * <p>
   * Due to limitations in OpenGL drivers, requesting a depth buffer may also
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
   * Request that the resulting framebuffer share the color buffer of
   * <code>framebuffer</code> at attachment point <code>attachment</code>.
   * </p>
   * 
   * @param framebuffer
   *          The source framebuffer
   * @param attachment
   *          The attachment point
   * @throws ConstraintError
   *           If any of the following hold:
   *           <ul>
   *           <li>Any of the parameters are <code>null</code></li>
   *           <li>If <code>framebuffer</code> has no color attachment at
   *           <code>attachment</code></li>
   *           </ul>
   */

  void requestSharedColor(
    @Nonnull FramebufferUsable framebuffer,
    @Nonnull FramebufferColorAttachmentPoint attachment)
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
   * Due to limitations in OpenGL drivers, requesting a stencil buffer may
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
