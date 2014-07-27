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

package com.io7m.jcanephora.tests.fake.contracts.gles2;

import com.io7m.jcanephora.api.JCGLScissorType;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.contracts.ScissorContract;
import com.io7m.jcanephora.tests.fake.FakeShaderControl;
import com.io7m.jcanephora.tests.fake.FakeTestContext;
import com.io7m.jcanephora.tests.fake.FakeTestContextUtilities;

public final class FakeES2ScissorTest extends ScissorContract
{
  @Override public JCGLScissorType getGLScissor(
    final TestContext tc)
  {
    return FakeTestContextUtilities.getGLES2(tc);
  }

  @Override public boolean isGLSupported()
  {
    return true;
  }

  @Override public TestContext newTestContext()
  {
    return FakeTestContext.makeContextWithOpenGL_ES2(new FakeShaderControl());
  }
}
