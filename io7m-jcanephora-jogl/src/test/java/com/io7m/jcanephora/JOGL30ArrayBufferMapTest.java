package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Option;
import com.io7m.jcanephora.contracts_full.ArrayBufferMapContract;
import com.io7m.jlog.Log;
import com.io7m.jvvfs.FilesystemAPI;

public final class JOGL30ArrayBufferMapTest extends ArrayBufferMapContract
{
  @Override public Log getLog()
  {
    return JOGL30TestLog.getLog();
  }

  @Override public FilesystemAPI makeNewFS()
  {
    return JOGL30TestFilesystem.getFS();
  }

  @Override public Option<GLInterface> makeNewGL()
    throws GLException,
      ConstraintError
  {
    return JOGL30TestDisplay.makeFreshGLFull();
  }
}
