package com.io7m.jcanephora.contracts_full;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLImplementation;
import com.io7m.jcanephora.GLUnsupportedException;
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
   * Construct a fresh OpenGL implementation.
   * 
   * @throws GLException
   * @throws ConstraintError
   * @throws GLUnsupportedException
   */

  public GLImplementation makeNewGLImplementation()
    throws GLException, GLUnsupportedException,
      ConstraintError,
      GLUnsupportedException;
}
