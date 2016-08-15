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

package com.io7m.jcanephora.lwjgl3;

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
import com.io7m.jcanephora.core.api.JCGLByteBufferProducerType;
import com.io7m.jcanephora.core.api.JCGLIndexBuffersType;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeCheck;
import com.io7m.jranges.Ranges;
import com.io7m.junsigned.ranges.UnsignedRangeInclusiveL;
import org.lwjgl.opengl.GL15;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Optional;

final class LWJGL3IndexBuffers implements JCGLIndexBuffersType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(LWJGL3IndexBuffers.class);
  }

  private final LWJGL3Context context;
  private LWJGL3ArrayObjects array_objects;

  LWJGL3IndexBuffers(
    final LWJGL3Context c)
  {
    this.context = NullCheck.notNull(c);

    /**
     * Configure baseline defaults.
     */

    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    LWJGL3ErrorChecking.checkErrors();
  }

  private static JCGLExceptionBufferNotBound notBound(
    final JCGLIndexBufferUsableType ii,
    final Optional<JCGLIndexBufferUsableType> i_opt)
  {
    final StringBuilder sb = new StringBuilder(128);
    sb.append("Buffer is not bound.");
    sb.append(System.lineSeparator());
    sb.append("  Required: ");
    sb.append(ii);
    sb.append(System.lineSeparator());
    sb.append("  Actual: ");
    sb.append(i_opt);
    return new JCGLExceptionBufferNotBound(sb.toString());
  }

  void setArrayObjects(
    final LWJGL3ArrayObjects ao)
  {
    this.array_objects = NullCheck.notNull(ao);
  }

  @Override
  public ByteBuffer indexBufferRead(
    final JCGLIndexBufferUsableType i,
    final JCGLByteBufferProducerType f)
    throws JCGLException, JCGLExceptionDeleted, JCGLExceptionBufferNotBound
  {
    NullCheck.notNull(i);
    this.checkIndexBuffer(i);

    final JCGLArrayObjectUsableType ao =
      this.array_objects.arrayObjectGetCurrentlyBound();

    final Optional<JCGLIndexBufferUsableType> i_opt = ao.getIndexBufferBound();
    if (i_opt.isPresent()) {
      final JCGLIndexBufferUsableType current_ib = i_opt.get();
      if (i.equals(current_ib)) {
        final long size = i.getRange().getInterval();
        final ByteBuffer b = f.apply(size);
        GL15.glGetBufferSubData(GL15.GL_ELEMENT_ARRAY_BUFFER, 0L, b);
        return b;
      }
    }

    throw LWJGL3IndexBuffers.notBound(i, i_opt);
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

    if (LWJGL3IndexBuffers.LOG.isDebugEnabled()) {
      LWJGL3IndexBuffers.LOG.debug(
        "allocate {} {} ({} bytes, {})",
        Long.toUnsignedString(size),
        type,
        Long.valueOf(size),
        usage);
    }

    final int id = GL15.glGenBuffers();
    if (LWJGL3IndexBuffers.LOG.isDebugEnabled()) {
      LWJGL3IndexBuffers.LOG.debug("allocated {}", Integer.valueOf(id));
    }

    final LWJGL3IndexBuffer ib =
      new LWJGL3IndexBuffer(this.context, id, indices, type, size, usage);
    this.actualBind(ib);

    GL15.glBufferData(
      GL15.GL_ELEMENT_ARRAY_BUFFER,
      size,
      LWJGL3TypeConversions.usageHintToGL(usage));
    return ib;
  }

  private void actualBind(final LWJGL3IndexBuffer ib)
  {
    final LWJGL3ArrayObject ao =
      (LWJGL3ArrayObject) this.array_objects.arrayObjectGetCurrentlyBound();

    ao.setIndexBuffer(
      ib_opt -> {
        if (LWJGL3IndexBuffers.LOG.isTraceEnabled()) {
          LWJGL3IndexBuffers.LOG.trace("bind {}/{} -> {}", ao, ib_opt, ib);
        }

        if (ib_opt.isPresent()) {
          final JCGLIndexBufferUsableType current = ib_opt.get();
          if (current.equals(ib)) {
            return ib_opt;
          }
        }

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ib.getGLName());
        return Optional.of(ib);
      });
  }

  private void actualUnbind()
  {
    final LWJGL3ArrayObject ao =
      (LWJGL3ArrayObject) this.array_objects.arrayObjectGetCurrentlyBound();

    ao.setIndexBuffer(
      ib_opt -> {
        LWJGL3IndexBuffers.LOG.trace("unbind {}/{}", ao, ib_opt);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
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

  private LWJGL3IndexBuffer checkIndexBuffer(final JCGLIndexBufferUsableType i)
  {
    NullCheck.notNull(i);
    LWJGL3IndexBuffer.checkIndexBuffer(this.context, i);
    JCGLResources.checkNotDeleted(i);
    return (LWJGL3IndexBuffer) i;
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
    final LWJGL3IndexBuffer i = this.checkIndexBuffer(ii);

    GL15.glDeleteBuffers(i.getGLName());
    i.setDeleted();

    for (final JCGLReferenceContainerType c : i.getReferringContainers()) {
      if (c instanceof LWJGL3ArrayObject) {
        final LWJGL3ArrayObject ao = (LWJGL3ArrayObject) c;
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
        GL15.glBufferSubData(
          GL15.GL_ELEMENT_ARRAY_BUFFER, r.getLower(), data);
        return;
      }
    }

    throw LWJGL3IndexBuffers.notBound(ii, i_opt);
  }

  @Override
  public void indexBufferReallocate(
    final JCGLIndexBufferUsableType i)
    throws JCGLException, JCGLExceptionDeleted, JCGLExceptionBufferNotBound
  {
    this.checkIndexBuffer(i);

    final JCGLArrayObjectUsableType ao =
      this.array_objects.arrayObjectGetCurrentlyBound();

    final Optional<JCGLIndexBufferUsableType> i_opt = ao.getIndexBufferBound();
    if (i_opt.isPresent()) {
      final JCGLIndexBufferUsableType current_ib = i_opt.get();
      if (i.equals(current_ib)) {
        if (LWJGL3IndexBuffers.LOG.isTraceEnabled()) {
          final int id = current_ib.getGLName();
          LWJGL3IndexBuffers.LOG.trace("reallocated {}", Integer.valueOf(id));
        }

        final UnsignedRangeInclusiveL r = i.getRange();
        final long size = r.getInterval();
        final int usage = LWJGL3TypeConversions.usageHintToGL(i.getUsageHint());
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, size, usage);
        return;
      }
    }

    throw LWJGL3IndexBuffers.notBound(i, i_opt);
  }

  @Override
  public boolean indexBufferIsBound()
  {
    final JCGLArrayObjectUsableType ao =
      this.array_objects.arrayObjectGetCurrentlyBound();
    return ao.getIndexBufferBound().isPresent();
  }
}
