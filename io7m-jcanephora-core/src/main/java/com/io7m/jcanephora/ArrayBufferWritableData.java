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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;

/**
 * An allocated region of data, to replace or update an array buffer.
 */

public final class ArrayBufferWritableData
{
  private final @Nonnull ArrayBuffer    buffer;
  private final @Nonnull RangeInclusive range;
  private final @Nonnull ByteBuffer     target_data;
  private final long                    target_data_offset;
  private final long                    target_data_size;
  private final @Nonnull RangeInclusive target_range;

  /**
   * Construct a buffer of data that will be used to replace the entirety of
   * the data in <code>buffer</code> on the GPU.
   * 
   * @param buffer1
   *          The array buffer.
   * @throws ConstraintError
   *           Iff any of the following conditions hold:
   *           <ul>
   *           <li><code>buffer == null</code></li>
   *           </ul>
   */

  public ArrayBufferWritableData(
    final @Nonnull ArrayBuffer buffer1)
    throws ConstraintError
  {
    this(buffer1, buffer1.getRange());
  }

  /**
   * Construct a buffer of data that will be used to replace elements in the
   * range <code>range</code> of the data in <code>buffer</code> on the GPU.
   * 
   * @param buffer1
   *          The array buffer.
   * @param range1
   *          The inclusive range defining the area of the array buffer that
   *          will be modified.
   * @throws ConstraintError
   *           Iff any of the following conditions hold:
   *           <ul>
   *           <li><code>buffer == null</code></li>
   *           <li><code>range == null/code></li>
   *           <li><code>range.isIncludedIn(buffer.getRange()) == false</code>
   *           </li>
   *           </ul>
   */

  public ArrayBufferWritableData(
    final @Nonnull ArrayBuffer buffer1,
    final @Nonnull RangeInclusive range1)
    throws ConstraintError
  {
    this.buffer = Constraints.constrainNotNull(buffer1, "Array buffer");
    this.range = Constraints.constrainNotNull(range1, "Range");

    Constraints.constrainArbitrary(
      range1.isIncludedIn(buffer1.getRange()),
      "Given range is in for the given buffer");

    this.target_range =
      new RangeInclusive(0, this.buffer.getRange().getInterval() - 1);
    this.target_data_size =
      range1.getInterval() * buffer1.getElementSizeBytes();
    this.target_data_offset =
      range1.getLower() * buffer1.getElementSizeBytes();
    this.target_data =
      ByteBuffer.allocateDirect((int) this.target_data_size).order(
        ByteOrder.nativeOrder());
  }

  /**
   * Retrieve the array buffer to which the given data will be uploaded.
   */

  public @Nonnull ArrayBufferUsable getArrayBuffer()
  {
    return this.buffer;
  }

  /**
   * Retrieve a cursor that may only point to elements of the attribute
   * <code>attribute_name</code> in the array data. The cursor interface
   * allows constant time access to any element and also minimizes the number
   * of checks performed for each access (attribute existence and types are
   * checked once on cursor creation).
   * 
   * @param attribute_name
   *          The name of the attribute.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>attribute_name == null</code>.</li>
   *           <li>
   *           <code>getAttribute(attribute_name).getElements() != 2</code></li>
   *           <li>
   *           <code>getAttribute(attribute_name).getType() != TYPE_FLOAT</code>
   *           </li>
   *           </ul>
   */

  public @Nonnull CursorWritable2f getCursor2f(
    final @Nonnull String attribute_name)
    throws ConstraintError
  {
    final ArrayBufferTypeDescriptor d =
      this.buffer.getType().getTypeDescriptor();
    final ArrayBufferAttributeDescriptor a = d.getAttribute(attribute_name);

    Constraints.constrainArbitrary(
      a.getElements() == 2,
      "Attribute has two elements");
    Constraints.constrainArbitrary(
      a.getType() == JCGLScalarType.TYPE_FLOAT,
      "Attribute elements are of type float");

    return new ByteBufferCursorWritable2f(
      this.target_data,
      this.target_range,
      d.getAttributeOffset(attribute_name),
      d.getSize());
  }

  /**
   * Retrieve a cursor that may only point to elements of the attribute
   * <code>attribute_name</code> in the array data. The cursor interface
   * allows constant time access to any element and also minimizes the number
   * of checks performed for each access (attribute existence and types are
   * checked once on cursor creation).
   * 
   * @param attribute_name
   *          The name of the attribute.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>attribute_name == null</code>.</li>
   *           <li>
   *           <code>getAttribute(attribute_name).getElements() != 3</code></li>
   *           <li>
   *           <code>getAttribute(attribute_name).getType() != TYPE_FLOAT</code>
   *           </li>
   *           </ul>
   */

  public @Nonnull CursorWritable3f getCursor3f(
    final @Nonnull String attribute_name)
    throws ConstraintError
  {
    final ArrayBufferTypeDescriptor d =
      this.buffer.getType().getTypeDescriptor();
    final ArrayBufferAttributeDescriptor a = d.getAttribute(attribute_name);

    Constraints.constrainArbitrary(
      a.getElements() == 3,
      "Attribute has three elements");
    Constraints.constrainArbitrary(
      a.getType() == JCGLScalarType.TYPE_FLOAT,
      "Attribute elements are of type float");

    return new ByteBufferCursorWritable3f(
      this.target_data,
      this.target_range,
      d.getAttributeOffset(attribute_name),
      d.getSize());
  }

  /**
   * Retrieve a cursor that may only point to elements of the attribute
   * <code>attribute_name</code> in the array data. The cursor interface
   * allows constant time access to any element and also minimizes the number
   * of checks performed for each access (attribute existence and types are
   * checked once on cursor creation).
   * 
   * @param attribute_name
   *          The name of the attribute.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>attribute_name == null</code>.</li>
   *           <li>
   *           <code>getAttribute(attribute_name).getElements() != 4</code></li>
   *           <li>
   *           <code>getAttribute(attribute_name).getType() != TYPE_FLOAT</code>
   *           </li>
   *           </ul>
   */

  public @Nonnull CursorWritable4f getCursor4f(
    final @Nonnull String attribute_name)
    throws ConstraintError
  {
    final ArrayBufferTypeDescriptor d =
      this.buffer.getType().getTypeDescriptor();
    final ArrayBufferAttributeDescriptor a = d.getAttribute(attribute_name);

    Constraints.constrainArbitrary(
      a.getElements() == 4,
      "Attribute has four elements");
    Constraints.constrainArbitrary(
      a.getType() == JCGLScalarType.TYPE_FLOAT,
      "Attribute elements are of type float");

    return new ByteBufferCursorWritable4f(
      this.target_data,
      this.target_range,
      d.getAttributeOffset(attribute_name),
      d.getSize());
  }

  /**
   * Retrieve the data that will be used to update the array buffer.
   */

  @Nonnull ByteBuffer getTargetData()
  {
    return this.target_data;
  }

  /**
   * Return the offset in bytes of the area of the array buffer to be updated.
   */

  long getTargetDataOffset()
  {
    return this.target_data_offset;
  }

  /**
   * Return the size in bytes of the area of the array buffer to be updated.
   */

  long getTargetDataSize()
  {
    return this.target_data_size;
  }
}
