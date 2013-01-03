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

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnimplementedCodeException;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jaux.functional.Indeterminate;
import com.io7m.jaux.functional.Indeterminate.Failure;
import com.io7m.jaux.functional.Indeterminate.Success;
import com.io7m.jaux.functional.Option;
import com.io7m.jaux.functional.Option.Some;

/**
 * Instructions to create an ES2 compatible framebuffer configuration (using
 * OpenGL 3.0 features if available).
 */

@NotThreadSafe public final class FramebufferConfigurationES2
{
  private static enum RequestColor
  {
    WANT_NOTHING,
    WANT_COLOR_BEST_RGB_RENDERBUFFER,
    WANT_COLOR_BEST_RGBA_RENDERBUFFER,
    WANT_COLOR_SPECIFIC_RENDERBUFFER,
    WANT_COLOR_BEST_RGB_TEXTURE_2D,
    WANT_COLOR_BEST_RGBA_TEXTURE_2D,
    WANT_COLOR_SPECIFIC_TEXTURE_2D,
    WANT_COLOR_SHARED_WITH,
    WANT_COLOR_BEST_RGB_TEXTURE_CUBE,
    WANT_COLOR_BEST_RGBA_TEXTURE_CUBE,
    WANT_COLOR_SPECIFIC_TEXTURE_CUBE,
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
    @CheckForNull FramebufferReference framebuffer                = null;
    @CheckForNull Texture2DStatic      color_texture_2d           = null;
    @CheckForNull TextureCubeStatic    color_texture_cube         = null;
    @CheckForNull Renderbuffer         color_renderbuffer         = null;
    @CheckForNull Renderbuffer         depth_stencil_renderbuffer = null;
    @CheckForNull Renderbuffer         depth_renderbuffer         = null;
    @CheckForNull Renderbuffer         stencil_renderbuffer       = null;
    final @Nonnull StringBuilder       text;

    WorkingBuffers()
    {
      this.text = new StringBuilder();
    }

    /**
     * A simple sanity check to ensure that each possible allocation path is
     * correct.
     */

    boolean allocationsConsistent()
    {
      /**
       * If there's a depth/stencil buffer, then there cannot be a separate
       * depth buffer.
       */

      if ((this.depth_stencil_renderbuffer != null)
        && (this.depth_renderbuffer != null)) {
        return false;
      }

      /**
       * If there's a depth/stencil buffer, then there cannot be a separate
       * stencil buffer.
       */

      if ((this.depth_stencil_renderbuffer != null)
        && (this.stencil_renderbuffer != null)) {
        return false;
      }

      /**
       * If there's a color texture 2D, then there cannot be a separate color
       * renderbuffer.
       */

      if ((this.color_texture_2d != null)
        && (this.color_renderbuffer != null)) {
        return false;
      }

      /**
       * If there's a color texture 2D, then there cannot be a separate color
       * texture cube.
       */

      if ((this.color_texture_2d != null)
        && (this.color_texture_cube != null)) {
        return false;
      }

      /**
       * If there's a color renderbuffer, then there cannot be a separate
       * color texture cube.
       */

      if ((this.color_renderbuffer != null)
        && (this.color_texture_cube != null)) {
        return false;
      }

      return true;
    }

    void emergencyCleanup(
      final @Nonnull GLInterfaceES2 gl)
      throws ConstraintError,
        GLException
    {
      GLException saved = null;

      if (this.framebuffer != null) {
        try {
          gl.framebufferDelete(this.framebuffer);
        } catch (final GLException e) {
          saved = e;
        }
      }
      if (this.color_texture_2d != null) {
        try {
          gl.texture2DStaticDelete(this.color_texture_2d);
        } catch (final GLException e) {
          saved = e;
        }
      }
      if (this.color_texture_cube != null) {
        try {
          gl.textureCubeStaticDelete(this.color_texture_cube);
        } catch (final GLException e) {
          saved = e;
        }
      }
      if (this.color_renderbuffer != null) {
        try {
          gl.renderbufferDelete(this.color_renderbuffer);
        } catch (final GLException e) {
          saved = e;
        }
      }
      if (this.depth_stencil_renderbuffer != null) {
        try {
          gl.renderbufferDelete(this.depth_stencil_renderbuffer);
        } catch (final GLException e) {
          saved = e;
        }
      }
      if (this.depth_renderbuffer != null) {
        try {
          gl.renderbufferDelete(this.depth_renderbuffer);
        } catch (final GLException e) {
          saved = e;
        }
      }
      if (this.stencil_renderbuffer != null) {
        try {
          gl.renderbufferDelete(this.stencil_renderbuffer);
        } catch (final GLException e) {
          saved = e;
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
   * @throws GLException
   * @throws ConstraintError
   */

  private static void attachBuffers_ES2(
    final @Nonnull WorkingBuffers buffers,
    final @Nonnull GLInterfaceES2 gl,
    final @CheckForNull GLExtensionPackedDepthStencil extension)
    throws GLException,
      ConstraintError
  {
    gl.framebufferDrawBind(buffers.framebuffer);

    if (buffers.color_renderbuffer != null) {
      gl.framebufferDrawAttachColorRenderbuffer(
        buffers.framebuffer,
        buffers.color_renderbuffer);
    }
    if (buffers.color_texture_2d != null) {
      gl.framebufferDrawAttachColorTexture2D(
        buffers.framebuffer,
        buffers.color_texture_2d);
    }
    if (buffers.color_texture_cube != null) {
      gl.framebufferDrawAttachColorTextureCube(
        buffers.framebuffer,
        buffers.color_texture_cube,
        CubeMapFace.CUBE_MAP_POSITIVE_X);
    }
    if (buffers.depth_stencil_renderbuffer != null) {
      assert extension != null;
      extension.framebufferDrawAttachDepthStencilRenderbuffer(
        buffers.framebuffer,
        buffers.depth_stencil_renderbuffer);
    } else {
      if (buffers.depth_renderbuffer != null) {
        gl.framebufferDrawAttachDepthRenderbuffer(
          buffers.framebuffer,
          buffers.depth_renderbuffer);
      }
      if (buffers.stencil_renderbuffer != null) {
        gl.framebufferDrawAttachStencilRenderbuffer(
          buffers.framebuffer,
          buffers.stencil_renderbuffer);
      }
    }
  }

  /**
   * Attach all allocated buffers to the current framebuffer on OpenGL 3.0.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  private static void attachBuffers_GL3(
    final @Nonnull WorkingBuffers buffers,
    final @Nonnull GLInterface3 gl)
    throws GLException,
      ConstraintError
  {
    gl.framebufferDrawBind(buffers.framebuffer);

    if (buffers.color_renderbuffer != null) {
      gl.framebufferDrawAttachColorRenderbuffer(
        buffers.framebuffer,
        buffers.color_renderbuffer);
    }
    if (buffers.color_texture_2d != null) {
      gl.framebufferDrawAttachColorTexture2D(
        buffers.framebuffer,
        buffers.color_texture_2d);
    }
    if (buffers.color_texture_cube != null) {
      gl.framebufferDrawAttachColorTextureCube(
        buffers.framebuffer,
        buffers.color_texture_cube,
        CubeMapFace.CUBE_MAP_POSITIVE_X);
    }
    if (buffers.depth_stencil_renderbuffer != null) {
      gl.framebufferDrawAttachDepthStencilRenderbuffer(
        buffers.framebuffer,
        buffers.depth_stencil_renderbuffer);
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

  private @Nonnull RequestDepth             want_depth;
  private @Nonnull RequestStencil           want_stencil;
  private @Nonnull RequestColor             want_color;
  private @CheckForNull RequestColorTypeES2 want_color_specific;
  private final int                         width;
  private final int                         height;
  private @Nonnull TextureFilter            mag_filter;
  private @Nonnull TextureFilter            min_filter;
  private @Nonnull TextureWrap              wrap_r;
  private @Nonnull TextureWrap              wrap_s;
  private @Nonnull TextureWrap              wrap_t;

  /**
   * Construct a configuration that will, by default, ask for the best RGBA
   * color texture available from the implementation, and for whatever depth
   * buffer is available.
   * 
   * The framebuffer will be of width <code>width</code> and height
   * <code>height</code>.
   * 
   * @throws ConstraintError
   *           Iff <code>width &lt; 1 || height &lt; 1</code>
   */

  public FramebufferConfigurationES2(
    final int width,
    final int height)
    throws ConstraintError
  {
    this.width =
      Constraints.constrainRange(width, 1, Integer.MAX_VALUE, "Width");
    this.height =
      Constraints.constrainRange(height, 1, Integer.MAX_VALUE, "Height");
    this.want_stencil = RequestStencil.WANT_NOTHING;
    this.want_depth = RequestDepth.WANT_DEPTH_RENDERBUFFER;
    this.want_color = RequestColor.WANT_COLOR_BEST_RGBA_TEXTURE_2D;
    this.want_color_specific = null;

    this.wrap_r = TextureWrap.TEXTURE_WRAP_REPEAT;
    this.wrap_s = TextureWrap.TEXTURE_WRAP_REPEAT;
    this.wrap_t = TextureWrap.TEXTURE_WRAP_REPEAT;
    this.mag_filter = TextureFilter.TEXTURE_FILTER_NEAREST;
    this.min_filter = TextureFilter.TEXTURE_FILTER_NEAREST;
  }

  private Renderbuffer allocateBestRGBARenderbuffer_ES2(
    final @Nonnull GLInterfaceES2 gl)
    throws ConstraintError,
      GLException
  {
    return gl.renderbufferAllocateRGBA4444(this.width, this.height);
  }

  private Renderbuffer allocateBestRGBARenderbuffer_GL3(
    final @Nonnull GLInterface3 gl)
    throws ConstraintError,
      GLException
  {
    return gl.renderbufferAllocateRGBA8888(this.width, this.height);
  }

  private Texture2DStatic allocateBestRGBATexture2D(
    final @Nonnull GLInterfaceES2 gl,
    final @Nonnull StringBuilder name)
    throws ConstraintError,
      GLException
  {
    return gl.texture2DStaticAllocateRGBA8888(
      name.toString(),
      this.width,
      this.height,
      this.wrap_s,
      this.wrap_t,
      this.min_filter,
      this.mag_filter);
  }

  private @Nonnull TextureCubeStatic allocateBestRGBATextureCube(
    final @Nonnull GLInterfaceES2 gl,
    final @Nonnull StringBuilder name)
    throws GLException,
      ConstraintError
  {
    return gl.textureCubeStaticAllocateRGBA8888(
      name.toString(),
      this.width,
      this.wrap_r,
      this.wrap_s,
      this.wrap_t,
      this.min_filter,
      this.mag_filter);
  }

  private Renderbuffer allocateBestRGBRenderbuffer_ES2(
    final @Nonnull GLInterfaceES2 gl)
    throws ConstraintError,
      GLException
  {
    return gl.renderbufferAllocateRGB565(this.width, this.height);
  }

  private Renderbuffer allocateBestRGBRenderbuffer_GL3(
    final @Nonnull GLInterface3 gl)
    throws ConstraintError,
      GLException
  {
    return gl.renderbufferAllocateRGB888(this.width, this.height);
  }

  private Texture2DStatic allocateBestRGBTexture2D(
    final @Nonnull GLInterfaceES2 gl,
    final @Nonnull StringBuilder name)
    throws ConstraintError,
      GLException
  {
    return gl.texture2DStaticAllocateRGB888(
      name.toString(),
      this.width,
      this.height,
      this.wrap_s,
      this.wrap_t,
      this.min_filter,
      this.mag_filter);
  }

  private TextureCubeStatic allocateBestRGBTextureCube(
    final @Nonnull GLInterfaceES2 gl,
    final @Nonnull StringBuilder name)
    throws GLException,
      ConstraintError
  {
    return gl.textureCubeStaticAllocateRGB888(
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

  private void allocateColorBuffers_ES2(
    final @Nonnull WorkingBuffers buffers,
    final @Nonnull GLInterfaceES2 gl)
    throws GLException,
      ConstraintError
  {
    FramebufferConfigurationES2.makeTexture2DName(buffers);

    switch (this.want_color) {
      case WANT_COLOR_BEST_RGBA_RENDERBUFFER:
      {
        buffers.color_renderbuffer =
          this.allocateBestRGBARenderbuffer_ES2(gl);
        break;
      }
      case WANT_COLOR_BEST_RGBA_TEXTURE_2D:
      {
        buffers.color_texture_2d =
          this.allocateBestRGBATexture2D(gl, buffers.text);
        break;
      }
      case WANT_COLOR_BEST_RGBA_TEXTURE_CUBE:
      {
        buffers.color_texture_cube =
          this.allocateBestRGBATextureCube(gl, buffers.text);
        break;
      }
      case WANT_COLOR_BEST_RGB_RENDERBUFFER:
      {
        buffers.color_renderbuffer = this.allocateBestRGBRenderbuffer_ES2(gl);
        break;
      }
      case WANT_COLOR_BEST_RGB_TEXTURE_2D:
      {
        buffers.color_texture_2d =
          this.allocateBestRGBTexture2D(gl, buffers.text);
        break;
      }
      case WANT_COLOR_BEST_RGB_TEXTURE_CUBE:
      {
        buffers.color_texture_cube =
          this.allocateBestRGBTextureCube(gl, buffers.text);
        break;
      }
      case WANT_COLOR_SHARED_WITH:
      {
        throw new UnimplementedCodeException();
      }
      case WANT_COLOR_SPECIFIC_RENDERBUFFER:
      {
        buffers.color_renderbuffer =
          this.allocateSpecificColorRenderbuffer(gl);
        break;
      }
      case WANT_COLOR_SPECIFIC_TEXTURE_2D:
      {
        buffers.color_texture_2d =
          this.allocateSpecificColorTexture2D(gl, buffers.text);
        break;
      }
      case WANT_COLOR_SPECIFIC_TEXTURE_CUBE:
      {
        buffers.color_texture_cube =
          this.allocateSpecificColorTextureCube(gl, buffers.text);
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

  private void allocateColorBuffers_GL3(
    final @Nonnull WorkingBuffers buffers,
    final @Nonnull GLInterface3 gl)
    throws GLException,
      ConstraintError
  {
    switch (this.want_color) {
      case WANT_COLOR_BEST_RGBA_RENDERBUFFER:
      {
        buffers.color_renderbuffer =
          this.allocateBestRGBARenderbuffer_GL3(gl);
        break;
      }
      case WANT_COLOR_BEST_RGBA_TEXTURE_2D:
      {
        buffers.color_texture_2d =
          this.allocateBestRGBATexture2D(gl, buffers.text);
        break;
      }
      case WANT_COLOR_BEST_RGBA_TEXTURE_CUBE:
      {
        buffers.color_texture_cube =
          this.allocateBestRGBATextureCube(gl, buffers.text);
        break;
      }
      case WANT_COLOR_BEST_RGB_RENDERBUFFER:
      {
        buffers.color_renderbuffer = this.allocateBestRGBRenderbuffer_GL3(gl);
        break;
      }
      case WANT_COLOR_BEST_RGB_TEXTURE_2D:
      {
        buffers.color_texture_2d =
          this.allocateBestRGBTexture2D(gl, buffers.text);
        break;
      }
      case WANT_COLOR_BEST_RGB_TEXTURE_CUBE:
      {
        buffers.color_texture_cube =
          this.allocateBestRGBTextureCube(gl, buffers.text);
        break;
      }
      case WANT_COLOR_SHARED_WITH:
      {
        throw new UnimplementedCodeException();
      }
      case WANT_COLOR_SPECIFIC_RENDERBUFFER:
      {
        buffers.color_renderbuffer =
          this.allocateSpecificColorRenderbuffer(gl);
        break;
      }
      case WANT_COLOR_SPECIFIC_TEXTURE_2D:
      {
        buffers.color_texture_2d =
          this.allocateSpecificColorTexture2D(gl, buffers.text);
        break;
      }
      case WANT_COLOR_SPECIFIC_TEXTURE_CUBE:
      {
        buffers.color_texture_cube =
          this.allocateSpecificColorTextureCube(gl, buffers.text);
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
   * @throws GLException
   */

  private void allocateDepth_ES2(
    final @Nonnull WorkingBuffers buffers,
    final @Nonnull GLInterfaceES2 gl)
    throws GLException,
      ConstraintError
  {
    switch (this.want_depth) {
      case WANT_DEPTH_RENDERBUFFER:
      {
        buffers.depth_renderbuffer =
          gl.renderbufferAllocateDepth16(this.width, this.height);
        break;
      }
      case WANT_DEPTH_SHARED_WITH:
      {
        throw new UnimplementedCodeException();
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
   * @throws GLException
   */

  private void allocateDepthStencil_ES2(
    final @Nonnull WorkingBuffers buffers,
    final GLExtensionPackedDepthStencil extension)
    throws GLException,
      ConstraintError
  {
    if ((this.want_depth == RequestDepth.WANT_DEPTH_RENDERBUFFER)
      || (this.want_stencil == RequestStencil.WANT_STENCIL_RENDERBUFFER)) {
      buffers.depth_stencil_renderbuffer =
        extension
          .renderbufferAllocateDepth24Stencil8(this.width, this.height);
      return;
    }

    if ((this.want_depth == RequestDepth.WANT_DEPTH_SHARED_WITH)
      || (this.want_stencil == RequestStencil.WANT_STENCIL_SHARED_WITH)) {
      throw new UnimplementedCodeException();
    }
  }

  /**
   * Allocate a depth/stencil buffer using the packed depth/stencil format, if
   * requested.
   * 
   * @throws ConstraintError
   * @throws GLException
   */

  private void allocateDepthStencil_GL3(
    final @Nonnull WorkingBuffers buffers,
    final @Nonnull GLInterface3 gl)
    throws GLException,
      ConstraintError
  {
    if ((this.want_depth == RequestDepth.WANT_DEPTH_RENDERBUFFER)
      || (this.want_stencil == RequestStencil.WANT_STENCIL_RENDERBUFFER)) {
      buffers.depth_stencil_renderbuffer =
        gl.renderbufferAllocateDepth24Stencil8(this.width, this.height);
      return;
    }

    if ((this.want_depth == RequestDepth.WANT_DEPTH_SHARED_WITH)
      || (this.want_stencil == RequestStencil.WANT_STENCIL_SHARED_WITH)) {
      throw new UnimplementedCodeException();
    }
  }

  private Renderbuffer allocateSpecificColorRenderbuffer(
    final @Nonnull GLInterfaceES2 gl)
    throws ConstraintError,
      GLException
  {
    Renderbuffer color_rb = null;

    switch (this.want_color_specific) {
      case REQUEST_ES2_COLOR_RGB565:
      {
        color_rb = gl.renderbufferAllocateRGB565(this.width, this.height);
        break;
      }
      case REQUEST_ES2_COLOR_RGBA4444:
      {
        color_rb = gl.renderbufferAllocateRGBA4444(this.width, this.height);
        break;
      }
      case REQUEST_ES2_COLOR_RGBA5551:
      {
        color_rb = gl.renderbufferAllocateRGBA5551(this.width, this.height);
        break;
      }
    }

    return color_rb;
  }

  private Texture2DStatic allocateSpecificColorTexture2D(
    final @Nonnull GLInterfaceES2 gl,
    final @Nonnull StringBuilder name)
    throws ConstraintError,
      GLException
  {
    Texture2DStatic color_texture = null;

    switch (this.want_color_specific) {
      case REQUEST_ES2_COLOR_RGB565:
      {
        color_texture =
          gl.texture2DStaticAllocateRGB565(
            name.toString(),
            this.width,
            this.height,
            this.wrap_s,
            this.wrap_t,
            this.min_filter,
            this.mag_filter);
        break;
      }
      case REQUEST_ES2_COLOR_RGBA4444:
      {
        color_texture =
          gl.texture2DStaticAllocateRGBA4444(
            name.toString(),
            this.width,
            this.height,
            this.wrap_s,
            this.wrap_t,
            this.min_filter,
            this.mag_filter);
        break;
      }
      case REQUEST_ES2_COLOR_RGBA5551:
      {
        color_texture =
          gl.texture2DStaticAllocateRGBA5551(
            name.toString(),
            this.width,
            this.height,
            this.wrap_s,
            this.wrap_t,
            this.min_filter,
            this.mag_filter);
        break;
      }
    }

    return color_texture;
  }

  private TextureCubeStatic allocateSpecificColorTextureCube(
    final @Nonnull GLInterfaceES2 gl,
    final @Nonnull StringBuilder name)
    throws GLException,
      ConstraintError
  {
    TextureCubeStatic color_texture = null;

    switch (this.want_color_specific) {
      case REQUEST_ES2_COLOR_RGB565:
      {
        color_texture =
          gl.textureCubeStaticAllocateRGB565(
            name.toString(),
            this.width,
            this.wrap_r,
            this.wrap_s,
            this.wrap_t,
            this.min_filter,
            this.mag_filter);
        break;
      }
      case REQUEST_ES2_COLOR_RGBA4444:
      {
        color_texture =
          gl.textureCubeStaticAllocateRGBA4444(
            name.toString(),
            this.width,
            this.wrap_r,
            this.wrap_s,
            this.wrap_t,
            this.min_filter,
            this.mag_filter);
        break;
      }
      case REQUEST_ES2_COLOR_RGBA5551:
      {
        color_texture =
          gl.textureCubeStaticAllocateRGBA5551(
            name.toString(),
            this.width,
            this.wrap_r,
            this.wrap_s,
            this.wrap_t,
            this.min_filter,
            this.mag_filter);
        break;
      }
    }

    return color_texture;
  }

  /**
   * Allocate a stencil buffer on ES2.
   * 
   * @throws ConstraintError
   * @throws GLException
   */

  private void allocateStencil_ES2(
    final @Nonnull WorkingBuffers buffers,
    final @Nonnull GLInterfaceES2 gl)
    throws GLException,
      ConstraintError
  {
    switch (this.want_stencil) {
      case WANT_NOTHING:
      {
        break;
      }
      case WANT_STENCIL_RENDERBUFFER:
      {
        buffers.stencil_renderbuffer =
          gl.renderbufferAllocateStencil8(this.width, this.height);
        break;
      }
      case WANT_STENCIL_SHARED_WITH:
      {
        throw new UnimplementedCodeException();
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
    final FramebufferConfigurationES2 other =
      (FramebufferConfigurationES2) obj;
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
    if (this.want_color_specific != other.want_color_specific) {
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

  /**
   * Retrieve the height of the resulting framebuffer.
   */

  public int getHeight()
  {
    return this.height;
  }

  /**
   * Retrieve the width of the resulting framebuffer.
   */

  public int getWidth()
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
    result =
      (prime * result)
        + ((this.want_color_specific == null) ? 0 : this.want_color_specific
          .hashCode());
    result = (prime * result) + this.want_depth.hashCode();
    result = (prime * result) + this.want_stencil.hashCode();
    result = (prime * result) + this.width;
    result = (prime * result) + this.wrap_r.hashCode();
    result = (prime * result) + this.wrap_s.hashCode();
    result = (prime * result) + this.wrap_t.hashCode();
    return result;
  }

  /**
   * Construct a framebuffer based on the current configuration.
   * 
   * @throws ConstraintError
   *           Iff <code>gi == null</code>.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull Indeterminate<Framebuffer, FramebufferStatus> make(
    final @Nonnull GLImplementation gi)
    throws GLException,
      ConstraintError
  {
    final WorkingBuffers buffers = new WorkingBuffers();

    if (gi.implementationProvidesGL3()) {
      final GLInterface3 gl = gi.implementationGetGL3();

      try {
        return this.makeGL3(buffers, gl);
      } catch (final GLException e) {
        buffers.emergencyCleanup(gl);
        throw e;
      } catch (final ConstraintError e) {
        buffers.emergencyCleanup(gl);
        throw e;
      }
    }

    final GLInterfaceES2 gl = gi.implementationGetGLES2();

    try {
      return this.makeES2(buffers, gl);
    } catch (final GLException e) {
      buffers.emergencyCleanup(gl);
      throw e;
    } catch (final ConstraintError e) {
      buffers.emergencyCleanup(gl);
      throw e;
    }
  }

  private Indeterminate<Framebuffer, FramebufferStatus> makeES2(
    final @Nonnull WorkingBuffers buffers,
    final @Nonnull GLInterfaceES2 gl)
    throws GLException,
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

    final Option<GLExtensionPackedDepthStencil> support =
      gl.extensionPackedDepthStencil().extensionGetSupport();

    switch (support.type) {
      case OPTION_NONE:
      {
        return this.makeES2_NoDS(buffers, gl);
      }
      case OPTION_SOME:
      {
        final Some<GLExtensionPackedDepthStencil> some =
          (Option.Some<GLExtensionPackedDepthStencil>) support;
        return this.makeES2_DS(buffers, gl, some.value);
      }
    }

    throw new UnreachableCodeException();
  }

  /**
   * Construct a framebuffer using ES2 and the packed depth/stencil extension.
   * 
   * @throws ConstraintError
   * @throws GLException
   */

  private Indeterminate<Framebuffer, FramebufferStatus> makeES2_DS(
    final @Nonnull WorkingBuffers buffers,
    final @Nonnull GLInterfaceES2 gl,
    final GLExtensionPackedDepthStencil extension)
    throws GLException,
      ConstraintError
  {
    this.allocateColorBuffers_ES2(buffers, gl);
    this.allocateDepthStencil_ES2(buffers, extension);
    assert buffers.allocationsConsistent();
    FramebufferConfigurationES2.attachBuffers_ES2(buffers, gl, extension);
    return this.validateAndMakeState(buffers, gl);
  }

  /**
   * Construct a framebuffer using ES2 without the packed depth/stencil
   * extension.
   * 
   * @throws ConstraintError
   * @throws GLException
   */

  private Indeterminate<Framebuffer, FramebufferStatus> makeES2_NoDS(
    final @Nonnull WorkingBuffers buffers,
    final @Nonnull GLInterfaceES2 gl)
    throws GLException,
      ConstraintError
  {
    this.allocateColorBuffers_ES2(buffers, gl);
    this.allocateDepth_ES2(buffers, gl);
    this.allocateStencil_ES2(buffers, gl);
    assert buffers.allocationsConsistent();
    FramebufferConfigurationES2.attachBuffers_ES2(buffers, gl, null);
    return this.validateAndMakeState(buffers, gl);
  }

  /**
   * Construct a framebuffer using OpenGL 3.0.
   * 
   * @throws ConstraintError
   * @throws GLException
   */

  private Indeterminate<Framebuffer, FramebufferStatus> makeGL3(
    final @Nonnull WorkingBuffers buffers,
    final @Nonnull GLInterface3 gl)
    throws GLException,
      ConstraintError
  {
    buffers.framebuffer = gl.framebufferAllocate();
    this.allocateColorBuffers_GL3(buffers, gl);
    this.allocateDepthStencil_GL3(buffers, gl);
    assert buffers.allocationsConsistent();
    FramebufferConfigurationES2.attachBuffers_GL3(buffers, gl);
    return this.validateAndMakeState(buffers, gl);
  }

  /**
   * Request the best available RGBA color renderbuffer in the resulting
   * framebuffer.
   * 
   * Note that this function is able to request RGBA renderbuffers of a better
   * quality than those given in the OpenGL ES2 specification <i>when not
   * running on an ES2 implementation</i>. This prevents programs from
   * effectively downgrading all framebuffers to OpenGL ES2 quality.
   * 
   * Essentially, the function will pick the "best" quality ES2 type when
   * running on ES2, and will pick the "best" quality OpenGL 3.0 type when
   * running on OpenGL 3.0.
   */

  public void requestBestRGBAColorRenderbuffer()
  {
    this.want_color = RequestColor.WANT_COLOR_BEST_RGBA_RENDERBUFFER;
    this.want_color_specific = null;
  }

  /**
   * Request the best available RGBA color 2D texture in the resulting
   * framebuffer.
   * 
   * Note that this function is able to request RGBA textures of a better
   * quality than those given in the OpenGL ES2 specification <i>when not
   * running on an ES2 implementation</i>. This prevents programs from
   * effectively downgrading all textures to OpenGL ES2 quality.
   * 
   * Essentially, the function will pick the "best" quality ES2 type when
   * running on ES2, and will pick the "best" quality OpenGL 3.0 type when
   * running on OpenGL 3.0.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   */

  public void requestBestRGBAColorTexture2D(
    final @Nonnull TextureWrap texture_wrap_s,
    final @Nonnull TextureWrap texture_wrap_t,
    final @Nonnull TextureFilter texture_min_filter,
    final @Nonnull TextureFilter texture_mag_filter)
    throws ConstraintError
  {
    this.want_color = RequestColor.WANT_COLOR_BEST_RGBA_TEXTURE_2D;
    this.want_color_specific = null;
    this.wrap_s = Constraints.constrainNotNull(texture_wrap_s, "Wrap S");
    this.wrap_t = Constraints.constrainNotNull(texture_wrap_t, "Wrap T");
    this.mag_filter =
      Constraints
        .constrainNotNull(texture_mag_filter, "Magnification filter");
    this.min_filter =
      Constraints.constrainNotNull(texture_min_filter, "Minification filter");
  }

  /**
   * Request the best available RGBA color cube-map texture in the resulting
   * framebuffer.
   * 
   * Note that this function is able to request RGBA textures of a better
   * quality than those given in the OpenGL ES2 specification <i>when not
   * running on an ES2 implementation</i>. This prevents programs from
   * effectively downgrading all textures to OpenGL ES2 quality.
   * 
   * Essentially, the function will pick the "best" quality ES2 type when
   * running on ES2, and will pick the "best" quality OpenGL 3.0 type when
   * running on OpenGL 3.0.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>, or if the
   *           framebuffer width is not equal to its height (cube maps must be
   *           square).
   */

  public void requestBestRGBAColorTextureCube(
    final @Nonnull TextureWrap texture_wrap_r,
    final @Nonnull TextureWrap texture_wrap_s,
    final @Nonnull TextureWrap texture_wrap_t,
    final @Nonnull TextureFilter texture_min_filter,
    final @Nonnull TextureFilter texture_mag_filter)
    throws ConstraintError
  {
    Constraints.constrainArbitrary(
      this.width == this.height,
      "Framebuffer width == height required for cube maps");

    this.want_color = RequestColor.WANT_COLOR_BEST_RGBA_TEXTURE_CUBE;

    this.wrap_r = Constraints.constrainNotNull(texture_wrap_r, "Wrap R");
    this.wrap_s = Constraints.constrainNotNull(texture_wrap_s, "Wrap S");
    this.wrap_t = Constraints.constrainNotNull(texture_wrap_t, "Wrap T");

    this.mag_filter =
      Constraints
        .constrainNotNull(texture_mag_filter, "Magnification filter");
    this.min_filter =
      Constraints.constrainNotNull(texture_min_filter, "Minification filter");
  }

  /**
   * Request the best available RGB color renderbuffer in the resulting
   * framebuffer.
   * 
   * Note that this function is able to request RGB renderbuffers of a better
   * quality than those given in the OpenGL ES2 specification <i>when not
   * running on an ES2 implementation</i>. This prevents programs from
   * effectively downgrading all framebuffers to OpenGL ES2 quality.
   * 
   * Essentially, the function will pick the "best" quality ES2 type when
   * running on ES2, and will pick the "best" quality OpenGL 3.0 type when
   * running on OpenGL 3.0.
   */

  public void requestBestRGBColorRenderbuffer()
  {
    this.want_color = RequestColor.WANT_COLOR_BEST_RGB_RENDERBUFFER;
    this.want_color_specific = null;
  }

  /**
   * Request the best available RGB color 2D texture in the resulting
   * framebuffer.
   * 
   * Note that this function is able to request RGB textures of a better
   * quality than those given in the OpenGL ES2 specification <i>when not
   * running on an ES2 implementation</i>. This prevents programs from
   * effectively downgrading all textures to OpenGL ES2 quality.
   * 
   * Essentially, the function will pick the "best" quality ES2 type when
   * running on ES2, and will pick the "best" quality OpenGL 3.0 type when
   * running on OpenGL 3.0.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   */

  public void requestBestRGBColorTexture2D(
    final @Nonnull TextureWrap texture_wrap_s,
    final @Nonnull TextureWrap texture_wrap_t,
    final @Nonnull TextureFilter texture_min_filter,
    final @Nonnull TextureFilter texture_mag_filter)
    throws ConstraintError
  {
    this.want_color = RequestColor.WANT_COLOR_BEST_RGB_TEXTURE_2D;
    this.want_color_specific = null;
    this.wrap_s = Constraints.constrainNotNull(texture_wrap_s, "Wrap S");
    this.wrap_t = Constraints.constrainNotNull(texture_wrap_t, "Wrap T");
    this.mag_filter =
      Constraints
        .constrainNotNull(texture_mag_filter, "Magnification filter");
    this.min_filter =
      Constraints.constrainNotNull(texture_min_filter, "Minification filter");
  }

  /**
   * Request the best available RGB color cube-map texture in the resulting
   * framebuffer.
   * 
   * Note that this function is able to request RGB textures of a better
   * quality than those given in the OpenGL ES2 specification <i>when not
   * running on an ES2 implementation</i>. This prevents programs from
   * effectively downgrading all textures to OpenGL ES2 quality.
   * 
   * Essentially, the function will pick the "best" quality ES2 type when
   * running on ES2, and will pick the "best" quality OpenGL 3.0 type when
   * running on OpenGL 3.0.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>, or if the
   *           framebuffer width is not equal to its height (cube maps must be
   *           square).
   */

  public void requestBestRGBColorTextureCube(
    final @Nonnull TextureWrap texture_wrap_r,
    final @Nonnull TextureWrap texture_wrap_s,
    final @Nonnull TextureWrap texture_wrap_t,
    final @Nonnull TextureFilter texture_min_filter,
    final @Nonnull TextureFilter texture_mag_filter)
    throws ConstraintError
  {
    Constraints.constrainArbitrary(
      this.width == this.height,
      "Framebuffer width == height required for cube maps");

    this.want_color = RequestColor.WANT_COLOR_BEST_RGB_TEXTURE_CUBE;

    this.wrap_r = Constraints.constrainNotNull(texture_wrap_r, "Wrap R");
    this.wrap_s = Constraints.constrainNotNull(texture_wrap_s, "Wrap S");
    this.wrap_t = Constraints.constrainNotNull(texture_wrap_t, "Wrap T");

    this.mag_filter =
      Constraints
        .constrainNotNull(texture_mag_filter, "Magnification filter");
    this.min_filter =
      Constraints.constrainNotNull(texture_min_filter, "Minification filter");
  }

  /**
   * Request a depth renderbuffer in the resulting framebuffer.
   */

  public void requestDepthRenderbuffer()
  {
    this.want_depth = RequestDepth.WANT_DEPTH_RENDERBUFFER;
  }

  /**
   * Request no color buffer in the resulting framebuffer.
   */

  public void requestNoColor()
  {
    this.want_color = RequestColor.WANT_NOTHING;
    this.want_color_specific = null;
  }

  /**
   * Request no depth buffer in the resulting framebuffer.
   * 
   * Due to limitations in OpenGL drivers, requesting a depth buffer may also
   * request a stencil buffer, regardless of whether or not one was desired.
   */

  public void requestNoDepth()
  {
    this.want_depth = RequestDepth.WANT_NOTHING;
  }

  /**
   * Request no stencil buffer in the resulting framebuffer.
   */

  public void requestNoStencil()
  {
    this.want_stencil = RequestStencil.WANT_NOTHING;
  }

  /**
   * Request a specific type of color renderbuffer in the resulting
   * framebuffer.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   */

  public void requestSpecificColorRenderbuffer(
    final @Nonnull RequestColorTypeES2 type)
    throws ConstraintError
  {
    this.want_color = RequestColor.WANT_COLOR_SPECIFIC_RENDERBUFFER;
    this.want_color_specific =
      Constraints.constrainNotNull(type, "Color type");
  }

  /**
   * Request a specific type of color 2D texture in the resulting framebuffer.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   */

  public void requestSpecificColorTexture2D(
    final @Nonnull RequestColorTypeES2 type,
    final @Nonnull TextureWrap texture_wrap_s,
    final @Nonnull TextureWrap texture_wrap_t,
    final @Nonnull TextureFilter texture_min_filter,
    final @Nonnull TextureFilter texture_mag_filter)
    throws ConstraintError
  {
    this.want_color = RequestColor.WANT_COLOR_SPECIFIC_TEXTURE_2D;
    this.want_color_specific =
      Constraints.constrainNotNull(type, "Color type");

    this.wrap_s = Constraints.constrainNotNull(texture_wrap_s, "Wrap S");
    this.wrap_t = Constraints.constrainNotNull(texture_wrap_t, "Wrap T");
    this.mag_filter =
      Constraints
        .constrainNotNull(texture_mag_filter, "Magnification filter");
    this.min_filter =
      Constraints.constrainNotNull(texture_min_filter, "Minification filter");
  }

  /**
   * Request a specific type of color cube-map texture in the resulting
   * framebuffer.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>, or if the
   *           framebuffer width is not equal to its height (cube maps must be
   *           square).
   */

  public void requestSpecificColorTextureCube(
    final @Nonnull RequestColorTypeES2 type,
    final @Nonnull TextureWrap texture_wrap_r,
    final @Nonnull TextureWrap texture_wrap_s,
    final @Nonnull TextureWrap texture_wrap_t,
    final @Nonnull TextureFilter texture_min_filter,
    final @Nonnull TextureFilter texture_mag_filter)
    throws ConstraintError
  {
    Constraints.constrainArbitrary(
      this.width == this.height,
      "Framebuffer width == height required for cube maps");

    this.want_color = RequestColor.WANT_COLOR_SPECIFIC_TEXTURE_CUBE;
    this.want_color_specific =
      Constraints.constrainNotNull(type, "Color type");

    this.wrap_r = Constraints.constrainNotNull(texture_wrap_r, "Wrap R");
    this.wrap_s = Constraints.constrainNotNull(texture_wrap_s, "Wrap S");
    this.wrap_t = Constraints.constrainNotNull(texture_wrap_t, "Wrap T");

    this.mag_filter =
      Constraints
        .constrainNotNull(texture_mag_filter, "Magnification filter");
    this.min_filter =
      Constraints.constrainNotNull(texture_min_filter, "Minification filter");
  }

  /**
   * Request a stencil renderbuffer in the resulting framebuffer.
   * 
   * Due to limitations in OpenGL drivers, requesting a stencil buffer may
   * also request a depth buffer, regardless of whether or not one was
   * desired.
   */

  public void requestStencilRenderbuffer()
  {
    this.want_stencil = RequestStencil.WANT_STENCIL_RENDERBUFFER;
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
    builder.append(", want_color_specific=");
    builder.append(this.want_color_specific);
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
   * Validate the constructed framebuffer, and return a completed framebuffer
   * structure if validation succeeds.
   */

  private Indeterminate<Framebuffer, FramebufferStatus> validateAndMakeState(
    final @Nonnull WorkingBuffers buffers,
    final @Nonnull GLInterfaceES2 gl)
    throws GLException,
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

    final FramebufferColorAttachmentPoint[] points =
      gl.framebufferGetColorAttachmentPoints();

    final Framebuffer state =
      new Framebuffer(buffers.framebuffer, this.width, this.height);

    if (buffers.color_renderbuffer != null) {
      state.configAddColorRenderbuffer(
        points[0],
        buffers.color_renderbuffer,
        false);
    }
    if (buffers.color_texture_2d != null) {
      state.configAddColorTexture2D(
        points[0],
        buffers.color_texture_2d,
        false);
    }
    if (buffers.color_texture_cube != null) {
      state.configAddColorTextureCube(
        points[0],
        CubeMapFace.CUBE_MAP_POSITIVE_X,
        buffers.color_texture_cube,
        false);
    }
    if (buffers.depth_stencil_renderbuffer != null) {
      state.configSetDepthStencilBuffer(
        buffers.depth_stencil_renderbuffer,
        false);
    } else {
      if (buffers.depth_renderbuffer != null) {
        state.configSetDepthBuffer(buffers.depth_renderbuffer, false);
      }
      if (buffers.stencil_renderbuffer != null) {
        state.configSetStencilBuffer(buffers.stencil_renderbuffer, false);
      }
    }

    return new Success<Framebuffer, FramebufferStatus>(state);
  }
}
