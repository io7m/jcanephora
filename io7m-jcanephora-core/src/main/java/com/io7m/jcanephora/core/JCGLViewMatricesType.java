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

import com.io7m.jtensors.MatrixWritable4x4FType;
import com.io7m.jtensors.VectorReadable3FType;

/**
 * Functions for producing view matrices.
 */

public interface JCGLViewMatricesType
{
  /**
   * <p>Calculate a view matrix representing a camera looking from the point
   * {@code camera} to the point {@code target}. {@code target} must represent
   * the "up" vector for the camera. Usually, this is simply a unit vector
   * {@code (0, 1, 0)} representing the Y axis.</p>
   *
   * <p>The view is expressed as a translation and a rotation matrix, which is
   * multiplied with the contents of {@code out}. Therefore, for most use cases,
   * {@code out} should be set to the 4x4 identity matrix before calling the
   * function.</p>
   *
   * @param out    The input/output matrix.
   * @param camera The position of the viewer.
   * @param target The target being viewed.
   * @param up     The up vector.
   */

  void lookAt(
    final MatrixWritable4x4FType out,
    final VectorReadable3FType camera,
    final VectorReadable3FType target,
    final VectorReadable3FType up);
}
