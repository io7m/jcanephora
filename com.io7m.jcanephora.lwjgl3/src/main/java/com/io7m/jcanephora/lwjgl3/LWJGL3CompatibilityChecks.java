/*
 * Copyright © 2016 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.lwjgl3;

import com.io7m.jcanephora.core.JCGLExceptionWrongContext;
import com.io7m.junreachable.UnreachableCodeException;

// @formatter:off

/**
 * <p>Functions for checking whether objects are usable on the current context.
 * </p>
 *
 * <p> For an arbitrary <i>shared</i> OpenGL object {@code o}, the context
 * {@code C} upon which {@code o} was created, and an arbitrary object {@code
 * D}, {@code o} can be passed to functions on {@code D} iff:</p>
 *
 * <ul>
 * <li>{@code C} is equal to {@code D}.</li>
 * <li>{@code C} is shared with {@code D} (sharing is symmetric).</li>
 * </ul>
 *
 * <p>For an arbitrary <i>unshared</i>
 * OpenGL object {@code o}, the context {@code C} upon which {@code o} was
 * created, and an arbitrary object {@code D}, {@code o} can be passed to
 * functions on {@code D} iff:</p>
 *
 * <ul>
 * <li>{@code C} is equal to {@code D}.</li>
 * </ul>
 *
 * @see LWJGL3ObjectShared
 * @see LWJGL3ObjectUnshared
 */

// @formatter:on

final class LWJGL3CompatibilityChecks
{
  private LWJGL3CompatibilityChecks()
  {
    throw new UnreachableCodeException();
  }

  @SuppressWarnings("unchecked")
  static <A> A checkAny(
    final LWJGL3Context current,
    final A x)
    throws JCGLExceptionWrongContext
  {
    if (x instanceof LWJGL3ObjectShared) {
      return (A) LWJGL3CompatibilityChecks.checkObjectShared(
        current, (LWJGL3ObjectShared) x);
    }

    if (x instanceof LWJGL3ObjectUnshared) {
      return (A) LWJGL3CompatibilityChecks.checkObjectUnshared(
        current, (LWJGL3ObjectUnshared) x);
    }

    if (x instanceof LWJGL3ObjectPseudoUnshared) {
      return (A) LWJGL3CompatibilityChecks.checkObjectPseudoUnshared(
        current, (LWJGL3ObjectPseudoUnshared) x);
    }

    final String r = String.format(
      "Object cannot be used: The object %s was not created on this context",
      x);
    assert r != null;
    throw new JCGLExceptionWrongContext(r);
  }

  static <A extends LWJGL3ObjectPseudoUnshared> A
  checkObjectPseudoUnshared(
    final LWJGL3Context current,
    final A x)
    throws JCGLExceptionWrongContext
  {
    final LWJGL3Context target = x.getContext();
    if (current.equals(target)) {
      return x;
    }

    throw new JCGLExceptionWrongContext(
      String.format(
        "Object cannot be used: Current context %s is not equal to object's "
          + "context %s", current, target));
  }

  private static <A extends LWJGL3ObjectShared> A checkObjectShared(
    final LWJGL3Context current,
    final A x)
    throws JCGLExceptionWrongContext
  {
    final LWJGL3Context target = x.getContext();
    if (current.equals(target)) {
      return x;
    }

    /**
     * Sharing is symmetric.
     */

    if (current.isShared()) {
      final boolean ok = current.contextGetShares().contains(target);
      if (ok) {
        return x;
      }

      throw new JCGLExceptionWrongContext(
        String.format(
          "Object cannot be used: Current context %s is shared, but is not "
            + "shared with object's context %s", current, target));
    }

    throw new JCGLExceptionWrongContext(
      String.format(
        "Object cannot be used: Current context %s is not shared, and is not "
          + "equal to object's context %s", current, target));
  }

  private static <A extends LWJGL3ObjectUnshared> A checkObjectUnshared(
    final LWJGL3Context current,
    final A x)
    throws JCGLExceptionWrongContext
  {
    final LWJGL3Context target = x.getContext();
    if (current.equals(target)) {
      return x;
    }

    throw new JCGLExceptionWrongContext(
      String.format(
        "Object cannot be used: Current context %s is not equal to object's "
          + "context %s", current, target));
  }

}
