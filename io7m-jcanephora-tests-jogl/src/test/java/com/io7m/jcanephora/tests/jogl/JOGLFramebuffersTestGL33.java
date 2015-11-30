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
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.core.api.JCGLFramebuffersType;
import com.io7m.jcanephora.core.api.JCGLInterfaceGL33Type;
import com.io7m.jcanephora.tests.contracts.JCGLFramebuffersContract;
import com.jogamp.opengl.DebugGL3;
import com.jogamp.opengl.GL3;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.IntBuffer;

public final class JOGLFramebuffersTestGL33 extends JCGLFramebuffersContract
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(JOGLFramebuffersTestGL33.class);
  }

  @Test public void testNonCompliantColorAttachments()
    throws Exception
  {
    this.expected.expect(JCGLExceptionNonCompliant.class);
    JOGLTestContexts.newGL33ContextWithSupplierAndErrors(
      "main", c -> {
        final GL3 base = c.getGL().getGL3();
        return new DebugGL3(base)
        {
          @Override public void glGetIntegerv(
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
      });
  }

  @Test public void testClampedDrawBuffers()
    throws Exception
  {
    JCGLContextType gg = JOGLTestContexts.newGL33ContextWithSupplierAndErrors(
      "main", c -> {
        final GL3 base = c.getGL().getGL3();
        return new DebugGL3(base)
        {
          @Override public void glGetIntegerv(
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
      });

    JCGLInterfaceGL33Type i = gg.contextGetGL33();
    JCGLFramebuffersType gf = i.getFramebuffers();
    Assert.assertEquals(1024, gf.framebufferGetDrawBuffers().size());
  }

  @Test public void testClampedColorAttachments()
    throws Exception
  {
    JCGLContextType gg = JOGLTestContexts.newGL33ContextWithSupplierAndErrors(
      "main", c -> {
        final GL3 base = c.getGL().getGL3();
        return new DebugGL3(base)
        {
          @Override public void glGetIntegerv(
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
      });

    JCGLInterfaceGL33Type i = gg.contextGetGL33();
    JCGLFramebuffersType gf = i.getFramebuffers();
    Assert.assertEquals(1024, gf.framebufferGetColorAttachments().size());
  }

  @Test public void testNonCompliantDrawBuffers()
    throws Exception
  {
    this.expected.expect(JCGLExceptionNonCompliant.class);
    JOGLTestContexts.newGL33ContextWithSupplierAndErrors(
      "main", c -> {
        final GL3 base = c.getGL().getGL3();
        return new DebugGL3(base)
        {
          @Override public void glGetIntegerv(
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
      });
  }

  @Override public void onTestCompleted()
  {
    JOGLTestContexts.closeAllContexts();
  }

  @Override protected Interfaces getInterfaces(final String name)
  {
    final JCGLContextType c = JOGLTestContexts.newGL33Context(name);
    final JCGLInterfaceGL33Type cg = c.contextGetGL33();
    return new Interfaces(c, cg.getFramebuffers(), cg.getTextures());
  }
}
