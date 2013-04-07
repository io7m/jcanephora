/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

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
