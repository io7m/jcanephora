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

package com.io7m.jcanephora.fake;

import com.io7m.jcanephora.core.JCGLProgramAttributeType;
import com.io7m.jcanephora.core.JCGLProgramShaderUsableType;
import com.io7m.jcanephora.core.JCGLType;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeCheck;
import com.io7m.jranges.Ranges;

/**
 * A fake implementation of program attributes.
 */

public final class FakeProgramAttribute extends FakeObjectPseudoShared
  implements JCGLProgramAttributeType
{
  private final int location;
  private final String name;
  private final JCGLProgramShaderUsableType program;
  private final JCGLType type;

  /**
   * Construct an attribute.
   *
   * @param in_context  The context
   * @param in_program  The owning program
   * @param in_location The attribute location
   * @param in_name     The attribute name
   * @param in_type     The attribute type
   */

  public FakeProgramAttribute(
    final FakeContext in_context,
    final JCGLProgramShaderUsableType in_program,
    final int in_location,
    final String in_name,
    final JCGLType in_type)
  {
    super(in_context);

    this.program = NullCheck.notNull(in_program, "Program");
    this.location = RangeCheck.checkIncludedInInteger(
      in_location,
      "Attribute location",
      Ranges.NATURAL_INTEGER,
      "Valid attribute locations");
    this.type = NullCheck.notNull(in_type, "Attribute type");
    this.name = NullCheck.notNull(in_name, "Attribute name");
  }

  @Override
  public String name()
  {
    return this.name;
  }

  @Override
  public JCGLProgramShaderUsableType program()
  {
    return this.program;
  }

  @Override
  public JCGLType type()
  {
    return this.type;
  }

  @Override
  public int glName()
  {
    return this.location;
  }
}
