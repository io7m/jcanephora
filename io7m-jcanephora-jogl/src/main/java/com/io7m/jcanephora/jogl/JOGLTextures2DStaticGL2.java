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

package com.io7m.jcanephora.jogl;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLContext;

import com.io7m.jcanephora.JCGLExceptionDeleted;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.JCGLExceptionWrongContext;
import com.io7m.jcanephora.Texture2DStaticReadableType;
import com.io7m.jcanephora.Texture2DStaticType;
import com.io7m.jcanephora.Texture2DStaticUsableType;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureFormat;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.api.JCGLTextures2DStaticGL2Type;
import com.io7m.jcanephora.jogl.JOGL_TextureSpecs.TextureSpec;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;

final class JOGLTextures2DStaticGL2 extends JOGLTextures2DStaticAbstract implements
  JCGLTextures2DStaticGL2Type
{
  private final GL2 g;

  JOGLTextures2DStaticGL2(
    final GL in_gl,
    final LogUsableType in_log,
    final JOGLIntegerCacheType in_icache,
    final JOGLLogMessageCacheType in_tcache)
  {
    super(in_gl, in_log, in_icache, in_tcache);
    this.g = NullCheck.notNull(in_gl.getGL2(), "GL");
  }

  @Override protected TextureSpec getTextureSpec(
    final TextureFormat type)
  {
    return JOGL_TextureSpecs.getGL3TextureSpec(type);
  }

  @Override public
    Texture2DStaticType
    texture2DStaticAllocateDepth24Stencil8(
      final String name,
      final int width,
      final int height,
      final TextureWrapS wrap_s,
      final TextureWrapT wrap_t,
      final TextureFilterMinification min_filter,
      final TextureFilterMagnification mag_filter)
      throws JCGLExceptionRuntime
  {
    return this.texture2DStaticAllocate(
      name,
      width,
      height,
      TextureFormat.TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRGB8(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return this.texture2DStaticAllocate(
      name,
      width,
      height,
      TextureFormat.TEXTURE_FORMAT_RGB_8_3BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRGBA8(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return this.texture2DStaticAllocate(
      name,
      width,
      height,
      TextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticReadableType texture2DStaticGetImage(
    final Texture2DStaticUsableType texture)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    final GLContext context = this.getContext();
    JOGLTextures2DStaticAbstract.checkTexture(context, texture);

    final TextureSpec spec =
      JOGL_TextureSpecs.getGL3TextureSpec(texture.textureGetFormat());
    final JOGLTexture2DReadableData td =
      new JOGLTexture2DReadableData(
        texture.textureGetFormat(),
        texture.textureGetArea());

    this.g.glBindTexture(GL.GL_TEXTURE_2D, texture.getGLName());
    this.g.glGetTexImage(
      GL.GL_TEXTURE_2D,
      0,
      spec.getFormat(),
      spec.getType(),
      td.getData());
    this.g.glBindTexture(GL.GL_TEXTURE_2D, 0);

    return td;
  }
}
