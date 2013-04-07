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
package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

public class TextureUnitTest
{
  @SuppressWarnings("static-method") @Test public void testEquals()
  {
    final TextureUnit tu0 = new TextureUnit(1);
    final TextureUnit tu1 = new TextureUnit(2);
    final TextureUnit tu2 = new TextureUnit(1);

    Assert.assertEquals(tu0, tu0);
    Assert.assertEquals(tu0, tu2);
    Assert.assertEquals(tu2, tu0);
    Assert.assertFalse(tu0.equals(tu1));
    Assert.assertFalse(tu0.equals(null));
    Assert.assertFalse(tu0.equals(Integer.valueOf(23)));
  }

  @SuppressWarnings("static-method") @Test public void testHashCode()
  {
    final TextureUnit tu0 = new TextureUnit(1);
    final TextureUnit tu1 = new TextureUnit(2);

    Assert.assertTrue(tu0.hashCode() == tu0.hashCode());
    Assert.assertTrue(tu0.hashCode() != tu1.hashCode());
  }

  @SuppressWarnings("static-method") @Test public void testIdentities()
  {
    final TextureUnit tu0 = new TextureUnit(1);

    Assert.assertEquals(1, tu0.getIndex());
  }

  @SuppressWarnings("static-method") @Test public void testToString()
  {
    final TextureUnit tu0 = new TextureUnit(1);
    final TextureUnit tu1 = new TextureUnit(2);
    final TextureUnit tu2 = new TextureUnit(1);

    Assert.assertEquals(tu0.toString(), tu0.toString());
    Assert.assertEquals(tu0.toString(), tu2.toString());
    Assert.assertEquals(tu2.toString(), tu0.toString());
    Assert.assertFalse(tu0.toString().equals(tu1.toString()));
    Assert.assertFalse(tu0.toString().equals(null));
    Assert.assertFalse(tu0.toString().equals(Integer.valueOf(23)));
  }
}
