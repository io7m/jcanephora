package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * A resource of some description that requires access to OpenGL for deletion.
 */

public interface GLResource
{
  void delete(
    final @Nonnull GLInterface gl)
    throws ConstraintError,
      GLException;
}
