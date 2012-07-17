package com.io7m.jcanephora.contracts;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jcanephora.TextureUnit;

public abstract class TexturesContract implements GLTestContract
{
  /**
   * OpenGL implementations support a minimum of two texture units.
   * 
   * Note: this number is picked based on older OpenGL ES limits.
   */

  @Test public void testGetUnits()
    throws ConstraintError,
      GLException
  {
    final GLInterface gl = this.getGL();
    final TextureUnit[] u = gl.textureGetUnits();
    Assert.assertTrue(u.length >= 2);
  }
}
