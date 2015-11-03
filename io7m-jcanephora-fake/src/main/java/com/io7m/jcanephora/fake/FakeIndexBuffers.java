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

package com.io7m.jcanephora.fake;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Optional;

final class FakeIndexBuffers implements JCGLIndexBuffersType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(FakeIndexBuffers.class);
  }

  private final FakeContext      context;
  private       FakeArrayObjects array_objects;

  FakeIndexBuffers(
    final FakeContext c)
  {
    this.context = c;
  }

  void setArrayObjects(
    final FakeArrayObjects ao)
  {
    this.array_objects = NullCheck.notNull(ao);
  }

  @Override public JCGLIndexBufferType indexBufferAllocate(
    final long indices,
    final JCGLUnsignedType type,
    final JCGLUsageHint usage)
  {
    NullCheck.notNull(usage);
    NullCheck.notNull(type);
    RangeCheck.checkIncludedInLong(
      indices, "Index count", Ranges.NATURAL_LONG, "Valid index counts");

    final long size = indices * (long) type.getSizeBytes();

    FakeIndexBuffers.LOG.debug(
      "allocate {} {} ({} bytes, {})",
      Long.toUnsignedString(size),
      type,
      Long.valueOf(size),
      usage);

    final int id = this.context.getFreshID();
    final ByteBuffer data = ByteBuffer.allocate((int) size);

    FakeIndexBuffers.LOG.debug("allocated {}", Integer.valueOf(id));

    final FakeIndexBuffer ib =
      new FakeIndexBuffer(this.context, id, indices, type, data, usage);
    this.actualBind(ib);

    return ib;
  }

  private void actualBind(final FakeIndexBuffer ib)
  {
    final FakeArrayObject ao =
      (FakeArrayObject) this.array_objects.arrayObjectGetCurrentlyBound();

    ao.setIndexBuffer(
      ib_opt -> {
        FakeIndexBuffers.LOG.trace("bind {}/{} → {}", ao, ib_opt, ib);

        if (ib_opt.isPresent()) {
          final JCGLIndexBufferUsableType current = ib_opt.get();
          if (current.equals(ib)) {
            return ib_opt;
          }
        }

        return Optional.of(ib);
      });
  }

  private void actualUnbind()
  {
    final FakeArrayObject ao =
      (FakeArrayObject) this.array_objects.arrayObjectGetCurrentlyBound();

    ao.setIndexBuffer(
      ib_opt -> {
        FakeIndexBuffers.LOG.trace("unbind {}/{}", ao, ib_opt);
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

  @Override public void indexBufferBind(
    final JCGLIndexBufferUsableType i)
    throws JCGLException, JCGLExceptionDeleted
  {
    this.actualBind(this.checkIndexBuffer(i));
  }

  private FakeIndexBuffer checkIndexBuffer(final JCGLIndexBufferUsableType i)
  {
    NullCheck.notNull(i);
    FakeCompatibilityChecks.checkIndexBuffer(this.context, i);
    JCGLResources.checkNotDeleted(i);
    return (FakeIndexBuffer) i;
  }

  @Override public void indexBufferUnbind()
    throws JCGLException
  {
    this.actualUnbind();
  }

  @Override public void indexBufferDelete(
    final JCGLIndexBufferType ii)
    throws JCGLException, JCGLExceptionDeleted
  {
    final FakeIndexBuffer i = this.checkIndexBuffer(ii);
    i.setDeleted();

    for (final JCGLReferenceContainerType c : i.getReferringContainers()) {
      if (c instanceof FakeArrayObject) {
        final FakeArrayObject ao = (FakeArrayObject) c;
        ao.setIndexBuffer(ib -> Optional.empty());
      }
    }
  }

  @Override public void indexBufferUpdate(
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
        final UnsignedRangeInclusiveL r = u.getBufferUpdateRange();
        final ByteBuffer data = u.getData();
        data.rewind();
        final FakeIndexBuffer fa = (FakeIndexBuffer) ii;
        final ByteBuffer fa_data = fa.getData();

        /**
         * XXX: Clearly overflowing integers.
         */

        final long lo = r.getLower();
        final long hi = r.getUpper();
        for (long index = lo; Long.compareUnsigned(index, hi) < 0; ++index) {
          final int i_index = (int) index;
          fa_data.put(i_index, data.get(i_index));
        }
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
}
