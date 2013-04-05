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

import org.lwjgl.opengl.GL11;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jaux.functional.Option;
import com.io7m.jaux.functional.Pair;
import com.io7m.jlog.Log;

public final class GLImplementationLWJGL implements GLImplementation
{
  private static boolean isGLES2(
    final @Nonnull String version)
  {
    if (GLES2Functions.metaVersionIsES(version)) {
      final Pair<Integer, Integer> p =
        GLES2Functions.metaParseVersion(version);
      return p.first.intValue() == 2;
    }

    return false;
  }

  private static boolean isGLES3OrNewer(
    final @Nonnull String version)
  {
    if (GLES2Functions.metaVersionIsES(version)) {
      final Pair<Integer, Integer> p =
        GLES2Functions.metaParseVersion(version);
      return p.first.intValue() >= 3;
    }

    return false;
  }

  private static boolean isOpenGL21OrNewer(
    final @Nonnull String version)
  {
    final Pair<Integer, Integer> p = GLES2Functions.metaParseVersion(version);

    if (p.first.intValue() == 2) {
      return p.second.intValue() == 1;
    }

    return p.first.intValue() >= 3;
  }

  private final @Nonnull Log              log;
  private final @Nonnull GLInterfaceGLES2 gl_es2;
  private final @Nonnull GLInterfaceGL3   gl_3;

  public GLImplementationLWJGL(
    final @Nonnull Log log)
    throws ConstraintError,
      GLException,
      GLUnsupportedException
  {
    this.log =
      new Log(Constraints.constrainNotNull(log, "log output"), "LWJGL30");

    final String version = GL11.glGetString(GL11.GL_VERSION);
    log.debug("Context is " + version);

    if (GLImplementationLWJGL.isGLES2(version)) {
      log.debug("Creating GLES2 interface");
      this.gl_es2 = new GLInterfaceGLES2_LWJGL_ES2(log);
      this.gl_3 = null;
      return;
    }

    if (GLImplementationLWJGL.isGLES3OrNewer(version)
      || GLImplementationLWJGL.isOpenGL21OrNewer(version)) {
      log.debug("Creating OpenGL 3 interface");
      this.gl_3 = new GLInterfaceGL3_LWJGL_GL3(log);
      this.gl_es2 = null;
      return;
    }

    throw new GLUnsupportedException(
      "At least OpenGL 2.1 or OpenGL ES2 is required");
  }

  @Override public @Nonnull Option<GLInterfaceGLES2> getGLES2()
  {
    if (this.gl_es2 != null) {
      return new Option.Some<GLInterfaceGLES2>(this.gl_es2);
    }
    return new Option.None<GLInterfaceGLES2>();
  }

  @Override public @Nonnull Option<GLInterfaceGL3> getGL3()
  {
    if (this.gl_3 != null) {
      return new Option.Some<GLInterfaceGL3>(this.gl_3);
    }
    return new Option.None<GLInterfaceGL3>();
  }

  @Override public @Nonnull GLInterfaceCommon getGLCommon()
  {
    if (this.gl_es2 != null) {
      return this.gl_es2;
    }
    if (this.gl_3 != null) {
      return this.gl_3;
    }

    throw new UnreachableCodeException();
  }
}
