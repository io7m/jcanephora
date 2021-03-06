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

import com.io7m.jcanephora.core.JCGLTextureCubeType;
import com.io7m.jcanephora.core.JCGLTextureCubeUsableType;
import com.io7m.jcanephora.core.JCGLTextureFilterMagnification;
import com.io7m.jcanephora.core.JCGLTextureFilterMinification;
import com.io7m.jcanephora.core.JCGLTextureFormat;
import com.io7m.jcanephora.core.JCGLTextureWrapR;
import com.io7m.jcanephora.core.JCGLTextureWrapS;
import com.io7m.jcanephora.core.JCGLTextureWrapT;
import com.io7m.jnull.NullCheck;
import com.io7m.jregions.core.unparameterized.sizes.AreaSizeL;
import com.io7m.junsigned.ranges.UnsignedRangeInclusiveL;
import com.jogamp.opengl.GLContext;

final class JOGLTextureCube extends JOGLReferable
  implements JCGLTextureCubeType
{
  private final JCGLTextureFilterMagnification filter_mag;
  private final JCGLTextureFilterMinification filter_min;
  private final UnsignedRangeInclusiveL range_x;
  private final UnsignedRangeInclusiveL range_y;
  private final long width;
  private final long height;
  private final JCGLTextureFormat format;
  private final UnsignedRangeInclusiveL byte_range;
  private final JCGLTextureWrapR wrap_r;
  private final JCGLTextureWrapS wrap_s;
  private final JCGLTextureWrapT wrap_t;
  private final AreaSizeL area;

  JOGLTextureCube(
    final GLContext in_context,
    final int in_id,
    final JCGLTextureFilterMagnification in_filter_mag,
    final JCGLTextureFilterMinification in_filter_min,
    final JCGLTextureFormat in_format,
    final JCGLTextureWrapR in_wrap_r,
    final JCGLTextureWrapS in_wrap_s,
    final JCGLTextureWrapT in_wrap_t,
    final long in_size)
  {
    super(in_context, in_id);

    this.filter_mag =
      NullCheck.notNull(in_filter_mag, "Magnification filter");
    this.filter_min =
      NullCheck.notNull(in_filter_min, "Minification filter");
    this.format =
      NullCheck.notNull(in_format, "Format");
    this.wrap_r =
      NullCheck.notNull(in_wrap_r, "Wrapping mode R");
    this.wrap_s =
      NullCheck.notNull(in_wrap_s, "Wrapping mode S");
    this.wrap_t =
      NullCheck.notNull(in_wrap_t, "Wrapping mode T");

    this.width = in_size;
    this.height = in_size;
    this.area = AreaSizeL.of(in_size, in_size);
    this.range_x = new UnsignedRangeInclusiveL(0L, in_size - 1L);
    this.range_y = new UnsignedRangeInclusiveL(0L, in_size - 1L);

    final long size =
      this.width * this.height * (long) this.format.getBytesPerPixel();
    this.byte_range = new UnsignedRangeInclusiveL(0L, size - 1L);
  }

  public static JOGLTextureCube checkTextureCube(
    final GLContext c,
    final JCGLTextureCubeUsableType t)
  {
    return (JOGLTextureCube) JOGLCompatibilityChecks.checkAny(c, t);
  }

  @Override
  public JCGLTextureFilterMagnification magnificationFilter()
  {
    return this.filter_mag;
  }

  @Override
  public JCGLTextureFilterMinification minificationFilter()
  {
    return this.filter_min;
  }

  @Override
  public UnsignedRangeInclusiveL rangeX()
  {
    return this.range_x;
  }

  @Override
  public UnsignedRangeInclusiveL rangeY()
  {
    return this.range_y;
  }

  @Override
  public long width()
  {
    return this.width;
  }

  @Override
  public long height()
  {
    return this.height;
  }

  @Override
  public JCGLTextureFormat format()
  {
    return this.format;
  }

  @Override
  public UnsignedRangeInclusiveL byteRange()
  {
    return this.byte_range;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder("[TextureCube ");
    sb.append(super.glName());
    sb.append(" ");
    sb.append(this.width);
    sb.append("x");
    sb.append(this.height);
    sb.append(" ").append(this.filter_mag);
    sb.append(" ").append(this.filter_min);
    sb.append(" ").append(this.format);
    sb.append(" ").append(this.wrap_r);
    sb.append(" ").append(this.wrap_s);
    sb.append(" ").append(this.wrap_t);
    sb.append(']');
    return sb.toString();
  }

  @Override
  public AreaSizeL size()
  {
    return this.area;
  }

  @Override
  public JCGLTextureWrapR wrapR()
  {
    return this.wrap_r;
  }

  @Override
  public JCGLTextureWrapS wrapS()
  {
    return this.wrap_s;
  }

  @Override
  public JCGLTextureWrapT wrapT()
  {
    return this.wrap_t;
  }

  @Override
  public boolean equals(final Object o)
  {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }

    final JOGLTextureCube that = (JOGLTextureCube) o;
    return this.glName() == that.glName();
  }

  @Override
  public int hashCode()
  {
    return this.glName();
  }
}
