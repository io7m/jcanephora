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
 * Immutable descriptor for an array buffer.
 * 
 * If array buffers are considered as arrays of records, this type represents
 * the type of the record.
 */

@Immutable public final class ArrayBufferDescriptor
{
  private final ArrayBufferAttribute     attributes[];
  private final int                      offsets[];
  private final HashMap<String, Integer> indices;
  private final int                      stride;

  public ArrayBufferDescriptor(
    final @Nonnull ArrayBufferAttribute[] attributes)
    throws ConstraintError
  {
    Constraints.constrainNotNull(attributes, "Buffer attributes");
    Constraints.constrainRange(
      attributes.length,
      1,
      Integer.MAX_VALUE,
      "Number of attributes");

    this.attributes = new ArrayBufferAttribute[attributes.length];
    this.offsets = new int[attributes.length];
    this.indices = new HashMap<String, Integer>();

    int bytes = 0;
    for (int index = 0; index < attributes.length; ++index) {
      final ArrayBufferAttribute a = attributes[index];

      if (this.indices.containsKey(a.getName())) {
        Constraints.constrainArbitrary(false, "Attribute names are unique");
      } else {
        this.indices.put(a.getName(), Integer.valueOf(index));
      }

      this.attributes[index] =
        new ArrayBufferAttribute(a.getName(), a.getType(), a.getElements());
      this.offsets[index] = bytes;

      final int size = a.getType().getSizeBytes();
      final int elem = a.getElements();
      bytes += size * elem;
    }

    this.stride = bytes;
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
      "Attribute name exists");

    final Integer index = this.indices.get(name);
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
    final ArrayBufferAttribute a = this.getAttribute(name);
    return a.getElements();
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
    Constraints.constrainArbitrary(
      this.hasAttribute(name),
      "Attribute name exists");

    final Integer index = this.indices.get(name);
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
    final ArrayBufferAttribute a = this.getAttribute(name);
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
    final ArrayBufferAttribute a = this.getAttribute(name);
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
    return this.indices.containsKey(name);
  }
}
