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
import com.io7m.jcanephora.core.JCGLFramebufferBuilderType;
import com.io7m.jcanephora.core.JCGLFramebufferColorAttachmentPointType;
import com.io7m.jcanephora.core.JCGLFramebufferDrawBufferType;
import com.io7m.jcanephora.core.JCGLFramebufferUsableType;
import com.io7m.jcanephora.core.JCGLGeometryShaderUsableType;
import com.io7m.jcanephora.core.JCGLIndexBufferUsableType;
import com.io7m.jcanephora.core.JCGLProgramShaderUsableType;
import com.io7m.jcanephora.core.JCGLTexture2DUsableType;
import com.io7m.jcanephora.core.JCGLTextureCubeUsableType;
import com.io7m.jcanephora.core.JCGLTextureUnitType;
import com.io7m.jcanephora.core.JCGLTimerQueryUsableType;
import com.io7m.jcanephora.core.JCGLVertexShaderUsableType;
import com.io7m.jnull.NullCheck;
import com.io7m.junreachable.UnreachableCodeException;

import java.util.Objects;

// @formatter:off

/**
 * <p> Functions for checking whether objects are usable on the current context.
 * </p> <p> For an arbitrary <i>shared</i> OpenGL object {@code o}, the context
 * {@code C} upon which {@code o} was created, and an arbitrary object {@code
 * D}, {@code o} can be passed to functions on {@code D} iff: </p> <ul>
 * <li>{@code C} is equal to {@code D}.</li> <li>{@code C} is shared with {@code
 * D} (sharing is symmetric).</li> </ul> <p> For an arbitrary <i>unshared</i>
 * OpenGL object {@code o}, the context {@code C} upon which {@code o} was
 * created, and an arbitrary object {@code D}, {@code o} can be passed to
 * functions on {@code D} iff: </p> <ul> <li>{@code C} is equal to {@code
 * D}.</li> </ul>
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

  @SuppressWarnings("unchecked")
  private static <A> A checkAny(
    final FakeContext current,
    final A x)
    throws JCGLExceptionWrongContext
  {
    if (x instanceof FakeObjectShared) {
      return (A) checkObjectShared(
        current, (FakeObjectShared) x);
    }

    if (x instanceof FakeObjectUnshared) {
      return (A) checkObjectUnshared(
        current, (FakeObjectUnshared) x);
    }

    if (x instanceof FakeObjectPseudoUnshared) {
      return (A) checkObjectPseudoUnshared(
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
    if (Objects.equals(current, target)) {
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
    NullCheck.notNull(x, "Array");
    checkAny(current, x);
  }

  public static void checkIndexBuffer(
    final FakeContext current,
    final JCGLIndexBufferUsableType x)
    throws JCGLExceptionWrongContext
  {
    NullCheck.notNull(x, "Array");
    checkAny(current, x);
  }

  public static void checkArrayObjectBuilder(
    final FakeContext current,
    final JCGLArrayObjectBuilderType x)
    throws JCGLExceptionWrongContext
  {
    NullCheck.notNull(x, "Array");
    checkAny(current, x);
  }

  private static <A extends FakeObjectShared> A checkObjectShared(
    final FakeContext current,
    final A x)
    throws JCGLExceptionWrongContext
  {
    final FakeContext target = x.getContext();
    if (Objects.equals(current, target)) {
      return x;
    }

    /*
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
    if (Objects.equals(current, target)) {
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
    checkAny(c, attrib);
  }

  public static void checkArrayObject(
    final FakeContext context,
    final JCGLArrayObjectUsableType a)
  {
    checkAny(context, a);
  }

  public static FakeVertexShader checkVertexShader(
    final FakeContext c,
    final JCGLVertexShaderUsableType v)
  {
    return (FakeVertexShader) checkAny(c, v);
  }

  public static FakeFragmentShader checkFragmentShader(
    final FakeContext c,
    final JCGLFragmentShaderUsableType f)
  {
    return (FakeFragmentShader) checkAny(c, f);
  }

  public static FakeGeometryShader checkGeometryShader(
    final FakeContext c,
    final JCGLGeometryShaderUsableType g)
  {
    return (FakeGeometryShader) checkAny(c, g);
  }

  public static FakeProgramShader checkProgramShader(
    final FakeContext c,
    final JCGLProgramShaderUsableType p)
  {
    return (FakeProgramShader) checkAny(c, p);
  }

  public static FakeTextureUnit checkTextureUnit(
    final FakeContext c,
    final JCGLTextureUnitType u)
  {
    return (FakeTextureUnit) checkAny(c, u);
  }

  public static FakeTexture2D checkTexture2D(
    final FakeContext c,
    final JCGLTexture2DUsableType t)
  {
    return (FakeTexture2D) checkAny(c, t);
  }

  public static FakeTextureCube checkTextureCube(
    final FakeContext c,
    final JCGLTextureCubeUsableType t)
  {
    return (FakeTextureCube) checkAny(c, t);
  }

  public static void checkFramebufferBuilder(
    final FakeContext c,
    final JCGLFramebufferBuilderType b)
  {
    checkAny(c, b);
  }

  public static void checkFramebufferColorAttachmentPoint(
    final FakeContext c,
    final JCGLFramebufferColorAttachmentPointType p)
  {
    checkAny(c, p);
  }

  public static void checkDrawBuffer(
    final FakeContext c,
    final JCGLFramebufferDrawBufferType b)
  {
    checkAny(c, b);
  }

  public static void checkFramebuffer(
    final FakeContext c,
    final JCGLFramebufferUsableType fb)
  {
    checkAny(c, fb);
  }

  public static FakeTimerQuery checkTimerQuery(
    final FakeContext c,
    final JCGLTimerQueryUsableType q)
  {
    return (FakeTimerQuery) checkAny(c, q);
  }
}
