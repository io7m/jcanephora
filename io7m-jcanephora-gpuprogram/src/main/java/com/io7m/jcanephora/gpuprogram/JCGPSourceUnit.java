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

import java.util.Collections;
import java.util.List;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.JCGLApiKindES;
import com.io7m.jcanephora.JCGLApiKindFull;
import com.io7m.jcanephora.JCGLCompileException;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.JCGLVersionNumber;

/**
 * A named source unit that returns GLSL program text when "evaluated".
 */

public abstract class JCGPSourceUnit
{
  private final @Nonnull String                            name;
  private final @Nonnull List<String>                      imports;
  private final @Nonnull JCGPVersionRange<JCGLApiKindES>   versions_es;
  private final @Nonnull JCGPVersionRange<JCGLApiKindFull> versions_full;

  protected JCGPSourceUnit(
    final @Nonnull String name,
    final @Nonnull List<String> imports,
    final @CheckForNull JCGPVersionRange<JCGLApiKindES> versions_es,
    final @CheckForNull JCGPVersionRange<JCGLApiKindFull> versions_full)
    throws ConstraintError
  {
    this.name = Constraints.constrainNotNull(name, "Unit name");
    this.imports = Constraints.constrainNotNull(imports, "Import list");
    this.versions_es = versions_es;
    this.versions_full = versions_full;

    Constraints.constrainArbitrary((versions_es != null)
      || (versions_full != null), "At least one version range is required");
  }

  /**
   * Retrieve the range of OpenGL ES versions supported by this source unit
   * (or <tt>null</tt> if OpenGL ES is not supported).
   */

  public final @CheckForNull
    JCGPVersionRange<JCGLApiKindES>
    sourceESVersionRange()
  {
    return this.versions_es;
  }

  /**
   * Evaluate this source unit for the given OpenGL version and produce GLSL
   * program text.
   * 
   * @throws JCGLUnsupportedException
   *           Iff the given version does not fall within the range supported
   *           by this unit.
   * @throws JCGLCompileException
   *           Iff a problem occurs during source evaluation (such as an I/O
   *           error whilst reading from a source file).
   * @throws ConstraintError
   *           Iff <tt>version == null</tt>.
   */

  public final @Nonnull String sourceEvaluate(
    final @Nonnull JCGLVersionNumber version,
    final boolean es)
    throws JCGLUnsupportedException,
      JCGLCompileException,
      ConstraintError
  {
    Constraints.constrainNotNull(version, "Version");

    if (es) {
      if (this.versions_es == null) {
        throw this.unsupportedVersion(version, es);
      }
      if (this.versions_es.includes(version) == false) {
        throw this.unsupportedVersion(version, es);
      }
    } else {
      if (this.versions_full == null) {
        throw this.unsupportedVersion(version, es);
      }
      if (this.versions_full.includes(version) == false) {
        throw this.unsupportedVersion(version, es);
      }
    }

    return this.sourceEvaluateActual(version, es);
  }

  /**
   * Evaluate this source unit for the given OpenGL version and produce GLSL
   * program text. The function will always be called with a version that this
   * unit claims to support.
   * 
   * @see #sourceESVersionRange()
   * @see #sourceFullVersionRange()
   */

  protected abstract @Nonnull String sourceEvaluateActual(
    final @Nonnull JCGLVersionNumber version,
    final boolean es)
    throws JCGLUnsupportedException,
      JCGLCompileException;

  /**
   * Retrieve the range of desktop OpenGL versions supported by this source
   * unit (or <tt>null</tt> if desktop OpenGL is not supported).
   */

  public final @CheckForNull
    JCGPVersionRange<JCGLApiKindFull>
    sourceFullVersionRange()
  {
    return this.versions_full;
  }

  /**
   * Retrieve the list of units directly imported by this unit, in the order
   * that they were imported.
   */

  public final @Nonnull List<String> sourceGetImports()
  {
    return Collections.unmodifiableList(this.imports);
  }

  /**
   * Retrieve the unique name of this source unit.
   */

  public final @Nonnull String sourceUnitName()
  {
    return this.name;
  }

  private @Nonnull JCGLUnsupportedException unsupportedVersion(
    final @Nonnull JCGLVersionNumber version,
    final boolean es)
  {
    final StringBuilder b = new StringBuilder();
    b.append("Source unit ");
    b.append(this.name);
    b.append(" does not support OpenGL ");
    b.append(es ? "ES" : "");
    b.append(" version ");
    b.append(version);
    b.append(". Supported versions are:");
    if (this.versions_es != null) {
      b.append(" ES ");
      b.append(this.versions_es.toRangeNotation());
    }
    if (this.versions_full != null) {
      b.append(" ");
      b.append(this.versions_full.toRangeNotation());
    }
    return new JCGLUnsupportedException(b.toString());
  }
}
