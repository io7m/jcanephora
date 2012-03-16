package com.io7m.jcanephora;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * A immutable reference to an active attribute variable for a program.
 * 
 * An attribute has a name, a location, and a type.
 */

@Immutable public final class Attribute
{
  private final int                       index;
  private final int                       location;
  private final @Nonnull GLType.Type      type;
  private final @Nonnull String           name;
  private final @Nonnull ProgramReference program;

  Attribute(
    final @Nonnull ProgramReference program,
    final int index,
    final int location,
    final @CheckForNull String name,
    final @Nonnull GLType.Type type)
    throws ConstraintError
  {
    this.program = Constraints.constrainNotNull(program, "Program");
    this.index =
      Constraints.constrainRange(
        index,
        0,
        Integer.MAX_VALUE,
        "Attribute index");
    this.location =
      Constraints.constrainRange(
        location,
        0,
        Integer.MAX_VALUE,
        "Attribute location");
    this.type = Constraints.constrainNotNull(type, "Attribute type");
    this.name = Constraints.constrainNotNull(name, "Attribute name");
  }

  public int getLocation()
  {
    return this.location;
  }

  public @CheckForNull String getName()
  {
    return this.name;
  }

  public @Nonnull ProgramReference getProgram()
  {
    return this.program;
  }

  public @Nonnull GLType.Type getType()
  {
    return this.type;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[AttributeID ");
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
