/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.examples;

import com.io7m.jcanephora.TextureLoaderType;
import com.io7m.jcanephora.api.JCGLImplementationType;
import com.io7m.jlog.LogUsableType;
import com.io7m.jtensors.VectorM2I;
import com.io7m.jtensors.VectorReadable2IType;
import com.io7m.jvvfs.FilesystemType;

public final class ExampleConfig
{
  private final FilesystemType         filesystem;
  private final JCGLImplementationType gl_implementation;
  private final LogUsableType          log;
  private final TextureLoaderType      texture_loader;
  private final VectorM2I              window_position;
  private final VectorM2I              window_size;

  public ExampleConfig(
    final JCGLImplementationType gl,
    final TextureLoaderType texture_loader1,
    final LogUsableType log1,
    final FilesystemType filesystem1,
    final VectorM2I window_position1,
    final VectorM2I window_size1)
  {
    this.gl_implementation = gl;
    this.log = log1;
    this.texture_loader = texture_loader1;
    this.filesystem = filesystem1;
    this.window_position = window_position1;
    this.window_size = window_size1;
  }

  FilesystemType getFilesystem()
  {
    return this.filesystem;
  }

  JCGLImplementationType getGL()
  {
    return this.gl_implementation;
  }

  LogUsableType getLog()
  {
    return this.log;
  }

  TextureLoaderType getTextureLoader()
  {
    return this.texture_loader;
  }

  VectorReadable2IType getWindowPosition()
  {
    return this.window_position;
  }

  VectorReadable2IType getWindowSize()
  {
    return this.window_size;
  }
}
