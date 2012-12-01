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

  <G extends GLInterfaceEmbedded> void resourceDelete(
    final @Nonnull G gl)
    throws ConstraintError,
      GLException;

  /**
   * Return <code>true</code> iff the given resource has been deleted.
   */

  boolean resourceIsDeleted();
}
