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

import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLPolygonMode;
import com.io7m.jcanephora.core.api.JCGLPolygonModesType;
import com.io7m.jnull.NullCheck;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;

final class JOGLPolygonMode implements JCGLPolygonModesType
{
  private final GL3 gl;
  private JCGLPolygonMode mode;

  JOGLPolygonMode(final JOGLContext c)
  {
    final JOGLContext context = NullCheck.notNull(c, "Context");
    this.gl = context.getGL3();
    this.mode = JCGLPolygonMode.POLYGON_FILL;

    /*
     * Configure baseline defaults.
     */

    this.gl.glPolygonMode(
      GL.GL_FRONT_AND_BACK,
      JOGLTypeConversions.polygonModeToGL(this.mode));
  }

  @Override
  public JCGLPolygonMode polygonGetMode()
    throws JCGLException
  {
    return this.mode;
  }

  @Override
  public void polygonSetMode(
    final JCGLPolygonMode m)
    throws JCGLException
  {
    NullCheck.notNull(m, "Mode");

    final int im = JOGLTypeConversions.polygonModeToGL(m);
    this.gl.glPolygonMode(GL.GL_FRONT_AND_BACK, im);
    this.mode = m;
  }
}
