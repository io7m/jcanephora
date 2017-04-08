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

package com.io7m.jcanephora.tests.jogl;

import com.io7m.jcanephora.core.JCGLBlendEquation;
import com.io7m.jcanephora.core.JCGLBlendFunction;
import com.io7m.jcanephora.core.api.JCGLBlendingType;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.core.api.JCGLInterfaceGL33Type;
import com.io7m.jcanephora.tests.contracts.JCGLBlendingContract;
import com.jogamp.opengl.DebugGL3;
import com.jogamp.opengl.GL3;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public final class JOGLBlendingTestGL33 extends JCGLBlendingContract
{
  @Override
  protected JCGLBlendingType getBlending(final String name)
  {
    final JCGLContextType c = JOGLTestContexts.newGL33Context(name, 24, 8);
    return c.contextGetGL33().blending();
  }

  @Override
  public void onTestCompleted()
  {
    JOGLTestContexts.closeAllContexts();
  }

  @Test
  public void testRedundantEnableDisable()
    throws Exception
  {
    final AtomicInteger blends = new AtomicInteger(0);
    final AtomicInteger unblends = new AtomicInteger(0);
    final AtomicInteger funcs = new AtomicInteger(0);
    final AtomicInteger eqs = new AtomicInteger(0);

    final JCGLContextType c =
      JOGLTestContexts.newGL33ContextWithSupplierAndErrors(
        "main", ctx -> {
          final GL3 base = ctx.getGL().getGL3();
          return new DebugGL3(base)
          {
            @Override
            public void glDisable(final int i)
            {
              super.glDisable(i);

              if (i == GL3.GL_BLEND) {
                unblends.incrementAndGet();
              }
            }

            @Override
            public void glBlendEquationSeparate(
              final int i,
              final int i1)
            {
              super.glBlendEquationSeparate(i, i1);
              eqs.incrementAndGet();
            }

            @Override
            public void glBlendFuncSeparate(
              final int i,
              final int i1,
              final int i2,
              final int i3)
            {
              super.glBlendFuncSeparate(i, i1, i2, i3);
              funcs.incrementAndGet();
            }

            @Override
            public void glEnable(final int i)
            {
              super.glEnable(i);

              if (i == GL3.GL_BLEND) {
                blends.incrementAndGet();
              }
            }
          };
        }, 24, 8);

    final JCGLInterfaceGL33Type g = c.contextGetGL33();
    final JCGLBlendingType g_b = g.blending();

    Assert.assertEquals(0L, (long) blends.get());
    Assert.assertEquals(0L, (long) unblends.get());
    Assert.assertEquals(0L, (long) eqs.get());
    Assert.assertEquals(0L, (long) funcs.get());

    g_b.blendingEnable(
      JCGLBlendFunction.BLEND_ONE, JCGLBlendFunction.BLEND_ONE);
    Assert.assertEquals(1L, (long) blends.get());
    Assert.assertEquals(0L, (long) unblends.get());
    Assert.assertEquals(1L, (long) eqs.get());
    Assert.assertEquals(1L, (long) funcs.get());

    g_b.blendingDisable();
    Assert.assertEquals(1L, (long) blends.get());
    Assert.assertEquals(1L, (long) unblends.get());
    Assert.assertEquals(1L, (long) eqs.get());
    Assert.assertEquals(1L, (long) funcs.get());

    g_b.blendingEnable(
      JCGLBlendFunction.BLEND_ONE, JCGLBlendFunction.BLEND_ONE);
    Assert.assertEquals(2L, (long) blends.get());
    Assert.assertEquals(1L, (long) unblends.get());
    Assert.assertEquals(1L, (long) eqs.get());
    Assert.assertEquals(1L, (long) funcs.get());

    g_b.blendingEnable(
      JCGLBlendFunction.BLEND_ONE, JCGLBlendFunction.BLEND_ONE);
    Assert.assertEquals(2L, (long) blends.get());
    Assert.assertEquals(1L, (long) unblends.get());
    Assert.assertEquals(1L, (long) eqs.get());
    Assert.assertEquals(1L, (long) funcs.get());

    g_b.blendingDisable();
    Assert.assertEquals(2L, (long) blends.get());
    Assert.assertEquals(2L, (long) unblends.get());
    Assert.assertEquals(1L, (long) eqs.get());
    Assert.assertEquals(1L, (long) funcs.get());

    g_b.blendingDisable();
    Assert.assertEquals(2L, (long) blends.get());
    Assert.assertEquals(2L, (long) unblends.get());
    Assert.assertEquals(1L, (long) eqs.get());
    Assert.assertEquals(1L, (long) funcs.get());

    g_b.blendingEnable(
      JCGLBlendFunction.BLEND_ONE, JCGLBlendFunction.BLEND_ONE);
    Assert.assertEquals(3L, (long) blends.get());
    Assert.assertEquals(2L, (long) unblends.get());
    Assert.assertEquals(1L, (long) eqs.get());
    Assert.assertEquals(1L, (long) funcs.get());

    g_b.blendingEnable(
      JCGLBlendFunction.BLEND_ONE_MINUS_SOURCE_ALPHA,
      JCGLBlendFunction.BLEND_ONE);
    Assert.assertEquals(3L, (long) blends.get());
    Assert.assertEquals(2L, (long) unblends.get());
    Assert.assertEquals(1L, (long) eqs.get());
    Assert.assertEquals(2L, (long) funcs.get());

    g_b.blendingEnable(
      JCGLBlendFunction.BLEND_ONE_MINUS_SOURCE_ALPHA,
      JCGLBlendFunction.BLEND_ONE_MINUS_DESTINATION_COLOR);
    Assert.assertEquals(3L, (long) blends.get());
    Assert.assertEquals(2L, (long) unblends.get());
    Assert.assertEquals(1L, (long) eqs.get());
    Assert.assertEquals(3L, (long) funcs.get());

    g_b.blendingEnableWithEquation(
      JCGLBlendFunction.BLEND_ONE_MINUS_SOURCE_ALPHA,
      JCGLBlendFunction.BLEND_ONE_MINUS_DESTINATION_COLOR,
      JCGLBlendEquation.BLEND_EQUATION_ADD);
    Assert.assertEquals(3L, (long) blends.get());
    Assert.assertEquals(2L, (long) unblends.get());
    Assert.assertEquals(1L, (long) eqs.get());
    Assert.assertEquals(3L, (long) funcs.get());

    g_b.blendingEnableWithEquation(
      JCGLBlendFunction.BLEND_ONE_MINUS_SOURCE_ALPHA,
      JCGLBlendFunction.BLEND_ONE_MINUS_DESTINATION_COLOR,
      JCGLBlendEquation.BLEND_EQUATION_MAXIMUM);
    Assert.assertEquals(3L, (long) blends.get());
    Assert.assertEquals(2L, (long) unblends.get());
    Assert.assertEquals(2L, (long) eqs.get());
    Assert.assertEquals(3L, (long) funcs.get());
  }
}
