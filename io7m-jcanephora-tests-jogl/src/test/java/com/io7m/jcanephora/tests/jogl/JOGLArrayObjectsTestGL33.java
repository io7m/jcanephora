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
import com.io7m.jcanephora.core.api.JCGLArrayBuffersType;
import com.io7m.jcanephora.core.api.JCGLArrayObjectsType;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.core.api.JCGLIndexBuffersType;
import com.io7m.jcanephora.core.api.JCGLInterfaceGL33Type;
import com.io7m.jcanephora.tests.contracts.JCGLArrayObjectsContract;
import com.jogamp.opengl.DebugGL3;
import com.jogamp.opengl.GL3;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.IntBuffer;

public final class JOGLArrayObjectsTestGL33 extends JCGLArrayObjectsContract
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(JOGLArrayObjectsTestGL33.class);
  }

  @Override protected Interfaces getInterfaces(final String name)
  {
    final JCGLContextType c = JOGLTestContexts.newGL33Context(name, 24, 8);
    final JCGLInterfaceGL33Type cg = c.contextGetGL33();
    final JCGLArrayBuffersType ga = cg.getArrayBuffers();
    final JCGLIndexBuffersType gi = cg.getIndexBuffers();
    final JCGLArrayObjectsType go = cg.getArrayObjects();
    return new Interfaces(c, ga, gi, go);
  }

  @Test public void testNonCompliantArrayBuffers()
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
            if (name == GL3.GL_MAX_VERTEX_ATTRIBS) {
              JOGLArrayObjectsTestGL33.LOG.debug(
                "overriding request for {}",
                Integer.valueOf(GL3.GL_MAX_VERTEX_ATTRIBS));
              buffer.put(0, 1);
            } else {
              super.glGetIntegerv(name, buffer);
            }
          }
        };
      }, 24, 8);
  }

  @Override public void onTestCompleted()
  {
    JOGLTestContexts.closeAllContexts();
  }
}
