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
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * <p>
 * An implementation of a source backed by a single {@link java.io.File}. When
 * evaluated, the implementation simply returns the contents of the given
 * file.
 * </p>
 */

@Immutable public final class JCGPFileSource implements JCGPSource
{
  private final @Nonnull File source;

  public JCGPFileSource(
    final @Nonnull File source)
    throws ConstraintError
  {
    this.source = Constraints.constrainNotNull(source, "File");
  }

  @Override public boolean sourceChangedSince(
    final @Nonnull Calendar since)
  {
    final Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    c.setTimeInMillis(this.source.lastModified());
    return c.after(since);
  }

  @Override public void sourceGet(
    final @Nonnull JCGPGeneratorContext context,
    final @Nonnull ArrayList<String> output)
    throws Exception,
      ConstraintError
  {
    Constraints.constrainNotNull(context, "Compilation context");
    Constraints.constrainNotNull(output, "Output array");

    BufferedReader reader = null;

    try {
      reader = new BufferedReader(new FileReader(this.source));
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
}
