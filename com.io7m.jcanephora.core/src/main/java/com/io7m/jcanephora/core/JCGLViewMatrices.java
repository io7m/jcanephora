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
import com.io7m.jtensors.Matrix4x4FType;
import com.io7m.jtensors.MatrixHeapArrayM4x4F;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.MatrixWritable4x4FType;
import com.io7m.jtensors.VectorM3F;
import com.io7m.jtensors.VectorReadable3FType;

/**
 * Functions to produce view matrices.
 *
 * Memory for this implementation is allocated on creation of the class instance
 * and reused.
 */

public final class JCGLViewMatrices implements JCGLViewMatricesType
{
  private final VectorM3F      camera_inv;
  private final VectorM3F      forward;
  private final VectorM3F      new_up;
  private final Matrix4x4FType rotation;
  private final VectorM3F      side;
  private final Matrix4x4FType translation;

  private JCGLViewMatrices()
  {
    this.forward = new VectorM3F();
    this.side = new VectorM3F();
    this.new_up = new VectorM3F();
    this.camera_inv = new VectorM3F();
    this.rotation = MatrixHeapArrayM4x4F.newMatrix();
    this.translation = MatrixHeapArrayM4x4F.newMatrix();
  }

  /**
   * @return A {@link JCGLViewMatricesType} instance
   */

  public static JCGLViewMatricesType newMatrices()
  {
    return new JCGLViewMatrices();
  }

  @Override
  public void lookAt(
    final MatrixWritable4x4FType out,
    final VectorReadable3FType camera,
    final VectorReadable3FType target,
    final VectorReadable3FType up)
  {
    NullCheck.notNull(out, "Output matrix");
    NullCheck.notNull(camera, "Camera position");
    NullCheck.notNull(target, "Target point");
    NullCheck.notNull(up, "Up vector");

    /**
     * Calculate "forward" vector.
     */

    {
      final float dx = target.getXF() - camera.getXF();
      final float dy = target.getYF() - camera.getYF();
      final float dz = target.getZF() - camera.getZF();
      this.forward.set3F(dx, dy, dz);
      VectorM3F.normalizeInPlace(this.forward);
    }

    /**
     * Calculate "side" vector.
     */

    VectorM3F.crossProduct(this.forward, up, this.side);
    VectorM3F.normalizeInPlace(this.side);

    /**
     * Calculate new "up" vector.
     */

    VectorM3F.crossProduct(this.side, this.forward, this.new_up);

    /**
     * Calculate rotation matrix.
     */

    this.rotation.setR0C0F(this.side.getXF());
    this.rotation.setR0C1F(this.side.getYF());
    this.rotation.setR0C2F(this.side.getZF());
    this.rotation.setR1C0F(this.new_up.getXF());
    this.rotation.setR1C1F(this.new_up.getYF());
    this.rotation.setR1C2F(this.new_up.getZF());
    this.rotation.setR2C0F(-this.forward.getXF());
    this.rotation.setR2C1F(-this.forward.getYF());
    this.rotation.setR2C2F(-this.forward.getZF());

    /**
     * Calculate camera translation vector.
     */

    this.camera_inv.set3F(-camera.getXF(), -camera.getYF(), -camera.getZF());

    /**
     * Calculate translation matrix.
     */

    MatrixM4x4F.makeTranslation3F(this.camera_inv, this.translation);
    MatrixM4x4F.multiply(this.rotation, this.translation, out);
  }
}
