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
import javax.media.opengl.GL2GL3;

import com.io7m.jcanephora.CubeMapFaceLH;
import com.io7m.jcanephora.CubeMapFaceRH;
import com.io7m.jcanephora.JCGLExceptionDeleted;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.JCGLExceptionWrongContext;
import com.io7m.jcanephora.TextureCubeStaticReadableType;
import com.io7m.jcanephora.TextureCubeStaticType;
import com.io7m.jcanephora.TextureCubeStaticUsableType;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureFormat;
import com.io7m.jcanephora.TextureWrapR;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.api.JCGLTexturesCubeStaticGL3Type;
import com.io7m.jcanephora.jogl.JOGL_TextureSpecs.TextureSpec;
import com.io7m.jlog.LogUsableType;

final class JOGLTexturesCubeStaticGL3 extends
  JOGLTexturesCubeStaticAbstractGL3ES3 implements
  JCGLTexturesCubeStaticGL3Type
{
  public JOGLTexturesCubeStaticGL3(
    final GL in_gl,
    final LogUsableType in_log,
    final JOGLIntegerCacheType in_icache,
    final JOGLLogMessageCacheType in_tcache)
  {
    super(in_gl, in_log, in_icache, in_tcache);
  }

  @Override protected TextureSpec getTextureSpec(
    final TextureFormat type)
  {
    return JOGL_TextureSpecs.getGL3TextureSpec(type);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateR16(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return this.textureCubeStaticAllocate(
      name,
      size,
      TextureFormat.TEXTURE_FORMAT_R_16_2BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateRG16(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return this.textureCubeStaticAllocate(
      name,
      size,
      TextureFormat.TEXTURE_FORMAT_RG_16_4BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateRGB16(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return this.textureCubeStaticAllocate(
      name,
      size,
      TextureFormat.TEXTURE_FORMAT_RGB_16_6BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public
    TextureCubeStaticType
    textureCubeStaticAllocateRGBA1010102(
      final String name,
      final int size,
      final TextureWrapR wrap_r,
      final TextureWrapS wrap_s,
      final TextureWrapT wrap_t,
      final TextureFilterMinification min_filter,
      final TextureFilterMagnification mag_filter)
      throws JCGLExceptionRuntime
  {
    return this.textureCubeStaticAllocate(
      name,
      size,
      TextureFormat.TEXTURE_FORMAT_RGBA_1010102_4BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateRGBA16(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return this.textureCubeStaticAllocate(
      name,
      size,
      TextureFormat.TEXTURE_FORMAT_RGBA_16_8BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticReadableType textureCubeStaticGetImageLH(
    final TextureCubeStaticUsableType texture,
    final CubeMapFaceLH face)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    JOGLTexturesCubeStaticAbstract.checkTexture(this.getContext(), texture);

    final GL2GL3 g = this.getGL().getGL2GL3();

    final TextureSpec spec =
      JOGL_TextureSpecs.getGL3TextureSpec(texture.textureGetFormat());
    final JOGLTextureCubeReadableData td =
      new JOGLTextureCubeReadableData(
        texture.textureGetFormat(),
        texture.textureGetArea());

    final int face_i = JOGLTypeConversions.cubeFaceToGL(face);

    g.glBindTexture(GL.GL_TEXTURE_CUBE_MAP, texture.getGLName());
    g.glGetTexImage(
      face_i,
      0,
      spec.getFormat(),
      spec.getType(),
      td.targetData());
    g.glBindTexture(GL.GL_TEXTURE_CUBE_MAP, 0);
    JOGLErrors.check(g);
    return td;
  }

  @Override public TextureCubeStaticReadableType textureCubeStaticGetImageRH(
    final TextureCubeStaticUsableType texture,
    final CubeMapFaceRH face)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    return this.textureCubeStaticGetImageLH(
      texture,
      CubeMapFaceLH.fromRH(face));
  }
}
