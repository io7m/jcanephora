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

import com.io7m.jareas.core.AreaInclusiveUnsignedLType;
import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.api.JCGLViewportsType;
import com.io7m.jnull.NullCheck;
import com.io7m.junsigned.ranges.UnsignedRangeInclusiveL;
import com.jogamp.opengl.GL3;

final class JOGLViewports implements JCGLViewportsType
{
  private final GL3 gl;

  JOGLViewports(final JOGLContext c)
  {
    final JOGLContext context = NullCheck.notNull(c);
    this.gl = context.getGL3();
    JOGLErrorChecking.checkErrors(this.gl);
  }

  @Override public void viewportSet(
    final AreaInclusiveUnsignedLType area)
    throws JCGLException
  {
    NullCheck.notNull(area, "Viewport area");

    final UnsignedRangeInclusiveL range_x = area.getRangeX();
    final UnsignedRangeInclusiveL range_y = area.getRangeY();
    this.gl.glViewport(
      (int) range_x.getLower(),
      (int) range_y.getLower(),
      (int) range_x.getInterval(),
      (int) range_y.getInterval());
  }
}