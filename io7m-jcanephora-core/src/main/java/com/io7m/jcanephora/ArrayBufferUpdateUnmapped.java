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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;

import com.io7m.jcanephora.cursors.ByteBufferCursorWritable2f;
import com.io7m.jcanephora.cursors.ByteBufferCursorWritable3f;
import com.io7m.jcanephora.cursors.ByteBufferCursorWritable4f;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeCheck;
import com.io7m.jranges.RangeCheckException;
import com.io7m.jranges.RangeInclusiveL;

/**
 * An allocated region of data, to replace or update an array buffer.
 */

public final class ArrayBufferUpdateUnmapped implements
  ArrayBufferUpdateUnmappedType
{
  /**
   * @return An {@link ArrayBufferUpdateUnmappedConstructorType} that uses the
   *         functions defined in this class.
   */

  public static ArrayBufferUpdateUnmappedConstructorType newConstructor()
  {
    return new ArrayBufferUpdateUnmappedConstructorType() {
      @Override public ArrayBufferUpdateUnmappedType newUpdateReplacingAll(
        final ArrayBufferType in_array)
        throws JCGLExceptionDeleted
      {
        return ArrayBufferUpdateUnmapped.newUpdateReplacingAll(in_array);
      }

      @Override public ArrayBufferUpdateUnmappedType newUpdateReplacingRange(
        final ArrayBufferType in_array,
        final RangeInclusiveL in_range)
        throws RangeCheckException,
          JCGLExceptionDeleted
      {
        return ArrayBufferUpdateUnmapped.newUpdateReplacingRange(
          in_array,
          in_range);
      }
    };
  }

  /**
   * Construct a buffer of data that will be used to replace the entirety of
   * the data in <code>buffer</code> on the GPU.
   *
   * @param array
   *          The array buffer.
   * @return An array update.
   * @throws JCGLExceptionDeleted
   *           If the array is deleted.
   */

  public static ArrayBufferUpdateUnmappedType newUpdateReplacingAll(
    final ArrayBufferType array)
    throws JCGLExceptionDeleted
  {
    NullCheck.notNull(array, "Array");
    return new ArrayBufferUpdateUnmapped(array, array.bufferGetRange());
  }

  /**
   * Construct a buffer of data that will be used to replace elements in the
   * range <code>range</code> of the data in <code>buffer</code> on the GPU.
   *
   * @return An array update.
   * @param array
   *          The array buffer.
   * @param range
   *          The range of elements to replace.
   * @throws RangeCheckException
   *           If the given range is not included in the buffer's range.
   * @throws JCGLExceptionDeleted
   *           If the array is deleted.
   */

  public static ArrayBufferUpdateUnmappedType newUpdateReplacingRange(
    final ArrayBufferType array,
    final RangeInclusiveL range)
    throws RangeCheckException,
      JCGLExceptionDeleted
  {
    return new ArrayBufferUpdateUnmapped(array, range);
  }

  private final ArrayBufferType buffer;
  private final RangeInclusiveL range;
  private final ByteBuffer      target_data;
  private final long            target_data_offset;
  private final long            target_data_size;
  private final RangeInclusiveL target_range;

  private ArrayBufferUpdateUnmapped(
    final ArrayBufferType in_buffer,
    final RangeInclusiveL in_range)
    throws RangeCheckException,
      JCGLExceptionDeleted
  {
    this.buffer = NullCheck.notNull(in_buffer, "Array buffer");
    this.range = NullCheck.notNull(in_range, "Range");

    RangeCheck.checkRangeIncludedIn(
      in_range,
      "Target range",
      in_buffer.bufferGetRange(),
      "Buffer range");

    if (this.buffer.resourceIsDeleted()) {
      final String s =
        String.format("Array %s is already deleted", in_buffer);
      assert s != null;
      throw new JCGLExceptionDeleted(s);
    }

    this.target_range =
      new RangeInclusiveL(0, this.buffer.bufferGetRange().getInterval() - 1);
    this.target_data_size =
      in_range.getInterval() * in_buffer.bufferGetElementSizeBytes();
    this.target_data_offset =
      in_range.getLower() * in_buffer.bufferGetElementSizeBytes();

    final ByteBuffer data =
      ByteBuffer.allocateDirect((int) this.target_data_size);
    data.order(ByteOrder.nativeOrder());

    this.target_data = data;
  }

  private ArrayDescriptor checkCursorType(
    final String attribute_name,
    final int required_components,
    final JCGLScalarType required_type)
    throws JCGLExceptionAttributeMissing,
      JCGLExceptionTypeError
  {
    final ArrayDescriptor d = this.buffer.arrayGetDescriptor();
    final Map<String, ArrayAttributeDescriptor> attrs = d.getAttributes();

    if (attrs.containsKey(attribute_name) == false) {
      throw JCGLExceptionAttributeMissing.noSuchAttribute(attribute_name);
    }

    final ArrayAttributeDescriptor a = attrs.get(attribute_name);
    a.checkTypes(required_components, required_type);
    return d;
  }

  @Override public ArrayBufferUsableType getArrayBuffer()
  {
    return this.buffer;
  }

  @Override public CursorWritable2fType getCursor2f(
    final String attribute_name)
    throws JCGLExceptionAttributeMissing,
      JCGLExceptionTypeError
  {
    final ArrayDescriptor d =
      this.checkCursorType(attribute_name, 2, JCGLScalarType.TYPE_FLOAT);

    return ByteBufferCursorWritable2f.newCursor(
      this.target_data,
      this.target_range,
      d.getAttributeOffset(attribute_name),
      d.getElementSizeBytes());
  }

  @Override public CursorWritable3fType getCursor3f(
    final String attribute_name)
    throws JCGLExceptionAttributeMissing,
      JCGLExceptionTypeError
  {
    final ArrayDescriptor d =
      this.checkCursorType(attribute_name, 3, JCGLScalarType.TYPE_FLOAT);

    return ByteBufferCursorWritable3f.newCursor(
      this.target_data,
      this.target_range,
      d.getAttributeOffset(attribute_name),
      d.getElementSizeBytes());
  }

  @Override public CursorWritable4fType getCursor4f(
    final String attribute_name)
    throws JCGLExceptionAttributeMissing,
      JCGLExceptionTypeError
  {
    final ArrayDescriptor d =
      this.checkCursorType(attribute_name, 4, JCGLScalarType.TYPE_FLOAT);

    return ByteBufferCursorWritable4f.newCursor(
      this.target_data,
      this.target_range,
      d.getAttributeOffset(attribute_name),
      d.getElementSizeBytes());
  }

  @Override public ByteBuffer getTargetData()
  {
    return this.target_data;
  }

  @Override public long getTargetDataOffset()
  {
    return this.target_data_offset;
  }

  @Override public long getTargetDataSize()
  {
    return this.target_data_size;
  }
}
