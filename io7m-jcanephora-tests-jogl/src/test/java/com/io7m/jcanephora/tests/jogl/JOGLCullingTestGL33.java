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
import com.io7m.jcanephora.core.JCGLFaceSelection;
import com.io7m.jcanephora.core.JCGLFaceWindingOrder;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.core.api.JCGLCullingType;
import com.io7m.jcanephora.core.api.JCGLDepthBuffersType;
import com.io7m.jcanephora.core.api.JCGLInterfaceGL33Type;
import com.io7m.jcanephora.tests.contracts.JCGLCullingContract;
import com.jogamp.opengl.DebugGL3;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public final class JOGLCullingTestGL33 extends JCGLCullingContract
{
  @Override public void onTestCompleted()
  {
    JOGLTestContexts.closeAllContexts();
  }

  @Override protected JCGLCullingType getCulling(final String name)
  {
    final JCGLContextType c = JOGLTestContexts.newGL33Context(name, 24, 8);
    final JCGLInterfaceGL33Type cg = c.contextGetGL33();
    return cg.getCulling();
  }

  @Test
  public void testRedundantEnableDisable()
    throws Exception
  {
    final AtomicInteger culls = new AtomicInteger(0);
    final AtomicInteger unculls = new AtomicInteger(0);
    final AtomicInteger faces = new AtomicInteger(0);
    final AtomicInteger orders = new AtomicInteger(0);

    final JCGLContextType c =
      JOGLTestContexts.newGL33ContextWithSupplierAndErrors(
        "main", ctx -> {
          final GL3 base = ctx.getGL().getGL3();
          return new DebugGL3(base)
          {
            @Override
            public void glEnable(final int i)
            {
              super.glEnable(i);
              if (i == GL.GL_CULL_FACE) {
                culls.incrementAndGet();
              }
            }

            @Override
            public void glFrontFace(final int i)
            {
              super.glFrontFace(i);
              orders.incrementAndGet();
            }

            @Override
            public void glCullFace(final int i)
            {
              super.glCullFace(i);
              faces.incrementAndGet();
            }

            @Override
            public void glDisable(final int i)
            {
              super.glDisable(i);
              if (i == GL.GL_CULL_FACE) {
                unculls.incrementAndGet();
              }
            }
          };
        }, 24, 8);

    final JCGLInterfaceGL33Type g = c.contextGetGL33();
    final JCGLCullingType g_c = g.getCulling();

    Assert.assertEquals(0L, (long) culls.get());
    Assert.assertEquals(1L, (long) unculls.get());
    Assert.assertEquals(1L, (long) faces.get());
    Assert.assertEquals(1L, (long) orders.get());

    g_c.cullingDisable();

    Assert.assertEquals(0L, (long) culls.get());
    Assert.assertEquals(1L, (long) unculls.get());
    Assert.assertEquals(1L, (long) faces.get());
    Assert.assertEquals(1L, (long) orders.get());

    g_c.cullingDisable();

    Assert.assertEquals(0L, (long) culls.get());
    Assert.assertEquals(1L, (long) unculls.get());
    Assert.assertEquals(1L, (long) faces.get());
    Assert.assertEquals(1L, (long) orders.get());

    g_c.cullingEnable(
      JCGLFaceSelection.FACE_BACK,
      JCGLFaceWindingOrder.FRONT_FACE_COUNTER_CLOCKWISE);

    Assert.assertEquals(1L, (long) culls.get());
    Assert.assertEquals(1L, (long) unculls.get());
    Assert.assertEquals(1L, (long) faces.get());
    Assert.assertEquals(1L, (long) orders.get());

    g_c.cullingEnable(
      JCGLFaceSelection.FACE_BACK,
      JCGLFaceWindingOrder.FRONT_FACE_COUNTER_CLOCKWISE);

    Assert.assertEquals(1L, (long) culls.get());
    Assert.assertEquals(1L, (long) unculls.get());
    Assert.assertEquals(1L, (long) faces.get());
    Assert.assertEquals(1L, (long) orders.get());

    g_c.cullingEnable(
      JCGLFaceSelection.FACE_FRONT,
      JCGLFaceWindingOrder.FRONT_FACE_COUNTER_CLOCKWISE);

    Assert.assertEquals(1L, (long) culls.get());
    Assert.assertEquals(1L, (long) unculls.get());
    Assert.assertEquals(2L, (long) faces.get());
    Assert.assertEquals(1L, (long) orders.get());

    g_c.cullingEnable(
      JCGLFaceSelection.FACE_FRONT,
      JCGLFaceWindingOrder.FRONT_FACE_COUNTER_CLOCKWISE);

    Assert.assertEquals(1L, (long) culls.get());
    Assert.assertEquals(1L, (long) unculls.get());
    Assert.assertEquals(2L, (long) faces.get());
    Assert.assertEquals(1L, (long) orders.get());

    g_c.cullingEnable(
      JCGLFaceSelection.FACE_FRONT,
      JCGLFaceWindingOrder.FRONT_FACE_CLOCKWISE);

    Assert.assertEquals(1L, (long) culls.get());
    Assert.assertEquals(1L, (long) unculls.get());
    Assert.assertEquals(2L, (long) faces.get());
    Assert.assertEquals(2L, (long) orders.get());

    g_c.cullingDisable();

    Assert.assertEquals(1L, (long) culls.get());
    Assert.assertEquals(2L, (long) unculls.get());
    Assert.assertEquals(2L, (long) faces.get());
    Assert.assertEquals(2L, (long) orders.get());

    g_c.cullingEnable(
      JCGLFaceSelection.FACE_FRONT,
      JCGLFaceWindingOrder.FRONT_FACE_CLOCKWISE);

    Assert.assertEquals(2L, (long) culls.get());
    Assert.assertEquals(2L, (long) unculls.get());
    Assert.assertEquals(2L, (long) faces.get());
    Assert.assertEquals(2L, (long) orders.get());
  }
}
