package com.io7m.jcanephora.contracts.common;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLUnsupportedException;
import com.io7m.jcanephora.TestContext;

public interface TestContract
{
  /**
   * Return <code>true</code> if the test can run given the current OpenGL
   * implementation.
   */

  public boolean isGLSupported();

  /**
   * Construct test context data.
   * 
   * This generally involves mounting a filesystem and opening a new OpenGL
   * context.
   * 
   * @throws ConstraintError
   * @throws GLUnsupportedException
   * @throws GLException
   */

  public @Nonnull TestContext newTestContext()
    throws GLException,
      GLUnsupportedException,
      ConstraintError;
}
