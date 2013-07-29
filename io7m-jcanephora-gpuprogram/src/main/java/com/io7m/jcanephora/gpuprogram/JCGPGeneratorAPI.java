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
import com.io7m.jcanephora.JCGLApi;
import com.io7m.jcanephora.JCGLCompileException;
import com.io7m.jcanephora.JCGLSLVersionNumber;
import com.io7m.jcanephora.JCGLShaderKindFragment;
import com.io7m.jcanephora.JCGLShaderKindVertex;
import com.io7m.jcanephora.JCGLUnsupportedException;

/**
 * <p>
 * The interface provided by generators.
 * </p>
 */

public interface JCGPGeneratorAPI
{
  /**
   * <p>
   * Generate GLSL source code for a fragment shader, compatible with version
   * <tt>version</tt> and API <tt>api</tt>.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li>
   *           <code>version == null || api == null</code></li>
   *           </ul>
   * @throws JCGLCompileException
   *           Iff any of the following hold:
   *           <ul>
   *           <li>An I/O error occurs whilst evaluating a unit.</li>
   *           <li>No unit provided a main function for the fragment shader.</li>
   *           <li>Two or more units imported each other in a manner that was
   *           cyclic.</li>
   *           <li>At least one unit imported a nonexistent unit.</li>
   *           </ul>
   * @throws JCGLUnsupportedException
   *           Iff the generator cannot produce GLSL code for the requested
   *           version.
   */

  public @Nonnull
    JCGPGeneratedSource<JCGLShaderKindFragment>
    generatorGenerateFragmentShader(
      final @Nonnull JCGLSLVersionNumber version,
      final @Nonnull JCGLApi api)
      throws JCGLCompileException,
        ConstraintError,
        JCGLUnsupportedException;

  /**
   * <p>
   * Generate GLSL source code for a vertex shader, compatible with version
   * <tt>version</tt> and API <tt>api</tt>.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li>
   *           <code>version == null || api == null</code></li>
   *           </ul>
   * @throws JCGLCompileException
   *           Iff any of the following hold:
   *           <ul>
   *           <li>An I/O error occurs whilst evaluating a unit.</li>
   *           <li>No unit provided a main function for the vertex shader.</li>
   *           <li>Two or more units imported each other in a manner that was
   *           cyclic.</li>
   *           <li>At least one unit imported a nonexistent unit.</li>
   *           </ul>
   * @throws JCGLUnsupportedException
   *           Iff the generator cannot produce GLSL code for the requested
   *           version.
   */

  public @Nonnull
    JCGPGeneratedSource<JCGLShaderKindVertex>
    generatorGenerateVertexShader(
      final @Nonnull JCGLSLVersionNumber version,
      final @Nonnull JCGLApi api)
      throws JCGLCompileException,
        ConstraintError,
        JCGLUnsupportedException;

  /**
   * <p>
   * Return <tt>true</tt> if debugging is currently enabled.
   * </p>
   */

  public boolean generatorIsDebugging();

  /**
   * <p>
   * Enable or disable debugging.
   * </p>
   * <p>
   * When debugging is enabled, comments may be placed into GLSL source.
   * </p>
   */

  public void generatorSetDebugging(
    boolean on);

  /**
   * <p>
   * Add the unit <tt>unit</tt> to the generator.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>unit == null</code></li>
   *           <li>A unit already exists named <code>unit.getName()</code></li>
   *           <li>The unit is a vertex shader main function, and the
   *           generator already has a vertex shader main function defined.</li>
   *           <li>The unit is a fragment shader main function, and the
   *           generator already has a fragment shader main function defined.</li>
   *           </ul>
   * @throws JCGLUnsupportedException
   *           Iff the given unit does not provide an implementation for one
   *           or more of the versions required by the generator.
   */

  public void generatorUnitAdd(
    final @Nonnull JCGPUnit unit)
    throws ConstraintError,
      JCGLUnsupportedException;

  /**
   * <p>
   * Remove the unit named <tt>unit</tt> (if any) from the generator.
   * </p>
   */

  public void generatorUnitRemove(
    final @Nonnull String unit)
    throws ConstraintError;

  /**
   * <p>
   * Return <tt>true</tt> iff the generator would produce different source if
   * asked to generate now (that is, at least one of the source units has been
   * modified in some way since the last generation).
   * </p>
   */

  public boolean generatorUnitsUpdated();
}
