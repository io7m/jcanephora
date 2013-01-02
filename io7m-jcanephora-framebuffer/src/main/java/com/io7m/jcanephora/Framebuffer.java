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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Pair;

/**
 * A created framebuffer, with all associated textures and/or renderbuffers.
 */

public final class Framebuffer implements GLResource
{
  private final @Nonnull FramebufferReference                                                           framebuffer;
  private final int                                                                                     width;
  private final int                                                                                     height;
  private final boolean                                                                                 is_deleted;

  private final @Nonnull TreeSet<FramebufferColorAttachmentPoint>                                       color_shared;
  private final @Nonnull HashMap<FramebufferColorAttachmentPoint, Texture2DStatic>                      color_textures_2d;
  private final @Nonnull HashMap<FramebufferColorAttachmentPoint, Pair<TextureCubeStatic, CubeMapFace>> color_textures_cube;
  private final @Nonnull HashMap<FramebufferColorAttachmentPoint, Renderbuffer>                         color_buffers;

  private boolean                                                                                       depth_buffer_shared;
  private @CheckForNull Renderbuffer                                                                    depth_buffer;

  private @CheckForNull Renderbuffer                                                                    stencil_buffer;
  private boolean                                                                                       stencil_buffer_shared;

  Framebuffer(
    final @Nonnull FramebufferReference framebuffer,
    final int width,
    final int height)
  {
    this.framebuffer = framebuffer;
    this.width = width;
    this.height = height;

    this.color_shared = new TreeSet<FramebufferColorAttachmentPoint>();
    this.color_buffers =
      new HashMap<FramebufferColorAttachmentPoint, Renderbuffer>();
    this.color_textures_2d =
      new HashMap<FramebufferColorAttachmentPoint, Texture2DStatic>();
    this.color_textures_cube =
      new HashMap<FramebufferColorAttachmentPoint, Pair<TextureCubeStatic, CubeMapFace>>();

    this.depth_buffer = null;
    this.depth_buffer_shared = false;
    this.stencil_buffer = null;
    this.stencil_buffer_shared = false;
    this.is_deleted = false;
  }

  void configAddColorRenderbuffer(
    final @Nonnull FramebufferColorAttachmentPoint attachment_point,
    final @Nonnull Renderbuffer buffer,
    final boolean shared)
    throws ConstraintError
  {
    Constraints.constrainArbitrary(
      this.color_buffers.containsKey(attachment_point) == false,
      "No color buffer at attachment point");
    Constraints.constrainArbitrary(
      this.color_textures_2d.containsKey(attachment_point) == false,
      "No color 2D texture at attachment point");
    Constraints.constrainArbitrary(
      this.color_textures_cube.containsKey(attachment_point) == false,
      "No color cube-map texture at attachment point");

    this.color_buffers.put(attachment_point, buffer);

    if (shared) {
      this.color_shared.add(attachment_point);
    }
  }

  void configAddColorTexture2D(
    final @Nonnull FramebufferColorAttachmentPoint attachment_point,
    final @Nonnull Texture2DStatic texture,
    final boolean shared)
    throws ConstraintError
  {
    Constraints.constrainArbitrary(
      this.color_buffers.containsKey(attachment_point) == false,
      "No color buffer at attachment point");
    Constraints.constrainArbitrary(
      this.color_textures_2d.containsKey(attachment_point) == false,
      "No color 2D texture at attachment point");
    Constraints.constrainArbitrary(
      this.color_textures_cube.containsKey(attachment_point) == false,
      "No color cube-map texture at attachment point");

    this.color_textures_2d.put(attachment_point, texture);

    if (shared) {
      this.color_shared.add(attachment_point);
    }
  }

  void configAddColorTextureCube(
    final @Nonnull FramebufferColorAttachmentPoint attachment_point,
    final @Nonnull CubeMapFace current_face,
    final @Nonnull TextureCubeStatic texture,
    final boolean shared)
    throws ConstraintError
  {
    Constraints.constrainArbitrary(
      this.color_buffers.containsKey(attachment_point) == false,
      "No color buffer at attachment point");
    Constraints.constrainArbitrary(
      this.color_textures_2d.containsKey(attachment_point) == false,
      "No color 2D texture at attachment point");
    Constraints.constrainArbitrary(
      this.color_textures_cube.containsKey(attachment_point) == false,
      "No color cube-map texture at attachment point");

    final Pair<TextureCubeStatic, CubeMapFace> pair =
      new Pair<TextureCubeStatic, CubeMapFace>(texture, current_face);
    this.color_textures_cube.put(attachment_point, pair);

    if (shared) {
      this.color_shared.add(attachment_point);
    }
  }

  void configSetDepthBuffer(
    final @Nonnull Renderbuffer buffer,
    final boolean shared)
    throws ConstraintError
  {
    Constraints.constrainArbitrary(
      this.depth_buffer == null,
      "No depth buffer set");

    this.depth_buffer = buffer;
    this.depth_buffer_shared = shared;
  }

  void configSetDepthStencilBuffer(
    final @Nonnull Renderbuffer buffer,
    final boolean shared)
    throws ConstraintError
  {
    Constraints.constrainArbitrary(
      this.depth_buffer == null,
      "No depth buffer set");
    Constraints.constrainArbitrary(
      this.stencil_buffer == null,
      "No stencil buffer set");

    this.depth_buffer = buffer;
    this.depth_buffer_shared = shared;
    this.stencil_buffer = buffer;
    this.stencil_buffer_shared = shared;
  }

  void configSetStencilBuffer(
    final @Nonnull Renderbuffer buffer,
    final boolean shared)
    throws ConstraintError
  {
    Constraints.constrainArbitrary(
      this.stencil_buffer == null,
      "No stencil buffer set");

    this.stencil_buffer = buffer;
    this.stencil_buffer_shared = shared;
  }

  public @Nonnull
    Map<FramebufferColorAttachmentPoint, Renderbuffer>
    getColorBufferAttachments()
  {
    return Collections.unmodifiableMap(this.color_buffers);
  }

  public @Nonnull
    Map<FramebufferColorAttachmentPoint, Texture2DStatic>
    getColorTexture2DAttachments()
  {
    return Collections.unmodifiableMap(this.color_textures_2d);
  }

  public @Nonnull
    Map<FramebufferColorAttachmentPoint, Pair<TextureCubeStatic, CubeMapFace>>
    getColorTextureCubeAttachments()
  {
    return Collections.unmodifiableMap(this.color_textures_cube);
  }

  public @Nonnull Renderbuffer getDepthBufferAttachment()
    throws ConstraintError
  {
    Constraints.constrainArbitrary(
      this.hasDepthBufferAttachment(),
      "Has depth buffer");
    return this.depth_buffer;
  }

  public @Nonnull FramebufferReference getFramebuffer()
  {
    return this.framebuffer;
  }

  public int getHeight()
  {
    return this.height;
  }

  public @Nonnull Renderbuffer getStencilBufferAttachment()
    throws ConstraintError
  {
    Constraints.constrainArbitrary(
      this.hasStencilBufferAttachment(),
      "Has stencil buffer");
    return this.stencil_buffer;
  }

  public int getWidth()
  {
    return this.width;
  }

  public boolean hasDepthBufferAttachment()
  {
    return this.depth_buffer != null;
  }

  public boolean hasStencilBufferAttachment()
  {
    return this.stencil_buffer != null;
  }

  @Override public <G extends GLInterfaceES2> void resourceDelete(
    final @Nonnull G gl)
    throws ConstraintError,
      GLException
  {
    for (final Entry<FramebufferColorAttachmentPoint, Renderbuffer> e : this.color_buffers
      .entrySet()) {
      if (this.color_shared.contains(e.getKey()) == false) {
        gl.renderbufferDelete(e.getValue());
      }
    }

    for (final Entry<FramebufferColorAttachmentPoint, Texture2DStatic> e : this.color_textures_2d
      .entrySet()) {
      if (this.color_shared.contains(e.getKey()) == false) {
        gl.texture2DStaticDelete(e.getValue());
      }
    }

    for (final Entry<FramebufferColorAttachmentPoint, Pair<TextureCubeStatic, CubeMapFace>> e : this.color_textures_cube
      .entrySet()) {
      if (this.color_shared.contains(e.getKey()) == false) {
        gl.textureCubeStaticDelete(e.getValue().first);
      }
    }

    if (this.depth_buffer != null) {
      if (this.depth_buffer_shared == false) {
        gl.renderbufferDelete(this.depth_buffer);
      }
    }

    if (this.stencil_buffer != null) {
      if (this.stencil_buffer_shared == false) {
        /**
         * With combined depth/stencil buffers, deleting the depth buffer
         * above may have deleted the stencil buffer.
         */
        if (this.stencil_buffer.resourceIsDeleted() == false) {
          gl.renderbufferDelete(this.stencil_buffer);
        }
      }
    }

    gl.framebufferDelete(this.framebuffer);
  }

  @Override public boolean resourceIsDeleted()
  {
    return this.is_deleted;
  }
}
