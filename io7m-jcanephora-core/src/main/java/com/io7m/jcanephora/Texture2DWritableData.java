package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;

/**
 * An allocated region of data, to replace or update a 2D RGBA texture.
 */

public final class Texture2DWritableData
{
  private final @Nonnull Texture2DStatic texture;
  private final @Nonnull AreaInclusive   target_area;
  private final @Nonnull AreaInclusive   source_area;

  /**
   * Construct a buffer of data that will be used to replace the entirety of
   * the data in <code>texture</code> on the GPU.
   * 
   * @param texture
   *          The texture.
   * @throws ConstraintError
   *           Iff any of the following conditions hold:
   *           <ul>
   *           <li><code>buffer == null</code></li>
   *           </ul>
   */

  public Texture2DWritableData(
    final @Nonnull Texture2DStatic texture)
    throws ConstraintError
  {
    this(texture, texture.getArea());
  }

  /**
   * Construct a buffer of data that will be used to replace elements in the
   * area <code>area</code> of the data in <code>texture</code> on the GPU.
   * 
   * @param texture
   *          The texture.
   * @param area
   *          The inclusive area defining the area of the texture that will be
   *          modified.
   * @throws ConstraintError
   *           Iff any of the following conditions hold:
   *           <ul>
   *           <li><code>buffer == null</code></li>
   *           <li><code>area == null/code></li>
   *           <li><code>area.isIncludedIn(texture.getArea()) == false</code></li>
   *           </ul>
   */

  public Texture2DWritableData(
    final @Nonnull Texture2DStatic texture,
    final @Nonnull AreaInclusive area)
    throws ConstraintError
  {
    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainNotNull(area, "Area");
    Constraints.constrainArbitrary(
      area.isIncludedIn(texture.getArea()),
      "Area is included within texture");

    this.texture = texture;
    this.target_area = area;

    final RangeInclusive srx =
      new RangeInclusive(0, area.getRangeX().getInterval() - 1);
    final RangeInclusive sry =
      new RangeInclusive(0, area.getRangeY().getInterval() - 1);
    this.source_area = new AreaInclusive(srx, sry);
  }
}
