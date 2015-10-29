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

import com.io7m.jcanephora.core.JCGLArrayBufferUsableType;
import com.io7m.jcanephora.core.JCGLArrayVertexAttributeFloatingPointType;
import com.io7m.jcanephora.core.JCGLArrayVertexAttributeMatcherType;
import com.io7m.jcanephora.core.JCGLScalarType;
import com.io7m.jnull.NullCheck;

final class FakeArrayVertexAttributeFloating extends FakeObjectPseudoUnshared
  implements JCGLArrayVertexAttributeFloatingPointType
{
  private final int                       index;
  private final JCGLArrayBufferUsableType array;
  private final JCGLScalarType            type;
  private final int                       stride;
  private final long                      offset;
  private final int                       elements;
  private final boolean                   normalized;

  FakeArrayVertexAttributeFloating(
    final FakeContext in_context,
    final int in_index,
    final JCGLArrayBufferUsableType in_a,
    final JCGLScalarType in_type,
    final int in_elements,
    final int in_stride,
    final long in_offset,
    final boolean in_normalized)
  {
    super(in_context);

    this.index = in_index;
    this.array = NullCheck.notNull(in_a);
    this.type = NullCheck.notNull(in_type);
    this.elements = in_elements;
    this.stride = in_stride;
    this.offset = in_offset;
    this.normalized = in_normalized;
  }

  @Override public int getElements()
  {
    return this.elements;
  }

  @Override public boolean isNormalized()
  {
    return this.normalized;
  }

  @Override public long getOffset()
  {
    return this.offset;
  }

  @Override public int getStride()
  {
    return this.stride;
  }

  @Override public JCGLScalarType getType()
  {
    return this.type;
  }

  @Override public int getIndex()
  {
    return this.index;
  }

  @Override public JCGLArrayBufferUsableType getArrayBuffer()
  {
    return this.array;
  }

  @Override public <A, E extends Exception> A matchVertexAttribute(
    final JCGLArrayVertexAttributeMatcherType<A, E> m)
    throws E
  {
    return m.matchFloatingPoint(this);
  }
}
