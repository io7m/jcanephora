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

import com.io7m.jcanephora.core.JCGLTexture2DType;
import com.io7m.jcanephora.core.JCGLTextureFilterMagnification;
import com.io7m.jcanephora.core.JCGLTextureFilterMinification;
import com.io7m.jcanephora.core.JCGLTextureFormat;
import com.io7m.jcanephora.core.JCGLTextureWrapS;
import com.io7m.jcanephora.core.JCGLTextureWrapT;
import com.io7m.jnull.NullCheck;
import com.io7m.jregions.core.unparameterized.sizes.AreaSizeL;
import com.io7m.junsigned.ranges.UnsignedRangeInclusiveL;

import java.nio.ByteBuffer;

final class FakeTexture2D extends FakeReferable implements JCGLTexture2DType
{
  private final JCGLTextureFilterMagnification filter_mag;
  private final JCGLTextureFilterMinification filter_min;
  private final UnsignedRangeInclusiveL range_x;
  private final UnsignedRangeInclusiveL range_y;
  private final long width;
  private final long height;
  private final JCGLTextureFormat format;
  private final UnsignedRangeInclusiveL byte_range;
  private final JCGLTextureWrapS wrap_s;
  private final JCGLTextureWrapT wrap_t;
  private final ByteBuffer data;
  private final AreaSizeL size;

  FakeTexture2D(
    final FakeContext in_context,
    final int in_id,
    final JCGLTextureFilterMagnification in_filter_mag,
    final JCGLTextureFilterMinification in_filter_min,
    final JCGLTextureFormat in_format,
    final JCGLTextureWrapS in_wrap_s,
    final JCGLTextureWrapT in_wrap_t,
    final long in_width,
    final long in_height)
  {
    super(in_context, in_id);

    this.filter_mag =
      NullCheck.notNull(in_filter_mag, "Magnification filter");
    this.filter_min =
      NullCheck.notNull(in_filter_min, "Minification filter");
    this.format =
      NullCheck.notNull(in_format, "Format");
    this.wrap_s =
      NullCheck.notNull(in_wrap_s, "Wrapping S mode");
    this.wrap_t =
      NullCheck.notNull(in_wrap_t, "Wrapping T mode");

    this.size = AreaSizeL.of(in_width, in_height);
    this.width = in_width;
    this.height = in_height;
    this.range_x = new UnsignedRangeInclusiveL(0L, in_width - 1L);
    this.range_y = new UnsignedRangeInclusiveL(0L, in_height - 1L);

    final long byte_size =
      this.width * this.height * (long) this.format.getBytesPerPixel();
    this.byte_range =
      new UnsignedRangeInclusiveL(0L, byte_size - 1L);

    this.data = ByteBuffer.allocate(Math.toIntExact(byte_size));
  }

  ByteBuffer getData()
  {
    return this.data;
  }

  @Override
  public JCGLTextureFilterMagnification textureGetMagnificationFilter()
  {
    return this.filter_mag;
  }

  @Override
  public JCGLTextureFilterMinification textureGetMinificationFilter()
  {
    return this.filter_min;
  }

  @Override
  public UnsignedRangeInclusiveL textureGetRangeX()
  {
    return this.range_x;
  }

  @Override
  public UnsignedRangeInclusiveL textureGetRangeY()
  {
    return this.range_y;
  }

  @Override
  public long textureGetWidth()
  {
    return this.width;
  }

  @Override
  public long textureGetHeight()
  {
    return this.height;
  }

  @Override
  public JCGLTextureFormat textureGetFormat()
  {
    return this.format;
  }

  @Override
  public UnsignedRangeInclusiveL getRange()
  {
    return this.byte_range;
  }

  @Override
  public AreaSizeL textureGetSize()
  {
    return this.size;
  }

  @Override
  public JCGLTextureWrapS textureGetWrapS()
  {
    return this.wrap_s;
  }

  @Override
  public JCGLTextureWrapT textureGetWrapT()
  {
    return this.wrap_t;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder("[Texture2D ");
    sb.append(super.getGLName());
    sb.append(" ");
    sb.append(this.width);
    sb.append("x");
    sb.append(this.height);
    sb.append(" ").append(this.filter_mag);
    sb.append(" ").append(this.filter_min);
    sb.append(" ").append(this.format);
    sb.append(" ").append(this.wrap_s);
    sb.append(" ").append(this.wrap_t);
    sb.append(']');
    return sb.toString();
  }
}
