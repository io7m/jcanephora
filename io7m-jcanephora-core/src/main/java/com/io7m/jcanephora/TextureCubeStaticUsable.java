package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.RangeInclusive;

/**
 * A read-only interface to the {@link TextureCubeStatic} type that allows use
 * of the type but not mutation and/or deletion of the contents.
 */

public interface TextureCubeStaticUsable extends GLName, GLResourceUsable
{
  /**
   * Retrieve the inclusive area of this texture.
   */

  public @Nonnull abstract AreaInclusive getArea();

  /**
   * Return the height in pixels of the texture.
   */

  public int getHeight();

  /**
   * Retrieve the magnification filter used for the texture.
   */

  public @Nonnull TextureFilterMagnification getMagnificationFilter();

  /**
   * Retrieve the minification filter used for the texture.
   */

  public @Nonnull TextureFilterMinification getMinificationFilter();

  /**
   * Retrieve the name of the texture.
   */

  public @Nonnull String getName();

  /**
   * Return the range of valid indices on the X axis.
   */

  public @Nonnull RangeInclusive getRangeX();

  /**
   * Return the range of valid indices on the Y axis.
   */

  public @Nonnull RangeInclusive getRangeY();

  /**
   * Retrieve the type of the texture.
   */

  public @Nonnull TextureType getType();

  /**
   * Retrieve the width in pixels of the texture.
   */

  public int getWidth();

  /**
   * Retrieve the wrapping mode used on the R axis of the texture.
   */

  public @Nonnull TextureWrapR getWrapR();

  /**
   * Retrieve the wrapping mode used on the S axis of the texture.
   */

  public @Nonnull TextureWrapS getWrapS();

  /**
   * Retrieve the wrapping mode used on the T axis of the texture.
   */

  public @Nonnull TextureWrapT getWrapT();
}
