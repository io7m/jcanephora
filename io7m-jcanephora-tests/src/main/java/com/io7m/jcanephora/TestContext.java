/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */
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