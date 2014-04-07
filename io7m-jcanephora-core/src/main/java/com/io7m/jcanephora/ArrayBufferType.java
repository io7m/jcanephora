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

import java.util.HashMap;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * <p>
 * An array buffer type associated with a specific array.
 * </p>
 */

@Immutable public final class ArrayBufferType
{
  private final @Nonnull ArrayBuffer                           array;
  private final @Nonnull HashMap<String, ArrayBufferAttribute> attributes;
  private final @Nonnull ArrayBufferTypeDescriptor             descriptor;

  ArrayBufferType(
    final @Nonnull ArrayBuffer array1,
    final @Nonnull ArrayBufferTypeDescriptor descriptor1)
    throws ConstraintError
  {
    this.array = Constraints.constrainNotNull(array1, "Array buffer");
    this.descriptor =
      Constraints.constrainNotNull(descriptor1, "Type descriptor");
    this.attributes = new HashMap<String, ArrayBufferAttribute>();

    for (final String name : descriptor1.getAttributeNames()) {
      this.attributes.put(
        name,
        new ArrayBufferAttribute(array1, descriptor1.getAttribute(name)));
    }
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
    final ArrayBufferType other = (ArrayBufferType) obj;
    if (!this.array.equals(other.array)) {
      return false;
    }
    if (!this.descriptor.equals(other.descriptor)) {
      return false;
    }
    return true;
  }

  /**
   * Retrieve the array buffer associated with this type.
   */

  public @Nonnull ArrayBufferUsable getArrayBuffer()
  {
    return this.array;
  }

  /**
   * Retrieve the attribute named <code>name</code>.
   * 
   * @param name
   *          The name of the attribute.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>name == null</code></li>
   *           <li>No attribute named <code>name</code> exists in the
   *           descriptor.</li>
   *           </ul>
   */

  public @Nonnull ArrayBufferAttribute getAttribute(
    final @Nonnull String name)
    throws ConstraintError
  {
    Constraints.constrainArbitrary(
      this.hasAttribute(name),
      "Array has attribute");
    return this.attributes.get(name);
  }

  /**
   * Retrieve the type descriptor associated with this type.
   */

  public @Nonnull ArrayBufferTypeDescriptor getTypeDescriptor()
  {
    return this.descriptor;
  }

  /**
   * Return <code>true</code> iff an attribute named <code>name</code> exists.
   * 
   * @param name
   *          The name of the attribute.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>name == null</code></li>
   *           </ul>
   */

  public boolean hasAttribute(
    final @Nonnull String name)
    throws ConstraintError
  {
    Constraints.constrainNotNull(name, "Attribute name");
    return this.attributes.containsKey(name);
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
    builder.append("[ArrayBufferType ");
    builder.append(this.array);
    builder.append(" ");
    builder.append(this.descriptor);
    builder.append("]");
    return builder.toString();
  }

}
