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

package com.io7m.jcanephora.fake;

import com.io7m.jcanephora.ClearSpecification;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionNoDepthBuffer;
import com.io7m.jcanephora.JCGLExceptionNoStencilBuffer;
import com.io7m.jcanephora.api.JCGLClearType;
import com.io7m.jcanephora.api.JCGLDepthBufferType;
import com.io7m.jcanephora.api.JCGLStencilBufferType;
import com.io7m.jfunctional.OptionType;
import com.io7m.jnull.NullCheck;

final class FakeClear implements JCGLClearType
{
  private final JCGLDepthBufferType   d;
  private final JCGLStencilBufferType s;

  FakeClear(
    final JCGLDepthBufferType in_d,
    final JCGLStencilBufferType in_s)
  {
    this.d = NullCheck.notNull(in_d, "Depth buffers");
    this.s = NullCheck.notNull(in_s, "Stencil buffers");
  }

  @Override public void clear(
    final ClearSpecification spec)
    throws JCGLException
  {
    NullCheck.notNull(spec, "Specification");

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
  }
}
