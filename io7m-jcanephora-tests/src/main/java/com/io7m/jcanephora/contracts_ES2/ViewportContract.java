package com.io7m.jcanephora.contracts_ES2;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterfaceES2;
import com.io7m.jcanephora.GLUnsupportedException;
import com.io7m.jcanephora.TestContext;
import com.io7m.jtensors.VectorI2I;

public abstract class ViewportContract implements GLES2TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  /**
   * Setting a viewport works.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test public final void testViewport()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.getTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    gl.viewportSet(new VectorI2I(0, 0), new VectorI2I(64, 64));
  }

  /**
   * Setting a viewport with a negative X dimension fails.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testViewportDimensionNegativeX()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.getTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    gl.viewportSet(new VectorI2I(0, 0), new VectorI2I(-1, 32));
  }

  /**
   * Setting a viewport with a negative Y dimension fails.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testViewportDimensionNegativeY()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.getTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    gl.viewportSet(new VectorI2I(0, 0), new VectorI2I(32, -1));
  }

  /**
   * Setting a viewport with a null dimension fails.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testViewportDimensionNull()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.getTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    gl.viewportSet(new VectorI2I(0, 0), null);
  }

  /**
   * Setting a viewport with a null position fails.
   * 
   * @throws GLException
   *           , GLUnsupportedException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testViewportPositionNull()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.getTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    gl.viewportSet(null, new VectorI2I(64, 64));
  }
}
