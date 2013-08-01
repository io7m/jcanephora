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

public class JCGLSLVersionNumberTest
{
  @SuppressWarnings("static-method") @Test public void testCompareTo()
  {
    final JCGLSLVersionNumber v0 = new JCGLSLVersionNumber(1, 0);
    final JCGLSLVersionNumber v1 = new JCGLSLVersionNumber(1, 0);
    final JCGLSLVersionNumber v2 = new JCGLSLVersionNumber(2, 0);
    final JCGLSLVersionNumber v3 = new JCGLSLVersionNumber(0, 0);
    final JCGLSLVersionNumber v4 = new JCGLSLVersionNumber(2, 1);
    final JCGLSLVersionNumber v5 = new JCGLSLVersionNumber(0, 1);

    Assert.assertTrue(v0.compareTo(v1) == 0);
    Assert.assertTrue(v0.compareTo(v2) < 0);
    Assert.assertTrue(v0.compareTo(v3) > 0);
    Assert.assertTrue(v0.compareTo(v4) < 0);
    Assert.assertTrue(v0.compareTo(v5) > 0);

    Assert.assertTrue(v1.compareTo(v0) == 0);
    Assert.assertTrue(v1.compareTo(v2) < 0);
    Assert.assertTrue(v1.compareTo(v3) > 0);
    Assert.assertTrue(v1.compareTo(v4) < 0);
    Assert.assertTrue(v1.compareTo(v5) > 0);

    Assert.assertTrue(v2.compareTo(v0) > 0);
    Assert.assertTrue(v2.compareTo(v2) == 0);
    Assert.assertTrue(v2.compareTo(v3) > 0);
    Assert.assertTrue(v2.compareTo(v4) < 0);
    Assert.assertTrue(v2.compareTo(v5) > 0);

    Assert.assertTrue(v3.compareTo(v0) < 0);
    Assert.assertTrue(v3.compareTo(v2) < 0);
    Assert.assertTrue(v3.compareTo(v3) == 0);
    Assert.assertTrue(v3.compareTo(v4) < 0);
    Assert.assertTrue(v3.compareTo(v5) < 0);

    Assert.assertTrue(v4.compareTo(v0) > 0);
    Assert.assertTrue(v4.compareTo(v2) > 0);
    Assert.assertTrue(v4.compareTo(v3) > 0);
    Assert.assertTrue(v4.compareTo(v4) == 0);
    Assert.assertTrue(v4.compareTo(v5) > 0);

    Assert.assertTrue(v5.compareTo(v0) < 0);
    Assert.assertTrue(v5.compareTo(v2) < 0);
    Assert.assertTrue(v5.compareTo(v3) > 0);
    Assert.assertTrue(v5.compareTo(v4) < 0);
    Assert.assertTrue(v5.compareTo(v5) == 0);
  }

  @SuppressWarnings("static-method") @Test public void testEquals()
  {
    final JCGLSLVersionNumber v0 = new JCGLSLVersionNumber(1, 0);
    final JCGLSLVersionNumber v1 = new JCGLSLVersionNumber(1, 0);
    final JCGLSLVersionNumber v2 = new JCGLSLVersionNumber(1, 1);

    Assert.assertTrue(v0.equals(v0));
    Assert.assertFalse(v0.equals(null));
    Assert.assertFalse(v0.equals(Integer.valueOf(23)));
    Assert.assertTrue(v0.equals(v1));
    Assert.assertFalse(v0.equals(v2));
  }

  @SuppressWarnings("static-method") @Test public void testHashCode()
  {
    final JCGLSLVersionNumber v0 = new JCGLSLVersionNumber(1, 0);
    final JCGLSLVersionNumber v1 = new JCGLSLVersionNumber(1, 0);
    final JCGLSLVersionNumber v2 = new JCGLSLVersionNumber(1, 1);

    Assert.assertTrue(v0.hashCode() == (v0.hashCode()));
    Assert.assertTrue(v0.hashCode() == (v1.hashCode()));
    Assert.assertFalse(v0.hashCode() == (v2.hashCode()));
  }

  @SuppressWarnings("static-method") @Test public void testIdentities()
  {
    final JCGLSLVersionNumber v0 = new JCGLSLVersionNumber(1, 0);

    Assert.assertEquals(1, v0.getVersionMajor());
    Assert.assertEquals(0, v0.getVersionMinor());
  }

  @SuppressWarnings("static-method") @Test public void testToString()
  {
    final JCGLSLVersionNumber v0 = new JCGLSLVersionNumber(1, 0);
    final JCGLSLVersionNumber v1 = new JCGLSLVersionNumber(1, 0);
    final JCGLSLVersionNumber v2 = new JCGLSLVersionNumber(1, 1);

    Assert.assertTrue(v0.toString().equals(v0.toString()));
    Assert.assertTrue(v0.toString().equals(v1.toString()));
    Assert.assertFalse(v0.toString().equals(v2.toString()));
  }
}