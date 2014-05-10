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

import com.io7m.junreachable.UnreachableCodeException;

/**
 * The names of commonly available extensions.
 */

public final class JCGLExtensionNames
{
  /**
   * GL_ARB_DEPTH_TEXTURE
   */

  public static final String GL_ARB_DEPTH_TEXTURE           =
                                                              "GL_ARB_depth_texture";

  /**
   * GL_ARB_FRAMEBUFFER_OBJECT
   */

  public static final String GL_ARB_FRAMEBUFFER_OBJECT      =
                                                              "GL_ARB_framebuffer_object";

  /**
   * GL_EXT_COLOR_BUFFER_FLOAT
   */

  public static final String GL_EXT_COLOR_BUFFER_FLOAT      =
                                                              "GL_EXT_color_buffer_float";

  /**
   * GL_EXT_COLOR_BUFFER_HALF_FLOAT
   */

  public static final String GL_EXT_COLOR_BUFFER_HALF_FLOAT =
                                                              "GL_EXT_color_buffer_half_float";

  /**
   * GL_EXT_FRAMEBUFFER_BLIT
   */

  public static final String GL_EXT_FRAMEBUFFER_BLIT        =
                                                              "GL_EXT_framebuffer_blit";

  /**
   * GL_EXT_FRAMEBUFFER_MULTISAMPLE
   */

  public static final String GL_EXT_FRAMEBUFFER_MULTISAMPLE =
                                                              "GL_EXT_framebuffer_multisample";

  /**
   * GL_EXT_FRAMEBUFFER_OBJECT
   */

  public static final String GL_EXT_FRAMEBUFFER_OBJECT      =
                                                              "GL_EXT_framebuffer_object";

  /**
   * GL_EXT_PACKED_DEPTH_STENCIL
   */

  public static final String GL_EXT_PACKED_DEPTH_STENCIL    =
                                                              "GL_EXT_packed_depth_stencil";

  /**
   * GL_OES_DEPTH_TEXTURE
   */

  public static final String GL_OES_DEPTH_TEXTURE           =
                                                              "GL_OES_depth_texture";

  /**
   * GL_OES_DEPTH_TEXTURE_CUBE_MAP
   */

  public static final String GL_OES_DEPTH_TEXTURE_CUBE_MAP  =
                                                              "GL_OES_depth_texture_cube_map";

  /**
   * GL_OES_PACKED_DEPTH_STENCIL
   */

  public static final String GL_OES_PACKED_DEPTH_STENCIL    =
                                                              "GL_OES_packed_depth_stencil";

  private JCGLExtensionNames()
  {
    throw new UnreachableCodeException();
  }

}
