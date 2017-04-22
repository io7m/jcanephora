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

import com.io7m.jcanephora.core.JCGLFaceSelection;
import com.io7m.jcanephora.core.JCGLFaceWindingOrder;
import com.io7m.jcanephora.core.api.JCGLCullingType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Blending contracts.
 */

// CHECKSTYLE_JAVADOC:OFF

public abstract class JCGLCullingContract extends JCGLContract
{
  @Rule public final ExpectedException expected = ExpectedException.none();

  protected abstract JCGLCullingType getCulling(String name);

  @Test
  public final void testCullingIdentities()
  {
    final JCGLCullingType g_c = this.getCulling("main");

    Assert.assertFalse(g_c.cullingIsEnabled());

    for (final JCGLFaceSelection s : JCGLFaceSelection.values()) {
      for (final JCGLFaceWindingOrder w : JCGLFaceWindingOrder.values()) {
        g_c.cullingEnable(s, w);
        Assert.assertTrue(g_c.cullingIsEnabled());
        g_c.cullingDisable();
        Assert.assertFalse(g_c.cullingIsEnabled());
      }
    }
  }
}
