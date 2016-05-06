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
import com.io7m.jcanephora.core.JCGLStencilFunction;
import com.io7m.jcanephora.core.JCGLStencilOperation;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.core.api.JCGLDepthBuffersType;
import com.io7m.jcanephora.core.api.JCGLInterfaceGL33Type;
import com.io7m.jcanephora.core.api.JCGLStencilBuffersType;
import com.io7m.jcanephora.tests.contracts.JCGLStencilBuffersContract;
import com.jogamp.opengl.DebugGL3;
import com.jogamp.opengl.GL3;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public final class JOGLStencilBuffersTestGL33 extends JCGLStencilBuffersContract
{
  @Override public void onTestCompleted()
  {
    JOGLTestContexts.closeAllContexts();
  }

  @Override protected Interfaces getInterfaces(
    final String name,
    final int depth,
    final int stencil)
  {
    final JCGLContextType c =
      JOGLTestContexts.newGL33Context(name, depth, stencil);
    final JCGLInterfaceGL33Type cg = c.contextGetGL33();
    return new Interfaces(
      c,
      cg.getFramebuffers(),
      cg.getTextures(),
      cg.getStencilBuffers());
  }

  @Test
  public void testRedundantFunc()
    throws Exception
  {
    final AtomicInteger funcs = new AtomicInteger(0);

    final JCGLContextType c =
      JOGLTestContexts.newGL33ContextWithSupplierAndErrors(
        "main", ctx -> {
          final GL3 base = ctx.getGL().getGL3();
          return new DebugGL3(base)
          {
            @Override
            public void glStencilFuncSeparate(
              final int i,
              final int i1,
              final int i2,
              final int i3)
            {
              super.glStencilFuncSeparate(i, i1, i2, i3);
              funcs.incrementAndGet();
            }
          };
        }, 24, 8);

    final JCGLInterfaceGL33Type g = c.contextGetGL33();
    final JCGLStencilBuffersType g_s = g.getStencilBuffers();

    Assert.assertEquals(1L, (long) funcs.get());

    g_s.stencilBufferFunction(
      JCGLFaceSelection.FACE_FRONT,
      JCGLStencilFunction.STENCIL_ALWAYS,
      0,
      0);
    Assert.assertEquals(2L, (long) funcs.get());

    g_s.stencilBufferFunction(
      JCGLFaceSelection.FACE_FRONT,
      JCGLStencilFunction.STENCIL_ALWAYS,
      0,
      0);
    Assert.assertEquals(2L, (long) funcs.get());

    g_s.stencilBufferFunction(
      JCGLFaceSelection.FACE_BACK,
      JCGLStencilFunction.STENCIL_ALWAYS,
      0,
      0);
    Assert.assertEquals(3L, (long) funcs.get());

    g_s.stencilBufferFunction(
      JCGLFaceSelection.FACE_BACK,
      JCGLStencilFunction.STENCIL_ALWAYS,
      0,
      0);
    Assert.assertEquals(3L, (long) funcs.get());

    g_s.stencilBufferFunction(
      JCGLFaceSelection.FACE_BACK,
      JCGLStencilFunction.STENCIL_ALWAYS,
      1,
      0);
    Assert.assertEquals(4L, (long) funcs.get());

    g_s.stencilBufferFunction(
      JCGLFaceSelection.FACE_BACK,
      JCGLStencilFunction.STENCIL_ALWAYS,
      1,
      0);
    Assert.assertEquals(4L, (long) funcs.get());
  }

  @Test
  public void testRedundantTest()
    throws Exception
  {
    final AtomicInteger tests = new AtomicInteger(0);
    final AtomicInteger untests = new AtomicInteger(0);

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

              if (i == GL3.GL_STENCIL_TEST) {
                untests.incrementAndGet();
              }
            }

            @Override
            public void glEnable(final int i)
            {
              super.glEnable(i);

              if (i == GL3.GL_STENCIL_TEST) {
                tests.incrementAndGet();
              }
            }
          };
        }, 24, 8);

    final JCGLInterfaceGL33Type g = c.contextGetGL33();
    final JCGLStencilBuffersType g_s = g.getStencilBuffers();

    Assert.assertEquals(0L, (long) tests.get());
    Assert.assertEquals(1L, (long) untests.get());

    g_s.stencilBufferEnable();
    Assert.assertEquals(1L, (long) tests.get());
    Assert.assertEquals(1L, (long) untests.get());

    g_s.stencilBufferEnable();
    Assert.assertEquals(1L, (long) tests.get());
    Assert.assertEquals(1L, (long) untests.get());

    g_s.stencilBufferDisable();
    Assert.assertEquals(1L, (long) tests.get());
    Assert.assertEquals(2L, (long) untests.get());

    g_s.stencilBufferDisable();
    Assert.assertEquals(1L, (long) tests.get());
    Assert.assertEquals(2L, (long) untests.get());
  }

  @Test
  public void testRedundantMask()
    throws Exception
  {
    final AtomicInteger masks = new AtomicInteger(0);

    final JCGLContextType c =
      JOGLTestContexts.newGL33ContextWithSupplierAndErrors(
        "main", ctx -> {
          final GL3 base = ctx.getGL().getGL3();
          return new DebugGL3(base)
          {
            @Override
            public void glStencilMaskSeparate(
              final int i,
              final int i1)
            {
              super.glStencilMaskSeparate(i, i1);
              masks.incrementAndGet();
            }
          };
        }, 24, 8);

    final JCGLInterfaceGL33Type g = c.contextGetGL33();
    final JCGLStencilBuffersType g_s = g.getStencilBuffers();

    Assert.assertEquals(1L, (long) masks.get());

    g_s.stencilBufferMask(JCGLFaceSelection.FACE_FRONT, 0);
    Assert.assertEquals(2L, (long) masks.get());

    g_s.stencilBufferMask(JCGLFaceSelection.FACE_FRONT, 0);
    Assert.assertEquals(2L, (long) masks.get());

    g_s.stencilBufferMask(JCGLFaceSelection.FACE_FRONT, 1);
    Assert.assertEquals(3L, (long) masks.get());

    g_s.stencilBufferMask(JCGLFaceSelection.FACE_FRONT, 1);
    Assert.assertEquals(3L, (long) masks.get());

    g_s.stencilBufferMask(JCGLFaceSelection.FACE_BACK, 1);
    Assert.assertEquals(4L, (long) masks.get());

    g_s.stencilBufferMask(JCGLFaceSelection.FACE_BACK, 1);
    Assert.assertEquals(4L, (long) masks.get());

    g_s.stencilBufferMask(JCGLFaceSelection.FACE_FRONT_AND_BACK, 1);
    Assert.assertEquals(4L, (long) masks.get());

    g_s.stencilBufferMask(JCGLFaceSelection.FACE_FRONT_AND_BACK, 1);
    Assert.assertEquals(4L, (long) masks.get());

    g_s.stencilBufferMask(JCGLFaceSelection.FACE_FRONT_AND_BACK, 2);
    Assert.assertEquals(5L, (long) masks.get());

    g_s.stencilBufferMask(JCGLFaceSelection.FACE_FRONT_AND_BACK, 2);
    Assert.assertEquals(5L, (long) masks.get());

    g_s.stencilBufferMask(JCGLFaceSelection.FACE_FRONT, 2);
    Assert.assertEquals(5L, (long) masks.get());

    g_s.stencilBufferMask(JCGLFaceSelection.FACE_BACK, 2);
    Assert.assertEquals(5L, (long) masks.get());
  }

  @Test
  public void testRedundantOpsFront()
    throws Exception
  {
    final AtomicInteger ops = new AtomicInteger(0);

    final JCGLContextType c =
      JOGLTestContexts.newGL33ContextWithSupplierAndErrors(
        "main", ctx -> {
          final GL3 base = ctx.getGL().getGL3();
          return new DebugGL3(base)
          {
            @Override
            public void glStencilOpSeparate(
              final int i,
              final int i1,
              final int i2,
              final int i3)
            {
              super.glStencilOpSeparate(i, i1, i2, i3);
              ops.incrementAndGet();
            }
          };
        }, 24, 8);

    final JCGLInterfaceGL33Type g = c.contextGetGL33();
    final JCGLStencilBuffersType g_s = g.getStencilBuffers();

    g_s.stencilBufferOperation(
      JCGLFaceSelection.FACE_FRONT,
      JCGLStencilOperation.STENCIL_OP_KEEP,
      JCGLStencilOperation.STENCIL_OP_KEEP,
      JCGLStencilOperation.STENCIL_OP_KEEP);

    Assert.assertEquals(1L, (long) ops.get());

    g_s.stencilBufferOperation(
      JCGLFaceSelection.FACE_FRONT,
      JCGLStencilOperation.STENCIL_OP_KEEP,
      JCGLStencilOperation.STENCIL_OP_KEEP,
      JCGLStencilOperation.STENCIL_OP_KEEP);

    Assert.assertEquals(1L, (long) ops.get());

    g_s.stencilBufferOperation(
      JCGLFaceSelection.FACE_FRONT,
      JCGLStencilOperation.STENCIL_OP_DECREMENT,
      JCGLStencilOperation.STENCIL_OP_KEEP,
      JCGLStencilOperation.STENCIL_OP_KEEP);

    Assert.assertEquals(2L, (long) ops.get());

    g_s.stencilBufferOperation(
      JCGLFaceSelection.FACE_FRONT,
      JCGLStencilOperation.STENCIL_OP_DECREMENT,
      JCGLStencilOperation.STENCIL_OP_KEEP,
      JCGLStencilOperation.STENCIL_OP_KEEP);

    Assert.assertEquals(2L, (long) ops.get());

    g_s.stencilBufferOperation(
      JCGLFaceSelection.FACE_FRONT,
      JCGLStencilOperation.STENCIL_OP_DECREMENT,
      JCGLStencilOperation.STENCIL_OP_DECREMENT_WRAP,
      JCGLStencilOperation.STENCIL_OP_KEEP);

    Assert.assertEquals(3L, (long) ops.get());

    g_s.stencilBufferOperation(
      JCGLFaceSelection.FACE_FRONT,
      JCGLStencilOperation.STENCIL_OP_DECREMENT,
      JCGLStencilOperation.STENCIL_OP_DECREMENT_WRAP,
      JCGLStencilOperation.STENCIL_OP_KEEP);

    Assert.assertEquals(3L, (long) ops.get());

    g_s.stencilBufferOperation(
      JCGLFaceSelection.FACE_FRONT,
      JCGLStencilOperation.STENCIL_OP_DECREMENT,
      JCGLStencilOperation.STENCIL_OP_DECREMENT_WRAP,
      JCGLStencilOperation.STENCIL_OP_INCREMENT);

    Assert.assertEquals(4L, (long) ops.get());

    g_s.stencilBufferOperation(
      JCGLFaceSelection.FACE_FRONT,
      JCGLStencilOperation.STENCIL_OP_DECREMENT,
      JCGLStencilOperation.STENCIL_OP_DECREMENT_WRAP,
      JCGLStencilOperation.STENCIL_OP_INCREMENT);

    Assert.assertEquals(4L, (long) ops.get());
  }

  @Test
  public void testRedundantOpsBack()
    throws Exception
  {
    final AtomicInteger ops = new AtomicInteger(0);

    final JCGLContextType c =
      JOGLTestContexts.newGL33ContextWithSupplierAndErrors(
        "main", ctx -> {
          final GL3 base = ctx.getGL().getGL3();
          return new DebugGL3(base)
          {
            @Override
            public void glStencilOpSeparate(
              final int i,
              final int i1,
              final int i2,
              final int i3)
            {
              super.glStencilOpSeparate(i, i1, i2, i3);
              ops.incrementAndGet();
            }
          };
        }, 24, 8);

    final JCGLInterfaceGL33Type g = c.contextGetGL33();
    final JCGLStencilBuffersType g_s = g.getStencilBuffers();

    g_s.stencilBufferOperation(
      JCGLFaceSelection.FACE_BACK,
      JCGLStencilOperation.STENCIL_OP_KEEP,
      JCGLStencilOperation.STENCIL_OP_KEEP,
      JCGLStencilOperation.STENCIL_OP_KEEP);

    Assert.assertEquals(1L, (long) ops.get());

    g_s.stencilBufferOperation(
      JCGLFaceSelection.FACE_BACK,
      JCGLStencilOperation.STENCIL_OP_KEEP,
      JCGLStencilOperation.STENCIL_OP_KEEP,
      JCGLStencilOperation.STENCIL_OP_KEEP);

    Assert.assertEquals(1L, (long) ops.get());

    g_s.stencilBufferOperation(
      JCGLFaceSelection.FACE_BACK,
      JCGLStencilOperation.STENCIL_OP_DECREMENT,
      JCGLStencilOperation.STENCIL_OP_KEEP,
      JCGLStencilOperation.STENCIL_OP_KEEP);

    Assert.assertEquals(2L, (long) ops.get());

    g_s.stencilBufferOperation(
      JCGLFaceSelection.FACE_BACK,
      JCGLStencilOperation.STENCIL_OP_DECREMENT,
      JCGLStencilOperation.STENCIL_OP_KEEP,
      JCGLStencilOperation.STENCIL_OP_KEEP);

    Assert.assertEquals(2L, (long) ops.get());

    g_s.stencilBufferOperation(
      JCGLFaceSelection.FACE_BACK,
      JCGLStencilOperation.STENCIL_OP_DECREMENT,
      JCGLStencilOperation.STENCIL_OP_DECREMENT_WRAP,
      JCGLStencilOperation.STENCIL_OP_KEEP);

    Assert.assertEquals(3L, (long) ops.get());

    g_s.stencilBufferOperation(
      JCGLFaceSelection.FACE_BACK,
      JCGLStencilOperation.STENCIL_OP_DECREMENT,
      JCGLStencilOperation.STENCIL_OP_DECREMENT_WRAP,
      JCGLStencilOperation.STENCIL_OP_KEEP);

    Assert.assertEquals(3L, (long) ops.get());

    g_s.stencilBufferOperation(
      JCGLFaceSelection.FACE_BACK,
      JCGLStencilOperation.STENCIL_OP_DECREMENT,
      JCGLStencilOperation.STENCIL_OP_DECREMENT_WRAP,
      JCGLStencilOperation.STENCIL_OP_INCREMENT);

    Assert.assertEquals(4L, (long) ops.get());

    g_s.stencilBufferOperation(
      JCGLFaceSelection.FACE_BACK,
      JCGLStencilOperation.STENCIL_OP_DECREMENT,
      JCGLStencilOperation.STENCIL_OP_DECREMENT_WRAP,
      JCGLStencilOperation.STENCIL_OP_INCREMENT);

    Assert.assertEquals(4L, (long) ops.get());
  }

  @Test
  public void testRedundantOpsFrontBack()
    throws Exception
  {
    final AtomicInteger ops = new AtomicInteger(0);

    final JCGLContextType c =
      JOGLTestContexts.newGL33ContextWithSupplierAndErrors(
        "main", ctx -> {
          final GL3 base = ctx.getGL().getGL3();
          return new DebugGL3(base)
          {
            @Override
            public void glStencilOpSeparate(
              final int i,
              final int i1,
              final int i2,
              final int i3)
            {
              super.glStencilOpSeparate(i, i1, i2, i3);
              ops.incrementAndGet();
            }
          };
        }, 24, 8);

    final JCGLInterfaceGL33Type g = c.contextGetGL33();
    final JCGLStencilBuffersType g_s = g.getStencilBuffers();

    g_s.stencilBufferOperation(
      JCGLFaceSelection.FACE_FRONT_AND_BACK,
      JCGLStencilOperation.STENCIL_OP_KEEP,
      JCGLStencilOperation.STENCIL_OP_KEEP,
      JCGLStencilOperation.STENCIL_OP_KEEP);
    Assert.assertEquals(1L, (long) ops.get());

    g_s.stencilBufferOperation(
      JCGLFaceSelection.FACE_BACK,
      JCGLStencilOperation.STENCIL_OP_KEEP,
      JCGLStencilOperation.STENCIL_OP_KEEP,
      JCGLStencilOperation.STENCIL_OP_KEEP);
    Assert.assertEquals(1L, (long) ops.get());

    g_s.stencilBufferOperation(
      JCGLFaceSelection.FACE_FRONT,
      JCGLStencilOperation.STENCIL_OP_KEEP,
      JCGLStencilOperation.STENCIL_OP_KEEP,
      JCGLStencilOperation.STENCIL_OP_KEEP);
    Assert.assertEquals(1L, (long) ops.get());

    g_s.stencilBufferOperation(
      JCGLFaceSelection.FACE_BACK,
      JCGLStencilOperation.STENCIL_OP_DECREMENT,
      JCGLStencilOperation.STENCIL_OP_KEEP,
      JCGLStencilOperation.STENCIL_OP_KEEP);
    Assert.assertEquals(2L, (long) ops.get());

    g_s.stencilBufferOperation(
      JCGLFaceSelection.FACE_FRONT,
      JCGLStencilOperation.STENCIL_OP_DECREMENT_WRAP,
      JCGLStencilOperation.STENCIL_OP_KEEP,
      JCGLStencilOperation.STENCIL_OP_KEEP);
    Assert.assertEquals(3L, (long) ops.get());

    g_s.stencilBufferOperation(
      JCGLFaceSelection.FACE_FRONT_AND_BACK,
      JCGLStencilOperation.STENCIL_OP_KEEP,
      JCGLStencilOperation.STENCIL_OP_KEEP,
      JCGLStencilOperation.STENCIL_OP_KEEP);
    Assert.assertEquals(4L, (long) ops.get());
  }
}
