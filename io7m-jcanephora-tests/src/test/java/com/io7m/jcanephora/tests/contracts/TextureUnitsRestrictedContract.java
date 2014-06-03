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

package com.io7m.jcanephora.tests.contracts;

import java.util.List;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.TextureUnitType;
import com.io7m.jcanephora.api.JCGLSoftRestrictionsType;
import com.io7m.jcanephora.api.JCGLTextureUnitsType;
import com.io7m.jcanephora.tests.TestContext;

public abstract class TextureUnitsRestrictedContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract boolean isGLSupported();

  public abstract TestContext newTestContext(
    final JCGLSoftRestrictionsType r)
    throws JCGLException;

  /**
   * Restricting the number of texture units works.
   */

  @Test public final void testGetUnits()
    throws JCGLException
  {
    final TestContext tc =
      this.newTestContext(new JCGLSoftRestrictionsType() {
        @Override public boolean restrictExtensionVisibility(
          final String name)
        {
          return true;
        }

        @Override public int restrictTextureUnitCount(
          final int count)
        {
          return 1;
        }
      });

    final JCGLTextureUnitsType gl = tc.getGLImplementation().getGLCommon();
    final List<TextureUnitType> u = gl.textureGetUnits();
    Assert.assertTrue(u.size() == 1);
  }

}
