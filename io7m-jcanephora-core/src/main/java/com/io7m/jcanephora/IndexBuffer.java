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
import com.io7m.jaux.RangeInclusive;

/**
 * An immutable reference to an allocated index buffer.
 */

@Immutable public final class IndexBuffer extends JCGLResourceDeletable implements
  Buffer,
  IndexBufferUsable
{
  private final RangeInclusive   range;
  private final JCGLUnsignedType type;
  private final int              value;

  IndexBuffer(
    final int value,
    final @Nonnull RangeInclusive range,
    final JCGLUnsignedType type)
    throws ConstraintError
  {
    this.value =
      Constraints.constrainRange(
        value,
        0,
        Integer.MAX_VALUE,
        "buffer ID value");
    this.range = range;
    this.type = Constraints.constrainNotNull(type, "GL type");
  }

  @Override public long getElementSizeBytes()
  {
    return this.type.getSizeBytes();
  }

  /**
   * Retrieve the raw OpenGL 'location' of the buffer.
   */

  @Override public int getGLName()
  {
    return this.value;
  }

  @Override public @Nonnull RangeInclusive getRange()
  {
    return this.range;
  }

  @Override public @Nonnull JCGLUnsignedType getType()
  {
    return this.type;
  }

  @Override public long resourceGetSizeBytes()
  {
    return this.getElementSizeBytes() * this.range.getInterval();
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[ArrayBufferID ");
    builder.append(this.value);
    builder.append(" ");
    builder.append(this.range);
    builder.append(" ");
    builder.append(this.getElementSizeBytes());
    builder.append("]");

    return builder.toString();
  }
}
