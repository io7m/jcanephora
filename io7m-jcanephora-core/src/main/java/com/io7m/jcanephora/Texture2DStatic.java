/*
 * Copyright © 2012 http://io7m.com
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

/**
 * 2D "static" texture type.
 */

public final class Texture2DStatic extends Deletable implements
  GLResource,
  Texture2DStaticUsable
{
  private boolean                       deleted = false;
  private final int                     id;
  private final @Nonnull RangeInclusive range_x;
  private final @Nonnull RangeInclusive range_y;
  private final @Nonnull String         name;
  private final @Nonnull AreaInclusive  area;
  private final @Nonnull TextureType    type;
  private final @Nonnull TextureWrap    wrap_s;
  private final @Nonnull TextureWrap    wrap_t;
  private final @Nonnull TextureFilter  min_filter;
  private final @Nonnull TextureFilter  mag_filter;

  Texture2DStatic(
    final @Nonnull String name,
    final @Nonnull TextureType type,
    final int id,
    final int width,
    final int height,
    final @Nonnull TextureWrap wrap_s,
    final @Nonnull TextureWrap wrap_t,
    final @Nonnull TextureFilter min_filter,
    final @Nonnull TextureFilter mag_filter)
    throws ConstraintError
  {
    this.id =
      Constraints
        .constrainRange(id, 0, Integer.MAX_VALUE, "Texture ID value");
    this.name = name;
    this.type = type;
    this.range_x = new RangeInclusive(0, width - 1);
    this.range_y = new RangeInclusive(0, height - 1);
    this.area = new AreaInclusive(this.range_x, this.range_y);
    this.deleted = false;
    this.wrap_s = wrap_s;
    this.wrap_t = wrap_t;
    this.min_filter = min_filter;
    this.mag_filter = mag_filter;
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
    final Texture2DStatic other = (Texture2DStatic) obj;
    if (this.id != other.id) {
      return false;
    }
    return true;
  }

  @Override public @Nonnull AreaInclusive getArea()
  {
    return this.area;
  }

  @Override public int getGLName()
  {
    return this.id;
  }

  @Override public int getHeight()
  {
    return (int) this.range_y.getInterval();
  }

  @Override public @Nonnull TextureFilter getMagnificationFilter()
  {
    return this.mag_filter;
  }

  @Override public @Nonnull TextureFilter getMinificationFilter()
  {
    return this.min_filter;
  }

  @Override public @Nonnull String getName()
  {
    return this.name;
  }

  @Override public @Nonnull RangeInclusive getRangeX()
  {
    return this.range_x;
  }

  @Override public @Nonnull RangeInclusive getRangeY()
  {
    return this.range_y;
  }

  @Override public @Nonnull TextureType getType()
  {
    return this.type;
  }

  @Override public int getWidth()
  {
    return (int) this.range_x.getInterval();
  }

  @Override public @Nonnull TextureWrap getWrapS()
  {
    return this.wrap_s;
  }

  @Override public @Nonnull TextureWrap getWrapT()
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

  @Override public <G extends GLInterfaceES2> void resourceDelete(
    @Nonnull final G gl)
    throws ConstraintError,
      GLException
  {
    gl.texture2DStaticDelete(this);
  }

  @Override public boolean resourceIsDeleted()
  {
    return this.deleted;
  }

  @Override void setDeleted()
  {
    this.deleted = true;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[Texture2DStatic ");
    builder.append(this.id);
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
    return builder.toString();
  }
}
