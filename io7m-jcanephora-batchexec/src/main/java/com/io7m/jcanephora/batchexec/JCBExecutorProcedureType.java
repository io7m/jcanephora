/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.batchexec;

import com.io7m.jcanephora.JCGLException;

/**
 * A procedure that executes using a given program.
 * 
 * @param <E>
 *          The type of thrown exceptions (other than {@link JCGLException}).
 */

public interface JCBExecutorProcedureType<E extends Throwable>
{
  /**
   * Execute the procedure with the given program.
   * 
   * @param program
   *          The program.
   * @throws JCGLException
   *           If required.
   * @throws E
   *           If required.
   */

  void call(
    final JCBProgramType program)
    throws JCGLException,
      E;
}
