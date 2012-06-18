package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorM3F;
import com.io7m.jtensors.VectorReadable3F;

/**
 * Functions for producing view matrices (such as those required to implement
 * 3D camera abstractions).
 */

public final class ViewMatrix
{
  /**
   * Calculate a view matrix representing a camera looking from the point
   * <code>camera</code> to the point <code>target</code>. <code>target</code>
   * must represent the "up" vector for the camera. Usually, this is simply a
   * unit vector <code>(0, 1, 0)</code> representing the Y axis.
   * 
   * The view is expressed as a translation and a rotation matrix, which is
   * multiplied with the contents of <code>matrix</code>. Therefore, for most
   * use cases, <code>matrix</code> should be set to the 4x4 identity matrix
   * before calling the function.
   * 
   * @param matrix
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
    final @Nonnull MatrixM4x4F matrix,
    final @Nonnull VectorReadable3F camera,
    final @Nonnull VectorReadable3F target,
    final @Nonnull VectorReadable3F up)
    throws ConstraintError
  {
    Constraints.constrainNotNull(matrix, "Output matrix");
    Constraints.constrainNotNull(camera, "Camera position");
    Constraints.constrainNotNull(target, "Target point");
    Constraints.constrainNotNull(up, "Up vector");

    final VectorM3F forward = new VectorM3F();
    forward.x = target.getXF() - camera.getXF();
    forward.y = target.getYF() - camera.getYF();
    forward.z = target.getZF() - camera.getZF();
    VectorM3F.normalizeInPlace(forward);

    final VectorM3F side = new VectorM3F();
    VectorM3F.crossProduct(forward, up, side);
    VectorM3F.normalizeInPlace(side);

    final VectorM3F new_up = new VectorM3F();
    VectorM3F.crossProduct(side, forward, new_up);

    final MatrixM4x4F rotation = new MatrixM4x4F();
    rotation.set(0, 0, side.x);
    rotation.set(0, 1, side.y);
    rotation.set(0, 2, side.z);
    rotation.set(1, 0, new_up.x);
    rotation.set(1, 1, new_up.y);
    rotation.set(1, 2, new_up.z);
    rotation.set(2, 0, -forward.x);
    rotation.set(2, 1, -forward.y);
    rotation.set(2, 2, -forward.z);

    final VectorM3F camera_inv = new VectorM3F();
    camera_inv.x = -camera.getXF();
    camera_inv.y = -camera.getYF();
    camera_inv.z = -camera.getZF();

    final MatrixM4x4F translation = new MatrixM4x4F();
    MatrixM4x4F.translateByVector3FInPlace(translation, camera_inv);
    MatrixM4x4F.multiply(rotation, translation, matrix);
  }
}
