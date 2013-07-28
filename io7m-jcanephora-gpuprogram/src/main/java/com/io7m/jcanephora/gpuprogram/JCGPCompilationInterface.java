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
import com.io7m.jcanephora.JCGLUnsupportedException;

/**
 * <p>
 * The interface provided by compilers.
 * </p>
 */

public interface JCGPCompilationInterface
{
  /**
   * <p>
   * Return <tt>true</tt> if debugging is currently enabled.
   * </p>
   */

  public boolean compilationIsDebugging();

  /**
   * <p>
   * Enable or disable debugging.
   * </p>
   * <p>
   * When debugging is enabled, comments may be placed into GLSL source.
   * </p>
   */

  public void compilationSetDebugging(
    boolean on);

  /**
   * <p>
   * Add the unit <tt>unit</tt> to the compilation.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>unit == null</code></li>
   *           <li>A unit already exists named <code>unit.getName()</code></li>
   *           <li>The unit is a vertex shader main function, and the
   *           compilation already has a vertex shader main function defined.</li>
   *           <li>The unit is a fragment shader main function, and the
   *           compilation already has a fragment shader main function
   *           defined.</li>
   *           </ul>
   * @throws JCGLUnsupportedException
   *           Iff the given unit does not provide an implementation for one
   *           or more of the versions required by the compilation.
   */

  public void compilationUnitAdd(
    final @Nonnull JCGPUnit unit)
    throws ConstraintError,
      JCGLUnsupportedException;

  /**
   * <p>
   * Remove the unit named <tt>unit</tt> (if any) from the compilation.
   * </p>
   */

  public void compilationUnitRemove(
    final @Nonnull String unit)
    throws ConstraintError;

}
