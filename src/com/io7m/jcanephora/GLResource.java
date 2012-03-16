package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * A resource of some description that requires access to OpenGL for deletion.
 */

public interface GLResource
{
  /**
   * Deallocate the resources associated with the associated object.
   * 
   * @param gl
   *          The current OpenGL interface.
   * @throws ConstraintError
   *           Iff <code>gl == null</code>.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void delete(
    final @Nonnull GLInterface gl)
    throws ConstraintError,
      GLException;
}
