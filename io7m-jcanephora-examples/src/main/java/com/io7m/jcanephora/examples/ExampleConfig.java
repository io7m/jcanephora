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

package com.io7m.jcanephora.examples;

import javax.annotation.Nonnull;

import com.io7m.jcanephora.JCGLImplementation;
import com.io7m.jcanephora.TextureLoader;
import com.io7m.jlog.Log;
import com.io7m.jtensors.VectorM2I;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jvvfs.FSCapabilityAll;

final class ExampleConfig
{
  private final @Nonnull FSCapabilityAll    filesystem;
  private final @Nonnull JCGLImplementation gl_implementation;
  private final @Nonnull Log                log;
  private final @Nonnull TextureLoader      texture_loader;
  private final @Nonnull VectorM2I          window_position;
  private final @Nonnull VectorM2I          window_size;

  public ExampleConfig(
    final @Nonnull JCGLImplementation gl,
    final @Nonnull TextureLoader texture_loader1,
    final @Nonnull Log log1,
    final @Nonnull FSCapabilityAll filesystem1,
    final @Nonnull VectorM2I window_position1,
    final @Nonnull VectorM2I window_size1)
  {
    this.gl_implementation = gl;
    this.log = log1;
    this.texture_loader = texture_loader1;
    this.filesystem = filesystem1;
    this.window_position = window_position1;
    this.window_size = window_size1;
  }

  @Nonnull FSCapabilityAll getFilesystem()
  {
    return this.filesystem;
  }

  @Nonnull JCGLImplementation getGL()
  {
    return this.gl_implementation;
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
