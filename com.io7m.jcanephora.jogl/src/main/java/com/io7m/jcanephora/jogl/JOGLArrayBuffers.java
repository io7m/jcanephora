/*
 * Copyright © 2015 <code@io7m.com> http://io7m.com
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

import com.io7m.jcanephora.core.JCGLArrayBufferType;
import com.io7m.jcanephora.core.JCGLArrayBufferUsableType;
import com.io7m.jcanephora.core.JCGLBufferUpdateType;
import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLExceptionBufferNotBound;
import com.io7m.jcanephora.core.JCGLExceptionDeleted;
import com.io7m.jcanephora.core.JCGLResources;
import com.io7m.jcanephora.core.JCGLUsageHint;
import com.io7m.jcanephora.core.api.JCGLArrayBuffersType;
import com.io7m.jcanephora.core.api.JCGLByteBufferProducerType;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;
import com.io7m.jranges.RangeCheck;
import com.io7m.jranges.Ranges;
import com.io7m.junsigned.ranges.UnsignedRangeInclusiveL;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Objects;
import java.util.Optional;

final class JOGLArrayBuffers implements JCGLArrayBuffersType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(JOGLArrayBuffers.class);
  }

  private final GL3 gl;
  private final IntBuffer int_cache;
  private @Nullable JOGLArrayBuffer bind;

  JOGLArrayBuffers(
    final JOGLContext c)
  {
    NullCheck.notNull(c, "Context");
    this.gl = c.getGL3();
    this.int_cache = Buffers.newDirectIntBuffer(1);

    /*
     * Configure baseline defaults.
     */

    this.gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    JOGLErrorChecking.checkErrors(this.gl);
  }

  @Override
  public ByteBuffer arrayBufferRead(
    final JCGLArrayBufferUsableType a,
    final JCGLByteBufferProducerType f)
    throws JCGLException, JCGLExceptionDeleted, JCGLExceptionBufferNotBound
  {
    NullCheck.notNull(a, "Array buffer");
    NullCheck.notNull(f, "Producer");
    this.checkArray(a);

    if (Objects.equals(a, this.bind)) {
      final long size = a.byteRange().getInterval();
      final ByteBuffer b = f.apply(size);
      this.gl.glGetBufferSubData(GL.GL_ARRAY_BUFFER, 0L, size, b);
      return b;
    }

    throw this.notBound(a);
  }

  @Override
  public JCGLArrayBufferType arrayBufferAllocate(
    final long size,
    final JCGLUsageHint usage)
    throws JCGLException
  {
    NullCheck.notNull(usage, "Usage");
    RangeCheck.checkIncludedInLong(
      size, "Size", Ranges.NATURAL_LONG, "Valid size range");

    if (LOG.isDebugEnabled()) {
      LOG.debug(
        "allocate ({} bytes, {})", Long.valueOf(size), usage);
    }

    this.int_cache.rewind();
    this.gl.glGenBuffers(1, this.int_cache);
    final int id = this.int_cache.get(0);

    if (LOG.isDebugEnabled()) {
      LOG.debug("allocated {}", Integer.valueOf(id));
    }

    final JOGLArrayBuffer a =
      new JOGLArrayBuffer(this.gl.getContext(), id, size, usage);
    this.actualBind(a);

    this.gl.glBufferData(
      GL.GL_ARRAY_BUFFER, size, null, JOGLTypeConversions.usageHintToGL(usage));

    return a;
  }

  @Override
  public void arrayBufferReallocate(
    final JCGLArrayBufferUsableType a)
    throws JCGLException, JCGLExceptionDeleted, JCGLExceptionBufferNotBound
  {
    this.checkArray(a);

    if (Objects.equals(a, this.bind)) {
      final UnsignedRangeInclusiveL r = a.byteRange();
      final long size = r.getInterval();
      final JCGLUsageHint usage = a.usageHint();

      if (LOG.isTraceEnabled()) {
        LOG.trace(
          "reallocate ({} bytes, {})", Long.valueOf(size), usage);
      }

      this.gl.glBufferData(
        GL.GL_ARRAY_BUFFER,
        size,
        null,
        JOGLTypeConversions.usageHintToGL(usage));
    } else {
      throw this.notBound(a);
    }
  }

  private void actualBind(final JOGLArrayBuffer a)
  {
    if (LOG.isTraceEnabled()) {
      LOG.trace("bind {} -> {}", this.bind, a);
    }

    if (!Objects.equals(a, this.bind)) {
      this.gl.glBindBuffer(GL.GL_ARRAY_BUFFER, a.glName());
      this.bind = a;
    }
  }

  private void actualUnbind()
  {
    if (LOG.isTraceEnabled()) {
      LOG.trace("unbind {} -> {}", this.bind, null);
    }

    if (this.bind != null) {
      this.gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
      this.bind = null;
    }
  }

  @Override
  public Optional<JCGLArrayBufferUsableType> arrayBufferGetCurrentlyBound()
    throws JCGLException
  {
    return Optional.ofNullable(this.bind);
  }

  @Override
  public boolean arrayBufferAnyIsBound()
    throws JCGLException
  {
    return this.bind != null;
  }

  @Override
  public boolean arrayBufferIsBound(
    final JCGLArrayBufferUsableType a)
    throws JCGLException
  {
    this.checkArray(a);
    return Objects.equals(a, this.bind);
  }

  @Override
  public void arrayBufferBind(final JCGLArrayBufferUsableType a)
    throws JCGLException, JCGLExceptionDeleted
  {
    this.checkArray(a);
    this.actualBind((JOGLArrayBuffer) a);
  }

  private void checkArray(final JCGLArrayBufferUsableType a)
  {
    JOGLArrayBuffer.checkArray(this.gl.getContext(), a);
    JCGLResources.checkNotDeleted(a);
  }

  @Override
  public void arrayBufferUnbind()
    throws JCGLException
  {
    this.actualUnbind();
  }

  @Override
  public void arrayBufferDelete(final JCGLArrayBufferType a)
    throws JCGLException, JCGLExceptionDeleted
  {
    this.checkArray(a);

    if (LOG.isDebugEnabled()) {
      LOG.debug("delete {}", Integer.valueOf(a.glName()));
    }

    this.int_cache.rewind();
    this.int_cache.put(0, a.glName());
    this.gl.glDeleteBuffers(1, this.int_cache);
    ((JOGLArrayBuffer) a).setDeleted();

    if (Objects.equals(a, this.bind)) {
      this.actualUnbind();
    }
  }

  @Override
  public void arrayBufferUpdate(
    final JCGLBufferUpdateType<JCGLArrayBufferType> u)
    throws JCGLException, JCGLExceptionDeleted, JCGLExceptionBufferNotBound
  {
    NullCheck.notNull(u, "Update");
    final JCGLArrayBufferType a = u.buffer();
    this.checkArray(a);

    if (Objects.equals(a, this.bind)) {
      final UnsignedRangeInclusiveL r = u.dataUpdateRange();
      final ByteBuffer data = u.data();
      data.rewind();
      this.gl.glBufferSubData(
        GL.GL_ARRAY_BUFFER, r.getLower(), r.getInterval(), data);
    } else {
      throw this.notBound(a);
    }
  }

  private JCGLExceptionBufferNotBound notBound(
    final JCGLArrayBufferUsableType a)
  {
    final StringBuilder sb = new StringBuilder(128);
    sb.append("Buffer is not bound.");
    sb.append(System.lineSeparator());
    sb.append("  Required: ");
    sb.append(a);
    sb.append(System.lineSeparator());
    sb.append("  Actual: ");
    sb.append(this.bind == null ? "none" : this.bind);
    return new JCGLExceptionBufferNotBound(sb.toString());
  }
}
