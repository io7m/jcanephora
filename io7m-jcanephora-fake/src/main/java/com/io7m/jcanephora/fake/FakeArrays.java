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

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.io7m.jcanephora.ArrayBufferType;
import com.io7m.jcanephora.ArrayBufferUpdateMappedType;
import com.io7m.jcanephora.ArrayBufferUpdateUnmappedType;
import com.io7m.jcanephora.ArrayBufferUsableType;
import com.io7m.jcanephora.ArrayDescriptor;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionBufferMappedCorrupted;
import com.io7m.jcanephora.JCGLExceptionBufferMappedMultiple;
import com.io7m.jcanephora.JCGLExceptionBufferMappedNot;
import com.io7m.jcanephora.JCGLExceptionDeleted;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.JCGLExceptionWrongContext;
import com.io7m.jcanephora.ResourceCheck;
import com.io7m.jcanephora.UsageHint;
import com.io7m.jcanephora.api.JCGLArrayBuffersMappedType;
import com.io7m.jcanephora.api.JCGLArrayBuffersType;
import com.io7m.jlog.LogLevel;
import com.io7m.jlog.LogType;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeCheck;
import com.io7m.jranges.RangeInclusiveL;

final class FakeArrays implements
  JCGLArrayBuffersType,
  JCGLArrayBuffersMappedType
{
  /**
   * Check that the given array:
   * <ul>
   * <li>Is not null</li>
   * <li>Was created by this context (or a shared context)</li>
   * <li>Is not deleted</li>
   * </ul>
   */

  static void checkArray(
    final FakeContext ctx,
    final ArrayBufferUsableType array)
    throws JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    NullCheck.notNull(array, "Array");
    FakeCompatibilityChecks.checkArray(ctx, array);
    ResourceCheck.notDeleted(array);
  }

  private int                              binding;
  private final Map<Integer, ByteBuffer>   buffers;
  private final FakeContext                context;
  private final LogType                    log;
  private final Set<ArrayBufferUsableType> mapped;
  private int                              pool;
  private final FakeLogMessageCacheType    tcache;

  FakeArrays(
    final FakeContext in_context,
    final LogUsableType in_log,
    final FakeLogMessageCacheType in_tcache)
  {
    this.log = NullCheck.notNull(in_log, "Log").with("array");
    this.mapped = new HashSet<ArrayBufferUsableType>();
    this.tcache = NullCheck.notNull(in_tcache, "Text cache");
    this.context = NullCheck.notNull(in_context, "Context");
    this.buffers = new HashMap<Integer, ByteBuffer>();
    this.pool = 1;
  }

  @Override public ArrayBufferType arrayBufferAllocate(
    final long elements,
    final ArrayDescriptor descriptor,
    final UsageHint usage)
    throws JCGLExceptionRuntime
  {
    NullCheck.notNull(descriptor, "Array descriptor");
    NullCheck.notNull(usage, "Usage hint");
    RangeCheck.checkIncludedIn(
      elements,
      "Element count",
      RangeCheck.POSITIVE_INTEGER,
      "Valid element count");

    final long size = descriptor.getElementSizeBytes();
    final long bytes_total = elements * size;
    final RangeInclusiveL range = new RangeInclusiveL(0, elements - 1);

    final StringBuilder text = this.tcache.getTextCache();

    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("allocate (");
      text.append(elements);
      text.append(" elements, ");
      text.append(size);
      text.append(" bytes per element, ");
      text.append(bytes_total);
      text.append(" bytes, usage ");
      text.append(usage);
      text.append("))");
      final String r = text.toString();
      assert r != null;
      this.log.debug(r);
    }

    final int id = this.pool;
    ++this.pool;
    this.binding = id;
    try {
      if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
        text.setLength(0);
        text.append("allocated ");
        text.append(id);
        final String r = text.toString();
        assert r != null;
        this.log.debug(r);
      }

      this.buffers.put(
        Integer.valueOf(id),
        ByteBuffer.allocate((int) bytes_total));

    } finally {
      this.binding = 0;
    }

    return new FakeArrayBuffer(
      NullCheck.notNull(this.context),
      id,
      range,
      descriptor,
      usage);
  }

  @Override public boolean arrayBufferAnyIsBound()
  {
    return this.binding != 0;
  }

  @Override public void arrayBufferBind(
    final ArrayBufferUsableType array)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    FakeArrays.checkArray(this.context, array);
    this.binding = array.getGLName();
  }

  @Override public void arrayBufferDelete(
    final ArrayBufferType array)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    FakeArrays.checkArray(this.context, array);

    final StringBuilder text = this.tcache.getTextCache();

    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("delete ");
      text.append(array);
      final String r = text.toString();
      assert r != null;
      this.log.debug(r);
    }

    ((FakeArrayBuffer) array).resourceSetDeleted();
    if (this.binding == array.getGLName()) {
      this.binding = 0;
    }

    this.buffers.remove(Integer.valueOf(array.getGLName()));
  }

  @Override public boolean arrayBufferIsBound(
    final ArrayBufferUsableType array)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    FakeArrays.checkArray(this.context, array);
    return this.binding == array.getGLName();
  }

  @Override public boolean arrayBufferIsMapped(
    final ArrayBufferUsableType id)
    throws JCGLException
  {
    FakeArrays.checkArray(this.context, id);
    return this.mapped.contains(id);
  }

  @Override public ByteBuffer arrayBufferMapReadUntyped(
    final ArrayBufferUsableType array)
    throws JCGLException
  {
    FakeArrays.checkArray(this.context, array);
    return this.arrayBufferMapReadUntypedRange(array, array.bufferGetRange());
  }

  @Override public ByteBuffer arrayBufferMapReadUntypedRange(
    final ArrayBufferUsableType array,
    final RangeInclusiveL range)
    throws JCGLException
  {
    FakeArrays.checkArray(this.context, array);

    NullCheck.notNull(range, "Range");
    RangeCheck.checkRangeIncludedIn(
      range,
      "Mapped range",
      array.bufferGetRange(),
      "Array range");

    final StringBuilder text = this.tcache.getTextCache();

    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("map ");
      text.append(array);
      text.append(" range ");
      text.append(range);
      final String s = text.toString();
      assert s != null;
      this.log.debug(s);
    }

    this.mappingAdd(array);
    this.binding = array.getGLName();

    ByteBuffer b;
    try {
      b = this.buffers.get(Integer.valueOf(array.getGLName()));
    } finally {
      this.binding = 0;
    }

    assert b != null;
    return b;
  }

  @Override public ArrayBufferUpdateMappedType arrayBufferMapWrite(
    final ArrayBufferType array)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted,
      JCGLExceptionBufferMappedMultiple
  {
    FakeArrays.checkArray(this.context, array);

    final StringBuilder text = this.tcache.getTextCache();

    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("map ");
      text.append(array);
      text.append(" range ");
      text.append(array.bufferGetRange());
      final String s = text.toString();
      assert s != null;
      this.log.debug(s);
    }

    this.mappingAdd(array);
    this.binding = array.getGLName();

    ByteBuffer b;
    try {
      b = this.buffers.get(Integer.valueOf(array.getGLName()));
    } finally {
      this.binding = 0;
    }

    assert b != null;
    return new FakeArrayBufferUpdateMapped(array, b);
  }

  @Override public void arrayBufferUnbind()
    throws JCGLExceptionRuntime
  {
    this.binding = 0;
  }

  @Override public void arrayBufferUnmap(
    final ArrayBufferUsableType array)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted,
      JCGLExceptionBufferMappedNot,
      JCGLExceptionBufferMappedCorrupted
  {
    FakeArrays.checkArray(this.context, array);

    final StringBuilder text = this.tcache.getTextCache();

    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("unmap ");
      text.append(array);
      final String s = text.toString();
      assert s != null;
      this.log.debug(s);
    }

    this.binding = array.getGLName();
    try {
      this.mappingDelete(array);
    } finally {
      this.binding = 0;
    }
  }

  @Override public void arrayBufferUpdate(
    final ArrayBufferUpdateUnmappedType data)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    NullCheck.notNull(data, "Array update");
    final ArrayBufferUsableType array = data.getArrayBuffer();
    FakeArrays.checkArray(this.context, array);

    this.arrayBufferBind(data.getArrayBuffer());
    try {
      // Nothing!
    } finally {
      this.arrayBufferUnbind();
    }
  }

  private void mappingAdd(
    final ArrayBufferUsableType array)
    throws JCGLExceptionBufferMappedMultiple
  {
    if (this.mapped.contains(array)) {
      throw JCGLExceptionBufferMappedMultiple.ofArray(array);
    }
    this.mapped.add(array);
  }

  private void mappingDelete(
    final ArrayBufferUsableType array)
    throws JCGLExceptionBufferMappedNot
  {
    if (this.mapped.contains(array) == false) {
      throw JCGLExceptionBufferMappedNot.ofArray(array);
    }

    this.mapped.remove(array);
  }
}
