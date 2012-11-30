package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;

/**
 * 2D texture type.
 */

public final class Texture2DStatic extends Deletable implements
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

  Texture2DStatic(
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
    final Texture2DStatic other = (Texture2DStatic) obj;
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

  @Override public <G extends GLInterfaceEmbedded> void resourceDelete(
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
    builder.append("Texture2DStatic ");
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
