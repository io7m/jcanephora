/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.fake;

import com.io7m.jcanephora.JCGLType;
import com.io7m.jcanephora.ProgramUniformType;
import com.io7m.jcanephora.ProgramUsableType;
import com.io7m.jequality.annotations.EqualityStructural;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;
import com.io7m.jranges.RangeCheck;

/**
 * <p>
 * A reference to an active uniform variable for a program.
 * </p>
 * <p>
 * A uniform variable has a name, a location, and a type.
 * </p>
 */

@EqualityStructural public final class FakeProgramUniform extends
  FakeObjectPseudoShared implements ProgramUniformType
{
  /**
   * Construct a new program uniform.
   *
   * @param in_context
   *          The OpenGL context.
   * @param in_program
   *          The program.
   * @param in_index
   *          The index.
   * @param in_location
   *          The location.
   * @param in_name
   *          The name.
   * @param in_type
   *          The type.
   * @return A new program uniform.
   */

  public static FakeProgramUniform newUniform(
    final FakeContext in_context,
    final ProgramUsableType in_program,
    final int in_index,
    final int in_location,
    final String in_name,
    final JCGLType in_type)
  {
    return new FakeProgramUniform(
      in_context,
      in_program,
      in_index,
      in_location,
      in_name,
      in_type);
  }
  private final int               index;
  private final int               location;
  private final String            name;
  private final ProgramUsableType program;

  private final JCGLType          type;

  private FakeProgramUniform(
    final FakeContext in_context,
    final ProgramUsableType in_program,
    final int in_index,
    final int in_location,
    final String in_name,
    final JCGLType in_type)
  {
    super(in_context);

    this.program = NullCheck.notNull(in_program, "Program");
    this.index =
      (int) RangeCheck.checkIncludedIn(
        in_index,
        "Uniform index",
        RangeCheck.NATURAL_INTEGER,
        "Valid attribute indices");
    this.location =
      (int) RangeCheck.checkIncludedIn(
        in_location,
        "Uniform location",
        RangeCheck.NATURAL_INTEGER,
        "Valid attribute locations");
    this.type = NullCheck.notNull(in_type, "Uniform type");
    this.name = NullCheck.notNull(in_name, "Uniform name");
  }

  @Override public boolean equals(
    final @Nullable Object obj)
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
    final FakeProgramUniform other = (FakeProgramUniform) obj;
    return (this.index == other.index)
      && (this.location == other.location)
      && (this.name.equals(other.name))
      && (this.program.equals(other.program))
      && (this.type == other.type);
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + this.index;
    result = (prime * result) + this.location;
    result = (prime * result) + this.name.hashCode();
    result = (prime * result) + this.program.hashCode();
    result = (prime * result) + this.type.hashCode();
    return result;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[FakeUniform ");
    builder.append(this.uniformGetLocation());
    builder.append(" ");
    builder.append(this.index);
    builder.append(" ");
    builder.append(this.uniformGetType());
    builder.append(" \"");
    builder.append(this.uniformGetName());
    builder.append("\"");
    builder.append("]");
    final String r = builder.toString();
    assert r != null;
    return r;
  }

  @Override public int uniformGetLocation()
  {
    return this.location;
  }

  @Override public String uniformGetName()
  {
    return this.name;
  }

  @Override public ProgramUsableType uniformGetProgram()
  {
    return this.program;
  }

  @Override public JCGLType uniformGetType()
  {
    return this.type;
  }
}
