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

/**
 * <p>
 * An array buffer attribute associated with a specific array.
 * </p>
 */

@Immutable public final class ArrayBufferAttribute
{
  private final @Nonnull ArrayBuffer                    array;
  private final @Nonnull ArrayBufferAttributeDescriptor descriptor;

  ArrayBufferAttribute(
    final @Nonnull ArrayBuffer array1,
    final @Nonnull ArrayBufferAttributeDescriptor descriptor1)
    throws ConstraintError
  {
    this.array = Constraints.constrainNotNull(array1, "Array buffer");
    this.descriptor =
      Constraints.constrainNotNull(descriptor1, "Attribute descriptor");
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
    final ArrayBufferAttribute other = (ArrayBufferAttribute) obj;
    if (!this.array.equals(other.array)) {
      return false;
    }
    if (!this.descriptor.equals(other.descriptor)) {
      return false;
    }
    return true;
  }

  public @Nonnull ArrayBufferUsable getArray()
  {
    return this.array;
  }

  public @Nonnull ArrayBufferAttributeDescriptor getDescriptor()
  {
    return this.descriptor;
  }

  public @Nonnull String getName()
  {
    return this.descriptor.getName();
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + this.array.hashCode();
    result = (prime * result) + this.descriptor.hashCode();
    return result;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[ArrayBufferAttribute ");
    builder.append(this.array);
    builder.append(" ");
    builder.append(this.descriptor);
    builder.append("]");
    return builder.toString();
  }
}
