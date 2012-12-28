package com.io7m.jcanephora.contracts_ES2;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterfaceES2;

public abstract class ColorBufferContract implements GLES2TestContract
{
  /**
   * Color masking works.
   */

  @Test public final void testColorBufferMask()
    throws ConstraintError,
      GLException
  {
    final GLInterfaceES2 gl = this.makeNewGL();

    {
      final boolean r = gl.colorBufferMaskStatusRed();
      final boolean g = gl.colorBufferMaskStatusGreen();
      final boolean b = gl.colorBufferMaskStatusBlue();
      final boolean a = gl.colorBufferMaskStatusAlpha();

      Assert.assertTrue(r);
      Assert.assertTrue(g);
      Assert.assertTrue(b);
      Assert.assertTrue(a);
    }

    {
      gl.colorBufferMask(false, true, true, true);

      final boolean r = gl.colorBufferMaskStatusRed();
      final boolean g = gl.colorBufferMaskStatusGreen();
      final boolean b = gl.colorBufferMaskStatusBlue();
      final boolean a = gl.colorBufferMaskStatusAlpha();

      Assert.assertFalse(r);
      Assert.assertTrue(g);
      Assert.assertTrue(b);
      Assert.assertTrue(a);
    }

    {
      gl.colorBufferMask(true, false, true, true);

      final boolean r = gl.colorBufferMaskStatusRed();
      final boolean g = gl.colorBufferMaskStatusGreen();
      final boolean b = gl.colorBufferMaskStatusBlue();
      final boolean a = gl.colorBufferMaskStatusAlpha();

      Assert.assertTrue(r);
      Assert.assertFalse(g);
      Assert.assertTrue(b);
      Assert.assertTrue(a);
    }

    {
      gl.colorBufferMask(true, true, false, true);

      final boolean r = gl.colorBufferMaskStatusRed();
      final boolean g = gl.colorBufferMaskStatusGreen();
      final boolean b = gl.colorBufferMaskStatusBlue();
      final boolean a = gl.colorBufferMaskStatusAlpha();

      Assert.assertTrue(r);
      Assert.assertTrue(g);
      Assert.assertFalse(b);
      Assert.assertTrue(a);
    }

    {
      gl.colorBufferMask(true, true, true, false);

      final boolean r = gl.colorBufferMaskStatusRed();
      final boolean g = gl.colorBufferMaskStatusGreen();
      final boolean b = gl.colorBufferMaskStatusBlue();
      final boolean a = gl.colorBufferMaskStatusAlpha();

      Assert.assertTrue(r);
      Assert.assertTrue(g);
      Assert.assertTrue(b);
      Assert.assertFalse(a);
    }

    {
      gl.colorBufferMask(false, false, false, false);

      final boolean r = gl.colorBufferMaskStatusRed();
      final boolean g = gl.colorBufferMaskStatusGreen();
      final boolean b = gl.colorBufferMaskStatusBlue();
      final boolean a = gl.colorBufferMaskStatusAlpha();

      Assert.assertFalse(r);
      Assert.assertFalse(g);
      Assert.assertFalse(b);
      Assert.assertFalse(a);
    }
  }
}
