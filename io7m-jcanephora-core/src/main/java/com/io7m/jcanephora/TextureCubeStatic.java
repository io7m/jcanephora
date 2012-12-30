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
 * Cube-map "static" texture type.
 */

public final class TextureCubeStatic extends Deletable implements
  GLResource,
  GLName
{
  private boolean                       deleted = false;
  private final int                     id;
  private final @Nonnull RangeInclusive range_x;
  private final @Nonnull RangeInclusive range_y;
  private final @Nonnull String         name;
  private final @Nonnull AreaInclusive  area;
  private final @Nonnull TextureType    type;

  TextureCubeStatic(
    final @Nonnull String name,
    final @Nonnull TextureType type,
    final int id,
    final int width,
    final int height)
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

  /**
   * Retrieve the inclusive area of this texture.
   */

  public @Nonnull AreaInclusive getArea()
  {
    return this.area;
  }

  @Override public int getGLName()
  {
    return this.id;
  }

  /**
   * Return the height in pixels of the texture.
   */

  public int getHeight()
  {
    return (int) this.range_y.getInterval();
  }

  /**
   * Retrieve the name of the texture.
   */

  public @Nonnull String getName()
  {
    return this.name;
  }

  /**
   * Return the range of valid indices on the X axis.
   */

  public @Nonnull RangeInclusive getRangeX()
  {
    return this.range_x;
  }

  /**
   * Return the range of valid indices on the Y axis.
   */

  public @Nonnull RangeInclusive getRangeY()
  {
    return this.range_y;
  }

  /**
   * Retrieve the type of the texture.
   */

  public @Nonnull TextureType getType()
  {
    return this.type;
  }

  /**
   * Retrieve the width in pixels of the texture.
   */

  public int getWidth()
  {
    return (int) this.range_x.getInterval();
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
    gl.textureCubeStaticDelete(this);
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
