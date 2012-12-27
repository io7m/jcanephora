package com.io7m.jcanephora.contracts_embedded;

import com.io7m.jvvfs.FilesystemAPI;

public interface FilesystemTestContract
{
  public FilesystemAPI makeNewFS();
}
