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

import com.io7m.jcanephora.core.JCGLFragmentShaderType;
import com.io7m.jcanephora.core.JCGLFragmentShaderUsableType;
import com.io7m.jnull.NullCheck;
import com.jogamp.opengl.GLContext;

final class JOGLFragmentShader extends JOGLReferable
  implements JCGLFragmentShaderType
{
  private final String name;

  JOGLFragmentShader(
    final GLContext ctx,
    final int id,
    final String in_name)
  {
    super(ctx, id);
    this.name = NullCheck.notNull(in_name, "Name");
  }

  static JOGLFragmentShader checkFragmentShader(
    final GLContext c,
    final JCGLFragmentShaderUsableType f)
  {
    return (JOGLFragmentShader) JOGLCompatibilityChecks.checkAny(c, f);
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder("[FragmentShader ");
    sb.append(super.glName());
    sb.append(" ");
    sb.append(this.name);
    sb.append(']');
    return sb.toString();
  }

  @Override
  public String name()
  {
    return this.name;
  }
}
