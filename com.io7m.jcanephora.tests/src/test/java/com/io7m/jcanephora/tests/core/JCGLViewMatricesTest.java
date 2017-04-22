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

package com.io7m.jcanephora.tests.core;

import com.io7m.jcanephora.core.JCGLViewMatrices;
import com.io7m.jcanephora.tests.contracts.JCGLViewMatricesContract;
import com.io7m.jtensors.core.parameterized.matrices.PMatrix4x4D;
import com.io7m.jtensors.core.unparameterized.matrices.Matrix4x4D;
import com.io7m.jtensors.core.unparameterized.vectors.Vector3D;

/**
 * View matrix test.
 */

// CHECKSTYLE_JAVADOC:OFF

public final class JCGLViewMatricesTest extends JCGLViewMatricesContract
{
  public JCGLViewMatricesTest()
  {

  }

  @Override
  protected Matrix4x4D lookAtRH(
    final Vector3D camera,
    final Vector3D target,
    final Vector3D up)
  {
    return JCGLViewMatrices.lookAtRH(camera, target, up);
  }

  @Override
  protected PMatrix4x4D<Object, Object> lookAtRHP(
    final Vector3D camera,
    final Vector3D target,
    final Vector3D up)
  {
    return JCGLViewMatrices.lookAtRHP(camera, target, up);
  }
}
