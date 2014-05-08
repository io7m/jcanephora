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

package com.io7m.jcanephora;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.io7m.jequality.annotations.EqualityStructural;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;
import com.io7m.jranges.RangeCheck;

/**
 * <p>
 * Immutable type descriptor for an array buffer.
 * </p>
 * <p>
 * Array buffers are considered as arrays of records, this type represents the
 * type of the record.
 * </p>
 */

@EqualityStructural public final class ArrayDescriptor
{
  private static class Builder implements ArrayDescriptorBuilderType
  {
    private final Map<String, ArrayAttributeDescriptor> descriptors;
    private final List<ArrayAttributeDescriptor>        ordered;

    Builder()
    {
      this.descriptors = new HashMap<String, ArrayAttributeDescriptor>();
      this.ordered = new ArrayList<ArrayAttributeDescriptor>();
    }

    @Override public void addAttribute(
      final ArrayAttributeDescriptor a)
      throws JCGLExceptionAttributeDuplicate
    {
      NullCheck.notNull(a, "Attribute");

      if (this.descriptors.containsKey(a.getName())) {
        final String s =
          String.format(
            "An attribute already exists with name %s",
            a.getName());
        assert s != null;
        throw new JCGLExceptionAttributeDuplicate(s);
      }

      this.descriptors.put(a.getName(), a);
      this.ordered.add(a);
    }

    @SuppressWarnings("synthetic-access") @Override public
      ArrayDescriptor
      build()
    {
      return new ArrayDescriptor(this.descriptors, this.ordered);
    }
  }

  /**
   * @return A new array descriptor builder.
   */

  public static ArrayDescriptorBuilderType newBuilder()
  {
    return new Builder();
  }

  private final Map<String, ArrayAttributeDescriptor> attributes;
  private final Map<String, Integer>                  indices;
  private final int[]                                 offsets;
  private final List<ArrayAttributeDescriptor>        ordered;
  private final int                                   stride;

  private ArrayDescriptor(
    final Map<String, ArrayAttributeDescriptor> descriptors,
    final List<ArrayAttributeDescriptor> in_ordered)
  {
    NullCheck.notNull(descriptors, "Descriptors by name");
    NullCheck.notNull(in_ordered, "Ordered descriptors");

    RangeCheck.checkIncludedIn(
      in_ordered.size(),
      "Number of attributes",
      RangeCheck.POSITIVE_INTEGER,
      "Required attribute count");

    this.indices = new HashMap<String, Integer>();
    this.offsets = new int[in_ordered.size()];
    this.attributes = descriptors;
    this.ordered = in_ordered;

    int bytes = 0;
    for (int index = 0; index < in_ordered.size(); ++index) {
      final ArrayAttributeDescriptor a = in_ordered.get(index);

      final String name = a.getName();
      assert this.indices.containsKey(name) == false;
      this.indices.put(name, Integer.valueOf(index));

      this.offsets[index] = bytes;
      final int size = a.getType().getSizeBytes();
      final int elem = a.getComponents();
      bytes += size * elem;
    }

    this.stride = bytes;
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
    final ArrayDescriptor other = (ArrayDescriptor) obj;
    return this.ordered.equals(other.ordered);
  }

  /**
   * @return The offset in bytes of the attribute named <code>name</code> from
   *         the start of an arbitrary element.
   * 
   * @param name
   *          The name of the attribute.
   * @throws NoSuchElementException
   *           If there is no such attribute.
   */

  public int getAttributeOffset(
    final String name)
    throws NoSuchElementException
  {
    NullCheck.notNull(name, "Attribute name");

    if (this.attributes.containsKey(name) == false) {
      throw new NoSuchElementException(String.format(
        "No such attribute '%s'",
        name));
    }

    final Integer index = this.indices.get(name);
    return this.offsets[index.intValue()];
  }

  /**
   * @return The offset in bytes of the attribute named <code>name</code> from
   *         the start of a hypothetical given element.
   * 
   * @param name
   *          The name of the attribute.
   * @param element
   *          The element index.
   * @throws NoSuchElementException
   *           If there is no such attribute.
   */

  public int getAttributeOffsetForElement(
    final String name,
    final int element)
    throws NoSuchElementException
  {
    final int off = this.getAttributeOffset(name);
    return (element * this.stride) + off;
  }

  /**
   * @return A read-only view of the attributes, by name.
   */

  public Map<String, ArrayAttributeDescriptor> getAttributes()
  {
    final Map<String, ArrayAttributeDescriptor> r =
      Collections.unmodifiableMap(this.attributes);
    assert r != null;
    return r;
  }

  /**
   * @return The list of attributes in declaration order.
   */

  public List<ArrayAttributeDescriptor> getAttributesOrdered()
  {
    final List<ArrayAttributeDescriptor> r =
      Collections.unmodifiableList(this.ordered);
    assert r != null;
    return r;
  }

  /**
   * @return The total size in bytes of a single array element.
   */

  public long getElementSizeBytes()
  {
    return this.stride;
  }

  @Override public int hashCode()
  {
    return this.ordered.hashCode();
  }

  @Override public String toString()
  {
    final StringBuilder b = new StringBuilder();
    b.append("[ArrayDescriptor\n");
    for (final ArrayAttributeDescriptor o : this.ordered) {
      b.append("  ");
      b.append(o);
      b.append("\n");
    }
    b.append("]");
    final String r = b.toString();
    assert r != null;
    return r;
  }
}
