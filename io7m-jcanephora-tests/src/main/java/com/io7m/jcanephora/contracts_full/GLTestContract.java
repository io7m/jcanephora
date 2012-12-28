package com.io7m.jcanephora.contracts_full;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jlog.Log;

public interface GLTestContract
{
  /**
   * Construct a fresh logger.
   */

  public Log getLog();

  /**
   * Return <code>true</code> if the test can run given the current OpenGL
   * implementation.
   */

  public boolean isGLSupported();

  /**
   * Construct a fresh implementation of the {@link GLInterface} interface.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  public GLInterface makeNewGL()
    throws GLException,
      ConstraintError;
}
