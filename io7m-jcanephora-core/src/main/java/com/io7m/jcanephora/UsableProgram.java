/*
 * Copyright © 2012 http://io7m.com
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

package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * A usable shading language program.
 */

public interface UsableProgram
{
  /**
   * Make the program active.
   * 
   * @param gl
   *          An OpenGL interface.
   * @throws ConstraintError
   *           Iff <code>gl == null</code> or one of the constraints for
   *           {@link GLInterfaceEmbedded#programActivate(ProgramReference)}
   *           does not hold.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void activate(
    final @Nonnull GLInterfaceEmbedded gl)
    throws ConstraintError,
      GLException;

  /**
   * Deactivate the current program. If the current program is not active, the
   * function does nothing.
   * 
   * @param gl
   * @throws ConstraintError
   *           Iff <code>gl == null</code>.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void deactivate(
    final @Nonnull GLInterfaceEmbedded gl)
    throws ConstraintError,
      GLException;
}
