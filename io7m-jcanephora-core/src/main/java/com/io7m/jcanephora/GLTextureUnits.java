package com.io7m.jcanephora;

import javax.annotation.Nonnull;

/**
 * Type-safe interface to the GPU's texture units.
 */

public interface GLTextureUnits
{
  /**
   * Return the maximum texture size supported by the current implementation.
   * 'Size' refers to the length of a side, so if the implementation returns
   * <code>8192</code>, the largest texture that can be created is
   * <code>8192 * 8192</code>.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  public int textureGetMaximumSize()
    throws GLException;

  /**
   * Retrieve all available texture units for the current implementation.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  public @Nonnull TextureUnit[] textureGetUnits()
    throws GLException;
}
