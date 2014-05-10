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

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.api.JCGLColorBufferType;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.TestUtilities;
import com.io7m.jnull.NullCheckException;
import com.io7m.jtensors.VectorI3F;
import com.io7m.jtensors.VectorI4F;
import com.io7m.jtensors.VectorReadable3FType;
import com.io7m.jtensors.VectorReadable4FType;

public abstract class ColorBufferContract implements TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract JCGLColorBufferType getGLColorBuffer(
    final TestContext context);

  @Test public final void testColorBufferClear()
    throws JCGLExceptionRuntime
  {
    final TestContext tc = this.newTestContext();
    final JCGLColorBufferType gl = this.getGLColorBuffer(tc);

    gl.colorBufferClear3f(1.0f, 0.0f, 0.0f);
    gl.colorBufferClear4f(1.0f, 0.0f, 0.0f, 1.0f);
    gl.colorBufferClearV3f(new VectorI3F(1.0f, 0.0f, 0.0f));
    gl.colorBufferClearV4f(new VectorI4F(0.0f, 1.0f, 1.0f, 1.0f));
  }

  @Test(expected = NullCheckException.class) public final
    void
    testColorBufferClearV3Null()
      throws JCGLExceptionRuntime
  {
    final TestContext tc = this.newTestContext();
    final JCGLColorBufferType gl = this.getGLColorBuffer(tc);

    gl.colorBufferClearV3f((VectorReadable3FType) TestUtilities
      .actuallyNull());
  }

  @Test(expected = NullCheckException.class) public final
    void
    testColorBufferClearV4Null()
      throws JCGLExceptionRuntime
  {
    final TestContext tc = this.newTestContext();
    final JCGLColorBufferType gl = this.getGLColorBuffer(tc);

    gl.colorBufferClearV4f((VectorReadable4FType) TestUtilities
      .actuallyNull());
  }

  /**
   * Color masking works.
   */

  @Test public final void testColorBufferMask()
    throws JCGLExceptionRuntime
  {
    final TestContext tc = this.newTestContext();
    final JCGLColorBufferType gl = this.getGLColorBuffer(tc);

    {
      final boolean r = gl.colorBufferMaskStatusRed();
      final boolean g = gl.colorBufferMaskStatusGreen();
      final boolean b = gl.colorBufferMaskStatusBlue();
      final boolean a = gl.colorBufferMaskStatusAlpha();

      Assert.assertTrue(r);
      Assert.assertTrue(g);
      Assert.assertTrue(b);
      Assert.assertTrue(a);
    }

    {
      gl.colorBufferMask(false, true, true, true);

      final boolean r = gl.colorBufferMaskStatusRed();
      final boolean g = gl.colorBufferMaskStatusGreen();
      final boolean b = gl.colorBufferMaskStatusBlue();
      final boolean a = gl.colorBufferMaskStatusAlpha();

      Assert.assertFalse(r);
      Assert.assertTrue(g);
      Assert.assertTrue(b);
      Assert.assertTrue(a);
    }

    {
      gl.colorBufferMask(true, false, true, true);

      final boolean r = gl.colorBufferMaskStatusRed();
      final boolean g = gl.colorBufferMaskStatusGreen();
      final boolean b = gl.colorBufferMaskStatusBlue();
      final boolean a = gl.colorBufferMaskStatusAlpha();

      Assert.assertTrue(r);
      Assert.assertFalse(g);
      Assert.assertTrue(b);
      Assert.assertTrue(a);
    }

    {
      gl.colorBufferMask(true, true, false, true);

      final boolean r = gl.colorBufferMaskStatusRed();
      final boolean g = gl.colorBufferMaskStatusGreen();
      final boolean b = gl.colorBufferMaskStatusBlue();
      final boolean a = gl.colorBufferMaskStatusAlpha();

      Assert.assertTrue(r);
      Assert.assertTrue(g);
      Assert.assertFalse(b);
      Assert.assertTrue(a);
    }

    {
      gl.colorBufferMask(true, true, true, false);

      final boolean r = gl.colorBufferMaskStatusRed();
      final boolean g = gl.colorBufferMaskStatusGreen();
      final boolean b = gl.colorBufferMaskStatusBlue();
      final boolean a = gl.colorBufferMaskStatusAlpha();

      Assert.assertTrue(r);
      Assert.assertTrue(g);
      Assert.assertTrue(b);
      Assert.assertFalse(a);
    }

    {
      gl.colorBufferMask(false, false, false, false);

      final boolean r = gl.colorBufferMaskStatusRed();
      final boolean g = gl.colorBufferMaskStatusGreen();
      final boolean b = gl.colorBufferMaskStatusBlue();
      final boolean a = gl.colorBufferMaskStatusAlpha();

      Assert.assertFalse(r);
      Assert.assertFalse(g);
      Assert.assertFalse(b);
      Assert.assertFalse(a);
    }
  }
}
