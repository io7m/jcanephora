package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts_ES2.ArrayBufferContract;
import com.io7m.jlog.Log;
import com.io7m.jvvfs.FilesystemAPI;
import com.io7m.jvvfs.PathVirtual;

public final class LWJGLES2ArrayBufferTest extends ArrayBufferContract
{
  @Override public Log getLog()
  {
    return LWJGLTestLog.getLog();
  }

  @Override public PathVirtual getShaderPath()
    throws ConstraintError
  {
    return new PathVirtual("/com/io7m/jcanephora/shaders/glsles100");
  }

  @Override public boolean isGLSupported()
  {
    return LWJGLTestDisplay.isOpenGLES2Supported();
  }

  @Override public FilesystemAPI makeNewFS()
  {
    return LWJGLTestFilesystem.getFS();
  }

  @Override public GLImplementation makeNewGLImplementation()
    throws GLException,
      ConstraintError,
      GLUnsupportedException
  {
    return LWJGLTestDisplay.makeImplementationWithOpenGLES2();
  }
}
