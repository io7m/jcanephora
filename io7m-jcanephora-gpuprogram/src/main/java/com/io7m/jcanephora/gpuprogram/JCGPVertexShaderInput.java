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

package com.io7m.jcanephora.gpuprogram;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.JCGLApi;
import com.io7m.jcanephora.JCGLSLVersionNumber;
import com.io7m.jcanephora.JCGLType;
import com.io7m.jcanephora.JCGLUnsupportedException;

@Immutable public final class JCGPVertexShaderInput implements JCGPToGLSL
{
  public static @Nonnull JCGPVertexShaderInput make(
    final @Nonnull JCGLType type,
    final @Nonnull String name)
    throws ConstraintError
  {
    return new JCGPVertexShaderInput(type, name);
  }

  private final @Nonnull JCGLType type;
  private final @Nonnull String   name;

  private JCGPVertexShaderInput(
    final @Nonnull JCGLType type,
    final @Nonnull String name)
    throws ConstraintError
  {
    this.type = Constraints.constrainNotNull(type, "Type");
    this.name = Constraints.constrainNotNull(name, "Name");
  }

  @Override public boolean equals(
    final Object obj)
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
    final JCGPVertexShaderInput other = (JCGPVertexShaderInput) obj;
    if (!this.name.equals(other.name)) {
      return false;
    }
    if (this.type != other.type) {
      return false;
    }
    return true;
  }

  public @Nonnull String getName()
  {
    return this.name;
  }

  public @Nonnull JCGLType getType()
  {
    return this.type;
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + this.name.hashCode();
    result = (prime * result) + this.type.hashCode();
    return result;
  }

  @Override public @Nonnull String toGLSL(
    final @Nonnull JCGLSLVersionNumber version,
    final @Nonnull JCGLApi api)
    throws JCGLUnsupportedException,
      ConstraintError
  {
    Constraints.constrainNotNull(version, "Version");
    Constraints.constrainNotNull(api, "API");

    final StringBuilder b = new StringBuilder();

    switch (api) {
      case JCGL_ES:
      {
        // GLSL ES 1.0 and earlier use "attribute" to declare vertex shader
        // inputs.
        if (version.getVersionMajor() <= 1) {
          b.append("attribute");
        } else {
          b.append("in");
        }
        break;
      }
      case JCGL_FULL:
      {
        // GLSL 1.2 and earlier use "attribute" to declare vertex shader
        // inputs.
        if (version.getVersionMajor() <= 1) {
          if (version.getVersionMinor() <= 2) {
            b.append("attribute");
          }
        } else {
          b.append("in");
        }
        break;
      }
    }

    b.append(" ");
    b.append(this.type.getName());
    b.append(" ");
    b.append(this.name);
    b.append(";");
    return b.toString();
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[JCGPVertexShaderInput  ");
    builder.append(this.type);
    builder.append(" ");
    builder.append(this.name);
    builder.append("]");
    return builder.toString();
  }
}
