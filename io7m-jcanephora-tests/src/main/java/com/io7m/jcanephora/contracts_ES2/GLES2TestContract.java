package com.io7m.jcanephora.contracts_ES2;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLUnsupportedException;
import com.io7m.jcanephora.TestContext;

public interface GLES2TestContract
{
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

  public @Nonnull TestContext getTestContext()
    throws GLException,
      GLUnsupportedException,
      ConstraintError;

  /**
   * Return <code>true</code> if the test can run given the current OpenGL
   * implementation.
   * 
   * The purpose of this is to allow for sets of tests to be executed first on
   * an OpenGL "desktop" profile like 3.0, and then executed on an OpenGL ES 2
   * context if there is one available.
   */

  public boolean isGLSupported();
}
