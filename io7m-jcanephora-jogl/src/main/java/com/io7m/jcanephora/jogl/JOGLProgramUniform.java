/*
 * Copyright © 2015 <code@io7m.com> http://io7m.com
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

import com.io7m.jcanephora.core.JCGLProgramShaderUsableType;
import com.io7m.jcanephora.core.JCGLProgramUniformType;
import com.io7m.jcanephora.core.JCGLType;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeCheck;
import com.io7m.jranges.Ranges;
import com.jogamp.opengl.GLContext;

final class JOGLProgramUniform extends JOGLObjectPseudoShared
  implements JCGLProgramUniformType
{
  private final int location;
  private final String name;
  private final JCGLProgramShaderUsableType program;
  private final JCGLType type;
  private final int size;

  JOGLProgramUniform(
    final GLContext in_context,
    final JCGLProgramShaderUsableType in_program,
    final int in_location,
    final String in_name,
    final JCGLType in_type,
    final int in_size)
  {
    super(in_context);

    this.program = NullCheck.notNull(in_program, "Program");
    this.location = RangeCheck.checkIncludedInInteger(
      in_location,
      "Uniform location",
      Ranges.NATURAL_INTEGER,
      "Valid uniform locations");
    this.type = NullCheck.notNull(in_type, "Uniform type");
    this.name = NullCheck.notNull(in_name, "Uniform name");
    this.size = RangeCheck.checkIncludedInInteger(
      in_size, "Size", Ranges.POSITIVE_INTEGER, "Valid sizes");
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder("[ProgramUniform ");
    sb.append(this.location);
    sb.append(" ");
    sb.append(this.name);
    sb.append(" ");
    sb.append(this.type);
    sb.append(']');
    return sb.toString();
  }

  @Override
  public String getName()
  {
    return this.name;
  }

  @Override
  public JCGLProgramShaderUsableType getProgram()
  {
    return this.program;
  }

  @Override
  public JCGLType getType()
  {
    return this.type;
  }

  @Override
  public int getSize()
  {
    return this.size;
  }

  @Override
  public int getGLName()
  {
    return this.location;
  }
}
