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
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLShaders;

/**
 * <p>
 * The API supported by program executions.
 * </p>
 */

public interface JCGPExecutionAPI
{
  /**
   * <p>
   * Prepare to start executing the given program.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff <code>new_program == null || gl == null</code>.
   * @throws JCGLException
   *           Iff the program cannot start, or an OpenGL error occurs.
   */

  public void execPrepare(
    @Nonnull JCGLShaders gl,
    @Nonnull JCGPProgram new_program)
    throws ConstraintError,
      JCGLException;

  /**
   * <p>
   * Execute the {@link #execRunActual()} function with the program specified
   * with {@link #start(JCGPProgram)} as the current program, after checking
   * that all of the attributes and uniforms in the program have been bound.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>gl == null</code></li>
   *           <li>The {@link #execPrepare(JCGLShaders, JCGPProgram)} method
   *           has not been called.</li>
   *           <li>At least one of the programs uniforms have not been
   *           assigned values.</li>
   *           <li>At least one of the programs attributes have not been
   *           assigned values.</li>
   *           </ul>
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public void execRun(
    @Nonnull JCGLShaders gl)
    throws ConstraintError;
}
