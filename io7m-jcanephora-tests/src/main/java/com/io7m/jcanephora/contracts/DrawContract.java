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

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.IndexBuffer;
import com.io7m.jcanephora.JCGLDraw;
import com.io7m.jcanephora.JCGLRuntimeException;
import com.io7m.jcanephora.JCGLIndexBuffers;
import com.io7m.jcanephora.JCGLUnsignedType;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.Primitives;
import com.io7m.jcanephora.TestContext;

public abstract class DrawContract implements TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract @Nonnull JCGLDraw getGLDraw(
    TestContext context);

  public abstract @Nonnull JCGLIndexBuffers getGLIndexBuffers(
    TestContext context);

  /**
   * Drawing primitives works.
   * 
   * @throws JCGLRuntimeException
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test public final void testDraw()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLDraw gd = this.getGLDraw(tc);
    final JCGLIndexBuffers gi = this.getGLIndexBuffers(tc);

    final IndexBuffer ib =
      gi.indexBufferAllocateType(JCGLUnsignedType.TYPE_UNSIGNED_BYTE, 100);

    for (final Primitives p : Primitives.values()) {
      gd.drawElements(p, ib);
    }
  }

  /**
   * Drawing primitives with a deleted index buffer fails.
   * 
   * @throws JCGLRuntimeException
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testDrawIndexDeleted()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLDraw gd = this.getGLDraw(tc);
    final JCGLIndexBuffers gi = this.getGLIndexBuffers(tc);

    final IndexBuffer ib =
      gi.indexBufferAllocateType(JCGLUnsignedType.TYPE_UNSIGNED_BYTE, 100);

    gi.indexBufferDelete(ib);
    gd.drawElements(Primitives.PRIMITIVE_TRIANGLES, ib);
  }

  /**
   * Drawing primitives with a null index buffer fails.
   * 
   * @throws JCGLRuntimeException
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testDrawNullIndex()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLDraw gd = this.getGLDraw(tc);

    gd.drawElements(Primitives.PRIMITIVE_TRIANGLES, null);
  }

  /**
   * Drawing null primitives fails.
   * 
   * @throws JCGLRuntimeException
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testDrawNullPrimitive()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLDraw gd = this.getGLDraw(tc);
    final JCGLIndexBuffers gi = this.getGLIndexBuffers(tc);

    final IndexBuffer ib =
      gi.indexBufferAllocateType(JCGLUnsignedType.TYPE_UNSIGNED_BYTE, 100);

    gd.drawElements(null, ib);
  }
}
