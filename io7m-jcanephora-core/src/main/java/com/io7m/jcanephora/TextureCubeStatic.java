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

/**
 * Cube-map "static" texture type.
 */

public final class TextureCubeStatic extends GLResourceDeletable implements
  TextureCubeStaticUsable
{
  private final int                                 id;
  private final @Nonnull RangeInclusive             range_x;
  private final @Nonnull RangeInclusive             range_y;
  private final @Nonnull String                     name;
  private final @Nonnull AreaInclusive              area;
  private final @Nonnull TextureType                type;
  private final @Nonnull TextureWrapR               wrap_r;
  private final @Nonnull TextureWrapS               wrap_s;
  private final @Nonnull TextureWrapT               wrap_t;
  private final @Nonnull TextureFilterMinification  min_filter;
  private final @Nonnull TextureFilterMagnification mag_filter;

  TextureCubeStatic(
    final @Nonnull String name,
    final @Nonnull TextureType type,
    final int id,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError
  {
    this.id =
      Constraints
        .constrainRange(id, 0, Integer.MAX_VALUE, "Texture ID value");
    this.name = name;
    this.type = type;
    this.range_x = new RangeInclusive(0, size - 1);
    this.range_y = new RangeInclusive(0, size - 1);
    this.area = new AreaInclusive(this.range_x, this.range_y);
    this.wrap_r = wrap_r;
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
    final TextureCubeStatic other = (TextureCubeStatic) obj;
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

  @Override public @Nonnull
    TextureFilterMagnification
    getMagnificationFilter()
  {
    return this.mag_filter;
  }

  @Override public @Nonnull TextureFilterMinification getMinificationFilter()
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

  @Override public @Nonnull TextureWrapR getWrapR()
  {
    return this.wrap_r;
  }

  @Override public @Nonnull TextureWrapS getWrapS()
  {
    return this.wrap_s;
  }

  @Override public @Nonnull TextureWrapT getWrapT()
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
    builder.append(this.getWidth());
    builder.append("x");
    builder.append(this.getHeight());
    builder.append("]");
    return builder.toString();
  }
}
