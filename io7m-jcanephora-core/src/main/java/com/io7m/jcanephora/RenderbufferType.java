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

import com.io7m.jaux.UnreachableCodeException;

/**
 * The type of a renderbuffer.
 */

public enum RenderbufferType
{
  /**
   * 16 bit depth buffer
   */

  RENDERBUFFER_DEPTH_16,

  /**
   * 8 bit stencil buffer
   */

  RENDERBUFFER_STENCIL_8,

  /**
   * Packed 24 bit depth buffer, 8 bit stencil buffer
   */

  RENDERBUFFER_DEPTH_24_STENCIL_8,

  /**
   * Four-channel RGBA, 5 bits per (R, G, B) channels, 1 bit alpha, two bytes
   * per pixel.
   */

  RENDERBUFFER_COLOR_RGBA_5551,

  /**
   * Four-channel RGBA, 4 bits per channel, two bytes per pixel.
   */

  RENDERBUFFER_COLOR_RGBA_4444,

  /**
   * Four-channel RGBA, 5 bits R, 6 bits G, 5 bits B, two bytes per pixel.
   */

  RENDERBUFFER_COLOR_RGB_565,

  /**
   * Four-channel RGBA, 8 bits per channel, four bytes per pixel.
   */

  RENDERBUFFER_COLOR_RGBA_8888,

  /**
   * Three-channel RGB, 8 bits per channel, three bytes per pixel.
   */

  RENDERBUFFER_COLOR_RGB_888;

  /**
   * The subset of renderbuffer types supported by ES2
   */

  static final @Nonnull RenderbufferType[] ES2_TYPES;

  static {
    ES2_TYPES =
      new RenderbufferType[] {
    RENDERBUFFER_DEPTH_16,
    RENDERBUFFER_STENCIL_8,
    RENDERBUFFER_COLOR_RGBA_5551,
    RENDERBUFFER_COLOR_RGBA_4444,
    RENDERBUFFER_COLOR_RGB_565 };
  }

  /**
   * Retrieve a list of the texture types supported by ES2
   */

  public static @Nonnull RenderbufferType[] getES2Types()
  {
    final RenderbufferType[] est =
      new RenderbufferType[RenderbufferType.ES2_TYPES.length];
    for (int index = 0; index < RenderbufferType.ES2_TYPES.length; ++index) {
      est[index] = RenderbufferType.ES2_TYPES[index];
    }
    return est;
  }

  /**
   * Return <code>true</code> iff this type is color-renderable.
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
      case RENDERBUFFER_STENCIL_8:
      case RENDERBUFFER_DEPTH_24_STENCIL_8:
        return false;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Return <code>true</code> iff this type is depth-renderable.
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
      case RENDERBUFFER_DEPTH_24_STENCIL_8:
        return true;
    }

    throw new UnreachableCodeException();
  }

  /**
   * Return <code>true</code> iff this type is stencil-renderable.
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
        return false;
      case RENDERBUFFER_STENCIL_8:
      case RENDERBUFFER_DEPTH_24_STENCIL_8:
        return true;
    }

    throw new UnreachableCodeException();
  }
}
