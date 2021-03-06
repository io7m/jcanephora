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

import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.api.JCGLViewportsType;
import com.io7m.jnull.NullCheck;
import com.io7m.jregions.core.unparameterized.areas.AreaL;
import org.lwjgl.opengl.GL11;

final class LWJGL3Viewports implements JCGLViewportsType
{
  LWJGL3Viewports(final LWJGL3Context c)
  {
    NullCheck.notNull(c, "Context");
    LWJGL3ErrorChecking.checkErrors();
  }

  @Override
  public void viewportSet(
    final AreaL area)
    throws JCGLException
  {
    NullCheck.notNull(area, "Viewport area");

    GL11.glViewport(
      Math.toIntExact(area.minimumX()),
      Math.toIntExact(area.minimumY()),
      Math.toIntExact(area.width()),
      Math.toIntExact(area.height()));
  }
}
