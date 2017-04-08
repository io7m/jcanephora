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

import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLExceptionBufferNotBound;
import com.io7m.jcanephora.core.JCGLPrimitives;
import com.io7m.jcanephora.core.api.JCGLDrawType;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeCheck;
import com.io7m.jranges.Ranges;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL31;

final class LWJGL3Draw implements JCGLDrawType
{
  private final LWJGL3ArrayObjects array_objects;
  private final LWJGL3IndexBuffers index_buffers;

  LWJGL3Draw(
    final LWJGL3Context in_context,
    final LWJGL3ArrayObjects in_array_objects,
    final LWJGL3IndexBuffers in_index_buffers)
  {
    NullCheck.notNull(in_context, "Context");
    this.array_objects = NullCheck.notNull(in_array_objects, "Array objects");
    this.index_buffers = NullCheck.notNull(in_index_buffers, "Index buffers");
  }

  @Override
  public void draw(
    final JCGLPrimitives p,
    final int first,
    final int count)
    throws JCGLException
  {
    NullCheck.notNull(p, "Primitives");
    RangeCheck.checkIncludedInInteger(
      first, "First", Ranges.NATURAL_INTEGER, "Valid index");
    RangeCheck.checkIncludedInInteger(
      count, "Count", Ranges.NATURAL_INTEGER, "Valid counts");

    GL11.glDrawArrays(LWJGL3TypeConversions.primitiveToGL(p), first, count);
  }

  @Override
  public void drawInstanced(
    final JCGLPrimitives p,
    final int first,
    final int count,
    final int instances)
    throws JCGLException
  {
    NullCheck.notNull(p, "Primitives");
    RangeCheck.checkIncludedInInteger(
      first, "First", Ranges.NATURAL_INTEGER, "Valid index");
    RangeCheck.checkIncludedInInteger(
      count, "Count", Ranges.NATURAL_INTEGER, "Valid counts");
    RangeCheck.checkIncludedInInteger(
      instances, "Instances", Ranges.NATURAL_INTEGER, "Valid instances");

    GL31.glDrawArraysInstanced(
      LWJGL3TypeConversions.primitiveToGL(p), first, count, instances);
  }

  @Override
  public void drawElements(final JCGLPrimitives p)
    throws JCGLException, JCGLExceptionBufferNotBound
  {
    NullCheck.notNull(p, "Primitives");

    if (this.index_buffers.indexBufferIsBound()) {
      final LWJGL3IndexBuffer ib = this.array_objects.getCurrentIndexBuffer();
      final int pgl = LWJGL3TypeConversions.primitiveToGL(p);
      final int type = LWJGL3TypeConversions.unsignedTypeToGL(ib.type());
      GL11.glDrawElements(pgl, (int) ib.indices(), type, 0L);
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
    NullCheck.notNull(p, "Primitives");
    RangeCheck.checkIncludedInInteger(
      instances, "Instances", Ranges.NATURAL_INTEGER, "Valid instances");

    if (this.index_buffers.indexBufferIsBound()) {
      final LWJGL3IndexBuffer ib = this.array_objects.getCurrentIndexBuffer();
      final int pgl = LWJGL3TypeConversions.primitiveToGL(p);
      final int type = LWJGL3TypeConversions.unsignedTypeToGL(ib.type());
      GL31.glDrawElementsInstanced(
        pgl, (int) ib.indices(), type, 0L, instances);
    } else {
      throw new JCGLExceptionBufferNotBound(
        "No index buffer is currently bound");
    }
  }
}
