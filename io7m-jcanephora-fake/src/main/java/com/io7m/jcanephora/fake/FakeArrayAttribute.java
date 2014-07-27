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

import com.io7m.jcanephora.ArrayAttributeDescriptor;
import com.io7m.jcanephora.ArrayAttributeType;
import com.io7m.jcanephora.ArrayBufferUsableType;
import com.io7m.jequality.annotations.EqualityStructural;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;

@EqualityStructural final class FakeArrayAttribute extends
  FakeObjectPseudoShared implements ArrayAttributeType
{
  private final ArrayBufferUsableType    array;
  private final ArrayAttributeDescriptor descriptor;

  public FakeArrayAttribute(
    final FakeContext in_context,
    final ArrayBufferUsableType in_array,
    final ArrayAttributeDescriptor in_descriptor)
  {
    super(in_context);
    this.array = NullCheck.notNull(in_array, "Array");
    this.descriptor = NullCheck.notNull(in_descriptor, "Descriptor");
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
    final FakeArrayAttribute other = (FakeArrayAttribute) obj;
    if (!this.array.equals(other.array)) {
      return false;
    }
    if (!this.descriptor.equals(other.descriptor)) {
      return false;
    }
    return true;
  }

  @Override public ArrayBufferUsableType getArray()
  {
    return this.array;
  }

  @Override public ArrayAttributeDescriptor getDescriptor()
  {
    return this.descriptor;
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + this.array.hashCode();
    result = (prime * result) + this.descriptor.getName().hashCode();
    return result;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[FakeArrayAttribute ");
    builder.append(this.array);
    builder.append(" ");
    builder.append(this.descriptor);
    builder.append("]");
    final String r = builder.toString();
    assert r != null;
    return r;
  }
}
