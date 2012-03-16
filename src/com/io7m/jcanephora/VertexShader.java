package com.io7m.jcanephora;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * An immutable reference to a vertex shader.
 */

@Immutable public final class VertexShader implements GLResource
{
  private final int             id;
  private final @Nonnull String name;

  VertexShader(
    final int id,
    final @Nonnull String name)
    throws ConstraintError
  {
    this.id =
      Constraints.constrainRange(id, 1, Integer.MAX_VALUE, "shader ID");
    this.name = Constraints.constrainNotNull(name, "shader file");
  }

  @Override public void delete(
    final @Nonnull GLInterface gl)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    gl.deleteVertexShader(this);
  }

  public int getID()
  {
    return this.id;
  }

  public @Nonnull String getName()
  {
    return this.name;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[VertexShaderID ");
    builder.append(this.getID());
    builder.append(" \"");
    builder.append(this.getName());
    builder.append("\"]");
    return builder.toString();
  }
}
