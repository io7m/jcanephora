/*
 * Copyright © 2015 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.tests.texture_loader;

import com.io7m.jcanephora.tests.contracts.JCGLTLTextureDataProviderContract;
import com.io7m.jcanephora.texture.loader.awt.JCGLAWTTextureDataProvider;
import com.io7m.jcanephora.texture.loader.core.JCGLTLTextureDataProviderType;

/**
 * AWT texture data provider test.
 */

// CHECKSTYLE_JAVADOC:OFF

public final class JCGLAWTTextureDataProviderTest extends
  JCGLTLTextureDataProviderContract
{
  @Override
  protected JCGLTLTextureDataProviderType getProvider()
  {
    return JCGLAWTTextureDataProvider.newProvider();
  }

  @Override
  public void onTestCompleted()
  {

  }
}
