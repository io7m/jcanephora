/*
 * Copyright © 2016 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.tests.contracts;


import com.io7m.jtensors.core.unparameterized.vectors.Vector2D;
import com.io7m.jtensors.core.unparameterized.vectors.Vector3D;
import com.io7m.jtensors.core.unparameterized.vectors.Vector4D;

import java.nio.FloatBuffer;

public interface JCGLShaderTestFunctionEvaluatorType
{
  Vector4D evaluate4f(Vector4D v);

  default Vector3D evaluate3f(
    final Vector4D v)
  {
    final Vector4D r =
      this.evaluate4f(Vector4D.of(v.x(), v.y(), v.z(), 0.0));
    return Vector3D.of(r.x(), r.y(), r.z());
  }

  default Vector2D evaluate2f(
    final Vector4D v)
  {
    final Vector4D r = this.evaluate4f(Vector4D.of(v.x(), v.y(), 0.0, 0.0));
    return Vector2D.of(r.x(), r.y());
  }

  default double evaluate1f(
    final double v)
  {
    final Vector4D r =
      this.evaluate4f(Vector4D.of(v, 0.0, 0.0, 0.0));
    return r.x();
  }

  Vector4D evaluateArrayF(
    final FloatBuffer x);
}
