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

import com.io7m.jcanephora.core.JCGLArrayBufferType;
import com.io7m.jcanephora.core.JCGLArrayBufferUsableType;
import com.io7m.jcanephora.core.JCGLBufferUpdateType;
import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLExceptionBufferNotBound;
import com.io7m.jcanephora.core.JCGLExceptionDeleted;
import com.io7m.jcanephora.core.JCGLResources;
import com.io7m.jcanephora.core.JCGLUsageHint;
import com.io7m.jcanephora.core.api.JCGLArrayBuffersType;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;
import com.io7m.jranges.RangeCheck;
import com.io7m.jranges.Ranges;
import com.io7m.junsigned.ranges.UnsignedRangeInclusiveL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Optional;

final class FakeArrayBuffers implements JCGLArrayBuffersType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(FakeArrayBuffers.class);
  }

  private final     FakeContext     context;
  private @Nullable FakeArrayBuffer bind;

  FakeArrayBuffers(final FakeContext c)
  {
    this.context = NullCheck.notNull(c);
  }

  private void actualBind(final FakeArrayBuffer a)
  {
    FakeArrayBuffers.LOG.trace("bind {} -> {}", this.bind, a);
    if (!a.equals(this.bind)) {
      this.bind = a;
    }
  }

  private void actualUnbind()
  {
    FakeArrayBuffers.LOG.trace(
      "unbind {} -> {}", this.bind, null);
    if (this.bind != null) {
      this.bind = null;
    }
  }

  @Override public JCGLArrayBufferType arrayBufferAllocate(
    final long size,
    final JCGLUsageHint usage)
    throws JCGLException
  {
    RangeCheck.checkIncludedInLong(
      size, "Size", Ranges.NATURAL_LONG, "Valid size range");

    FakeArrayBuffers.LOG.debug(
      "allocate ({} bytes, {})", Long.valueOf(size), usage);

    final ByteBuffer data = ByteBuffer.allocate((int) size);
    final FakeArrayBuffer ao = new FakeArrayBuffer(
      this.context, this.context.getFreshID(), data, usage);

    this.actualBind(ao);
    return ao;
  }

  @Override
  public Optional<JCGLArrayBufferUsableType> arrayBufferGetCurrentlyBound()
    throws JCGLException
  {
    return Optional.ofNullable(this.bind);
  }

  @Override public void arrayBufferBind(final JCGLArrayBufferUsableType a)
    throws JCGLException, JCGLExceptionDeleted
  {
    this.checkArray(a);
    this.actualBind((FakeArrayBuffer) a);
  }

  private void checkArray(final JCGLArrayBufferUsableType a)
  {
    FakeCompatibilityChecks.checkArrayBuffer(this.context, a);
    JCGLResources.checkNotDeleted(a);
  }

  @Override public void arrayBufferUnbind()
    throws JCGLException
  {
    this.actualUnbind();
  }

  @Override public void arrayBufferDelete(final JCGLArrayBufferType a)
    throws JCGLException, JCGLExceptionDeleted
  {
    this.checkArray(a);

    FakeArrayBuffers.LOG.debug("delete {}", Integer.valueOf(a.getGLName()));

    ((FakeArrayBuffer) a).setDeleted();

    if (a.equals(this.bind)) {
      this.actualUnbind();
    }
  }

  @Override public void arrayBufferUpdate(
    final JCGLBufferUpdateType<JCGLArrayBufferType> u)
    throws JCGLException, JCGLExceptionDeleted, JCGLExceptionBufferNotBound
  {
    NullCheck.notNull(u);
    final JCGLArrayBufferType a = u.getBuffer();
    this.checkArray(a);

    if (a.equals(this.bind)) {
      final UnsignedRangeInclusiveL r = u.getDataUpdateRange();
      final ByteBuffer data = u.getData();
      data.rewind();
      final FakeArrayBuffer fa = (FakeArrayBuffer) a;
      final ByteBuffer fa_data = fa.getData();

      /**
       * XXX: Clearly overflowing integers.
       */

      final long lo = r.getLower();
      final long hi = r.getUpper();
      for (long index = lo; Long.compareUnsigned(index, hi) < 0; ++index) {
        final int ii = (int) index;
        fa_data.put(ii, data.get(ii));
      }
    } else {
      final StringBuilder sb = new StringBuilder(128);
      sb.append("Buffer is not bound.");
      sb.append(System.lineSeparator());
      sb.append("  Required: ");
      sb.append(a);
      sb.append(System.lineSeparator());
      sb.append("  Actual: ");
      sb.append(this.bind == null ? "none" : this.bind);
      throw new JCGLExceptionBufferNotBound(sb.toString());
    }
  }
}
