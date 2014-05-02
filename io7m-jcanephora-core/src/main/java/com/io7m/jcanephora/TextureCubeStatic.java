/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;
import com.io7m.jcanephora.jogl.JCGLResourceDeletable;

/**
 * Cube-map "static" texture type.
 */

public final class TextureCubeStatic extends JCGLResourceDeletable implements
  TextureCubeStaticUsable
{
  private final  AreaInclusive              area;
  private final int                                 id;
  private final  TextureFilterMagnification mag_filter;
  private final  TextureFilterMinification  min_filter;
  private final  String                     name;
  private final  RangeInclusive             range_x;
  private final  RangeInclusive             range_y;
  private final  TextureFormat                type;
  private final  TextureWrapR               wrap_r;
  private final  TextureWrapS               wrap_s;
  private final  TextureWrapT               wrap_t;

  TextureCubeStatic(
    final  String name1,
    final  TextureFormat type1,
    final int id1,
    final int size,
    final  TextureWrapR wrap_r1,
    final  TextureWrapS wrap_s1,
    final  TextureWrapT wrap_t1,
    final  TextureFilterMinification min_filter1,
    final  TextureFilterMagnification mag_filter1)
    throws ConstraintError
  {
    this.id =
      Constraints.constrainRange(
        id1,
        0,
        Integer.MAX_VALUE,
        "Texture ID value");
    this.name = name1;
    this.type = type1;
    this.range_x = new RangeInclusive(0, size - 1);
    this.range_y = new RangeInclusive(0, size - 1);
    this.area = new AreaInclusive(this.range_x, this.range_y);
    this.wrap_r = wrap_r1;
    this.wrap_s = wrap_s1;
    this.wrap_t = wrap_t1;
    this.min_filter = min_filter1;
    this.mag_filter = mag_filter1;
  }

  @Override public boolean equals(
    final Object obj)
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
    final TextureCubeStatic other = (TextureCubeStatic) obj;
    if (this.id != other.id) {
      return false;
    }
    return true;
  }

  @Override public  AreaInclusive textureGetArea()
  {
    return this.area;
  }

  @Override public int getGLName()
  {
    return this.id;
  }

  @Override public int renderbufferGetHeight()
  {
    return (int) this.range_y.getInterval();
  }

  @Override public 
    TextureFilterMagnification
    textureGetMagnificationFilter()
  {
    return this.mag_filter;
  }

  @Override public  TextureFilterMinification textureGetMinificationFilter()
  {
    return this.min_filter;
  }

  @Override public  String uniformGetName()
  {
    return this.name;
  }

  @Override public  RangeInclusive textureGetRangeX()
  {
    return this.range_x;
  }

  @Override public  RangeInclusive textureGetRangeY()
  {
    return this.range_y;
  }

  @Override public  TextureFormat uniformGetType()
  {
    return this.type;
  }

  @Override public int renderbufferGetWidth()
  {
    return (int) this.range_x.getInterval();
  }

  @Override public  TextureWrapR getWrapR()
  {
    return this.wrap_r;
  }

  @Override public  TextureWrapS textureGetWrapS()
  {
    return this.wrap_s;
  }

  @Override public  TextureWrapT textureGetWrapT()
  {
    return this.wrap_t;
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + this.id;
    return result;
  }

  @Override public long resourceGetSizeBytes()
  {
    return this.textureGetWidth()
      * this.type.getBytesPerPixel()
      * this.textureGetHeight()
      * 6L;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("TextureCubeStatic ");
    builder.append(this.name);
    builder.append(" ");
    builder.append(this.id);
    builder.append(" ");
    builder.append(this.type);
    builder.append(" ");
    builder.append(this.textureGetWidth());
    builder.append("x");
    builder.append(this.textureGetHeight());
    builder.append("]");
    return builder.toString();
  }
}
