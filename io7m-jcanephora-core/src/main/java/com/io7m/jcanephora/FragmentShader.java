package com.io7m.jcanephora;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * An immutable reference to a fragment shader.
 */

@Immutable public final class FragmentShader extends Deletable implements
  GLName,
  GLResource
{
  private final int             id;
  private final @Nonnull String name;
  private boolean               deleted;

  FragmentShader(
    final int id,
    final @Nonnull String name)
    throws ConstraintError
  {
    this.id =
      Constraints.constrainRange(id, 1, Integer.MAX_VALUE, "shader ID");
    this.name = Constraints.constrainNotNull(name, "shader file");
    this.deleted = false;
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
    final FragmentShader other = (FragmentShader) obj;
    if (this.id != other.id) {
      return false;
    }
    return this.name.equals(other.name);
  }

  /**
   * Retrieve the raw OpenGL 'location' of the fragment shader.
   */

  @Override public int getGLName()
  {
    return this.id;
  }

  /**
   * Retrieve the name of the vertex shader.
   */

  public @Nonnull String getName()
  {
    return this.name;
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + this.id;
    result = (prime * result) + this.name.hashCode();
    return result;
  }

  @Override public void resourceDelete(
    final @Nonnull GLInterfaceEmbedded gl)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    gl.fragmentShaderDelete(this);
  }

  @Override public boolean resourceIsDeleted()
  {
    return this.deleted;
  }

  @Override void setDeleted()
  {
    this.deleted = true;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[FragmentShader ");
    builder.append(this.getGLName());
    builder.append(" \"");
    builder.append(this.getName());
    builder.append("\"]");
    return builder.toString();
  }
}