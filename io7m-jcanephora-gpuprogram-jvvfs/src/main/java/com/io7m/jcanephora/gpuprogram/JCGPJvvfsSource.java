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

package com.io7m.jcanephora.gpuprogram;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jvvfs.FSCapabilityRead;
import com.io7m.jvvfs.Filesystem;
import com.io7m.jvvfs.PathVirtual;

/**
 * <p>
 * An implementation of a source backed by a single file in a
 * {@link Filesystem}. When evaluated, the implementation simply returns the
 * contents of the given file.
 * </p>
 */

public final class JCGPJvvfsSource implements JCGPSource
{
  private final @Nonnull FSCapabilityRead filesystem;
  private final @Nonnull PathVirtual      path;
  private long                            last_get;

  public JCGPJvvfsSource(
    final @Nonnull FSCapabilityRead filesystem,
    final @Nonnull PathVirtual path)
    throws ConstraintError
  {
    this.filesystem = Constraints.constrainNotNull(filesystem, "Filesystem");
    this.path = Constraints.constrainNotNull(path, "Path");
    this.last_get = Long.MIN_VALUE;
  }

  @Override public void sourceGet(
    final @Nonnull JCGPGeneratorContext context,
    final @Nonnull ArrayList<String> output)
    throws Exception,
      ConstraintError
  {
    Constraints.constrainNotNull(output, "Output array");

    this.last_get =
      this.filesystem.getModificationTime(this.path).getTimeInMillis();

    final InputStream stream = this.filesystem.openFile(this.path);
    BufferedReader reader = null;

    try {
      reader = new BufferedReader(new InputStreamReader(stream));
      for (;;) {
        final String line = reader.readLine();
        if (line == null) {
          break;
        }
        output.add(line);
      }
    } finally {
      if (reader != null) {
        reader.close();
      }
    }
  }

  @Override public boolean sourceChanged()
    throws Exception,
      ConstraintError
  {
    final long last_m =
      this.filesystem.getModificationTime(this.path).getTimeInMillis();
    return last_m != this.last_get;
  }

}
