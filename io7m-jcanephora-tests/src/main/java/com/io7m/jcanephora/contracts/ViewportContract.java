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

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.JCGLViewport;
import com.io7m.jcanephora.TestContext;
import com.io7m.jtensors.VectorI2I;

public abstract class ViewportContract implements TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract JCGLViewport getGLViewport(
    TestContext tc);

  /**
   * Setting a viewport works.
   * 
   * @throws JCGLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test public final void testViewport()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLViewport gl = this.getGLViewport(tc);

    gl.viewportSet(new VectorI2I(0, 0), new VectorI2I(64, 64));
  }

  /**
   * Setting a viewport with a negative X dimension fails.
   * 
   * @throws JCGLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testViewportDimensionNegativeX()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLViewport gl = this.getGLViewport(tc);

    gl.viewportSet(new VectorI2I(0, 0), new VectorI2I(-1, 32));
  }

  /**
   * Setting a viewport with a negative Y dimension fails.
   * 
   * @throws JCGLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testViewportDimensionNegativeY()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLViewport gl = this.getGLViewport(tc);

    gl.viewportSet(new VectorI2I(0, 0), new VectorI2I(32, -1));
  }

  /**
   * Setting a viewport with a null dimension fails.
   * 
   * @throws JCGLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testViewportDimensionNull()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLViewport gl = this.getGLViewport(tc);

    gl.viewportSet(new VectorI2I(0, 0), null);
  }

  /**
   * Setting a viewport with a null position fails.
   * 
   * @throws JCGLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testViewportPositionNull()
      throws JCGLException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLViewport gl = this.getGLViewport(tc);

    gl.viewportSet(null, new VectorI2I(64, 64));
  }
}
