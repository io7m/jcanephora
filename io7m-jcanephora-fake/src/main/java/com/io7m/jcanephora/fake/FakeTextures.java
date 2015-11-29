/*
 * Copyright © 2015 <code@io7m.com> http://io7m.com
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

import com.io7m.jareas.core.AreaInclusiveUnsignedLType;
import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLResources;
import com.io7m.jcanephora.core.JCGLTexture2DType;
import com.io7m.jcanephora.core.JCGLTexture2DUpdateType;
import com.io7m.jcanephora.core.JCGLTexture2DUsableType;
import com.io7m.jcanephora.core.JCGLTextureFilterMagnification;
import com.io7m.jcanephora.core.JCGLTextureFilterMinification;
import com.io7m.jcanephora.core.JCGLTextureFormat;
import com.io7m.jcanephora.core.JCGLTextureUnitType;
import com.io7m.jcanephora.core.JCGLTextureWrapS;
import com.io7m.jcanephora.core.JCGLTextureWrapT;
import com.io7m.jcanephora.core.api.JCGLTexturesType;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeCheck;
import com.io7m.junsigned.ranges.UnsignedRangeInclusiveL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.valid4j.Assertive;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class FakeTextures implements JCGLTexturesType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(FakeTextures.class);
  }

  private final FakeContext               context;
  private final List<JCGLTextureUnitType> units;
  private final int                       size;
  private final int[]                     bindings;

  FakeTextures(
    final FakeContext c)
  {
    this.context = c;
    this.units = FakeTextures.makeUnits(c);
    this.size = FakeTextures.makeSize();
    this.bindings = new int[this.units.size()];
  }

  private static List<JCGLTextureUnitType> makeUnits(
    final FakeContext in_c)
  {
    final int max = 16;

    FakeTextures.LOG.debug(
      "implementation supports {} texture units",
      Integer.valueOf(max));

    final List<JCGLTextureUnitType> u = new ArrayList<>(max);
    for (int index = 0; index < max; ++index) {
      u.add(new FakeTextureUnit(in_c, index));
    }

    return Collections.unmodifiableList(u);
  }

  private static int makeSize()
  {
    final int size = 1024;

    FakeTextures.LOG.debug(
      "implementation reports maximum texture size {}",
      Integer.valueOf(size));

    return size;
  }

  static void checkTextureUnit(
    final FakeContext ctx,
    final JCGLTextureUnitType unit)
  {
    NullCheck.notNull(unit, "Texture unit");
    FakeCompatibilityChecks.checkTextureUnit(ctx, unit);
  }

  static void checkTexture2D(
    final FakeContext ctx,
    final JCGLTexture2DUsableType t)
  {
    NullCheck.notNull(t, "Texture");
    FakeCompatibilityChecks.checkTexture2D(ctx, t);
    JCGLResources.checkNotDeleted(t);
  }

  private static long getByteOffset(
    final long width,
    final int bpp,
    final long x,
    final long y)
  {
    final long row_bytes = width * bpp;
    return (y * row_bytes) + (x * bpp);
  }

  @Override public int textureGetMaximumSize()
    throws JCGLException
  {
    return this.size;
  }

  @Override public List<JCGLTextureUnitType> textureGetUnits()
    throws JCGLException
  {
    return this.units;
  }

  @Override public boolean textureUnitIsBound(final JCGLTextureUnitType unit)
    throws JCGLException
  {
    FakeTextures.checkTextureUnit(this.context, unit);
    return this.bindings[unit.unitGetIndex()] != 0;
  }

  @Override public void texture2DBind(
    final JCGLTextureUnitType unit,
    final JCGLTexture2DUsableType texture)
    throws JCGLException
  {
    FakeTextures.checkTexture2D(this.context, texture);
    FakeTextures.checkTextureUnit(this.context, unit);

    final int index = unit.unitGetIndex();
    final int texture_id = texture.getGLName();
    this.bind2DAdd(index, texture_id);
  }

  private void bind2DAdd(
    final int index,
    final int texture_id)
  {
    final int binding = this.bindings[index];
    FakeTextures.LOG.trace(
      "bind 2D [{}]: {} → {}",
      Integer.valueOf(index),
      Integer.valueOf(binding),
      Integer.valueOf(texture_id));
    this.bindings[index] = texture_id;
  }

  private void bind2DRemove(
    final int index)
  {
    final int binding = this.bindings[index];
    FakeTextures.LOG.trace(
      "unbind 2D [{}]: {} → {}",
      Integer.valueOf(index),
      Integer.valueOf(binding),
      Integer.valueOf(0));
    this.bindings[index] = 0;
  }

  @Override public void texture2DDelete(
    final JCGLTexture2DType texture)
    throws JCGLException
  {
    FakeTextures.checkTexture2D(this.context, texture);

    FakeTextures.LOG.debug("delete {}", Integer.valueOf(texture.getGLName()));

    final int texture_id = texture.getGLName();
    ((FakeTexture2D) texture).setDeleted();

    for (int index = 0; index < this.bindings.length; ++index) {
      if (this.bindings[index] == texture_id) {
        this.bind2DRemove(index);
      }
    }
  }

  @Override public boolean texture2DIsBound(
    final JCGLTextureUnitType unit,
    final JCGLTexture2DUsableType texture)
    throws JCGLException
  {
    FakeTextures.checkTexture2D(this.context, texture);
    FakeTextures.checkTextureUnit(this.context, unit);
    return this.bindings[unit.unitGetIndex()] == texture.getGLName();
  }

  @Override public void texture2DUnbind(final JCGLTextureUnitType unit)
    throws JCGLException
  {
    FakeTextures.checkTextureUnit(this.context, unit);
    this.bind2DRemove(unit.unitGetIndex());
  }

  @Override public JCGLTexture2DType texture2DAllocate(
    final JCGLTextureUnitType unit,
    final long width,
    final long height,
    final JCGLTextureFormat format,
    final JCGLTextureWrapS wrap_s,
    final JCGLTextureWrapT wrap_t,
    final JCGLTextureFilterMinification min_filter,
    final JCGLTextureFilterMagnification mag_filter)
  {
    FakeTextures.checkTextureUnit(this.context, unit);
    NullCheck.notNull(format, "Texture format");
    NullCheck.notNull(wrap_s, "Wrap S mode");
    NullCheck.notNull(wrap_t, "Wrap T mode");
    NullCheck.notNull(min_filter, "Minification filter");
    NullCheck.notNull(mag_filter, "Magnification filter");
    RangeCheck.checkGreaterEqualLong(width, "Width", 2L, "Valid widths");
    RangeCheck.checkGreaterEqualLong(height, "Height", 2L, "Valid heights");

    final long bytes = width * height * format.getBytesPerPixel();
    FakeTextures.LOG.debug(
      "allocate {} {}x{} {} bytes",
      format,
      Long.valueOf(width),
      Long.valueOf(height),
      Long.valueOf(bytes));

    final int texture_id = this.context.getFreshID();
    this.bind2DAdd(unit.unitGetIndex(), texture_id);

    FakeTextures.LOG.debug("allocated {}", Integer.valueOf(texture_id));
    return new FakeTexture2D(
      this.context,
      texture_id,
      mag_filter,
      min_filter,
      format,
      wrap_s,
      wrap_t,
      width,
      height);
  }

  @Override public void texture2DUpdate(
    final JCGLTextureUnitType unit,
    final JCGLTexture2DUpdateType data)
    throws JCGLException
  {
    NullCheck.notNull(data);
    NullCheck.notNull(unit);

    final JCGLTexture2DUsableType texture = data.getTexture();
    FakeTextures.checkTextureUnit(this.context, unit);
    FakeTextures.checkTexture2D(this.context, texture);

    final AreaInclusiveUnsignedLType source_area = data.getArea();
    final AreaInclusiveUnsignedLType texture_area = texture.textureGetArea();
    Assertive.require(source_area.isIncludedIn(texture_area));

    this.bind2DAdd(unit.unitGetIndex(), texture.getGLName());

    final FakeTexture2D ft = (FakeTexture2D) data.getTexture();
    final ByteBuffer target_data = ft.getData();

    final UnsignedRangeInclusiveL source_range_x = source_area.getRangeX();
    final UnsignedRangeInclusiveL source_range_y = source_area.getRangeY();
    final long source_min_x = source_range_x.getLower();
    final long source_width = source_range_x.getInterval();
    final long source_min_y = source_range_y.getLower();
    final long source_height = source_range_y.getInterval();
    final ByteBuffer source_data = data.getData();

    final UnsignedRangeInclusiveL target_range_x = texture_area.getRangeX();
    final long target_width = target_range_x.getInterval();

    final int bpp = texture.textureGetFormat().getBytesPerPixel();
    for (long y = 0L; y < source_height; ++y) {
      for (long x = 0L; x < source_width; ++x) {
        final long source_offset =
          FakeTextures.getByteOffset(source_width, bpp, x, y);
        final long target_offset =
          FakeTextures.getByteOffset(
            target_width, bpp, source_min_x + x, source_min_y + y);

        switch (bpp) {
          case 1: {
            target_data.put(
              (int) target_offset,
              source_data.get((int) source_offset));
            break;
          }
          case 2: {
            target_data.put(
              (int) (target_offset + 0L),
              source_data.get((int) (source_offset + 0L)));
            target_data.put(
              (int) (target_offset + 1L),
              source_data.get((int) (source_offset + 1L)));
            break;
          }
          case 3: {
            target_data.put(
              (int) (target_offset + 0L),
              source_data.get((int) (source_offset + 0L)));
            target_data.put(
              (int) (target_offset + 1L),
              source_data.get((int) (source_offset + 1L)));
            target_data.put(
              (int) (target_offset + 2L),
              source_data.get((int) (source_offset + 2L)));
            break;
          }
          case 4: {
            target_data.put(
              (int) (target_offset + 0L),
              source_data.get((int) (source_offset + 0L)));
            target_data.put(
              (int) (target_offset + 1L),
              source_data.get((int) (source_offset + 1L)));
            target_data.put(
              (int) (target_offset + 2L),
              source_data.get((int) (source_offset + 2L)));
            target_data.put(
              (int) (target_offset + 3L),
              source_data.get((int) (source_offset + 3L)));
            break;
          }
        }

      }
    }
  }

  @Override public ByteBuffer texture2DGetImage(
    final JCGLTextureUnitType unit,
    final JCGLTexture2DUsableType texture)
    throws JCGLException
  {
    NullCheck.notNull(texture);
    NullCheck.notNull(unit);

    FakeTextures.checkTextureUnit(this.context, unit);
    FakeTextures.checkTexture2D(this.context, texture);

    final FakeTexture2D ft = (FakeTexture2D) texture;
    this.bind2DAdd(unit.unitGetIndex(), texture.getGLName());
    return ft.getData().duplicate();
  }
}
