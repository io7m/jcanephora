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

import com.io7m.jcanephora.core.JCGLArrayObjectUsableType;
import com.io7m.jcanephora.core.JCGLBufferUpdateType;
import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLExceptionBufferNotBound;
import com.io7m.jcanephora.core.JCGLExceptionDeleted;
import com.io7m.jcanephora.core.JCGLIndexBufferType;
import com.io7m.jcanephora.core.JCGLIndexBufferUsableType;
import com.io7m.jcanephora.core.JCGLReferenceContainerType;
import com.io7m.jcanephora.core.JCGLResources;
import com.io7m.jcanephora.core.JCGLUnsignedType;
import com.io7m.jcanephora.core.JCGLUsageHint;
import com.io7m.jcanephora.core.api.JCGLIndexBuffersType;
import com.io7m.jnull.NullCheck;
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
import java.util.Optional;

final class JOGLIndexBuffers implements JCGLIndexBuffersType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(JOGLIndexBuffers.class);
  }

  private final GL3              gl;
  private final IntBuffer        int_cache;
  private       JOGLArrayObjects array_objects;

  JOGLIndexBuffers(
    final JOGLContext c)
  {
    final JOGLContext context = NullCheck.notNull(c);

    this.gl = c.getGL3();
    this.int_cache = Buffers.newDirectIntBuffer(1);

    /**
     * Configure baseline defaults.
     */

    this.gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
    JOGLErrorChecking.checkErrors(this.gl);
  }

  void setArrayObjects(
    final JOGLArrayObjects ao)
  {
    this.array_objects = NullCheck.notNull(ao);
  }

  @Override
  public JCGLIndexBufferType indexBufferAllocate(
    final long indices,
    final JCGLUnsignedType type,
    final JCGLUsageHint usage)
  {
    NullCheck.notNull(usage);
    NullCheck.notNull(type);
    RangeCheck.checkIncludedInLong(
      indices, "Index count", Ranges.NATURAL_LONG, "Valid index counts");

    final long size = indices * (long) type.getSizeBytes();

    if (JOGLIndexBuffers.LOG.isDebugEnabled()) {
      JOGLIndexBuffers.LOG.debug(
        "allocate {} {} ({} bytes, {})",
        Long.toUnsignedString(size),
        type,
        Long.valueOf(size),
        usage);
    }

    this.int_cache.rewind();
    this.gl.glGenBuffers(1, this.int_cache);
    final int id = this.int_cache.get(0);

    if (JOGLIndexBuffers.LOG.isDebugEnabled()) {
      JOGLIndexBuffers.LOG.debug("allocated {}", Integer.valueOf(id));
    }

    final JOGLIndexBuffer ib =
      new JOGLIndexBuffer(this.gl.getContext(), id, indices, type, size, usage);
    this.actualBind(ib);

    this.gl.glBufferData(
      GL.GL_ELEMENT_ARRAY_BUFFER,
      size,
      null,
      JOGLTypeConversions.usageHintToGL(usage));

    return ib;
  }

  private void actualBind(final JOGLIndexBuffer ib)
  {
    final JOGLArrayObject ao =
      (JOGLArrayObject) this.array_objects.arrayObjectGetCurrentlyBound();

    ao.setIndexBuffer(
      ib_opt -> {
        if (JOGLIndexBuffers.LOG.isTraceEnabled()) {
          JOGLIndexBuffers.LOG.trace("bind {}/{} -> {}", ao, ib_opt, ib);
        }

        if (ib_opt.isPresent()) {
          final JCGLIndexBufferUsableType current = ib_opt.get();
          if (current.equals(ib)) {
            return ib_opt;
          }
        }

        this.gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, ib.getGLName());
        return Optional.of(ib);
      });
  }

  private void actualUnbind()
  {
    final JOGLArrayObject ao =
      (JOGLArrayObject) this.array_objects.arrayObjectGetCurrentlyBound();

    ao.setIndexBuffer(
      ib_opt -> {
        JOGLIndexBuffers.LOG.trace("unbind {}/{}", ao, ib_opt);
        this.gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
        return Optional.empty();
      });
  }

  @Override
  public Optional<JCGLIndexBufferUsableType> indexBufferGetCurrentlyBound()
    throws JCGLException
  {
    return this.array_objects.arrayObjectGetCurrentlyBound()
      .getIndexBufferBound();
  }

  @Override
  public void indexBufferBind(
    final JCGLIndexBufferUsableType i)
    throws JCGLException, JCGLExceptionDeleted
  {
    this.actualBind(this.checkIndexBuffer(i));
  }

  private JOGLIndexBuffer checkIndexBuffer(final JCGLIndexBufferUsableType i)
  {
    NullCheck.notNull(i);
    JOGLCompatibilityChecks.checkIndexBuffer(this.gl.getContext(), i);
    JCGLResources.checkNotDeleted(i);
    return (JOGLIndexBuffer) i;
  }

  @Override
  public void indexBufferUnbind()
    throws JCGLException
  {
    this.actualUnbind();
  }

  @Override
  public void indexBufferDelete(
    final JCGLIndexBufferType ii)
    throws JCGLException, JCGLExceptionDeleted
  {
    final JOGLIndexBuffer i = this.checkIndexBuffer(ii);

    this.int_cache.rewind();
    this.int_cache.put(0, i.getGLName());
    this.gl.glDeleteBuffers(1, this.int_cache);
    i.setDeleted();

    for (final JCGLReferenceContainerType c : i.getReferringContainers()) {
      if (c instanceof JOGLArrayObject) {
        final JOGLArrayObject ao = (JOGLArrayObject) c;
        ao.setIndexBuffer(ib -> Optional.empty());
      }
    }
  }

  @Override
  public void indexBufferUpdate(
    final JCGLBufferUpdateType<JCGLIndexBufferType> u)
    throws JCGLException, JCGLExceptionDeleted, JCGLExceptionBufferNotBound
  {
    NullCheck.notNull(u);
    final JCGLIndexBufferType ii = u.getBuffer();
    this.checkIndexBuffer(ii);

    final JCGLArrayObjectUsableType ao =
      this.array_objects.arrayObjectGetCurrentlyBound();

    final Optional<JCGLIndexBufferUsableType> i_opt = ao.getIndexBufferBound();
    if (i_opt.isPresent()) {
      final JCGLIndexBufferUsableType current_ib = i_opt.get();
      if (ii.equals(current_ib)) {
        final UnsignedRangeInclusiveL r = u.getDataUpdateRange();
        final ByteBuffer data = u.getData();
        data.rewind();
        this.gl.glBufferSubData(
          GL.GL_ELEMENT_ARRAY_BUFFER, r.getLower(), r.getInterval(), data);
        return;
      }
    }

    final StringBuilder sb = new StringBuilder(128);
    sb.append("Buffer is not bound.");
    sb.append(System.lineSeparator());
    sb.append("  Required: ");
    sb.append(ii);
    sb.append(System.lineSeparator());
    sb.append("  Actual: ");
    sb.append(i_opt);
    throw new JCGLExceptionBufferNotBound(sb.toString());
  }

  @Override
  public boolean indexBufferIsBound()
  {
    final JCGLArrayObjectUsableType ao =
      this.array_objects.arrayObjectGetCurrentlyBound();
    return ao.getIndexBufferBound().isPresent();
  }
}
