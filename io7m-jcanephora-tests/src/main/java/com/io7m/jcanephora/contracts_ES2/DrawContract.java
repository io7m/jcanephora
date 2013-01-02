package com.io7m.jcanephora.contracts_ES2;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterfaceES2;
import com.io7m.jcanephora.GLUnsignedType;
import com.io7m.jcanephora.GLUnsupportedException;
import com.io7m.jcanephora.IndexBuffer;
import com.io7m.jcanephora.Primitives;
import com.io7m.jcanephora.TestContext;

public abstract class DrawContract implements GLES2TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

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
    final TestContext tc = this.getTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

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
    final TestContext tc = this.getTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    final IndexBuffer ib =
      gl.indexBufferAllocateType(GLUnsignedType.TYPE_UNSIGNED_BYTE, 100);

    ib.resourceDelete(gl);
    gl.drawElements(Primitives.PRIMITIVE_TRIANGLES, ib);
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
    final TestContext tc = this.getTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    gl.drawElements(Primitives.PRIMITIVE_TRIANGLES, null);
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
    final TestContext tc = this.getTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    final IndexBuffer ib =
      gl.indexBufferAllocateType(GLUnsignedType.TYPE_UNSIGNED_BYTE, 100);

    gl.drawElements(null, ib);
  }
}
