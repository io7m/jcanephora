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
import javax.media.opengl.GL;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Option;
import com.io7m.jlog.Log;

/**
 * The depth texture extension.
 */

class ExtDepthCubeTexture<G extends GL> implements
  JCGLExtensionDepthCubeTexture
{
  public static @Nonnull
    <G extends GL>
    Option<JCGLExtensionDepthCubeTexture>
    create(
      final @Nonnull G g,
      final @Nonnull JCGLStateCache state,
      final @Nonnull JCGLNamedExtensions extensions,
      final @Nonnull Log log)
      throws ConstraintError
  {
    final String names[] = { "GL_OES_depth_texture_cube_map" };

    for (final String name : names) {
      if (extensions.extensionIsSupported(name)) {
        log.debug("Extension " + name + " is available");
        return new Option.Some<JCGLExtensionDepthCubeTexture>(
          new ExtDepthCubeTexture<G>(g, state, log));
      }
      log.debug("Extension " + name + " is not available");
    }

    return new Option.None<JCGLExtensionDepthCubeTexture>();
  }

  private final @Nonnull JCGLStateCache cache;
  private final @Nonnull G              gl;
  private final @Nonnull Log            log;

  ExtDepthCubeTexture(
    final @Nonnull G gl,
    final @Nonnull JCGLStateCache cache,
    final @Nonnull Log log)
  {
    this.gl = gl;
    this.cache = cache;
    this.log = log;
  }

  @Override public TextureCubeStatic textureCubeStaticAllocateDepth16(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES2_Functions.textureCubeStaticAllocate(
      this.gl,
      this.cache,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_DEPTH_16_2BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }
}
