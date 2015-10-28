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
import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLExceptionDeleted;
import com.io7m.jcanephora.core.JCGLResources;
import com.io7m.jcanephora.core.JCGLUsageHint;
import com.io7m.jcanephora.core.api.JCGLArrayBuffersType;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;
import com.io7m.jranges.RangeCheck;
import com.io7m.jranges.Ranges;
import com.jogamp.opengl.GL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Optional;

final class JOGLArrayBuffers implements JCGLArrayBuffersType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(JOGLArrayBuffers.class);
  }

  private final     GL              gl;
  private final     IntBuffer       int_cache;
  private final     JOGLContext     context;
  private @Nullable JOGLArrayBuffer bind;

  JOGLArrayBuffers(final JOGLContext c)
  {
    this.context = NullCheck.notNull(c);
    this.gl = c.getContext().getGL();
    this.int_cache =
      ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
  }

  @Override public JCGLArrayBufferType arrayBufferAllocate(
    final long size,
    final JCGLUsageHint usage)
    throws JCGLException
  {
    RangeCheck.checkIncludedInLong(
      size, "Size", Ranges.NATURAL_LONG, "Valid size range");

    JOGLArrayBuffers.LOG.debug(
      "allocate ({} bytes, {})", Long.valueOf(size), usage);

    this.int_cache.rewind();
    this.gl.glGenBuffers(1, this.int_cache);
    final int id = this.int_cache.get(0);

    this.bind = null;
    this.bind(id);

    try {
      this.gl.glBufferData(
        GL.GL_ARRAY_BUFFER,
        size,
        null,
        JOGLTypeConversions.usageHintToGL(usage));
      JOGLArrayBuffers.LOG.debug("allocated {}", Integer.valueOf(id));
    } finally {
      this.bind(0);
      this.bind = null;
    }

    return new JOGLArrayBuffer(this.gl.getContext(), id, size, usage);
  }

  private void bind(final int id)
  {
    if (id == 0) {
      JOGLArrayBuffers.LOG.trace("unbind");
    } else {
      JOGLArrayBuffers.LOG.trace("bind {}", Integer.valueOf(id));
    }

    this.gl.glBindBuffer(GL.GL_ARRAY_BUFFER, id);
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
    this.bind(a.getGLName());
    this.bind = (JOGLArrayBuffer) a;
  }

  private void checkArray(final JCGLArrayBufferUsableType a)
  {
    JOGLCompatibilityChecks.checkArray(this.gl.getContext(), a);
    JCGLResources.checkNotDeleted(a);
  }

  @Override public void arrayBufferUnbind()
    throws JCGLException
  {
    this.bind(0);
    this.bind = null;
  }

  @Override public void arrayBufferDelete(final JCGLArrayBufferType a)
    throws JCGLException, JCGLExceptionDeleted
  {
    this.checkArray(a);

    JOGLArrayBuffers.LOG.debug("delete {}", Integer.valueOf(a.getGLName()));

    this.int_cache.rewind();
    this.int_cache.put(0, a.getGLName());
    this.gl.glDeleteBuffers(1, this.int_cache);
    ((JOGLArrayBuffer) a).setDeleted();

    if (this.bind != null) {
      if (this.bind.getGLName() == a.getGLName()) {
        this.arrayBufferUnbind();
      }
    }
  }
}
