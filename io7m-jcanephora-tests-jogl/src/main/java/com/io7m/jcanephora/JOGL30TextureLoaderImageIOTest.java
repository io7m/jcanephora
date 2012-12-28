package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jlog.Log;
import com.io7m.jvvfs.FilesystemAPI;

public final class JOGL30TextureLoaderImageIOTest extends
  TextureLoaderImageIOTest
{
  @Override public Log getLog()
  {
    return JOGL30TestLog.getLog();
  }

  @Override public FilesystemAPI makeNewFS()
  {
    return JOGL30TestFilesystem.getFS();
  }

  @Override public GLInterfaceES2 makeNewGL()
    throws GLException,
      ConstraintError
  {
    return JOGL30TestDisplay.makeFreshGLEmbedded();
  }
}
