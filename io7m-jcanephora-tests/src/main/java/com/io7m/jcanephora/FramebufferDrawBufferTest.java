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

public class FramebufferDrawBufferTest
{
  @SuppressWarnings("static-method") @Test public void testCompare()
  {
    final FramebufferDrawBuffer db0 = new FramebufferDrawBuffer(0);
    final FramebufferDrawBuffer db1 = new FramebufferDrawBuffer(1);

    Assert.assertEquals(0, db0.compareTo(db0));
    Assert.assertEquals(-1, db0.compareTo(db1));
    Assert.assertEquals(1, db1.compareTo(db0));
  }

  @SuppressWarnings("static-method") @Test public void testEquals()
  {
    final FramebufferDrawBuffer db0 = new FramebufferDrawBuffer(1);
    final FramebufferDrawBuffer db1 = new FramebufferDrawBuffer(2);
    final FramebufferDrawBuffer db2 = new FramebufferDrawBuffer(1);

    Assert.assertEquals(db0, db0);
    Assert.assertEquals(db0, db2);
    Assert.assertEquals(db2, db0);
    Assert.assertFalse(db0.equals(db1));
    Assert.assertFalse(db0.equals(null));
    Assert.assertFalse(db0.equals(Integer.valueOf(23)));
  }

  @SuppressWarnings("static-method") @Test public void testHashCode()
  {
    final FramebufferDrawBuffer db0 = new FramebufferDrawBuffer(1);
    final FramebufferDrawBuffer db1 = new FramebufferDrawBuffer(2);

    Assert.assertTrue(db0.hashCode() == db0.hashCode());
    Assert.assertTrue(db0.hashCode() != db1.hashCode());
  }

  @SuppressWarnings("static-method") @Test public void testIdentities()
  {
    final FramebufferDrawBuffer db0 = new FramebufferDrawBuffer(1);

    Assert.assertEquals(1, db0.getIndex());
  }

  @SuppressWarnings("static-method") @Test public void testToString()
  {
    final FramebufferDrawBuffer db0 = new FramebufferDrawBuffer(1);
    final FramebufferDrawBuffer db1 = new FramebufferDrawBuffer(2);
    final FramebufferDrawBuffer db2 = new FramebufferDrawBuffer(1);

    Assert.assertEquals(db0.toString(), db0.toString());
    Assert.assertEquals(db0.toString(), db2.toString());
    Assert.assertEquals(db2.toString(), db0.toString());
    Assert.assertFalse(db0.toString().equals(db1.toString()));
    Assert.assertFalse(db0.toString().equals(null));
    Assert.assertFalse(db0.toString().equals(Integer.valueOf(23)));
  }
}
