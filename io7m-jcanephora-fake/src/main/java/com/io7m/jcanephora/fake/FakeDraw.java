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

import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLExceptionBufferNotBound;
import com.io7m.jcanephora.core.JCGLPrimitives;
import com.io7m.jcanephora.core.api.JCGLDrawType;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeCheck;
import com.io7m.jranges.Ranges;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class FakeDraw implements JCGLDrawType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(FakeDraw.class);
  }

  private final FakeContext      context;
  private final FakeShaders      shaders;
  private final FakeIndexBuffers index_buffers;

  FakeDraw(
    final FakeContext in_c,
    final FakeShaders in_shaders,
    final FakeIndexBuffers in_index_buffers)
  {
    this.context = NullCheck.notNull(in_c);
    this.shaders = NullCheck.notNull(in_shaders);
    this.index_buffers = NullCheck.notNull(in_index_buffers);
  }

  @Override
  public void draw(
    final JCGLPrimitives p,
    final int first,
    final int count)
    throws JCGLException
  {
    NullCheck.notNull(p);
    RangeCheck.checkIncludedInInteger(
      first, "First", Ranges.NATURAL_INTEGER, "Valid index");
    RangeCheck.checkIncludedInInteger(
      count, "Count", Ranges.NATURAL_INTEGER, "Valid count");

    FakeDraw.LOG.trace(
      "draw: count {} of {} from {}",
      Integer.valueOf(count),
      p,
      Integer.valueOf(first));
  }

  @Override
  public void drawInstanced(
    final JCGLPrimitives p,
    final int first,
    final int count,
    final int instances)
    throws JCGLException
  {
    NullCheck.notNull(p);
    RangeCheck.checkIncludedInInteger(
      first, "First", Ranges.NATURAL_INTEGER, "Valid index");
    RangeCheck.checkIncludedInInteger(
      count, "Count", Ranges.NATURAL_INTEGER, "Valid counts");
    RangeCheck.checkIncludedInInteger(
      instances, "Instances", Ranges.NATURAL_INTEGER, "Valid instances");

    FakeDraw.LOG.trace(
      "draw: count {} of {} from {}, {} instances",
      Integer.valueOf(count),
      p,
      Integer.valueOf(first),
      Integer.valueOf(instances));
  }

  @Override
  public void drawElements(final JCGLPrimitives p)
    throws JCGLException, JCGLExceptionBufferNotBound
  {
    NullCheck.notNull(p);

    if (this.index_buffers.indexBufferIsBound()) {
      FakeDraw.LOG.trace("drawElements: {}", p);
    } else {
      throw new JCGLExceptionBufferNotBound("No index buffer is bound");
    }
  }

  @Override
  public void drawElementsInstanced(
    final JCGLPrimitives p,
    final int instances)
    throws JCGLException, JCGLExceptionBufferNotBound
  {
    NullCheck.notNull(p);
    RangeCheck.checkIncludedInInteger(
      instances, "Instances", Ranges.NATURAL_INTEGER, "Valid instances");

    if (this.index_buffers.indexBufferIsBound()) {
      FakeDraw.LOG.trace("drawElementsInstanced: {}", p);
    } else {
      throw new JCGLExceptionBufferNotBound(
        "No index buffer is currently bound");
    }
  }
}
