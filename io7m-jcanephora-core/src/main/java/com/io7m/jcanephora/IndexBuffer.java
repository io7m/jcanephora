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
import com.io7m.jaux.RangeInclusive;

/**
 * An immutable reference to an allocated index buffer.
 */

@Immutable public final class IndexBuffer extends Deletable implements
  Buffer,
  GLResource
{
  private final int            value;
  private final GLUnsignedType type;
  private boolean              deleted;
  private final RangeInclusive range;

  IndexBuffer(
    final int value,
    final @Nonnull RangeInclusive range,
    final GLUnsignedType type)
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
    this.deleted = false;
  }

  /**
   * Retrieve the size in bytes of each element.
   */

  @Override public long getElementSizeBytes()
  {
    return GLUnsignedTypeMeta.getSizeBytes(this.type);
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

  /**
   * Retrieve the total size in bytes of the allocated buffer.
   */

  @Override public long getSizeBytes()
  {
    return this.getElementSizeBytes() * this.range.getInterval();
  }

  /**
   * Retrieve the type of the elements in the buffer.
   */

  public @Nonnull GLUnsignedType getType()
  {
    return this.type;
  }

  @Override public void resourceDelete(
    final @Nonnull GLInterfaceEmbedded gl)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    gl.indexBufferDelete(this);
  }

  @Override public boolean resourceIsDeleted()
  {
    return this.deleted;
  }

  @Override void setDeleted()
  {
    this.deleted = true;
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
