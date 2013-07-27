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

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Option.Some;
import com.io7m.jcanephora.contracts.gles2.TextureCubeStaticGLES2Contract;

public final class JOGLES3TextureCubeStaticGLES2Test extends
  TextureCubeStaticGLES2Contract
{
  @Override public JCGLTexturesCubeStaticGLES2 getGLTextureCubeStatic(
    final TestContext tc)
  {
    final Some<JCGLInterfaceGLES3> some =
      (Some<JCGLInterfaceGLES3>) tc.getGLImplementation().getGLES3();
    return some.value;
  }

  @Override public JCGLTextureUnits getGLTextureUnits(
    final TestContext tc)
  {
    final Some<JCGLInterfaceGLES3> some =
      (Some<JCGLInterfaceGLES3>) tc.getGLImplementation().getGLES3();
    return some.value;
  }

  @Override public boolean isGLSupported()
  {
    return JOGLTestContext.isOpenGLES3Supported();
  }

  @Override public @Nonnull TestContext newTestContext()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    return JOGLTestContext.makeContextWithOpenGL_ES3();
  }
}
