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
import com.io7m.jcanephora.TextureCubeStaticType;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureFormat;
import com.io7m.jcanephora.TextureFormatMeta;
import com.io7m.jcanephora.TextureWrapR;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jequality.annotations.EqualityStructural;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;
import com.io7m.jranges.RangeCheck;
import com.io7m.jranges.RangeInclusiveL;

/**
 * Cube-map "static" texture type.
 */

@EqualityStructural final class FakeTextureCubeStatic extends
  FakeObjectShared implements
  TextureCubeStaticType,
  FakeFramebufferAttachableType,
  FakeTextureType
{
  private final AreaInclusive              area;
  private final ByteBuffer                 face_negative_x;
  private final ByteBuffer                 face_negative_y;
  private final ByteBuffer                 face_negative_z;
  private final ByteBuffer                 face_positive_x;
  private final ByteBuffer                 face_positive_y;
  private final ByteBuffer                 face_positive_z;
  private final TextureFilterMagnification mag_filter;
  private final TextureFilterMinification  min_filter;
  private final String                     name;
  private final RangeInclusiveL            range_x;
  private final RangeInclusiveL            range_y;
  private final TextureFormat              type;
  private final TextureWrapR               wrap_r;
  private final TextureWrapS               wrap_s;
  private final TextureWrapT               wrap_t;

  FakeTextureCubeStatic(
    final FakeContext in_context,
    final int in_id,
    final String in_name,
    final TextureFormat in_format,
    final int in_size,
    final TextureWrapR in_wrap_r,
    final TextureWrapS in_wrap_s,
    final TextureWrapT in_wrap_t,
    final TextureFilterMinification in_min_filter,
    final TextureFilterMagnification in_mag_filter)
  {
    super(in_context, in_id);

    RangeCheck.checkGreater(in_size, "Size", 2, "Minimum texture size");
    this.name = NullCheck.notNull(in_name, "Name");
    this.type = NullCheck.notNull(in_format, "Format");
    this.range_x = new RangeInclusiveL(0, in_size - 1);
    this.range_y = new RangeInclusiveL(0, in_size - 1);
    this.area = new AreaInclusive(this.range_x, this.range_y);
    this.wrap_r = NullCheck.notNull(in_wrap_r, "Wrap R");
    this.wrap_s = NullCheck.notNull(in_wrap_s, "Wrap S");
    this.wrap_t = NullCheck.notNull(in_wrap_t, "Wrap T");
    this.min_filter = NullCheck.notNull(in_min_filter, "Minification filter");
    this.mag_filter =
      NullCheck.notNull(in_mag_filter, "Magnification filter");

    final int bpp = in_format.getBytesPerPixel();
    this.face_positive_x = ByteBuffer.allocate(in_size * in_size * bpp);
    this.face_positive_y = ByteBuffer.allocate(in_size * in_size * bpp);
    this.face_positive_z = ByteBuffer.allocate(in_size * in_size * bpp);
    this.face_negative_x = ByteBuffer.allocate(in_size * in_size * bpp);
    this.face_negative_y = ByteBuffer.allocate(in_size * in_size * bpp);
    this.face_negative_z = ByteBuffer.allocate(in_size * in_size * bpp);
  }

  @Override public <T, E extends Exception> T attachableAccept(
    final FakeFramebufferAttachableVisitorType<T, E> v)
    throws E,
      JCGLException
  {
    return v.textureCube(this);
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
    final FakeTextureCubeStatic other = (FakeTextureCubeStatic) obj;
    if (super.getGLName() != other.getGLName()) {
      return false;
    }
    return true;
  }

  public ByteBuffer getFaceNegativeX()
  {
    return this.face_negative_x;
  }

  public ByteBuffer getFaceNegativeY()
  {
    return this.face_negative_y;
  }

  public ByteBuffer getFaceNegativeZ()
  {
    return this.face_negative_z;
  }

  public ByteBuffer getFacePositiveX()
  {
    return this.face_positive_x;
  }

  public ByteBuffer getFacePositiveY()
  {
    return this.face_positive_y;
  }

  public ByteBuffer getFacePositiveZ()
  {
    return this.face_positive_z;
  }

  @Override public int getStencilBits()
  {
    return TextureFormatMeta.getStencilBits(this.type);
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + super.getGLName();
    return result;
  }

  @Override public boolean isStencilRenderable()
  {
    return TextureFormatMeta.isStencilRenderable(this.type);
  }

  @Override public long resourceGetSizeBytes()
  {
    return this.textureGetWidth()
      * this.type.getBytesPerPixel()
      * this.textureGetHeight()
      * 6L;
  }

  @Override public <T, E extends Exception> T textureAccept(
    final FakeTextureVisitorType<T, E> v)
    throws JCGLException,
      E
  {
    return v.textureCube(this);
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

  @Override public TextureWrapR textureGetWrapR()
  {
    return this.wrap_r;
  }

  @Override public TextureWrapS textureGetWrapS()
  {
    return this.wrap_s;
  }

  @Override public TextureWrapT textureGetWrapT()
  {
    return this.wrap_t;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[FakeTextureCubeStatic ");
    builder.append(this.name);
    builder.append(" ");
    builder.append(super.getGLName());
    builder.append(" ");
    builder.append(this.type);
    builder.append(" ");
    builder.append(this.textureGetWidth());
    builder.append("x");
    builder.append(this.textureGetHeight());
    builder.append("]");
    final String r = builder.toString();
    assert r != null;
    return r;
  }
}
