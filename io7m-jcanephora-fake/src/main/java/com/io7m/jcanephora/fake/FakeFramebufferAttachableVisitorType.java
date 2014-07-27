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

package com.io7m.jcanephora.fake;

import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.RenderbufferKind;

/**
 * The type of generic framebuffer attachment visitors.
 *
 * @param <T>
 *          The type of returned values.
 * @param <E>
 *          The type of raised exceptions.
 */

public interface FakeFramebufferAttachableVisitorType<T, E extends Exception>
{
  /**
   * Visit a renderbuffer.
   *
   * @param <K>
   *          The renderbuffer kind.
   * @param r
   *          The rendebuffer.
   * @return A value of <code>T</code>.
   * @throws E
   *           If required.
   * @throws JCGLException
   *           If required.
   */

  <K extends RenderbufferKind> T renderbuffer(
    final FakeRenderbuffer<K> r)
    throws E,
      JCGLException;

  /**
   * Visit a texture.
   *
   * @param t
   *          The texture.
   * @return A value of <code>T</code>.
   * @throws E
   *           If required.
   * @throws JCGLException
   *           If required.
   */

  T texture2D(
    final FakeTexture2DStatic t)
    throws E,
      JCGLException;

  /**
   * Visit a cube texture.
   *
   * @param t
   *          The texture.
   * @return A value of <code>T</code>.
   * @throws E
   *           If required.
   * @throws JCGLException
   *           If required.
   */

  T textureCube(
    final FakeTextureCubeStatic t)
    throws E,
      JCGLException;
}
