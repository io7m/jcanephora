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
import com.io7m.jcanephora.core.JCGLCubeMapFaceLH;
import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLExceptionNonCompliant;
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
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLContext;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.valid4j.Assertive;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
  private final Int2ObjectMap<BitSet> texture_to_units;
  private       JOGLFramebuffers framebuffers;

  JOGLTextures(final JOGLContext c)
    throws JCGLExceptionNonCompliant
  {
    this.context = NullCheck.notNull(c);
    this.g3 = c.getGL3();
    this.icache = Buffers.newDirectIntBuffer(1);
    this.units = JOGLTextures.makeUnits(c, this.g3, this.icache);
    this.size = JOGLTextures.makeSize(this.g3, this.icache);
    this.texture_to_units = new Int2ObjectOpenHashMap<>(this.units.size());

    /**
     * Configure baseline defaults.
     */

    for (int index = 0; index < this.units.size(); ++index) {
      this.g3.glActiveTexture(GL.GL_TEXTURE0 + index);
      this.g3.glBindTexture(GL.GL_TEXTURE_2D, 0);
      this.g3.glBindTexture(GL.GL_TEXTURE_CUBE_MAP, 0);
    }
    JOGLErrorChecking.checkErrors(this.g3);
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

    if (JOGLTextures.LOG.isDebugEnabled()) {
      JOGLTextures.LOG.debug(
        "implementation supports {} texture units",
        Integer.valueOf(max));
    }

    if (max < 16) {
      final String message = String.format(
        "Reported number of texture units %d is less than the required %d",
        Integer.valueOf(max), Integer.valueOf(16));
      JOGLTextures.LOG.error(message);
      throw new JCGLExceptionNonCompliant(message);
    }

    final int clamped = Math.min(1024, max);
    if (clamped != max) {
      if (JOGLTextures.LOG.isDebugEnabled()) {
        JOGLTextures.LOG.debug(
          "clamped unreasonable texture unit count {} to {}",
          Integer.valueOf(max),
          Integer.valueOf(clamped));
      }
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

    if (JOGLTextures.LOG.isDebugEnabled()) {
      JOGLTextures.LOG.debug(
        "implementation reports maximum texture size {}",
        Integer.valueOf(size));
    }

    if (size < 1024) {
      final String message = String.format(
        "Reported maximum texture size %d is less than the required %d",
        Integer.valueOf(size), Integer.valueOf(1024));
      JOGLTextures.LOG.error(message);
      throw new JCGLExceptionNonCompliant(message);
    }

    return size;
  }

  static JOGLTextureUnit checkTextureUnit(
    final GLContext ctx,
    final JCGLTextureUnitType unit)
  {
    NullCheck.notNull(unit, "Texture unit");
    return JOGLCompatibilityChecks.checkTextureUnit(ctx, unit);
  }

  static JOGLTexture2D checkTexture2D(
    final GLContext ctx,
    final JCGLTexture2DUsableType t)
  {
    NullCheck.notNull(t, "Texture");
    final JOGLTexture2D r = JOGLCompatibilityChecks.checkTexture2D(ctx, t);
    JCGLResources.checkNotDeleted(t);
    return r;
  }

  static JOGLTextureCube checkTextureCube(
    final GLContext c,
    final JCGLTextureCubeUsableType t)
  {
    NullCheck.notNull(t, "Texture");
    final JOGLTextureCube r = JOGLCompatibilityChecks.checkTextureCube(c, t);
    JCGLResources.checkNotDeleted(t);
    return r;
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
    final JOGLTextureUnit u =
      JOGLTextures.checkTextureUnit(this.context.getContext(), unit);
    return u.getBind2D() != null || u.getBindCube() != null;
  }

  @Override
  public void textureUnitUnbind(final JCGLTextureUnitType unit)
    throws JCGLException
  {
    final GLContext c = this.context.getContext();
    final JOGLTextureUnit u = JOGLTextures.checkTextureUnit(c, unit);
    final int index = u.unitGetIndex();

    {
      final JOGLTexture2D t2d = u.getBind2D();
      if (t2d != null) {
        if (JOGLTextures.LOG.isTraceEnabled()) {
          JOGLTextures.LOG.trace(
            "unbind 2D [{}]: {} -> none",
            Integer.valueOf(index),
            t2d);
        }
        this.g3.glActiveTexture(GL.GL_TEXTURE0 + index);
        this.g3.glBindTexture(GL.GL_TEXTURE_2D, 0);
        this.bindingRemoveTextureReference(t2d.getGLName(), index);
        u.setBind2D(null);
      }
    }

    {
      final JOGLTextureCube tc = u.getBindCube();
      if (tc != null) {
        if (JOGLTextures.LOG.isTraceEnabled()) {
          JOGLTextures.LOG.trace(
            "unbind cube [{}]: {} -> none",
            Integer.valueOf(index),
            tc);
        }
        this.g3.glActiveTexture(GL.GL_TEXTURE0 + index);
        this.g3.glBindTexture(GL.GL_TEXTURE_CUBE_MAP, 0);
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
    final GLContext c = this.context.getContext();
    final JOGLTexture2D t = JOGLTextures.checkTexture2D(c, texture);
    final JOGLTextureUnit u = JOGLTextures.checkTextureUnit(c, unit);
    final int index = unit.unitGetIndex();
    final int texture_id = texture.getGLName();

    this.checkFeedback(texture);

    /**
     * Do not re-bind already bound textures.
     */

    if (Objects.equals(u.getBind2D(), t)) {
      if (JOGLTextures.LOG.isTraceEnabled()) {
        JOGLTextures.LOG.trace(
          "bind 2D [{}]: keep existing",
          Integer.valueOf(index));
      }
      return;
    }

    this.textureUnitUnbind(u);

    if (JOGLTextures.LOG.isTraceEnabled()) {
      JOGLTextures.LOG.trace(
        "bind 2D [{}]: none -> {}",
        Integer.valueOf(index),
        texture);
    }
    this.g3.glActiveTexture(GL.GL_TEXTURE0 + index);
    this.g3.glBindTexture(GL.GL_TEXTURE_2D, texture_id);
    this.bindingAddTextureReference(texture_id, index);
    u.setBind2D(t);
  }

  private void bindingAddTextureReference(
    final int texture_id,
    final int index)
  {
    final BitSet tc;
    if (this.texture_to_units.containsKey(texture_id)) {
      tc = this.texture_to_units.get(texture_id);
    } else {
      tc = new BitSet(this.units.size());
    }

    tc.set(index, true);
    this.texture_to_units.put(texture_id, tc);
  }

  private void bindingRemoveTextureReference(
    final int texture_id,
    final int index)
  {
    if (this.texture_to_units.containsKey(texture_id)) {
      final BitSet tc = this.texture_to_units.get(texture_id);
      tc.set(index, false);
      if (tc.isEmpty()) {
        this.texture_to_units.remove(texture_id);
      }
    }
  }

  @Override
  public void texture2DDelete(
    final JCGLTexture2DType texture)
    throws JCGLException
  {
    final GLContext c = this.context.getContext();
    JOGLTextures.checkTexture2D(c, texture);

    if (JOGLTextures.LOG.isDebugEnabled()) {
      JOGLTextures.LOG.debug("delete {}", Integer.valueOf(texture.getGLName()));
    }

    final int texture_id = texture.getGLName();
    this.icache.rewind();
    this.icache.put(0, texture_id);
    this.g3.glDeleteTextures(1, this.icache);
    ((JOGLTexture2D) texture).setDeleted();

    this.unbindDeleted(texture_id);
  }

  private void unbindDeleted(final int texture_id)
  {
    final BitSet bound_units = this.texture_to_units.get(texture_id);
    if (bound_units != null) {
      for (int index = 0; index < this.units.size(); ++index) {
        if (bound_units.get(index)) {
          this.textureUnitUnbind(this.units.get(index));
        }
      }
    }
  }

  @Override
  public boolean texture2DIsBound(
    final JCGLTextureUnitType unit,
    final JCGLTexture2DUsableType texture)
    throws JCGLException
  {
    final GLContext c = this.context.getContext();
    JOGLTextures.checkTexture2D(c, texture);
    final JOGLTextureUnit u = JOGLTextures.checkTextureUnit(c, unit);
    return texture.equals(u.getBind2D());
  }

  @Override
  public boolean texture2DIsBoundAnywhere(final JCGLTexture2DUsableType texture)
    throws JCGLException
  {
    final GLContext c = this.context.getContext();
    JOGLTextures.checkTexture2D(c, texture);
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
    final GLContext c = this.context.getContext();
    JOGLTextures.checkTextureUnit(c, unit);
    NullCheck.notNull(format, "Texture format");
    NullCheck.notNull(wrap_s, "Wrap S mode");
    NullCheck.notNull(wrap_t, "Wrap T mode");
    NullCheck.notNull(min_filter, "Minification filter");
    NullCheck.notNull(mag_filter, "Magnification filter");
    RangeCheck.checkGreaterEqualLong(width, "Width", 2L, "Valid widths");
    RangeCheck.checkGreaterEqualLong(height, "Height", 2L, "Valid heights");

    final long bytes = width * height * (long) format.getBytesPerPixel();
    if (JOGLTextures.LOG.isDebugEnabled()) {
      JOGLTextures.LOG.debug(
        "allocate {} {}x{} {} bytes",
        format,
        Long.valueOf(width),
        Long.valueOf(height),
        Long.valueOf(bytes));
    }

    this.icache.rewind();
    this.g3.glGenTextures(1, this.icache);
    final int texture_id = this.icache.get(0);

    final JOGLTexture2D t = new JOGLTexture2D(
      this.context.getContext(),
      texture_id,
      mag_filter,
      min_filter,
      format,
      wrap_s,
      wrap_t,
      width,
      height);
    this.texture2DBind(unit, t);

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

    if (JOGLTextures.LOG.isDebugEnabled()) {
      JOGLTextures.LOG.debug("allocated {}", Integer.valueOf(texture_id));
    }
    return t;
  }

  @Override
  public void texture2DUpdate(
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

    this.texture2DBind(unit, texture);
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

  @Override
  public ByteBuffer texture2DGetImage(
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
    data.order(ByteOrder.nativeOrder());

    this.texture2DBind(unit, texture);
    this.g3.glGetTexImage(
      GL.GL_TEXTURE_2D,
      0,
      spec.getFormat(),
      spec.getType(),
      data);
    return data;
  }

  @Override
  public void texture2DRegenerateMipmaps(
    final JCGLTextureUnitType unit)
    throws JCGLException
  {
    NullCheck.notNull(unit);

    final GLContext c = this.context.getContext();
    final JOGLTextureUnit u = JOGLTextures.checkTextureUnit(c, unit);
    final JOGLTexture2D b = u.getBind2D();

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
          this.g3.glActiveTexture(GL.GL_TEXTURE0 + u.unitGetIndex());
          this.g3.glGenerateMipmap(GL.GL_TEXTURE_2D);
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

  void setFramebuffers(final JOGLFramebuffers in_fb)
  {
    this.framebuffers = NullCheck.notNull(in_fb);
  }

  @Override
  public void textureCubeBind(
    final JCGLTextureUnitType unit,
    final JCGLTextureCubeUsableType texture)
    throws JCGLException
  {
    final GLContext c = this.context.getContext();
    final JOGLTextureCube t = JOGLTextures.checkTextureCube(c, texture);
    final JOGLTextureUnit u = JOGLTextures.checkTextureUnit(c, unit);
    final int index = unit.unitGetIndex();
    final int texture_id = texture.getGLName();

    this.checkFeedback(texture);

    /**
     * Do not re-bind already bound textures.
     */

    if (Objects.equals(u.getBindCube(), t)) {
      if (JOGLTextures.LOG.isTraceEnabled()) {
        JOGLTextures.LOG.trace(
          "bind cube [{}]: keep existing",
          Integer.valueOf(index));
      }
      return;
    }

    this.textureUnitUnbind(unit);

    if (JOGLTextures.LOG.isTraceEnabled()) {
      JOGLTextures.LOG.trace(
        "bind cube [{}]: none -> {}",
        Integer.valueOf(index),
        texture);
    }
    this.g3.glActiveTexture(GL.GL_TEXTURE0 + index);
    this.g3.glBindTexture(GL.GL_TEXTURE_CUBE_MAP, texture_id);
    this.bindingAddTextureReference(texture_id, index);
    u.setBindCube(t);
  }

  private void checkFeedback(final JCGLTextureUsableType texture)
  {
    final JOGLFramebuffer fb = this.framebuffers.getBindDraw();
    if (fb != null) {
      for (final JCGLReferableType r : fb.getReferences()) {
        if (texture.equals(r)) {
          JOGLFramebuffers.onFeedbackLoop(fb, texture);
        }
      }
    }
  }

  @Override
  public void textureCubeDelete(final JCGLTextureCubeType texture)
    throws JCGLException
  {
    final GLContext c = this.context.getContext();
    JOGLTextures.checkTextureCube(c, texture);

    if (JOGLTextures.LOG.isDebugEnabled()) {
      JOGLTextures.LOG.debug("delete {}", Integer.valueOf(texture.getGLName()));
    }

    final int texture_id = texture.getGLName();
    this.icache.rewind();
    this.icache.put(0, texture_id);
    this.g3.glDeleteTextures(1, this.icache);
    ((JOGLTextureCube) texture).setDeleted();
    this.unbindDeleted(texture_id);
  }

  @Override
  public boolean textureCubeIsBound(
    final JCGLTextureUnitType unit,
    final JCGLTextureCubeUsableType texture)
    throws JCGLException
  {
    final GLContext c = this.context.getContext();
    JOGLTextures.checkTextureCube(c, texture);
    final JOGLTextureUnit u = JOGLTextures.checkTextureUnit(c, unit);
    return texture.equals(u.getBindCube());
  }

  @Override
  public boolean textureCubeIsBoundAnywhere(
    final JCGLTextureCubeUsableType texture)
    throws JCGLException
  {
    final GLContext c = this.context.getContext();
    JOGLTextures.checkTextureCube(c, texture);
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
    NullCheck.notNull(data);
    NullCheck.notNull(unit);

    final JCGLTextureCubeUsableType texture = data.getTexture();
    final GLContext c = this.context.getContext();
    JOGLTextures.checkTextureUnit(c, unit);
    JOGLTextures.checkTextureCube(c, texture);

    final AreaInclusiveUnsignedLType update_area = data.getArea();
    Assertive.require(update_area.isIncludedIn(texture.textureGetArea()));

    final int x_offset = (int) update_area.getRangeX().getLower();
    final int y_offset = (int) update_area.getRangeY().getLower();
    final int width = (int) update_area.getRangeX().getInterval();
    final int height = (int) update_area.getRangeY().getInterval();
    final JCGLTextureFormat format = texture.textureGetFormat();
    final JOGLTextureSpec spec = JOGLTextureSpecs.getTextureSpec(format);
    final ByteBuffer buffer = data.getData();
    final int gface = JOGLTypeConversions.cubeFaceToGL(face);

    this.textureCubeBind(unit, texture);
    this.g3.glTexSubImage2D(
      gface,
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
        this.g3.glGenerateMipmap(GL.GL_TEXTURE_CUBE_MAP);
        break;
      }
    }
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
    final GLContext c = this.context.getContext();
    JOGLTextures.checkTextureUnit(c, unit);
    NullCheck.notNull(format, "Texture format");
    NullCheck.notNull(wrap_r, "Wrap R mode");
    NullCheck.notNull(wrap_s, "Wrap S mode");
    NullCheck.notNull(wrap_t, "Wrap T mode");
    NullCheck.notNull(min_filter, "Minification filter");
    NullCheck.notNull(mag_filter, "Magnification filter");
    RangeCheck.checkGreaterEqualLong(in_size, "Size", 2L, "Valid sizes");

    final long bytes = (in_size * in_size) * 6L * format.getBytesPerPixel();
    if (JOGLTextures.LOG.isDebugEnabled()) {
      JOGLTextures.LOG.debug(
        "allocate {} {}x{}x6 {} bytes",
        format,
        Long.valueOf(in_size),
        Long.valueOf(in_size),
        Long.valueOf(bytes));
    }

    this.icache.rewind();
    this.g3.glGenTextures(1, this.icache);
    final int texture_id = this.icache.get(0);

    final JOGLTextureCube t = new JOGLTextureCube(
      this.context.getContext(),
      texture_id,
      mag_filter,
      min_filter,
      format,
      wrap_r,
      wrap_s,
      wrap_t,
      in_size);
    this.textureCubeBind(unit, t);

    this.g3.glTexParameteri(
      GL.GL_TEXTURE_CUBE_MAP,
      GL3.GL_TEXTURE_WRAP_R,
      JOGLTypeConversions.textureWrapRToGL(wrap_r));
    this.g3.glTexParameteri(
      GL.GL_TEXTURE_CUBE_MAP,
      GL.GL_TEXTURE_WRAP_S,
      JOGLTypeConversions.textureWrapSToGL(wrap_s));
    this.g3.glTexParameteri(
      GL.GL_TEXTURE_CUBE_MAP,
      GL.GL_TEXTURE_WRAP_T,
      JOGLTypeConversions.textureWrapTToGL(wrap_t));
    this.g3.glTexParameteri(
      GL.GL_TEXTURE_CUBE_MAP,
      GL.GL_TEXTURE_MIN_FILTER,
      JOGLTypeConversions.textureFilterMinToGL(min_filter));
    this.g3.glTexParameteri(
      GL.GL_TEXTURE_CUBE_MAP,
      GL.GL_TEXTURE_MAG_FILTER,
      JOGLTypeConversions.textureFilterMagToGL(mag_filter));

    final JOGLTextureSpec spec = JOGLTextureSpecs.getTextureSpec(format);
    this.g3.glPixelStorei(GL.GL_PACK_ALIGNMENT, 1);
    this.g3.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1);

    for (final JCGLCubeMapFaceLH f : JCGLCubeMapFaceLH.values()) {
      final int gface = JOGLTypeConversions.cubeFaceToGL(f);
      this.g3.glTexImage2D(
        gface,
        0,
        spec.getInternalFormat(),
        (int) in_size,
        (int) in_size,
        0,
        spec.getFormat(),
        spec.getType(),
        null);
    }

    switch (min_filter) {
      case TEXTURE_FILTER_NEAREST:
      case TEXTURE_FILTER_LINEAR:
        break;
      case TEXTURE_FILTER_LINEAR_MIPMAP_LINEAR:
      case TEXTURE_FILTER_LINEAR_MIPMAP_NEAREST:
      case TEXTURE_FILTER_NEAREST_MIPMAP_LINEAR:
      case TEXTURE_FILTER_NEAREST_MIPMAP_NEAREST: {
        this.g3.glGenerateMipmap(GL.GL_TEXTURE_CUBE_MAP);
        break;
      }
    }

    if (JOGLTextures.LOG.isDebugEnabled()) {
      JOGLTextures.LOG.debug("allocated {}", Integer.valueOf(texture_id));
    }
    return t;
  }

  @Override
  public ByteBuffer textureCubeGetImageLH(
    final JCGLTextureUnitType unit,
    final JCGLCubeMapFaceLH face,
    final JCGLTextureCubeUsableType texture)
    throws JCGLException
  {
    NullCheck.notNull(texture);
    NullCheck.notNull(face);
    NullCheck.notNull(unit);

    final GLContext c = this.context.getContext();
    JOGLTextures.checkTextureUnit(c, unit);
    JOGLTextures.checkTextureCube(c, texture);

    final JCGLTextureFormat format = texture.textureGetFormat();
    final JOGLTextureSpec spec = JOGLTextureSpecs.getTextureSpec(format);

    final AreaInclusiveUnsignedLType area = texture.textureGetArea();
    final long width = area.getRangeX().getInterval();
    final long height = area.getRangeY().getInterval();
    final ByteBuffer data = ByteBuffer.allocateDirect(
      (int) (width * height * (long) format.getBytesPerPixel()));
    data.order(ByteOrder.nativeOrder());

    final int gface = JOGLTypeConversions.cubeFaceToGL(face);

    this.textureCubeBind(unit, texture);
    this.g3.glGetTexImage(
      gface,
      0,
      spec.getFormat(),
      spec.getType(),
      data);
    return data;
  }

  @Override
  public void textureCubeRegenerateMipmaps(
    final JCGLTextureUnitType unit)
    throws JCGLException
  {
    NullCheck.notNull(unit);

    final GLContext c = this.context.getContext();
    final JOGLTextureUnit u = JOGLTextures.checkTextureUnit(c, unit);
    final JOGLTextureCube b = u.getBindCube();

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
          this.g3.glActiveTexture(GL.GL_TEXTURE0 + u.unitGetIndex());
          this.g3.glGenerateMipmap(GL.GL_TEXTURE_CUBE_MAP);
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
}
