package com.io7m.jcanephora.examples;

import com.io7m.jcanephora.GLInterfaceEmbedded;
import com.io7m.jlog.Log;
import com.io7m.jtensors.VectorM2I;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jvvfs.FilesystemAPI;

final class ExampleConfig
{
  private final GLInterfaceEmbedded gl;
  private final Log                 log;
  private final FilesystemAPI       filesystem;
  private final VectorM2I           window_position;
  private final VectorM2I           window_size;

  public ExampleConfig(
    final GLInterfaceEmbedded gl,
    final Log log,
    final FilesystemAPI filesystem,
    final VectorM2I window_position,
    final VectorM2I window_size)
  {
    this.gl = gl;
    this.log = log;
    this.filesystem = filesystem;
    this.window_position = window_position;
    this.window_size = window_size;
  }

  FilesystemAPI getFilesystem()
  {
    return this.filesystem;
  }

  GLInterfaceEmbedded getGL()
  {
    return this.gl;
  }

  Log getLog()
  {
    return this.log;
  }

  VectorReadable2I getWindowPize()
  {
    return this.window_size;
  }

  VectorReadable2I getWindowPosition()
  {
    return this.window_position;
  }
}
