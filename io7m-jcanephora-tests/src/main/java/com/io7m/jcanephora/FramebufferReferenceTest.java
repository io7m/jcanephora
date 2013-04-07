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

public class FramebufferReferenceTest
{
  @SuppressWarnings("static-method") @Test public void testEquals()
    throws ConstraintError
  {
    final FramebufferReference f0 = new FramebufferReference(1);
    final FramebufferReference f1 = new FramebufferReference(2);
    final FramebufferReference f2 = new FramebufferReference(1);

    Assert.assertEquals(f0, f0);
    Assert.assertEquals(f0, f2);
    Assert.assertEquals(f2, f0);
    Assert.assertFalse(f0.equals(f1));
    Assert.assertFalse(f0.equals(null));
    Assert.assertFalse(f0.equals(Integer.valueOf(23)));
  }

  @SuppressWarnings("static-method") @Test public void testHashCode()
    throws ConstraintError
  {
    final FramebufferReference f0 = new FramebufferReference(1);
    final FramebufferReference f1 = new FramebufferReference(2);

    Assert.assertTrue(f0.hashCode() == f0.hashCode());
    Assert.assertTrue(f0.hashCode() != f1.hashCode());
  }

  @SuppressWarnings("static-method") @Test public void testIdentities()
    throws ConstraintError
  {
    final FramebufferReference f0 = new FramebufferReference(1);

    Assert.assertEquals(1, f0.getGLName());
  }

  @SuppressWarnings("static-method") @Test public void testToString()
    throws ConstraintError
  {
    final FramebufferReference f0 = new FramebufferReference(1);
    final FramebufferReference f1 = new FramebufferReference(2);
    final FramebufferReference f2 = new FramebufferReference(1);

    Assert.assertEquals(f0.toString(), f0.toString());
    Assert.assertEquals(f0.toString(), f2.toString());
    Assert.assertEquals(f2.toString(), f0.toString());
    Assert.assertFalse(f0.toString().equals(f1.toString()));
    Assert.assertFalse(f0.toString().equals(null));
    Assert.assertFalse(f0.toString().equals(Integer.valueOf(23)));
  }
}
