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

package com.io7m.jcanephora.jogl;

import javax.media.opengl.GLContext;

import com.io7m.jcanephora.JCGLType;
import com.io7m.jcanephora.ProgramAttributeType;
import com.io7m.jcanephora.ProgramUsableType;
import com.io7m.jequality.annotations.EqualityStructural;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;
import com.io7m.jranges.RangeCheck;

/**
 * <p>
 * An immutable reference to an active attribute variable for a program.
 * </p>
 */

@EqualityStructural final class JOGLProgramAttribute extends
  JOGLObjectPseudoShared implements ProgramAttributeType
{
  private final int               index;
  private final int               location;
  private final String            name;
  private final ProgramUsableType program;
  private final JCGLType          type;

  JOGLProgramAttribute(
    final GLContext in_context,
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

  @Override public int attributeGetLocation()
  {
    return this.location;
  }

  @Override public String attributeGetName()
  {
    return this.name;
  }

  @Override public ProgramUsableType attributeGetProgram()
  {
    return this.program;
  }

  @Override public JCGLType attributeGetType()
  {
    return this.type;
  }

  @Override public boolean equals(
    @Nullable final Object obj)
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
    final JOGLProgramAttribute other = (JOGLProgramAttribute) obj;
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
    builder.append("[JOGLProgramAttribute ");
    builder.append(this.attributeGetLocation());
    builder.append(" ");
    builder.append(this.index);
    builder.append(" ");
    builder.append(this.attributeGetType());
    builder.append(" \"");
    builder.append(this.attributeGetName());
    builder.append("\"");
    builder.append("]");
    final String r = builder.toString();
    assert r != null;
    return r;
  }
}
