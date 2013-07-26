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

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLScissor;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.TestContext;
import com.io7m.jtensors.VectorI2I;

public abstract class ScissorContract implements TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract JCGLScissor getGLScissor(
    TestContext tc);

  @Test public void testScissorEnableWorks()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLScissor gl = this.getGLScissor(tc);

    gl.scissorDisable();
    Assert.assertFalse(gl.scissorIsEnabled());
    gl.scissorEnable(new VectorI2I(8, 8), new VectorI2I(8, 8));
    Assert.assertTrue(gl.scissorIsEnabled());
  }

  @Test(expected = ConstraintError.class) public
    void
    testScissorNullDimensions()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLScissor gl = this.getGLScissor(tc);

    gl.scissorEnable(new VectorI2I(8, 8), null);
  }

  @Test(expected = ConstraintError.class) public
    void
    testScissorNullPosition()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLScissor gl = this.getGLScissor(tc);

    gl.scissorEnable(null, new VectorI2I(8, 8));
  }
}
