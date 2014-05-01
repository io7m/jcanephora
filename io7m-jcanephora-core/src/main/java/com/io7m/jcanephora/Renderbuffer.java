/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
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

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.jogl.JCGLResourceDeletable;

/**
 * A immutable reference to an allocated renderbuffer.
 */

@Immutable public final class Renderbuffer<K extends RenderbufferKind> extends
  JCGLResourceDeletable implements RenderbufferUsableType<K>, JCGLResourceSizedType
{
  /**
   * Unsafe operation for branding a renderbuffer <code>r</code> with the
   * phantom type <code>RenderableColor</code>.
   * 
   * Not intended to be accessible by user code.
   */

  @SuppressWarnings("unchecked") static 
    Renderbuffer<RenderableColorKind>
    unsafeBrandColor(
      final  Renderbuffer<?> r)
  {
    return (Renderbuffer<RenderableColorKind>) r;
  }

  /**
   * Unsafe operation for branding a renderbuffer <code>r</code> with the
   * phantom type <code>RenderableDepth</code>.
   * 
   * Not intended to be accessible by user code.
   */

  @SuppressWarnings("unchecked") static 
    Renderbuffer<RenderableDepthKind>
    unsafeBrandDepth(
      final  Renderbuffer<?> r)
  {
    return (Renderbuffer<RenderableDepthKind>) r;
  }

  /**
   * Unsafe operation for branding a renderbuffer <code>r</code> with the
   * phantom type <code>RenderableDepthStencil</code>.
   * 
   * Not intended to be accessible by user code.
   */

  @SuppressWarnings("unchecked") static 
    Renderbuffer<RenderableDepthStencilKind>
    unsafeBrandDepthStencil(
      final  Renderbuffer<?> r)
  {
    return (Renderbuffer<RenderableDepthStencilKind>) r;
  }

  /**
   * Unsafe operation for branding a renderbuffer <code>r</code> with the
   * phantom type <code>RenderableStencil</code>.
   * 
   * Not intended to be accessible by user code.
   */

  @SuppressWarnings("unchecked") static 
    Renderbuffer<RenderableStencilKind>
    unsafeBrandStencil(
      final  Renderbuffer<?> r)
  {
    return (Renderbuffer<RenderableStencilKind>) r;
  }

  private final int                       height;
  private final  RenderbufferType type;
  private final int                       value;
  private final int                       width;

  Renderbuffer(
    final  RenderbufferType type1,
    final int value1,
    final int width1,
    final int height1)
    throws ConstraintError
  {
    this.type = NullCheck.notNull(type1, "Renderbuffer type");
    this.value =
      Constraints.constrainRange(
        value1,
        0,
        Integer.MAX_VALUE,
        "Buffer ID value");
    this.width = width1;
    this.height = height1;
  }

  @Override public boolean equals(
    final Object obj)
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
    final Renderbuffer<?> other = (Renderbuffer<?>) obj;
    if (this.value != other.value) {
      return false;
    }
    return true;
  }

  @Override public int getGLName()
  {
    return this.value;
  }

  @Override public int getHeight()
  {
    return this.height;
  }

  @Override public  RenderbufferType getType()
  {
    return this.type;
  }

  @Override public int getWidth()
  {
    return this.width;
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + this.value;
    return result;
  }

  @Override public long resourceGetSizeBytes()
  {
    return this.width * this.type.getBytesPerPixel() * this.height;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[Renderbuffer ");
    builder.append(this.type);
    builder.append(" ");
    builder.append(this.value);
    builder.append(" ");
    builder.append(this.width);
    builder.append("x");
    builder.append(this.height);
    builder.append("]");
    return builder.toString();
  }
}
