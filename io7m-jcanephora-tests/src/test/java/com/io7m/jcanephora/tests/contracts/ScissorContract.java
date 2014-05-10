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

import com.io7m.jcanephora.AreaInclusive;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.api.JCGLScissorType;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.TestUtilities;
import com.io7m.jnull.NullCheckException;
import com.io7m.jranges.RangeInclusiveL;

public abstract class ScissorContract implements TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract JCGLScissorType getGLScissor(
    TestContext tc);

  @Test public void testScissorEnableWorks()
    throws JCGLExceptionRuntime
  {
    final TestContext tc = this.newTestContext();
    final JCGLScissorType gl = this.getGLScissor(tc);

    final RangeInclusiveL range_x = new RangeInclusiveL(0, 7);
    final RangeInclusiveL range_y = new RangeInclusiveL(0, 7);
    final AreaInclusive area = new AreaInclusive(range_x, range_y);

    gl.scissorDisable();
    Assert.assertFalse(gl.scissorIsEnabled());
    gl.scissorEnable(area);
    Assert.assertTrue(gl.scissorIsEnabled());
  }

  @Test(expected = NullCheckException.class) public void testScissorNull()
    throws JCGLExceptionRuntime
  {
    final TestContext tc = this.newTestContext();
    final JCGLScissorType gl = this.getGLScissor(tc);

    gl.scissorEnable((AreaInclusive) TestUtilities.actuallyNull());
  }
}
