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

import javax.media.opengl.GLContext;

import com.io7m.jcanephora.AreaInclusive;
import com.io7m.jcanephora.Texture2DStaticType;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureFormat;
import com.io7m.jcanephora.TextureUsableVisitorType;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jnull.Nullable;
import com.io7m.jranges.RangeInclusiveL;

/**
 * 2D "static" texture type.
 */

final class JOGLTexture2DStatic extends JOGLObjectShared implements
  Texture2DStaticType
{
  private final AreaInclusive              area;
  private final TextureFilterMagnification mag_filter;
  private final TextureFilterMinification  min_filter;
  private final String                     name;
  private final RangeInclusiveL            range_x;
  private final RangeInclusiveL            range_y;
  private final TextureFormat              type;
  private final TextureWrapS               wrap_s;
  private final TextureWrapT               wrap_t;

  JOGLTexture2DStatic(
    final GLContext in_context,
    final int in_id,
    final String in_name,
    final TextureFormat in_format,
    final int width,
    final int height,
    final TextureWrapS in_wrap_s,
    final TextureWrapT in_wrap_t,
    final TextureFilterMinification in_min_filter,
    final TextureFilterMagnification in_mag_filter)
  {
    super(in_context, in_id);

    this.name = in_name;
    this.type = in_format;
    this.range_x = new RangeInclusiveL(0, width - 1);
    this.range_y = new RangeInclusiveL(0, height - 1);
    this.area = new AreaInclusive(this.range_x, this.range_y);
    this.wrap_s = in_wrap_s;
    this.wrap_t = in_wrap_t;
    this.min_filter = in_min_filter;
    this.mag_filter = in_mag_filter;
  }

  @Override public boolean equals(
    final @Nullable Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    final JOGLTexture2DStatic other = (JOGLTexture2DStatic) obj;
    if (super.getGLName() != other.getGLName()) {
      return false;
    }
    return true;
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + super.getGLName();
    return result;
  }

  @Override public long resourceGetSizeBytes()
  {
    return this.textureGetWidth()
      * this.type.getBytesPerPixel()
      * this.textureGetHeight();
  }

  @Override public AreaInclusive textureGetArea()
  {
    return this.area;
  }

  @Override public TextureFormat textureGetFormat()
  {
    return this.type;
  }

  @Override public int textureGetHeight()
  {
    return (int) this.range_y.getInterval();
  }

  @Override public TextureFilterMagnification textureGetMagnificationFilter()
  {
    return this.mag_filter;
  }

  @Override public TextureFilterMinification textureGetMinificationFilter()
  {
    return this.min_filter;
  }

  @Override public String textureGetName()
  {
    return this.name;
  }

  @Override public RangeInclusiveL textureGetRangeX()
  {
    return this.range_x;
  }

  @Override public RangeInclusiveL textureGetRangeY()
  {
    return this.range_y;
  }

  @Override public int textureGetWidth()
  {
    return (int) this.range_x.getInterval();
  }

  @Override public TextureWrapS textureGetWrapS()
  {
    return this.wrap_s;
  }

  @Override public TextureWrapT textureGetWrapT()
  {
    return this.wrap_t;
  }

  @Override public <A, E extends Exception> A textureUsableAccept(
    final TextureUsableVisitorType<A, E> v)
    throws E
  {
    return v.texture2D(this);
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[JOGLTexture2DStatic ");
    builder.append(this.getGLName());
    builder.append(" ");
    builder.append(this.name);
    builder.append(" ");
    builder.append(this.type);
    builder.append(" ");
    builder.append(this.area);
    builder.append(" ");
    builder.append(this.wrap_s);
    builder.append(" ");
    builder.append(this.wrap_t);
    builder.append(" ");
    builder.append(this.min_filter);
    builder.append(" ");
    builder.append(this.mag_filter);
    builder.append("]");
    final String r = builder.toString();
    assert r != null;
    return r;
  }
}
