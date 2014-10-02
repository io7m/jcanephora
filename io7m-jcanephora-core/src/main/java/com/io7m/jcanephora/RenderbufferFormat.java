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

package com.io7m.jcanephora;

import java.util.EnumSet;
import java.util.Set;

import com.io7m.junreachable.UnreachableCodeException;

/**
 * The type of a renderbuffer.
 */

public enum RenderbufferFormat
{
  /**
   * Four-channel RGBA, 5 bits R, 6 bits G, 5 bits B, two bytes per pixel.
   */

  RENDERBUFFER_COLOR_RGB_565(2, 0, 0),

  /**
   * Three-channel RGB, 8 bits per channel, three bytes per pixel.
   */

  RENDERBUFFER_COLOR_RGB_888(3, 0, 0),

  /**
   * Four-channel RGBA, 4 bits per channel, two bytes per pixel.
   */

  RENDERBUFFER_COLOR_RGBA_4444(2, 0, 0),

  /**
   * Four-channel RGBA, 5 bits per (R, G, B) channels, 1 bit alpha, two bytes
   * per pixel.
   */

  RENDERBUFFER_COLOR_RGBA_5551(2, 0, 0),

  /**
   * Four-channel RGBA, 8 bits per channel, four bytes per pixel.
   */

  RENDERBUFFER_COLOR_RGBA_8888(4, 0, 0),

  /**
   * 16 bit depth buffer
   */

  RENDERBUFFER_DEPTH_16(2, 16, 0),

  /**
   * 24 bit depth buffer (implementation dependent: most likely four bytes per
   * pixel).
   */

  RENDERBUFFER_DEPTH_24(4, 24, 0),

  /**
   * Packed 24 bit depth buffer, 8 bit stencil buffer
   */

  RENDERBUFFER_DEPTH_24_STENCIL_8(4, 24, 8),

  /**
   * 8 bit stencil buffer
   */

  RENDERBUFFER_STENCIL_8(1, 0, 8);

  /**
   * Check that the given renderbuffer is of a color renderable format.
   *
   * @throws JCGLExceptionFormatError
   *           If the format does not satisfy the constraints
   * @param r
   *          The renderbuffer
   */

  public static void checkColorRenderableRenderbuffer(
    final RenderbufferUsableType<RenderableColorKind> r)
    throws JCGLExceptionFormatError
  {
    final RenderbufferFormat format = r.renderbufferGetFormat();
    if (format.isColorRenderable() == false) {
      final String m =
        String.format("Format %s is not color-renderable", format);
      assert m != null;
      throw new JCGLExceptionFormatError(m);
    }
  }

  /**
   * Check that the given renderbuffer is of a depth (not stencil) renderable
   * format.
   *
   * @throws JCGLExceptionFormatError
   *           If the format does not satisfy the constraints
   * @param r
   *          The renderbuffer
   */

  public static void checkDepthOnlyRenderableRenderbuffer(
    final RenderbufferUsableType<RenderableDepthKind> r)
    throws JCGLExceptionFormatError
  {
    final RenderbufferFormat format = r.renderbufferGetFormat();
    if (format.isDepthRenderable() == false) {
      final String m =
        String.format("Format %s is not depth-renderable", format);
      assert m != null;
      throw new JCGLExceptionFormatError(m);
    }
    if (format.isStencilRenderable()) {
      final String m =
        String
          .format(
            "Format %s is stencil-renderable: Must be used as a depth+stencil attachment",
            format);
      assert m != null;
      throw new JCGLExceptionFormatError(m);
    }
  }

  /**
   * Check that the given renderbuffer is of a depth+stencil renderable
   * format.
   *
   * @throws JCGLExceptionFormatError
   *           If the format does not satisfy the constraints
   * @param r
   *          The renderbuffer
   */

  public static void checkDepthStencilRenderableRenderbuffer(
    final RenderbufferUsableType<RenderableDepthStencilKind> r)
    throws JCGLExceptionFormatError
  {
    final RenderbufferFormat format = r.renderbufferGetFormat();
    if ((format.isDepthRenderable() == false)
      || (format.isStencilRenderable() == false)) {
      final String m =
        String.format("Format %s is not depth+stencil-renderable", format);
      assert m != null;
      throw new JCGLExceptionFormatError(m);
    }
  }

  /**
   * Check that the given renderbuffer is of a stencil renderable format.
   *
   * @throws JCGLExceptionFormatError
   *           If the format does not satisfy the constraints
   * @param r
   *          The renderbuffer
   */

  public static void checkStencilRenderableRenderbuffer(
    final RenderbufferUsableType<RenderableStencilKind> r)
    throws JCGLExceptionFormatError
  {
    final RenderbufferFormat format = r.renderbufferGetFormat();
    if (format.isStencilRenderable() == false) {
      final String m =
        String.format("Format %s is not stencil-renderable", format);
      assert m != null;
      throw new JCGLExceptionFormatError(m);
    }
  }

  /**
   * @return The subset of renderbuffer types supported by both OpenGL 3.* and
   *         ES 3.*.
   */

  public static Set<RenderbufferFormat> getGL3ES3Types()
  {
    final EnumSet<RenderbufferFormat> r =
      EnumSet.of(
        RENDERBUFFER_DEPTH_16,
        RENDERBUFFER_DEPTH_24,
        RENDERBUFFER_DEPTH_24_STENCIL_8,
        RENDERBUFFER_COLOR_RGBA_8888,
        RENDERBUFFER_COLOR_RGB_888);
    assert r != null;
    return r;
  }

  /**
   * @return The subset of renderbuffer types supported by OpenGL 3.*.
   */

  public static Set<RenderbufferFormat> getGL3Types()
  {
    final EnumSet<RenderbufferFormat> r =
      EnumSet.of(
        RENDERBUFFER_DEPTH_16,
        RENDERBUFFER_DEPTH_24,
        RENDERBUFFER_DEPTH_24_STENCIL_8,
        RENDERBUFFER_COLOR_RGBA_8888,
        RENDERBUFFER_COLOR_RGB_888);
    assert r != null;
    return r;
  }

  /**
   * @return The subset of renderbuffer types supported by OpenGL ES2.
   */

  public static Set<RenderbufferFormat> getGLES2Types()
  {
    final EnumSet<RenderbufferFormat> r =
      EnumSet.of(
        RENDERBUFFER_DEPTH_16,
        RENDERBUFFER_STENCIL_8,
        RENDERBUFFER_COLOR_RGBA_5551,
        RENDERBUFFER_COLOR_RGBA_4444,
        RENDERBUFFER_COLOR_RGB_565);
    assert r != null;
    return r;
  }

  /**
   * @return The set of renderbuffer types supported by ES 3.*.
   */

  public static Set<RenderbufferFormat> getGLES3Types()
  {
    final Set<RenderbufferFormat> e0 = RenderbufferFormat.getGLES2Types();
    final Set<RenderbufferFormat> e1 = RenderbufferFormat.getGL3Types();
    final EnumSet<RenderbufferFormat> s0 =
      EnumSet.noneOf(RenderbufferFormat.class);
    s0.addAll(e0);
    s0.addAll(e1);
    return s0;
  }

  private final int bytes_per_pixel;
  private final int depth_bits;
  private final int stencil_bits;

  private RenderbufferFormat(
    final int in_bytes_per_pixel,
    final int in_depth_bits,
    final int in_stencil_bits)
  {
    this.bytes_per_pixel = in_bytes_per_pixel;
    this.depth_bits = in_depth_bits;
    this.stencil_bits = in_stencil_bits;
  }

  /**
   * @return The number of bytes per pixel that this texture type requires.
   */

  public int getBytesPerPixel()
  {
    return this.bytes_per_pixel;
  }

  /**
   * @return The number of depth bits in the format
   */

  public final int getDepthBits()
  {
    return this.depth_bits;
  }

  /**
   * @return The number of stencil bits in the format
   */

  public final int getStencilBits()
  {
    return this.stencil_bits;
  }

  /**
   * @return <code>true</code> iff this type is color-renderable.
   */

  public boolean isColorRenderable()
  {
    switch (this) {
      case RENDERBUFFER_COLOR_RGBA_4444:
      case RENDERBUFFER_COLOR_RGBA_5551:
      case RENDERBUFFER_COLOR_RGB_565:
      case RENDERBUFFER_COLOR_RGBA_8888:
      case RENDERBUFFER_COLOR_RGB_888:
        return true;
      case RENDERBUFFER_DEPTH_16:
      case RENDERBUFFER_DEPTH_24:
      case RENDERBUFFER_STENCIL_8:
      case RENDERBUFFER_DEPTH_24_STENCIL_8:
        return false;
    }

    throw new UnreachableCodeException();
  }

  /**
   * @return <code>true</code> iff this type is depth-renderable.
   */

  public boolean isDepthRenderable()
  {
    switch (this) {
      case RENDERBUFFER_COLOR_RGBA_4444:
      case RENDERBUFFER_COLOR_RGBA_5551:
      case RENDERBUFFER_COLOR_RGB_565:
      case RENDERBUFFER_STENCIL_8:
      case RENDERBUFFER_COLOR_RGBA_8888:
      case RENDERBUFFER_COLOR_RGB_888:
        return false;
      case RENDERBUFFER_DEPTH_16:
      case RENDERBUFFER_DEPTH_24:
      case RENDERBUFFER_DEPTH_24_STENCIL_8:
        return true;
    }

    throw new UnreachableCodeException();
  }

  /**
   * @return <code>true</code> iff this type is stencil-renderable.
   */

  public boolean isStencilRenderable()
  {
    switch (this) {
      case RENDERBUFFER_COLOR_RGBA_4444:
      case RENDERBUFFER_COLOR_RGBA_5551:
      case RENDERBUFFER_COLOR_RGB_565:
      case RENDERBUFFER_DEPTH_16:
      case RENDERBUFFER_COLOR_RGBA_8888:
      case RENDERBUFFER_COLOR_RGB_888:
      case RENDERBUFFER_DEPTH_24:
        return false;
      case RENDERBUFFER_STENCIL_8:
      case RENDERBUFFER_DEPTH_24_STENCIL_8:
        return true;
    }

    throw new UnreachableCodeException();
  }
}
