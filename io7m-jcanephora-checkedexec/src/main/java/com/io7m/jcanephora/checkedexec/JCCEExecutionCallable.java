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

package com.io7m.jcanephora.checkedexec;

import java.util.Map;
import java.util.concurrent.Callable;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.JCGLType;
import com.io7m.jcanephora.ProgramReferenceUsable;

/**
 * <p>
 * An implementation of the {@link JCCEExecutionAPI} that executes a given
 * {@link Callable} on request.
 * <p>
 * Values of this type cannot be manipulated by multiple threads without
 * explicit synchronization.
 * </p>
 */

public final class JCCEExecutionCallable extends JCCEExecutionAbstract
{
  /**
   * <p>
   * Declare a new program with the given uniform and attribute declarations.
   * The attributes and uniforms that appear in <code>program</code> must be a
   * subset of those given in <code>declared_uniforms</code> and
   * <code>declared_attributes</code> and the declared types must match the
   * actual types.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li>
   *           <code>program == null || declared_uniforms == null || declared_attributes == null</code>
   *           </li>
   *           <li>The program's parameters are not a subset of the given
   *           declared parameters.</li>
   *           </ul>
   */

  public static @Nonnull JCCEExecutionCallable newProgramWithDeclarations(
    final @Nonnull ProgramReferenceUsable program,
    final @Nonnull Map<String, JCGLType> declared_uniforms,
    final @Nonnull Map<String, JCGLType> declared_attributes)
    throws ConstraintError
  {
    Constraints.constrainNotNull(declared_uniforms, "Declared uniforms");
    Constraints.constrainNotNull(declared_attributes, "Declared attributes");
    return new JCCEExecutionCallable(program, null, null);
  }

  /**
   * <p>
   * Declare a new program without any specific uniform or attribute
   * declarations.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff <code>program == null</code>.
   */

  public static @Nonnull JCCEExecutionCallable newProgramWithoutDeclarations(
    final @Nonnull ProgramReferenceUsable program)
    throws ConstraintError
  {
    return new JCCEExecutionCallable(program, null, null);
  }

  private @CheckForNull Callable<Void> callable;

  private JCCEExecutionCallable(
    final @Nonnull ProgramReferenceUsable program,
    final @CheckForNull Map<String, JCGLType> declared_uniforms,
    final @CheckForNull Map<String, JCGLType> declared_attributes)
    throws ConstraintError
  {
    super(program, declared_uniforms, declared_attributes);
  }

  @Override protected void execRunActual()
    throws ConstraintError,
      Exception
  {
    if (this.callable == null) {
      Constraints.constrainArbitrary(
        false,
        "Execution has been assigned a Callable");
    }

    this.callable.call();
  }

  /**
   * Set the callable that will be executed by this execution to
   * <code>c</code>.
   * 
   * @throws ConstraintError
   *           Iff <code>c == null</code>.
   */

  public final void execSetCallable(
    final @Nonnull Callable<Void> c)
    throws ConstraintError
  {
    this.callable = Constraints.constrainNotNull(c, "Callable");
  }
}
