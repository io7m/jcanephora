package com.io7m.jcanephora;

/**
 * Simplified interface to the stencil buffer.
 */

public interface GLStencilBuffer
{
  /**
   * Retrieve the number of bits available in the stencil buffer for the
   * current framebuffer configuration.
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  int stencilBufferGetBits()
    throws GLException;
}
