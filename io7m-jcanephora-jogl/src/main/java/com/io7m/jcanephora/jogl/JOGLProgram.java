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

import java.util.Collections;
import java.util.Map;

import javax.media.opengl.GLContext;

import com.io7m.jcanephora.ProgramAttributeType;
import com.io7m.jcanephora.ProgramType;
import com.io7m.jcanephora.ProgramUniformType;
import com.io7m.jequality.annotations.EqualityStructural;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;

/**
 * An immutable reference to an OpenGL shading program.
 */

@EqualityStructural final class JOGLProgram extends JOGLObjectShared implements
  ProgramType
{
  private final Map<String, ProgramAttributeType> attributes;
  private final String                            name;
  private final Map<String, ProgramUniformType>   uniforms;

  JOGLProgram(
    final GLContext in_context,
    final int in_id,
    final String in_name,
    final Map<String, ProgramUniformType> in_uniforms,
    final Map<String, ProgramAttributeType> in_attributes)
  {
    super(in_context, in_id);
    this.name = NullCheck.notNull(in_name, "Program name");

    final Map<String, ProgramUniformType> u =
      Collections.unmodifiableMap(NullCheck.notNull(in_uniforms, "Uniforms"));
    assert u != null;
    this.uniforms = u;

    final Map<String, ProgramAttributeType> a =
      Collections.unmodifiableMap(NullCheck.notNull(
        in_attributes,
        "Attributes"));
    assert a != null;
    this.attributes = a;
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
    final JOGLProgram other = (JOGLProgram) obj;
    return super.getGLName() == other.getGLName();
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + super.getGLName();
    result = (prime * result) + this.name.hashCode();
    return result;
  }

  @Override public Map<String, ProgramAttributeType> programGetAttributes()
  {
    return this.attributes;
  }

  @Override public String programGetName()
  {
    return this.name;
  }

  @Override public Map<String, ProgramUniformType> programGetUniforms()
  {
    return this.uniforms;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[JOGLProgramReference ");
    builder.append(super.getGLName());
    builder.append(" \"");
    builder.append(this.name);
    builder.append("\"]");
    final String r = builder.toString();
    assert r != null;
    return r;
  }
}
