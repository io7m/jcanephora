/*
 * Copyright © 2012 http://io7m.com
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

/**
 * A immutable reference to an allocated renderbuffer.
 */

@Immutable public final class Renderbuffer<K extends RenderbufferKind> extends
  GLResourceDeletable implements RenderbufferUsable<K>
{
  /**
   * Unsafe operation for branding a renderbuffer <code>r</code> with the
   * phantom type <code>RenderableColor</code>.
   * 
   * Not intended to be accessible by user code.
   */

  @SuppressWarnings("unchecked") static @Nonnull
    Renderbuffer<RenderableColor>
    unsafeBrandColor(
      final @Nonnull Renderbuffer<?> r)
  {
    return (Renderbuffer<RenderableColor>) r;
  }

  /**
   * Unsafe operation for branding a renderbuffer <code>r</code> with the
   * phantom type <code>RenderableDepth</code>.
   * 
   * Not intended to be accessible by user code.
   */

  @SuppressWarnings("unchecked") static @Nonnull
    Renderbuffer<RenderableDepth>
    unsafeBrandDepth(
      final @Nonnull Renderbuffer<?> r)
  {
    return (Renderbuffer<RenderableDepth>) r;
  }

  /**
   * Unsafe operation for branding a renderbuffer <code>r</code> with the
   * phantom type <code>RenderableDepthStencil</code>.
   * 
   * Not intended to be accessible by user code.
   */

  @SuppressWarnings("unchecked") static @Nonnull
    Renderbuffer<RenderableDepthStencil>
    unsafeBrandDepthStencil(
      final @Nonnull Renderbuffer<?> r)
  {
    return (Renderbuffer<RenderableDepthStencil>) r;
  }

  /**
   * Unsafe operation for branding a renderbuffer <code>r</code> with the
   * phantom type <code>RenderableStencil</code>.
   * 
   * Not intended to be accessible by user code.
   */

  @SuppressWarnings("unchecked") static @Nonnull
    Renderbuffer<RenderableStencil>
    unsafeBrandStencil(
      final @Nonnull Renderbuffer<?> r)
  {
    return (Renderbuffer<RenderableStencil>) r;
  }

  private final @Nonnull RenderbufferType type;
  private final int                       value;
  private final int                       width;
  private final int                       height;

  Renderbuffer(
    final @Nonnull RenderbufferType type,
    final int value,
    final int width,
    final int height)
    throws ConstraintError
  {
    this.type = Constraints.constrainNotNull(type, "Renderbuffer type");
    this.value =
      Constraints.constrainRange(
        value,
        0,
        Integer.MAX_VALUE,
        "Buffer ID value");
    this.width = width;
    this.height = height;
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

  @Override public @Nonnull RenderbufferType getType()
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
