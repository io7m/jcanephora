package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts_embedded.ArrayBufferContract;
import com.io7m.jlog.Log;
import com.io7m.jvvfs.FilesystemAPI;

public final class LWJGL30ArrayBufferTest extends ArrayBufferContract
{
  @Override public Log getLog()
  {
    return LWJGL30TestLog.getLog();
  }

  @Override public FilesystemAPI makeNewFS()
  {
    return LWJGL30TestFilesystem.getFS();
  }

  @Override public GLInterface makeNewGL()
    throws GLException,
      ConstraintError
  {
    return LWJGL30TestDisplay.getGL();
  }
}
