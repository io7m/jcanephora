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

import com.io7m.jcanephora.core.JCGLExceptionNonCompliant;
import com.io7m.jcanephora.core.JCGLFramebufferBuilderType;
import com.io7m.jcanephora.core.JCGLFramebufferColorAttachmentPointType;
import com.io7m.jcanephora.core.JCGLFramebufferDrawBufferType;
import com.io7m.jcanephora.core.JCGLFramebufferType;
import com.io7m.jcanephora.core.JCGLTexture2DType;
import com.io7m.jcanephora.core.JCGLTextureFilterMagnification;
import com.io7m.jcanephora.core.JCGLTextureFilterMinification;
import com.io7m.jcanephora.core.JCGLTextureFormat;
import com.io7m.jcanephora.core.JCGLTextureUnitType;
import com.io7m.jcanephora.core.JCGLTextureWrapS;
import com.io7m.jcanephora.core.JCGLTextureWrapT;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.core.api.JCGLFramebuffersType;
import com.io7m.jcanephora.core.api.JCGLInterfaceGL33Type;
import com.io7m.jcanephora.core.api.JCGLTexturesType;
import com.io7m.jcanephora.tests.contracts.JCGLFramebuffersContract;
import com.jogamp.opengl.DebugGL3;
import com.jogamp.opengl.GL3;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class JOGLFramebuffersTestGL33 extends JCGLFramebuffersContract
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(JOGLFramebuffersTestGL33.class);
  }

  @Test
  public void testNonCompliantColorAttachments()
    throws Exception
  {
    this.expected.expect(JCGLExceptionNonCompliant.class);
    JOGLTestContexts.newGL33ContextWithSupplierAndErrors(
      "main", c -> {
        final GL3 base = c.getGL().getGL3();
        return new DebugGL3(base)
        {
          @Override
          public void glGetIntegerv(
            final int name,
            final IntBuffer buffer)
          {
            if (name == GL3.GL_MAX_COLOR_ATTACHMENTS) {
              JOGLFramebuffersTestGL33.LOG.debug(
                "overriding request for {}",
                Integer.valueOf(GL3.GL_MAX_COLOR_ATTACHMENTS));
              buffer.put(0, 1);
            } else {
              super.glGetIntegerv(name, buffer);
            }
          }
        };
      }, 24, 8);
  }

  @Test
  public void testClampedDrawBuffers()
    throws Exception
  {
    final JCGLContextType gg =
      JOGLTestContexts.newGL33ContextWithSupplierAndErrors(
        "main", c -> {
          final GL3 base = c.getGL().getGL3();
          return new DebugGL3(base)
          {
            @Override
            public void glGetIntegerv(
              final int name,
              final IntBuffer buffer)
            {
              if (name == GL3.GL_MAX_DRAW_BUFFERS) {
                JOGLFramebuffersTestGL33.LOG.debug(
                  "overriding request for {}",
                  Integer.valueOf(GL3.GL_MAX_DRAW_BUFFERS));
                buffer.put(0, 4096);
              } else {
                super.glGetIntegerv(name, buffer);
              }
            }
          };
        }, 24, 8);

    final JCGLInterfaceGL33Type i = gg.contextGetGL33();
    final JCGLFramebuffersType gf = i.framebuffers();
    Assert.assertEquals(1024L, (long) gf.framebufferGetDrawBuffers().size());
  }

  @Test
  public void testClampedColorAttachments()
    throws Exception
  {
    final JCGLContextType gg =
      JOGLTestContexts.newGL33ContextWithSupplierAndErrors(
        "main", c -> {
          final GL3 base = c.getGL().getGL3();
          return new DebugGL3(base)
          {
            @Override
            public void glGetIntegerv(
              final int name,
              final IntBuffer buffer)
            {
              if (name == GL3.GL_MAX_COLOR_ATTACHMENTS) {
                JOGLFramebuffersTestGL33.LOG.debug(
                  "overriding request for {}",
                  Integer.valueOf(GL3.GL_MAX_COLOR_ATTACHMENTS));
                buffer.put(0, 4096);
              } else {
                super.glGetIntegerv(name, buffer);
              }
            }
          };
        }, 24, 8);

    final JCGLInterfaceGL33Type i = gg.contextGetGL33();
    final JCGLFramebuffersType gf = i.framebuffers();
    Assert.assertEquals(
      1024L, (long) gf.framebufferGetColorAttachments().size());
  }

  @Test
  public void testNonCompliantDrawBuffers()
    throws Exception
  {
    this.expected.expect(JCGLExceptionNonCompliant.class);
    JOGLTestContexts.newGL33ContextWithSupplierAndErrors(
      "main", c -> {
        final GL3 base = c.getGL().getGL3();
        return new DebugGL3(base)
        {
          @Override
          public void glGetIntegerv(
            final int name,
            final IntBuffer buffer)
          {
            if (name == GL3.GL_MAX_DRAW_BUFFERS) {
              JOGLFramebuffersTestGL33.LOG.debug(
                "overriding request for {}",
                Integer.valueOf(GL3.GL_MAX_DRAW_BUFFERS));
              buffer.put(0, 1);
            } else {
              super.glGetIntegerv(name, buffer);
            }
          }
        };
      }, 24, 8);
  }

  @Test
  public void testRedundantReadBind()
    throws Exception
  {
    final AtomicInteger binds = new AtomicInteger(0);

    final JCGLContextType c =
      JOGLTestContexts.newGL33ContextWithSupplierAndErrors(
        "main", ctx -> {
          final GL3 base = ctx.getGL().getGL3();
          return new DebugGL3(base)
          {
            @Override
            public void glBindFramebuffer(
              final int arg0,
              final int arg1)
            {
              binds.incrementAndGet();
              super.glBindFramebuffer(arg0, arg1);
            }
          };
        }, 24, 8);

    final JCGLInterfaceGL33Type g = c.contextGetGL33();
    final JCGLFramebuffersType g_fb = g.framebuffers();
    final JCGLTexturesType g_tx = g.textures();
    final JCGLFramebufferBuilderType fbb = g_fb.framebufferNewBuilder();

    final List<JCGLFramebufferColorAttachmentPointType> ps =
      g_fb.framebufferGetColorAttachments();
    final List<JCGLFramebufferDrawBufferType> db =
      g_fb.framebufferGetDrawBuffers();

    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();
    final JCGLTextureUnitType u0 = us.get(0);

    final int min = Math.min(db.size(), ps.size());
    final List<JCGLTexture2DType> attaches = new ArrayList<>(min);
    for (int index = 0; index < min; ++index) {
      final JCGLTexture2DType tc = g_tx.texture2DAllocate(
        u0,
        64L,
        64L,
        JCGLTextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
      fbb.attachColorTexture2DAt(ps.get(index), db.get(index), tc);
      attaches.add(tc);
    }

    binds.set(0);

    final JCGLFramebufferType fb = g_fb.framebufferAllocate(fbb);
    Assert.assertEquals(0L, (long) fb.framebufferStencilBits());
    Assert.assertEquals(0L, (long) fb.framebufferDepthBits());
    Assert.assertEquals(1L, binds.get());

    g_fb.framebufferDrawUnbind();
    Assert.assertEquals(2L, binds.get());

    g_fb.framebufferReadBind(fb);
    Assert.assertEquals(3L, binds.get());
    g_fb.framebufferReadBind(fb);
    Assert.assertEquals(3L, binds.get());
    g_fb.framebufferReadBind(fb);
    Assert.assertEquals(3L, binds.get());

    g_fb.framebufferReadUnbind();
    Assert.assertEquals(4L, binds.get());
    g_fb.framebufferReadUnbind();
    Assert.assertEquals(4L, binds.get());
    g_fb.framebufferReadUnbind();
    Assert.assertEquals(4L, binds.get());
  }

  @Test
  public void testRedundantDrawBind()
    throws Exception
  {
    final AtomicInteger binds = new AtomicInteger(0);

    final JCGLContextType c =
      JOGLTestContexts.newGL33ContextWithSupplierAndErrors(
        "main", ctx -> {
          final GL3 base = ctx.getGL().getGL3();
          return new DebugGL3(base)
          {
            @Override
            public void glBindFramebuffer(
              final int arg0,
              final int arg1)
            {
              binds.incrementAndGet();
              super.glBindFramebuffer(arg0, arg1);
            }
          };
        }, 24, 8);

    final JCGLInterfaceGL33Type g = c.contextGetGL33();
    final JCGLFramebuffersType g_fb = g.framebuffers();
    final JCGLTexturesType g_tx = g.textures();
    final JCGLFramebufferBuilderType fbb = g_fb.framebufferNewBuilder();

    final List<JCGLFramebufferColorAttachmentPointType> ps =
      g_fb.framebufferGetColorAttachments();
    final List<JCGLFramebufferDrawBufferType> db =
      g_fb.framebufferGetDrawBuffers();

    final List<JCGLTextureUnitType> us = g_tx.textureGetUnits();
    final JCGLTextureUnitType u0 = us.get(0);

    final int min = Math.min(db.size(), ps.size());
    final List<JCGLTexture2DType> attaches = new ArrayList<>(min);
    for (int index = 0; index < min; ++index) {
      final JCGLTexture2DType tc = g_tx.texture2DAllocate(
        u0,
        64L,
        64L,
        JCGLTextureFormat.TEXTURE_FORMAT_RGBA_8_4BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
      fbb.attachColorTexture2DAt(ps.get(index), db.get(index), tc);
      attaches.add(tc);
    }

    binds.set(0);

    final JCGLFramebufferType fb = g_fb.framebufferAllocate(fbb);
    Assert.assertEquals(0L, (long) fb.framebufferStencilBits());
    Assert.assertEquals(0L, (long) fb.framebufferDepthBits());
    Assert.assertEquals(1L, binds.get());

    g_fb.framebufferDrawBind(fb);
    Assert.assertEquals(1L, binds.get());
    g_fb.framebufferDrawBind(fb);
    Assert.assertEquals(1L, binds.get());
    g_fb.framebufferDrawBind(fb);
    Assert.assertEquals(1L, binds.get());

    g_fb.framebufferDrawUnbind();
    Assert.assertEquals(2L, binds.get());
    g_fb.framebufferDrawUnbind();
    Assert.assertEquals(2L, binds.get());
    g_fb.framebufferDrawUnbind();
    Assert.assertEquals(2L, binds.get());
  }

  @Override
  public void onTestCompleted()
  {
    JOGLTestContexts.closeAllContexts();
  }

  @Override
  protected Interfaces getInterfaces(final String name)
  {
    final JCGLContextType c = JOGLTestContexts.newGL33Context(name, 24, 8);
    final JCGLInterfaceGL33Type cg = c.contextGetGL33();
    return new Interfaces(c, cg.framebuffers(), cg.textures());
  }

  @Override
  protected boolean hasRealBlitting()
  {
    return true;
  }
}
