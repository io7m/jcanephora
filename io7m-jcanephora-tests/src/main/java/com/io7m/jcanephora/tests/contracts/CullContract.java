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

import javax.annotation.Nonnull;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jcanephora.FaceSelection;
import com.io7m.jcanephora.FaceWindingOrder;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.api.JCGLCullType;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.TestUtilities;
import com.io7m.jnull.NullCheckException;

@SuppressWarnings({ "null" }) public abstract class CullContract implements
  TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract JCGLCullType getGLCull(
    final @Nonnull TestContext context);

  /**
   * Enabling culling works.
   */

  @Test public void testCullingEnable()
    throws JCGLExceptionRuntime
  {
    final TestContext tc = this.newTestContext();
    final JCGLCullType gl = this.getGLCull(tc);

    for (final FaceSelection select : FaceSelection.values()) {
      for (final FaceWindingOrder order : FaceWindingOrder.values()) {
        gl.cullingDisable();
        Assert.assertFalse(gl.cullingIsEnabled());
        gl.cullingEnable(select, order);
        Assert.assertTrue(gl.cullingIsEnabled());
      }
    }
  }

  /**
   * Null parameters fail.
   */

  @Test(expected = NullCheckException.class) public
    void
    testCullingNullFace()
      throws JCGLExceptionRuntime
  {
    final TestContext tc = this.newTestContext();
    final JCGLCullType gl = this.getGLCull(tc);

    gl.cullingEnable(
      (FaceSelection) TestUtilities.actuallyNull(),
      FaceWindingOrder.FRONT_FACE_CLOCKWISE);
  }

  /**
   * Null parameters fail.
   */

  @Test(expected = NullCheckException.class) public
    void
    testCullingNullOrder()
      throws JCGLExceptionRuntime
  {
    final TestContext tc = this.newTestContext();
    final JCGLCullType gl = this.getGLCull(tc);

    gl.cullingEnable(
      FaceSelection.FACE_BACK,
      (FaceWindingOrder) TestUtilities.actuallyNull());
  }
}
