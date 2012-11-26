package com.io7m.jcanephora;

/**
 * Control over polygon smoothing.
 */

public interface GLPolygonSmoothing
{
  /**
   * Disable smooth rasterization of polygons.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void polygonSmoothingDisable()
    throws GLException;

  /**
   * Enable smooth rasterization of polygons.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void polygonSmoothingEnable()
    throws GLException;

  /**
   * Return <code>true</code> iff smooth rasterization of polygons is enabled.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  boolean polygonSmoothingIsEnabled()
    throws GLException;
}
