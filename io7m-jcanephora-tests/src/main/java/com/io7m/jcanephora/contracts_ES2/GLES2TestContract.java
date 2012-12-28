package com.io7m.jcanephora.contracts_ES2;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterfaceES2;
import com.io7m.jlog.Log;

public interface GLES2TestContract
{
  /**
   * Construct a fresh logger.
   */

  public Log getLog();

  /**
   * Return <code>true</code> if the test can run given the current OpenGL
   * implementation.
   * 
   * The purpose of this is to allow for sets of tests to be executed first on
   * an OpenGL "desktop" profile like 3.0, and then executed on an OpenGL ES 2
   * context if there is one available.
   */

  public boolean isGLSupported();

  /**
   * Construct a fresh implementation of the {@link GLInterfaceES2} interface.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  public GLInterfaceES2 makeNewGL()
    throws GLException,
      ConstraintError;
}
