package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.RangeInclusive;

/**
 * Methods common to all OpenGL buffer types.
 */

public interface Buffer extends GLName
{
  /**
   * Return the range of elements in the buffer. The lower bound will always
   * be <code>0</code>.
   */

  @Nonnull RangeInclusive getRange();

  /**
   * Return the size in bytes of individual elements in the array.
   */

  long getElementSizeBytes();

  /**
   * Get the total size in bytes of the given buffer.
   * 
   * Should always be equal to {@link Buffer#getElements() *
   * Buffer#getElementSizeBytes()}.
   */

  long getSizeBytes();
}
