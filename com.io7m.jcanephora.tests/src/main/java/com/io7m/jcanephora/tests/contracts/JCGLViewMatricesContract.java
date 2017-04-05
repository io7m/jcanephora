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

package com.io7m.jcanephora.tests.contracts;

import com.io7m.jtensors.core.unparameterized.matrices.Matrix4x4D;
import com.io7m.jtensors.core.unparameterized.vectors.Vector3D;
import org.junit.Assert;
import org.junit.Test;

/**
 * View matrix contract.
 */

// CHECKSTYLE_JAVADOC:OFF

public abstract class JCGLViewMatricesContract
{
  protected abstract Matrix4x4D lookAtRH(
    final Vector3D camera,
    final Vector3D target,
    final Vector3D up);

  @Test
  public final void testLookAt()
  {
    final Vector3D vc = Vector3D.of(10.0, 10.0, -10.0);
    final Vector3D vt = Vector3D.of(10.0, 10.0, 0.0);
    final Vector3D vu = Vector3D.of(0.0, 1.0, 0.0);
    final Matrix4x4D m = this.lookAtRH(vc, vt, vu);

    Assert.assertEquals(-1.0, m.rowColumn(0, 0), 0.0);
    Assert.assertEquals(0.0, m.rowColumn(0, 1), 0.0);
    Assert.assertEquals(0.0, m.rowColumn(0, 2), 0.0);
    Assert.assertEquals(10.0, m.rowColumn(0, 3), 0.0);

    Assert.assertEquals(0.0, m.rowColumn(1, 0), 0.0);
    Assert.assertEquals(1.0, m.rowColumn(1, 1), 0.0);
    Assert.assertEquals(0.0, m.rowColumn(1, 2), 0.0);
    Assert.assertEquals(-10.0, m.rowColumn(1, 3), 0.0);

    Assert.assertEquals(0.0, m.rowColumn(2, 0), 0.0);
    Assert.assertEquals(0.0, m.rowColumn(2, 1), 0.0);
    Assert.assertEquals(-1.0, m.rowColumn(2, 2), 0.0);
    Assert.assertEquals(-10.0, m.rowColumn(2, 3), 0.0);

    Assert.assertEquals(0.0, m.rowColumn(3, 0), 0.0);
    Assert.assertEquals(0.0, m.rowColumn(3, 1), 0.0);
    Assert.assertEquals(0.0, m.rowColumn(3, 2), 0.0);
    Assert.assertEquals(1.0, m.rowColumn(3, 3), 0.0);
  }
}
