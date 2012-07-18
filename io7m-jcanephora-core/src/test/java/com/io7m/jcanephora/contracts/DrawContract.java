package com.io7m.jcanephora.contracts;

import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jcanephora.GLUnsignedType;
import com.io7m.jcanephora.IndexBuffer;
import com.io7m.jcanephora.Primitives;

public abstract class DrawContract implements GLTestContract
{
  /**
   * Drawing primitives works.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  @Test public final void testDraw()
    throws GLException,
      ConstraintError
  {
    final GLInterface gl = this.getGL();
    final IndexBuffer ib =
      gl.indexBufferAllocateType(GLUnsignedType.TYPE_UNSIGNED_BYTE, 100);

    for (final Primitives p : Primitives.values()) {
      gl.drawElements(p, ib);
    }
  }

  /**
   * Drawing primitives with a deleted index buffer fails.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testDrawIndexDeleted()
      throws GLException,
        ConstraintError
  {
    final GLInterface gl = this.getGL();
    final IndexBuffer ib =
      gl.indexBufferAllocateType(GLUnsignedType.TYPE_UNSIGNED_BYTE, 100);

    ib.resourceDelete(gl);
    gl.drawElements(Primitives.PRIMITIVE_TRIANGLES, ib);
  }

  /**
   * Drawing primitives with a null index buffer fails.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testDrawNullIndex()
      throws GLException,
        ConstraintError
  {
    final GLInterface gl = this.getGL();
    gl.drawElements(Primitives.PRIMITIVE_TRIANGLES, null);
  }

  /**
   * Drawing null primitives fails.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testDrawNullPrimitive()
      throws GLException,
        ConstraintError
  {
    final GLInterface gl = this.getGL();
    final IndexBuffer ib =
      gl.indexBufferAllocateType(GLUnsignedType.TYPE_UNSIGNED_BYTE, 100);

    gl.drawElements(null, ib);
  }
}