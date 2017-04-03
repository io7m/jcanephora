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
import com.io7m.jcanephora.core.api.JCGLTexturesType;
import com.io7m.jcanephora.tests.contracts.JCGLTexturesContract;
import com.jogamp.opengl.DebugGL3;
import com.jogamp.opengl.GL3;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.IntBuffer;

public final class JOGLTexturesTestGL33 extends JCGLTexturesContract
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(JOGLTexturesTestGL33.class);
  }

  @Override public void onTestCompleted()
  {
    JOGLTestContexts.closeAllContexts();
  }

  @Override protected JCGLTexturesType getTextures(final String name)
  {
    final JCGLContextType c = JOGLTestContexts.newGL33Context(name, 24, 8);
    return c.contextGetGL33().getTextures();
  }

  @Test public void testNonCompliantTextureSize()
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
            if (name == GL3.GL_MAX_TEXTURE_SIZE) {
              JOGLTexturesTestGL33.LOG.debug(
                "overriding request for {}",
                Integer.valueOf(GL3.GL_MAX_TEXTURE_SIZE));
              buffer.put(0, 1);
            } else {
              super.glGetIntegerv(name, buffer);
            }
          }
        };
      }, 24, 8);
  }

  @Test public void testNonCompliantTextureUnits()
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
            if (name == GL3.GL_MAX_TEXTURE_IMAGE_UNITS) {
              JOGLTexturesTestGL33.LOG.debug(
                "overriding request for {}",
                Integer.valueOf(GL3.GL_MAX_TEXTURE_IMAGE_UNITS));
              buffer.put(0, 1);
            } else {
              super.glGetIntegerv(name, buffer);
            }
          }
        };
      }, 24, 8);
  }
}
