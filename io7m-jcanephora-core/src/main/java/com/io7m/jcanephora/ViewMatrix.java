/*
 * Copyright © 2012 http://io7m.com
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

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.concurrent.ThreadSafe;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorI3F;
import com.io7m.jtensors.VectorM3F;
import com.io7m.jtensors.VectorReadable3F;

/**
 * Functions for producing view matrices (such as those required to implement
 * 3D camera abstractions).
 */

@ThreadSafe public final class ViewMatrix
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

  @NotThreadSafe public static final class Context
  {
    protected final @Nonnull VectorM3F   forward;
    protected final @Nonnull VectorM3F   side;
    protected final @Nonnull VectorM3F   new_up;
    protected final @Nonnull VectorM3F   camera_inv;
    protected final @Nonnull MatrixM4x4F rotation;
    protected final @Nonnull MatrixM4x4F translation;

    public Context()
    {
      this.forward = new VectorM3F();
      this.side = new VectorM3F();
      this.new_up = new VectorM3F();
      this.camera_inv = new VectorM3F();

      this.rotation = new MatrixM4x4F();
      this.translation = new MatrixM4x4F();
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
   * @throws ConstraintError
   *           Iff:
   *           <ul>
   *           <li><code>matrix == null</code></li>
   *           <li><code>camera == null</code></li>
   *           <li><code>target == null</code></li>
   *           <li><code>up == null</code></li>
   *           </ul>
   */

  public static void lookAt(
    final @Nonnull MatrixM4x4F out,
    final @Nonnull VectorReadable3F camera,
    final @Nonnull VectorReadable3F target,
    final @Nonnull VectorReadable3F up)
    throws ConstraintError
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
   * @throws ConstraintError
   *           Iff:
   *           <ul>
   *           <li><code>matrix == null</code></li>
   *           <li><code>camera == null</code></li>
   *           <li><code>target == null</code></li>
   *           <li><code>up == null</code></li>
   *           </ul>
   */

  public static void lookAtWithContext(
    final @Nonnull Context context,
    final @Nonnull MatrixM4x4F out,
    final @Nonnull VectorReadable3F camera,
    final @Nonnull VectorReadable3F target,
    final @Nonnull VectorReadable3F up)
    throws ConstraintError
  {
    Constraints.constrainNotNull(context, "Context");
    Constraints.constrainNotNull(out, "Output matrix");
    Constraints.constrainNotNull(camera, "Camera position");
    Constraints.constrainNotNull(target, "Target point");
    Constraints.constrainNotNull(up, "Up vector");

    context.reset();

    /**
     * Calculate "forward" vector.
     */

    context.forward.x = target.getXF() - camera.getXF();
    context.forward.y = target.getYF() - camera.getYF();
    context.forward.z = target.getZF() - camera.getZF();
    VectorM3F.normalizeInPlace(context.forward);

    /**
     * Calculate "side" vector.
     */

    VectorM3F.crossProduct(context.forward, up, context.side);
    VectorM3F.normalizeInPlace(context.side);

    /**
     * Calculate new "up" vector.
     */

    VectorM3F.crossProduct(context.side, context.forward, context.new_up);

    /**
     * Calculate rotation matrix.
     */

    context.rotation.set(0, 0, context.side.x);
    context.rotation.set(0, 1, context.side.y);
    context.rotation.set(0, 2, context.side.z);
    context.rotation.set(1, 0, context.new_up.x);
    context.rotation.set(1, 1, context.new_up.y);
    context.rotation.set(1, 2, context.new_up.z);
    context.rotation.set(2, 0, -context.forward.x);
    context.rotation.set(2, 1, -context.forward.y);
    context.rotation.set(2, 2, -context.forward.z);

    /**
     * Calculate camera translation vector.
     */

    context.camera_inv.x = -camera.getXF();
    context.camera_inv.y = -camera.getYF();
    context.camera_inv.z = -camera.getZF();

    /**
     * Calculate translation matrix.
     */

    MatrixM4x4F.translateByVector3FInPlace(
      context.translation,
      context.camera_inv);

    MatrixM4x4F.multiply(context.rotation, context.translation, out);
  }

  private ViewMatrix()
  {
    throw new UnreachableCodeException();
  }
}
