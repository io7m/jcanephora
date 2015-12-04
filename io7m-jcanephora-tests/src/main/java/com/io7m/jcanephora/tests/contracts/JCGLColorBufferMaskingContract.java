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

import com.io7m.jcanephora.core.api.JCGLColorBufferMaskingType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Array buffer contracts.
 */

// CHECKSTYLE_JAVADOC:OFF

public abstract class JCGLColorBufferMaskingContract extends JCGLContract
{
  @Rule public final ExpectedException expected = ExpectedException.none();

  protected abstract JCGLColorBufferMaskingType getColorMasking(String name);

  @Test public final void testColorMasking()
  {
    final JCGLColorBufferMaskingType g_cm = this.getColorMasking("main");

    Assert.assertTrue(g_cm.colorBufferMaskStatusRed());
    Assert.assertTrue(g_cm.colorBufferMaskStatusGreen());
    Assert.assertTrue(g_cm.colorBufferMaskStatusBlue());
    Assert.assertTrue(g_cm.colorBufferMaskStatusAlpha());

    g_cm.colorBufferMask(true, false, false, false);
    Assert.assertTrue(g_cm.colorBufferMaskStatusRed());
    Assert.assertFalse(g_cm.colorBufferMaskStatusGreen());
    Assert.assertFalse(g_cm.colorBufferMaskStatusBlue());
    Assert.assertFalse(g_cm.colorBufferMaskStatusAlpha());

    g_cm.colorBufferMask(false, true, false, false);
    Assert.assertFalse(g_cm.colorBufferMaskStatusRed());
    Assert.assertTrue(g_cm.colorBufferMaskStatusGreen());
    Assert.assertFalse(g_cm.colorBufferMaskStatusBlue());
    Assert.assertFalse(g_cm.colorBufferMaskStatusAlpha());

    g_cm.colorBufferMask(false, false, true, false);
    Assert.assertFalse(g_cm.colorBufferMaskStatusRed());
    Assert.assertFalse(g_cm.colorBufferMaskStatusGreen());
    Assert.assertTrue(g_cm.colorBufferMaskStatusBlue());
    Assert.assertFalse(g_cm.colorBufferMaskStatusAlpha());

    g_cm.colorBufferMask(false, false, false, true);
    Assert.assertFalse(g_cm.colorBufferMaskStatusRed());
    Assert.assertFalse(g_cm.colorBufferMaskStatusGreen());
    Assert.assertFalse(g_cm.colorBufferMaskStatusBlue());
    Assert.assertTrue(g_cm.colorBufferMaskStatusAlpha());
  }

}
