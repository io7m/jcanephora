package com.io7m.jcanephora;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts_ES2.FilesystemTestContract;
import com.io7m.jvvfs.Filesystem;
import com.io7m.jvvfs.FilesystemAPI;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathVirtual;

public final class LWJGL30TestFilesystem
{
  static FilesystemAPI getFS()
  {
    try {
      final Filesystem fs = new Filesystem(LWJGL30TestLog.getLog());
      fs.mountUnsafeClasspathItem(
        FilesystemTestContract.class,
        new PathVirtual("/"));
      return fs;
    } catch (final FilesystemError e) {
      throw new java.lang.RuntimeException(e);
    } catch (final ConstraintError e) {
      throw new java.lang.RuntimeException(e);
    }
  }
}
