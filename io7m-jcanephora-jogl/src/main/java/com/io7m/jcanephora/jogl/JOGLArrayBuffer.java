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

import com.io7m.jcanephora.ArrayBufferType;
import com.io7m.jcanephora.ArrayDescriptor;
import com.io7m.jequality.annotations.EqualityStructural;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;
import com.io7m.jranges.RangeInclusiveL;

@EqualityStructural final class JOGLArrayBuffer extends JOGLObjectShared implements
  ArrayBufferType
{
  private final RangeInclusiveL range;
  private final ArrayDescriptor type;

  JOGLArrayBuffer(
    final GLContext in_context,
    final int in_name,
    final RangeInclusiveL in_range,
    final ArrayDescriptor in_type)
  {
    super(in_context, in_name);
    this.range = NullCheck.notNull(in_range, "Range");
    this.type = NullCheck.notNull(in_type, "Type");
  }

  @Override public ArrayDescriptor arrayGetDescriptor()
  {
    return this.type;
  }

  @Override public long bufferGetElementSizeBytes()
  {
    return this.type.getElementSizeBytes();
  }

  @Override public RangeInclusiveL bufferGetRange()
  {
    return this.range;
  }

  @Override public boolean equals(
    final @Nullable Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    final JOGLArrayBuffer other = (JOGLArrayBuffer) obj;
    if (super.getGLName() != other.getGLName()) {
      return false;
    }
    if (!this.range.equals(other.range)) {
      return false;
    }
    if (!this.type.equals(other.type)) {
      return false;
    }
    return true;
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + this.getGLName();
    result = (prime * result) + this.range.hashCode();
    result = (prime * result) + this.type.hashCode();
    return result;
  }

  @Override public long resourceGetSizeBytes()
  {
    return this.range.getInterval() * this.type.getElementSizeBytes();
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[JOGLArrayBuffer ");
    builder.append(this.range);
    builder.append(" ");
    builder.append(this.type);
    builder.append("]");
    final String r = builder.toString();
    assert r != null;
    return r;
  }
}
