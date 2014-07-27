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

import com.io7m.jcanephora.VertexShaderType;
import com.io7m.jequality.annotations.EqualityStructural;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;

/**
 * An immutable reference to a vertex shader.
 */

@EqualityStructural final class FakeVertexShader extends FakeObjectShared implements
  VertexShaderType
{
  private final String name;

  FakeVertexShader(
    final FakeContext in_context,
    final int in_id,
    final String in_name)
  {
    super(in_context, in_id);
    this.name = NullCheck.notNull(in_name, "shader file");
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
    final FakeVertexShader other = (FakeVertexShader) obj;
    if (super.getGLName() != other.getGLName()) {
      return false;
    }
    if (!this.name.equals(other.name)) {
      return false;
    }
    return true;
  }

  /**
   * @return The name associated with the shader.
   */

  public String getName()
  {
    return this.name;
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + this.getGLName();
    result = (prime * result) + this.name.hashCode();
    return result;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[FakeVertexShader ");
    builder.append(this.getGLName());
    builder.append(" \"");
    builder.append(this.getName());
    builder.append("\"]");
    final String r = builder.toString();
    assert r != null;
    return r;
  }
}
