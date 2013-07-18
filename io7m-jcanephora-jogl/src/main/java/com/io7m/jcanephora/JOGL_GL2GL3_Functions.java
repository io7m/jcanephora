/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
 * 
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.io7m.jcanephora;

import javax.annotation.Nonnull;
import javax.media.opengl.GL;
import javax.media.opengl.GL2GL3;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

final class JOGL_GL2GL3_Functions
{
  static void logicOperationsDisable(
    final @Nonnull GL2GL3 gl)
    throws GLException
  {
    gl.glDisable(GL.GL_COLOR_LOGIC_OP);
    JOGL_GL_Functions.checkError(gl);
  }

  static void logicOperationsEnable(
    final @Nonnull GL2GL3 gl,
    final @Nonnull LogicOperation operation)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(operation, "Logic operation");
    gl.glEnable(GL.GL_COLOR_LOGIC_OP);
    gl.glLogicOp(JOGL_GLTypeConversions.logicOpToGL(operation));
    JOGL_GL_Functions.checkError(gl);
  }

  static boolean logicOperationsEnabled(
    final @Nonnull GL2GL3 gl)
    throws GLException
  {
    final boolean e = gl.glIsEnabled(GL.GL_COLOR_LOGIC_OP);
    JOGL_GL_Functions.checkError(gl);
    return e;
  }

  static void pointProgramSizeControlDisable(
    final @Nonnull GL2GL3 gl)
    throws GLException
  {
    gl.glDisable(GL2GL3.GL_VERTEX_PROGRAM_POINT_SIZE);
    JOGL_GL_Functions.checkError(gl);
  }

  static void pointProgramSizeControlEnable(
    final @Nonnull GL2GL3 gl)
    throws GLException
  {
    gl.glEnable(GL2GL3.GL_VERTEX_PROGRAM_POINT_SIZE);
    JOGL_GL_Functions.checkError(gl);
  }

  static boolean pointProgramSizeControlIsEnabled(
    final @Nonnull GL2GL3 gl)
    throws GLException
  {
    final boolean e = gl.glIsEnabled(GL2GL3.GL_VERTEX_PROGRAM_POINT_SIZE);
    JOGL_GL_Functions.checkError(gl);
    return e;
  }

  static void polygonSetMode(
    final @Nonnull GL2GL3 gl,
    final @Nonnull GLStateCache cache,
    final @Nonnull PolygonMode mode)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(mode, "Polygon mode");

    final int im = JOGL_GLTypeConversions.polygonModeToGL(mode);
    gl.glPolygonMode(GL.GL_FRONT_AND_BACK, im);
    JOGL_GL_Functions.checkError(gl);
    cache.polygon_mode = mode;
  }

  static void polygonSmoothingDisable(
    final @Nonnull GL2GL3 gl)
    throws GLException
  {
    gl.glDisable(GL2GL3.GL_POLYGON_SMOOTH);
    JOGL_GL_Functions.checkError(gl);
  }

  static void polygonSmoothingEnable(
    final @Nonnull GL2GL3 gl)
    throws GLException
  {
    gl.glEnable(GL2GL3.GL_POLYGON_SMOOTH);
    JOGL_GL_Functions.checkError(gl);
  }

  static boolean polygonSmoothingIsEnabled(
    final @Nonnull GL2GL3 gl)
    throws GLException
  {
    final boolean e = gl.glIsEnabled(GL2GL3.GL_POLYGON_SMOOTH);
    JOGL_GL_Functions.checkError(gl);
    return e;
  }

}
