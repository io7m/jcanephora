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

import java.util.List;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jaux.functional.Indeterminate;
import com.io7m.jaux.functional.Indeterminate.Failure;
import com.io7m.jaux.functional.Indeterminate.Success;
import com.io7m.jaux.functional.Option;
import com.io7m.jaux.functional.Option.Some;
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
 * Instructions to create a framebuffer configuration that will behave
 * identically on OpenGL ES2, OpenGL ES3, and OpenGL 3.0 (using
 * version-specific features of each, if necessary).
 */

@NotThreadSafe public final class FramebufferConfigurationGL3ES2Actual implements
  FramebufferConfigurationGL3ES2
{
  private static enum RequestColor
  {
    WANT_COLOR_RGB_RENDERBUFFER,
    WANT_COLOR_RGB_TEXTURE_2D,
    WANT_COLOR_RGB_TEXTURE_CUBE,
    WANT_COLOR_RGBA_RENDERBUFFER,
    WANT_COLOR_RGBA_TEXTURE_2D,
    WANT_COLOR_RGBA_TEXTURE_CUBE,
    WANT_COLOR_SHARED_WITH,
    WANT_NOTHING
  }

  private static enum RequestDepth
  {
    WANT_DEPTH_RENDERBUFFER,
    WANT_DEPTH_SHARED_WITH,
    WANT_NOTHING
  }

  private static enum RequestStencil
  {
    WANT_NOTHING,
    WANT_STENCIL_RENDERBUFFER,
    WANT_STENCIL_SHARED_WITH
  }

  private static class WorkingBuffers
  {
    @CheckForNull AttachmentColor              attachment_color   = null;
    @CheckForNull AttachmentDepth              attachment_depth   = null;
    @CheckForNull AttachmentStencil            attachment_stencil = null;
    private final JCGLFramebuffersCommon       fb;
    @CheckForNull FramebufferReference         framebuffer        = null;
    private final JCGLRenderbuffersCommon      rb;
    private final JCGLTextures2DStaticCommon   t2ds;
    private final JCGLTexturesCubeStaticCommon tcs;
    final @Nonnull StringBuilder               text;

    WorkingBuffers(
      final JCGLRenderbuffersCommon rb,
      final JCGLFramebuffersCommon fb,
      final JCGLTextures2DStaticCommon t2ds,
      final JCGLTexturesCubeStaticCommon tcs)
    {
      this.rb = rb;
      this.fb = fb;
      this.t2ds = t2ds;
      this.tcs = tcs;
      this.text = new StringBuilder();
    }

    void emergencyCleanup()
      throws ConstraintError,
        JCGLRuntimeException
    {
      JCGLRuntimeException saved = null;

      if (this.framebuffer != null) {
        try {
          this.fb.framebufferDelete(this.framebuffer);
        } catch (final JCGLRuntimeException e) {
          saved = e;
        }
      }

      /**
       * Delete unshared color attachments.
       */

      if (this.attachment_color != null) {
        switch (this.attachment_color.type) {
          case ATTACHMENT_COLOR_RENDERBUFFER:
          {
            final AttachmentColorRenderbuffer a =
              (AttachmentColorRenderbuffer) this.attachment_color;
            this.rb.renderbufferDelete(a.getRenderbufferWritable());
            break;
          }
          case ATTACHMENT_COLOR_TEXTURE_2D:
          {
            final AttachmentColorTexture2DStatic a =
              (AttachmentColorTexture2DStatic) this.attachment_color;
            this.t2ds.texture2DStaticDelete(a.getTextureWritable());
            break;
          }
          case ATTACHMENT_COLOR_TEXTURE_CUBE:
          {
            final AttachmentColorTextureCubeStatic a =
              (AttachmentColorTextureCubeStatic) this.attachment_color;
            this.tcs.textureCubeStaticDelete(a.getTextureWritable());
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
            this.rb.renderbufferDelete(a.getRenderbufferWritable());
            break;
          }
          case ATTACHMENT_DEPTH_STENCIL_RENDERBUFFER:
          {
            final AttachmentDepthStencilRenderbuffer a =
              (AttachmentDepthStencilRenderbuffer) this.attachment_depth;
            this.rb.renderbufferDelete(a.getRenderbufferWritable());
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
            this.rb.renderbufferDelete(a.getRenderbufferWritable());
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
   * Attach all allocated buffers to the current framebuffer on ES2.
   * 
   * @throws JCGLRuntimeException
   * @throws ConstraintError
   */

  private static void attachBuffers_ES2(
    final @Nonnull WorkingBuffers buffers,
    final @Nonnull JCGLInterfaceGLES2 gl,
    final @CheckForNull JCGLExtensionPackedDepthStencil extension)
    throws JCGLRuntimeException,
      ConstraintError
  {
    gl.framebufferDrawBind(buffers.framebuffer);

    FramebufferConfigurationGL3ES2Actual.attachColorBuffers(buffers, gl);
    FramebufferConfigurationGL3ES2Actual.attachDepthBuffers_ES2_DS(
      buffers,
      extension);
    FramebufferConfigurationGL3ES2Actual.attachStencilBuffers(buffers, gl);
  }

  /**
   * Attach all allocated buffers to the current framebuffer on OpenGL 3.0.
   * 
   * @throws JCGLRuntimeException
   * @throws ConstraintError
   */

  private static void attachBuffers_GL3(
    final @Nonnull WorkingBuffers buffers,
    final @Nonnull JCGLFramebuffersGL3 gl)
    throws JCGLRuntimeException,
      ConstraintError
  {
    gl.framebufferDrawBind(buffers.framebuffer);

    FramebufferConfigurationGL3ES2Actual.attachColorBuffers(buffers, gl);
    FramebufferConfigurationGL3ES2Actual.attachDepthBuffers_GL3(buffers, gl);
    FramebufferConfigurationGL3ES2Actual.attachStencilBuffers(buffers, gl);
  }

  private static void attachColorBuffers(
    final WorkingBuffers buffers,
    final JCGLFramebuffersGLES2 gl)
    throws JCGLRuntimeException,
      ConstraintError
  {
    if (buffers.attachment_color != null) {
      switch (buffers.attachment_color.type) {
        case ATTACHMENT_SHARED_COLOR_RENDERBUFFER:
        {
          final AttachmentSharedColorRenderbuffer a =
            (AttachmentSharedColorRenderbuffer) buffers.attachment_color;

          gl.framebufferDrawAttachColorRenderbuffer(
            buffers.framebuffer,
            a.getRenderbuffer());
          break;
        }
        case ATTACHMENT_COLOR_RENDERBUFFER:
        {
          final AttachmentColorRenderbuffer a =
            (AttachmentColorRenderbuffer) buffers.attachment_color;

          gl.framebufferDrawAttachColorRenderbuffer(
            buffers.framebuffer,
            a.getRenderbufferWritable());
          break;
        }
        case ATTACHMENT_SHARED_COLOR_TEXTURE_2D:
        {
          final AttachmentSharedColorTexture2DStatic a =
            (AttachmentSharedColorTexture2DStatic) buffers.attachment_color;

          gl.framebufferDrawAttachColorTexture2D(
            buffers.framebuffer,
            a.getTexture2D());
          break;
        }
        case ATTACHMENT_COLOR_TEXTURE_2D:
        {
          final AttachmentColorTexture2DStatic a =
            (AttachmentColorTexture2DStatic) buffers.attachment_color;

          gl.framebufferDrawAttachColorTexture2D(
            buffers.framebuffer,
            a.getTextureWritable());
          break;
        }
        case ATTACHMENT_SHARED_COLOR_TEXTURE_CUBE:
        {
          final AttachmentSharedColorTextureCubeStatic a =
            (AttachmentSharedColorTextureCubeStatic) buffers.attachment_color;

          gl.framebufferDrawAttachColorTextureCube(
            buffers.framebuffer,
            a.getTextureCube(),
            CubeMapFaceLH.CUBE_MAP_LH_POSITIVE_X);
          break;
        }
        case ATTACHMENT_COLOR_TEXTURE_CUBE:
        {
          final AttachmentColorTextureCubeStatic a =
            (AttachmentColorTextureCubeStatic) buffers.attachment_color;
          gl.framebufferDrawAttachColorTextureCube(
            buffers.framebuffer,
            a.getTextureWritable(),
            CubeMapFaceLH.CUBE_MAP_LH_POSITIVE_X);
          break;
        }
      }
    }
  }

  private static void attachDepthBuffers_ES2_DS(
    final WorkingBuffers buffers,
    final JCGLExtensionPackedDepthStencil extension)
    throws JCGLRuntimeException,
      ConstraintError
  {
    if (buffers.attachment_depth != null) {
      switch (buffers.attachment_depth.type) {
        case ATTACHMENT_SHARED_DEPTH_RENDERBUFFER:
        case ATTACHMENT_DEPTH_RENDERBUFFER:
        {
          /**
           * If depth/shared attachments are supported, then the
           * allocated/shared attachment can't be a single depth renderbuffer.
           */

          throw new UnreachableCodeException();
        }
        case ATTACHMENT_DEPTH_STENCIL_RENDERBUFFER:
        {
          final AttachmentDepthStencilRenderbuffer a =
            (AttachmentDepthStencilRenderbuffer) buffers.attachment_depth;

          extension.framebufferDrawAttachDepthStencilRenderbuffer(
            buffers.framebuffer,
            a.getRenderbufferWritable());
          break;
        }
        case ATTACHMENT_SHARED_DEPTH_STENCIL_RENDERBUFFER:
        {
          final AttachmentSharedDepthStencilRenderbuffer a =
            (AttachmentSharedDepthStencilRenderbuffer) buffers.attachment_depth;

          extension.framebufferDrawAttachDepthStencilRenderbuffer(
            buffers.framebuffer,
            a.getRenderbuffer());
          break;
        }
      }
    }
  }

  private static void attachDepthBuffers_GL3(
    final WorkingBuffers buffers,
    final JCGLFramebuffersGL3 gl)
    throws JCGLRuntimeException,
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

  private static void attachStencilBuffers(
    final WorkingBuffers buffers,
    final JCGLFramebuffersGLES2 gl)
    throws JCGLRuntimeException,
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
    final @Nonnull WorkingBuffers buffers)
  {
    buffers.text.setLength(0);
    buffers.text.append("framebuffer-");
    buffers.text.append(buffers.framebuffer.getGLName());
    buffers.text.append("-texture2D-0");
  }

  private static void makeTextureCubeName(
    final @Nonnull WorkingBuffers buffers)
  {
    buffers.text.setLength(0);
    buffers.text.append("framebuffer-");
    buffers.text.append(buffers.framebuffer.getGLName());
    buffers.text.append("-textureCube-0");
  }

  private int                                 height;
  private @Nonnull TextureFilterMagnification mag_filter;
  private @Nonnull TextureFilterMinification  min_filter;
  private @Nonnull RequestColor               want_color;
  private @CheckForNull AttachmentColor       want_color_shared;
  private @Nonnull RequestDepth               want_depth;

  private @CheckForNull AttachmentDepth       want_depth_shared;
  private @Nonnull RequestStencil             want_stencil;
  private @CheckForNull AttachmentStencil     want_stencil_shared;
  private int                                 width;
  private @Nonnull TextureWrapR               wrap_r;
  private @Nonnull TextureWrapS               wrap_s;
  private @Nonnull TextureWrapT               wrap_t;

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

  public FramebufferConfigurationGL3ES2Actual(
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

    this.want_color = RequestColor.WANT_NOTHING;
    this.want_color_shared = null;

    this.wrap_r = TextureWrapR.TEXTURE_WRAP_REPEAT;
    this.wrap_s = TextureWrapS.TEXTURE_WRAP_REPEAT;
    this.wrap_t = TextureWrapT.TEXTURE_WRAP_REPEAT;
    this.mag_filter = TextureFilterMagnification.TEXTURE_FILTER_NEAREST;
    this.min_filter = TextureFilterMinification.TEXTURE_FILTER_NEAREST;
  }

  private Renderbuffer<RenderableColor> allocateBestRGBARenderbuffer_ES2(
    final @Nonnull JCGLRenderbuffersGLES2 gl)
    throws ConstraintError,
      JCGLRuntimeException
  {
    return gl.renderbufferAllocateRGBA4444(this.width, this.height);
  }

  private Renderbuffer<RenderableColor> allocateBestRGBARenderbuffer_GL3ES3(
    final @Nonnull JCGLRenderbuffersGL3ES3 gl)
    throws ConstraintError,
      JCGLRuntimeException
  {
    return gl.renderbufferAllocateRGBA8888(this.width, this.height);
  }

  private Texture2DStatic allocateBestRGBATexture2D(
    final @Nonnull JCGLTextures2DStaticCommon gl,
    final @Nonnull StringBuilder name)
    throws ConstraintError,
      JCGLRuntimeException
  {
    return gl.texture2DStaticAllocateRGBA8(
      name.toString(),
      this.width,
      this.height,
      this.wrap_s,
      this.wrap_t,
      this.min_filter,
      this.mag_filter);
  }

  private @Nonnull TextureCubeStatic allocateBestRGBATextureCube(
    final @Nonnull JCGLTexturesCubeStaticCommon gl,
    final @Nonnull StringBuilder name)
    throws JCGLRuntimeException,
      ConstraintError
  {
    return gl.textureCubeStaticAllocateRGBA8(
      name.toString(),
      this.width,
      this.wrap_r,
      this.wrap_s,
      this.wrap_t,
      this.min_filter,
      this.mag_filter);
  }

  private Renderbuffer<RenderableColor> allocateBestRGBRenderbuffer_ES2(
    final @Nonnull JCGLRenderbuffersGLES2 gl)
    throws ConstraintError,
      JCGLRuntimeException
  {
    return gl.renderbufferAllocateRGB565(this.width, this.height);
  }

  private Renderbuffer<RenderableColor> allocateBestRGBRenderbuffer_GL3ES3(
    final @Nonnull JCGLRenderbuffersGL3ES3 gl)
    throws ConstraintError,
      JCGLRuntimeException
  {
    return gl.renderbufferAllocateRGB888(this.width, this.height);
  }

  private Texture2DStatic allocateBestRGBTexture2D(
    final @Nonnull JCGLTextures2DStaticCommon gl,
    final @Nonnull StringBuilder name)
    throws ConstraintError,
      JCGLRuntimeException
  {
    return gl.texture2DStaticAllocateRGB8(
      name.toString(),
      this.width,
      this.height,
      this.wrap_s,
      this.wrap_t,
      this.min_filter,
      this.mag_filter);
  }

  private TextureCubeStatic allocateBestRGBTextureCube(
    final @Nonnull JCGLTexturesCubeStaticCommon gl,
    final @Nonnull StringBuilder name)
    throws JCGLRuntimeException,
      ConstraintError
  {
    return gl.textureCubeStaticAllocateRGB8(
      name.toString(),
      this.width,
      this.wrap_r,
      this.wrap_s,
      this.wrap_t,
      this.min_filter,
      this.mag_filter);
  }

  /**
   * Allocate all requested color buffers on ES2.
   */

  private
    <G extends JCGLRenderbuffersGLES2 & JCGLTextures2DStaticCommon & JCGLTexturesCubeStaticCommon>
    void
    allocateColorBuffers_ES2(
      final @Nonnull WorkingBuffers buffers,
      final @Nonnull G gl)
      throws JCGLRuntimeException,
        ConstraintError
  {
    switch (this.want_color) {
      case WANT_COLOR_RGBA_RENDERBUFFER:
      {
        buffers.attachment_color =
          new AttachmentColorRenderbuffer(
            this.allocateBestRGBARenderbuffer_ES2(gl));
        break;
      }
      case WANT_COLOR_RGB_RENDERBUFFER:
      {
        buffers.attachment_color =
          new AttachmentColorRenderbuffer(
            this.allocateBestRGBRenderbuffer_ES2(gl));
        break;
      }
      case WANT_COLOR_RGBA_TEXTURE_2D:
      {
        FramebufferConfigurationGL3ES2Actual.makeTexture2DName(buffers);
        buffers.attachment_color =
          new AttachmentColorTexture2DStatic(this.allocateBestRGBATexture2D(
            gl,
            buffers.text));
        break;
      }
      case WANT_COLOR_RGBA_TEXTURE_CUBE:
      {
        FramebufferConfigurationGL3ES2Actual.makeTextureCubeName(buffers);
        buffers.attachment_color =
          new AttachmentColorTextureCubeStatic(
            this.allocateBestRGBATextureCube(gl, buffers.text),
            CubeMapFaceLH.CUBE_MAP_LH_POSITIVE_X);
        break;
      }
      case WANT_COLOR_RGB_TEXTURE_2D:
      {
        FramebufferConfigurationGL3ES2Actual.makeTexture2DName(buffers);
        buffers.attachment_color =
          new AttachmentColorTexture2DStatic(this.allocateBestRGBTexture2D(
            gl,
            buffers.text));
        break;
      }
      case WANT_COLOR_RGB_TEXTURE_CUBE:
      {
        FramebufferConfigurationGL3ES2Actual.makeTextureCubeName(buffers);
        buffers.attachment_color =
          new AttachmentColorTextureCubeStatic(
            this.allocateBestRGBTextureCube(gl, buffers.text),
            CubeMapFaceLH.CUBE_MAP_LH_POSITIVE_X);
        break;
      }
      case WANT_COLOR_SHARED_WITH:
      {
        buffers.attachment_color = this.want_color_shared;
        break;
      }
      case WANT_NOTHING:
      {
        break;
      }
    }
  }

  /**
   * Allocate all requested color buffers on OpenGL 3.0.
   */

  private
    <G extends JCGLRenderbuffersGL3ES3 & JCGLTextures2DStaticCommon & JCGLTexturesCubeStaticCommon>
    void
    allocateColorBuffers_GL3ES3(
      final @Nonnull WorkingBuffers buffers,
      final @Nonnull G gl)
      throws JCGLRuntimeException,
        ConstraintError
  {
    FramebufferConfigurationGL3ES2Actual.makeTexture2DName(buffers);

    switch (this.want_color) {
      case WANT_COLOR_RGBA_RENDERBUFFER:
      {
        buffers.attachment_color =
          new AttachmentColorRenderbuffer(
            this.allocateBestRGBARenderbuffer_GL3ES3(gl));
        break;
      }
      case WANT_COLOR_RGB_RENDERBUFFER:
      {
        buffers.attachment_color =
          new AttachmentColorRenderbuffer(
            this.allocateBestRGBRenderbuffer_GL3ES3(gl));
        break;
      }
      case WANT_COLOR_RGBA_TEXTURE_2D:
      {
        FramebufferConfigurationGL3ES2Actual.makeTexture2DName(buffers);
        buffers.attachment_color =
          new AttachmentColorTexture2DStatic(this.allocateBestRGBATexture2D(
            gl,
            buffers.text));
        break;
      }
      case WANT_COLOR_RGBA_TEXTURE_CUBE:
      {
        FramebufferConfigurationGL3ES2Actual.makeTextureCubeName(buffers);
        buffers.attachment_color =
          new AttachmentColorTextureCubeStatic(
            this.allocateBestRGBATextureCube(gl, buffers.text),
            CubeMapFaceLH.CUBE_MAP_LH_POSITIVE_X);
        break;
      }
      case WANT_COLOR_RGB_TEXTURE_2D:
      {
        FramebufferConfigurationGL3ES2Actual.makeTexture2DName(buffers);
        buffers.attachment_color =
          new AttachmentColorTexture2DStatic(this.allocateBestRGBTexture2D(
            gl,
            buffers.text));
        break;
      }
      case WANT_COLOR_RGB_TEXTURE_CUBE:
      {
        FramebufferConfigurationGL3ES2Actual.makeTextureCubeName(buffers);
        buffers.attachment_color =
          new AttachmentColorTextureCubeStatic(
            this.allocateBestRGBTextureCube(gl, buffers.text),
            CubeMapFaceLH.CUBE_MAP_LH_POSITIVE_X);
        break;
      }
      case WANT_COLOR_SHARED_WITH:
      {
        buffers.attachment_color = this.want_color_shared;
        break;
      }
      case WANT_NOTHING:
      {
        break;
      }
    }
  }

  /**
   * Allocate a depth buffer on ES2.
   * 
   * @throws ConstraintError
   * @throws JCGLRuntimeException
   */

  private void allocateDepth_ES2(
    final @Nonnull WorkingBuffers buffers,
    final @Nonnull JCGLRenderbuffersGLES2 gl)
    throws JCGLRuntimeException,
      ConstraintError
  {
    switch (this.want_depth) {
      case WANT_DEPTH_RENDERBUFFER:
      {
        buffers.attachment_depth =
          new AttachmentDepthRenderbuffer(gl.renderbufferAllocateDepth16(
            this.width,
            this.height));
        break;
      }
      case WANT_DEPTH_SHARED_WITH:
      {
        buffers.attachment_depth = this.want_depth_shared;
        break;
      }
      case WANT_NOTHING:
      {
        break;
      }
    }
  }

  /**
   * Allocate a depth/stencil buffer using the packed depth/stencil extension,
   * if requested.
   * 
   * @throws ConstraintError
   * @throws JCGLRuntimeException
   */

  private void allocateDepthStencil_ES2(
    final @Nonnull WorkingBuffers buffers,
    final JCGLExtensionPackedDepthStencil extension)
    throws JCGLRuntimeException,
      ConstraintError
  {
    if ((this.want_depth == RequestDepth.WANT_DEPTH_RENDERBUFFER)
      || (this.want_stencil == RequestStencil.WANT_STENCIL_RENDERBUFFER)) {
      buffers.attachment_depth =
        new AttachmentDepthStencilRenderbuffer(
          extension.renderbufferAllocateDepth24Stencil8(
            this.width,
            this.height));

      buffers.attachment_stencil = new AttachmentStencilAsDepthStencil();
      return;
    }

    if ((this.want_depth == RequestDepth.WANT_DEPTH_SHARED_WITH)
      || (this.want_stencil == RequestStencil.WANT_STENCIL_SHARED_WITH)) {

      /**
       * If depth/stencil buffers are supported, then the depth and/or stencil
       * buffer of the other framebuffer must be a depth/stencil buffer.
       */

      buffers.attachment_depth = this.want_depth_shared;
      buffers.attachment_stencil = this.want_stencil_shared;
    }
  }

  /**
   * Allocate a depth/stencil buffer using the packed depth/stencil format, if
   * requested.
   */

  private void allocateDepthStencil_GL3ES3(
    final @Nonnull WorkingBuffers buffers,
    final @Nonnull JCGLRenderbuffersGL3ES3 gl)
    throws JCGLRuntimeException,
      ConstraintError
  {
    if ((this.want_depth == RequestDepth.WANT_DEPTH_RENDERBUFFER)
      || (this.want_stencil == RequestStencil.WANT_STENCIL_RENDERBUFFER)) {
      buffers.attachment_depth =
        new AttachmentDepthStencilRenderbuffer(
          gl.renderbufferAllocateDepth24Stencil8(this.width, this.height));

      buffers.attachment_stencil = new AttachmentStencilAsDepthStencil();
      return;
    }

    if ((this.want_depth == RequestDepth.WANT_DEPTH_SHARED_WITH)
      || (this.want_stencil == RequestStencil.WANT_STENCIL_SHARED_WITH)) {

      /**
       * The depth and/or stencil buffer of the other framebuffer must be a
       * depth/stencil buffer.
       */

      buffers.attachment_depth = this.want_depth_shared;
      buffers.attachment_stencil = this.want_stencil_shared;
    }
  }

  /**
   * Allocate a stencil buffer on ES2.
   * 
   * @throws ConstraintError
   * @throws JCGLRuntimeException
   */

  private void allocateStencil_ES2(
    final @Nonnull WorkingBuffers buffers,
    final @Nonnull JCGLInterfaceGLES2 gl)
    throws JCGLRuntimeException,
      ConstraintError
  {
    switch (this.want_stencil) {
      case WANT_NOTHING:
      {
        break;
      }
      case WANT_STENCIL_RENDERBUFFER:
      {
        buffers.attachment_stencil =
          new AttachmentStencilRenderbuffer(gl.renderbufferAllocateStencil8(
            this.width,
            this.height));
        break;
      }
      case WANT_STENCIL_SHARED_WITH:
      {
        buffers.attachment_stencil = this.want_stencil_shared;
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
    final FramebufferConfigurationGL3ES2Actual other =
      (FramebufferConfigurationGL3ES2Actual) obj;
    if (this.height != other.height) {
      return false;
    }
    if (this.mag_filter != other.mag_filter) {
      return false;
    }
    if (this.min_filter != other.min_filter) {
      return false;
    }
    if (this.want_color != other.want_color) {
      return false;
    }
    if (this.want_depth != other.want_depth) {
      return false;
    }
    if (this.want_stencil != other.want_stencil) {
      return false;
    }
    if (this.width != other.width) {
      return false;
    }
    if (this.wrap_r != other.wrap_r) {
      return false;
    }
    if (this.wrap_s != other.wrap_s) {
      return false;
    }
    if (this.wrap_t != other.wrap_t) {
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
    result = (prime * result) + this.mag_filter.hashCode();
    result = (prime * result) + this.min_filter.hashCode();
    result = (prime * result) + this.want_color.hashCode();
    result = (prime * result) + this.want_depth.hashCode();
    result = (prime * result) + this.want_stencil.hashCode();
    result = (prime * result) + this.width;
    result = (prime * result) + this.wrap_r.hashCode();
    result = (prime * result) + this.wrap_s.hashCode();
    result = (prime * result) + this.wrap_t.hashCode();
    return result;
  }

  private boolean hasRequestedColor()
  {
    switch (this.want_color) {
      case WANT_NOTHING:
        break;
      // $CASES-OMITTED$
      default:
        return true;
    }

    return false;
  }

  @Override public @Nonnull
    Indeterminate<Framebuffer, FramebufferStatus>
    make(
      final @Nonnull JCGLImplementation gi)
      throws JCGLRuntimeException,
        ConstraintError
  {
    Constraints.constrainArbitrary(
      this.hasRequestedColor(),
      "Resulting framebuffer has a color attachment");

    {
      final Option<JCGLInterfaceGL3> gl3opt = gi.getGL3();
      switch (gl3opt.type) {
        case OPTION_NONE:
        {
          break;
        }
        case OPTION_SOME:
        {
          final Some<JCGLInterfaceGL3> some = (Some<JCGLInterfaceGL3>) gl3opt;
          final JCGLInterfaceGL3 gl3 = some.value;
          final WorkingBuffers buffers =
            new WorkingBuffers(gl3, gl3, gl3, gl3);

          try {
            return this.makeGL3(buffers, gl3);
          } catch (final JCGLRuntimeException e) {
            buffers.emergencyCleanup();
            throw e;
          } catch (final ConstraintError e) {
            buffers.emergencyCleanup();
            throw e;
          }
        }
      }
    }

    {
      final Option<JCGLInterfaceGL2> gl2opt = gi.getGL2();
      switch (gl2opt.type) {
        case OPTION_NONE:
        {
          break;
        }
        case OPTION_SOME:
        {
          final Some<JCGLInterfaceGL2> some = (Some<JCGLInterfaceGL2>) gl2opt;
          final JCGLInterfaceGL2 gl2 = some.value;
          final WorkingBuffers buffers =
            new WorkingBuffers(gl2, gl2, gl2, gl2);

          try {
            return this.makeGL2(buffers, gl2);
          } catch (final JCGLRuntimeException e) {
            buffers.emergencyCleanup();
            throw e;
          } catch (final ConstraintError e) {
            buffers.emergencyCleanup();
            throw e;
          }
        }
      }
    }

    {
      final Option<JCGLInterfaceGLES3> gles3opt = gi.getGLES3();
      switch (gles3opt.type) {
        case OPTION_NONE:
        {
          break;
        }
        case OPTION_SOME:
        {
          final Some<JCGLInterfaceGLES3> some =
            (Some<JCGLInterfaceGLES3>) gles3opt;
          final JCGLInterfaceGLES3 gl = some.value;
          final WorkingBuffers buffers = new WorkingBuffers(gl, gl, gl, gl);

          try {
            return this.makeGLES3(buffers, gl);
          } catch (final JCGLRuntimeException e) {
            buffers.emergencyCleanup();
            throw e;
          } catch (final ConstraintError e) {
            buffers.emergencyCleanup();
            throw e;
          }
        }
      }
    }

    {
      final Option<JCGLInterfaceGLES2> gles2opt = gi.getGLES2();
      switch (gles2opt.type) {
        case OPTION_NONE:
        {
          break;
        }
        case OPTION_SOME:
        {
          final Some<JCGLInterfaceGLES2> some =
            (Some<JCGLInterfaceGLES2>) gles2opt;
          final JCGLInterfaceGLES2 gl = some.value;
          final WorkingBuffers buffers = new WorkingBuffers(gl, gl, gl, gl);

          try {
            return this.makeES2(buffers, gl);
          } catch (final JCGLRuntimeException e) {
            buffers.emergencyCleanup();
            throw e;
          } catch (final ConstraintError e) {
            buffers.emergencyCleanup();
            throw e;
          }
        }
      }
    }

    throw new UnreachableCodeException();
  }

  private Indeterminate<Framebuffer, FramebufferStatus> makeES2(
    final @Nonnull WorkingBuffers buffers,
    final @Nonnull JCGLInterfaceGLES2 gl)
    throws JCGLRuntimeException,
      ConstraintError
  {
    /**
     * Allocate the initial framebuffer.
     */

    buffers.framebuffer = gl.framebufferAllocate();

    /**
     * The existence of the packed depth/stencil extension almost inevitably
     * means that the implementation doesn't support separate depth/stencil
     * buffers.
     */

    final Option<JCGLExtensionPackedDepthStencil> support =
      gl.extensionPackedDepthStencil();

    switch (support.type) {
      case OPTION_NONE:
      {
        return this.makeES2_NoDS(buffers, gl);
      }
      case OPTION_SOME:
      {
        final Some<JCGLExtensionPackedDepthStencil> some =
          (Option.Some<JCGLExtensionPackedDepthStencil>) support;
        return this.makeES2_DS(buffers, gl, some.value);
      }
    }

    throw new UnreachableCodeException();
  }

  /**
   * Construct a framebuffer using ES2 and the packed depth/stencil extension.
   * 
   * @throws ConstraintError
   * @throws JCGLRuntimeException
   */

  private Indeterminate<Framebuffer, FramebufferStatus> makeES2_DS(
    final @Nonnull WorkingBuffers buffers,
    final @Nonnull JCGLInterfaceGLES2 gl,
    final JCGLExtensionPackedDepthStencil extension)
    throws JCGLRuntimeException,
      ConstraintError
  {
    this.allocateColorBuffers_ES2(buffers, gl);
    this.allocateDepthStencil_ES2(buffers, extension);
    FramebufferConfigurationGL3ES2Actual.attachBuffers_ES2(
      buffers,
      gl,
      extension);
    return this.validateAndMakeState(buffers, gl);
  }

  /**
   * Construct a framebuffer using ES2 without the packed depth/stencil
   * extension.
   * 
   * @throws ConstraintError
   * @throws JCGLRuntimeException
   */

  private Indeterminate<Framebuffer, FramebufferStatus> makeES2_NoDS(
    final @Nonnull WorkingBuffers buffers,
    final @Nonnull JCGLInterfaceGLES2 gl)
    throws JCGLRuntimeException,
      ConstraintError
  {
    this.allocateColorBuffers_ES2(buffers, gl);
    this.allocateDepth_ES2(buffers, gl);
    this.allocateStencil_ES2(buffers, gl);
    FramebufferConfigurationGL3ES2Actual.attachBuffers_ES2(buffers, gl, null);
    return this.validateAndMakeState(buffers, gl);
  }

  private @Nonnull Indeterminate<Framebuffer, FramebufferStatus> makeGL2(
    final @Nonnull WorkingBuffers buffers,
    final @Nonnull JCGLInterfaceGL2 gl)
    throws JCGLRuntimeException,
      ConstraintError
  {
    buffers.framebuffer = gl.framebufferAllocate();
    this.allocateColorBuffers_GL3ES3(buffers, gl);
    this.allocateDepthStencil_GL3ES3(buffers, gl);
    FramebufferConfigurationGL3ES2Actual.attachBuffers_GL3(buffers, gl);
    return this.validateAndMakeState(buffers, gl);
  }

  /**
   * Construct a framebuffer using OpenGL 3.0.
   * 
   * @throws ConstraintError
   * @throws JCGLRuntimeException
   */

  private
    <G extends JCGLFramebuffersGL3 & JCGLRenderbuffersGL3 & JCGLTextures2DStaticCommon & JCGLTexturesCubeStaticCommon>
    Indeterminate<Framebuffer, FramebufferStatus>
    makeGL3(
      final @Nonnull WorkingBuffers buffers,
      final @Nonnull G gl)
      throws JCGLRuntimeException,
        ConstraintError
  {
    buffers.framebuffer = gl.framebufferAllocate();
    this.allocateColorBuffers_GL3ES3(buffers, gl);
    this.allocateDepthStencil_GL3ES3(buffers, gl);
    FramebufferConfigurationGL3ES2Actual.attachBuffers_GL3(buffers, gl);
    return this.validateAndMakeState(buffers, gl);
  }

  private @Nonnull Indeterminate<Framebuffer, FramebufferStatus> makeGLES3(
    final @Nonnull WorkingBuffers buffers,
    final @Nonnull JCGLInterfaceGLES3 gl)
    throws JCGLRuntimeException,
      ConstraintError
  {
    buffers.framebuffer = gl.framebufferAllocate();
    this.allocateColorBuffers_GL3ES3(buffers, gl);
    this.allocateDepthStencil_GL3ES3(buffers, gl);
    FramebufferConfigurationGL3ES2Actual.attachBuffers_GL3(buffers, gl);
    return this.validateAndMakeState(buffers, gl);
  }

  @Override public void requestBestRGBAColorRenderbuffer()
  {
    this.want_color = RequestColor.WANT_COLOR_RGBA_RENDERBUFFER;
    this.want_color_shared = null;
  }

  @Override public void requestBestRGBAColorTexture2D(
    final @Nonnull TextureWrapS texture_wrap_s,
    final @Nonnull TextureWrapT texture_wrap_t,
    final @Nonnull TextureFilterMinification texture_min_filter,
    final @Nonnull TextureFilterMagnification texture_mag_filter)
    throws ConstraintError
  {
    Constraints.constrainNotNull(texture_wrap_s, "Wrap S");
    Constraints.constrainNotNull(texture_wrap_t, "Wrap T");
    Constraints.constrainNotNull(texture_mag_filter, "Magnification filter");
    Constraints.constrainNotNull(texture_min_filter, "Minification filter");

    this.want_color = RequestColor.WANT_COLOR_RGBA_TEXTURE_2D;
    this.want_color_shared = null;
    this.wrap_s = texture_wrap_s;
    this.wrap_t = texture_wrap_t;
    this.mag_filter = texture_mag_filter;
    this.min_filter = texture_min_filter;
  }

  @Override public void requestBestRGBAColorTextureCube(
    final @Nonnull TextureWrapR texture_wrap_r,
    final @Nonnull TextureWrapS texture_wrap_s,
    final @Nonnull TextureWrapT texture_wrap_t,
    final @Nonnull TextureFilterMinification texture_min_filter,
    final @Nonnull TextureFilterMagnification texture_mag_filter)
    throws ConstraintError
  {
    Constraints.constrainArbitrary(
      this.width == this.height,
      "Framebuffer width == height required for cube maps");
    Constraints.constrainNotNull(texture_wrap_r, "Wrap R");
    Constraints.constrainNotNull(texture_wrap_s, "Wrap S");
    Constraints.constrainNotNull(texture_wrap_t, "Wrap T");
    Constraints.constrainNotNull(texture_mag_filter, "Magnification filter");
    Constraints.constrainNotNull(texture_min_filter, "Minification filter");

    this.want_color = RequestColor.WANT_COLOR_RGBA_TEXTURE_CUBE;
    this.want_color_shared = null;
    this.wrap_r = texture_wrap_r;
    this.wrap_s = texture_wrap_s;
    this.wrap_t = texture_wrap_t;
    this.mag_filter = texture_mag_filter;
    this.min_filter = texture_min_filter;
  }

  @Override public void requestBestRGBColorRenderbuffer()
  {
    this.want_color = RequestColor.WANT_COLOR_RGB_RENDERBUFFER;
    this.want_color_shared = null;
  }

  @Override public void requestBestRGBColorTexture2D(
    final @Nonnull TextureWrapS texture_wrap_s,
    final @Nonnull TextureWrapT texture_wrap_t,
    final @Nonnull TextureFilterMinification texture_min_filter,
    final @Nonnull TextureFilterMagnification texture_mag_filter)
    throws ConstraintError
  {
    Constraints.constrainNotNull(texture_wrap_s, "Wrap S");
    Constraints.constrainNotNull(texture_wrap_t, "Wrap T");
    Constraints.constrainNotNull(texture_mag_filter, "Magnification filter");
    Constraints.constrainNotNull(texture_min_filter, "Minification filter");

    this.want_color = RequestColor.WANT_COLOR_RGB_TEXTURE_2D;
    this.want_color_shared = null;
    this.wrap_s = texture_wrap_s;
    this.wrap_t = texture_wrap_t;
    this.mag_filter = texture_mag_filter;
    this.min_filter = texture_min_filter;
  }

  @Override public void requestBestRGBColorTextureCube(
    final @Nonnull TextureWrapR texture_wrap_r,
    final @Nonnull TextureWrapS texture_wrap_s,
    final @Nonnull TextureWrapT texture_wrap_t,
    final @Nonnull TextureFilterMinification texture_min_filter,
    final @Nonnull TextureFilterMagnification texture_mag_filter)
    throws ConstraintError
  {
    Constraints.constrainArbitrary(
      this.width == this.height,
      "Framebuffer width == height required for cube maps");
    Constraints.constrainNotNull(texture_wrap_r, "Wrap R");
    Constraints.constrainNotNull(texture_wrap_s, "Wrap S");
    Constraints.constrainNotNull(texture_wrap_t, "Wrap T");
    Constraints.constrainNotNull(texture_mag_filter, "Magnification filter");
    Constraints.constrainNotNull(texture_min_filter, "Minification filter");

    this.want_color = RequestColor.WANT_COLOR_RGB_TEXTURE_CUBE;
    this.want_color_shared = null;
    this.wrap_r = texture_wrap_r;
    this.wrap_s = texture_wrap_s;
    this.wrap_t = texture_wrap_t;
    this.mag_filter = texture_mag_filter;
    this.min_filter = texture_min_filter;
  }

  @Override public void requestDepthRenderbuffer()
  {
    this.want_depth = RequestDepth.WANT_DEPTH_RENDERBUFFER;
    this.want_depth_shared = null;
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
    final @Nonnull FramebufferUsable framebuffer,
    final @Nonnull FramebufferColorAttachmentPoint attachment)
    throws ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");

    final AttachmentColor attach = framebuffer.getColorAttachment(attachment);
    switch (attach.type) {
      case ATTACHMENT_COLOR_RENDERBUFFER:
      {
        final AttachmentColorRenderbuffer a =
          (AttachmentColorRenderbuffer) attach;
        this.want_color_shared =
          new AttachmentSharedColorRenderbuffer(a.getRenderbuffer());
        break;
      }
      case ATTACHMENT_COLOR_TEXTURE_2D:
      {
        final AttachmentColorTexture2DStatic a =
          (AttachmentColorTexture2DStatic) attach;
        this.want_color_shared =
          new AttachmentSharedColorTexture2DStatic(a.getTexture2D());
        break;
      }
      case ATTACHMENT_COLOR_TEXTURE_CUBE:
      {
        final AttachmentColorTextureCubeStatic a =
          (AttachmentColorTextureCubeStatic) attach;
        this.want_color_shared =
          new AttachmentSharedColorTextureCubeStatic(
            a.getTextureCube(),
            a.getFace());
        break;
      }
      case ATTACHMENT_SHARED_COLOR_RENDERBUFFER:
      {
        final AttachmentSharedColorRenderbuffer a =
          (AttachmentSharedColorRenderbuffer) attach;
        this.want_color_shared = new AttachmentSharedColorRenderbuffer(a);
        break;
      }
      case ATTACHMENT_SHARED_COLOR_TEXTURE_2D:
      {
        final AttachmentSharedColorTexture2DStatic a =
          (AttachmentSharedColorTexture2DStatic) attach;
        this.want_color_shared = new AttachmentSharedColorTexture2DStatic(a);
        break;
      }
      case ATTACHMENT_SHARED_COLOR_TEXTURE_CUBE:
      {
        final AttachmentSharedColorTextureCubeStatic a =
          (AttachmentSharedColorTextureCubeStatic) attach;
        this.want_color_shared =
          new AttachmentSharedColorTextureCubeStatic(a);
        break;
      }
    }

    this.want_color = RequestColor.WANT_COLOR_SHARED_WITH;
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
    this.want_stencil = RequestStencil.WANT_STENCIL_RENDERBUFFER;
    this.want_stencil_shared = null;
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
    builder.append("[FramebufferConfigurationES2 want_depth=");
    builder.append(this.want_depth);
    builder.append(", want_stencil=");
    builder.append(this.want_stencil);
    builder.append(", want_color=");
    builder.append(this.want_color);
    builder.append(", width=");
    builder.append(this.width);
    builder.append(", height=");
    builder.append(this.height);
    builder.append(", mag_filter=");
    builder.append(this.mag_filter);
    builder.append(", min_filter=");
    builder.append(this.min_filter);
    builder.append(", wrap_r=");
    builder.append(this.wrap_r);
    builder.append(", wrap_s=");
    builder.append(this.wrap_s);
    builder.append(", wrap_t=");
    builder.append(this.wrap_t);
    builder.append("]");
    return builder.toString();
  }

  /**
   * <p>
   * Validate the constructed framebuffer, and return a completed framebuffer
   * structure if validation succeeds.
   * </p>
   */

  private Indeterminate<Framebuffer, FramebufferStatus> validateAndMakeState(
    final @Nonnull WorkingBuffers buffers,
    final @Nonnull JCGLFramebuffersCommon fb)
    throws JCGLRuntimeException,
      ConstraintError
  {
    final FramebufferStatus status =
      fb.framebufferDrawValidate(buffers.framebuffer);

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
        buffers.emergencyCleanup();
        return new Failure<Framebuffer, FramebufferStatus>(status);
      }
    }

    final List<FramebufferColorAttachmentPoint> points =
      fb.framebufferGetColorAttachmentPoints();

    final Framebuffer state =
      new Framebuffer(buffers.framebuffer, this.width, this.height);

    if (buffers.attachment_color != null) {
      state.configAddColorAttachment(points.get(0), buffers.attachment_color);
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
