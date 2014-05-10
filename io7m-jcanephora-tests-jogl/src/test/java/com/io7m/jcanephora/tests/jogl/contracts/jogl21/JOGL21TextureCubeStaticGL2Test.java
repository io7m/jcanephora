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

package com.io7m.jcanephora.tests.jogl.contracts.jogl21;

import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.TextureCubeStaticType;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureWrapR;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.api.JCGLTextureUnitsType;
import com.io7m.jcanephora.api.JCGLTexturesCubeStaticGL2Type;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.contracts.gl2.TextureCubeStaticGL2Contract;
import com.io7m.jcanephora.tests.jogl.JOGLTestContext;
import com.io7m.jcanephora.tests.jogl.JOGLTestContextUtilities;

public final class JOGL21TextureCubeStaticGL2Test extends
  TextureCubeStaticGL2Contract
{
  @Override public TextureCubeStaticType allocateTextureRGBA(
    final JCGLTexturesCubeStaticGL2Type t,
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

  @Override public JCGLTexturesCubeStaticGL2Type getGLTextureCubeStatic(
    final TestContext tc)
  {
    return JOGLTestContextUtilities.getGL2(tc);
  }

  @Override public JCGLTextureUnitsType getGLTextureUnits(
    final TestContext tc)
  {
    return JOGLTestContextUtilities.getGL2(tc);
  }

  @Override public boolean isGLSupported()
  {
    return JOGLTestContext.isOpenGL21WithExtensionsSupported();
  }

  @Override public TestContext newTestContext()
  {
    return JOGLTestContext.makeContextWithOpenGL2_1();
  }
}
