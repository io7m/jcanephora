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

package com.io7m.jcanephora.jogl;

import javax.media.opengl.GLContext;

import com.io7m.jcanephora.FramebufferType;
import com.io7m.jequality.annotations.EqualityStructural;
import com.io7m.jnull.Nullable;

@EqualityStructural final class JOGLFramebuffer extends JOGLObjectUnshared implements
  FramebufferType
{
  private final int depth_bits;
  private final int stencil_bits;

  protected JOGLFramebuffer(
    final GLContext in_context,
    final int in_id,
    final int in_depth_bits,
    final int in_stencil_bits)
  {
    super(in_context, in_id);
    this.depth_bits = in_depth_bits;
    this.stencil_bits = in_stencil_bits;
  }

  @Override public boolean equals(
    @Nullable final Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    final JOGLFramebuffer other = (JOGLFramebuffer) obj;
    if (this.depth_bits != other.depth_bits) {
      return false;
    }
    if (this.stencil_bits != other.stencil_bits) {
      return false;
    }
    return true;
  }

  /**
   * @return The number of depth bits in the framebuffer
   */

  @Override public int framebufferGetDepthBits()
  {
    return this.depth_bits;
  }

  /**
   * @return The number of stencil bits in the framebuffer
   */

  @Override public int framebufferGetStencilBits()
  {
    return this.stencil_bits;
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + this.depth_bits;
    result = (prime * result) + this.stencil_bits;
    return result;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[JOGLFramebuffer ");
    builder.append(this.getGLName());
    builder.append("]");
    final String r = builder.toString();
    assert r != null;
    return r;
  }
}
