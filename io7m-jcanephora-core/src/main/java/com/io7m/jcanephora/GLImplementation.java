package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * The interface exposed by the current OpenGL implementation.
 */

public interface GLImplementation
{
  /**
   * Retrieve access to the implementation's OpenGL 3.0 interface.
   * 
   * @throws ConstraintError
   *           Iff <code>implementationProvidesGL3() == false</code>.
   */

  @Nonnull GLInterface3 implementationGetGL3()
    throws ConstraintError;

  /**
   * Retrieve access to the implementation's OpenGL ES2 interface.
   */

  @Nonnull GLInterfaceES2 implementationGetGLES2();

  /**
   * Return <code>true</code> iff the implementation supports OpenGL 3.0.
   */

  boolean implementationProvidesGL3();
}
