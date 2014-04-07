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
import javax.annotation.concurrent.Immutable;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * <p>
 * A reference to an active uniform variable for a program.
 * </p>
 * <p>
 * A uniform variable has a name, a location, and a type.
 * </p>
 * <p>
 * The type is not final, to allow programs to make semantically distinct
 * values type-incompatible via subclassing and/or the addition of phantom
 * type parameters.
 * </p>
 */

@Immutable public class ProgramUniform
{
  private final int                             index;
  private final int                             location;
  private final @Nonnull String                 name;
  private final @Nonnull ProgramReferenceUsable program;
  private final @Nonnull JCGLType               type;

  ProgramUniform(
    final @Nonnull ProgramReferenceUsable program1,
    final int index1,
    final int location1,
    final @CheckForNull String name1,
    final @Nonnull JCGLType type1)
    throws ConstraintError
  {
    this.program = Constraints.constrainNotNull(program1, "Program");
    this.index =
      Constraints.constrainRange(
        index1,
        0,
        Integer.MAX_VALUE,
        "Uniform index");
    this.location =
      Constraints.constrainRange(
        location1,
        0,
        Integer.MAX_VALUE,
        "Uniform location");
    this.type = Constraints.constrainNotNull(type1, "Uniform type");
    this.name = Constraints.constrainNotNull(name1, "Uniform name");
  }

  /**
   * Retrieve the raw OpenGL 'location' of the uniform value.
   */

  public final int getLocation()
  {
    return this.location;
  }

  /**
   * Retrieve the name of the uniform. This is the name of the uniform as
   * declared in the respective shading program.
   */

  public final @CheckForNull String getName()
  {
    return this.name;
  }

  /**
   * Retrieve a reference to the program to which the uniform belongs.
   */

  public final @Nonnull ProgramReferenceUsable getProgram()
  {
    return this.program;
  }

  /**
   * Retrieve the OpenGL type of the uniform.
   */

  public final @Nonnull JCGLType getType()
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
