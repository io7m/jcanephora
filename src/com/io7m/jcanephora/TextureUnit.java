package com.io7m.jcanephora;

/**
 * An OpenGL texture unit.
 */

public final class TextureUnit
{
  private final int index;

  TextureUnit(
    final int index)
  {
    this.index = index;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[TextureUnit ");
    builder.append(this.index);
    builder.append("]");
    return builder.toString();
  }

  /**
   * Retrieve the index of the texture unit. This value will be between 0 and
   * some implementation-defined exclusive upper limit (usually 16 or 32 in
   * current implementations).
   */

  public int getIndex()
  {
    return this.index;
  }
}
