package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts_ES2.ProgramContract;
import com.io7m.jlog.Log;
import com.io7m.jvvfs.FilesystemAPI;
import com.io7m.jvvfs.PathVirtual;

public final class LWJGL30ProgramTest extends ProgramContract
{
  @Override public Log getLog()
  {
    return LWJGLTestLog.getLog();
  }

  @Override public PathVirtual getShaderPath()
    throws ConstraintError
  {
    return new PathVirtual("/com/io7m/jcanephora/shaders/glsl110");
  }

  @Override public boolean isGLSupported()
  {
    return LWJGLTestDisplay.isOpenGL3Supported();
  }

  @Override public FilesystemAPI makeNewFS()
  {
    return LWJGLTestFilesystem.getFS();
  }

  @Override public GLInterfaceES2 makeNewGL()
    throws GLException,
      ConstraintError
  {
    return LWJGLTestDisplay.makeES2WithOpenGL3();
  }
}
