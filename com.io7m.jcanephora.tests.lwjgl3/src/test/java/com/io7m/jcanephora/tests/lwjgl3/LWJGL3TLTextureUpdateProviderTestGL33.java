/*
 * Copyright © 2016 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.tests.lwjgl3;

import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.core.api.JCGLTexturesType;
import com.io7m.jcanephora.tests.contracts.JCGLTLTextureUpdateProviderContract;
import com.io7m.jcanephora.texture.loader.awt.JCGLAWTTextureDataProvider;
import com.io7m.jcanephora.texture.loader.core.JCGLTLTextureDataProviderType;
import com.io7m.jcanephora.texture.loader.core.JCGLTLTextureUpdateProvider;
import com.io7m.jcanephora.texture.loader.core.JCGLTLTextureUpdateProviderType;

public final class LWJGL3TLTextureUpdateProviderTestGL33 extends
  JCGLTLTextureUpdateProviderContract
{
  @Override
  protected JCGLTLTextureUpdateProviderType getUpdateProvider()
  {
    return JCGLTLTextureUpdateProvider.newProvider();
  }

  @Override
  protected JCGLTLTextureDataProviderType getDataProvider()
  {
    return JCGLAWTTextureDataProvider.newProvider();
  }

  @Override
  protected JCGLTexturesType getTextures(final String name)
  {
    final JCGLContextType c = LWJGL3TestContexts.newGL33Context(name, 24, 8);
    return c.contextGetGL33().textures();
  }

  @Override
  public void onTestCompleted()
  {
    LWJGL3TestContexts.closeAllContexts();
  }
}
