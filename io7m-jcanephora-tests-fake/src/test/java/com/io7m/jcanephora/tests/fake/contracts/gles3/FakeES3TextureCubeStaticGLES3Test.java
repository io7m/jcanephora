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

package com.io7m.jcanephora.tests.fake.contracts.gles3;

import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.TextureCubeStaticType;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureWrapR;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.api.JCGLTextureUnitsType;
import com.io7m.jcanephora.api.JCGLTexturesCubeStaticGLES3Type;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.contracts.gles3.TextureCubeStaticGLES3Contract;
import com.io7m.jcanephora.tests.fake.FakeShaderControl;
import com.io7m.jcanephora.tests.fake.FakeTestContext;
import com.io7m.jcanephora.tests.fake.FakeTestContextUtilities;

public final class FakeES3TextureCubeStaticGLES3Test extends
  TextureCubeStaticGLES3Contract
{
  @Override public TextureCubeStaticType allocateTextureRGBA(
    final JCGLTexturesCubeStaticGLES3Type t,
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification filter_min,
    final TextureFilterMagnification filter_mag)
    throws JCGLException
  {
    return t.textureCubeStaticAllocateRGBA8(
      name,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      filter_min,
      filter_mag);
  }

  @Override public JCGLTexturesCubeStaticGLES3Type getGLTextureCubeStatic(
    final TestContext tc)
  {
    return FakeTestContextUtilities.getGLES3(tc);
  }

  @Override public JCGLTextureUnitsType getGLTextureUnits(
    final TestContext tc)
  {
    return FakeTestContextUtilities.getGLES3(tc);
  }

  @Override public boolean isGLSupported()
  {
    return true;
  }

  @Override public TestContext newTestContext()
  {
    return FakeTestContext.makeContextWithOpenGL_ES3(new FakeShaderControl());
  }
}
