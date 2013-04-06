package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.functional.Option;

/**
 * The interface exposed by the current OpenGL implementation.
 */

public interface GLImplementation
{
  /**
   * Return a reference to the OpenGL 3.* interface provided by the
   * implementation, or <code>None</code> if it is not supported.
   */

  @Nonnull Option<GLInterfaceGL3> getGL3();

  /**
   * Return a reference to the interface representing the common subset of
   * OpenGL 3.* and OpenGL ES2.
   */

  @Nonnull GLInterfaceCommon getGLCommon();

  /**
   * Return a reference to the OpenGL ES2 interface provided by the
   * implementation, or <code>None</code> if it is not supported.
   */

  @Nonnull Option<GLInterfaceGLES2> getGLES2();
}
