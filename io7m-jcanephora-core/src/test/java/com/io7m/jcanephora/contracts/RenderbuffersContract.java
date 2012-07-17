package com.io7m.jcanephora.contracts;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jcanephora.RenderbufferD24S8;

public abstract class RenderbuffersContract implements GLTestContract
{
  /**
   * Deleting a renderbuffer works.
   * 
   * @throws ConstraintError
   * @throws GLException
   */

  @Test public void testRenderbufferDelete()
    throws GLException,
      ConstraintError
  {
    final GLInterface gl = this.getGL();

    final RenderbufferD24S8 rb = gl.renderbufferD24S8Allocate(128, 128);
    Assert.assertFalse(rb.resourceIsDeleted());
    rb.resourceDelete(gl);
    Assert.assertTrue(rb.resourceIsDeleted());
  }

  /**
   * Deleting a renderbuffer twice fails.
   * 
   * @throws ConstraintError
   * @throws GLException
   */

  @Test(expected = ConstraintError.class) public
    void
    testRenderbufferDeleteTwice()
      throws GLException,
        ConstraintError
  {
    final GLInterface gl = this.getGL();

    final RenderbufferD24S8 rb = gl.renderbufferD24S8Allocate(128, 128);
    Assert.assertFalse(rb.resourceIsDeleted());
    rb.resourceDelete(gl);
    Assert.assertTrue(rb.resourceIsDeleted());
    rb.resourceDelete(gl);
  }
}
