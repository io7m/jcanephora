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

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jcanephora.IndexBufferType;
import com.io7m.jcanephora.IndexBufferUsableType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionDeleted;
import com.io7m.jcanephora.JCGLUnsignedType;
import com.io7m.jcanephora.Primitives;
import com.io7m.jcanephora.UsageHint;
import com.io7m.jcanephora.api.JCGLDrawType;
import com.io7m.jcanephora.api.JCGLIndexBuffersType;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.TestUtilities;
import com.io7m.jnull.NullCheckException;

@SuppressWarnings({ "null" }) public abstract class DrawContract implements
  TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract @Nonnull JCGLDrawType getGLDraw(
    TestContext context);

  public abstract @Nonnull JCGLIndexBuffersType getGLIndexBuffers(
    TestContext context);

  /**
   * Drawing primitives works.
   */

  @Test public final void testDraw()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLDrawType gd = this.getGLDraw(tc);
    final JCGLIndexBuffersType gi = this.getGLIndexBuffers(tc);

    final IndexBufferType ib =
      gi.indexBufferAllocateType(
        JCGLUnsignedType.TYPE_UNSIGNED_BYTE,
        100,
        UsageHint.USAGE_STATIC_DRAW);

    for (final Primitives p : Primitives.values()) {
      gd.drawElements(p, ib);
    }
  }

  /**
   * Drawing primitives with a deleted index buffer fails.
   */

  @Test(expected = JCGLExceptionDeleted.class) public final
    void
    testDrawIndexDeleted()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLDrawType gd = this.getGLDraw(tc);
    final JCGLIndexBuffersType gi = this.getGLIndexBuffers(tc);

    final IndexBufferType ib =
      gi.indexBufferAllocateType(
        JCGLUnsignedType.TYPE_UNSIGNED_BYTE,
        100,
        UsageHint.USAGE_STATIC_DRAW);

    gi.indexBufferDelete(ib);
    gd.drawElements(Primitives.PRIMITIVE_TRIANGLES, ib);
  }

  /**
   * Drawing primitives with a null index buffer fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testDrawNullIndex()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLDrawType gd = this.getGLDraw(tc);

    gd.drawElements(
      Primitives.PRIMITIVE_TRIANGLES,
      (IndexBufferUsableType) TestUtilities.actuallyNull());
  }

  /**
   * Drawing null primitives fails.
   */

  @Test(expected = NullCheckException.class) public final
    void
    testDrawNullPrimitive()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLDrawType gd = this.getGLDraw(tc);
    final JCGLIndexBuffersType gi = this.getGLIndexBuffers(tc);

    final IndexBufferType ib =
      gi.indexBufferAllocateType(
        JCGLUnsignedType.TYPE_UNSIGNED_BYTE,
        100,
        UsageHint.USAGE_STATIC_DRAW);

    gd.drawElements((Primitives) TestUtilities.actuallyNull(), ib);
  }
}
