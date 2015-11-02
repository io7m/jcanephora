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

package com.io7m.jcanephora.fake;

import com.io7m.jcanephora.core.JCGLArrayBufferUsableType;
import com.io7m.jcanephora.core.JCGLArrayObjectBuilderType;
import com.io7m.jcanephora.core.JCGLArrayObjectUsableType;
import com.io7m.jcanephora.core.JCGLArrayVertexAttributeType;
import com.io7m.jcanephora.core.JCGLExceptionWrongContext;
import com.io7m.jcanephora.core.JCGLFragmentShaderUsableType;
import com.io7m.jcanephora.core.JCGLGeometryShaderUsableType;
import com.io7m.jcanephora.core.JCGLIndexBufferUsableType;
import com.io7m.jcanephora.core.JCGLProgramShaderUsableType;
import com.io7m.jcanephora.core.JCGLVertexShaderUsableType;
import com.io7m.jnull.NullCheck;
import com.io7m.junreachable.UnreachableCodeException;

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
 * @see FakeObjectShared
 * @see FakeObjectUnshared
 */

// @formatter:on

final class FakeCompatibilityChecks
{
  private FakeCompatibilityChecks()
  {
    throw new UnreachableCodeException();
  }

  @SuppressWarnings("unchecked") private static <A> A checkAny(
    final FakeContext current,
    final A x)
    throws JCGLExceptionWrongContext
  {
    if (x instanceof FakeObjectShared) {
      return (A) FakeCompatibilityChecks.checkObjectShared(
        current, (FakeObjectShared) x);
    }

    if (x instanceof FakeObjectUnshared) {
      return (A) FakeCompatibilityChecks.checkObjectUnshared(
        current, (FakeObjectUnshared) x);
    }

    if (x instanceof FakeObjectPseudoUnshared) {
      return (A) FakeCompatibilityChecks.checkObjectPseudoUnshared(
        current, (FakeObjectPseudoUnshared) x);
    }

    final String r = String.format(
      "Object cannot be used: The object %s was not created on this context",
      x);
    assert r != null;
    throw new JCGLExceptionWrongContext(r);
  }

  private static <A extends FakeObjectPseudoUnshared> A
  checkObjectPseudoUnshared(
    final FakeContext current,
    final A x)
    throws JCGLExceptionWrongContext
  {
    final FakeContext target = x.getContext();
    if (current.equals(target)) {
      return x;
    }

    throw new JCGLExceptionWrongContext(
      String.format(
        "Object cannot be used: Current context %s is not equal to object's "
        + "context %s", current, target));
  }

  public static void checkArrayBuffer(
    final FakeContext current,
    final JCGLArrayBufferUsableType x)
    throws JCGLExceptionWrongContext
  {
    NullCheck.notNull(x);
    FakeCompatibilityChecks.checkAny(current, x);
  }

  public static void checkIndexBuffer(
    final FakeContext current,
    final JCGLIndexBufferUsableType x)
    throws JCGLExceptionWrongContext
  {
    NullCheck.notNull(x);
    FakeCompatibilityChecks.checkAny(current, x);
  }

  public static void checkArrayObjectBuilder(
    final FakeContext current,
    final JCGLArrayObjectBuilderType x)
    throws JCGLExceptionWrongContext
  {
    NullCheck.notNull(x);
    FakeCompatibilityChecks.checkAny(current, x);
  }

  private static <A extends FakeObjectShared> A checkObjectShared(
    final FakeContext current,
    final A x)
    throws JCGLExceptionWrongContext
  {
    final FakeContext target = x.getContext();
    if (current.equals(target)) {
      return x;
    }

    /**
     * Sharing is symmetric.
     */

    if (current.contextIsSharedWith(target)) {
      return x;
    }

    throw new JCGLExceptionWrongContext(
      String.format(
        "Object cannot be used: Current context %s is not shared with "
        + "object's context %s", current, target));
  }

  private static <A extends FakeObjectUnshared> A checkObjectUnshared(
    final FakeContext current,
    final A x)
    throws JCGLExceptionWrongContext
  {
    final FakeContext target = x.getContext();
    if (current.equals(target)) {
      return x;
    }

    throw new JCGLExceptionWrongContext(
      String.format(
        "Object cannot be used: Current context %s is not equal to object's "
        + "context %s", current, target));
  }

  public static void checkArrayAttribute(
    final FakeContext c,
    final JCGLArrayVertexAttributeType attrib)
  {
    FakeCompatibilityChecks.checkAny(c, attrib);
  }

  public static void checkArrayObject(
    final FakeContext context,
    final JCGLArrayObjectUsableType a)
  {
    FakeCompatibilityChecks.checkAny(context, a);
  }

  public static FakeVertexShader checkVertexShader(
    final FakeContext c,
    final JCGLVertexShaderUsableType v)
  {
    return (FakeVertexShader) FakeCompatibilityChecks.checkAny(c, v);
  }

  public static FakeFragmentShader checkFragmentShader(
    final FakeContext c,
    final JCGLFragmentShaderUsableType f)
  {
    return (FakeFragmentShader) FakeCompatibilityChecks.checkAny(c, f);
  }

  public static FakeGeometryShader checkGeometryShader(
    final FakeContext c,
    final JCGLGeometryShaderUsableType g)
  {
    return (FakeGeometryShader) FakeCompatibilityChecks.checkAny(c, g);
  }

  public static FakeProgramShader checkProgramShader(
    final FakeContext c,
    final JCGLProgramShaderUsableType p)
  {
    return (FakeProgramShader) FakeCompatibilityChecks.checkAny(c, p);
  }
}
