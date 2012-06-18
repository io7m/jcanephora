package com.io7m.jcanephora;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * A reference to an active uniform variable for a program.
 * 
 * A uniform variable has a name, a location, and a type.
 */

public final class ProgramUniform
{
  private final int                       index;
  private final int                       location;
  private final @Nonnull GLType.Type      type;
  private final @Nonnull String           name;
  private final @Nonnull ProgramReference program;

  ProgramUniform(
    final @Nonnull ProgramReference program,
    final int index,
    final int location,
    final @CheckForNull String name,
    final @Nonnull GLType.Type type)
    throws ConstraintError
  {
    this.program = Constraints.constrainNotNull(program, "Program");
    this.index =
      Constraints
        .constrainRange(index, 0, Integer.MAX_VALUE, "Uniform index");
    this.location =
      Constraints.constrainRange(
        location,
        0,
        Integer.MAX_VALUE,
        "Uniform location");
    this.type = Constraints.constrainNotNull(type, "Uniform type");
    this.name = Constraints.constrainNotNull(name, "Uniform name");
  }

  /**
   * Retrieve the raw OpenGL 'location' of the uniform value.
   */

  public int getLocation()
  {
    return this.location;
  }

  /**
   * Retrieve the name of the uniform. This is the name of the uniform as
   * declared in the respective shading program.
   */

  public @CheckForNull String getName()
  {
    return this.name;
  }

  /**
   * Retrieve a reference to the program to which the uniform belongs.
   */

  public @Nonnull ProgramReference getProgram()
  {
    return this.program;
  }

  /**
   * Retrieve the OpenGL type of the uniform.
   */

  public @Nonnull GLType.Type getType()
  {
    return this.type;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[Uniform ");
    builder.append(this.getLocation());
    builder.append(" ");
    builder.append(this.index);
    builder.append(" ");
    builder.append(this.getType());
    builder.append(" \"");
    builder.append(this.getName());
    builder.append("\"");
    builder.append("]");
    return builder.toString();
  }
}
