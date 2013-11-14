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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * <p>
 * Immutable type descriptor for an array buffer.
 * </p>
 * <p>
 * If array buffers are considered as arrays of records, this type represents
 * the type of the record.
 * </p>
 */

@Immutable public final class ArrayBufferTypeDescriptor
{
  private final @Nonnull ArrayBufferAttributeDescriptor attributes[];
  private final @Nonnull HashMap<String, Integer>       indices_by_name;
  private final int                                     offsets[];
  private final int                                     stride;

  public ArrayBufferTypeDescriptor(
    final @Nonnull ArrayBufferAttributeDescriptor[] attributes)
    throws ConstraintError
  {
    Constraints.constrainNotNull(attributes, "Buffer attributes");
    Constraints.constrainRange(
      attributes.length,
      1,
      Integer.MAX_VALUE,
      "Number of attributes");

    this.attributes = new ArrayBufferAttributeDescriptor[attributes.length];
    this.offsets = new int[attributes.length];
    this.indices_by_name = new HashMap<String, Integer>();

    int bytes = 0;
    for (int index = 0; index < attributes.length; ++index) {
      final ArrayBufferAttributeDescriptor a = attributes[index];
      Constraints.constrainNotNull(a, "Array attribute");

      if (this.indices_by_name.containsKey(a.getName())) {
        Constraints.constrainArbitrary(false, "Attribute names are unique");
      } else {
        this.indices_by_name.put(a.getName(), Integer.valueOf(index));
      }

      this.attributes[index] =
        new ArrayBufferAttributeDescriptor(
          a.getName(),
          a.getType(),
          a.getElements());
      this.offsets[index] = bytes;

      final int size = a.getType().getSizeBytes();
      final int elem = a.getElements();
      bytes += size * elem;
    }

    this.stride = bytes;
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
    final ArrayBufferTypeDescriptor other = (ArrayBufferTypeDescriptor) obj;
    if (!Arrays.equals(this.attributes, other.attributes)) {
      return false;
    }
    return true;
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

  public @Nonnull ArrayBufferAttributeDescriptor getAttribute(
    final @Nonnull String name)
    throws ConstraintError
  {
    Constraints.constrainNotNull(name, "Attribute name");
    Constraints.constrainArbitrary(
      this.hasAttribute(name),
      "Attribute name exists");

    final Integer index = this.indices_by_name.get(name);
    return this.attributes[index.intValue()];
  }

  /**
   * Retrieve the number of elements in the attribute named <code>name</code>.
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

  public int getAttributeElements(
    final @Nonnull String name)
    throws ConstraintError
  {
    final ArrayBufferAttributeDescriptor a = this.getAttribute(name);
    return a.getElements();
  }

  /**
   * Retrieve the set of names of all attributes in the type.
   */

  public @Nonnull Set<String> getAttributeNames()
  {
    return Collections.unmodifiableSet(this.indices_by_name.keySet());
  }

  /**
   * Retrieve the offset in bytes from the start of the element of the
   * attribute named <code>name</code>.
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

  public int getAttributeOffset(
    final @Nonnull String name)
    throws ConstraintError
  {
    Constraints.constrainNotNull(name, "Attribute name");
    Constraints.constrainArbitrary(
      this.hasAttribute(name),
      "Attribute name exists");

    final Integer index = this.indices_by_name.get(name);
    return this.offsets[index.intValue()];
  }

  /**
   * Retrieve the type of the attribute named <code>name</code>.
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

  public JCGLScalarType getAttributeType(
    final @Nonnull String name)
    throws ConstraintError
  {
    final ArrayBufferAttributeDescriptor a = this.getAttribute(name);
    return a.getType();
  }

  /**
   * Return the offset in bytes of the element <code>element</code> of the
   * attribute <code>name</code>.
   * 
   * @param name
   *          The name of the attribute.
   * @param element
   *          The index of the element.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>name == null</code></li>
   *           <li>No attribute named <code>name</code> exists in the
   *           descriptor.</li>
   *           <li><code>element</code> is greater than or equal to the number
   *           of elements in the attribute.</li>
   *           </ul>
   */

  public int getElementOffset(
    final @Nonnull String name,
    final int element)
    throws ConstraintError
  {
    final ArrayBufferAttributeDescriptor a = this.getAttribute(name);
    Constraints.constrainLessThan(element, a.getElements());

    final int base = this.getAttributeOffset(name);
    final int off = element * a.getType().getSizeBytes();
    return base + off;
  }

  /**
   * Retrieve the total size in bytes of one element.
   */

  public int getSize()
  {
    return this.stride;
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
    return this.indices_by_name.containsKey(name);
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + Arrays.hashCode(this.attributes);
    return result;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[ArrayBufferTypeDescriptor ");
    builder.append(Arrays.toString(this.attributes));
    builder.append("]");
    return builder.toString();
  }
}
