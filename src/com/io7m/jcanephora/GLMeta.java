package com.io7m.jcanephora;

import javax.annotation.Nonnull;

/**
 * Functions returning information about the OpenGL implementation.
 */

public interface GLMeta
{
  /**
   * Return and reset the current OpenGL error state.
   * 
   * @return The current OpenGL error code.
   */

  int getError();

  /**
   * Return the name of the OpenGL renderer.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  @Nonnull String getRenderer()
    throws GLException;

  /**
   * Return the name of the OpenGL vendor.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  @Nonnull String getVendor()
    throws GLException;

  /**
   * Return the version string for the OpenGL implementation.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  @Nonnull String getVersion()
    throws GLException;
}
