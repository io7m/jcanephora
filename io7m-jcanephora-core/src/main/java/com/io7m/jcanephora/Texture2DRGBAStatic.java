package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * 2D, 32-bit RGBA textures (8 bits per channel).
 */

public final class Texture2DRGBAStatic extends Deletable implements
  GLResource,
  GLName
{
  private boolean               deleted = false;
  private final int             id;
  private final int             width;
  private final int             height;
  private final @Nonnull String name;

  Texture2DRGBAStatic(
    final @Nonnull String name,
    final int id,
    final int width,
    final int height)
    throws ConstraintError
  {
    this.id =
      Constraints
        .constrainRange(id, 0, Integer.MAX_VALUE, "Texture ID value");
    this.name = name;
    this.width = width;
    this.height = height;
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
    final Texture2DRGBAStatic other = (Texture2DRGBAStatic) obj;
    if (this.id != other.id) {
      return false;
    }
    return true;
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
    return this.height;
  }

  /**
   * Retrieve the name of the texture.
   */

  public @Nonnull String getName()
  {
    return this.name;
  }

  /**
   * Retrieve the width in pixels of the texture.
   */

  public int getWidth()
  {
    return this.width;
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
    gl.texture2DRGBAStaticDelete(this);
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
    builder.append("Texture2DRGBAStatic ");
    builder.append(this.name);
    builder.append(" ");
    builder.append(this.id);
    builder.append(" ");
    builder.append(this.width);
    builder.append("x");
    builder.append(this.height);
    builder.append("]");
    return builder.toString();
  }
}
