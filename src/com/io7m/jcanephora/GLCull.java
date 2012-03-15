package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Simplified and type-safe interface to OpenGL face culling.
 */

public interface GLCull
{
  /**
   * Disable face culling.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void disableCulling()
    throws GLException;

  /**
   * Enable face culling for <code>faces</code> and specify that front faces
   * are specified by giving vertices in the winding order specified by
   * <code>order</code>. The OpenGL defaults are <code>FACE_CULL_BACK</code>
   * and <code>FRONT_FACE_COUNTER_CLOCKWISE</code> for <code>faces</code> and
   * <code>order</code> respectively.
   * 
   * @param faces
   *          The faces to cull.
   * @param order
   *          The order of vertices in front-facing faces.
   * @throws ConstraintError
   *           Iff <code>faces == null | order == null</code>.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void enableCulling(
    final @Nonnull FaceSelection faces,
    final @Nonnull FaceWindingOrder order)
    throws ConstraintError,
      GLException;
}
