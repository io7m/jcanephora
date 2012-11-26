package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * A usable shading language program.
 */

public interface UsableProgram
{
  /**
   * Make the program active.
   * 
   * @param gl
   *          An OpenGL interface.
   * @throws ConstraintError
   *           Iff <code>gl == null</code> or one of the constraints for
   *           {@link GLInterfaceEmbedded#programActivate(ProgramReference)}
   *           does not hold.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void activate(
    final @Nonnull GLInterfaceEmbedded gl)
    throws ConstraintError,
      GLException;

  /**
   * Deactivate the current program. If the current program is not active, the
   * function does nothing.
   * 
   * @param gl
   * @throws ConstraintError
   *           Iff <code>gl == null</code>.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void deactivate(
    final @Nonnull GLInterfaceEmbedded gl)
    throws ConstraintError,
      GLException;
}
