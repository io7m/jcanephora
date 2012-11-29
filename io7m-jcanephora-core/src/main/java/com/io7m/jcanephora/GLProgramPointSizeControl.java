package com.io7m.jcanephora;

/**
 * Control over whether shading programs have control of the size of
 * rasterized points.
 */

public interface GLProgramPointSizeControl
{
  /**
   * Disable control of the size of rasterized points by shading programs.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   * @see GLProgramPointSizeControl#pointProgramSizeControlEnable()
   */

  void pointProgramSizeControlDisable()
    throws GLException;

  /**
   * Enable control of the size of rasterized points by shading programs.
   * 
   * Shading programs should write the desired size to the built-in GLSL
   * variable <code>gl_PointSize</code>.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void pointProgramSizeControlEnable()
    throws GLException;

  /**
   * Returns <code>true</code> iff control of point size by shading programs
   * is enabled.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  boolean pointProgramSizeControlIsEnabled()
    throws GLException;
}
