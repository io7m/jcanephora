package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Simplified interface to the rasterization section of the pipeline.
 */

public interface GLRasterization
{
  /**
   * Return the maximum width of aliased lines supported by the
   * implementation.
   */

  int lineAliasedGetMaximumWidth();

  /**
   * Return the minimum width of aliased lines supported by the
   * implementation.
   */

  int lineAliasedGetMinimumWidth();

  /**
   * Set the width in pixels of rasterized lines.
   * 
   * @param width
   *          The width in pixels.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   * @throws ConstraintError
   *           Iff any of the following conditions hold:
   *           <ul>
   *           <li>Line smoothing is enabled and
   *           <code>lineSmoothGetMinimumWidth() <= width <= lineSmoothGetMaximumWidth() == false</code>
   *           </li>
   *           <li>Line smoothing is disabled and
   *           <code>lineAliasedGetMinimumWidth() <= width <= lineAliasedGetMaximumWidth() == false</code>
   *           </li>
   *           </ul>
   */

  void lineSetWidth(
    final float width)
    throws GLException,
      ConstraintError;

  /**
   * Return the maximum width of antialiased lines supported by the
   * implementation.
   */

  int lineSmoothGetMaximumWidth();

  /**
   * Return the minimum width of antialiased lines supported by the
   * implementation.
   */

  int lineSmoothGetMinimumWidth();

  /**
   * Disable smooth rasterization of lines.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void lineSmoothingDisable()
    throws GLException;

  /**
   * Enable smooth rasterization of lines.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void lineSmoothingEnable()
    throws GLException;

  /**
   * Return the maximum width of rasterized points supported by the
   * implementation.
   */

  int pointGetMaximumWidth();

  /**
   * Return the minimum width of rasterized points supported by the
   * implementation.
   */

  int pointGetMinimumWidth();

  /**
   * Disable control of the size of rasterized points by shading programs.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   * @see GLRasterization#pointProgramSizeControlEnable()
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
