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

package com.io7m.jcanephora.lwjgl3;

import com.io7m.jareas.core.AreaInclusiveUnsignedLType;
import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.api.JCGLViewportsType;
import com.io7m.jnull.NullCheck;
import com.io7m.junsigned.ranges.UnsignedRangeInclusiveL;
import org.lwjgl.opengl.GL11;

final class LWJGL3Viewports implements JCGLViewportsType
{
  LWJGL3Viewports(final LWJGL3Context c)
  {
    final LWJGL3Context context = NullCheck.notNull(c);
    LWJGL3ErrorChecking.checkErrors();
  }

  @Override
  public void viewportSet(
    final AreaInclusiveUnsignedLType area)
    throws JCGLException
  {
    NullCheck.notNull(area, "Viewport area");

    final UnsignedRangeInclusiveL range_x = area.getRangeX();
    final UnsignedRangeInclusiveL range_y = area.getRangeY();
    GL11.glViewport(
      (int) range_x.getLower(),
      (int) range_y.getLower(),
      (int) range_x.getInterval(),
      (int) range_y.getInterval());
  }
}
