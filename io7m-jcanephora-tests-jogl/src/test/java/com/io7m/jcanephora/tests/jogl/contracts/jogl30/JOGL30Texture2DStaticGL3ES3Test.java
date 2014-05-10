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

import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.Texture2DStaticType;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.api.JCGLTextureUnitsType;
import com.io7m.jcanephora.api.JCGLTextures2DStaticGL3ES3Type;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.contracts.gl3es3.Texture2DStaticGL3ES3Contract;
import com.io7m.jcanephora.tests.jogl.JOGLTestContext;
import com.io7m.jcanephora.tests.jogl.JOGLTestContextUtilities;

@SuppressWarnings("null") public final class JOGL30Texture2DStaticGL3ES3Test extends
  Texture2DStaticGL3ES3Contract
{
  @Override public Texture2DStaticType allocateTextureRGBA(
    final JCGLTextures2DStaticGL3ES3Type t,
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification filter_min,
    final TextureFilterMagnification filter_mag)
    throws JCGLException
  {
    return t.texture2DStaticAllocateRGBA8(
      name,
      width,
      height,
      wrap_s,
      wrap_t,
      filter_min,
      filter_mag);
  }

  @Override public JCGLTextures2DStaticGL3ES3Type getGLTexture2DStatic(
    final TestContext tc)
  {
    return JOGLTestContextUtilities.getGL3(tc);
  }

  @Override public JCGLTextureUnitsType getGLTextureUnits(
    final TestContext tc)
  {
    return JOGLTestContextUtilities.getGL3(tc);
  }

  @Override public boolean isGLSupported()
  {
    return JOGLTestContext.isOpenGL30Supported();
  }

  @Override public  TestContext newTestContext()
  {
    return JOGLTestContext.makeContextWithOpenGL3_0();
  }
}
