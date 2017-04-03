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
import com.io7m.jcanephora.core.JCGLPolygonMode;
import com.io7m.jcanephora.core.api.JCGLPolygonModesType;
import com.io7m.jnull.NullCheck;
import org.lwjgl.opengl.GL11;

final class LWJGL3PolygonMode implements JCGLPolygonModesType
{
  private JCGLPolygonMode mode;

  LWJGL3PolygonMode(final LWJGL3Context c)
  {
    final LWJGL3Context context = NullCheck.notNull(c);
    this.mode = JCGLPolygonMode.POLYGON_FILL;

    /**
     * Configure baseline defaults.
     */

    GL11.glPolygonMode(
      GL11.GL_FRONT_AND_BACK,
      LWJGL3TypeConversions.polygonModeToGL(this.mode));
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
    NullCheck.notNull(m);

    final int im = LWJGL3TypeConversions.polygonModeToGL(m);
    GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, im);
    this.mode = m;
  }
}
