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

package com.io7m.jcanephora.tests.fake.contracts.gl30;

import com.io7m.jcanephora.api.JCGLTexturesCubeStaticGL3Type;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.contracts.gl3.TextureCubeStaticReadableGL3Contract;
import com.io7m.jcanephora.tests.fake.FakeShaderControl;
import com.io7m.jcanephora.tests.fake.FakeTestContext;
import com.io7m.jcanephora.tests.fake.FakeTestContextUtilities;

public final class Fake30TextureCubeStaticReadableGL3Test extends
  TextureCubeStaticReadableGL3Contract
{
  @Override public JCGLTexturesCubeStaticGL3Type getTextures()
  {
    return FakeTestContextUtilities.getGL3(this.newTestContext());
  }

  @Override public boolean isGLSupported()
  {
    return true;
  }

  @Override public TestContext newTestContext()
  {
    return FakeTestContext.makeContextWithOpenGL3_0(new FakeShaderControl());
  }
}