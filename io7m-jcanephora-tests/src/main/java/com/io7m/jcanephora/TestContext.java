package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jlog.Log;
import com.io7m.jvvfs.FilesystemAPI;
import com.io7m.jvvfs.PathVirtual;

public final class TestContext
{
  private final @Nonnull FilesystemAPI    fs;
  private final @Nonnull GLImplementation gi;
  private final @Nonnull Log              log;
  private final @Nonnull PathVirtual      shader_path;

  public TestContext(
    final @Nonnull FilesystemAPI fs,
    final @Nonnull GLImplementation gi,
    final @Nonnull Log log,
    final @Nonnull PathVirtual shader_path)
  {
    this.fs = fs;
    this.gi = gi;
    this.log = log;
    this.shader_path = shader_path;
  }

  public FilesystemAPI getFilesystem()
  {
    return this.fs;
  }

  public GLImplementation getGLImplementation()
  {
    return this.gi;
  }

  public Log getLog()
  {
    return this.log;
  }

  public PathVirtual getShaderPath()
  {
    return this.shader_path;
  }
}
