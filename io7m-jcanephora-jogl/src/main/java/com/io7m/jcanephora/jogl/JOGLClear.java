/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

import javax.media.opengl.GL;

import com.io7m.jcanephora.ClearSpecification;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionNoDepthBuffer;
import com.io7m.jcanephora.JCGLExceptionNoStencilBuffer;
import com.io7m.jcanephora.api.JCGLClearType;
import com.io7m.jcanephora.api.JCGLDepthBufferType;
import com.io7m.jcanephora.api.JCGLStencilBufferType;
import com.io7m.jfunctional.OptionType;
import com.io7m.jfunctional.Some;
import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.VectorReadable4FType;

final class JOGLClear implements JCGLClearType
{
  private final GL                    gl;
  private final JCGLDepthBufferType   d;
  private final JCGLStencilBufferType s;

  JOGLClear(
    final GL in_gl,
    final JCGLDepthBufferType in_d,
    final JCGLStencilBufferType in_s)
  {
    this.gl = NullCheck.notNull(in_gl, "GL");
    this.d = NullCheck.notNull(in_d, "Depth buffers");
    this.s = NullCheck.notNull(in_s, "Stencil buffers");
  }

  @Override public void clear(
    final ClearSpecification spec)
    throws JCGLException
  {
    NullCheck.notNull(spec, "Specification");

    int buffers = 0;
    final OptionType<VectorReadable4FType> opt_color = spec.getColor();
    final OptionType<Float> opt_depth = spec.getDepth();
    final OptionType<Integer> opt_stencil = spec.getStencil();

    /**
     * Perform checks.
     */

    if (spec.isStrict()) {
      if (opt_depth.isSome()) {
        if (this.d.depthBufferGetBits() == 0) {
          throw new JCGLExceptionNoDepthBuffer("No depth buffer available");
        }
      }
      if (opt_stencil.isSome()) {
        if (this.s.stencilBufferGetBits() == 0) {
          throw new JCGLExceptionNoStencilBuffer(
            "No stencil buffer available");
        }
      }
    }

    /**
     * Set clear color, if necessary.
     */

    if (opt_color.isSome()) {
      buffers |= GL.GL_COLOR_BUFFER_BIT;

      final Some<VectorReadable4FType> some_color =
        (Some<VectorReadable4FType>) opt_color;
      final VectorReadable4FType c = some_color.get();
      this.gl.glClearColor(c.getXF(), c.getYF(), c.getZF(), c.getWF());
    }

    /**
     * Set depth, if necessary.
     */

    if (opt_depth.isSome()) {
      buffers |= GL.GL_DEPTH_BUFFER_BIT;

      final Some<Float> some_depth = (Some<Float>) opt_depth;
      final Float dv = some_depth.get();
      final float df = dv.floatValue();
      this.gl.glClearDepth(df);
    }

    /**
     * Set stencil, if necessary.
     */

    if (opt_stencil.isSome()) {
      buffers |= GL.GL_STENCIL_BUFFER_BIT;

      final Some<Integer> some_stencil = (Some<Integer>) opt_stencil;
      final Integer x = some_stencil.get();
      final int xi = x.intValue();
      this.gl.glClearStencil(xi);
    }

    this.gl.glClear(buffers);
  }
}
