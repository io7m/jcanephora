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

import com.io7m.jcanephora.RenderbufferFormat;
import com.io7m.jcanephora.RenderbufferKind;
import com.io7m.jcanephora.RenderbufferType;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;

/**
 * A immutable reference to an allocated renderbuffer.
 */

final class JOGLRenderbuffer<K extends RenderbufferKind> extends
  JOGLObjectShared implements RenderbufferType<K>
{
  private final RenderbufferFormat format;
  private final int                height;
  private final int                width;

  JOGLRenderbuffer(
    final GLContext context,
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
    final JOGLRenderbuffer<?> other = (JOGLRenderbuffer<?>) obj;
    return super.getGLName() == other.getGLName();
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + super.getGLName();
    return result;
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
    builder.append("[JOGLRenderbuffer ");
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
