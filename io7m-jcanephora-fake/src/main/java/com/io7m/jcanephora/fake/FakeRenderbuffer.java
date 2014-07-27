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
import com.io7m.jcanephora.RenderbufferFormat;
import com.io7m.jcanephora.RenderbufferKind;
import com.io7m.jcanephora.RenderbufferType;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;
import com.io7m.junreachable.UnreachableCodeException;

/**
 * A immutable reference to an allocated renderbuffer.
 */

final class FakeRenderbuffer<K extends RenderbufferKind> extends
  FakeObjectShared implements
  RenderbufferType<K>,
  FakeFramebufferAttachableType
{
  private final RenderbufferFormat format;
  private final int                height;
  private final int                width;

  FakeRenderbuffer(
    final FakeContext context,
    final RenderbufferFormat in_format,
    final int in_value,
    final int in_width,
    final int in_height)
  {
    super(context, in_value);
    this.format = NullCheck.notNull(in_format, "Renderbuffer format");
    this.width = in_width;
    this.height = in_height;
  }

  @Override public <T, E extends Exception> T attachableAccept(
    final FakeFramebufferAttachableVisitorType<T, E> v)
    throws E,
      JCGLException
  {
    return v.renderbuffer(this);
  }

  @Override public boolean equals(
    final @Nullable Object obj)
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
    final FakeRenderbuffer<?> other = (FakeRenderbuffer<?>) obj;
    return super.getGLName() == other.getGLName();
  }

  @Override public int getStencilBits()
  {
    switch (this.format) {
      case RENDERBUFFER_COLOR_RGBA_4444:
      case RENDERBUFFER_COLOR_RGBA_5551:
      case RENDERBUFFER_COLOR_RGBA_8888:
      case RENDERBUFFER_COLOR_RGB_565:
      case RENDERBUFFER_COLOR_RGB_888:
      case RENDERBUFFER_DEPTH_16:
      case RENDERBUFFER_DEPTH_24:
        return 0;
      case RENDERBUFFER_DEPTH_24_STENCIL_8:
      case RENDERBUFFER_STENCIL_8:
        return 8;
    }

    throw new UnreachableCodeException();
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + super.getGLName();
    return result;
  }

  @Override public boolean isStencilRenderable()
  {
    return this.format.isStencilRenderable();
  }

  @Override public RenderbufferFormat renderbufferGetFormat()
  {
    return this.format;
  }

  @Override public int renderbufferGetHeight()
  {
    return this.height;
  }

  @Override public int renderbufferGetWidth()
  {
    return this.width;
  }

  @Override public long resourceGetSizeBytes()
  {
    return this.width * this.format.getBytesPerPixel() * this.height;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[FakeRenderbuffer ");
    builder.append(this.format);
    builder.append(" ");
    builder.append(super.getGLName());
    builder.append(" ");
    builder.append(this.width);
    builder.append("x");
    builder.append(this.height);
    builder.append("]");
    final String r = builder.toString();
    assert r != null;
    return r;
  }
}
