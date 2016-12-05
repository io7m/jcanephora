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

import com.io7m.jcanephora.core.JCGLPolygonMode;
import com.io7m.jcanephora.core.api.JCGLPolygonModesType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Polygon modes contract.
 */

// CHECKSTYLE_JAVADOC:OFF

public abstract class JCGLPolygonModesContract extends JCGLContract
{
  @Rule public final ExpectedException expected = ExpectedException.none();

  protected abstract JCGLPolygonModesType getPolygonModes(String name);

  @Test
  public final void testIdentities()
  {
    final JCGLPolygonModesType g_p = this.getPolygonModes("main");

    Assert.assertEquals(JCGLPolygonMode.POLYGON_FILL, g_p.polygonGetMode());

    for (final JCGLPolygonMode v : JCGLPolygonMode.values()) {
      g_p.polygonSetMode(v);
      Assert.assertEquals(v, g_p.polygonGetMode());
    }
  }
}
