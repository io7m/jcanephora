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
 * @see JOGLObjectShared
 * @see JOGLObjectUnshared
 */

@EqualityReference @SuppressWarnings("null") final class JOGLCompatibilityChecks
{
  @SuppressWarnings("unchecked") private static <A> A checkAny(
    final GLContext current,
    final A x)
    throws JCGLExceptionWrongContext
  {
    if (x instanceof JOGLObjectShared) {
      return (A) JOGLCompatibilityChecks.checkObjectShared(
        current,
        (JOGLObjectShared) x);
    }

    if (x instanceof JOGLObjectUnshared) {
      return (A) JOGLCompatibilityChecks.checkObjectUnshared(
        current,
        (JOGLObjectUnshared) x);
    }

    if (x instanceof JOGLObjectPseudoUnshared) {
      return (A) JOGLCompatibilityChecks.checkObjectPseudoUnshared(
        current,
        (JOGLObjectPseudoUnshared) x);
    }

    if (x instanceof JOGLObjectPseudoShared) {
      return (A) JOGLCompatibilityChecks.checkObjectPseudoShared(
        current,
        (JOGLObjectPseudoShared) x);
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
    final GLContext current,
    final ArrayBufferUsableType x)
    throws JCGLExceptionWrongContext
  {
    JOGLCompatibilityChecks.checkAny(current, x);
  }

  public static void checkColorAttachmentPoint(
    final GLContext ctx,
    final FramebufferColorAttachmentPointType b)
    throws JCGLExceptionWrongContext
  {
    JOGLCompatibilityChecks.checkAny(ctx, b);
  }

  public static void checkDrawBuffer(
    final GLContext ctx,
    final FramebufferDrawBufferType b)
    throws JCGLExceptionWrongContext
  {
    JOGLCompatibilityChecks.checkAny(ctx, b);
  }

  public static void checkFragmentShader(
    final GLContext ctx,
    final FragmentShaderType x)
    throws JCGLExceptionWrongContext
  {
    JOGLCompatibilityChecks.checkAny(ctx, x);
  }

  public static void checkFramebuffer(
    final GLContext ctx,
    final FramebufferUsableType framebuffer)
    throws JCGLExceptionWrongContext
  {
    JOGLCompatibilityChecks.checkAny(ctx, framebuffer);
  }

  public static void checkIndexBuffer(
    final GLContext current,
    final IndexBufferUsableType x)
    throws JCGLExceptionWrongContext
  {
    JOGLCompatibilityChecks.checkAny(current, x);
  }

  private static
    <A extends JOGLObjectPseudoShared>
    A
    checkObjectPseudoShared(
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
    <A extends JOGLObjectPseudoUnshared>
    A
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
      String
        .format(
          "Object cannot be used: Current context %s is not equal to object's context %s",
          current,
          target));
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
      String
        .format(
          "Object cannot be used: Current context %s is not equal to object's context %s",
          current,
          target));
  }

  public static void checkProgram(
    final GLContext current,
    final ProgramUsableType x)
    throws JCGLExceptionWrongContext
  {
    JOGLCompatibilityChecks.checkAny(current, x);
  }

  public static void checkProgramAttribute(
    final GLContext ctx,
    final ProgramAttributeType x)
    throws JCGLExceptionWrongContext
  {
    JOGLCompatibilityChecks.checkAny(ctx, x);
  }

  public static void checkProgramUniform(
    final GLContext ctx,
    final ProgramUniformType x)
    throws JCGLExceptionWrongContext
  {
    JOGLCompatibilityChecks.checkAny(ctx, x);
  }

  public static <K extends RenderbufferKind> void checkRenderbuffer(
    final GLContext ctx,
    final RenderbufferUsableType<K> r)
    throws JCGLExceptionWrongContext
  {
    JOGLCompatibilityChecks.checkAny(ctx, r);
  }

  public static void checkTexture(
    final GLContext ctx,
    final Texture2DStaticUsableType x)
    throws JCGLExceptionWrongContext
  {
    JOGLCompatibilityChecks.checkAny(ctx, x);
  }

  public static void checkTextureCube(
    final GLContext ctx,
    final TextureCubeStaticUsableType x)
    throws JCGLExceptionWrongContext
  {
    JOGLCompatibilityChecks.checkAny(ctx, x);
  }

  public static void checkTextureUnit(
    final GLContext ctx,
    final TextureUnitType u)
    throws JCGLExceptionWrongContext
  {
    JOGLCompatibilityChecks.checkAny(ctx, u);
  }

  public static void checkVertexShader(
    final GLContext ctx,
    final VertexShaderType x)
    throws JCGLExceptionWrongContext
  {
    JOGLCompatibilityChecks.checkAny(ctx, x);
  }

  private JOGLCompatibilityChecks()
  {
    throw new UnreachableCodeException();
  }
}
