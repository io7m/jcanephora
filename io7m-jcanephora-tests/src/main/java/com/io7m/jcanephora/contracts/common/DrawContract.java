package com.io7m.jcanephora.contracts.common;

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

  abstract @Nonnull GLDraw getGLDraw(
    TestContext context);

  abstract @Nonnull GLIndexBuffers getGLIndexBuffers(
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
