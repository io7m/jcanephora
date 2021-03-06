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

import com.io7m.jcanephora.core.JCGLClearSpecificationType;
import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.api.JCGLClearType;
import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.core.unparameterized.vectors.Vector4D;
import org.lwjgl.opengl.GL11;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;

final class LWJGL3Clear implements JCGLClearType
{
  LWJGL3Clear(
    final LWJGL3Context in_context)
  {
    NullCheck.notNull(in_context, "Context");
  }

  @Override
  public void clear(final JCGLClearSpecificationType c)
    throws JCGLException
  {
    NullCheck.notNull(c, "Clear");

    int buffers = 0;
    final Optional<Vector4D> opt_color = c.colorBufferClear();
    final OptionalDouble opt_depth = c.depthBufferClear();
    final OptionalInt opt_stencil = c.stencilBufferClear();

    if (opt_color.isPresent()) {
      buffers |= GL11.GL_COLOR_BUFFER_BIT;
      final Vector4D cc = opt_color.get();
      GL11.glClearColor(
        (float) cc.x(),
        (float) cc.y(),
        (float) cc.z(),
        (float) cc.w());
    }

    if (opt_depth.isPresent()) {
      buffers |= GL11.GL_DEPTH_BUFFER_BIT;
      GL11.glClearDepth(opt_depth.getAsDouble());
    }

    if (opt_stencil.isPresent()) {
      buffers |= GL11.GL_STENCIL_BUFFER_BIT;
      GL11.glClearStencil(opt_stencil.getAsInt());
    }

    GL11.glClear(buffers);
  }
}
