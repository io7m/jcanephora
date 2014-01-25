/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
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

import java.util.Set;
import java.util.TreeSet;

import javax.annotation.Nonnull;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jaux.functional.Pair;
import com.io7m.jlog.Log;

/**
 * A LWJGL-based implementation of the <code>jcanephora</code> API.
 */

public final class JCGLImplementationLWJGL implements JCGLImplementation
{
  private static final @Nonnull JCGLSoftRestrictions DEFAULT_RESTRICTIONS;

  static {
    DEFAULT_RESTRICTIONS = new JCGLSoftRestrictions() {
      @Override public boolean restrictExtensionVisibility(
        final @Nonnull String name)
      {
        return true;
      }

      @Override public int restrictTextureUnitCount(
        final int count)
      {
        return count;
      }
    };
  }

  static void checkFBOSupport(
    final @Nonnull Log log,
    final @Nonnull Set<String> extensions)
    throws JCGLUnsupportedException
  {
    if (extensions.contains(JCGLExtensionNames.GL_ARB_FRAMEBUFFER_OBJECT) == false) {
      log
        .debug("GL_ARB_framebuffer_object not supported, checking for EXT extensions");

      if (extensions.contains(JCGLExtensionNames.GL_EXT_FRAMEBUFFER_OBJECT) == false) {
        throw new JCGLUnsupportedException(
          "Context supports OpenGL 2.1 but does not support the required GL_EXT_framebuffer_object extension");
      }
      if (extensions
        .contains(JCGLExtensionNames.GL_EXT_FRAMEBUFFER_MULTISAMPLE) == false) {
        throw new JCGLUnsupportedException(
          "Context supports OpenGL 2.1 but does not support the required GL_EXT_framebuffer_multisample extension");
      }
      if (extensions.contains(JCGLExtensionNames.GL_EXT_FRAMEBUFFER_BLIT) == false) {
        throw new JCGLUnsupportedException(
          "Context supports OpenGL 2.1 but does not support the required GL_EXT_framebuffer_blit extension");
      }
      if (extensions.contains(JCGLExtensionNames.GL_EXT_PACKED_DEPTH_STENCIL) == false) {
        throw new JCGLUnsupportedException(
          "Context supports OpenGL 2.1 but does not support the required GL_EXT_packed_depth_stencil extension");
      }

      log
        .debug("(EXT_framebuffer_object|EXT_framebuffer_multisample|EXT_framebuffer_blit|GL_EXT_packed_depth_stencil) supported");
    } else {
      log.debug("ARB_framebuffer_object supported");
    }
  }

  private static Set<String> getExtensionsGL3p()
  {
    final TreeSet<String> ext_set = new TreeSet<String>();
    final int count = GL11.glGetInteger(GL30.GL_NUM_EXTENSIONS);
    for (int index = 0; index < count; ++index) {
      final String name = GL30.glGetStringi(GL11.GL_EXTENSIONS, index);
      ext_set.add(name);
    }
    return ext_set;
  }

  private static Set<String> getExtensionsGL21_30()
  {
    final String eraw = GL11.glGetString(GL11.GL_EXTENSIONS);
    final String[] exts = eraw.split(" ");
    final TreeSet<String> ext_set = new TreeSet<String>();

    for (final String e : exts) {
      ext_set.add(e);
    }

    return ext_set;
  }

  private static boolean isGL2(
    final @Nonnull String version)
  {
    if (LWJGL_GLES2Functions.metaVersionIsES(version) == false) {
      final Pair<Integer, Integer> p =
        LWJGL_GLES2Functions.metaParseVersion(version);
      return (p.first.intValue() == 2) && (p.second.intValue() == 1);
    }

    return false;
  }

  private static boolean isGL3(
    final @Nonnull String version)
  {
    if (LWJGL_GLES2Functions.metaVersionIsES(version) == false) {
      final Pair<Integer, Integer> p =
        LWJGL_GLES2Functions.metaParseVersion(version);
      return (p.first.intValue() >= 3) && (p.second.intValue() >= 0);
    }

    return false;
  }

  private static boolean isGLES2(
    final @Nonnull String version)
  {
    if (LWJGL_GLES2Functions.metaVersionIsES(version)) {
      final Pair<Integer, Integer> p =
        LWJGL_GLES2Functions.metaParseVersion(version);
      return p.first.intValue() == 2;
    }

    return false;
  }

  /**
   * Construct an implementation assuming that the LWJGL library has already
   * been initialized.
   * 
   * @throws ConstraintError
   *           Iff <code>log == null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws JCGLUnsupportedException
   *           Iff the given graphics context does not support either of
   *           OpenGL 3.* or ES2.
   */

  public static @Nonnull JCGLImplementationLWJGL newImplementation(
    final @Nonnull Log log)
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    return new JCGLImplementationLWJGL(
      log,
      JCGLImplementationLWJGL.DEFAULT_RESTRICTIONS);
  }

  /**
   * Construct an implementation assuming that the LWJGL library has already
   * been initialized, with restrictions <code>r</code>.
   * 
   * @throws ConstraintError
   *           Iff <code>log == null || r == null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws JCGLUnsupportedException
   *           Iff the given graphics context does not support either of
   *           OpenGL 3.* or ES2.
   */

  public static @Nonnull
    JCGLImplementationLWJGL
    newImplementationWithRestrictions(
      final @Nonnull Log log,
      final @Nonnull JCGLSoftRestrictions r)
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError
  {
    return new JCGLImplementationLWJGL(log, r);
  }

  private final @Nonnull JCGLInterfaceGL2     gl_2;
  private final @Nonnull JCGLInterfaceGL3     gl_3;
  private final @Nonnull JCGLInterfaceGLES2   gl_es2;
  private final @Nonnull Log                  log;
  private final @Nonnull JCGLSoftRestrictions restrictions;

  private JCGLImplementationLWJGL(
    final @Nonnull Log log,
    final @Nonnull JCGLSoftRestrictions r)
    throws ConstraintError,
      JCGLRuntimeException,
      JCGLUnsupportedException
  {
    this.log =
      new Log(Constraints.constrainNotNull(log, "log output"), "lwjgl30");
    this.restrictions = Constraints.constrainNotNull(r, "Restrictions");

    final String vs = GL11.glGetString(GL11.GL_VERSION);

    if (JCGLImplementationLWJGL.isGLES2(vs)) {
      final Set<String> extensions =
        JCGLImplementationLWJGL.getExtensionsGL21_30();
      JCGLImplementationLWJGL.checkFBOSupport(log, extensions);

      log.debug("Context is GLES2 - creating GLES2 interface");
      this.gl_es2 = new JCGLInterfaceGLES2_LWJGL_ES2(log, extensions, r);
      this.gl_2 = null;
      this.gl_3 = null;
      return;
    }

    if (JCGLImplementationLWJGL.isGL2(vs)) {
      final Set<String> extensions =
        JCGLImplementationLWJGL.getExtensionsGL21_30();
      JCGLImplementationLWJGL.checkFBOSupport(log, extensions);

      log.debug("Context is GL2, creating OpenGL 2.1 interface");
      this.gl_2 = new JCGLInterfaceGL2_LWJGL_GL2(log, r, extensions);
      this.gl_3 = null;
      this.gl_es2 = null;
      return;
    }

    if (JCGLImplementationLWJGL.isGL3(vs)) {
      final Set<String> extensions =
        JCGLImplementationLWJGL.getExtensionsGL3p();

      log.debug("Context is GL3, creating OpenGL >= 3.1 interface");
      this.gl_3 = new JCGLInterfaceGL3_LWJGL_GL3(log, extensions, r);
      this.gl_2 = null;
      this.gl_es2 = null;
      return;
    }

    throw new JCGLUnsupportedException(
      "At least OpenGL 2.1 or OpenGL ES2 is required");
  }

  @Override public @Nonnull JCGLInterfaceCommon getGLCommon()
  {
    if (this.gl_es2 != null) {
      return this.gl_es2;
    }
    if (this.gl_2 != null) {
      return this.gl_2;
    }
    if (this.gl_3 != null) {
      return this.gl_3;
    }

    throw new UnreachableCodeException();
  }

  @Override public <A, E extends Throwable> A implementationAccept(
    final @Nonnull JCGLImplementationVisitor<A, E> v)
    throws JCGLException,
      ConstraintError,
      E
  {
    Constraints.constrainNotNull(v, "Visitor");

    if (this.gl_es2 != null) {
      return v.implementationIsGLES2(this.gl_es2);
    }
    if (this.gl_3 != null) {
      return v.implementationIsGL3(this.gl_3);
    }
    if (this.gl_2 != null) {
      return v.implementationIsGL2(this.gl_2);
    }

    throw new UnreachableCodeException();
  }
}
