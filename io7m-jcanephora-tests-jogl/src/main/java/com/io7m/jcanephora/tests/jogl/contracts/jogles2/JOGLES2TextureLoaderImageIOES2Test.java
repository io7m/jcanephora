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

package com.io7m.jcanephora.tests.jogl.contracts.jogles2;

import com.io7m.jcanephora.TextureLoaderType;
import com.io7m.jcanephora.api.JCGLTextures2DStaticGLES2Type;
import com.io7m.jcanephora.api.JCGLTexturesCubeStaticGLES2Type;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.contracts.gles2.TextureLoaderGLES2Contract;
import com.io7m.jcanephora.tests.jogl.JOGLTestContext;
import com.io7m.jcanephora.tests.jogl.JOGLTestContextUtilities;
import com.io7m.jcanephora.texload.imageio.TextureLoaderImageIO;

@SuppressWarnings("null") public final class JOGLES2TextureLoaderImageIOES2Test extends
  TextureLoaderGLES2Contract<TextureLoaderImageIO>
{
  @Override public JCGLTextures2DStaticGLES2Type getGLTextures2D(
    final TestContext tc)
  {
    return JOGLTestContextUtilities.getGLES2(tc);
  }

  @Override public JCGLTexturesCubeStaticGLES2Type getGLTexturesCube(
    final TestContext tc)
  {
    return JOGLTestContextUtilities.getGLES2(tc);
  }

  @Override public boolean isGLSupported()
  {
    return JOGLTestContext.isOpenGLES2Supported();
  }

  @Override public TextureLoaderType makeTextureLoader(
    final TestContext tc,
    final JCGLTextures2DStaticGLES2Type gl)
  {
    return TextureLoaderImageIO.newTextureLoader(tc.getLog());
  }

  @Override public TestContext newTestContext()
  {
    return JOGLTestContext.makeContextWithOpenGL_ES2();
  }
}
