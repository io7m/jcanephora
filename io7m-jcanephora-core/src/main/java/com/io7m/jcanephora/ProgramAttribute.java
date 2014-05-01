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

package com.io7m.jcanephora;

import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeCheck;

/**
 * <p>
 * An immutable reference to an active attribute variable for a program.
 * </p>
 */

public final class ProgramAttribute
{
  private final int                    index;
  private final int                    location;
  private final String                 name;
  private final ProgramReferenceUsableType program;
  private final JCGLType               type;

  ProgramAttribute(
    final ProgramReferenceUsableType in_program,
    final int in_index,
    final int in_location,
    final String in_name,
    final JCGLType in_type)
  {
    this.program = NullCheck.notNull(in_program, "Program");
    this.index =
      (int) RangeCheck.checkIncludedIn(
        in_index,
        "Attribute index",
        RangeCheck.NATURAL_INTEGER,
        "Valid attribute indices");
    this.location =
      (int) RangeCheck.checkIncludedIn(
        in_location,
        "Attribute location",
        RangeCheck.NATURAL_INTEGER,
        "Valid attribute locations");
    this.type = NullCheck.notNull(in_type, "Attribute type");
    this.name = NullCheck.notNull(in_name, "Attribute name");
  }

  /**
   * @return The raw OpenGL 'location' of the attribute.
   */

  public int getLocation()
  {
    return this.location;
  }

  /**
   * @return The name of the attribute. This is the name of the attribute as
   *         declared in the respective shading program.
   */

  public String getName()
  {
    return this.name;
  }

  /**
   * @return A reference to the program that owns the attribute.
   */

  public ProgramReferenceUsableType getProgram()
  {
    return this.program;
  }

  /**
   * @return The OpenGL type of the attribute.
   */

  public JCGLType getType()
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
    final String r = builder.toString();
    assert r != null;
    return r;
  }
}
