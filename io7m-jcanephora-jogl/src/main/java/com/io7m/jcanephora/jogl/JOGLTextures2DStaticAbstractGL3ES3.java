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
import com.io7m.jcanephora.Texture2DStaticType;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureFormat;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.api.JCGLTextures2DStaticGL3ES3Type;
import com.io7m.jlog.LogUsableType;

abstract class JOGLTextures2DStaticAbstractGL3ES3 extends
  JOGLTextures2DStaticAbstract implements JCGLTextures2DStaticGL3ES3Type
{
  JOGLTextures2DStaticAbstractGL3ES3(
    final GL in_gl,
    final LogUsableType in_log,
    final JOGLIntegerCacheType in_icache,
    final JOGLLogMessageCacheType in_tcache)
  {
    super(in_gl, in_log, in_icache, in_tcache);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateDepth16(
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
      TextureFormat.TEXTURE_FORMAT_DEPTH_16_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateDepth24(
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
      TextureFormat.TEXTURE_FORMAT_DEPTH_24_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final
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

  @Override public final Texture2DStaticType texture2DStaticAllocateDepth32f(
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
      TextureFormat.TEXTURE_FORMAT_DEPTH_32F_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateR16f(
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
      TextureFormat.TEXTURE_FORMAT_R_16F_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateR16I(
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
      TextureFormat.TEXTURE_FORMAT_R_16I_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateR16U(
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
      TextureFormat.TEXTURE_FORMAT_R_16U_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateR32f(
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
      TextureFormat.TEXTURE_FORMAT_R_32F_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateR32I(
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
      TextureFormat.TEXTURE_FORMAT_R_32I_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateR32U(
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
      TextureFormat.TEXTURE_FORMAT_R_32U_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateR8(
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
      TextureFormat.TEXTURE_FORMAT_R_8_1BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateR8I(
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
      TextureFormat.TEXTURE_FORMAT_R_8I_1BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateR8U(
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
      TextureFormat.TEXTURE_FORMAT_R_8U_1BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateRG16f(
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
      TextureFormat.TEXTURE_FORMAT_RG_16F_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateRG16I(
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
      TextureFormat.TEXTURE_FORMAT_RG_16I_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateRG16U(
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
      TextureFormat.TEXTURE_FORMAT_RG_16U_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateRG32f(
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
      TextureFormat.TEXTURE_FORMAT_RG_32F_8BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateRG32I(
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
      TextureFormat.TEXTURE_FORMAT_RG_32I_8BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateRG32U(
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
      TextureFormat.TEXTURE_FORMAT_RG_32U_8BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateRG8(
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
      TextureFormat.TEXTURE_FORMAT_RG_8_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateRG8I(
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
      TextureFormat.TEXTURE_FORMAT_RG_8I_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateRG8U(
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
      TextureFormat.TEXTURE_FORMAT_RG_8U_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateRGB16f(
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
      TextureFormat.TEXTURE_FORMAT_RGB_16F_6BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateRGB16I(
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
      TextureFormat.TEXTURE_FORMAT_RGB_16I_6BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateRGB16U(
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
      TextureFormat.TEXTURE_FORMAT_RGB_16U_6BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateRGB32f(
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
      TextureFormat.TEXTURE_FORMAT_RGB_32F_12BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateRGB32I(
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
      TextureFormat.TEXTURE_FORMAT_RGB_32I_12BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateRGB32U(
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
      TextureFormat.TEXTURE_FORMAT_RGB_32U_12BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateRGB8(
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

  @Override public final Texture2DStaticType texture2DStaticAllocateRGB8I(
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
      TextureFormat.TEXTURE_FORMAT_RGB_8I_3BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateRGB8U(
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
      TextureFormat.TEXTURE_FORMAT_RGB_8U_3BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final
    Texture2DStaticType
    texture2DStaticAllocateRGBA1010102(
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
      TextureFormat.TEXTURE_FORMAT_RGBA_1010102_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateRGBA16f(
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
      TextureFormat.TEXTURE_FORMAT_RGBA_16F_8BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateRGBA16I(
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
      TextureFormat.TEXTURE_FORMAT_RGBA_16I_8BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateRGBA16U(
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
      TextureFormat.TEXTURE_FORMAT_RGBA_16U_8BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateRGBA32f(
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
      TextureFormat.TEXTURE_FORMAT_RGBA_32F_16BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateRGBA32I(
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
      TextureFormat.TEXTURE_FORMAT_RGBA_32I_16BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateRGBA32U(
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
      TextureFormat.TEXTURE_FORMAT_RGBA_32U_16BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateRGBA8(
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

  @Override public final Texture2DStaticType texture2DStaticAllocateRGBA8I(
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
      TextureFormat.TEXTURE_FORMAT_RGBA_8I_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public final Texture2DStaticType texture2DStaticAllocateRGBA8U(
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
      TextureFormat.TEXTURE_FORMAT_RGBA_8U_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }
}
