/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
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
package com.io7m.jcanephora.tests.jogl.contracts.jogles3;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jcanephora.TextureWrapR;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.jogl.JOGL_GLTypeConversions;
import com.io7m.junreachable.UnreachableCodeException;

public class JOGLES3TextureWrapTest
{
  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testRNonsense()
  {
    JOGL_GLTypeConversions.textureWrapRFromGL(-1);
  }

  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testSNonsense()
  {
    JOGL_GLTypeConversions.textureWrapSFromGL(-1);
  }

  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testTNonsense()
  {
    JOGL_GLTypeConversions.textureWrapTFromGL(-1);
  }

  /**
   * ∀w. textureWrapRFromGL(textureWrapRToGL(w)) == w.
   */

  @SuppressWarnings("static-method") @Test public void testWrapRBijection()
  {
    for (final TextureWrapR w : TextureWrapR.values()) {
      Assert.assertEquals(w, JOGL_GLTypeConversions
        .textureWrapRFromGL(JOGL_GLTypeConversions.textureWrapRToGL(w)));
    }
  }

  /**
   * ∀w. textureWrapSFromGL(textureWrapSToGL(w)) == w.
   */

  @SuppressWarnings("static-method") @Test public void testWrapSBijection()
  {
    for (final TextureWrapS w : TextureWrapS.values()) {
      Assert.assertEquals(w, JOGL_GLTypeConversions
        .textureWrapSFromGL(JOGL_GLTypeConversions.textureWrapSToGL(w)));
    }
  }

  /**
   * ∀w. textureWrapTFromGL(textureWrapTToGL(w)) == w.
   */

  @SuppressWarnings("static-method") @Test public void testWrapTBijection()
  {
    for (final TextureWrapT w : TextureWrapT.values()) {
      Assert.assertEquals(w, JOGL_GLTypeConversions
        .textureWrapTFromGL(JOGL_GLTypeConversions.textureWrapTToGL(w)));
    }
  }
}
