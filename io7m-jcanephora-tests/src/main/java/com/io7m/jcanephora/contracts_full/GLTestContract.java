package com.io7m.jcanephora.contracts_full;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLUnsupportedException;
import com.io7m.jcanephora.TestContext;
import com.io7m.jvvfs.PathVirtual;

public interface GLTestContract
{
  /**
   * The path that should be used to find shaders, given the current OpenGL
   * implementation.
   * 
   * @throws ConstraintError
   */

  public @Nonnull PathVirtual getShaderPath()
    throws ConstraintError;

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
   */

  public boolean isGLSupported();
}
