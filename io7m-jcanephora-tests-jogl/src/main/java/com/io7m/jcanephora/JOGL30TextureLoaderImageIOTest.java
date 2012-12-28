package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jlog.Log;
import com.io7m.jvvfs.FilesystemAPI;
import com.io7m.jvvfs.PathVirtual;

public final class JOGL30TextureLoaderImageIOTest extends
  TextureLoaderImageIOTest
{
  @Override public Log getLog()
  {
    return JOGLTestLog.getLog();
  }

  @Override public PathVirtual getShaderPath()
    throws ConstraintError
  {
    return new PathVirtual("/com/io7m/jcanephora/shaders/glsl110");
  }

  @Override public boolean isGLSupported()
  {
    return JOGLTestDisplay.isOpenGL3Supported();
  }

  @Override public FilesystemAPI makeNewFS()
  {
    return JOGLTestFilesystem.getFS();
  }

  @Override public GLInterfaceES2 makeNewGL()
    throws GLException,
      ConstraintError
  {
    return JOGLTestDisplay.makeES2WithOpenGL3();
  }
}
