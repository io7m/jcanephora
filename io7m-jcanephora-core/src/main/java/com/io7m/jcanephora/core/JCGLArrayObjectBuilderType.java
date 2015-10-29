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

package com.io7m.jcanephora.core;

import java.util.Optional;

/**
 * A mutable builder for configuring array objects.
 */

public interface JCGLArrayObjectBuilderType
{
  /**
   * @param index The attribute index in the range {@code [0,
   *              getMaximumVertexAttributes() - 1]}
   *
   * @return The attribute at the given index, if any
   */

  Optional<JCGLArrayVertexAttributeType> getAttributeAt(int index);

  /**
   * @return The supported maximum number of vertex attributes. Must be {@code
   * >= 16}.
   */

  int getMaximumVertexAttributes();

  /**
   * <p>Configure the attribute {@code index} to retrieve data from {@code
   * a}.</p>
   *
   * <p>The data is assumed to be {@code elements} values of type {@code type},
   * occuring every {@code stride} bytes in the buffer, starting at {@code
   * offset}. The data will be converted to floating point values when delivered
   * to a shading program. If {@code normalized == true}, then values of
   * integral types will be treated as <i>normalized fixed-point</i> values when
   * the conversion takes place. Otherwise, they will be directly converted to
   * floating point (so, for example, an unsigned byte value of {@code 255} will
   * become {@code 255.0} as opposed to the {@code 1.0} value it would become if
   * treated as <i>normalized fixed-point</i>).</p>
   *
   * <p>As a concrete example, if an buffer contains vertices with the following
   * type:</p>
   *
   * <pre>
   * struct vertex {
   *   vec3 position;
   *   vec2 uv;
   *   vec3 normal;
   * }
   * </pre>
   *
   * <p>Attribute {@code 0}, the {@code position} field, has an initial offset
   * of {@code 0}, and has {@code 3} elements of type {@link
   * JCGLScalarType#TYPE_FLOAT}. </p>
   *
   * <p>Attribute {@code 1}, the {@code uv} field, has an initial offset of
   * {@code 3 * (sizeof float) == 3 * 4 == 12} bytes, and has {@code 2} elements
   * of type {@link JCGLScalarType#TYPE_FLOAT}.</p>
   *
   * <p>Attribute {@code 2}, the {@code normal} field, has an initial offset of
   * {@code (3 + 2) * (sizeof float) == 5 * 4 == 20} bytes, and has {@code 3}
   * elements of type {@link JCGLScalarType#TYPE_FLOAT}.</p>
   *
   * <p>If the {@code vertex} type has no padding, then the {@code stride} value
   * is equal to the size of the type, which is {@code (3 + 2 + 3) * (sizeof
   * float) == 8 * 4 == 32} bytes. </p>
   *
   * @param index      The attribute
   * @param a          The array buffer
   * @param elements   The number of elements
   * @param type       The type of elements
   * @param offset     The offset from the start of the buffer
   * @param stride     The number of bytes to step forward at each vertex
   * @param normalized {@code true} iff the data should be treated as
   *                   <i>normalized fixed-point</i> values when converting to
   *                   floating point
   */

  void setAttributeFloatingPoint(
    int index,
    JCGLArrayBufferUsableType a,
    int elements,
    JCGLScalarType type,
    int stride,
    long offset,
    boolean normalized);

  /**
   * <p>Configure the attribute {@code index} to retrieve data from {@code
   * a}.</p>
   *
   * <p>The data is assumed to be {@code elements} values of type {@code type},
   * occuring every {@code stride} bytes in the buffer, starting at {@code
   * offset}.</p>
   *
   * <p>The data is assumed to be of an integral type and will <i>not</i> be
   * converted to floating-point prior to being delivered to a shading
   * program.</p>
   *
   * @param index    The attribute
   * @param a        The array buffer
   * @param elements The number of elements
   * @param type     The type of elements
   * @param offset   The offset from the start of the buffer
   * @param stride   The number of bytes to step forward at each vertex
   *
   * @see #setAttributeFloatingPoint(int, JCGLArrayBufferUsableType, int,
   * JCGLScalarType, int, long, boolean)
   */

  void setAttributeIntegral(
    int index,
    JCGLArrayBufferUsableType a,
    int elements,
    JCGLScalarIntegralType type,
    int stride,
    long offset);

  /**
   * Clear all and any configured parameters specified so far.
   */

  void reset();
}
