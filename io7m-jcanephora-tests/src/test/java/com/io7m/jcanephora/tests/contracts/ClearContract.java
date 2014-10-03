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

package com.io7m.jcanephora.tests.contracts;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jcanephora.ClearSpecification;
import com.io7m.jcanephora.ClearSpecificationBuilderType;
import com.io7m.jcanephora.FramebufferType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionNoDepthBuffer;
import com.io7m.jcanephora.JCGLExceptionNoStencilBuffer;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.api.JCGLClearType;
import com.io7m.jcanephora.api.JCGLFramebuffersCommonType;
import com.io7m.jcanephora.api.JCGLImplementationType;
import com.io7m.jcanephora.tests.TestContext;

public abstract class ClearContract implements TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract JCGLFramebuffersCommonType getFramebuffers(
    final TestContext context);

  public abstract JCGLClearType getClear(
    final TestContext context);

  public abstract FramebufferType makeFramebufferWithDepth(
    final TestContext tc,
    final JCGLImplementationType gi)
    throws JCGLException;

  public abstract FramebufferType makeFramebufferWithDepthStencil(
    final TestContext tc,
    final JCGLImplementationType gi)
    throws JCGLException;

  public abstract FramebufferType makeFramebuffer(
    final TestContext tc,
    final JCGLImplementationType gi)
    throws JCGLException;

  @Test public final void testColorBufferClear()
    throws JCGLExceptionRuntime
  {
    final TestContext tc = this.newTestContext();
    final JCGLClearType glc = this.getClear(tc);
    final JCGLFramebuffersCommonType glf = this.getFramebuffers(tc);

    final FramebufferType f =
      this.makeFramebufferWithDepthStencil(tc, tc.getGLImplementation());
    glf.framebufferDrawBind(f);

    final ClearSpecificationBuilderType cb = ClearSpecification.newBuilder();
    cb.enableColorBufferClear4f(1.0f, 2.0f, 3.0f, 4.0f);
    cb.enableDepthBufferClear(0.5f);
    cb.enableStencilBufferClear(1);
    cb.setStrictChecking(true);

    final ClearSpecification c = cb.build();
    glc.clear(c);
  }

  @Test(expected = JCGLExceptionNoDepthBuffer.class) public final
    void
    testColorBufferClearNoDepth()
      throws JCGLExceptionRuntime
  {
    final TestContext tc = this.newTestContext();
    final JCGLClearType glc = this.getClear(tc);
    final JCGLFramebuffersCommonType glf = this.getFramebuffers(tc);

    final FramebufferType f =
      this.makeFramebuffer(tc, tc.getGLImplementation());
    glf.framebufferDrawBind(f);

    final ClearSpecificationBuilderType cb = ClearSpecification.newBuilder();
    cb.enableColorBufferClear4f(1.0f, 2.0f, 3.0f, 4.0f);
    cb.enableDepthBufferClear(0.5f);
    cb.setStrictChecking(true);

    final ClearSpecification c = cb.build();
    glc.clear(c);
  }

  @Test(expected = JCGLExceptionNoStencilBuffer.class) public final
    void
    testColorBufferClearNoStencil()
      throws JCGLExceptionRuntime
  {
    final TestContext tc = this.newTestContext();
    final JCGLClearType glc = this.getClear(tc);
    final JCGLFramebuffersCommonType glf = this.getFramebuffers(tc);

    final FramebufferType f =
      this.makeFramebuffer(tc, tc.getGLImplementation());
    glf.framebufferDrawBind(f);

    final ClearSpecificationBuilderType cb = ClearSpecification.newBuilder();
    cb.enableColorBufferClear4f(1.0f, 2.0f, 3.0f, 4.0f);
    cb.enableStencilBufferClear(1);
    cb.setStrictChecking(true);

    final ClearSpecification c = cb.build();
    glc.clear(c);
  }
}
