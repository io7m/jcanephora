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

package com.io7m.jcanephora.jogl;

import javax.annotation.Nonnull;
import javax.media.opengl.GL;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Option;
import com.io7m.jcanephora.JCGLExtensionNames;
import com.io7m.jcanephora.JCGLRuntimeException;
import com.io7m.jcanephora.JCGLStateCache;
import com.io7m.jcanephora.Texture2DStatic;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureFormat;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.api.JCGLExtensionESDepthTexture;
import com.io7m.jcanephora.api.JCGLNamedExtensionsType;
import com.io7m.jlog.Log;

/**
 * The depth texture extension (OpenGL ES variant).
 */

class ExtESDepthTexture<G extends GL> implements JCGLExtensionESDepthTexture
{
  public static @Nonnull
    <G extends GL>
    Option<JCGLExtensionESDepthTexture>
    create(
      final @Nonnull G g,
      final @Nonnull JCGLStateCache state,
      final @Nonnull JCGLNamedExtensionsType extensions,
      final @Nonnull Log log)
      throws ConstraintError
  {
    final String names[] = { JCGLExtensionNames.GL_OES_DEPTH_TEXTURE };

    for (final String name : names) {
      if (extensions.extensionIsVisible(name)) {
        return new Option.Some<JCGLExtensionESDepthTexture>(
          new ExtESDepthTexture<G>(g, state, log));
      }
    }

    return new Option.None<JCGLExtensionESDepthTexture>();
  }

  private final @Nonnull JCGLStateCache cache;
  private final @Nonnull G              gl;
  private final @Nonnull Log            log;

  private ExtESDepthTexture(
    final @Nonnull G gl1,
    final @Nonnull JCGLStateCache cache1,
    final @Nonnull Log log1)
  {
    this.gl = gl1;
    this.cache = cache1;
    this.log = log1;
  }

  @Override public JOGLTexture2DStatic texture2DStaticAllocateDepth16(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLRuntimeException
  {
    return JOGL_GLES2_Functions.texture2DStaticAllocate(
      this.gl,
      this.cache,
      this.log,
      name,
      width,
      height,
      TextureFormat.TEXTURE_FORMAT_DEPTH_16_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }
}
