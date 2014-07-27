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

package com.io7m.jcanephora.fake;

import com.io7m.jcanephora.ArrayBufferUsableType;
import com.io7m.jcanephora.FragmentShaderType;
import com.io7m.jcanephora.FramebufferColorAttachmentPointType;
import com.io7m.jcanephora.FramebufferDrawBufferType;
import com.io7m.jcanephora.FramebufferUsableType;
import com.io7m.jcanephora.IndexBufferUsableType;
import com.io7m.jcanephora.JCGLExceptionWrongContext;
import com.io7m.jcanephora.ProgramAttributeType;
import com.io7m.jcanephora.ProgramUniformType;
import com.io7m.jcanephora.ProgramUsableType;
import com.io7m.jcanephora.RenderbufferKind;
import com.io7m.jcanephora.RenderbufferUsableType;
import com.io7m.jcanephora.Texture2DStaticUsableType;
import com.io7m.jcanephora.TextureCubeStaticUsableType;
import com.io7m.jcanephora.TextureUnitType;
import com.io7m.jcanephora.VertexShaderType;
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
 * @see FakeObjectShared
 * @see FakeObjectUnshared
 */

@EqualityReference @SuppressWarnings("null") final class FakeCompatibilityChecks
{
  @SuppressWarnings("unchecked") private static <A> A checkAny(
    final FakeContext current,
    final A x)
    throws JCGLExceptionWrongContext
  {
    if (x instanceof FakeObjectShared) {
      return (A) FakeCompatibilityChecks.checkObjectShared(
        current,
        (FakeObjectShared) x);
    }

    if (x instanceof FakeObjectUnshared) {
      return (A) FakeCompatibilityChecks.checkObjectUnshared(
        current,
        (FakeObjectUnshared) x);
    }

    if (x instanceof FakeObjectPseudoUnshared) {
      return (A) FakeCompatibilityChecks.checkObjectPseudoUnshared(
        current,
        (FakeObjectPseudoUnshared) x);
    }

    if (x instanceof FakeObjectPseudoShared) {
      return (A) FakeCompatibilityChecks.checkObjectPseudoShared(
        current,
        (FakeObjectPseudoShared) x);
    }

    final String r =
      String
        .format(
          "Object cannot be used: The object %s was not created on this context",
          x);
    assert r != null;
    throw new JCGLExceptionWrongContext(r);
  }

  public static void checkArray(
    final FakeContext current,
    final ArrayBufferUsableType x)
    throws JCGLExceptionWrongContext
  {
    FakeCompatibilityChecks.checkAny(current, x);
  }

  public static void checkColorAttachmentPoint(
    final FakeContext ctx,
    final FramebufferColorAttachmentPointType b)
    throws JCGLExceptionWrongContext
  {
    FakeCompatibilityChecks.checkAny(ctx, b);
  }

  public static void checkDrawBuffer(
    final FakeContext ctx,
    final FramebufferDrawBufferType b)
    throws JCGLExceptionWrongContext
  {
    FakeCompatibilityChecks.checkAny(ctx, b);
  }

  public static void checkFragmentShader(
    final FakeContext ctx,
    final FragmentShaderType x)
    throws JCGLExceptionWrongContext
  {
    FakeCompatibilityChecks.checkAny(ctx, x);
  }

  public static void checkFramebuffer(
    final FakeContext ctx,
    final FramebufferUsableType framebuffer)
    throws JCGLExceptionWrongContext
  {
    FakeCompatibilityChecks.checkAny(ctx, framebuffer);
  }

  public static void checkIndexBuffer(
    final FakeContext current,
    final IndexBufferUsableType x)
    throws JCGLExceptionWrongContext
  {
    FakeCompatibilityChecks.checkAny(current, x);
  }

  private static
    <A extends FakeObjectPseudoShared>
    A
    checkObjectPseudoShared(
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

    if (current.isShared()) {
      final boolean ok = current.getCreatedShares().contains(target);
      if (ok) {
        return x;
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

  private static
    <A extends FakeObjectPseudoUnshared>
    A
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
      String
        .format(
          "Object cannot be used: Current context %s is not equal to object's context %s",
          current,
          target));
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

    if (current.isShared()) {
      final boolean ok = current.getCreatedShares().contains(target);
      if (ok) {
        return x;
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
      String
        .format(
          "Object cannot be used: Current context %s is not equal to object's context %s",
          current,
          target));
  }

  public static void checkProgram(
    final FakeContext current,
    final ProgramUsableType x)
    throws JCGLExceptionWrongContext
  {
    FakeCompatibilityChecks.checkAny(current, x);
  }

  public static void checkProgramAttribute(
    final FakeContext ctx,
    final ProgramAttributeType x)
    throws JCGLExceptionWrongContext
  {
    FakeCompatibilityChecks.checkAny(ctx, x);
  }

  public static void checkProgramUniform(
    final FakeContext ctx,
    final ProgramUniformType x)
    throws JCGLExceptionWrongContext
  {
    FakeCompatibilityChecks.checkAny(ctx, x);
  }

  public static <K extends RenderbufferKind> void checkRenderbuffer(
    final FakeContext ctx,
    final RenderbufferUsableType<K> r)
    throws JCGLExceptionWrongContext
  {
    FakeCompatibilityChecks.checkAny(ctx, r);
  }

  public static void checkTexture(
    final FakeContext ctx,
    final Texture2DStaticUsableType x)
    throws JCGLExceptionWrongContext
  {
    FakeCompatibilityChecks.checkAny(ctx, x);
  }

  public static void checkTextureCube(
    final FakeContext ctx,
    final TextureCubeStaticUsableType x)
    throws JCGLExceptionWrongContext
  {
    FakeCompatibilityChecks.checkAny(ctx, x);
  }

  public static void checkTextureUnit(
    final FakeContext ctx,
    final TextureUnitType u)
    throws JCGLExceptionWrongContext
  {
    FakeCompatibilityChecks.checkAny(ctx, u);
  }

  public static void checkVertexShader(
    final FakeContext ctx,
    final VertexShaderType x)
    throws JCGLExceptionWrongContext
  {
    FakeCompatibilityChecks.checkAny(ctx, x);
  }

  private FakeCompatibilityChecks()
  {
    throw new UnreachableCodeException();
  }
}
