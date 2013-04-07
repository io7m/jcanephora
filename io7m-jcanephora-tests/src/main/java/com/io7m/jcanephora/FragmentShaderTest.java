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

import com.io7m.jaux.Constraints.ConstraintError;

public class FragmentShaderTest
{
  @SuppressWarnings("static-method") @Test public void testEquals()
    throws ConstraintError
  {
    final FragmentShader pr0 = new FragmentShader(1, "xyz");
    final FragmentShader pr1 = new FragmentShader(2, "xyz");
    final FragmentShader pr2 = new FragmentShader(1, "xyz");

    Assert.assertEquals(pr0, pr0);
    Assert.assertEquals(pr0, pr2);
    Assert.assertEquals(pr2, pr0);
    Assert.assertFalse(pr0.equals(pr1));
    Assert.assertFalse(pr0.equals(null));
    Assert.assertFalse(pr0.equals(Integer.valueOf(23)));
  }

  @SuppressWarnings("static-method") @Test public void testHashCode()
    throws ConstraintError
  {
    final FragmentShader pr0 = new FragmentShader(1, "xyz");
    final FragmentShader pr1 = new FragmentShader(2, "xyz");
    final FragmentShader pr2 = new FragmentShader(1, "xyz");
    final FragmentShader pr3 = new FragmentShader(2, "abc");
    final FragmentShader pr4 = new FragmentShader(1, "abc");

    Assert.assertTrue(pr0.hashCode() == pr0.hashCode());
    Assert.assertTrue(pr0.hashCode() == pr2.hashCode());
    Assert.assertTrue(pr2.hashCode() == pr0.hashCode());

    Assert.assertTrue(pr0.hashCode() != pr1.hashCode());
    Assert.assertTrue(pr0.hashCode() != pr3.hashCode());
    Assert.assertTrue(pr0.hashCode() != pr4.hashCode());

    Assert.assertTrue(pr3.hashCode() != pr4.hashCode());
  }

  @SuppressWarnings("static-method") @Test public void testIdentities()
    throws ConstraintError
  {
    final FragmentShader pr0 = new FragmentShader(1, "xyz");

    Assert.assertEquals(1, pr0.getGLName());
    Assert.assertEquals("xyz", pr0.getName());
  }

  @SuppressWarnings("static-method") @Test public void testToString()
    throws ConstraintError
  {
    final FragmentShader pr0 = new FragmentShader(1, "xyz");
    final FragmentShader pr1 = new FragmentShader(2, "xyz");
    final FragmentShader pr2 = new FragmentShader(1, "xyz");

    Assert.assertEquals(pr0.toString(), pr0.toString());
    Assert.assertEquals(pr0.toString(), pr2.toString());
    Assert.assertEquals(pr2.toString(), pr0.toString());
    Assert.assertFalse(pr0.toString().equals(pr1.toString()));
    Assert.assertFalse(pr0.toString().equals(null));
    Assert.assertFalse(pr0.toString().equals(Integer.valueOf(23)));
  }
}
