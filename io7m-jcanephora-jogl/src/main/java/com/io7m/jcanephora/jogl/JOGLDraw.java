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
import com.io7m.jcanephora.core.JCGLPrimitives;
import com.io7m.jcanephora.core.api.JCGLArrayObjectsType;
import com.io7m.jcanephora.core.api.JCGLDrawType;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeCheck;
import com.io7m.jranges.Ranges;
import com.jogamp.opengl.GL3;

final class JOGLDraw implements JCGLDrawType
{
  private final JCGLArrayObjectsType array_objects;
  private final JOGLIndexBuffers     index_buffers;
  private final JOGLContext          context;
  private final GL3                  g3;

  JOGLDraw(
    final JOGLContext in_context,
    final JCGLArrayObjectsType in_array_objects,
    final JOGLIndexBuffers in_index_buffers)
  {
    this.context = NullCheck.notNull(in_context);
    this.array_objects = NullCheck.notNull(in_array_objects);
    this.index_buffers = NullCheck.notNull(in_index_buffers);
    this.g3 = this.context.getGL3();
  }

  @Override public void draw(
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
}
