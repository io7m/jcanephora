package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Simplified interface to polygon modes.
 */

public interface GLPolygonModes
{
  /**
   * Return the <code>PolygonMode</code> used for back-facing polygons.
   */

  @Nonnull PolygonMode polygonGetModeBack()
    throws ConstraintError,
      GLException;

  /**
   * Return the <code>PolygonMode</code> used for front-facing polygons.
   */

  @Nonnull PolygonMode polygonGetModeFront()
    throws ConstraintError,
      GLException;

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
}
