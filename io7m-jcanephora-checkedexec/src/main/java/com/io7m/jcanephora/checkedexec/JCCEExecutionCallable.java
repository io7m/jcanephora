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

import java.util.concurrent.Callable;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
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
  private @CheckForNull Callable<Void> callable;

  public JCCEExecutionCallable(
    final @Nonnull ProgramReferenceUsable program)
    throws ConstraintError
  {
    super(program);
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
}
