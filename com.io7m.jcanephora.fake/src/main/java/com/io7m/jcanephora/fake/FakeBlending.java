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

package com.io7m.jcanephora.fake;

import com.io7m.jcanephora.core.JCGLBlendEquation;
import com.io7m.jcanephora.core.JCGLBlendFunction;
import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLExceptionBlendingMisconfigured;
import com.io7m.jcanephora.core.api.JCGLBlendingType;
import com.io7m.jnull.NullCheck;

final class FakeBlending implements JCGLBlendingType
{
  private boolean blend;

  FakeBlending(final FakeContext c)
  {
    NullCheck.notNull(c, "Context");
    this.blend = false;
  }

  @Override
  public void blendingDisable()
    throws JCGLException
  {
    this.blend = false;
  }

  @Override
  public boolean blendingIsEnabled()
    throws JCGLException
  {
    return this.blend;
  }

  @Override
  public void blendingEnableSeparateWithEquationSeparate(
    final JCGLBlendFunction source_rgb_factor,
    final JCGLBlendFunction source_alpha_factor,
    final JCGLBlendFunction destination_rgb_factor,
    final JCGLBlendFunction destination_alpha_factor,
    final JCGLBlendEquation equation_rgb,
    final JCGLBlendEquation equation_alpha)
    throws JCGLException, JCGLExceptionBlendingMisconfigured
  {
    NullCheck.notNull(source_rgb_factor, "Source RGB factor");
    NullCheck.notNull(source_alpha_factor, "Source alpha factor");
    NullCheck.notNull(destination_rgb_factor, "Destination RGB factor");
    NullCheck.notNull(destination_alpha_factor, "Destination alpha factor");
    NullCheck.notNull(equation_rgb, "Equation RGB");
    NullCheck.notNull(equation_alpha, "Equation alpha");

    if (destination_rgb_factor
      == JCGLBlendFunction.BLEND_SOURCE_ALPHA_SATURATE) {
      throw new JCGLExceptionBlendingMisconfigured(
        "Destination RGB factor not SOURCE_ALPHA_SATURATE");
    }
    if (destination_alpha_factor
      == JCGLBlendFunction.BLEND_SOURCE_ALPHA_SATURATE) {
      throw new JCGLExceptionBlendingMisconfigured(
        "Destination alpha factor not SOURCE_ALPHA_SATURATE");
    }

    this.blend = true;
  }
}
