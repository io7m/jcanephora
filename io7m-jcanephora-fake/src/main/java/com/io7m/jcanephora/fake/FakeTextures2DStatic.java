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
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionDeleted;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.JCGLExceptionWrongContext;
import com.io7m.jcanephora.ResourceCheck;
import com.io7m.jcanephora.Texture2DStaticReadableType;
import com.io7m.jcanephora.Texture2DStaticType;
import com.io7m.jcanephora.Texture2DStaticUpdateType;
import com.io7m.jcanephora.Texture2DStaticUsableType;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureFormat;
import com.io7m.jcanephora.TextureUnitType;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.api.JCGLTextures2DStaticGL2Type;
import com.io7m.jcanephora.api.JCGLTextures2DStaticGL3Type;
import com.io7m.jcanephora.api.JCGLTextures2DStaticGLES3Type;
import com.io7m.jlog.LogLevel;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeInclusiveL;

final class FakeTextures2DStatic implements
  JCGLTextures2DStaticGL2Type,
  JCGLTextures2DStaticGL3Type,
  JCGLTextures2DStaticGLES3Type
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
    final Texture2DStaticUsableType texture)
    throws JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    NullCheck.notNull(texture, "Texture");
    FakeCompatibilityChecks.checkTexture(ctx, texture);
    ResourceCheck.notDeleted(texture);
  }

  static void copyData(
    final int bpp,
    final ByteBuffer source_data,
    final AreaInclusive source_area,
    final ByteBuffer target_data,
    final AreaInclusive target_area)
  {
    final int row_bytes = (int) (target_area.getRangeX().getInterval() * bpp);
    final int x_first = (int) source_area.getRangeX().getLower();
    final int x_last = (int) source_area.getRangeX().getUpper();
    final int y_first = (int) source_area.getRangeY().getLower();
    final int y_last = (int) source_area.getRangeY().getUpper();

    int target_offset = 0;
    int source_offset = 0;
    for (int y = y_first; y <= y_last; ++y) {
      for (int x = x_first; x <= x_last; ++x) {
        target_offset = (y * row_bytes) + (x * bpp);

        switch (bpp) {
          case 1:
          {
            target_data.put(
              target_offset + 0,
              source_data.get(source_offset + 0));
            break;
          }
          case 2:
          {
            target_data.put(
              target_offset + 0,
              source_data.get(source_offset + 0));
            target_data.put(
              target_offset + 1,
              source_data.get(source_offset + 1));
            break;
          }
          case 3:
          {
            target_data.put(
              target_offset + 0,
              source_data.get(source_offset + 0));
            target_data.put(
              target_offset + 1,
              source_data.get(source_offset + 1));
            target_data.put(
              target_offset + 2,
              source_data.get(source_offset + 2));
            break;
          }
          case 4:
          {
            target_data.put(
              target_offset + 0,
              source_data.get(source_offset + 0));
            target_data.put(
              target_offset + 1,
              source_data.get(source_offset + 1));
            target_data.put(
              target_offset + 2,
              source_data.get(source_offset + 2));
            target_data.put(
              target_offset + 3,
              source_data.get(source_offset + 3));
            break;
          }
        }

        source_offset += bpp;
      }
    }

    // FakeTextures2DStatic.dumpBuffer(target_area, bpp, target_data);
  }
  static void dumpBuffer(
    final AreaInclusive area,
    final int bpp,
    final ByteBuffer b)
  {
    final RangeInclusiveL range_x = area.getRangeX();
    final RangeInclusiveL range_y = area.getRangeY();
    final long y_first = range_y.getLower();
    final long y_last = range_y.getUpper();
    final long x_first = range_x.getLower();
    final long x_last = range_x.getUpper();
    final long row_bytes = (x_last - x_first) * bpp;

    System.out.printf("Dumping buffer %s %s %dbpp:\n", b, area, bpp);

    for (long y = y_first; y <= y_last; ++y) {
      for (long x = x_first; x <= x_last; ++x) {
        final long byte_offset = (y * row_bytes) + (x * bpp);

        switch (bpp) {
          case 1:
          {
            System.out.printf("%02x ", b.get((int) byte_offset));
            break;
          }
          case 2:
          {
            System.out.printf(
              "%02x|%02x ",
              b.get((int) (byte_offset + 0)),
              b.get((int) (byte_offset + 1)));
            break;
          }
          case 3:
          {
            System.out.printf(
              "%02x|%02x|%02x ",
              b.get((int) (byte_offset + 0)),
              b.get((int) (byte_offset + 1)),
              b.get((int) (byte_offset + 2)));
            break;
          }
          case 4:
          {
            System.out.printf(
              "%02x|%02x|%02x|%02x ",
              b.get((int) (byte_offset + 0)),
              b.get((int) (byte_offset + 1)),
              b.get((int) (byte_offset + 2)),
              b.get((int) (byte_offset + 3)));
            break;
          }
        }
      }

      System.out.println();
    }
  }
  private final FakeContext             context;
  private final LogUsableType           log;
  private int                           pool;

  private final FakeLogMessageCacheType tcache;

  private final FakeTextureUnits        units;

  public FakeTextures2DStatic(
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

  @Override public Texture2DStaticType texture2DStaticAllocateDepth16(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_DEPTH_16_2BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateDepth24(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_DEPTH_24_4BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
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
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateDepth32f(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_DEPTH_32F_4BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateR16(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_R_16_2BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateR16f(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_R_16F_2BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateR16I(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_R_16I_2BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateR16U(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_R_16U_2BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateR32f(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_R_32F_4BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateR32I(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_R_32I_4BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateR32U(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_R_32U_4BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateR8(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_R_8_1BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateR8I(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_R_8I_1BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateR8U(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_R_8U_1BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRG16(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RG_16_4BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRG16f(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RG_16F_4BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRG16I(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RG_16I_4BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRG16U(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RG_16U_4BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRG32f(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RG_32F_8BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRG32I(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RG_32I_8BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRG32U(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RG_32U_8BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRG8(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RG_8_2BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRG8I(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RG_8I_2BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRG8U(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RG_8U_2BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRGB16(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGB_16_6BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRGB16f(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGB_16F_6BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRGB16I(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGB_16I_6BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRGB16U(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGB_16U_6BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRGB32f(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGB_32F_12BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRGB32I(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGB_32I_12BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRGB32U(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGB_32U_12BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRGB565(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGB_565_2BPP,
      width,
      height,
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
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGB_8_3BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRGB8I(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGB_8I_3BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRGB8U(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGB_8U_3BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRGBA1010102(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGBA_1010102_4BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRGBA16(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGBA_16_8BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRGBA16f(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGBA_16F_8BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRGBA16I(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGBA_16I_8BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRGBA16U(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGBA_16U_8BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRGBA32f(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGBA_32F_16BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRGBA32I(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGBA_32I_16BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRGBA32U(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGBA_32U_16BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRGBA4444(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGBA_4444_2BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRGBA5551(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGBA_5551_2BPP,
      width,
      height,
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
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRGBA8I(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGBA_8I_4BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRGBA8U(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return new FakeTexture2DStatic(
      this.context,
      this.freshID(),
      name,
      TextureFormat.TEXTURE_FORMAT_RGBA_8U_4BPP,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public void texture2DStaticBind(
    final TextureUnitType unit,
    final Texture2DStaticUsableType texture)
    throws JCGLException
  {
    NullCheck.notNull(unit, "Unit");
    FakeTextures2DStatic.checkTexture(this.context, texture);
    this.units.bind(unit.unitGetIndex(), (FakeTextureType) texture);
  }

  @Override public void texture2DStaticDelete(
    final Texture2DStaticType texture)
    throws JCGLException
  {
    FakeTextures2DStatic.checkTexture(this.context, texture);

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

  @Override public Texture2DStaticReadableType texture2DStaticGetImage(
    final Texture2DStaticUsableType texture)
    throws JCGLException
  {
    FakeTextures2DStatic.checkTexture(this.context, texture);

    final FakeTexture2DStatic fake = (FakeTexture2DStatic) texture;

    final FakeTexture2DReadableData td =
      new FakeTexture2DReadableData(
        texture.textureGetFormat(),
        texture.textureGetArea(),
        fake.getData().asReadOnlyBuffer());

    return td;
  }

  @Override public boolean texture2DStaticIsBound(
    final TextureUnitType unit,
    final Texture2DStaticUsableType texture)
    throws JCGLException
  {
    NullCheck.notNull(unit, "Unit");
    FakeTextures2DStatic.checkTexture(this.context, texture);
    return this.units.isBound(unit.unitGetIndex(), (FakeTextureType) texture);
  }

  @Override public void texture2DStaticUnbind(
    final TextureUnitType unit)
    throws JCGLException
  {
    NullCheck.notNull(unit, "Unit");
    this.units.unbind(unit.unitGetIndex());
  }

  @Override public void texture2DStaticUpdate(
    final Texture2DStaticUpdateType data)
    throws JCGLException
  {
    NullCheck.notNull(data, "Data");

    final Texture2DStaticType texture = data.getTexture();
    FakeTextures2DStatic.checkTexture(this.context, texture);

    final FakeTexture2DStatic fake_t = (FakeTexture2DStatic) texture;
    final int bpp = fake_t.textureGetFormat().getBytesPerPixel();

    /**
     * The "source" data is the data that has been given to update the
     * texture.
     */

    final ByteBuffer source_data = data.getTargetData();
    final AreaInclusive source_area = data.getTargetArea();
    final ByteBuffer target_data = fake_t.getData();

    FakeTextures2DStatic.copyData(
      bpp,
      source_data,
      source_area,
      target_data,
      fake_t.textureGetArea());
  }
}
