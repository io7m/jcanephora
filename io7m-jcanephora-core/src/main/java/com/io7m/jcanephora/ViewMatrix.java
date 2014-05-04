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

package com.io7m.jcanephora;

import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorI3F;
import com.io7m.jtensors.VectorM3F;
import com.io7m.jtensors.VectorReadable3FType;
import com.io7m.junreachable.UnreachableCodeException;

/**
 * Functions for producing view matrices (such as those required to implement
 * 3D camera abstractions).
 */

public final class ViewMatrix
{
  /**
   * The Context type contains the minimum storage required for all of the
   * functions of the <code>ViewMatrix</code> class.
   * 
   * <p>
   * The purpose of the class is to allow applications to allocate all storage
   * ahead of time in order to allow functions in the class to avoid
   * allocating memory (not including stack space) for intermediate
   * calculations. This can reduce garbage collection in speed critical code.
   * </p>
   * 
   * <p>
   * The user should allocate one <code>Context</code> value per thread, and
   * then pass this value to matrix functions. Any matrix function that takes
   * a <code>Context</code> value will not generate garbage.
   * </p>
   */

  public static final class Context
  {
    private final VectorM3F   camera_inv;
    private final VectorM3F   forward;
    private final VectorM3F   new_up;
    private final MatrixM4x4F rotation;
    private final VectorM3F   side;
    private final MatrixM4x4F translation;

    /**
     * Construct a new context.
     */

    public Context()
    {
      this.forward = new VectorM3F();
      this.side = new VectorM3F();
      this.new_up = new VectorM3F();
      this.camera_inv = new VectorM3F();
      this.rotation = new MatrixM4x4F();
      this.translation = new MatrixM4x4F();
    }

    VectorM3F getCameraInverse()
    {
      return this.camera_inv;
    }

    VectorM3F getForward()
    {
      return this.forward;
    }

    VectorM3F getNewUp()
    {
      return this.new_up;
    }

    MatrixM4x4F getRotation()
    {
      return this.rotation;
    }

    VectorM3F getSide()
    {
      return this.side;
    }

    MatrixM4x4F getTranslation()
    {
      return this.translation;
    }

    protected void reset()
    {
      VectorM3F.copy(VectorI3F.ZERO, this.forward);
      VectorM3F.copy(VectorI3F.ZERO, this.side);
      VectorM3F.copy(VectorI3F.ZERO, this.new_up);
      VectorM3F.copy(VectorI3F.ZERO, this.camera_inv);

      MatrixM4x4F.setIdentity(this.rotation);
      MatrixM4x4F.setIdentity(this.translation);
    }
  }

  /**
   * Calculate a view matrix representing a camera looking from the point
   * <code>camera</code> to the point <code>target</code>. <code>target</code>
   * must represent the "up" vector for the camera. Usually, this is simply a
   * unit vector <code>(0, 1, 0)</code> representing the Y axis.
   * 
   * The view is expressed as a translation and a rotation matrix, which is
   * multiplied with the contents of <code>out</code>. Therefore, for most use
   * cases, <code>out</code> should be set to the 4x4 identity matrix before
   * calling the function.
   * 
   * @see ViewMatrix#lookAtWithContext(Context, MatrixM4x4F, VectorReadable3F,
   *      VectorReadable3F, VectorReadable3F)
   * 
   * @param out
   *          The input/output matrix.
   * @param camera
   *          The position of the viewer.
   * @param target
   *          The target being viewed.
   * @param up
   *          The up vector.
   */

  public static void lookAt(
    final MatrixM4x4F out,
    final VectorReadable3FType camera,
    final VectorReadable3FType target,
    final VectorReadable3FType up)
  {
    final Context context = new Context();
    ViewMatrix.lookAtWithContext(context, out, camera, target, up);
  }

  /**
   * Calculate a view matrix representing a camera looking from the point
   * <code>camera</code> to the point <code>target</code>. <code>target</code>
   * must represent the "up" vector for the camera. Usually, this is simply a
   * unit vector <code>(0, 1, 0)</code> representing the Y axis.
   * 
   * The function uses preallocated storage from <code>context</code>.
   * 
   * The view is expressed as a translation and a rotation matrix, which is
   * multiplied with the contents of <code>out</code>. Therefore, for most use
   * cases, <code>out</code> should be set to the 4x4 identity matrix before
   * calling the function.
   * 
   * @param context
   *          Preallocated storage.
   * @param out
   *          The input/output matrix.
   * @param camera
   *          The position of the viewer.
   * @param target
   *          The target being viewed.
   * @param up
   *          The up vector.
   */

  public static void lookAtWithContext(
    final Context context,
    final MatrixM4x4F out,
    final VectorReadable3FType camera,
    final VectorReadable3FType target,
    final VectorReadable3FType up)
  {
    NullCheck.notNull(context, "Context");
    NullCheck.notNull(out, "Output matrix");
    NullCheck.notNull(camera, "Camera position");
    NullCheck.notNull(target, "Target point");
    NullCheck.notNull(up, "Up vector");

    context.reset();

    final VectorM3F forward = context.getForward();
    final VectorM3F side = context.getSide();
    final VectorM3F new_up = context.getNewUp();
    final MatrixM4x4F rotation = context.getRotation();
    final VectorM3F camera_inv = context.getCameraInverse();
    final MatrixM4x4F translation = context.getTranslation();

    /**
     * Calculate "forward" vector.
     */

    {
      final float dx = target.getXF() - camera.getXF();
      final float dy = target.getYF() - camera.getYF();
      final float dz = target.getZF() - camera.getZF();
      forward.set3F(dx, dy, dz);
      VectorM3F.normalizeInPlace(forward);
    }

    /**
     * Calculate "side" vector.
     */

    VectorM3F.crossProduct(forward, up, side);
    VectorM3F.normalizeInPlace(side);

    /**
     * Calculate new "up" vector.
     */

    VectorM3F.crossProduct(side, forward, new_up);

    /**
     * Calculate rotation matrix.
     */

    rotation.set(0, 0, side.getXF());
    rotation.set(0, 1, side.getYF());
    rotation.set(0, 2, side.getZF());
    rotation.set(1, 0, new_up.getXF());
    rotation.set(1, 1, new_up.getYF());
    rotation.set(1, 2, new_up.getZF());
    rotation.set(2, 0, -forward.getXF());
    rotation.set(2, 1, -forward.getYF());
    rotation.set(2, 2, -forward.getZF());

    /**
     * Calculate camera translation vector.
     */

    camera_inv.set3F(-camera.getXF(), -camera.getYF(), -camera.getZF());

    /**
     * Calculate translation matrix.
     */

    MatrixM4x4F.translateByVector3FInPlace(translation, camera_inv);
    MatrixM4x4F.multiply(rotation, translation, out);
  }

  private ViewMatrix()
  {
    throw new UnreachableCodeException();
  }
}
