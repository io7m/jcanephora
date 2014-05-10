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

package com.io7m.jcanephora;

import com.io7m.jnull.NullCheck;
import com.io7m.junreachable.UnreachableCodeException;

/**
 * Resource checking.
 */

public final class ResourceCheck
{
  /**
   * Check that the current object has not been deleted.
   * 
   * @param r
   *          The object.
   * @return The object.
   * @param <R>
   *          A deletable object.
   * @throws JCGLExceptionDeleted
   *           If the object has been deleted.
   * @see JCGLResourceUsableType#resourceIsDeleted()
   */

  public static <R extends JCGLResourceUsableType> R notDeleted(
    final R r)
    throws JCGLExceptionDeleted
  {
    NullCheck.notNull(r, "Resource");

    if (r.resourceIsDeleted()) {
      final String s =
        String.format(
          "Cannot perform operation: OpenGL object %s has been deleted.",
          r);
      assert s != null;
      throw new JCGLExceptionDeleted(s);
    }

    return r;
  }

  private ResourceCheck()
  {
    throw new UnreachableCodeException();
  }
}