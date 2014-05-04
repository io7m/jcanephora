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

package com.io7m.jcanephora.jogl;

import java.nio.ByteBuffer;
import java.util.Map;

import com.io7m.jcanephora.ArrayAttributeDescriptor;
import com.io7m.jcanephora.ArrayBufferType;
import com.io7m.jcanephora.ArrayBufferUpdateMappedType;
import com.io7m.jcanephora.ArrayBufferUsableType;
import com.io7m.jcanephora.ArrayDescriptor;
import com.io7m.jcanephora.CursorWritable2fType;
import com.io7m.jcanephora.CursorWritable3fType;
import com.io7m.jcanephora.CursorWritable4fType;
import com.io7m.jcanephora.JCGLExceptionMissingAttribute;
import com.io7m.jcanephora.JCGLExceptionTypeError;
import com.io7m.jcanephora.JCGLScalarType;
import com.io7m.jcanephora.cursors.ByteBufferCursorWritable2f;
import com.io7m.jcanephora.cursors.ByteBufferCursorWritable3f;
import com.io7m.jcanephora.cursors.ByteBufferCursorWritable4f;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeInclusiveL;

/**
 * An array buffer, mapped in writable mode.
 */

public final class ArrayBufferUpdateMapped implements
  ArrayBufferUpdateMappedType
{
  private final ArrayBufferType buffer;
  private final ByteBuffer      map;
  private final RangeInclusiveL target_range;

  private ArrayBufferUpdateMapped(
    final ArrayBufferType in_buffer,
    final ByteBuffer in_map)
  {
    this.buffer = NullCheck.notNull(in_buffer, "Buffer");
    this.map = NullCheck.notNull(in_map, "Map");
    this.target_range =
      new RangeInclusiveL(0, this.buffer.bufferGetRange().getInterval() - 1);
  }

  private ArrayDescriptor checkCursorType(
    final String attribute_name,
    final int required_components,
    final JCGLScalarType required_type)
    throws JCGLExceptionMissingAttribute,
      JCGLExceptionTypeError
  {
    final ArrayDescriptor d = this.buffer.arrayGetDescriptor();
    final Map<String, ArrayAttributeDescriptor> attrs = d.getAttributes();

    if (attrs.containsKey(attribute_name) == false) {
      throw JCGLExceptionMissingAttribute.noSuchAttribute(attribute_name);
    }

    final ArrayAttributeDescriptor a = attrs.get(attribute_name);
    a.checkTypes(required_components, required_type);
    return d;
  }

  @Override public ArrayBufferUsableType getArrayBuffer()
  {
    return this.buffer;
  }

  @Override public ByteBuffer getByteBuffer()
  {
    return this.map;
  }

  @Override public CursorWritable2fType getCursor2f(
    final String name)
    throws JCGLExceptionMissingAttribute,
      JCGLExceptionTypeError
  {
    final ArrayDescriptor d =
      this.checkCursorType(name, 2, JCGLScalarType.TYPE_FLOAT);

    return new ByteBufferCursorWritable2f(
      this.map,
      this.target_range,
      d.getAttributeOffset(name),
      d.getElementSizeBytes());
  }

  @Override public CursorWritable3fType getCursor3f(
    final String name)
    throws JCGLExceptionMissingAttribute,
      JCGLExceptionTypeError
  {
    final ArrayDescriptor d =
      this.checkCursorType(name, 3, JCGLScalarType.TYPE_FLOAT);

    return new ByteBufferCursorWritable3f(
      this.map,
      this.target_range,
      d.getAttributeOffset(name),
      d.getElementSizeBytes());
  }

  @Override public CursorWritable4fType getCursor4f(
    final String attribute_name)
    throws JCGLExceptionMissingAttribute,
      JCGLExceptionTypeError
  {
    final ArrayDescriptor d =
      this.checkCursorType(attribute_name, 4, JCGLScalarType.TYPE_FLOAT);

    return new ByteBufferCursorWritable4f(
      this.map,
      this.target_range,
      d.getAttributeOffset(attribute_name),
      d.getElementSizeBytes());
  }
}
