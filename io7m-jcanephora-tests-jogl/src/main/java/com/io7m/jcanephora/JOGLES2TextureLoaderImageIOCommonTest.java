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
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jcanephora.contracts.common.TextureLoaderContractCommon;

public final class JOGLES2TextureLoaderImageIOCommonTest extends
  TextureLoaderContractCommon<TextureLoaderImageIO>
{
  @Override public boolean isGLSupported()
  {
    return JOGLTestContext.isOpenGLES2Supported();
  }

  @Override public @Nonnull TextureLoaderImageIO makeTextureLoader(
    final @Nonnull TestContext tc,
    final @Nonnull JCGLTextures2DStaticCommon gl)
  {
    try {
      return TextureLoaderImageIO.newTextureLoader(tc.getLog());
    } catch (final ConstraintError x) {
      throw new UnreachableCodeException(x);
    }
  }

  @Override public @Nonnull TestContext newTestContext()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    return JOGLTestContext.makeContextWithOpenGL_ES2();
  }

  @Override public JCGLTextures2DStaticCommon getGLTextures2D(
    final TestContext tc)
  {
    return JOGLTestContextUtilities.getGLES2(tc);
  }

  @Override public JCGLTexturesCubeStaticCommon getGLTexturesCube(
    final TestContext tc)
  {
    return JOGLTestContextUtilities.getGLES2(tc);
  }
}
