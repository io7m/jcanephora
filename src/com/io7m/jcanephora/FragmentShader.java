package com.io7m.jcanephora;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * An immutable reference to a fragment shader.
 */

@Immutable public final class FragmentShader implements GLResource
{
  private final int             id;
  private final @Nonnull String name;

  FragmentShader(
    final int id,
    final @Nonnull String name)
    throws ConstraintError
  {
    this.id =
      Constraints.constrainRange(id, 1, Integer.MAX_VALUE, "shader ID");
    this.name = Constraints.constrainNotNull(name, "shader file");
  }

  /**
   * Retrieve the raw OpenGL 'location' of the fragment shader.
   */

  public int getLocation()
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

  @Override public void resourceDelete(
    final @Nonnull GLInterface gl)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    gl.fragmentShaderDelete(this);
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[FragmentShaderID ");
    builder.append(this.getLocation());
    builder.append(" \"");
    builder.append(this.getName());
    builder.append("\"]");
    return builder.toString();
  }
}
