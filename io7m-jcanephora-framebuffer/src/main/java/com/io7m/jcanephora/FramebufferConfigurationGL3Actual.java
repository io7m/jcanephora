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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jaux.functional.Indeterminate;
import com.io7m.jaux.functional.Indeterminate.Failure;
import com.io7m.jaux.functional.Indeterminate.Success;
import com.io7m.jcanephora.AttachmentColor.AttachmentColorRenderbuffer;
import com.io7m.jcanephora.AttachmentColor.AttachmentColorTexture2DStatic;
import com.io7m.jcanephora.AttachmentColor.AttachmentColorTextureCubeStatic;
import com.io7m.jcanephora.AttachmentColor.AttachmentSharedColorRenderbuffer;
import com.io7m.jcanephora.AttachmentColor.AttachmentSharedColorTexture2DStatic;
import com.io7m.jcanephora.AttachmentColor.AttachmentSharedColorTextureCubeStatic;
import com.io7m.jcanephora.AttachmentDepth.AttachmentDepthRenderbuffer;
import com.io7m.jcanephora.AttachmentDepth.AttachmentDepthStencilRenderbuffer;
import com.io7m.jcanephora.AttachmentDepth.AttachmentSharedDepthRenderbuffer;
import com.io7m.jcanephora.AttachmentDepth.AttachmentSharedDepthStencilRenderbuffer;
import com.io7m.jcanephora.AttachmentStencil.AttachmentSharedStencilRenderbuffer;
import com.io7m.jcanephora.AttachmentStencil.AttachmentStencilAsDepthStencil;
import com.io7m.jcanephora.AttachmentStencil.AttachmentStencilRenderbuffer;

/**
 * Instructions to create an OpenGL 3.0 compatible framebuffer.
 */

@NotThreadSafe public final class FramebufferConfigurationGL3Actual implements
  FramebufferConfigurationGL3
{
  private static class ColorRequest
  {
    @Nonnull RequestColor                                    want_color;
    @CheckForNull AttachmentColor                            want_color_shared;
    public TextureWrapR                                      wrap_r;
    public TextureWrapS                                      wrap_s;
    public TextureWrapT                                      wrap_t;
    public TextureFilterMinification                         min_filter;
    public TextureFilterMagnification                        mag_filter;
    @SuppressWarnings("unused") public FramebufferDrawBuffer draw_buffer;

    ColorRequest()
    {
      this.want_color = RequestColor.WANT_NOTHING;
    }
  }

  private static enum RequestColor
  {
    WANT_NOTHING,
    WANT_COLOR_RGB_RENDERBUFFER,
    WANT_COLOR_RGBA_RENDERBUFFER,
    WANT_COLOR_RGB_TEXTURE_2D,
    WANT_COLOR_RGBA_TEXTURE_2D,
    WANT_COLOR_SHARED_WITH,
    WANT_COLOR_RGB_TEXTURE_CUBE,
    WANT_COLOR_RGBA_TEXTURE_CUBE
  }

  private static enum RequestDepth
  {
    WANT_NOTHING,
    WANT_DEPTH_RENDERBUFFER,
    WANT_DEPTH_SHARED_WITH
  }

  private static enum RequestStencil
  {
    WANT_NOTHING,
    WANT_STENCIL_RENDERBUFFER,
    WANT_STENCIL_SHARED_WITH
  }

  private static class WorkingBuffers
  {
    @CheckForNull FramebufferReference                             framebuffer;
    @Nonnull Map<FramebufferColorAttachmentPoint, AttachmentColor> attachments_color;
    @CheckForNull AttachmentDepth                                  attachment_depth;
    @CheckForNull AttachmentStencil                                attachment_stencil;
    final @Nonnull StringBuilder                                   text;

    WorkingBuffers()
    {
      this.text = new StringBuilder();
      this.attachments_color =
        new HashMap<FramebufferColorAttachmentPoint, AttachmentColor>();
    }

      <G extends JCGLFramebuffersGL3 & JCGLRenderbuffersGL3 & JCGLTextures2DStaticCommon & JCGLTexturesCubeStaticCommon>
      void
      emergencyCleanup(
        final @Nonnull G gl)
        throws ConstraintError,
          JCGLException
    {
      JCGLException saved = null;

      if (this.framebuffer != null) {
        try {
          gl.framebufferDelete(this.framebuffer);
        } catch (final JCGLException e) {
          saved = e;
        }
      }

      /**
       * Delete unshared color attachments.
       */

      for (final AttachmentColor attach : this.attachments_color.values()) {
        switch (attach.type) {
          case ATTACHMENT_COLOR_RENDERBUFFER:
          {
            final AttachmentColorRenderbuffer a =
              (AttachmentColorRenderbuffer) attach;
            gl.renderbufferDelete(a.getRenderbufferWritable());
            break;
          }
          case ATTACHMENT_COLOR_TEXTURE_2D:
          {
            final AttachmentColorTexture2DStatic a =
              (AttachmentColorTexture2DStatic) attach;
            gl.texture2DStaticDelete(a.getTextureWritable());
            break;
          }
          case ATTACHMENT_COLOR_TEXTURE_CUBE:
          {
            final AttachmentColorTextureCubeStatic a =
              (AttachmentColorTextureCubeStatic) attach;
            gl.textureCubeStaticDelete(a.getTextureWritable());
            break;
          }
          case ATTACHMENT_SHARED_COLOR_RENDERBUFFER:
          case ATTACHMENT_SHARED_COLOR_TEXTURE_2D:
          case ATTACHMENT_SHARED_COLOR_TEXTURE_CUBE:
          {
            break;
          }
        }
      }

      /**
       * Delete unshared depth attachments.
       */

      if (this.attachment_depth != null) {
        switch (this.attachment_depth.type) {
          case ATTACHMENT_DEPTH_RENDERBUFFER:
          {
            final AttachmentDepthRenderbuffer a =
              (AttachmentDepthRenderbuffer) this.attachment_depth;
            gl.renderbufferDelete(a.getRenderbufferWritable());
            break;
          }
          case ATTACHMENT_DEPTH_STENCIL_RENDERBUFFER:
          {
            final AttachmentDepthStencilRenderbuffer a =
              (AttachmentDepthStencilRenderbuffer) this.attachment_depth;
            gl.renderbufferDelete(a.getRenderbufferWritable());
            break;
          }
          case ATTACHMENT_SHARED_DEPTH_RENDERBUFFER:
          case ATTACHMENT_SHARED_DEPTH_STENCIL_RENDERBUFFER:
          {
            break;
          }
        }
      }

      /**
       * Delete unshared stencil attachments.
       */

      if (this.attachment_stencil != null) {
        switch (this.attachment_stencil.type) {
          case ATTACHMENT_STENCIL_RENDERBUFFER:
          {
            final AttachmentStencilRenderbuffer a =
              (AttachmentStencilRenderbuffer) this.attachment_stencil;
            gl.renderbufferDelete(a.getRenderbufferWritable());
            break;
          }
          case ATTACHMENT_SHARED_STENCIL_RENDERBUFFER:
          case ATTACHMENT_STENCIL_AS_DEPTH_STENCIL:
          {
            break;
          }
        }
      }

      if (saved != null) {
        throw saved;
      }
    }
  }

  /**
   * Attach all allocated buffers to the current framebuffer on OpenGL 3.0.
   * 
   * @throws JCGLException
   * @throws ConstraintError
   */

  private static
    <G extends JCGLRenderbuffersGL3 & JCGLFramebuffersGL3>
    void
    attachBuffers(
      final @Nonnull WorkingBuffers buffers,
      final @Nonnull G gl)
      throws JCGLException,
        ConstraintError
  {
    gl.framebufferDrawBind(buffers.framebuffer);

    FramebufferConfigurationGL3Actual.attachColorBuffers(buffers, gl);
    FramebufferConfigurationGL3Actual.attachDepthBuffers_GL3(buffers, gl);
    FramebufferConfigurationGL3Actual.attachStencilBuffers(buffers, gl);
  }

  private static void attachColorBuffers(
    final WorkingBuffers buffers,
    final JCGLFramebuffersGL3 gl)
    throws JCGLException,
      ConstraintError
  {
    for (final Entry<FramebufferColorAttachmentPoint, AttachmentColor> e : buffers.attachments_color
      .entrySet()) {

      final FramebufferColorAttachmentPoint point = e.getKey();
      final AttachmentColor attach = e.getValue();

      switch (attach.type) {
        case ATTACHMENT_SHARED_COLOR_RENDERBUFFER:
        {
          final AttachmentSharedColorRenderbuffer a =
            (AttachmentSharedColorRenderbuffer) attach;

          gl.framebufferDrawAttachColorRenderbufferAt(
            buffers.framebuffer,
            point,
            a.getRenderbuffer());
          break;
        }
        case ATTACHMENT_COLOR_RENDERBUFFER:
        {
          final AttachmentColorRenderbuffer a =
            (AttachmentColorRenderbuffer) attach;

          gl.framebufferDrawAttachColorRenderbufferAt(
            buffers.framebuffer,
            point,
            a.getRenderbufferWritable());
          break;
        }
        case ATTACHMENT_SHARED_COLOR_TEXTURE_2D:
        {
          final AttachmentSharedColorTexture2DStatic a =
            (AttachmentSharedColorTexture2DStatic) attach;

          gl.framebufferDrawAttachColorTexture2DAt(
            buffers.framebuffer,
            point,
            a.getTexture2D());
          break;
        }
        case ATTACHMENT_COLOR_TEXTURE_2D:
        {
          final AttachmentColorTexture2DStatic a =
            (AttachmentColorTexture2DStatic) attach;

          gl.framebufferDrawAttachColorTexture2DAt(
            buffers.framebuffer,
            point,
            a.getTextureWritable());
          break;
        }
        case ATTACHMENT_SHARED_COLOR_TEXTURE_CUBE:
        {
          final AttachmentSharedColorTextureCubeStatic a =
            (AttachmentSharedColorTextureCubeStatic) attach;

          gl.framebufferDrawAttachColorTextureCubeAt(
            buffers.framebuffer,
            point,
            a.getTextureCube(),
            CubeMapFaceLH.CUBE_MAP_LH_POSITIVE_X);
          break;
        }
        case ATTACHMENT_COLOR_TEXTURE_CUBE:
        {
          final AttachmentColorTextureCubeStatic a =
            (AttachmentColorTextureCubeStatic) attach;
          gl.framebufferDrawAttachColorTextureCubeAt(
            buffers.framebuffer,
            point,
            a.getTextureWritable(),
            CubeMapFaceLH.CUBE_MAP_LH_POSITIVE_X);
          break;
        }
      }
    }
  }

  private static void attachDepthBuffers_GL3(
    final WorkingBuffers buffers,
    final JCGLFramebuffersGL3 gl)
    throws JCGLException,
      ConstraintError
  {
    if (buffers.attachment_depth != null) {
      switch (buffers.attachment_depth.type) {
        case ATTACHMENT_SHARED_DEPTH_RENDERBUFFER:
        case ATTACHMENT_DEPTH_RENDERBUFFER:
        {
          /**
           * Packed depth/stencil buffers are required by OpenGL 3.0, so
           * allocated/shared attachment can't be a single depth renderbuffer.
           */

          throw new UnreachableCodeException();
        }
        case ATTACHMENT_DEPTH_STENCIL_RENDERBUFFER:
        {
          final AttachmentDepthStencilRenderbuffer a =
            (AttachmentDepthStencilRenderbuffer) buffers.attachment_depth;

          gl.framebufferDrawAttachDepthStencilRenderbuffer(
            buffers.framebuffer,
            a.getRenderbufferWritable());
          break;
        }
        case ATTACHMENT_SHARED_DEPTH_STENCIL_RENDERBUFFER:
        {
          final AttachmentSharedDepthStencilRenderbuffer a =
            (AttachmentSharedDepthStencilRenderbuffer) buffers.attachment_depth;

          gl.framebufferDrawAttachDepthStencilRenderbuffer(
            buffers.framebuffer,
            a.getRenderbuffer());
          break;
        }
      }
    }
  }

  private static
    <G extends JCGLRenderbuffersGL3 & JCGLFramebuffersGL3>
    void
    attachStencilBuffers(
      final WorkingBuffers buffers,
      final G gl)
      throws JCGLException,
        ConstraintError
  {
    if (buffers.attachment_stencil != null) {
      switch (buffers.attachment_stencil.type) {
        case ATTACHMENT_SHARED_STENCIL_RENDERBUFFER:
        {
          final AttachmentSharedStencilRenderbuffer a =
            (AttachmentSharedStencilRenderbuffer) buffers.attachment_stencil;

          gl.framebufferDrawAttachStencilRenderbuffer(
            buffers.framebuffer,
            a.getRenderbuffer());
          break;
        }
        case ATTACHMENT_STENCIL_RENDERBUFFER:
        {
          final AttachmentStencilRenderbuffer a =
            (AttachmentStencilRenderbuffer) buffers.attachment_stencil;

          gl.framebufferDrawAttachStencilRenderbuffer(
            buffers.framebuffer,
            a.getRenderbufferWritable());
          break;
        }
        case ATTACHMENT_STENCIL_AS_DEPTH_STENCIL:
        {
          break;
        }
      }
    }
  }

  private static void makeTexture2DName(
    final @Nonnull WorkingBuffers buffers,
    final @Nonnull FramebufferColorAttachmentPoint point)
  {
    buffers.text.setLength(0);
    buffers.text.append("framebuffer-");
    buffers.text.append(buffers.framebuffer.getGLName());
    buffers.text.append("-texture2D-");
    buffers.text.append(point.getIndex());
  }

  private static void makeTextureCubeName(
    final @Nonnull WorkingBuffers buffers,
    final @Nonnull FramebufferColorAttachmentPoint point)
  {
    buffers.text.setLength(0);
    buffers.text.append("framebuffer-");
    buffers.text.append(buffers.framebuffer.getGLName());
    buffers.text.append("-textureCube-");
    buffers.text.append(point.getIndex());
  }

  private @Nonnull RequestDepth                                                      want_depth;
  private @CheckForNull AttachmentDepth                                              want_depth_shared;
  private @Nonnull RequestStencil                                                    want_stencil;
  private @CheckForNull AttachmentStencil                                            want_stencil_shared;
  private final @Nonnull Map<FramebufferColorAttachmentPoint, ColorRequest>          want_color;
  private final @Nonnull Map<FramebufferDrawBuffer, FramebufferColorAttachmentPoint> draw_buffers;
  private int                                                                        width;
  private int                                                                        height;

  /**
   * Construct a configuration that will, by default, not ask for any color,
   * depth, or stencil attachments.
   * 
   * The framebuffer will be of width <code>width</code> and height
   * <code>height</code>.
   * 
   * @throws ConstraintError
   *           Iff <code>width &lt; 1 || height &lt; 1</code>
   */

  public FramebufferConfigurationGL3Actual(
    final int width,
    final int height)
    throws ConstraintError
  {
    this.width =
      Constraints.constrainRange(width, 1, Integer.MAX_VALUE, "Width");
    this.height =
      Constraints.constrainRange(height, 1, Integer.MAX_VALUE, "Height");

    this.want_stencil = RequestStencil.WANT_NOTHING;
    this.want_stencil_shared = null;

    this.want_depth = RequestDepth.WANT_NOTHING;
    this.want_depth_shared = null;

    this.want_color =
      new HashMap<FramebufferColorAttachmentPoint, ColorRequest>();
    this.draw_buffers =
      new HashMap<FramebufferDrawBuffer, FramebufferColorAttachmentPoint>();
  }

  private void addDrawBufferMapping(
    final @Nonnull FramebufferDrawBuffer buffer,
    final @Nonnull FramebufferColorAttachmentPoint point)
    throws ConstraintError
  {
    if (this.draw_buffers.containsKey(buffer)) {
      final FramebufferColorAttachmentPoint existing =
        this.draw_buffers.get(buffer);
      Constraints.constrainArbitrary(
        existing.equals(point),
        "Draw buffer not mapped to a different color attachment");
    }

    this.draw_buffers.put(buffer, point);
  }

  private @Nonnull AttachmentColorRenderbuffer allocateBestRGBARenderbuffer(
    final @Nonnull JCGLRenderbuffersGL3 gl)
    throws JCGLException,
      ConstraintError
  {
    return new AttachmentColorRenderbuffer(gl.renderbufferAllocateRGBA8888(
      this.width,
      this.height));
  }

  private @Nonnull AttachmentColorTexture2DStatic allocateBestRGBATexture2D(
    final @Nonnull JCGLTextures2DStaticCommon gl,
    final @Nonnull String name,
    final @Nonnull ColorRequest req)
    throws JCGLException,
      ConstraintError
  {
    return new AttachmentColorTexture2DStatic(
      gl.texture2DStaticAllocateRGBA8888(
        name,
        this.width,
        this.height,
        req.wrap_s,
        req.wrap_t,
        req.min_filter,
        req.mag_filter));
  }

  private @Nonnull
    AttachmentColorTextureCubeStatic
    allocateBestRGBATextureCube(
      final @Nonnull JCGLTexturesCubeStaticCommon gl,
      final @Nonnull String name,
      final @Nonnull ColorRequest req)
      throws JCGLException,
        ConstraintError
  {
    return new AttachmentColorTextureCubeStatic(
      gl.textureCubeStaticAllocateRGBA8888(
        name,
        this.width,
        req.wrap_r,
        req.wrap_s,
        req.wrap_t,
        req.min_filter,
        req.mag_filter), CubeMapFaceLH.CUBE_MAP_LH_POSITIVE_X);
  }

  private @Nonnull AttachmentColorRenderbuffer allocateBestRGBRenderbuffer(
    final @Nonnull JCGLRenderbuffersGL3 gl)
    throws JCGLException,
      ConstraintError
  {
    return new AttachmentColorRenderbuffer(gl.renderbufferAllocateRGB888(
      this.width,
      this.height));
  }

  private @Nonnull AttachmentColorTexture2DStatic allocateBestRGBTexture2D(
    final @Nonnull JCGLTextures2DStaticCommon gl,
    final @Nonnull String name,
    final @Nonnull ColorRequest req)
    throws JCGLException,
      ConstraintError
  {
    return new AttachmentColorTexture2DStatic(
      gl.texture2DStaticAllocateRGB888(
        name,
        this.width,
        this.height,
        req.wrap_s,
        req.wrap_t,
        req.min_filter,
        req.mag_filter));
  }

  private @Nonnull
    AttachmentColorTextureCubeStatic
    allocateBestRGBTextureCube(
      final @Nonnull JCGLTexturesCubeStaticCommon gl,
      final @Nonnull String name,
      final @Nonnull ColorRequest req)
      throws JCGLException,
        ConstraintError
  {
    return new AttachmentColorTextureCubeStatic(
      gl.textureCubeStaticAllocateRGB888(
        name,
        this.width,
        req.wrap_r,
        req.wrap_s,
        req.wrap_t,
        req.min_filter,
        req.mag_filter), CubeMapFaceLH.CUBE_MAP_LH_POSITIVE_X);
  }

  private
    <G extends JCGLRenderbuffersGL3 & JCGLTextures2DStaticCommon & JCGLTexturesCubeStaticCommon>
    void
    allocateColorBuffers(
      final @Nonnull WorkingBuffers buffers,
      final @Nonnull G gl)
      throws JCGLException,
        ConstraintError
  {
    for (final Entry<FramebufferColorAttachmentPoint, ColorRequest> e : this.want_color
      .entrySet()) {

      final FramebufferColorAttachmentPoint point = e.getKey();
      final ColorRequest req = e.getValue();

      switch (req.want_color) {
        case WANT_COLOR_RGBA_RENDERBUFFER:
        {
          final AttachmentColorRenderbuffer attach =
            this.allocateBestRGBARenderbuffer(gl);
          buffers.attachments_color.put(point, attach);
          break;
        }
        case WANT_COLOR_RGBA_TEXTURE_2D:
        {
          FramebufferConfigurationGL3Actual.makeTexture2DName(buffers, point);
          final AttachmentColorTexture2DStatic attach =
            this.allocateBestRGBATexture2D(gl, buffers.text.toString(), req);
          buffers.attachments_color.put(point, attach);
          break;
        }
        case WANT_COLOR_RGBA_TEXTURE_CUBE:
        {
          FramebufferConfigurationGL3Actual.makeTextureCubeName(
            buffers,
            point);
          final AttachmentColorTextureCubeStatic attach =
            this
              .allocateBestRGBATextureCube(gl, buffers.text.toString(), req);
          buffers.attachments_color.put(point, attach);
          break;
        }
        case WANT_COLOR_RGB_RENDERBUFFER:
        {
          final AttachmentColorRenderbuffer attach =
            this.allocateBestRGBRenderbuffer(gl);
          buffers.attachments_color.put(point, attach);
          break;
        }
        case WANT_COLOR_RGB_TEXTURE_2D:
        {
          FramebufferConfigurationGL3Actual.makeTexture2DName(buffers, point);
          final AttachmentColorTexture2DStatic attach =
            this.allocateBestRGBTexture2D(gl, buffers.text.toString(), req);
          buffers.attachments_color.put(point, attach);
          break;
        }
        case WANT_COLOR_RGB_TEXTURE_CUBE:
        {
          FramebufferConfigurationGL3Actual.makeTextureCubeName(
            buffers,
            point);
          final AttachmentColorTextureCubeStatic attach =
            this.allocateBestRGBTextureCube(gl, buffers.text.toString(), req);
          buffers.attachments_color.put(point, attach);
          break;
        }
        case WANT_COLOR_SHARED_WITH:
        {
          buffers.attachments_color.put(point, req.want_color_shared);
          break;
        }
        case WANT_NOTHING:
        {
          throw new UnreachableCodeException();
        }
      }
    }
  }

  private <G extends JCGLRenderbuffersGL3> void allocateDepthStencil(
    final @Nonnull WorkingBuffers buffers,
    final @Nonnull G gl)
    throws JCGLException,
      ConstraintError
  {
    switch (this.want_stencil) {
      case WANT_NOTHING:
      {
        break;
      }
      case WANT_STENCIL_RENDERBUFFER:
      {
        break;
      }
      case WANT_STENCIL_SHARED_WITH:
      {
        buffers.attachment_depth = this.want_depth_shared;
        buffers.attachment_stencil = new AttachmentStencilAsDepthStencil();
        break;
      }
    }

    switch (this.want_depth) {
      case WANT_DEPTH_RENDERBUFFER:
      {
        buffers.attachment_depth =
          new AttachmentDepthStencilRenderbuffer(
            gl.renderbufferAllocateDepth24Stencil8(this.width, this.height));
        buffers.attachment_stencil = new AttachmentStencilAsDepthStencil();
        break;
      }
      case WANT_DEPTH_SHARED_WITH:
      {
        buffers.attachment_depth = this.want_depth_shared;
        buffers.attachment_stencil = new AttachmentStencilAsDepthStencil();
        break;
      }
      case WANT_NOTHING:
      {
        break;
      }
    }
  }

  @Override public boolean equals(
    final Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    final FramebufferConfigurationGL3Actual other =
      (FramebufferConfigurationGL3Actual) obj;
    if (this.height != other.height) {
      return false;
    }
    if (!this.want_color.equals(other.want_color)) {
      return false;
    }
    if (this.want_depth != other.want_depth) {
      return false;
    }
    if (this.want_depth_shared == null) {
      if (other.want_depth_shared != null) {
        return false;
      }
    } else if (!this.want_depth_shared.equals(other.want_depth_shared)) {
      return false;
    }
    if (this.want_stencil != other.want_stencil) {
      return false;
    }
    if (this.want_stencil_shared == null) {
      if (other.want_stencil_shared != null) {
        return false;
      }
    } else if (!this.want_stencil_shared.equals(other.want_stencil_shared)) {
      return false;
    }
    if (this.width != other.width) {
      return false;
    }
    return true;
  }

  @Override public int getHeight()
  {
    return this.height;
  }

  @Override public int getWidth()
  {
    return this.width;
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + this.height;
    result = (prime * result) + this.want_color.hashCode();
    result = (prime * result) + this.want_depth.hashCode();
    result =
      (prime * result)
        + ((this.want_depth_shared == null) ? 0 : this.want_depth_shared
          .hashCode());
    result = (prime * result) + this.want_stencil.hashCode();
    result =
      (prime * result)
        + ((this.want_stencil_shared == null) ? 0 : this.want_stencil_shared
          .hashCode());
    result = (prime * result) + this.width;
    return result;
  }

  private boolean hasRequestedColor()
  {
    return this.want_color.isEmpty() == false;
  }

  @Override public @Nonnull
    <G extends JCGLFramebuffersGL3 & JCGLTextures2DStaticCommon & JCGLTexturesCubeStaticCommon & JCGLRenderbuffersGL3>
    Indeterminate<Framebuffer, FramebufferStatus>
    make(
      final @Nonnull G gl)
      throws JCGLException,
        ConstraintError
  {
    final WorkingBuffers buffers = new WorkingBuffers();

    Constraints.constrainArbitrary(
      this.hasRequestedColor(),
      "Resulting framebuffer has a color attachment");

    buffers.framebuffer = gl.framebufferAllocate();
    this.allocateColorBuffers(buffers, gl);
    this.allocateDepthStencil(buffers, gl);
    FramebufferConfigurationGL3Actual.attachBuffers(buffers, gl);
    return this.validateAndMakeState(buffers, gl);
  }

  @Override public void requestBestRGBAColorRenderbuffer(
    final @Nonnull FramebufferColorAttachmentPoint point,
    final @Nonnull FramebufferDrawBuffer buffer)
    throws ConstraintError
  {
    Constraints.constrainNotNull(point, "Attachment point");
    Constraints.constrainNotNull(buffer, "Draw buffer");

    this.addDrawBufferMapping(buffer, point);
    final ColorRequest cr = new ColorRequest();
    cr.want_color = RequestColor.WANT_COLOR_RGBA_RENDERBUFFER;
    cr.want_color_shared = null;
    cr.draw_buffer = buffer;
    this.want_color.put(point, cr);
  }

  @Override public void requestBestRGBAColorTexture2D(
    final @Nonnull FramebufferColorAttachmentPoint point,
    final @Nonnull FramebufferDrawBuffer buffer,
    final @Nonnull TextureWrapS texture_wrap_s,
    final @Nonnull TextureWrapT texture_wrap_t,
    final @Nonnull TextureFilterMinification texture_min_filter,
    final @Nonnull TextureFilterMagnification texture_mag_filter)
    throws ConstraintError
  {
    Constraints.constrainNotNull(point, "Attachment point");
    Constraints.constrainNotNull(buffer, "Draw buffer");
    Constraints.constrainNotNull(texture_wrap_s, "Wrap S");
    Constraints.constrainNotNull(texture_wrap_t, "Wrap T");
    Constraints.constrainNotNull(texture_mag_filter, "Magnification filter");
    Constraints.constrainNotNull(texture_min_filter, "Minification filter");

    this.addDrawBufferMapping(buffer, point);
    final ColorRequest cr = new ColorRequest();
    cr.want_color = RequestColor.WANT_COLOR_RGBA_TEXTURE_2D;
    cr.want_color_shared = null;
    cr.wrap_s = texture_wrap_s;
    cr.wrap_t = texture_wrap_t;
    cr.min_filter = texture_min_filter;
    cr.mag_filter = texture_mag_filter;
    this.want_color.put(point, cr);
  }

  @Override public void requestBestRGBAColorTextureCube(
    final @Nonnull FramebufferColorAttachmentPoint point,
    final @Nonnull FramebufferDrawBuffer buffer,
    final @Nonnull TextureWrapR texture_wrap_r,
    final @Nonnull TextureWrapS texture_wrap_s,
    final @Nonnull TextureWrapT texture_wrap_t,
    final @Nonnull TextureFilterMinification texture_min_filter,
    final @Nonnull TextureFilterMagnification texture_mag_filter)
    throws ConstraintError
  {
    Constraints.constrainNotNull(point, "Attachment point");
    Constraints.constrainNotNull(buffer, "Draw buffer");
    Constraints.constrainNotNull(texture_wrap_r, "Wrap R");
    Constraints.constrainNotNull(texture_wrap_s, "Wrap S");
    Constraints.constrainNotNull(texture_wrap_t, "Wrap T");
    Constraints.constrainNotNull(texture_mag_filter, "Magnification filter");
    Constraints.constrainNotNull(texture_min_filter, "Minification filter");

    Constraints.constrainArbitrary(
      this.width == this.height,
      "Framebuffer width == height required for cube maps");

    this.addDrawBufferMapping(buffer, point);
    final ColorRequest cr = new ColorRequest();
    cr.want_color = RequestColor.WANT_COLOR_RGBA_TEXTURE_CUBE;
    cr.want_color_shared = null;
    cr.wrap_r = texture_wrap_r;
    cr.wrap_s = texture_wrap_s;
    cr.wrap_t = texture_wrap_t;
    cr.min_filter = texture_min_filter;
    cr.mag_filter = texture_mag_filter;
    this.want_color.put(point, cr);
  }

  @Override public void requestBestRGBColorRenderbuffer(
    final @Nonnull FramebufferColorAttachmentPoint point,
    final @Nonnull FramebufferDrawBuffer buffer)
    throws ConstraintError
  {
    Constraints.constrainNotNull(point, "Attachment point");
    Constraints.constrainNotNull(buffer, "Draw buffer");

    this.addDrawBufferMapping(buffer, point);
    final ColorRequest cr = new ColorRequest();
    cr.want_color = RequestColor.WANT_COLOR_RGB_RENDERBUFFER;
    cr.want_color_shared = null;
    this.want_color.put(point, cr);
  }

  @Override public void requestBestRGBColorTexture2D(
    final @Nonnull FramebufferColorAttachmentPoint point,
    final @Nonnull FramebufferDrawBuffer buffer,
    final @Nonnull TextureWrapS texture_wrap_s,
    final @Nonnull TextureWrapT texture_wrap_t,
    final @Nonnull TextureFilterMinification texture_min_filter,
    final @Nonnull TextureFilterMagnification texture_mag_filter)
    throws ConstraintError
  {
    Constraints.constrainNotNull(point, "Attachment point");
    Constraints.constrainNotNull(buffer, "Draw buffer");
    Constraints.constrainNotNull(texture_wrap_s, "Wrap S");
    Constraints.constrainNotNull(texture_wrap_t, "Wrap T");
    Constraints.constrainNotNull(texture_mag_filter, "Magnification filter");
    Constraints.constrainNotNull(texture_min_filter, "Minification filter");

    this.addDrawBufferMapping(buffer, point);
    final ColorRequest cr = new ColorRequest();
    cr.want_color = RequestColor.WANT_COLOR_RGB_TEXTURE_2D;
    cr.want_color_shared = null;
    cr.wrap_s = texture_wrap_s;
    cr.wrap_t = texture_wrap_t;
    cr.min_filter = texture_min_filter;
    cr.mag_filter = texture_mag_filter;
    this.want_color.put(point, cr);
  }

  @Override public void requestBestRGBColorTextureCube(
    final @Nonnull FramebufferColorAttachmentPoint point,
    final @Nonnull FramebufferDrawBuffer buffer,
    final @Nonnull TextureWrapR texture_wrap_r,
    final @Nonnull TextureWrapS texture_wrap_s,
    final @Nonnull TextureWrapT texture_wrap_t,
    final @Nonnull TextureFilterMinification texture_min_filter,
    final @Nonnull TextureFilterMagnification texture_mag_filter)
    throws ConstraintError
  {
    Constraints.constrainNotNull(point, "Attachment point");
    Constraints.constrainNotNull(buffer, "Draw buffer");
    Constraints.constrainNotNull(texture_wrap_r, "Wrap R");
    Constraints.constrainNotNull(texture_wrap_s, "Wrap S");
    Constraints.constrainNotNull(texture_wrap_t, "Wrap T");
    Constraints.constrainNotNull(texture_mag_filter, "Magnification filter");
    Constraints.constrainNotNull(texture_min_filter, "Minification filter");

    Constraints.constrainArbitrary(
      this.width == this.height,
      "Framebuffer width == height required for cube maps");

    this.addDrawBufferMapping(buffer, point);
    final ColorRequest cr = new ColorRequest();
    cr.want_color = RequestColor.WANT_COLOR_RGB_TEXTURE_CUBE;
    cr.want_color_shared = null;
    cr.wrap_r = texture_wrap_r;
    cr.wrap_s = texture_wrap_s;
    cr.wrap_t = texture_wrap_t;
    cr.min_filter = texture_min_filter;
    cr.mag_filter = texture_mag_filter;
    this.want_color.put(point, cr);
  }

  @Override public void requestDepthRenderbuffer()
  {
    this.want_depth = RequestDepth.WANT_DEPTH_RENDERBUFFER;
    this.want_depth_shared = null;
  }

  @Override public void requestNoColor()
    throws ConstraintError
  {
    this.want_color.clear();
  }

  @Override public void requestNoColorAt(
    final @Nonnull FramebufferColorAttachmentPoint point)
    throws ConstraintError
  {
    Constraints.constrainNotNull(point, "Attachment point");

    this.want_color.remove(point);

    /**
     * Search for the attachment in the draw buffer mappings, and remove it.
     * 
     * Linear time, but the map will typically contain at most eight items...
     */

    FramebufferDrawBuffer key = null;
    for (final Entry<FramebufferDrawBuffer, FramebufferColorAttachmentPoint> e : this.draw_buffers
      .entrySet()) {
      if (e.getValue().equals(point)) {
        key = e.getKey();
        break;
      }
    }

    if (key != null) {
      this.draw_buffers.remove(key);
    }
  }

  @Override public void requestNoDepth()
  {
    this.want_depth = RequestDepth.WANT_NOTHING;
    this.want_depth_shared = null;
  }

  @Override public void requestNoStencil()
  {
    this.want_stencil = RequestStencil.WANT_NOTHING;
    this.want_stencil_shared = null;
  }

  @Override public void requestSharedColor(
    final @Nonnull FramebufferColorAttachmentPoint point,
    final @Nonnull FramebufferDrawBuffer buffer,
    final @Nonnull FramebufferUsable source,
    final @Nonnull FramebufferColorAttachmentPoint source_point)
    throws ConstraintError
  {
    Constraints.constrainNotNull(point, "Attachment point");
    Constraints.constrainNotNull(buffer, "Draw buffer");
    Constraints.constrainNotNull(source, "Framebuffer");
    Constraints.constrainNotNull(source_point, "Source attachment");

    final ColorRequest cr = new ColorRequest();
    cr.want_color = RequestColor.WANT_COLOR_SHARED_WITH;

    final AttachmentColor attach = source.getColorAttachment(point);
    switch (attach.type) {
      case ATTACHMENT_COLOR_RENDERBUFFER:
      {
        final AttachmentColorRenderbuffer a =
          (AttachmentColorRenderbuffer) attach;
        cr.want_color_shared =
          new AttachmentSharedColorRenderbuffer(a.getRenderbuffer());
        break;
      }
      case ATTACHMENT_COLOR_TEXTURE_2D:
      {
        final AttachmentColorTexture2DStatic a =
          (AttachmentColorTexture2DStatic) attach;
        cr.want_color_shared =
          new AttachmentSharedColorTexture2DStatic(a.getTexture2D());
        break;
      }
      case ATTACHMENT_COLOR_TEXTURE_CUBE:
      {
        final AttachmentColorTextureCubeStatic a =
          (AttachmentColorTextureCubeStatic) attach;
        cr.want_color_shared =
          new AttachmentSharedColorTextureCubeStatic(
            a.getTextureCube(),
            a.getFace());
        break;
      }
      case ATTACHMENT_SHARED_COLOR_RENDERBUFFER:
      {
        final AttachmentSharedColorRenderbuffer a =
          (AttachmentSharedColorRenderbuffer) attach;
        cr.want_color_shared = new AttachmentSharedColorRenderbuffer(a);
        break;
      }
      case ATTACHMENT_SHARED_COLOR_TEXTURE_2D:
      {
        final AttachmentSharedColorTexture2DStatic a =
          (AttachmentSharedColorTexture2DStatic) attach;
        cr.want_color_shared = new AttachmentSharedColorTexture2DStatic(a);
        break;
      }
      case ATTACHMENT_SHARED_COLOR_TEXTURE_CUBE:
      {
        final AttachmentSharedColorTextureCubeStatic a =
          (AttachmentSharedColorTextureCubeStatic) attach;
        cr.want_color_shared = new AttachmentSharedColorTextureCubeStatic(a);
        break;
      }
    }

    this.addDrawBufferMapping(buffer, point);
    this.want_color.put(point, cr);
  }

  @Override public void requestSharedDepth(
    final @Nonnull Framebuffer framebuffer)
    throws ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");

    final AttachmentDepth attach = framebuffer.getDepthAttachment();
    switch (attach.type) {
      case ATTACHMENT_DEPTH_RENDERBUFFER:
      {
        final AttachmentDepthRenderbuffer a =
          (AttachmentDepthRenderbuffer) attach;
        this.want_depth_shared =
          new AttachmentSharedDepthRenderbuffer(a.getRenderbuffer());
        break;
      }
      case ATTACHMENT_DEPTH_STENCIL_RENDERBUFFER:
      {
        final AttachmentDepthStencilRenderbuffer a =
          (AttachmentDepthStencilRenderbuffer) attach;
        this.want_depth_shared =
          new AttachmentSharedDepthStencilRenderbuffer(a.getRenderbuffer());
        break;
      }
      case ATTACHMENT_SHARED_DEPTH_RENDERBUFFER:
      {
        final AttachmentSharedDepthRenderbuffer a =
          (AttachmentSharedDepthRenderbuffer) attach;
        this.want_depth_shared = new AttachmentSharedDepthRenderbuffer(a);
        break;
      }
      case ATTACHMENT_SHARED_DEPTH_STENCIL_RENDERBUFFER:
      {
        final AttachmentSharedDepthStencilRenderbuffer a =
          (AttachmentSharedDepthStencilRenderbuffer) attach;
        this.want_depth_shared =
          new AttachmentSharedDepthStencilRenderbuffer(a);
        break;
      }
    }

    this.want_depth = RequestDepth.WANT_DEPTH_SHARED_WITH;
  }

  @Override public void requestSharedStencil(
    final @Nonnull Framebuffer framebuffer)
    throws ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");

    final AttachmentStencil attach = framebuffer.getStencilAttachment();
    switch (attach.type) {
      case ATTACHMENT_SHARED_STENCIL_RENDERBUFFER:
      {
        final AttachmentSharedStencilRenderbuffer a =
          (AttachmentSharedStencilRenderbuffer) attach;
        this.want_stencil_shared = new AttachmentSharedStencilRenderbuffer(a);
        break;
      }
      case ATTACHMENT_STENCIL_AS_DEPTH_STENCIL:
      {
        /**
         * If the source framebuffer's stencil attachment is a combined
         * depth/stencil attachment, then request to share the depth
         * attachment and set the stencil attachment to point to it.
         */

        final AttachmentDepth depth = framebuffer.getDepthAttachment();
        switch (depth.type) {
          case ATTACHMENT_DEPTH_RENDERBUFFER:
          case ATTACHMENT_SHARED_DEPTH_RENDERBUFFER:
          {
            throw new UnreachableCodeException();
          }
          case ATTACHMENT_DEPTH_STENCIL_RENDERBUFFER:
          case ATTACHMENT_SHARED_DEPTH_STENCIL_RENDERBUFFER:
          {
            this.requestSharedDepth(framebuffer);
            break;
          }
        }

        this.want_stencil_shared = new AttachmentStencilAsDepthStencil();
        break;
      }
      case ATTACHMENT_STENCIL_RENDERBUFFER:
      {
        final AttachmentStencilRenderbuffer a =
          (AttachmentStencilRenderbuffer) attach;
        this.want_stencil_shared =
          new AttachmentSharedStencilRenderbuffer(a.getRenderbuffer());
        break;
      }
    }

    this.want_stencil = RequestStencil.WANT_STENCIL_SHARED_WITH;
  }

  @Override public void requestStencilRenderbuffer()
  {
    /**
     * Requesting a stencil renderbuffer will actually get a packed
     * depth/stencil attachment.
     */

    this.want_depth = RequestDepth.WANT_DEPTH_RENDERBUFFER;
    this.want_depth_shared = null;
  }

  @Override public void setHeight(
    final int height)
    throws ConstraintError
  {
    this.height =
      Constraints.constrainRange(height, 1, Integer.MAX_VALUE, "Height");
  }

  @Override public void setWidth(
    final int width)
    throws ConstraintError
  {
    this.width =
      Constraints.constrainRange(width, 1, Integer.MAX_VALUE, "Width");
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[FramebufferConfigurationGL3 want_depth=");
    builder.append(this.want_depth);
    builder.append(", want_depth_shared=");
    builder.append(this.want_depth_shared);
    builder.append(", want_stencil=");
    builder.append(this.want_stencil);
    builder.append(", want_stencil_shared=");
    builder.append(this.want_stencil_shared);
    builder.append(", want_color=");
    builder.append(this.want_color);
    builder.append(", width=");
    builder.append(this.width);
    builder.append(", height=");
    builder.append(this.height);
    builder.append("]");
    return builder.toString();
  }

  /**
   * <p>
   * Validate the constructed framebuffer, and return a completed framebuffer
   * structure if validation succeeds.
   * </p>
   */

  private
    <G extends JCGLFramebuffersGL3 & JCGLRenderbuffersGL3 & JCGLTextures2DStaticCommon & JCGLTexturesCubeStaticCommon>
    Indeterminate<Framebuffer, FramebufferStatus>
    validateAndMakeState(
      final @Nonnull WorkingBuffers buffers,
      final @Nonnull G gl)
      throws JCGLException,
        ConstraintError
  {
    final FramebufferStatus status =
      gl.framebufferDrawValidate(buffers.framebuffer);

    switch (status) {
      case FRAMEBUFFER_STATUS_COMPLETE:
      {
        break;
      }
      case FRAMEBUFFER_STATUS_ERROR_INCOMPLETE_ATTACHMENT:
      case FRAMEBUFFER_STATUS_ERROR_INCOMPLETE_DRAW_BUFFER:
      case FRAMEBUFFER_STATUS_ERROR_MISSING_IMAGE_ATTACHMENT:
      case FRAMEBUFFER_STATUS_ERROR_INCOMPLETE_READ_BUFFER:
      case FRAMEBUFFER_STATUS_ERROR_UNSUPPORTED:
      case FRAMEBUFFER_STATUS_ERROR_UNKNOWN:
      {
        buffers.emergencyCleanup(gl);
        return new Failure<Framebuffer, FramebufferStatus>(status);
      }
    }

    gl.framebufferDrawSetBuffers(buffers.framebuffer, this.draw_buffers);

    final Framebuffer state =
      new Framebuffer(buffers.framebuffer, this.width, this.height);

    for (final Entry<FramebufferColorAttachmentPoint, AttachmentColor> e : buffers.attachments_color
      .entrySet()) {
      state.configAddColorAttachment(e.getKey(), e.getValue());
    }
    if (buffers.attachment_depth != null) {
      state.configSetDepthAttachment(buffers.attachment_depth);
    }
    if (buffers.attachment_stencil != null) {
      state.configSetStencilAttachment(buffers.attachment_stencil);
    }

    return new Success<Framebuffer, FramebufferStatus>(state);
  }
}
