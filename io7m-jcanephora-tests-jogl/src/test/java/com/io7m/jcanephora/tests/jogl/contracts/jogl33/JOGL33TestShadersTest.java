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

package com.io7m.jcanephora.tests.jogl.contracts.jogl33;

import org.junit.Test;

import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.JCGLSLVersion;
import com.io7m.jcanephora.api.JCGLInterfaceCommonType;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.TestShaders;
import com.io7m.jcanephora.tests.contracts.TestContract;
import com.io7m.jcanephora.tests.jogl.JOGLTestContext;

public final class JOGL33TestShadersTest implements TestContract
{
  @Override public boolean isGLSupported()
  {
    return JOGLTestContext.isOpenGL33Supported();
  }

  @Override public TestContext newTestContext()
  {
    return JOGLTestContext.makeContextWithOpenGL3_p(false);
  }

  @Test public void testShaders()
    throws JCGLExceptionRuntime
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gc = tc.getGLImplementation().getGLCommon();
    final JCGLSLVersion v = gc.metaGetSLVersion();

    System.out.println(TestShaders.getVertexSourceName(v));
    System.out.println(TestShaders.getFragmentSourceName(v));
  }
}
