package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Simplified and type-safe interface to OpenGL logic operations.
 */

public interface GLLogic
{
  /**
   * Disable logical operations.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void logicOperationsDisable()
    throws GLException;

  /**
   * Enable logical operations on the framebuffer to be applied between the
   * incoming RGBA colour and the RGBA colour at the corresponding location in
   * the framebuffer.
   * 
   * @param operation
   *          The logical operation to use.
   * @throws ConstraintError
   *           Iff <code>operation == null</code>.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void logicOperationsEnable(
    final @Nonnull LogicOperation operation)
    throws ConstraintError,
      GLException;

  /**
   * Return <code>true</code> iff logical operations are enabled.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  boolean logicOperationsEnabled()
    throws GLException;
}