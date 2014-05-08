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

import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.TextureCubeStaticType;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureFormat;
import com.io7m.jcanephora.TextureWrapR;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.api.JCGLTexturesCubeStaticGL3ES3Type;
import com.io7m.jlog.LogUsableType;

abstract class JOGLTexturesCubeStaticAbstractGL3ES3 extends
  JOGLTexturesCubeStaticAbstract implements JCGLTexturesCubeStaticGL3ES3Type
{
  public JOGLTexturesCubeStaticAbstractGL3ES3(
    final GL in_gl,
    final LogUsableType in_log,
    final JOGLIntegerCacheType in_icache,
    final JOGLLogMessageCacheType in_tcache)
  {
    super(in_gl, in_log, in_icache, in_tcache);
  }

  @Override public final
    TextureCubeStaticType
    textureCubeStaticAllocateDepth16(
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
      TextureFormat.TEXTURE_FORMAT_DEPTH_16_2BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final
    TextureCubeStaticType
    textureCubeStaticAllocateDepth24(
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
      TextureFormat.TEXTURE_FORMAT_DEPTH_24_4BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final
    TextureCubeStaticType
    textureCubeStaticAllocateDepth24Stencil8(
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
      TextureFormat.TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final
    TextureCubeStaticType
    textureCubeStaticAllocateDepth32f(
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
      TextureFormat.TEXTURE_FORMAT_DEPTH_32F_4BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final TextureCubeStaticType textureCubeStaticAllocateR16f(
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
      TextureFormat.TEXTURE_FORMAT_R_16F_2BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final TextureCubeStaticType textureCubeStaticAllocateR16I(
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
      TextureFormat.TEXTURE_FORMAT_R_16I_2BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final TextureCubeStaticType textureCubeStaticAllocateR16U(
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
      TextureFormat.TEXTURE_FORMAT_R_16U_2BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final TextureCubeStaticType textureCubeStaticAllocateR32f(
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
      TextureFormat.TEXTURE_FORMAT_R_32F_4BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final TextureCubeStaticType textureCubeStaticAllocateR32I(
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
      TextureFormat.TEXTURE_FORMAT_R_32I_4BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final TextureCubeStaticType textureCubeStaticAllocateR32U(
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
      TextureFormat.TEXTURE_FORMAT_R_32U_4BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final TextureCubeStaticType textureCubeStaticAllocateR8(
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
      TextureFormat.TEXTURE_FORMAT_R_8_1BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final TextureCubeStaticType textureCubeStaticAllocateR8I(
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
      TextureFormat.TEXTURE_FORMAT_R_8I_1BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final TextureCubeStaticType textureCubeStaticAllocateR8U(
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
      TextureFormat.TEXTURE_FORMAT_R_8U_1BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final
    TextureCubeStaticType
    textureCubeStaticAllocateRG16f(
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
      TextureFormat.TEXTURE_FORMAT_RG_16F_4BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final
    TextureCubeStaticType
    textureCubeStaticAllocateRG16I(
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
      TextureFormat.TEXTURE_FORMAT_RG_16I_4BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final
    TextureCubeStaticType
    textureCubeStaticAllocateRG16U(
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
      TextureFormat.TEXTURE_FORMAT_RG_16U_4BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final
    TextureCubeStaticType
    textureCubeStaticAllocateRG32f(
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
      TextureFormat.TEXTURE_FORMAT_RG_32F_8BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final
    TextureCubeStaticType
    textureCubeStaticAllocateRG32I(
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
      TextureFormat.TEXTURE_FORMAT_RG_32I_8BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final
    TextureCubeStaticType
    textureCubeStaticAllocateRG32U(
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
      TextureFormat.TEXTURE_FORMAT_RG_32U_8BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final TextureCubeStaticType textureCubeStaticAllocateRG8(
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
      TextureFormat.TEXTURE_FORMAT_RG_8_2BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final TextureCubeStaticType textureCubeStaticAllocateRG8I(
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
      TextureFormat.TEXTURE_FORMAT_RG_8I_2BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final TextureCubeStaticType textureCubeStaticAllocateRG8U(
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
      TextureFormat.TEXTURE_FORMAT_RG_8U_2BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final
    TextureCubeStaticType
    textureCubeStaticAllocateRGB16f(
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
      TextureFormat.TEXTURE_FORMAT_RGB_16F_6BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final
    TextureCubeStaticType
    textureCubeStaticAllocateRGB16I(
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
      TextureFormat.TEXTURE_FORMAT_RGB_16I_6BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final
    TextureCubeStaticType
    textureCubeStaticAllocateRGB16U(
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
      TextureFormat.TEXTURE_FORMAT_RGB_16U_6BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final
    TextureCubeStaticType
    textureCubeStaticAllocateRGB32f(
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
      TextureFormat.TEXTURE_FORMAT_RGB_32F_12BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final
    TextureCubeStaticType
    textureCubeStaticAllocateRGB32I(
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
      TextureFormat.TEXTURE_FORMAT_RGB_32I_12BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final
    TextureCubeStaticType
    textureCubeStaticAllocateRGB32U(
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
      TextureFormat.TEXTURE_FORMAT_RGB_32U_12BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final TextureCubeStaticType textureCubeStaticAllocateRGB8(
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
      TextureFormat.TEXTURE_FORMAT_RGB_8_3BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final
    TextureCubeStaticType
    textureCubeStaticAllocateRGB8I(
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
      TextureFormat.TEXTURE_FORMAT_RGB_8I_3BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final
    TextureCubeStaticType
    textureCubeStaticAllocateRGB8U(
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
      TextureFormat.TEXTURE_FORMAT_RGB_8U_3BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final
    TextureCubeStaticType
    textureCubeStaticAllocateRGBA16f(
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
      TextureFormat.TEXTURE_FORMAT_RGBA_16F_8BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final
    TextureCubeStaticType
    textureCubeStaticAllocateRGBA16I(
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
      TextureFormat.TEXTURE_FORMAT_RGBA_16I_8BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final
    TextureCubeStaticType
    textureCubeStaticAllocateRGBA16U(
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
      TextureFormat.TEXTURE_FORMAT_RGBA_16U_8BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final
    TextureCubeStaticType
    textureCubeStaticAllocateRGBA32f(
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
      TextureFormat.TEXTURE_FORMAT_RGBA_32F_16BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final
    TextureCubeStaticType
    textureCubeStaticAllocateRGBA32I(
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
      TextureFormat.TEXTURE_FORMAT_RGBA_32I_16BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final
    TextureCubeStaticType
    textureCubeStaticAllocateRGBA32U(
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
      TextureFormat.TEXTURE_FORMAT_RGBA_32U_16BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final
    TextureCubeStaticType
    textureCubeStaticAllocateRGBA8(
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
      TextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final
    TextureCubeStaticType
    textureCubeStaticAllocateRGBA8I(
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
      TextureFormat.TEXTURE_FORMAT_RGBA_8I_4BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final
    TextureCubeStaticType
    textureCubeStaticAllocateRGBA8U(
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
      TextureFormat.TEXTURE_FORMAT_RGBA_8U_4BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }
}
