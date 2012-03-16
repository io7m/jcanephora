package com.io7m.jcanephora;

import org.lwjgl.opengl.GL11;

/**
 * A simple class for checking if an OpenGL error has occurred.
 */

public final class GLError
{
  /**
   * Raise <code>GLException</code> iff the current OpenGL error state is not
   * <code>GL_NO_ERROR</code>.
   * 
   * @param gl
   *          The current OpenGL interface.
   * @throws GLException
   *           Iff the current OpenGL error state is not
   *           <code>GL_NO_ERROR</code>.
   */

  public static void check(
    final GLInterface gl)
    throws GLException
  {
    final int code = gl.getError();

    if (code != GL11.GL_NO_ERROR) {
      throw new GLException(code, "OpenGL error: code " + code);
    }
  }
}
