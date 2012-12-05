/*
 * Copyright © 2012 http://io7m.com
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
import javax.annotation.concurrent.Immutable;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * A immutable reference to an active attribute variable for a program.
 * 
 * An attribute has a name, a location, and a type.
 */

@Immutable public final class ProgramAttribute
{
  private final int                       index;
  private final int                       location;
  private final @Nonnull GLType.Type      type;
  private final @Nonnull String           name;
  private final @Nonnull ProgramReference program;

  ProgramAttribute(
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

  /**
   * Retrieve the raw OpenGL 'location' of the attribute.
   */

  public int getLocation()
  {
    return this.location;
  }

  /**
   * Retrieve the name of the attribute. This is the name of the attribute as
   * declared in the respective shading program.
   */

  public @CheckForNull String getName()
  {
    return this.name;
  }

  /**
   * Retrieve a reference to the program that owns the attribute.
   */

  public @Nonnull ProgramReference getProgram()
  {
    return this.program;
  }

  /**
   * Retrieve the OpenGL type of the attribute.
   */

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
