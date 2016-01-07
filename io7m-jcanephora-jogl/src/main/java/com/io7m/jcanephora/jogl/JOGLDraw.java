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

import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLExceptionBufferNotBound;
import com.io7m.jcanephora.core.JCGLPrimitives;
import com.io7m.jcanephora.core.api.JCGLDrawType;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeCheck;
import com.io7m.jranges.Ranges;
import com.jogamp.opengl.GL3;

final class JOGLDraw implements JCGLDrawType
{
  private final JOGLArrayObjects array_objects;
  private final JOGLIndexBuffers index_buffers;
  private final JOGLContext      context;
  private final GL3              g3;

  JOGLDraw(
    final JOGLContext in_context,
    final JOGLArrayObjects in_array_objects,
    final JOGLIndexBuffers in_index_buffers)
  {
    this.context = NullCheck.notNull(in_context);
    this.array_objects = NullCheck.notNull(in_array_objects);
    this.index_buffers = NullCheck.notNull(in_index_buffers);
    this.g3 = this.context.getGL3();
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
      count, "Count", Ranges.NATURAL_INTEGER, "Valid counts");

    this.g3.glDrawArrays(JOGLTypeConversions.primitiveToGL(p), first, count);
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

    this.g3.glDrawArraysInstanced(
      JOGLTypeConversions.primitiveToGL(p), first, count, instances);
  }

  @Override
  public void drawElements(final JCGLPrimitives p)
    throws JCGLException, JCGLExceptionBufferNotBound
  {
    NullCheck.notNull(p);

    if (this.index_buffers.indexBufferIsBound()) {
      final JOGLIndexBuffer ib = this.array_objects.getCurrentIndexBuffer();
      final int pgl = JOGLTypeConversions.primitiveToGL(p);
      final int type = JOGLTypeConversions.unsignedTypeToGL(ib.getType());
      this.g3.glDrawElements(pgl, (int) ib.getIndices(), type, 0L);
    } else {
      throw new JCGLExceptionBufferNotBound(
        "No index buffer is currently bound");
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
      final JOGLIndexBuffer ib = this.array_objects.getCurrentIndexBuffer();
      final int pgl = JOGLTypeConversions.primitiveToGL(p);
      final int type = JOGLTypeConversions.unsignedTypeToGL(ib.getType());
      this.g3.glDrawElementsInstanced(
        pgl, (int) ib.getIndices(), type, 0L, instances);
    } else {
      throw new JCGLExceptionBufferNotBound(
        "No index buffer is currently bound");
    }
  }
}
