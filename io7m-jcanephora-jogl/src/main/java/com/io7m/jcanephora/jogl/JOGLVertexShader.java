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

import com.io7m.jcanephora.core.JCGLVertexShaderType;
import com.io7m.jcanephora.core.JCGLVertexShaderUsableType;
import com.io7m.jnull.NullCheck;
import com.jogamp.opengl.GLContext;

final class JOGLVertexShader extends JOGLReferable
  implements JCGLVertexShaderType
{
  private final String name;

  JOGLVertexShader(
    final GLContext ctx,
    final int id,
    final String in_name)
  {
    super(ctx, id);
    this.name = NullCheck.notNull(in_name);
  }

  static JOGLVertexShader checkVertexShader(
    final GLContext c,
    final JCGLVertexShaderUsableType v)
  {
    return (JOGLVertexShader) JOGLCompatibilityChecks.checkAny(c, v);
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder("[VertexShader ");
    sb.append(super.getGLName());
    sb.append(" ");
    sb.append(this.name);
    sb.append(']');
    return sb.toString();
  }

  @Override
  public String getName()
  {
    return this.name;
  }
}
