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

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.JCGLCompileException;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLShaderKindFragment;
import com.io7m.jcanephora.JCGLShaderKindVertex;
import com.io7m.jcanephora.JCGLShaders;

/**
 * <p>
 * The interface provided by compilers.
 * </p>
 */

public interface JCGPCompilerAPI
{
  /**
   * Compile a program named <code>name</code> consisting of the vertex shader
   * described by <code>vertex_source</code> and the fragment shader described
   * by <code>fragment_source</code>, using <code>gl</code> as the reference
   * to the GLSL compiler.
   * 
   * @throws ConstraintError
   *           Iff
   *           <code>gl == null || name == null || vertex_source == null || fragment_source == null</code>
   *           .
   */

  public @Nonnull
    JCGPProgram
    compileProgram(
      final @Nonnull JCGLShaders gl,
      final @Nonnull String name,
      final @Nonnull JCGPGeneratedSource<JCGLShaderKindVertex> vertex_source,
      final @Nonnull JCGPGeneratedSource<JCGLShaderKindFragment> fragment_source)
      throws ConstraintError,
        JCGLCompileException,
        JCGLException;
}
