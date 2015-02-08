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

package com.io7m.jcanephora.tests.contracts.gl3;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jcanephora.FramebufferType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionNoDepthBuffer;
import com.io7m.jcanephora.api.JCGLDepthClampingType;
import com.io7m.jcanephora.api.JCGLFramebuffersCommonType;
import com.io7m.jcanephora.api.JCGLImplementationType;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.contracts.TestContract;

@SuppressWarnings({ "null" }) public abstract class DepthClampContract implements
  TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract JCGLDepthClampingType getGLDepthClamping(
    final TestContext tc);

  public abstract JCGLFramebuffersCommonType getGLFramebuffers(
    final TestContext tc);

  public abstract FramebufferType makeFramebufferWithDepth(
    final TestContext tc,
    final JCGLImplementationType gi)
    throws JCGLException;

  public abstract FramebufferType makeFramebufferWithoutDepth(
    final TestContext tc,
    final JCGLImplementationType gi)
    throws JCGLException;

  @Test(expected = JCGLExceptionNoDepthBuffer.class) public
    void
    testDepthClampingWithoutDepthFails_0()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementationType gi = tc.getGLImplementation();
    final JCGLFramebuffersCommonType gl = this.getGLFramebuffers(tc);
    final JCGLDepthClampingType gd = this.getGLDepthClamping(tc);

    final FramebufferType fb = this.makeFramebufferWithoutDepth(tc, gi);
    gl.framebufferDrawBind(fb);
    gd.depthClampingIsEnabled();
  }

  @Test(expected = JCGLExceptionNoDepthBuffer.class) public
    void
    testDepthClampingWithoutDepthFails_1()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementationType gi = tc.getGLImplementation();
    final JCGLFramebuffersCommonType gl = this.getGLFramebuffers(tc);
    final JCGLDepthClampingType gd = this.getGLDepthClamping(tc);

    final FramebufferType fb = this.makeFramebufferWithoutDepth(tc, gi);
    gl.framebufferDrawBind(fb);
    gd.depthClampingEnable();
  }

  @Test(expected = JCGLExceptionNoDepthBuffer.class) public
    void
    testDepthClampingWithoutDepthFails_2()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementationType gi = tc.getGLImplementation();
    final JCGLFramebuffersCommonType gl = this.getGLFramebuffers(tc);
    final JCGLDepthClampingType gd = this.getGLDepthClamping(tc);

    final FramebufferType fb = this.makeFramebufferWithoutDepth(tc, gi);
    gl.framebufferDrawBind(fb);
    gd.depthClampingDisable();
  }

  @Test public void testDepthClampingWorks()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLImplementationType gi = tc.getGLImplementation();
    final JCGLFramebuffersCommonType gl = this.getGLFramebuffers(tc);
    final JCGLDepthClampingType gd = this.getGLDepthClamping(tc);

    final FramebufferType fb = this.makeFramebufferWithDepth(tc, gi);
    gl.framebufferDrawBind(fb);

    Assert.assertFalse(gd.depthClampingIsEnabled());
    gd.depthClampingEnable();
    Assert.assertTrue(gd.depthClampingIsEnabled());
    gd.depthClampingDisable();
    Assert.assertFalse(gd.depthClampingIsEnabled());
  }
}
