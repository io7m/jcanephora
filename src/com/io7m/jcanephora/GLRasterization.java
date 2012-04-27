package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Simplified interface to the rasterization section of the pipeline.
 */

public interface GLRasterization
{
  /**
   * Set the width in pixels of rasterized lines.
   * 
   * @param width
   *          The width in pixels.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void lineSetWidth(
    final float width)
    throws GLException;

  /**
   * Disable smooth rasterization of lines.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void lineSmoothingDisable()
    throws GLException;

  /**
   * Enable smooth rasterization of lines.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void lineSmoothingEnable()
    throws GLException;

  /**
   * Set the polygon rasterization mode for the polygons specified by
   * <code>faces</code> to <code>mode</code>. The OpenGL default is
   * <code>POLYGON_FILL</code> for both front and back faces.
   * 
   * @param faces
   *          The selected faces.
   * @param mode
   *          The rasterization mode.
   * @throws ConstraintError
   *           Iff <code>faces == null | mode == null</code>.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void polygonSetMode(
    final @Nonnull FaceSelection faces,
    final @Nonnull PolygonMode mode)
    throws ConstraintError,
      GLException;

  /**
   * Disable smooth rasterization of polygons.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void polygonSmoothingDisable()
    throws GLException;

  /**
   * Enable smooth rasterization of polygons.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void polygonSmoothingEnable()
    throws GLException;
}
