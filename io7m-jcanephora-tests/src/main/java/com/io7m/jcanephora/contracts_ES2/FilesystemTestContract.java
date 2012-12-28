package com.io7m.jcanephora.contracts_ES2;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jvvfs.FilesystemAPI;
import com.io7m.jvvfs.PathVirtual;

public interface FilesystemTestContract
{
  /**
   * Retrieve the path to the shaders that should be used on the current
   * OpenGL implementation.
   * 
   * @throws ConstraintError
   */

  public PathVirtual getShaderPath()
    throws ConstraintError;

  /**
   * Construct a fresh filesystem populated with various test resources.
   */

  public FilesystemAPI makeNewFS();
}
