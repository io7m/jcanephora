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
 * <p>
 * An identifier for a draw buffer.
 * </p>
 * <p>
 * Any given framebuffer has at most <code>GL_MAX_DRAW_BUFFERS</code>. A
 * fragment shader writing to output <code>n</code> is conceptually writing to
 * draw buffer <code>n</code>.
 * </p>
 */

public interface FramebufferDrawBufferType extends
  Comparable<FramebufferDrawBufferType>
{
  /**
   * @return The index of the draw buffer. This value will be between 0 and
   *         some implementation-defined exclusive upper limit (usually 4 or 8
   *         in current implementations).
   */

  int drawBufferGetIndex();
}
