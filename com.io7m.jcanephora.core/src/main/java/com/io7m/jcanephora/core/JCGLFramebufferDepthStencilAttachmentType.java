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

package com.io7m.jcanephora.core;

/**
 * The type of framebuffer depth+stencil attachments.
 */

public interface JCGLFramebufferDepthStencilAttachmentType
  extends JCGLReferableType
{
  /**
   * A function that accepts matchers.
   *
   * @param v   A matcher.
   * @param <A> The type of returned values.
   * @param <E> The type of exceptions thrown by the matcher.
   *
   * @return The value returned by {@code v}.
   *
   * @throws JCGLException Iff a {@link JCGLException} is propagated from {@code
   *                       v}.
   * @throws E             Iff {@code v} throws an {@code E}.
   */

  <A, E extends Throwable> A matchDepthStencilAttachment(
    final JCGLFramebufferDepthStencilAttachmentMatcherType<A, E> v)
    throws JCGLException,
    E;

}
