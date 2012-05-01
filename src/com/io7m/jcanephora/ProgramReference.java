package com.io7m.jcanephora;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * An immutable reference to an OpenGL shading program.
 */

@Immutable public final class ProgramReference implements GLResource
{
  private final int             id;
  private final @Nonnull String name;

  ProgramReference(
    final int id,
    final @Nonnull String name)
    throws ConstraintError
  {
    this.id =
      Constraints.constrainRange(id, 1, Integer.MAX_VALUE, "Program ID");
    this.name = Constraints.constrainNotNull(name, "Program name");
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
    final ProgramReference other = (ProgramReference) obj;
    if (this.id != other.id) {
      return false;
    }
    return true;
  }

  /**
   * Retrieve the raw OpenGL 'location' of the program.
   */

  public int getLocation()
  {
    return this.id;
  }

  /**
   * Retrieve the name of the program.
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
    return result;
  }

  @Override public void resourceDelete(
    final @Nonnull GLInterface gl)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    gl.programDelete(this);
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[ProgramReference ");
    builder.append(this.id);
    builder.append(" \"");
    builder.append(this.name);
    builder.append("\"]");
    return builder.toString();
  }
}
