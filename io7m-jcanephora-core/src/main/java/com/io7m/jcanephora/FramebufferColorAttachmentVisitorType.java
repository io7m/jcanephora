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

package com.io7m.jcanephora;

/**
 * The type of color attachment visitors.
 *
 * @param <A>
 *          The type of returned values.
 * @param <E>
 *          The type of exceptions raised.
 */

public interface FramebufferColorAttachmentVisitorType<A, E extends Throwable>
{
  /**
   * Visit an attachment.
   *
   * @param t
   *          The attachment.
   * @return A value of <code>A</code>
   * @throws JCGLException
   *           On errors
   * @throws E
   *           On errors
   */

  A renderbuffer(
    final RenderbufferUsableType<RenderableColorKind> t)
    throws JCGLException,
      E;

  /**
   * Visit an attachment.
   *
   * @param t
   *          The attachment.
   * @return A value of <code>A</code>
   * @throws JCGLException
   *           On errors
   * @throws E
   *           On errors
   */

  A texture2DStatic(
    final Texture2DStaticUsableType t)
    throws JCGLException,
      E;

  /**
   * Visit an attachment.
   *
   * @param t
   *          The attachment.
   * @param face
   *          The cube map face.
   * @return A value of <code>A</code>
   * @throws JCGLException
   *           On errors
   * @throws E
   *           On errors
   */

  A textureCubeStatic(
    TextureCubeStaticUsableType t,
    CubeMapFaceLH face)
    throws JCGLException,
      E;
}
