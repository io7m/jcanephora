/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

import javax.media.opengl.GLContext;

import com.io7m.jcanephora.IndexBufferType;
import com.io7m.jcanephora.JCGLUnsignedType;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeInclusiveL;

final class JOGLIndexBuffer extends JOGLObjectShared implements
  IndexBufferType
{
  private final RangeInclusiveL  range;
  private final JCGLUnsignedType type;

  JOGLIndexBuffer(
    final GLContext in_context,
    final int in_name,
    final RangeInclusiveL in_range,
    final JCGLUnsignedType in_type)
  {
    super(in_context, in_name);
    this.range = NullCheck.notNull(in_range, "Range");
    this.type = NullCheck.notNull(in_type, "GL type");
  }

  @Override public long bufferGetElementSizeBytes()
  {
    return this.type.getSizeBytes();
  }

  @Override public RangeInclusiveL bufferGetRange()
  {
    return this.range;
  }

  @Override public JCGLUnsignedType indexGetType()
  {
    return this.type;
  }

  @Override public long resourceGetSizeBytes()
  {
    return this.bufferGetElementSizeBytes() * this.range.getInterval();
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[JOGLIndexBuffer ");
    builder.append(super.getGLName());
    builder.append(" ");
    builder.append(this.range);
    builder.append(" ");
    builder.append(this.bufferGetElementSizeBytes());
    builder.append("]");

    final String r = builder.toString();
    assert r != null;
    return r;
  }
}
