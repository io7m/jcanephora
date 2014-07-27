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

import com.io7m.jcanephora.ArrayBufferUsableType;
import com.io7m.jcanephora.IndexBufferReadMappedType;
import com.io7m.jcanephora.IndexBufferType;
import com.io7m.jcanephora.IndexBufferUpdateMappedType;
import com.io7m.jcanephora.IndexBufferUpdateUnmappedType;
import com.io7m.jcanephora.IndexBufferUsableType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionBufferMappedMultiple;
import com.io7m.jcanephora.JCGLExceptionBufferMappedNot;
import com.io7m.jcanephora.JCGLExceptionDeleted;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.JCGLExceptionWrongContext;
import com.io7m.jcanephora.JCGLUnsignedType;
import com.io7m.jcanephora.ResourceCheck;
import com.io7m.jcanephora.UsageHint;
import com.io7m.jcanephora.api.JCGLIndexBuffersMappedType;
import com.io7m.jcanephora.api.JCGLIndexBuffersType;
import com.io7m.jlog.LogLevel;
import com.io7m.jlog.LogType;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeCheck;
import com.io7m.jranges.RangeInclusiveL;

final class FakeIndexBuffers implements
  JCGLIndexBuffersType,
  JCGLIndexBuffersMappedType
{
  /**
   * Check that the given index buffer:
   * <ul>
   * <li>Is not null</li>
   * <li>Was created by this context (or a shared context)</li>
   * <li>Is not deleted</li>
   * </ul>
   */

  static void checkIndex(
    final FakeContext ctx,
    final IndexBufferUsableType index)
    throws JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    NullCheck.notNull(index, "Index buffer");
    FakeCompatibilityChecks.checkIndexBuffer(ctx, index);
    ResourceCheck.notDeleted(index);
  }

  private final Map<Integer, ByteBuffer>   buffers;
  private final FakeContext                context;
  private final LogType                    log;
  private final Set<IndexBufferUsableType> mapped;
  private int                              pool;
  private final FakeLogMessageCacheType    tcache;

  FakeIndexBuffers(
    final FakeContext in_gl,
    final LogUsableType in_log,
    final FakeLogMessageCacheType in_tcache)
  {
    this.context = NullCheck.notNull(in_gl, "FakeContext");
    this.log = NullCheck.notNull(in_log, "Log").with("index");
    this.tcache = NullCheck.notNull(in_tcache, "Log message cache");
    this.mapped = new HashSet<IndexBufferUsableType>();
    this.buffers = new HashMap<Integer, ByteBuffer>();
    this.pool = 1;
  }

  @Override public IndexBufferType indexBufferAllocate(
    final ArrayBufferUsableType buffer,
    final long indices,
    final UsageHint usage)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    FakeArrays.checkArray(this.context, buffer);
    NullCheck.notNull(usage, "Usage");

    RangeCheck.checkIncludedIn(
      indices,
      "Index count",
      RangeCheck.POSITIVE_INTEGER,
      "Valid index count range");

    JCGLUnsignedType type = JCGLUnsignedType.TYPE_UNSIGNED_BYTE;
    if (buffer.bufferGetRange().getInterval() > 0xff) {
      type = JCGLUnsignedType.TYPE_UNSIGNED_SHORT;
    }
    if (buffer.bufferGetRange().getInterval() > 0xffff) {
      type = JCGLUnsignedType.TYPE_UNSIGNED_INT;
    }

    return this.indexBufferAllocateType(type, indices, usage);
  }

  @Override public IndexBufferType indexBufferAllocateType(
    final JCGLUnsignedType type,
    final long indices,
    final UsageHint usage)
    throws JCGLExceptionRuntime
  {
    NullCheck.notNull(type, "Index type");
    NullCheck.notNull(usage, "Usage");

    RangeCheck.checkIncludedIn(
      indices,
      "Index count",
      RangeCheck.POSITIVE_INTEGER,
      "Valid index count range");

    final long size = type.getSizeBytes();
    final long bytes_total = indices * size;

    final StringBuilder text = this.tcache.getTextCache();

    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("allocate (");
      text.append(indices);
      text.append(" elements, ");
      text.append(size);
      text.append(" bytes per element, ");
      text.append(bytes_total);
      text.append(" bytes)");
      final String s = text.toString();
      assert s != null;
      this.log.debug(s);
    }

    final int id = this.pool;
    ++this.pool;
    this.buffers.put(
      Integer.valueOf(id),
      ByteBuffer.allocate((int) bytes_total));

    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("allocated ");
      text.append(id);
      final String s = text.toString();
      assert s != null;
      this.log.debug(s);
    }

    final RangeInclusiveL range = new RangeInclusiveL(0, indices - 1);
    return new FakeIndexBuffer(this.context, id, range, type, usage);
  }

  @Override public void indexBufferDelete(
    final IndexBufferType id)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    FakeIndexBuffers.checkIndex(this.context, id);

    final StringBuilder text = this.tcache.getTextCache();

    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("delete ");
      text.append(id);
      final String s = text.toString();
      assert s != null;
      this.log.debug(s);
    }

    ((FakeIndexBuffer) id).resourceSetDeleted();
    this.buffers.remove(Integer.valueOf(id.getGLName()));
  }

  @Override public IndexBufferReadMappedType indexBufferMapRead(
    final IndexBufferUsableType id)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted,
      JCGLExceptionBufferMappedMultiple
  {
    NullCheck.notNull(id, "Index buffer");
    return this.indexBufferMapReadRange(id, id.bufferGetRange());
  }

  @Override public IndexBufferReadMappedType indexBufferMapReadRange(
    final IndexBufferUsableType id,
    final RangeInclusiveL range)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted,
      JCGLExceptionBufferMappedMultiple
  {
    FakeIndexBuffers.checkIndex(this.context, id);
    NullCheck.notNull(range, "Range");

    final StringBuilder text = this.tcache.getTextCache();

    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("map ");
      text.append(id);
      final String s = text.toString();
      assert s != null;
      this.log.debug(s);
    }

    this.mappingAdd(id);

    final ByteBuffer b = this.buffers.get(id.getGLName());
    assert b != null;
    return new FakeIndexBufferReadableMap(id, b);
  }

  @Override public IndexBufferUpdateMappedType indexBufferMapWrite(
    final IndexBufferType id)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted,
      JCGLExceptionBufferMappedMultiple
  {
    FakeIndexBuffers.checkIndex(this.context, id);

    final StringBuilder text = this.tcache.getTextCache();

    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("map ");
      text.append(id);
      final String s = text.toString();
      assert s != null;
      this.log.debug(s);
    }

    this.mappingAdd(id);
    final ByteBuffer b = this.buffers.get(id.getGLName());

    assert b != null;
    return new FakeIndexBufferUpdateMapped(id, b);
  }

  @Override public void indexBufferUnmap(
    final IndexBufferUsableType id)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted,
      JCGLExceptionBufferMappedNot
  {
    FakeIndexBuffers.checkIndex(this.context, id);

    final StringBuilder text = this.tcache.getTextCache();

    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("unmap ");
      text.append(id);
      final String s = text.toString();
      assert s != null;
      this.log.debug(s);
    }

    this.mappingDelete(id);
  }

  @Override public void indexBufferUpdate(
    final IndexBufferUpdateUnmappedType data)
    throws JCGLException
  {
    NullCheck.notNull(data, "Index buffer update");
    final IndexBufferUsableType b = data.getIndexBuffer();
    FakeIndexBuffers.checkIndex(this.context, b);
  }

  private void mappingAdd(
    final IndexBufferUsableType index)
    throws JCGLExceptionBufferMappedMultiple
  {
    if (this.mapped.contains(index)) {
      throw JCGLExceptionBufferMappedMultiple.ofIndex(index);
    }
    this.mapped.add(index);
  }

  private void mappingDelete(
    final IndexBufferUsableType index)
    throws JCGLExceptionBufferMappedNot
  {
    if (this.mapped.contains(index) == false) {
      throw JCGLExceptionBufferMappedNot.ofIndex(index);
    }

    this.mapped.remove(index);
  }
}
