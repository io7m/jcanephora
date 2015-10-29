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

/**
 * The type of array vertex attributes that will be converted to floating-point
 * during shading.
 */

public interface JCGLArrayVertexAttributeFloatingPointType
  extends JCGLArrayVertexAttributeType
{
  /**
   * @return The number of elements in the attribute
   */

  int getElements();

  /**
   * @return {@code true} iff integral type values should be treated as
   * <i>normalized fixed-point</i>
   */

  boolean isNormalized();

  /**
   * @return The attribute offset in bytes
   */

  long getOffset();

  /**
   * @return The stride in bytes
   */

  int getStride();

  /**
   * @return The element type
   */

  JCGLScalarType getType();

  /**
   * @return The attribute index
   */

  int getIndex();
}
