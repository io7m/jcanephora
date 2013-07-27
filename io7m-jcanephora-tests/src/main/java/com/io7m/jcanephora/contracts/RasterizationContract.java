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
import com.io7m.jcanephora.JCGLPolygonModes;
import com.io7m.jcanephora.JCGLPolygonSmoothing;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.PolygonMode;
import com.io7m.jcanephora.TestContext;

public abstract class RasterizationContract implements TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract JCGLPolygonModes getGLPolygonModes(
    TestContext tc);

  public abstract JCGLPolygonSmoothing getGLPolygonSmoothing(
    TestContext tc);

  /**
   * Polygon mode setting works.
   * 
   * @throws JCGLException
   * @throws ConstraintError
   */

  @Test public final void testPolygonModeIdentities()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLPolygonModes gl = this.getGLPolygonModes(tc);

    gl.polygonSetMode(PolygonMode.POLYGON_FILL);
    Assert.assertEquals(PolygonMode.POLYGON_FILL, gl.polygonGetMode());

    for (final PolygonMode pm : PolygonMode.values()) {
      System.err.println("front: " + pm);
      gl.polygonSetMode(pm);
      Assert.assertEquals(pm, gl.polygonGetMode());
    }
  }

  /**
   * Enabling/disabling polygon smoothing works.
   * 
   * @throws JCGLException
   * @throws ConstraintError
   */

  @Test public final void testPolygonSmoothing()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLPolygonSmoothing gl = this.getGLPolygonSmoothing(tc);

    gl.polygonSmoothingDisable();
    Assert.assertFalse(gl.polygonSmoothingIsEnabled());
    gl.polygonSmoothingEnable();
    Assert.assertTrue(gl.polygonSmoothingIsEnabled());
  }
}
