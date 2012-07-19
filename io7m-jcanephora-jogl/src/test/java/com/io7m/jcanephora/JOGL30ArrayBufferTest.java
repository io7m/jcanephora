package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts.ArrayBufferContract;
import com.io7m.jlog.Log;
import com.io7m.jvvfs.FilesystemAPI;

public final class JOGL30ArrayBufferTest extends ArrayBufferContract
{
  @Override public FilesystemAPI getFS()
  {
    return JOGL30TestFilesystem.getFS();
  }

  @Override public GLInterface getGL()
    throws GLException,
      ConstraintError
  {
    return JOGL30TestDisplay.makeFreshGL();
  }

  @Override public Log getLog()
  {
    return JOGL30TestLog.getLog();
  }
}
