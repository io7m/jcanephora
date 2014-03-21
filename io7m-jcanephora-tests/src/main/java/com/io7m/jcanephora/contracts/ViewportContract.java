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
import com.io7m.jaux.RangeInclusive;
import com.io7m.jcanephora.AreaInclusive;
import com.io7m.jcanephora.JCGLRuntimeException;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.JCGLViewport;
import com.io7m.jcanephora.TestContext;

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
   */

  @Test public final void testViewport()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLViewport gl = this.getGLViewport(tc);

    final AreaInclusive area =
      new AreaInclusive(new RangeInclusive(0, 63), new RangeInclusive(0, 63));

    gl.viewportSet(area);
  }

  /**
   * Setting a viewport with a null area fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testViewportNull()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLViewport gl = this.getGLViewport(tc);

    gl.viewportSet(null);
  }
}
