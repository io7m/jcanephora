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

import com.io7m.jcanephora.core.JCGLArrayBufferUsableType;
import com.io7m.jcanephora.core.JCGLArrayVertexAttributeIntegralType;
import com.io7m.jcanephora.core.JCGLArrayVertexAttributeMatcherType;
import com.io7m.jcanephora.core.JCGLScalarIntegralType;
import com.io7m.jnull.NullCheck;
import com.jogamp.opengl.GLContext;

import java.util.Objects;

final class JOGLArrayVertexAttributeIntegral extends JOGLObjectPseudoUnshared
  implements JCGLArrayVertexAttributeIntegralType
{
  private final int index;
  private final JCGLArrayBufferUsableType array;
  private final JCGLScalarIntegralType type;
  private final int stride;
  private final long offset;
  private final int elements;
  private final int divisor;

  JOGLArrayVertexAttributeIntegral(
    final GLContext in_context,
    final int in_index,
    final JCGLArrayBufferUsableType in_a,
    final JCGLScalarIntegralType in_type,
    final int in_elements,
    final int in_stride,
    final long in_offset,
    final int in_divisor)
  {
    super(in_context);

    this.index = in_index;
    this.array = NullCheck.notNull(in_a, "Array buffer");
    this.type = NullCheck.notNull(in_type, "Type");
    this.elements = in_elements;
    this.stride = in_stride;
    this.offset = in_offset;
    this.divisor = in_divisor;
  }

  @Override
  public boolean equals(final Object o)
  {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }

    final JOGLArrayVertexAttributeIntegral that =
      (JOGLArrayVertexAttributeIntegral) o;

    return this.index == that.index
      && this.stride == that.stride
      && this.offset == that.offset
      && this.elements == that.elements
      && this.divisor == that.divisor
      && Objects.equals(this.array, that.array)
      && this.type == that.type;
  }

  @Override
  public int hashCode()
  {
    int result = this.index;
    result = 31 * result + this.array.hashCode();
    result = 31 * result + this.type.hashCode();
    result = 31 * result + this.stride;
    result = 31 * result + (int) (this.offset ^ (this.offset >>> 32));
    result = 31 * result + this.elements;
    result = 31 * result + this.divisor;
    return result;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb =
      new StringBuilder("[ArrayVertexAttributeIntegral ");
    sb.append(" ").append(this.index);
    sb.append(" ").append(this.elements);
    sb.append("x").append(this.type);
    sb.append(" ").append(this.stride);
    sb.append(" ").append(this.offset);
    sb.append(" ").append(this.divisor);
    sb.append(']');
    return sb.toString();
  }

  @Override
  public int getElements()
  {
    return this.elements;
  }

  @Override
  public long getOffset()
  {
    return this.offset;
  }

  @Override
  public int getStride()
  {
    return this.stride;
  }

  @Override
  public JCGLScalarIntegralType getType()
  {
    return this.type;
  }

  @Override
  public int getIndex()
  {
    return this.index;
  }

  @Override
  public JCGLArrayBufferUsableType getArrayBuffer()
  {
    return this.array;
  }

  @Override
  public <A, E extends Exception> A matchVertexAttribute(
    final JCGLArrayVertexAttributeMatcherType<A, E> m)
    throws E
  {
    return m.matchIntegral(this);
  }

  @Override
  public int getDivisor()
  {
    return this.divisor;
  }
}
