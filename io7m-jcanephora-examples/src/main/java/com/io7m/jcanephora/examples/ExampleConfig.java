package com.io7m.jcanephora.examples;

import javax.annotation.Nonnull;

import com.io7m.jcanephora.GLInterface;
import com.io7m.jcanephora.TextureLoader;
import com.io7m.jlog.Log;
import com.io7m.jtensors.VectorM2I;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jvvfs.FilesystemAPI;

final class ExampleConfig
{
  private final @Nonnull GLInterface   gl;
  private final @Nonnull Log           log;
  private final @Nonnull FilesystemAPI filesystem;
  private final @Nonnull TextureLoader texture_loader;
  private final @Nonnull VectorM2I     window_position;
  private final @Nonnull VectorM2I     window_size;

  public ExampleConfig(
    final @Nonnull GLInterface gl,
    final @Nonnull TextureLoader texture_loader,
    final @Nonnull Log log,
    final @Nonnull FilesystemAPI filesystem,
    final @Nonnull VectorM2I window_position,
    final @Nonnull VectorM2I window_size)
  {
    this.gl = gl;
    this.log = log;
    this.texture_loader = texture_loader;
    this.filesystem = filesystem;
    this.window_position = window_position;
    this.window_size = window_size;
  }

  @Nonnull FilesystemAPI getFilesystem()
  {
    return this.filesystem;
  }

  @Nonnull GLInterface getGL()
  {
    return this.gl;
  }

  @Nonnull Log getLog()
  {
    return this.log;
  }

  @Nonnull TextureLoader getTextureLoader()
  {
    return this.texture_loader;
  }

  @Nonnull VectorReadable2I getWindowPosition()
  {
    return this.window_position;
  }

  @Nonnull VectorReadable2I getWindowSize()
  {
    return this.window_size;
  }
}
