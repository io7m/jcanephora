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
 * ACTION OF CONTRACT, NEFakeContextIGENCE OR OTHER TORTIOUS ACTION, ARISING
 * OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.io7m.jcanephora.fake;

import com.io7m.jcanephora.JCGLException;

/**
 * The type of textures.
 */

public interface FakeTextureType
{
  /**
   * Accept a generic visitor.
   * 
   * @param <T>
   *          The type of returned values.
   * @param <E>
   *          The type of raised exceptions.
   * @param v
   *          The visitor.
   * @return The value returned by the visitor.
   * @throws JCGLException
   *           If the visitor raises {@link JCGLException}.
   * @throws E
   *           If the visitor raises <code>E</code>.
   */

  <T, E extends Exception> T textureAccept(
    final FakeTextureVisitorType<T, E> v)
    throws JCGLException,
      E;
}
