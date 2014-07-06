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
import java.nio.IntBuffer;
import java.util.HashSet;
import java.util.Set;

import javax.media.opengl.GL;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLContext;

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

final class JOGLIndexBuffers implements
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
    final GL gl,
    final IndexBufferUsableType index)
    throws JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    NullCheck.notNull(index, "Array");
    final GLContext ctx = gl.getContext();
    assert ctx != null;
    JOGLCompatibilityChecks.checkIndexBuffer(ctx, index);
    ResourceCheck.notDeleted(index);
  }

  private final GL                         gl;
  private final JOGLIntegerCacheType       icache;
  private final LogType                    log;
  private final Set<IndexBufferUsableType> mapped;
  private final JOGLLogMessageCacheType    tcache;

  JOGLIndexBuffers(
    final GL in_gl,
    final LogUsableType in_log,
    final JOGLIntegerCacheType in_icache,
    final JOGLLogMessageCacheType in_tcache)
  {
    this.gl = NullCheck.notNull(in_gl, "GL");
    this.log = NullCheck.notNull(in_log, "Log").with("index");
    this.icache = NullCheck.notNull(in_icache, "Integer cache");
    this.tcache = NullCheck.notNull(in_tcache, "Log message cache");
    this.mapped = new HashSet<IndexBufferUsableType>();
  }

  @Override public IndexBufferType indexBufferAllocate(
    final ArrayBufferUsableType buffer,
    final long indices,
    final UsageHint usage)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    JOGLArrays.checkArray(this.gl, buffer);
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

    final IntBuffer ix = this.icache.getIntegerCache();
    this.gl.glGenBuffers(1, ix);
    JOGLErrors.check(this.gl);

    final int id = ix.get(0);
    this.gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, id);
    JOGLErrors.check(this.gl);
    try {
      this.gl.glBufferData(
        GL.GL_ELEMENT_ARRAY_BUFFER,
        bytes_total,
        null,
        GL2ES2.GL_STREAM_DRAW);
      JOGLErrors.check(this.gl);
    } finally {
      this.gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("allocated ");
      text.append(id);
      final String s = text.toString();
      assert s != null;
      this.log.debug(s);
    }

    final RangeInclusiveL range = new RangeInclusiveL(0, indices - 1);
    final GLContext ctx = this.gl.getContext();
    assert ctx != null;
    return new JOGLIndexBuffer(ctx, id, range, type, usage);
  }

  @Override public void indexBufferDelete(
    final IndexBufferType id)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    JOGLIndexBuffers.checkIndex(this.gl, id);

    final StringBuilder text = this.tcache.getTextCache();

    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("delete ");
      text.append(id);
      final String s = text.toString();
      assert s != null;
      this.log.debug(s);
    }

    final IntBuffer ix = this.icache.getIntegerCache();
    ix.put(0, id.getGLName());
    this.gl.glDeleteBuffers(1, ix);

    ((JOGLIndexBuffer) id).resourceSetDeleted();
    JOGLErrors.check(this.gl);
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
    JOGLIndexBuffers.checkIndex(this.gl, id);
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

    this.gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, id.getGLName());
    JOGLErrors.check(this.gl);

    final ByteBuffer b;
    try {
      final long offset = range.getLower() * id.bufferGetElementSizeBytes();
      final long length =
        range.getInterval() * id.bufferGetElementSizeBytes();

      b =
        this.gl.glMapBufferRange(
          GL.GL_ELEMENT_ARRAY_BUFFER,
          offset,
          length,
          GL.GL_MAP_READ_BIT);
      JOGLErrors.check(this.gl);

    } finally {
      this.gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    assert b != null;
    return new JOGLIndexBufferReadableMap(id, b);
  }

  @Override public IndexBufferUpdateMappedType indexBufferMapWrite(
    final IndexBufferType id)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted,
      JCGLExceptionBufferMappedMultiple
  {
    JOGLIndexBuffers.checkIndex(this.gl, id);

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

    this.gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, id.getGLName());
    JOGLErrors.check(this.gl);

    ByteBuffer b;
    try {
      this.gl.glBufferData(
        GL.GL_ELEMENT_ARRAY_BUFFER,
        id.resourceGetSizeBytes(),
        null,
        GL2ES2.GL_STREAM_DRAW);
      JOGLErrors.check(this.gl);

      final RangeInclusiveL range = id.bufferGetRange();
      final long offset = range.getLower() * id.bufferGetElementSizeBytes();
      final long length =
        range.getInterval() * id.bufferGetElementSizeBytes();

      b =
        this.gl.glMapBufferRange(
          GL.GL_ELEMENT_ARRAY_BUFFER,
          offset,
          length,
          GL.GL_MAP_WRITE_BIT);
      JOGLErrors.check(this.gl);

    } finally {
      this.gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    assert b != null;
    return new JOGLIndexBufferUpdateMapped(id, b);
  }

  @Override public void indexBufferUnmap(
    final IndexBufferUsableType id)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted,
      JCGLExceptionBufferMappedNot
  {
    JOGLIndexBuffers.checkIndex(this.gl, id);

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

    this.gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, id.getGLName());
    try {
      this.gl.glUnmapBuffer(GL.GL_ELEMENT_ARRAY_BUFFER);
    } finally {
      this.gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
    }
    JOGLErrors.check(this.gl);
  }

  @Override public void indexBufferUpdate(
    final IndexBufferUpdateUnmappedType data)
    throws JCGLException
  {
    NullCheck.notNull(data, "Index buffer update");
    final IndexBufferUsableType b = data.getIndexBuffer();
    JOGLIndexBuffers.checkIndex(this.gl, b);

    this.gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, b.getGLName());
    try {
      this.gl.glBufferSubData(
        GL.GL_ELEMENT_ARRAY_BUFFER,
        data.getTargetDataOffset(),
        data.getTargetDataSize(),
        data.getTargetData());
    } finally {
      this.gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
    }
    JOGLErrors.check(this.gl);
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
