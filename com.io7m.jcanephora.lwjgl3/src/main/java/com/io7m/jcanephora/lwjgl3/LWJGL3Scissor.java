/*
 * Copyright © 2016 <code@io7m.com> http://io7m.com
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
import com.io7m.jcanephora.core.api.JCGLScissorType;
import com.io7m.jnull.NullCheck;
import com.io7m.junsigned.ranges.UnsignedRangeInclusiveL;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class LWJGL3Scissor implements JCGLScissorType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(LWJGL3Scissor.class);
  }

  private boolean enabled;

  LWJGL3Scissor(final LWJGL3Context c)
  {
    final LWJGL3Context context = NullCheck.notNull(c);
    GL11.glDisable(GL11.GL_SCISSOR_TEST);
    this.enabled = false;
  }

  @Override
  public void scissorDisable()
    throws JCGLException
  {
    if (this.enabled) {
      GL11.glDisable(GL11.GL_SCISSOR_TEST);
      this.enabled = false;
    } else {
      LWJGL3Scissor.LOG.trace("redundant scissor disable ignored");
    }
  }

  @Override
  public void scissorEnable(
    final AreaInclusiveUnsignedLType area)
    throws JCGLException
  {
    NullCheck.notNull(area);

    final UnsignedRangeInclusiveL range_x = area.getRangeX();
    final UnsignedRangeInclusiveL range_y = area.getRangeY();

    if (!this.enabled) {
      GL11.glEnable(GL11.GL_SCISSOR_TEST);
      this.enabled = true;
      GL11.glScissor(
        (int) range_x.getLower(),
        (int) range_y.getLower(),
        (int) range_x.getInterval(),
        (int) range_y.getInterval());
    } else {
      LWJGL3Scissor.LOG.trace("redundant scissor enable ignored");
    }
  }

  @Override
  public boolean scissorIsEnabled()
    throws JCGLException
  {
    return this.enabled;
  }
}
