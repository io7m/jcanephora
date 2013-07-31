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
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * <p>
 * An implementation of a source backed by a single {@link java.net.URI}. When
 * evaluated, the implementation simply returns the data at the given URI.
 * </p>
 */

@Immutable public final class JCGPURISource implements JCGPSource
{
  private final @Nonnull URI source;
  private boolean            always;

  public JCGPURISource(
    final @Nonnull URI source)
    throws ConstraintError
  {
    this.source = Constraints.constrainNotNull(source, "URI");
    this.always = false;
  }

  /**
   * <p>
   * Because there is obviously no protocol-independent way to know if data at
   * a given URI has changed without fetching it, the programmer must simply
   * assume that either the data has always changed, or the data has never
   * changed. The default is not to check, as this may potentially be a very
   * expensive and blocking operation.
   * </p>
   * <p>
   * This method sets the return value of
   * {@link #sourceChangedSince(Calendar)}. Essentially,
   * <code>∀b t. s.sourceAlwaysChanged(b) => s.sourceChangedSince(t) == b</code>
   * </p>
   * <p>
   * 
   * </p>
   */

  public void sourceAlwaysChanged(
    final boolean always_changed)
  {
    this.always = always_changed;
  }

  @Override public boolean sourceChangedSince(
    final @Nonnull Calendar since)
  {
    return this.always;
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
      final URL url = this.source.toURL();
      reader = new BufferedReader(new InputStreamReader(url.openStream()));
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
