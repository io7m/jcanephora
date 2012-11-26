package com.io7m.jcanephora;

/**
 * An object that has a name according to OpenGL.
 * 
 * This could be, for example, the identifier of a texture created with
 * <code>glGenTextures</code>, or a buffer created with
 * <code>glGenBuffers</code>, etc.
 */

public interface GLName
{
  /**
   * Return the raw OpenGL 'name' of the object.
   */

  int getGLName();
}
