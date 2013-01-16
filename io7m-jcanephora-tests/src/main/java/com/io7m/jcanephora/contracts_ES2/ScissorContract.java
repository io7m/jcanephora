package com.io7m.jcanephora.contracts_ES2;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterfaceES2;
import com.io7m.jcanephora.GLUnsupportedException;
import com.io7m.jcanephora.TestContext;
import com.io7m.jtensors.VectorI2I;

public abstract class ScissorContract implements GLES2TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  @Test public void testScissorEnableWorks()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    gl.scissorDisable();
    Assert.assertFalse(gl.scissorIsEnabled());
    gl.scissorEnable(new VectorI2I(8, 8), new VectorI2I(8, 8));
    Assert.assertTrue(gl.scissorIsEnabled());
  }

  @Test(expected = ConstraintError.class) public
    void
    testScissorNullDimensions()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    gl.scissorEnable(new VectorI2I(8, 8), null);
  }

  @Test(expected = ConstraintError.class) public
    void
    testScissorNullPosition()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    gl.scissorEnable(null, new VectorI2I(8, 8));
  }
}
