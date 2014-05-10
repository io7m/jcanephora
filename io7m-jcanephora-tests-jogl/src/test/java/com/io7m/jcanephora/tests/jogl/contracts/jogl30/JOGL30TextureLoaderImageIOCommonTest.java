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

package com.io7m.jcanephora.tests.jogl.contracts.jogl30;

import com.io7m.jcanephora.TextureLoaderType;
import com.io7m.jcanephora.api.JCGLTextures2DStaticCommonType;
import com.io7m.jcanephora.api.JCGLTexturesCubeStaticCommonType;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.contracts.common.TextureLoaderContractCommon;
import com.io7m.jcanephora.tests.jogl.JOGLTestContext;
import com.io7m.jcanephora.texload.imageio.TextureLoaderImageIO;

@SuppressWarnings("null") public final class JOGL30TextureLoaderImageIOCommonTest extends
  TextureLoaderContractCommon<TextureLoaderImageIO>
{
  @Override public JCGLTextures2DStaticCommonType getGLTextures2D(
    final TestContext tc)
  {
    return tc.getGLImplementation().getGLCommon();
  }

  @Override public JCGLTexturesCubeStaticCommonType getGLTexturesCube(
    final TestContext tc)
  {
    return tc.getGLImplementation().getGLCommon();
  }

  @Override public boolean isGLSupported()
  {
    return JOGLTestContext.isOpenGL30Supported();
  }

  @Override public TextureLoaderType makeTextureLoader(
    final TestContext tc,
    final JCGLTextures2DStaticCommonType gl)
  {
    return TextureLoaderImageIO.newTextureLoader(tc.getLog());
  }

  @Override public TestContext newTestContext()
  {
    return JOGLTestContext.makeContextWithOpenGL3_0();
  }
}
