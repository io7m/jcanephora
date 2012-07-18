package com.io7m.jcanephora.contracts;

import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jtensors.VectorI2I;

public abstract class ViewportContract implements GLTestContract
{
  /**
   * Setting a viewport works.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  @Test public final void testViewport()
    throws GLException,
      ConstraintError
  {
    final GLInterface gl = this.getGL();
    gl.viewportSet(new VectorI2I(0, 0), new VectorI2I(64, 64));
  }

  /**
   * Setting a viewport with a null position fails.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testViewportPositionNull()
      throws GLException,
        ConstraintError
  {
    final GLInterface gl = this.getGL();
    gl.viewportSet(null, new VectorI2I(64, 64));
  }

  /**
   * Setting a viewport with a null dimension fails.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testViewportDimensionNull()
      throws GLException,
        ConstraintError
  {
    final GLInterface gl = this.getGL();
    gl.viewportSet(new VectorI2I(0, 0), null);
  }

  /**
   * Setting a viewport with a negative X dimension fails.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testViewportDimensionNegativeX()
      throws GLException,
        ConstraintError
  {
    final GLInterface gl = this.getGL();
    gl.viewportSet(new VectorI2I(0, 0), new VectorI2I(-1, 32));
  }

  /**
   * Setting a viewport with a negative Y dimension fails.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public final
    void
    testViewportDimensionNegativeY()
      throws GLException,
        ConstraintError
  {
    final GLInterface gl = this.getGL();
    gl.viewportSet(new VectorI2I(0, 0), new VectorI2I(32, -1));
  }
}
