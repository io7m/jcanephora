/*
 * Copyright © 2015 <code@io7m.com> http://io7m.com
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

import com.io7m.jcanephora.core.JCGLArrayBufferUsableType;
import com.io7m.jcanephora.core.JCGLArrayObjectBuilderType;
import com.io7m.jcanephora.core.JCGLArrayObjectUsableType;
import com.io7m.jcanephora.core.JCGLArrayVertexAttributeType;
import com.io7m.jcanephora.core.JCGLExceptionWrongContext;
import com.io7m.jcanephora.core.JCGLFragmentShaderUsableType;
import com.io7m.jcanephora.core.JCGLGeometryShaderUsableType;
import com.io7m.jcanephora.core.JCGLProgramShaderUsableType;
import com.io7m.jcanephora.core.JCGLVertexShaderUsableType;
import com.io7m.jnull.NullCheck;
import com.io7m.junreachable.UnreachableCodeException;
import com.jogamp.opengl.GLContext;

// @formatter:off

/**
 * <p>
 * Functions for checking whether objects are usable on the current context.
 * </p>
 * <p>
 * For an arbitrary <i>shared</i> OpenGL object {@code o}, the context
 * {@code C} upon which {@code o} was created, and an arbitrary
 * object {@code D}, {@code o} can be passed to functions on
 * {@code D} iff:
 * </p>
 * <ul>
 * <li>{@code C} is equal to {@code D}.</li>
 * <li>{@code C} is shared with {@code D} (sharing is symmetric).</li>
 * </ul>
 * <p>
 * For an arbitrary <i>unshared</i> OpenGL object {@code o}, the context
 * {@code C} upon which {@code o} was created, and an arbitrary
 * object {@code D}, {@code o} can be passed to functions on
 * {@code D} iff:
 * </p>
 * <ul>
 * <li>{@code C} is equal to {@code D}.</li>
 * </ul>
 *
 * @see JOGLObjectShared
 * @see JOGLObjectUnshared
 */

// @formatter:on

final class JOGLCompatibilityChecks
{
  private JOGLCompatibilityChecks()
  {
    throw new UnreachableCodeException();
  }

  @SuppressWarnings("unchecked") private static <A> A checkAny(
    final GLContext current,
    final A x)
    throws JCGLExceptionWrongContext
  {
    if (x instanceof JOGLObjectShared) {
      return (A) JOGLCompatibilityChecks.checkObjectShared(
        current, (JOGLObjectShared) x);
    }

    if (x instanceof JOGLObjectUnshared) {
      return (A) JOGLCompatibilityChecks.checkObjectUnshared(
        current, (JOGLObjectUnshared) x);
    }

    if (x instanceof JOGLObjectPseudoUnshared) {
      return (A) JOGLCompatibilityChecks.checkObjectPseudoUnshared(
        current, (JOGLObjectPseudoUnshared) x);
    }

    final String r = String.format(
      "Object cannot be used: The object %s was not created on this context",
      x);
    assert r != null;
    throw new JCGLExceptionWrongContext(r);
  }

  private static <A extends JOGLObjectPseudoUnshared> A
  checkObjectPseudoUnshared(
    final GLContext current,
    final A x)
    throws JCGLExceptionWrongContext
  {
    final GLContext target = x.getContext();
    if (current.equals(target)) {
      return x;
    }

    throw new JCGLExceptionWrongContext(
      String.format(
        "Object cannot be used: Current context %s is not equal to object's "
        + "context %s", current, target));
  }

  public static void checkArray(
    final GLContext current,
    final JCGLArrayBufferUsableType x)
    throws JCGLExceptionWrongContext
  {
    NullCheck.notNull(x);
    JOGLCompatibilityChecks.checkAny(current, x);
  }

  private static <A extends JOGLObjectShared> A checkObjectShared(
    final GLContext current,
    final A x)
    throws JCGLExceptionWrongContext
  {
    final GLContext target = x.getContext();
    if (current.equals(target)) {
      return x;
    }

    /**
     * Sharing is symmetric.
     */

    if (current.isShared()) {
      final boolean ok = current.getCreatedShares().contains(target);
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

  private static <A extends JOGLObjectUnshared> A checkObjectUnshared(
    final GLContext current,
    final A x)
    throws JCGLExceptionWrongContext
  {
    final GLContext target = x.getContext();
    if (current.equals(target)) {
      return x;
    }

    throw new JCGLExceptionWrongContext(
      String.format(
        "Object cannot be used: Current context %s is not equal to object's "
        + "context %s", current, target));
  }

  public static void checkArrayObjectBuilder(
    final GLContext context,
    final JCGLArrayObjectBuilderType b)
    throws JCGLExceptionWrongContext
  {
    NullCheck.notNull(context);
    NullCheck.notNull(b);
    JOGLCompatibilityChecks.checkAny(context, b);
  }

  public static void checkArrayAttribute(
    final GLContext c,
    final JCGLArrayVertexAttributeType a)
  {
    JOGLCompatibilityChecks.checkAny(c, a);
  }

  public static void checkArrayObject(
    final GLContext c,
    final JCGLArrayObjectUsableType a)
  {
    JOGLCompatibilityChecks.checkAny(c, a);
  }

  public static void checkVertexShader(
    final GLContext c,
    final JCGLVertexShaderUsableType v)
  {
    JOGLCompatibilityChecks.checkAny(c, v);
  }

  public static void checkFragmentShader(
    final GLContext c,
    final JCGLFragmentShaderUsableType f)
  {
    JOGLCompatibilityChecks.checkAny(c, f);
  }

  public static void checkGeometryShader(
    final GLContext c,
    final JCGLGeometryShaderUsableType gg)
  {
    JOGLCompatibilityChecks.checkAny(c, gg);
  }

  public static void checkProgramShader(
    final GLContext c,
    final JCGLProgramShaderUsableType p)
  {
    JOGLCompatibilityChecks.checkAny(c, p);
  }
}
