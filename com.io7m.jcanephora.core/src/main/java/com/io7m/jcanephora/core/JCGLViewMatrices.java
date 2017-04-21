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

import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.core.parameterized.matrices.PMatrices4x4D;
import com.io7m.jtensors.core.parameterized.matrices.PMatrix4x4D;
import com.io7m.jtensors.core.unparameterized.matrices.Matrices4x4D;
import com.io7m.jtensors.core.unparameterized.matrices.Matrix4x4D;
import com.io7m.jtensors.core.unparameterized.vectors.Vector3D;
import com.io7m.jtensors.core.unparameterized.vectors.Vectors3D;
import com.io7m.junreachable.UnreachableCodeException;

/**
 * Functions to produce view matrices.
 */

public final class JCGLViewMatrices
{
  private JCGLViewMatrices()
  {
    throw new UnreachableCodeException();
  }

  /**
   * <p>Calculate a view matrix representing a camera looking from the point
   * {@code camera} to the point {@code target}. {@code target} must represent
   * the "up" vector for the camera. Usually, this is simply a unit vector
   * {@code (0, 1, 0)} representing the Y axis.</p>
   *
   * <p>The function assumes a right-handed coordinate system.</p>
   *
   * @param camera The position of the viewer.
   * @param target The target being viewed.
   * @param up     The up vector.
   *
   * @return The view matrix
   */

  public static Matrix4x4D lookAtRH(
    final Vector3D camera,
    final Vector3D target,
    final Vector3D up)
  {
    NullCheck.notNull(camera, "Camera position");
    NullCheck.notNull(target, "Target point");
    NullCheck.notNull(up, "Up vector");

    final Vector3D forward =
      Vectors3D.normalize(Vectors3D.subtract(target, camera));
    final Vector3D side =
      Vectors3D.normalize(Vectors3D.crossProduct(forward, up));
    final Vector3D new_up =
      Vectors3D.crossProduct(side, forward);

    final Matrix4x4D rotation =
      Matrix4x4D.of(
        side.x(), side.y(), side.z(), 0.0,
        new_up.x(), new_up.y(), new_up.z(), 0.0,
        -forward.x(), -forward.y(), -forward.z(), 0.0,
        0.0, 0.0, 0.0, 1.0);

    final Matrix4x4D translation =
      Matrices4x4D.ofTranslation(-camera.x(), -camera.y(), -camera.z());

    return Matrices4x4D.multiply(rotation, translation);
  }

  /**
   * <p>Calculate a view matrix representing a camera looking from the point
   * {@code camera} to the point {@code target}. {@code target} must represent
   * the "up" vector for the camera. Usually, this is simply a unit vector
   * {@code (0, 1, 0)} representing the Y axis.</p>
   *
   * <p>The function assumes a right-handed coordinate system.</p>
   *
   * @param camera The position of the viewer.
   * @param target The target being viewed.
   * @param up     The up vector.
   * @param <A> A phantom type parameter, possibly representing a source coordinate system
   *           @param <B> A phantom type parameter, possibly representing a target coordinate system
   * @return The view matrix
   */

  @SuppressWarnings("unchecked")
  public static <A, B> PMatrix4x4D<A, B> lookAtRHP(
    final Vector3D camera,
    final Vector3D target,
    final Vector3D up)
  {
    NullCheck.notNull(camera, "Camera position");
    NullCheck.notNull(target, "Target point");
    NullCheck.notNull(up, "Up vector");

    final Vector3D forward =
      Vectors3D.normalize(Vectors3D.subtract(target, camera));
    final Vector3D side =
      Vectors3D.normalize(Vectors3D.crossProduct(forward, up));
    final Vector3D new_up =
      Vectors3D.crossProduct(side, forward);

    final PMatrix4x4D<Object, Object> rotation =
      PMatrix4x4D.of(
        side.x(), side.y(), side.z(), 0.0,
        new_up.x(), new_up.y(), new_up.z(), 0.0,
        -forward.x(), -forward.y(), -forward.z(), 0.0,
        0.0, 0.0, 0.0, 1.0);

    final PMatrix4x4D<Object, Object> translation =
      PMatrices4x4D.ofTranslation(-camera.x(), -camera.y(), -camera.z());

    return (PMatrix4x4D<A, B>) PMatrices4x4D.multiply(rotation, translation);
  }
}
