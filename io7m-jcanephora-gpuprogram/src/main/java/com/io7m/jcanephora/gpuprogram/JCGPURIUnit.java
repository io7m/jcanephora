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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.List;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.JCGLApi;
import com.io7m.jcanephora.JCGLApiKindES;
import com.io7m.jcanephora.JCGLApiKindFull;
import com.io7m.jcanephora.JCGLCompileException;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.JCGLVersionNumber;

/**
 * An implementation of a source unit backed by a single {@link java.net.URI}.
 * When evaluated with any version, the implementation simply returns the data
 * at the given URI.
 */

public final class JCGPURIUnit extends JCGPSourceUnit
{
  private final @Nonnull URI source;

  public JCGPURIUnit(
    final @Nonnull String name,
    final @Nonnull List<String> imports,
    final @Nonnull JCGPVersionRange<JCGLApiKindES> versions_es,
    final @Nonnull JCGPVersionRange<JCGLApiKindFull> versions_full,
    final @Nonnull URI source)
    throws ConstraintError
  {
    super(name, imports, versions_es, versions_full);
    this.source = Constraints.constrainNotNull(source, "URI");
  }

  @Override public @Nonnull String sourceEvaluateActual(
    final @Nonnull JCGLVersionNumber version,
    final @Nonnull JCGLApi api)
    throws JCGLUnsupportedException,
      JCGLCompileException
  {
    final StringBuilder message = new StringBuilder();
    final StringBuilder b = new StringBuilder();
    BufferedReader reader = null;

    try {
      final URL url = this.source.toURL();
      reader = new BufferedReader(new InputStreamReader(url.openStream()));

      for (;;) {
        final String line = reader.readLine();
        if (line == null) {
          break;
        }
        b.append(line);
        b.append("\n");
      }

      return b.toString();
    } catch (final IOException e) {
      message.setLength(0);
      message.append("I/O exception reading '");
      message.append(this.source);
      message.append("': ");
      message.append(e.getMessage());
      throw new JCGLCompileException(
        e,
        this.sourceUnitName(),
        message.toString());
    } finally {
      try {
        if (reader != null) {
          reader.close();
        }
      } catch (final IOException e) {
        message.setLength(0);
        message.append("I/O exception closing '");
        message.append(this.source);
        message.append("': ");
        message.append(e.getMessage());
        throw new JCGLCompileException(
          e,
          this.sourceUnitName(),
          message.toString());
      }
    }
  }
}
