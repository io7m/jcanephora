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

package com.io7m.jcanephora.tests.core;

import com.io7m.jcanephora.core.JCGLVersionNumber;
import org.junit.Assert;
import org.junit.Test;

public final class JCGLVersionNumberTest
{
  @Test
  public void testCompareTo()
  {
    final JCGLVersionNumber v0 = new JCGLVersionNumber(1, 0, 0);
    final JCGLVersionNumber v1 = new JCGLVersionNumber(1, 0, 0);
    final JCGLVersionNumber v2 = new JCGLVersionNumber(2, 0, 0);
    final JCGLVersionNumber v3 = new JCGLVersionNumber(0, 0, 0);
    final JCGLVersionNumber v4 = new JCGLVersionNumber(2, 1, 0);
    final JCGLVersionNumber v5 = new JCGLVersionNumber(0, 1, 0);
    final JCGLVersionNumber v6 = new JCGLVersionNumber(0, 0, 1);
    final JCGLVersionNumber v7 = new JCGLVersionNumber(2, 1, 1);

    Assert.assertEquals((long) v0.compareTo(v1), 0L);
    Assert.assertTrue(v0.compareTo(v2) < 0);
    Assert.assertTrue(v0.compareTo(v3) > 0);
    Assert.assertTrue(v0.compareTo(v4) < 0);
    Assert.assertTrue(v0.compareTo(v5) > 0);
    Assert.assertTrue(v0.compareTo(v6) > 0);
    Assert.assertTrue(v0.compareTo(v7) < 0);

    Assert.assertEquals((long) v1.compareTo(v0), 0L);
    Assert.assertTrue(v1.compareTo(v2) < 0);
    Assert.assertTrue(v1.compareTo(v3) > 0);
    Assert.assertTrue(v1.compareTo(v4) < 0);
    Assert.assertTrue(v1.compareTo(v5) > 0);
    Assert.assertTrue(v1.compareTo(v6) > 0);
    Assert.assertTrue(v1.compareTo(v7) < 0);

    Assert.assertTrue(v2.compareTo(v0) > 0);
    Assert.assertEquals((long) v2.compareTo(v2), 0L);
    Assert.assertTrue(v2.compareTo(v3) > 0);
    Assert.assertTrue(v2.compareTo(v4) < 0);
    Assert.assertTrue(v2.compareTo(v5) > 0);
    Assert.assertTrue(v2.compareTo(v6) > 0);
    Assert.assertTrue(v2.compareTo(v7) < 0);

    Assert.assertTrue(v3.compareTo(v0) < 0);
    Assert.assertTrue(v3.compareTo(v2) < 0);
    Assert.assertEquals((long) v3.compareTo(v3), 0L);
    Assert.assertTrue(v3.compareTo(v4) < 0);
    Assert.assertTrue(v3.compareTo(v5) < 0);
    Assert.assertTrue(v3.compareTo(v6) < 0);
    Assert.assertTrue(v3.compareTo(v7) < 0);

    Assert.assertTrue(v4.compareTo(v0) > 0);
    Assert.assertTrue(v4.compareTo(v2) > 0);
    Assert.assertTrue(v4.compareTo(v3) > 0);
    Assert.assertEquals((long) v4.compareTo(v4), 0L);
    Assert.assertTrue(v4.compareTo(v5) > 0);
    Assert.assertTrue(v4.compareTo(v6) > 0);
    Assert.assertTrue(v4.compareTo(v7) < 0);

    Assert.assertTrue(v5.compareTo(v0) < 0);
    Assert.assertTrue(v5.compareTo(v2) < 0);
    Assert.assertTrue(v5.compareTo(v3) > 0);
    Assert.assertTrue(v5.compareTo(v4) < 0);
    Assert.assertEquals((long) v5.compareTo(v5), 0L);
    Assert.assertTrue(v5.compareTo(v6) > 0);
    Assert.assertTrue(v5.compareTo(v7) < 0);

    Assert.assertTrue(v6.compareTo(v0) < 0);
    Assert.assertTrue(v6.compareTo(v2) < 0);
    Assert.assertTrue(v6.compareTo(v3) > 0);
    Assert.assertTrue(v6.compareTo(v4) < 0);
    Assert.assertTrue(v6.compareTo(v5) < 0);
    Assert.assertEquals((long) v6.compareTo(v6), 0L);
    Assert.assertTrue(v6.compareTo(v7) < 0);

    Assert.assertTrue(v7.compareTo(v0) > 0);
    Assert.assertTrue(v7.compareTo(v2) > 0);
    Assert.assertTrue(v7.compareTo(v3) > 0);
    Assert.assertTrue(v7.compareTo(v4) > 0);
    Assert.assertTrue(v7.compareTo(v5) > 0);
    Assert.assertTrue(v7.compareTo(v6) > 0);
    Assert.assertEquals((long) v7.compareTo(v7), 0L);
  }

  @Test
  public void testEquals()
  {
    final JCGLVersionNumber v0 = new JCGLVersionNumber(1, 0, 0);
    final JCGLVersionNumber v1 = new JCGLVersionNumber(1, 0, 0);
    final JCGLVersionNumber v2 = new JCGLVersionNumber(1, 1, 0);
    final JCGLVersionNumber v3 = new JCGLVersionNumber(1, 0, 1);

    Assert.assertEquals(v0, v0);
    Assert.assertNotEquals(v0, null);
    Assert.assertNotEquals(v0, Integer.valueOf(23));
    Assert.assertEquals(v0, v1);
    Assert.assertNotEquals(v0, v2);
    Assert.assertNotEquals(v0, v3);
  }

  @Test
  public void testHashCode()
  {
    final JCGLVersionNumber v0 = new JCGLVersionNumber(1, 0, 0);
    final JCGLVersionNumber v1 = new JCGLVersionNumber(1, 0, 0);
    final JCGLVersionNumber v2 = new JCGLVersionNumber(1, 1, 0);
    final JCGLVersionNumber v3 = new JCGLVersionNumber(1, 0, 1);

    Assert.assertEquals((long) v0.hashCode(), (long) (v0.hashCode()));
    Assert.assertEquals((long) v0.hashCode(), (long) (v1.hashCode()));
    Assert.assertNotEquals((long) v0.hashCode(), (long) (v2.hashCode()));
    Assert.assertNotEquals((long) v0.hashCode(), (long) (v3.hashCode()));
  }

  @Test
  public void testIdentities()
  {
    final JCGLVersionNumber v0 = new JCGLVersionNumber(1, 0, 2);

    Assert.assertEquals(1L, (long) v0.getVersionMajor());
    Assert.assertEquals(0L, (long) v0.getVersionMinor());
    Assert.assertEquals(2L, (long) v0.getVersionMicro());
  }

  @Test
  public void testToString()
  {
    final JCGLVersionNumber v0 = new JCGLVersionNumber(1, 0, 0);
    final JCGLVersionNumber v1 = new JCGLVersionNumber(1, 0, 0);
    final JCGLVersionNumber v2 = new JCGLVersionNumber(1, 1, 0);
    final JCGLVersionNumber v3 = new JCGLVersionNumber(1, 0, 1);

    Assert.assertEquals(v0.toString(), v0.toString());
    Assert.assertEquals(v0.toString(), v1.toString());
    Assert.assertNotEquals(v0.toString(), v2.toString());
    Assert.assertNotEquals(v0.toString(), v3.toString());
  }
}
