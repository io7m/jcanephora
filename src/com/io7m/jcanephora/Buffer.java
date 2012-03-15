package com.io7m.jcanephora;

/**
 * Methods common to all OpenGL buffer types.
 */

public interface Buffer
{
  /**
   * Return the number of elements in the buffer.
   */

  long getElements();

  /**
   * Return the size in bytes of individual elements in the array.
   */

  long getElementSizeBytes();

  /**
   * Return the location of the buffer.
   */

  int getLocation();

  /**
   * Get the total size in bytes of the given buffer.
   * 
   * Should always be equal to {@link Buffer#getElements() *
   * Buffer#getElementSizeBytes()}.
   */

  long getSizeBytes();
}
