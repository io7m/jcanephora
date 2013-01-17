package com.io7m.jcanephora.contracts_ES2;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.FaceSelection;
import com.io7m.jcanephora.FaceWindingOrder;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterfaceES2;
import com.io7m.jcanephora.GLUnsupportedException;
import com.io7m.jcanephora.TestContext;

public abstract class CullContract implements GLES2TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  /**
   * Enabling culling works.
   */

  @Test public void testCullingEnable()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    for (final FaceSelection select : FaceSelection.values()) {
      for (final FaceWindingOrder order : FaceWindingOrder.values()) {
        gl.cullingDisable();
        Assert.assertFalse(gl.cullingIsEnabled());
        gl.cullingEnable(select, order);
        Assert.assertTrue(gl.cullingIsEnabled());
      }
    }
  }

  /**
   * Null parameters fail.
   */

  @Test(expected = ConstraintError.class) public void testCullingNullFace()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    gl.cullingEnable(null, FaceWindingOrder.FRONT_FACE_CLOCKWISE);
  }

  /**
   * Null parameters fail.
   */

  @Test(expected = ConstraintError.class) public void testCullingNullOrder()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    gl.cullingEnable(FaceSelection.FACE_BACK, null);
  }
}