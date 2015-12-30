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

import com.io7m.jcanephora.core.JCGLViewMatricesType;
import com.io7m.jtensors.Matrix4x4FType;
import com.io7m.jtensors.MatrixHeapArrayM4x4F;
import com.io7m.jtensors.VectorM3F;
import org.junit.Assert;
import org.junit.Test;

/**
 * View matrix contract.
 */

// CHECKSTYLE_JAVADOC:OFF

public abstract class JCGLViewMatricesContract
{
  protected abstract JCGLViewMatricesType newViewMatrices();

  @Test
  public final void testLookAt()
  {
    final JCGLViewMatricesType vm = this.newViewMatrices();

    final Matrix4x4FType m = MatrixHeapArrayM4x4F.newMatrix();
    final VectorM3F vc = new VectorM3F(10.0f, 10.0f, -10.0f);
    final VectorM3F vt = new VectorM3F(10.0f, 10.0f, 0.0f);
    final VectorM3F vu = new VectorM3F(0.0f, 1.0f, 0.0f);

    vm.lookAt(m, vc, vt, vu);

    Assert.assertEquals(-1.0, (double) m.getRowColumnF(0, 0), 0.0);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(0, 1), 0.0);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(0, 2), 0.0);
    Assert.assertEquals(10.0, (double) m.getRowColumnF(0, 3), 0.0);

    Assert.assertEquals(0.0, (double) m.getRowColumnF(1, 0), 0.0);
    Assert.assertEquals(1.0, (double) m.getRowColumnF(1, 1), 0.0);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(1, 2), 0.0);
    Assert.assertEquals(-10.0, (double) m.getRowColumnF(1, 3), 0.0);

    Assert.assertEquals(0.0, (double) m.getRowColumnF(2, 0), 0.0);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(2, 1), 0.0);
    Assert.assertEquals(-1.0, (double) m.getRowColumnF(2, 2), 0.0);
    Assert.assertEquals(-10.0, (double) m.getRowColumnF(2, 3), 0.0);

    Assert.assertEquals(0.0, (double) m.getRowColumnF(3, 0), 0.0);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(3, 1), 0.0);
    Assert.assertEquals(0.0, (double) m.getRowColumnF(3, 2), 0.0);
    Assert.assertEquals(1.0, (double) m.getRowColumnF(3, 3), 0.0);
  }
}
