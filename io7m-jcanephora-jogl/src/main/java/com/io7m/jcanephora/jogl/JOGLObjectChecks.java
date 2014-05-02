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

package com.io7m.jcanephora.jogl;

import javax.media.opengl.GLContext;

import com.io7m.jcanephora.ArrayBufferUsableType;
import com.io7m.jcanephora.JCGLExceptionWrongContext;
import com.io7m.jequality.annotations.EqualityReference;
import com.io7m.junreachable.UnreachableCodeException;

/**
 * <p>
 * Functions for checking whether objects are usable on the current context.
 * </p>
 * <p>
 * For an arbitrary <i>shared</i> OpenGL object <code>o</code>, the context
 * <code>C</code> upon which <code>o</code> was created, and an arbitrary
 * object <code>D</code>, <code>o</code> can be passed to functions on
 * <code>D</code> iff:
 * </p>
 * <ul>
 * <li><code>C</code> is equal to <code>D</code>.</li>
 * <li><code>C</code> is shared with <code>D</code> (sharing is symmetric).</li>
 * </ul>
 * <p>
 * For an arbitrary <i>unshared</i> OpenGL object <code>o</code>, the context
 * <code>C</code> upon which <code>o</code> was created, and an arbitrary
 * object <code>D</code>, <code>o</code> can be passed to functions on
 * <code>D</code> iff:
 * </p>
 * <ul>
 * <li><code>C</code> is equal to <code>D</code>.</li>
 * </ul>
 * 
 * @see JOGLObjectShared
 * @see JOGLObjectUnshared
 */

@EqualityReference @SuppressWarnings("null") final class JOGLObjectChecks
{
  /**
   * Check if an arbitrary array can be used on the given context.
   * 
   * @param current
   *          The context
   * @param x
   *          The array
   * @throws JCGLExceptionWrongContext
   *           If the object cannot be used.
   */

  public static void checkArray(
    final GLContext current,
    final ArrayBufferUsableType x)
    throws JCGLExceptionWrongContext
  {
    if (x instanceof JOGLObjectShared) {
      JOGLObjectChecks.checkObjectShared(current, (JOGLObjectShared) x);
    }

    if (x instanceof JOGLObjectUnshared) {
      JOGLObjectChecks.checkObjectUnshared(current, (JOGLObjectUnshared) x);
    }

    if (x instanceof JOGLObjectPseudo) {
      JOGLObjectChecks.checkObjectPseudo(current, (JOGLObjectPseudo) x);
    }

    final String r =
      String
        .format(
          "Object cannot be used: The object %s was not created on this context",
          x);
    assert r != null;
    throw new JCGLExceptionWrongContext(r);
  }

  private static void checkObjectPseudo(
    final GLContext current,
    final JOGLObjectPseudo x)
    throws JCGLExceptionWrongContext
  {
    final GLContext target = x.getContext();
    if (current.equals(target)) {
      return;
    }

    throw new JCGLExceptionWrongContext(
      String
        .format(
          "Object cannot be used: Current context %s is not equal to object's context %s",
          current,
          target));
  }

  private static void checkObjectShared(
    final GLContext current,
    final JOGLObjectShared x)
    throws JCGLExceptionWrongContext
  {
    final GLContext target = x.getContext();
    if (current.equals(target)) {
      return;
    }

    /**
     * Sharing is symmetric.
     */

    if (current.isShared()) {
      final boolean ok = current.getCreatedShares().contains(target);
      if (ok) {
        return;
      }

      throw new JCGLExceptionWrongContext(
        String
          .format(
            "Object cannot be used: Current context %s is shared, but is not shared with object's context %s",
            current,
            target));
    }

    throw new JCGLExceptionWrongContext(
      String
        .format(
          "Object cannot be used: Current context %s is not shared, and is not equal to object's context %s",
          current,
          target));
  }

  private static void checkObjectUnshared(
    final GLContext current,
    final JOGLObjectUnshared x)
    throws JCGLExceptionWrongContext
  {
    final GLContext target = x.getContext();
    if (current.equals(target)) {
      return;
    }

    throw new JCGLExceptionWrongContext(
      String
        .format(
          "Object cannot be used: Current context %s is not equal to object's context %s",
          current,
          target));
  }

  private JOGLObjectChecks()
  {
    throw new UnreachableCodeException();
  }
}
