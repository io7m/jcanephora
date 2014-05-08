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

import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.LogicOperation;
import com.io7m.jcanephora.api.JCGLLogicType;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.TestUtilities;
import com.io7m.jnull.NullCheckException;

@SuppressWarnings({ "null" }) public abstract class LogicOpContract implements
  TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract JCGLLogicType getGLLogic(
    TestContext tc);

  /**
   * Enabling/disabling logic operations works.
   */

  @Test public void testLogicOpsEnable()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLLogicType gl = this.getGLLogic(tc);

    for (final LogicOperation op : LogicOperation.values()) {
      gl.logicOperationsDisable();
      Assert.assertFalse(gl.logicOperationsEnabled());
      gl.logicOperationsEnable(op);
      Assert.assertTrue(gl.logicOperationsEnabled());
    }
  }

  /**
   * Trying to enable a null logic operation fails.
   */

  @Test(expected = NullCheckException.class) public void testLogicOpsNull()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLLogicType gl = this.getGLLogic(tc);

    gl.logicOperationsEnable((LogicOperation) TestUtilities.actuallyNull());
  }
}
