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

package com.io7m.jcanephora.fake;

import java.nio.ByteBuffer;

import com.io7m.jcanephora.AreaInclusive;
import com.io7m.jcanephora.CubeMapFaceLH;
import com.io7m.jcanephora.CubeMapFaceRH;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionDeleted;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.JCGLExceptionWrongContext;
import com.io7m.jcanephora.ResourceCheck;
import com.io7m.jcanephora.TextureCubeStaticReadableType;
import com.io7m.jcanephora.TextureCubeStaticType;
import com.io7m.jcanephora.TextureCubeStaticUpdateType;
import com.io7m.jcanephora.TextureCubeStaticUsableType;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureFormat;
import com.io7m.jcanephora.TextureUnitType;
import com.io7m.jcanephora.TextureWrapR;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.api.JCGLTexturesCubeStaticGL2Type;
import com.io7m.jcanephora.api.JCGLTexturesCubeStaticGL3Type;
import com.io7m.jcanephora.api.JCGLTexturesCubeStaticGLES3Type;
import com.io7m.jlog.LogLevel;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;

final class FakeTexturesCubeStatic implements
  JCGLTexturesCubeStaticGL2Type,
  JCGLTexturesCubeStaticGL3Type,
  JCGLTexturesCubeStaticGLES3Type
{
  /**
   * Check that the given texture:
   *
   * <ul>
   * <li>Is not null</li>
   * <li>Was created on this context (or a shared context)</li>
   * <li>Is not deleted</li>
   * </ul>
   */

  static void checkTexture(
    final FakeContext ctx,
    final TextureCubeStaticUsableType texture)
    throws JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    NullCheck.notNull(texture, "Texture");
    FakeCompatibilityChecks.checkTextureCube(ctx, texture);
    ResourceCheck.notDeleted(texture);
  }

  private final FakeContext             context;
  private final LogUsableType           log;
  private int                           pool;
  private final FakeLogMessageCacheType tcache;
  private final FakeTextureUnits        units;

  public FakeTexturesCubeStatic(
    final FakeContext in_context,
    final FakeTextureUnits in_units,
    final FakeLogMessageCacheType in_tcache,
    final LogUsableType in_log)
  {
    this.context = NullCheck.notNull(in_context, "Context");
    this.units = NullCheck.notNull(in_units, "Units");
    this.pool = 1;
    this.tcache = NullCheck.notNull(in_tcache, "Text cache");
    this.log = NullCheck.notNull(in_log, "Log").with("textures-2d-static");
  }

  private int freshID()
  {
    final int id = this.pool;
    ++this.pool;
    return id;
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateDepth16(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_DEPTH_16_2BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateDepth24(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_DEPTH_24_4BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public
    TextureCubeStaticType
    textureCubeStaticAllocateDepth24Stencil8(
      final String name,
      final int size,
      final TextureWrapR wrap_r,
      final TextureWrapS wrap_s,
      final TextureWrapT wrap_t,
      final TextureFilterMinification min_filter,
      final TextureFilterMagnification mag_filter)
      throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateDepth32f(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_DEPTH_32F_4BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateR16(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_R_16_2BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateR16f(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_R_16F_2BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateR16I(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_R_16I_2BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateR16U(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_R_16U_2BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateR32f(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_R_32F_4BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateR32I(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_R_32I_4BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateR32U(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_R_32U_4BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateR8(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_R_8_1BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateR8I(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_R_8I_1BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateR8U(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_R_8U_1BPP,
      size,
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
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RG_16_4BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateRG16f(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RG_16F_4BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateRG16I(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RG_16I_4BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateRG16U(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RG_16U_4BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateRG32f(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RG_32F_8BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateRG32I(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RG_32I_8BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateRG32U(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RG_32U_8BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateRG8(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RG_8_2BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateRG8I(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RG_8I_2BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateRG8U(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RG_8U_2BPP,
      size,
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
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGB_16_6BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateRGB16f(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGB_16F_6BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateRGB16I(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGB_16I_6BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateRGB16U(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGB_16U_6BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateRGB32f(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGB_32F_12BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateRGB32I(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGB_32I_12BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateRGB32U(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGB_32U_12BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateRGB565(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGB_565_2BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateRGB8(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGB_8_3BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateRGB8I(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGB_8I_3BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateRGB8U(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGB_8U_3BPP,
      size,
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
      throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGBA_1010102_4BPP,
      size,
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
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGBA_16_8BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateRGBA16f(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGBA_16F_8BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateRGBA16I(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGBA_16I_8BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateRGBA16U(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGBA_16U_8BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateRGBA32f(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGBA_32F_16BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateRGBA32I(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGBA_32I_16BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateRGBA32U(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGBA_32U_16BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateRGBA4444(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGBA_4444_2BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateRGBA5551(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGBA_5551_2BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateRGBA8(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateRGBA8I(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGBA_8I_4BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateRGBA8U(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLException
  {
    return new FakeTextureCubeStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGBA_8U_4BPP,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public void textureCubeStaticBind(
    final TextureUnitType unit,
    final TextureCubeStaticUsableType texture)
    throws JCGLException
  {
    NullCheck.notNull(unit, "Unit");
    FakeCompatibilityChecks.checkTextureUnit(this.context, unit);
    FakeTexturesCubeStatic.checkTexture(this.context, texture);
    this.units.bind(unit.unitGetIndex(), (FakeTextureType) texture);
  }

  @Override public void textureCubeStaticDelete(
    final TextureCubeStaticType texture)
    throws JCGLException
  {
    FakeTexturesCubeStatic.checkTexture(this.context, texture);

    final StringBuilder text = this.tcache.getTextCache();
    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("delete ");
      text.append(texture);
      final String s = text.toString();
      assert s != null;
      this.log.debug(s);
    }

    ((FakeObjectDeletable) texture).resourceSetDeleted();
  }

  @Override public TextureCubeStaticReadableType textureCubeStaticGetImageLH(
    final TextureCubeStaticUsableType texture,
    final CubeMapFaceLH face)
    throws JCGLException
  {
    return this.textureCubeStaticGetImageRH(
      texture,
      CubeMapFaceRH.fromLH(face));
  }

  @Override public TextureCubeStaticReadableType textureCubeStaticGetImageRH(
    final TextureCubeStaticUsableType texture,
    final CubeMapFaceRH face)
    throws JCGLException
  {
    FakeTexturesCubeStatic.checkTexture(this.context, texture);

    final FakeTextureCubeStatic fake = (FakeTextureCubeStatic) texture;

    ByteBuffer data = null;
    switch (face) {
      case CUBE_MAP_RH_NEGATIVE_X:
      {
        data = fake.getFaceNegativeX();
        break;
      }
      case CUBE_MAP_RH_NEGATIVE_Y:
      {
        data = fake.getFaceNegativeY();
        break;
      }
      case CUBE_MAP_RH_NEGATIVE_Z:
      {
        data = fake.getFaceNegativeZ();
        break;
      }
      case CUBE_MAP_RH_POSITIVE_X:
      {
        data = fake.getFacePositiveX();
        break;
      }
      case CUBE_MAP_RH_POSITIVE_Y:
      {
        data = fake.getFacePositiveY();
        break;
      }
      case CUBE_MAP_RH_POSITIVE_Z:
      {
        data = fake.getFacePositiveZ();
        break;
      }
    }

    assert data != null;

    final FakeTextureCubeReadableData td =
      new FakeTextureCubeReadableData(
        texture.textureGetFormat(),
        texture.textureGetArea(),
        data.asReadOnlyBuffer());

    return td;
  }

  @Override public boolean textureCubeStaticIsBound(
    final TextureUnitType unit,
    final TextureCubeStaticUsableType texture)
    throws JCGLException
  {
    NullCheck.notNull(unit, "Unit");
    FakeTexturesCubeStatic.checkTexture(this.context, texture);
    return this.units.isBound(unit.unitGetIndex(), (FakeTextureType) texture);
  }

  @Override public void textureCubeStaticUnbind(
    final TextureUnitType unit)
    throws JCGLException
  {
    FakeCompatibilityChecks.checkTextureUnit(this.context, unit);
    this.units.unbind(unit.unitGetIndex());
  }

  @Override public void textureCubeStaticUpdateLH(
    final CubeMapFaceLH face,
    final TextureCubeStaticUpdateType data)
    throws JCGLException
  {
    this.textureCubeStaticUpdateRH(CubeMapFaceRH.fromLH(face), data);
  }

  @Override public void textureCubeStaticUpdateRH(
    final CubeMapFaceRH face,
    final TextureCubeStaticUpdateType data)
    throws JCGLException
  {
    NullCheck.notNull(face, "Face");
    NullCheck.notNull(data, "Data");
    FakeTexturesCubeStatic.checkTexture(this.context, data.getTexture());

    final FakeTextureCubeStatic texture =
      (FakeTextureCubeStatic) data.getTexture();
    final int bpp = texture.textureGetFormat().getBytesPerPixel();
    final AreaInclusive target_area = texture.textureGetArea();

    ByteBuffer target_data = null;
    switch (face) {
      case CUBE_MAP_RH_NEGATIVE_X:
      {
        target_data = texture.getFaceNegativeX();
        break;
      }
      case CUBE_MAP_RH_NEGATIVE_Y:
      {
        target_data = texture.getFaceNegativeY();
        break;
      }
      case CUBE_MAP_RH_NEGATIVE_Z:
      {
        target_data = texture.getFaceNegativeZ();
        break;
      }
      case CUBE_MAP_RH_POSITIVE_X:
      {
        target_data = texture.getFacePositiveX();
        break;
      }
      case CUBE_MAP_RH_POSITIVE_Y:
      {
        target_data = texture.getFacePositiveY();
        break;
      }
      case CUBE_MAP_RH_POSITIVE_Z:
      {
        target_data = texture.getFacePositiveZ();
        break;
      }
    }

    assert target_data != null;
    FakeTextures2DStatic.copyData(
      bpp,
      data.getTargetData(),
      data.getTargetArea(),
      target_data,
      target_area);
  }
}
