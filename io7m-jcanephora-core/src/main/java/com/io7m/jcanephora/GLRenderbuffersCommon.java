package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Type-safe interface to the renderbuffer API exposed by all OpenGL
 * implementations.
 */

public interface GLRenderbuffersCommon
{
  /**
   * Delete the depth renderbuffer specified by <code>buffer</code>.
   * 
   * @param buffer
   *          The buffer.
   * @throws ConstraintError
   *           Iff <code>buffer == null</code>.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void renderbufferDelete(
    final @Nonnull Renderbuffer<?> buffer)
    throws ConstraintError,
      GLException;
}
