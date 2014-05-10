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
import javax.media.opengl.GLContext;

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

final class JOGLArrays implements
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
    final GL gl,
    final ArrayBufferUsableType array)
    throws JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    NullCheck.notNull(array, "Array");
    final GLContext ctx = gl.getContext();
    assert ctx != null;
    JOGLCompatibilityChecks.checkArray(ctx, array);
    ResourceCheck.notDeleted(array);
  }

  private final GL                         gl;
  private final JOGLIntegerCacheType       icache;
  private final LogType                    log;
  private final Set<ArrayBufferUsableType> mapped;
  private final JOGLLogMessageCacheType    tcache;

  JOGLArrays(
    final GL in_gl,
    final LogUsableType in_log,
    final JOGLIntegerCacheType in_icache,
    final JOGLLogMessageCacheType in_tcache)
  {
    this.gl = NullCheck.notNull(in_gl, "GL");
    this.log = NullCheck.notNull(in_log, "Log").with("array");
    this.icache = NullCheck.notNull(in_icache, "Integer cache");
    this.tcache = NullCheck.notNull(in_tcache, "Log message cache");
    this.mapped = new HashSet<ArrayBufferUsableType>();
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

    final IntBuffer ix = this.icache.getIntegerCache();
    this.gl.glGenBuffers(1, ix);

    final int id = ix.get(0);
    this.gl.glBindBuffer(GL.GL_ARRAY_BUFFER, id);
    try {
      this.gl.glBufferData(
        GL.GL_ARRAY_BUFFER,
        bytes_total,
        null,
        JOGL_GLTypeConversions.usageHintES2ToGL(usage));

      if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
        text.setLength(0);
        text.append("allocated ");
        text.append(id);
        final String r = text.toString();
        assert r != null;
        this.log.debug(r);
      }

      JOGLErrors.check(this.gl);
    } finally {
      this.gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    }

    return new JOGLArrayBuffer(
      NullCheck.notNull(this.gl.getContext()),
      id,
      range,
      descriptor,
      usage);
  }

  @Override public void arrayBufferBind(
    final ArrayBufferUsableType array)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    JOGLArrays.checkArray(this.gl, array);

    this.gl.glBindBuffer(GL.GL_ARRAY_BUFFER, array.getGLName());
    JOGLErrors.check(this.gl);
  }

  @Override public void arrayBufferDelete(
    final ArrayBufferType array)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    JOGLArrays.checkArray(this.gl, array);

    final StringBuilder text = this.tcache.getTextCache();

    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("delete ");
      text.append(array);
      final String r = text.toString();
      assert r != null;
      this.log.debug(r);
    }

    final IntBuffer ix = this.icache.getIntegerCache();
    ix.put(0, array.getGLName());

    this.gl.glDeleteBuffers(1, ix);
    ((JOGLArrayBuffer) array).resourceSetDeleted();
    JOGLErrors.check(this.gl);
  }

  @Override public boolean arrayBufferIsBound(
    final ArrayBufferUsableType array)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    JOGLArrays.checkArray(this.gl, array);

    final int b = this.gl.getBoundBuffer(GL.GL_ARRAY_BUFFER);
    JOGLErrors.check(this.gl);
    return b == array.getGLName();
  }

  @Override public boolean arrayBufferIsMapped(
    final ArrayBufferUsableType id)
    throws JCGLException
  {
    JOGLArrays.checkArray(this.gl, id);
    return this.mapped.contains(id);
  }

  @Override public ByteBuffer arrayBufferMapReadUntyped(
    final ArrayBufferUsableType array)
    throws JCGLException
  {
    JOGLArrays.checkArray(this.gl, array);

    return this.arrayBufferMapReadUntypedRange(array, array.bufferGetRange());
  }

  @Override public ByteBuffer arrayBufferMapReadUntypedRange(
    final ArrayBufferUsableType array,
    final RangeInclusiveL range)
    throws JCGLException
  {
    JOGLArrays.checkArray(this.gl, array);

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

    this.gl.glBindBuffer(GL.GL_ARRAY_BUFFER, array.getGLName());
    JOGLErrors.check(this.gl);

    ByteBuffer b;
    try {
      final long offset =
        range.getLower() * array.bufferGetElementSizeBytes();
      final long length =
        range.getInterval() * array.bufferGetElementSizeBytes();

      b =
        this.gl.glMapBufferRange(
          GL.GL_ARRAY_BUFFER,
          offset,
          length,
          GL.GL_MAP_READ_BIT);
      JOGLErrors.check(this.gl);

    } finally {
      this.gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    }
    JOGLErrors.check(this.gl);

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
    JOGLArrays.checkArray(this.gl, array);

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

    this.gl.glBindBuffer(GL.GL_ARRAY_BUFFER, array.getGLName());
    JOGLErrors.check(this.gl);

    final ByteBuffer b;
    try {
      final UsageHint hint = array.arrayGetUsageHint();
      this.gl.glBufferData(
        GL.GL_ARRAY_BUFFER,
        array.resourceGetSizeBytes(),
        null,
        JOGL_GLTypeConversions.usageHintToGL(hint));
      JOGLErrors.check(this.gl);

      final RangeInclusiveL range = array.bufferGetRange();
      final long offset =
        range.getLower() * array.bufferGetElementSizeBytes();
      final long length =
        range.getInterval() * array.bufferGetElementSizeBytes();

      b =
        this.gl.glMapBufferRange(
          GL.GL_ARRAY_BUFFER,
          offset,
          length,
          GL.GL_MAP_WRITE_BIT);
      JOGLErrors.check(this.gl);

    } finally {
      this.gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    }
    JOGLErrors.check(this.gl);

    assert b != null;
    return new JOGLArrayBufferUpdateMapped(array, b);
  }

  @Override public void arrayBufferUnbind()
    throws JCGLExceptionRuntime
  {
    this.gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    JOGLErrors.check(this.gl);
  }

  @Override public void arrayBufferUnmap(
    final ArrayBufferUsableType array)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted,
      JCGLExceptionBufferMappedNot,
      JCGLExceptionBufferMappedCorrupted
  {
    JOGLArrays.checkArray(this.gl, array);

    final StringBuilder text = this.tcache.getTextCache();

    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("unmap ");
      text.append(array);
      final String s = text.toString();
      assert s != null;
      this.log.debug(s);
    }

    this.gl.glBindBuffer(GL.GL_ARRAY_BUFFER, array.getGLName());
    try {
      final boolean ok = this.gl.glUnmapBuffer(GL.GL_ARRAY_BUFFER);
      this.mappingDelete(array);
      if (ok == false) {
        throw new JCGLExceptionBufferMappedCorrupted(
          "Mapped buffer has been corrupted");
      }
    } finally {
      this.gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    }
    JOGLErrors.check(this.gl);
  }

  @Override public void arrayBufferUpdate(
    final ArrayBufferUpdateUnmappedType data)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    NullCheck.notNull(data, "Array update");
    final ArrayBufferUsableType array = data.getArrayBuffer();
    JOGLArrays.checkArray(this.gl, array);

    this.arrayBufferBind(data.getArrayBuffer());
    try {
      this.gl.glBufferSubData(
        GL.GL_ARRAY_BUFFER,
        data.getTargetDataOffset(),
        data.getTargetDataSize(),
        data.getTargetData());
      JOGLErrors.check(this.gl);
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
