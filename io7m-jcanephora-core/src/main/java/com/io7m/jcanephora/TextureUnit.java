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

  @Override public boolean equals(
    final Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    final TextureUnit other = (TextureUnit) obj;
    if (this.index != other.index) {
      return false;
    }
    return true;
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

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + this.index;
    return result;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[TextureUnit ");
    builder.append(this.index);
    builder.append("]");
    return builder.toString();
  }
}
