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

import com.io7m.jaux.functional.Option;

/**
 * Extensions typically supported by OpenGL ES2 implementations.
 */

public interface JCGLExtensionsGLES2
{
  /**
   * <code>GL_OES_depth_texture_cube_map</code>
   */

  public @Nonnull
    Option<JCGLExtensionDepthCubeTexture>
    extensionDepthCubeTexture();

  /**
   * <code>GL_OES_depth_texture</code>
   */

  public @Nonnull Option<JCGLExtensionESDepthTexture> extensionDepthTexture();

  /**
   * <code>GL_OES_packed_depth_stencil</code>
   */

  public @Nonnull
    Option<JCGLExtensionPackedDepthStencil>
    extensionPackedDepthStencil();
}
