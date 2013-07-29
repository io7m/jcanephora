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

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.JCGLApi;
import com.io7m.jcanephora.JCGLSLVersionNumber;
import com.io7m.jcanephora.JCGLShaderKind;
import com.io7m.jcanephora.JCGLShaderType;

/**
 * <p>
 * Source code produced by a {@link JCGPGenerator}.
 * </p>
 * 
 * @param <S>
 *          A <i>phantom type</i> parameter to statically track what type of
 *          shading program the generated source represents.
 */

@Immutable public final class JCGPGeneratedSource<S extends JCGLShaderKind>
{
  private final @Nonnull List<String>        lines;
  private final @Nonnull JCGLSLVersionNumber version;
  private final @Nonnull JCGLApi             api;
  private final @Nonnull JCGLShaderType      type;

  JCGPGeneratedSource(
    final @Nonnull List<String> lines,
    final @Nonnull JCGLSLVersionNumber version,
    final @Nonnull JCGLApi api,
    final @Nonnull JCGLShaderType type)
    throws ConstraintError
  {
    this.lines = Constraints.constrainNotNull(lines, "Lines");
    this.version = Constraints.constrainNotNull(version, "Version");
    this.api = Constraints.constrainNotNull(api, "API");
    this.type = Constraints.constrainNotNull(type, "Type");
  }

  /**
   * Retrieve the API for which the source code was produced.
   */

  public @Nonnull JCGLApi getApi()
  {
    return this.api;
  }

  /**
   * Retrieve the lines of generated source code.
   */

  public @Nonnull List<String> getLines()
  {
    return Collections.unmodifiableList(this.lines);
  }

  /**
   * Retrieve the type of shader generated.
   */

  public @Nonnull JCGLShaderType getType()
  {
    return this.type;
  }

  /**
   * Retrieve the shading language version for which the source code was
   * produced.
   */

  public @Nonnull JCGLSLVersionNumber getVersion()
  {
    return this.version;
  }
}
