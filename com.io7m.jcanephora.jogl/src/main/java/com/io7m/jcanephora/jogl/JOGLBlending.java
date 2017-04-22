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

import com.io7m.jcanephora.core.JCGLBlendEquation;
import com.io7m.jcanephora.core.JCGLBlendFunction;
import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLExceptionBlendingMisconfigured;
import com.io7m.jcanephora.core.api.JCGLBlendingType;
import com.io7m.jnull.NullCheck;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class JOGLBlending implements JCGLBlendingType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(JOGLBlending.class);
  }

  private final GL3 gl;
  private boolean blend;
  private JCGLBlendEquation equation_rgb;
  private JCGLBlendEquation equation_alpha;
  private JCGLBlendFunction source_rgb_factor;
  private JCGLBlendFunction source_alpha_factor;
  private JCGLBlendFunction destination_rgb_factor;
  private JCGLBlendFunction destination_alpha_factor;

  JOGLBlending(final JOGLContext c)
  {
    final JOGLContext context = NullCheck.notNull(c, "Context");
    this.gl = context.getGL3();
    this.blend = this.gl.glIsEnabled(GL.GL_BLEND);
    JOGLErrorChecking.checkErrors(this.gl);
  }

  @Override
  public void blendingDisable()
    throws JCGLException
  {
    if (this.blend) {
      this.gl.glDisable(GL.GL_BLEND);
      this.blend = false;
    } else {
      LOG.trace("redundant blend disable ignored");
    }
  }

  @Override
  public boolean blendingIsEnabled()
    throws JCGLException
  {
    return this.blend;
  }

  @Override
  public void blendingEnableSeparateWithEquationSeparate(
    final JCGLBlendFunction in_source_rgb_factor,
    final JCGLBlendFunction in_source_alpha_factor,
    final JCGLBlendFunction in_destination_rgb_factor,
    final JCGLBlendFunction in_destination_alpha_factor,
    final JCGLBlendEquation in_equation_rgb,
    final JCGLBlendEquation in_equation_alpha)
    throws JCGLException, JCGLExceptionBlendingMisconfigured
  {
    NullCheck.notNull(in_source_rgb_factor, "Source RGB factor");
    NullCheck.notNull(in_source_alpha_factor, "Source alpha factor");
    NullCheck.notNull(in_destination_rgb_factor, "Destination RGB factor");
    NullCheck.notNull(in_destination_alpha_factor, "Destination alpha factor");
    NullCheck.notNull(in_equation_rgb, "Equation RGB");
    NullCheck.notNull(in_equation_alpha, "Equation alpha");

    if (in_destination_rgb_factor
      == JCGLBlendFunction.BLEND_SOURCE_ALPHA_SATURATE) {
      throw new JCGLExceptionBlendingMisconfigured(
        "Destination RGB factor not SOURCE_ALPHA_SATURATE");
    }
    if (in_destination_alpha_factor
      == JCGLBlendFunction.BLEND_SOURCE_ALPHA_SATURATE) {
      throw new JCGLExceptionBlendingMisconfigured(
        "Destination alpha factor not SOURCE_ALPHA_SATURATE");
    }

    if (!this.blend) {
      this.gl.glEnable(GL.GL_BLEND);
      this.blend = true;
    } else {
      LOG.trace("redundant blend enable ignored");
    }

    if (this.equation_rgb != in_equation_rgb
      || this.equation_alpha != in_equation_alpha) {
      this.gl.glBlendEquationSeparate(
        JOGLTypeConversions.blendEquationToGL(in_equation_rgb),
        JOGLTypeConversions.blendEquationToGL(in_equation_alpha));
      this.equation_rgb = in_equation_rgb;
      this.equation_alpha = in_equation_alpha;
    } else {
      LOG.trace("redundant blend equation change ignored");
    }

    if (this.source_rgb_factor != in_source_rgb_factor
      || this.source_alpha_factor != in_source_alpha_factor
      || this.destination_rgb_factor != in_destination_rgb_factor
      || this.destination_alpha_factor != in_destination_alpha_factor) {
      this.gl.glBlendFuncSeparate(
        JOGLTypeConversions.blendFunctionToGL(in_source_rgb_factor),
        JOGLTypeConversions.blendFunctionToGL(in_destination_rgb_factor),
        JOGLTypeConversions.blendFunctionToGL(in_source_alpha_factor),
        JOGLTypeConversions.blendFunctionToGL(in_destination_alpha_factor));
      this.source_rgb_factor = in_source_rgb_factor;
      this.source_alpha_factor = in_source_alpha_factor;
      this.destination_rgb_factor = in_destination_rgb_factor;
      this.destination_alpha_factor = in_destination_alpha_factor;
    } else {
      LOG.trace("redundant blend function change ignored");
    }
  }
}
