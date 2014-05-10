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

package com.io7m.jcanephora.tests.contracts;

import com.io7m.jcanephora.TextureLoaderType;
import com.io7m.jcanephora.api.JCGLTextures2DStaticCommonType;
import com.io7m.jcanephora.api.JCGLTexturesCubeStaticCommonType;
import com.io7m.jcanephora.tests.TestContext;

public abstract class TextureLoaderContract<G2D extends JCGLTextures2DStaticCommonType, GC extends JCGLTexturesCubeStaticCommonType, T extends TextureLoaderType> implements
  TestContract
{
  public abstract G2D getGLTextures2D(
    final TestContext tc);

  public abstract GC getGLTexturesCube(
    final TestContext tc);

  public abstract TextureLoaderType makeTextureLoader(
    final TestContext tc,
    final G2D gl);
}
