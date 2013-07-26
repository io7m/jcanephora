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
import com.io7m.jcanephora.GLDraw;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLIndexBuffers;
import com.io7m.jcanephora.GLUnsignedType;
import com.io7m.jcanephora.GLUnsupportedException;
import com.io7m.jcanephora.IndexBuffer;
import com.io7m.jcanephora.Primitives;
import com.io7m.jcanephora.TestContext;

public abstract class DrawContract implements TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract @Nonnull GLDraw getGLDraw(
    TestContext context);

  public abstract @Nonnull GLIndexBuffers getGLIndexBuffers(
    TestContext context);

  /**
   * Drawing primitives works.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test public final void testDraw()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLDraw gd = this.getGLDraw(tc);
    final GLIndexBuffers gi = this.getGLIndexBuffers(tc);

    final IndexBuffer ib =
      gi.indexBufferAllocateType(GLUnsignedType.TYPE_UNSIGNED_BYTE, 100);

    for (final Primitives p : Primitives.values()) {
      gd.drawElements(p, ib);
    }
  }

  /**
   * Drawing primitives with a deleted index buffer fails.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testDrawIndexDeleted()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLDraw gd = this.getGLDraw(tc);
    final GLIndexBuffers gi = this.getGLIndexBuffers(tc);

    final IndexBuffer ib =
      gi.indexBufferAllocateType(GLUnsignedType.TYPE_UNSIGNED_BYTE, 100);

    gi.indexBufferDelete(ib);
    gd.drawElements(Primitives.PRIMITIVE_TRIANGLES, ib);
  }

  /**
   * Drawing primitives with a null index buffer fails.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testDrawNullIndex()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLDraw gd = this.getGLDraw(tc);

    gd.drawElements(Primitives.PRIMITIVE_TRIANGLES, null);
  }

  /**
   * Drawing null primitives fails.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testDrawNullPrimitive()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLDraw gd = this.getGLDraw(tc);
    final GLIndexBuffers gi = this.getGLIndexBuffers(tc);

    final IndexBuffer ib =
      gi.indexBufferAllocateType(GLUnsignedType.TYPE_UNSIGNED_BYTE, 100);

    gd.drawElements(null, ib);
  }
}
