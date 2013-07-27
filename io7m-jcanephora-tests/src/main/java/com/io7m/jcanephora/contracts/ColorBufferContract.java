/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
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
package com.io7m.jcanephora.contracts;

import javax.annotation.Nonnull;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.JCGLColorBuffer;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.TestContext;
import com.io7m.jtensors.VectorI3F;
import com.io7m.jtensors.VectorI4F;

public abstract class ColorBufferContract implements TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract JCGLColorBuffer getGLColorBuffer(
    final @Nonnull TestContext context);

  @Test public final void testColorBufferClear()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLColorBuffer gl = this.getGLColorBuffer(tc);

    gl.colorBufferClear3f(1.0f, 0.0f, 0.0f);
    gl.colorBufferClear4f(1.0f, 0.0f, 0.0f, 1.0f);
    gl.colorBufferClearV3f(new VectorI3F(1.0f, 0.0f, 0.0f));
    gl.colorBufferClearV4f(new VectorI4F(0.0f, 1.0f, 1.0f, 1.0f));
  }

  @Test(expected = ConstraintError.class) public final
    void
    testColorBufferClearV3Null()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLColorBuffer gl = this.getGLColorBuffer(tc);

    gl.colorBufferClearV3f(null);
  }

  @Test(expected = ConstraintError.class) public final
    void
    testColorBufferClearV4Null()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLColorBuffer gl = this.getGLColorBuffer(tc);

    gl.colorBufferClearV4f(null);
  }

  /**
   * Color masking works.
   */

  @Test public final void testColorBufferMask()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLColorBuffer gl = this.getGLColorBuffer(tc);

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
