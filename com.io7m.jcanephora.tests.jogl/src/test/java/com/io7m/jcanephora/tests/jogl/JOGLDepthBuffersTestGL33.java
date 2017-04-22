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

import com.io7m.jcanephora.core.JCGLDepthFunction;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.core.api.JCGLDepthBuffersType;
import com.io7m.jcanephora.core.api.JCGLInterfaceGL33Type;
import com.io7m.jcanephora.tests.contracts.JCGLDepthBuffersContract;
import com.jogamp.opengl.DebugGL3;
import com.jogamp.opengl.GL3;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public final class JOGLDepthBuffersTestGL33 extends JCGLDepthBuffersContract
{
  @Override
  public void onTestCompleted()
  {
    JOGLTestContexts.closeAllContexts();
  }

  @Override
  protected Interfaces getInterfaces(
    final String name,
    final int depth,
    final int stencil)
  {
    final JCGLContextType c =
      JOGLTestContexts.newGL33Context(name, depth, stencil);
    final JCGLInterfaceGL33Type cg = c.contextGetGL33();
    return new Interfaces(
      c,
      cg.framebuffers(),
      cg.textures(),
      cg.depthBuffers());
  }

  @Test
  public void testRedundantEnableDisable()
    throws Exception
  {
    final AtomicInteger tests = new AtomicInteger(0);
    final AtomicInteger untests = new AtomicInteger(0);
    final AtomicInteger clamps = new AtomicInteger(0);
    final AtomicInteger unclamps = new AtomicInteger(0);
    final AtomicInteger writes = new AtomicInteger(0);
    final AtomicInteger unwrites = new AtomicInteger(0);
    final AtomicInteger funcs = new AtomicInteger(0);

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

              if (i == GL3.GL_DEPTH_TEST) {
                untests.incrementAndGet();
              }
              if (i == GL3.GL_DEPTH_CLAMP) {
                unclamps.incrementAndGet();
              }
            }

            @Override
            public void glDepthFunc(final int i)
            {
              super.glDepthFunc(i);
              funcs.incrementAndGet();
            }

            @Override
            public void glDepthMask(final boolean b)
            {
              super.glDepthMask(b);

              if (b) {
                writes.incrementAndGet();
              } else {
                unwrites.incrementAndGet();
              }
            }

            @Override
            public void glEnable(final int i)
            {
              super.glEnable(i);

              if (i == GL3.GL_DEPTH_TEST) {
                tests.incrementAndGet();
              }
              if (i == GL3.GL_DEPTH_CLAMP) {
                clamps.incrementAndGet();
              }
            }
          };
        }, 24, 8);

    final JCGLInterfaceGL33Type g = c.contextGetGL33();
    final JCGLDepthBuffersType g_d = g.depthBuffers();

    Assert.assertEquals(0L, (long) tests.get());
    Assert.assertEquals(1L, (long) untests.get());
    Assert.assertEquals(1L, (long) writes.get());
    Assert.assertEquals(0L, (long) unwrites.get());
    Assert.assertEquals(0L, (long) clamps.get());
    Assert.assertEquals(1L, (long) unclamps.get());
    Assert.assertEquals(0L, (long) funcs.get());

    g_d.depthBufferTestDisable();

    Assert.assertEquals(0L, (long) tests.get());
    Assert.assertEquals(1L, (long) untests.get());
    Assert.assertEquals(1L, (long) writes.get());
    Assert.assertEquals(0L, (long) unwrites.get());
    Assert.assertEquals(0L, (long) clamps.get());
    Assert.assertEquals(1L, (long) unclamps.get());
    Assert.assertEquals(0L, (long) funcs.get());

    g_d.depthBufferWriteDisable();

    Assert.assertEquals(0L, (long) tests.get());
    Assert.assertEquals(1L, (long) untests.get());
    Assert.assertEquals(1L, (long) writes.get());
    Assert.assertEquals(1L, (long) unwrites.get());
    Assert.assertEquals(0L, (long) clamps.get());
    Assert.assertEquals(1L, (long) unclamps.get());
    Assert.assertEquals(0L, (long) funcs.get());

    g_d.depthBufferTestEnable(JCGLDepthFunction.DEPTH_ALWAYS);

    Assert.assertEquals(1L, (long) tests.get());
    Assert.assertEquals(1L, (long) untests.get());
    Assert.assertEquals(1L, (long) writes.get());
    Assert.assertEquals(1L, (long) unwrites.get());
    Assert.assertEquals(0L, (long) clamps.get());
    Assert.assertEquals(1L, (long) unclamps.get());
    Assert.assertEquals(1L, (long) funcs.get());

    g_d.depthBufferTestEnable(JCGLDepthFunction.DEPTH_GREATER_THAN);

    Assert.assertEquals(1L, (long) tests.get());
    Assert.assertEquals(1L, (long) untests.get());
    Assert.assertEquals(1L, (long) writes.get());
    Assert.assertEquals(1L, (long) unwrites.get());
    Assert.assertEquals(0L, (long) clamps.get());
    Assert.assertEquals(1L, (long) unclamps.get());
    Assert.assertEquals(2L, (long) funcs.get());

    g_d.depthBufferTestEnable(JCGLDepthFunction.DEPTH_GREATER_THAN);

    Assert.assertEquals(1L, (long) tests.get());
    Assert.assertEquals(1L, (long) untests.get());
    Assert.assertEquals(1L, (long) writes.get());
    Assert.assertEquals(1L, (long) unwrites.get());
    Assert.assertEquals(0L, (long) clamps.get());
    Assert.assertEquals(1L, (long) unclamps.get());
    Assert.assertEquals(2L, (long) funcs.get());

    g_d.depthBufferWriteEnable();

    Assert.assertEquals(1L, (long) tests.get());
    Assert.assertEquals(1L, (long) untests.get());
    Assert.assertEquals(2L, (long) writes.get());
    Assert.assertEquals(1L, (long) unwrites.get());
    Assert.assertEquals(0L, (long) clamps.get());
    Assert.assertEquals(1L, (long) unclamps.get());
    Assert.assertEquals(2L, (long) funcs.get());

    g_d.depthBufferWriteDisable();

    Assert.assertEquals(1L, (long) tests.get());
    Assert.assertEquals(1L, (long) untests.get());
    Assert.assertEquals(2L, (long) writes.get());
    Assert.assertEquals(2L, (long) unwrites.get());
    Assert.assertEquals(0L, (long) clamps.get());
    Assert.assertEquals(1L, (long) unclamps.get());
    Assert.assertEquals(2L, (long) funcs.get());

    g_d.depthBufferWriteDisable();

    Assert.assertEquals(1L, (long) tests.get());
    Assert.assertEquals(1L, (long) untests.get());
    Assert.assertEquals(2L, (long) writes.get());
    Assert.assertEquals(2L, (long) unwrites.get());
    Assert.assertEquals(0L, (long) clamps.get());
    Assert.assertEquals(1L, (long) unclamps.get());
    Assert.assertEquals(2L, (long) funcs.get());

    g_d.depthClampingEnable();

    Assert.assertEquals(1L, (long) tests.get());
    Assert.assertEquals(1L, (long) untests.get());
    Assert.assertEquals(2L, (long) writes.get());
    Assert.assertEquals(2L, (long) unwrites.get());
    Assert.assertEquals(1L, (long) clamps.get());
    Assert.assertEquals(1L, (long) unclamps.get());
    Assert.assertEquals(2L, (long) funcs.get());

    g_d.depthClampingEnable();

    Assert.assertEquals(1L, (long) tests.get());
    Assert.assertEquals(1L, (long) untests.get());
    Assert.assertEquals(2L, (long) writes.get());
    Assert.assertEquals(2L, (long) unwrites.get());
    Assert.assertEquals(1L, (long) clamps.get());
    Assert.assertEquals(1L, (long) unclamps.get());
    Assert.assertEquals(2L, (long) funcs.get());

    g_d.depthClampingDisable();

    Assert.assertEquals(1L, (long) tests.get());
    Assert.assertEquals(1L, (long) untests.get());
    Assert.assertEquals(2L, (long) writes.get());
    Assert.assertEquals(2L, (long) unwrites.get());
    Assert.assertEquals(1L, (long) clamps.get());
    Assert.assertEquals(2L, (long) unclamps.get());
    Assert.assertEquals(2L, (long) funcs.get());

    g_d.depthClampingDisable();

    Assert.assertEquals(1L, (long) tests.get());
    Assert.assertEquals(1L, (long) untests.get());
    Assert.assertEquals(2L, (long) writes.get());
    Assert.assertEquals(2L, (long) unwrites.get());
    Assert.assertEquals(1L, (long) clamps.get());
    Assert.assertEquals(2L, (long) unclamps.get());
    Assert.assertEquals(2L, (long) funcs.get());
  }
}
