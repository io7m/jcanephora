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

import com.io7m.jaffirm.core.Preconditions;
import com.io7m.jcanephora.core.JCGLCubeMapFaceLH;
import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLExceptionTextureNotBound;
import com.io7m.jcanephora.core.JCGLReferableType;
import com.io7m.jcanephora.core.JCGLResources;
import com.io7m.jcanephora.core.JCGLTexture2DType;
import com.io7m.jcanephora.core.JCGLTexture2DUpdateType;
import com.io7m.jcanephora.core.JCGLTexture2DUsableType;
import com.io7m.jcanephora.core.JCGLTextureCubeType;
import com.io7m.jcanephora.core.JCGLTextureCubeUpdateType;
import com.io7m.jcanephora.core.JCGLTextureCubeUsableType;
import com.io7m.jcanephora.core.JCGLTextureFilterMagnification;
import com.io7m.jcanephora.core.JCGLTextureFilterMinification;
import com.io7m.jcanephora.core.JCGLTextureFormat;
import com.io7m.jcanephora.core.JCGLTextureUnitType;
import com.io7m.jcanephora.core.JCGLTextureUsableType;
import com.io7m.jcanephora.core.JCGLTextureWrapR;
import com.io7m.jcanephora.core.JCGLTextureWrapS;
import com.io7m.jcanephora.core.JCGLTextureWrapT;
import com.io7m.jcanephora.core.api.JCGLTexturesType;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeCheck;
import com.io7m.jregions.core.unparameterized.areas.AreaL;
import com.io7m.jregions.core.unparameterized.areas.AreasL;
import com.io7m.jregions.core.unparameterized.sizes.AreaSizesL;
import com.io7m.junreachable.UnreachableCodeException;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

final class FakeTextures implements JCGLTexturesType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(FakeTextures.class);
  }

  private final FakeContext context;
  private final List<JCGLTextureUnitType> units;
  private final int size;
  private final Int2ObjectMap<IntSet> texture_to_units;
  private final boolean[] temp_unbind;
  private FakeFramebuffers framebuffers;

  FakeTextures(
    final FakeContext c)
  {
    this.context = c;
    this.units = makeUnits(c);
    this.size = makeSize();
    this.texture_to_units = new Int2ObjectOpenHashMap<>(this.units.size());
    this.temp_unbind = new boolean[this.units.size()];
  }

  private static List<JCGLTextureUnitType> makeUnits(
    final FakeContext in_c)
  {
    final int max = 16;

    LOG.debug(
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

    LOG.debug(
      "implementation reports maximum texture size {}",
      Integer.valueOf(size));

    return size;
  }

  static FakeTextureUnit checkTextureUnit(
    final FakeContext ctx,
    final JCGLTextureUnitType unit)
  {
    NullCheck.notNull(unit, "Texture unit");
    return FakeCompatibilityChecks.checkTextureUnit(ctx, unit);
  }

  static FakeTexture2D checkTexture2D(
    final FakeContext ctx,
    final JCGLTexture2DUsableType t)
  {
    NullCheck.notNull(t, "Texture");
    final FakeTexture2D r = FakeCompatibilityChecks.checkTexture2D(ctx, t);
    JCGLResources.checkNotDeleted(t);
    return r;
  }

  static FakeTextureCube checkTextureCube(
    final FakeContext ctx,
    final JCGLTextureCubeUsableType t)
  {
    NullCheck.notNull(t, "Texture");
    final FakeTextureCube r = FakeCompatibilityChecks.checkTextureCube(ctx, t);
    JCGLResources.checkNotDeleted(t);
    return r;
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

  static void copyBytes(
    final int bpp,
    final long source_min_x,
    final long source_width,
    final long source_min_y,
    final long source_height,
    final ByteBuffer source_data,
    final ByteBuffer target_data,
    final long target_width)
  {
    for (long y = 0L; y < source_height; ++y) {
      for (long x = 0L; x < source_width; ++x) {
        final long source_offset =
          getByteOffset(source_width, bpp, x, y);
        final long target_offset =
          getByteOffset(
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
          default:
            throw new UnreachableCodeException();
        }
      }
    }
  }

  private void bindingAddTextureReference(
    final int texture_id,
    final int index)
  {
    final IntSet tc;
    if (this.texture_to_units.containsKey(texture_id)) {
      tc = this.texture_to_units.get(texture_id);
    } else {
      tc = new IntOpenHashSet();
    }
    tc.add(index);
    this.texture_to_units.put(texture_id, tc);
  }

  private void bindingRemoveTextureReference(
    final int texture_id,
    final int index)
  {
    if (this.texture_to_units.containsKey(texture_id)) {
      final IntSet tc = this.texture_to_units.get(texture_id);
      tc.remove(index);
      if (tc.isEmpty()) {
        this.texture_to_units.remove(texture_id);
      }
    }
  }

  @Override
  public int textureGetMaximumSize()
    throws JCGLException
  {
    return this.size;
  }

  @Override
  public List<JCGLTextureUnitType> textureGetUnits()
    throws JCGLException
  {
    return this.units;
  }

  @Override
  public boolean textureUnitIsBound(final JCGLTextureUnitType unit)
    throws JCGLException
  {
    final FakeTextureUnit u = checkTextureUnit(this.context, unit);
    return u.getBind2D() != null || u.getBindCube() != null;
  }

  @Override
  public void textureUnitUnbind(final JCGLTextureUnitType unit)
    throws JCGLException
  {
    final FakeTextureUnit u = checkTextureUnit(this.context, unit);
    final int index = u.unitGetIndex();

    {
      final FakeTexture2D t2d = u.getBind2D();
      if (t2d != null) {
        LOG.trace("unbind 2D [{}]: {} -> none", Integer.valueOf(index), t2d);
        this.bindingRemoveTextureReference(t2d.getGLName(), index);
        u.setBind2D(null);
      }
    }

    {
      final FakeTextureCube tc = u.getBindCube();
      if (tc != null) {
        LOG.trace("unbind cube [{}]: {} -> none", Integer.valueOf(index), tc);
        this.bindingRemoveTextureReference(tc.getGLName(), index);
        u.setBindCube(null);
      }
    }
  }

  @Override
  public void texture2DBind(
    final JCGLTextureUnitType unit,
    final JCGLTexture2DUsableType texture)
    throws JCGLException
  {
    final FakeTexture2D t = checkTexture2D(this.context, texture);
    final FakeTextureUnit u = checkTextureUnit(this.context, unit);

    this.checkFeedback(texture);

    final int index = unit.unitGetIndex();
    final int texture_id = texture.getGLName();
    this.textureUnitUnbind(unit);

    LOG.trace(
      "bind 2D [{}]: none -> {}",
      Integer.valueOf(index),
      texture);
    this.bindingAddTextureReference(texture_id, index);
    u.setBind2D(t);
  }

  @Override
  public void texture2DDelete(
    final JCGLTexture2DType texture)
    throws JCGLException
  {
    checkTexture2D(this.context, texture);
    LOG.debug("delete {}", Integer.valueOf(texture.getGLName()));

    final int texture_id = texture.getGLName();
    ((FakeTexture2D) texture).setDeleted();
    this.unbindDeleted(texture_id);
  }

  private void unbindDeleted(final int texture_id)
  {
    for (int index = 0; index < this.temp_unbind.length; ++index) {
      this.temp_unbind[index] = false;
    }

    final IntSet bound_units = this.texture_to_units.get(texture_id);
    if (bound_units != null) {
      final IntIterator iter = bound_units.iterator();
      while (iter.hasNext()) {
        final int index = iter.nextInt();
        this.temp_unbind[index] = true;
      }
    }

    for (int index = 0; index < this.temp_unbind.length; ++index) {
      if (this.temp_unbind[index]) {
        this.textureUnitUnbind(this.units.get(index));
      }
    }
  }

  @Override
  public boolean texture2DIsBound(
    final JCGLTextureUnitType unit,
    final JCGLTexture2DUsableType texture)
    throws JCGLException
  {
    checkTexture2D(this.context, texture);
    checkTextureUnit(this.context, unit);
    final FakeTextureUnit u = checkTextureUnit(this.context, unit);
    return Objects.equals(texture, u.getBind2D());
  }

  @Override
  public boolean texture2DIsBoundAnywhere(final JCGLTexture2DUsableType texture)
    throws JCGLException
  {
    checkTexture2D(this.context, texture);
    final int texture_id = texture.getGLName();
    return this.texture_to_units.containsKey(texture_id);
  }

  @Override
  public JCGLTexture2DType texture2DAllocate(
    final JCGLTextureUnitType unit,
    final long width,
    final long height,
    final JCGLTextureFormat format,
    final JCGLTextureWrapS wrap_s,
    final JCGLTextureWrapT wrap_t,
    final JCGLTextureFilterMinification min_filter,
    final JCGLTextureFilterMagnification mag_filter)
  {
    checkTextureUnit(this.context, unit);
    NullCheck.notNull(format, "Texture format");
    NullCheck.notNull(wrap_s, "Wrap S mode");
    NullCheck.notNull(wrap_t, "Wrap T mode");
    NullCheck.notNull(min_filter, "Minification filter");
    NullCheck.notNull(mag_filter, "Magnification filter");
    RangeCheck.checkGreaterEqualLong(width, "Width", 2L, "Valid widths");
    RangeCheck.checkGreaterEqualLong(height, "Height", 2L, "Valid heights");

    final long bytes = width * height * format.getBytesPerPixel();
    LOG.debug(
      "allocate {} {}x{} {} bytes",
      format,
      Long.valueOf(width),
      Long.valueOf(height),
      Long.valueOf(bytes));

    final int texture_id = this.context.getFreshID();
    final FakeTexture2D t = new FakeTexture2D(
      this.context,
      texture_id,
      mag_filter,
      min_filter,
      format,
      wrap_s,
      wrap_t,
      width,
      height);
    this.texture2DBind(unit, t);

    LOG.debug("allocated {}", Integer.valueOf(texture_id));
    return t;
  }

  @Override
  public void texture2DUpdate(
    final JCGLTextureUnitType unit,
    final JCGLTexture2DUpdateType data)
    throws JCGLException
  {
    NullCheck.notNull(data, "Data");
    NullCheck.notNull(unit, "Unit");

    final JCGLTexture2DUsableType texture = data.getTexture();
    checkTextureUnit(this.context, unit);
    checkTexture2D(this.context, texture);

    final AreaL update_area = data.getArea();
    final AreaL texture_area = AreaSizesL.area(texture.textureGetSize());

    Preconditions.checkPrecondition(
      update_area,
      AreasL.contains(texture_area, update_area),
      ignored -> "Update area must be included in texture area");

    this.texture2DBind(unit, texture);

    final FakeTexture2D ft = (FakeTexture2D) data.getTexture();
    final ByteBuffer target_data = ft.getData();

    final long source_min_x = update_area.minimumX();
    final long source_width = update_area.width();
    final long source_min_y = update_area.minimumY();
    final long source_height = update_area.height();
    final ByteBuffer source_data = data.getData();

    final long target_width = texture_area.width();

    final int bpp = texture.textureGetFormat().getBytesPerPixel();
    copyBytes(
      bpp,
      source_min_x,
      source_width,
      source_min_y,
      source_height,
      source_data,
      target_data,
      target_width
    );
  }

  @Override
  public ByteBuffer texture2DGetImage(
    final JCGLTextureUnitType unit,
    final JCGLTexture2DUsableType texture)
    throws JCGLException
  {
    NullCheck.notNull(texture, "Texture");
    NullCheck.notNull(unit, "Unit");

    checkTextureUnit(this.context, unit);
    checkTexture2D(this.context, texture);

    final FakeTexture2D ft = (FakeTexture2D) texture;
    this.texture2DBind(unit, texture);
    final ByteBuffer data = ft.getData().duplicate();
    data.order(ByteOrder.nativeOrder());
    return data;
  }

  @Override
  public void texture2DRegenerateMipmaps(final JCGLTextureUnitType unit)
    throws JCGLException
  {
    NullCheck.notNull(unit, "Unit");

    final FakeTextureUnit u = checkTextureUnit(this.context, unit);
    final FakeTexture2D b = u.getBind2D();

    if (b != null) {
      final JCGLTextureFilterMinification mag =
        b.textureGetMinificationFilter();
      switch (mag) {
        case TEXTURE_FILTER_LINEAR:
        case TEXTURE_FILTER_NEAREST: {
          break;
        }
        case TEXTURE_FILTER_NEAREST_MIPMAP_NEAREST:
        case TEXTURE_FILTER_LINEAR_MIPMAP_NEAREST:
        case TEXTURE_FILTER_NEAREST_MIPMAP_LINEAR:
        case TEXTURE_FILTER_LINEAR_MIPMAP_LINEAR: {
          break;
        }
      }
    } else {
      final StringBuilder sb = new StringBuilder(128);
      sb.append("No 2D texture bound to the given unit.");
      sb.append(System.lineSeparator());
      sb.append("Unit: ");
      sb.append(u);
      sb.append(System.lineSeparator());
      throw new JCGLExceptionTextureNotBound(sb.toString());
    }
  }

  @Override
  public void textureCubeBind(
    final JCGLTextureUnitType unit,
    final JCGLTextureCubeUsableType texture)
    throws JCGLException
  {
    final FakeTextureCube t =
      checkTextureCube(this.context, texture);
    final FakeTextureUnit u = checkTextureUnit(this.context, unit);

    this.checkFeedback(texture);

    final int index = unit.unitGetIndex();
    final int texture_id = texture.getGLName();
    this.textureUnitUnbind(unit);

    LOG.trace("bind cube [{}]: none -> {}", Integer.valueOf(index), texture);
    this.bindingAddTextureReference(texture_id, index);
    u.setBindCube(t);
  }

  private void checkFeedback(final JCGLTextureUsableType texture)
  {
    final FakeFramebuffer fb = this.framebuffers.getBindDraw();
    if (fb != null) {
      for (final JCGLReferableType r : fb.getReferences()) {
        if (Objects.equals(texture, r)) {
          FakeFramebuffers.onFeedbackLoop(fb, texture);
        }
      }
    }
  }

  @Override
  public void textureCubeDelete(final JCGLTextureCubeType texture)
    throws JCGLException
  {
    checkTextureCube(this.context, texture);
    LOG.debug("delete {}", Integer.valueOf(texture.getGLName()));

    final int texture_id = texture.getGLName();
    ((FakeTextureCube) texture).setDeleted();
    this.unbindDeleted(texture_id);
  }

  @Override
  public boolean textureCubeIsBound(
    final JCGLTextureUnitType unit,
    final JCGLTextureCubeUsableType texture)
    throws JCGLException
  {
    checkTextureCube(this.context, texture);
    final FakeTextureUnit u = checkTextureUnit(this.context, unit);
    return Objects.equals(texture, u.getBindCube());
  }

  @Override
  public boolean textureCubeIsBoundAnywhere(
    final JCGLTextureCubeUsableType texture)
    throws JCGLException
  {
    checkTextureCube(this.context, texture);
    final int texture_id = texture.getGLName();
    return this.texture_to_units.containsKey(texture_id);
  }

  @Override
  public void textureCubeUpdateLH(
    final JCGLTextureUnitType unit,
    final JCGLCubeMapFaceLH face,
    final JCGLTextureCubeUpdateType data)
    throws JCGLException
  {
    NullCheck.notNull(data, "Data");
    NullCheck.notNull(unit, "Unit");

    final JCGLTextureCubeUsableType texture = data.getTexture();
    checkTextureUnit(this.context, unit);
    checkTextureCube(this.context, texture);

    final AreaL update_area = data.getArea();
    final AreaL texture_area = AreaSizesL.area(texture.textureGetSize());

    Preconditions.checkPrecondition(
      update_area,
      AreasL.contains(texture_area, update_area),
      ignored -> "Update area must be included in texture area");

    this.textureCubeBind(unit, texture);

    final FakeTextureCube ft = (FakeTextureCube) data.getTexture();
    final ByteBuffer target_data = ft.getData(face);

    final long source_min_x = update_area.minimumX();
    final long source_width = update_area.width();
    final long source_min_y = update_area.minimumY();
    final long source_height = update_area.height();
    final ByteBuffer source_data = data.getData();

    final long target_width = texture_area.width();

    final int bpp = texture.textureGetFormat().getBytesPerPixel();
    copyBytes(
      bpp,
      source_min_x,
      source_width,
      source_min_y,
      source_height,
      source_data,
      target_data,
      target_width);
  }

  @Override
  public JCGLTextureCubeType textureCubeAllocate(
    final JCGLTextureUnitType unit,
    final long in_size,
    final JCGLTextureFormat format,
    final JCGLTextureWrapR wrap_r,
    final JCGLTextureWrapS wrap_s,
    final JCGLTextureWrapT wrap_t,
    final JCGLTextureFilterMinification min_filter,
    final JCGLTextureFilterMagnification mag_filter)
    throws JCGLException
  {
    checkTextureUnit(this.context, unit);
    NullCheck.notNull(format, "Texture format");
    NullCheck.notNull(wrap_r, "Wrap R mode");
    NullCheck.notNull(wrap_s, "Wrap S mode");
    NullCheck.notNull(wrap_t, "Wrap T mode");
    NullCheck.notNull(min_filter, "Minification filter");
    NullCheck.notNull(mag_filter, "Magnification filter");
    RangeCheck.checkGreaterEqualLong(in_size, "Size", 2L, "Valid sizes");

    final long bytes = (in_size * in_size) * 6L * format.getBytesPerPixel();
    LOG.debug(
      "allocate {} {}x{}x6 {} bytes",
      format,
      Long.valueOf(in_size),
      Long.valueOf(in_size),
      Long.valueOf(bytes));

    final int texture_id = this.context.getFreshID();
    final FakeTextureCube t = new FakeTextureCube(
      this.context,
      texture_id,
      mag_filter,
      min_filter,
      format,
      wrap_r,
      wrap_s,
      wrap_t,
      in_size);
    this.textureCubeBind(unit, t);

    LOG.debug("allocated {}", Integer.valueOf(texture_id));
    return t;
  }

  @Override
  public ByteBuffer textureCubeGetImageLH(
    final JCGLTextureUnitType unit,
    final JCGLCubeMapFaceLH face,
    final JCGLTextureCubeUsableType texture)
    throws JCGLException
  {
    NullCheck.notNull(texture, "Texture");
    NullCheck.notNull(unit, "Unit");

    checkTextureUnit(this.context, unit);
    checkTextureCube(this.context, texture);

    final FakeTextureCube ft = (FakeTextureCube) texture;
    this.textureCubeBind(unit, texture);
    final ByteBuffer data = ft.getData(face).duplicate();
    data.order(ByteOrder.nativeOrder());
    return data;
  }

  @Override
  public void textureCubeRegenerateMipmaps(final JCGLTextureUnitType unit)
    throws JCGLException
  {
    NullCheck.notNull(unit, "Unit");

    final FakeTextureUnit u = checkTextureUnit(this.context, unit);
    final FakeTextureCube b = u.getBindCube();

    if (b != null) {
      final JCGLTextureFilterMinification mag =
        b.textureGetMinificationFilter();
      switch (mag) {
        case TEXTURE_FILTER_LINEAR:
        case TEXTURE_FILTER_NEAREST: {
          break;
        }
        case TEXTURE_FILTER_NEAREST_MIPMAP_NEAREST:
        case TEXTURE_FILTER_LINEAR_MIPMAP_NEAREST:
        case TEXTURE_FILTER_NEAREST_MIPMAP_LINEAR:
        case TEXTURE_FILTER_LINEAR_MIPMAP_LINEAR: {
          break;
        }
      }
    } else {
      final StringBuilder sb = new StringBuilder(128);
      sb.append("No cube texture bound to the given unit.");
      sb.append(System.lineSeparator());
      sb.append("Unit: ");
      sb.append(u);
      sb.append(System.lineSeparator());
      throw new JCGLExceptionTextureNotBound(sb.toString());
    }
  }

  void setFramebuffers(final FakeFramebuffers fb)
  {
    this.framebuffers = NullCheck.notNull(fb, "Framebuffers");
  }
}
