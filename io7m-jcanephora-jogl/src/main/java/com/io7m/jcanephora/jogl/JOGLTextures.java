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

package com.io7m.jcanephora.jogl;

import com.io7m.jareas.core.AreaInclusiveUnsignedLType;
import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLExceptionNonCompliant;
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
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.valid4j.Assertive;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class JOGLTextures implements JCGLTexturesType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(JOGLTextures.class);
  }

  private final JOGLContext context;
  private final GL3 g3;
  private final IntBuffer icache;
  private final List<JCGLTextureUnitType> units;
  private final int size;
  private final int[] bindings;

  JOGLTextures(final JOGLContext c)
    throws JCGLExceptionNonCompliant
  {
    this.context = NullCheck.notNull(c);
    this.g3 = c.getGL3();
    this.icache = Buffers.newDirectIntBuffer(1);
    this.units = JOGLTextures.makeUnits(c, this.g3, this.icache);
    this.size = JOGLTextures.makeSize(this.g3, this.icache);
    this.bindings = new int[this.units.size()];
  }

  private static List<JCGLTextureUnitType> makeUnits(
    final JOGLContext in_c,
    final GL3 in_g,
    final IntBuffer in_cache)
    throws JCGLExceptionNonCompliant
  {
    in_cache.rewind();
    in_g.glGetIntegerv(GL3.GL_MAX_TEXTURE_IMAGE_UNITS, in_cache);

    final int max = in_cache.get(0);

    JOGLTextures.LOG.debug(
      "implementation supports {} texture units",
      Integer.valueOf(max));

    if (max < 16) {
      final String message = String.format(
        "Reported number of texture units %d is less than the required %d",
        Integer.valueOf(max), Integer.valueOf(16));
      JOGLTextures.LOG.error(message);
      throw new JCGLExceptionNonCompliant(message);
    }

    final int clamped = Math.min(1024, max);
    if (clamped != max) {
      JOGLTextures.LOG.debug(
        "clamped unreasonable texture unit count {} to {}",
        Integer.valueOf(max),
        Integer.valueOf(clamped));
    }

    final List<JCGLTextureUnitType> u = new ArrayList<>(clamped);
    for (int index = 0; index < clamped; ++index) {
      u.add(new JOGLTextureUnit(in_c.getContext(), index));
    }

    return Collections.unmodifiableList(u);
  }

  private static int makeSize(
    final GL3 in_g,
    final IntBuffer in_cache)
    throws JCGLExceptionNonCompliant
  {
    in_cache.rewind();
    in_g.glGetIntegerv(GL.GL_MAX_TEXTURE_SIZE, in_cache);

    final int size = in_cache.get(0);

    JOGLTextures.LOG.debug(
      "implementation reports maximum texture size {}",
      Integer.valueOf(size));

    if (size < 1024) {
      final String message = String.format(
        "Reported maximum texture size %d is less than the required %d",
        Integer.valueOf(size), Integer.valueOf(1024));
      JOGLTextures.LOG.error(message);
      throw new JCGLExceptionNonCompliant(message);
    }

    return size;
  }

  static void checkTextureUnit(
    final GLContext ctx,
    final JCGLTextureUnitType unit)
  {
    NullCheck.notNull(unit, "Texture unit");
    JOGLCompatibilityChecks.checkTextureUnit(ctx, unit);
  }

  static void checkTexture2D(
    final GLContext ctx,
    final JCGLTexture2DUsableType t)
  {
    NullCheck.notNull(t, "Texture");
    JOGLCompatibilityChecks.checkTexture2D(ctx, t);
    JCGLResources.checkNotDeleted(t);
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
    JOGLTextures.checkTextureUnit(this.context.getContext(), unit);
    return this.bindings[unit.unitGetIndex()] != 0;
  }

  @Override public void texture2DBind(
    final JCGLTextureUnitType unit,
    final JCGLTexture2DUsableType texture)
    throws JCGLException
  {
    final GLContext c = this.context.getContext();
    JOGLTextures.checkTexture2D(c, texture);
    JOGLTextures.checkTextureUnit(c, unit);

    final int index = unit.unitGetIndex();
    final int texture_id = texture.getGLName();
    this.bind2DAdd(index, texture_id);
  }

  private void bind2DAdd(
    final int index,
    final int texture_id)
  {
    final int binding = this.bindings[index];
    JOGLTextures.LOG.trace(
      "bind 2D [{}]: {} → {}",
      Integer.valueOf(index),
      Integer.valueOf(binding),
      Integer.valueOf(texture_id));
    this.g3.glActiveTexture(GL.GL_TEXTURE0 + index);
    this.g3.glBindTexture(GL.GL_TEXTURE_2D, texture_id);
    this.bindings[index] = texture_id;
  }

  private void bind2DRemove(
    final int index)
  {
    final int binding = this.bindings[index];
    JOGLTextures.LOG.trace(
      "unbind 2D [{}]: {} → {}",
      Integer.valueOf(index),
      Integer.valueOf(binding),
      Integer.valueOf(0));
    this.g3.glActiveTexture(GL.GL_TEXTURE0 + index);
    this.g3.glBindTexture(GL.GL_TEXTURE_2D, 0);
    this.bindings[index] = 0;
  }

  @Override public void texture2DDelete(
    final JCGLTexture2DType texture)
    throws JCGLException
  {
    final GLContext c = this.context.getContext();
    JOGLTextures.checkTexture2D(c, texture);

    JOGLTextures.LOG.debug("delete {}", Integer.valueOf(texture.getGLName()));

    final int texture_id = texture.getGLName();
    this.icache.rewind();
    this.icache.put(0, texture_id);
    this.g3.glDeleteTextures(1, this.icache);
    ((JOGLTexture2D) texture).setDeleted();

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
    final GLContext c = this.context.getContext();
    JOGLTextures.checkTexture2D(c, texture);
    JOGLTextures.checkTextureUnit(c, unit);
    return this.bindings[unit.unitGetIndex()] == texture.getGLName();
  }

  @Override public void texture2DUnbind(final JCGLTextureUnitType unit)
    throws JCGLException
  {
    final GLContext c = this.context.getContext();
    JOGLTextures.checkTextureUnit(c, unit);
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
    final GLContext c = this.context.getContext();
    JOGLTextures.checkTextureUnit(c, unit);
    NullCheck.notNull(format, "Texture format");
    NullCheck.notNull(wrap_s, "Wrap S mode");
    NullCheck.notNull(wrap_t, "Wrap T mode");
    NullCheck.notNull(min_filter, "Minification filter");
    NullCheck.notNull(mag_filter, "Magnification filter");
    RangeCheck.checkGreaterEqualLong(width, "Width", 2L, "Valid widths");
    RangeCheck.checkGreaterEqualLong(height, "Height", 2L, "Valid heights");

    final long bytes = width * height * format.getBytesPerPixel();
    JOGLTextures.LOG.debug(
      "allocate {} {}x{} {} bytes",
      format,
      Long.valueOf(width),
      Long.valueOf(height),
      Long.valueOf(bytes));

    this.icache.rewind();
    this.g3.glGenTextures(1, this.icache);
    final int texture_id = this.icache.get(0);

    this.bind2DAdd(unit.unitGetIndex(), texture_id);

    this.g3.glTexParameteri(
      GL.GL_TEXTURE_2D,
      GL.GL_TEXTURE_WRAP_S,
      JOGLTypeConversions.textureWrapSToGL(wrap_s));
    this.g3.glTexParameteri(
      GL.GL_TEXTURE_2D,
      GL.GL_TEXTURE_WRAP_T,
      JOGLTypeConversions.textureWrapTToGL(wrap_t));
    this.g3.glTexParameteri(
      GL.GL_TEXTURE_2D,
      GL.GL_TEXTURE_MIN_FILTER,
      JOGLTypeConversions.textureFilterMinToGL(min_filter));
    this.g3.glTexParameteri(
      GL.GL_TEXTURE_2D,
      GL.GL_TEXTURE_MAG_FILTER,
      JOGLTypeConversions.textureFilterMagToGL(mag_filter));

    final JOGLTextureSpec spec = JOGLTextureSpecs.getTextureSpec(format);
    this.g3.glPixelStorei(GL.GL_PACK_ALIGNMENT, 1);
    this.g3.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1);
    this.g3.glTexImage2D(
      GL.GL_TEXTURE_2D,
      0,
      spec.getInternalFormat(),
      (int) width,
      (int) height,
      0,
      spec.getFormat(),
      spec.getType(),
      null);

    switch (min_filter) {
      case TEXTURE_FILTER_NEAREST:
      case TEXTURE_FILTER_LINEAR:
        break;
      case TEXTURE_FILTER_LINEAR_MIPMAP_LINEAR:
      case TEXTURE_FILTER_LINEAR_MIPMAP_NEAREST:
      case TEXTURE_FILTER_NEAREST_MIPMAP_LINEAR:
      case TEXTURE_FILTER_NEAREST_MIPMAP_NEAREST: {
        this.g3.glGenerateMipmap(GL.GL_TEXTURE_2D);
        break;
      }
    }

    JOGLTextures.LOG.debug("allocated {}", Integer.valueOf(texture_id));
    return new JOGLTexture2D(
      this.context.getContext(),
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
    final GLContext c = this.context.getContext();
    JOGLTextures.checkTextureUnit(c, unit);
    JOGLTextures.checkTexture2D(c, texture);

    final AreaInclusiveUnsignedLType update_area = data.getArea();
    Assertive.require(update_area.isIncludedIn(texture.textureGetArea()));

    final int x_offset = (int) update_area.getRangeX().getLower();
    final int y_offset = (int) update_area.getRangeY().getLower();
    final int width = (int) update_area.getRangeX().getInterval();
    final int height = (int) update_area.getRangeY().getInterval();
    final JCGLTextureFormat format = texture.textureGetFormat();
    final JOGLTextureSpec spec = JOGLTextureSpecs.getTextureSpec(format);
    final ByteBuffer buffer = data.getData();

    this.bind2DAdd(unit.unitGetIndex(), texture.getGLName());
    this.g3.glTexSubImage2D(
      GL.GL_TEXTURE_2D,
      0,
      x_offset,
      y_offset,
      width,
      height,
      spec.getFormat(),
      spec.getType(),
      buffer);

    switch (texture.textureGetMinificationFilter()) {
      case TEXTURE_FILTER_LINEAR:
      case TEXTURE_FILTER_NEAREST:
        break;
      case TEXTURE_FILTER_LINEAR_MIPMAP_LINEAR:
      case TEXTURE_FILTER_LINEAR_MIPMAP_NEAREST:
      case TEXTURE_FILTER_NEAREST_MIPMAP_LINEAR:
      case TEXTURE_FILTER_NEAREST_MIPMAP_NEAREST: {
        this.g3.glGenerateMipmap(GL.GL_TEXTURE_2D);
        break;
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

    final GLContext c = this.context.getContext();
    JOGLTextures.checkTextureUnit(c, unit);
    JOGLTextures.checkTexture2D(c, texture);

    final JCGLTextureFormat format = texture.textureGetFormat();
    final JOGLTextureSpec spec = JOGLTextureSpecs.getTextureSpec(format);

    final AreaInclusiveUnsignedLType area = texture.textureGetArea();
    final long width = area.getRangeX().getInterval();
    final long height = area.getRangeY().getInterval();
    final ByteBuffer data = ByteBuffer.allocateDirect(
      (int) (width * height * (long) format.getBytesPerPixel()));

    this.bind2DAdd(unit.unitGetIndex(), texture.getGLName());
    this.g3.glGetTexImage(
      GL.GL_TEXTURE_2D,
      0,
      spec.getFormat(),
      spec.getType(),
      data);
    return data;
  }

}
