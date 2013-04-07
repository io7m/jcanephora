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
import com.io7m.jaux.RangeInclusive;

public class AreaInclusiveTest
{
  @SuppressWarnings("static-method") @Test public void testAreaEquals()
    throws ConstraintError
  {
    final RangeInclusive r0 = new RangeInclusive(0, 99);
    final RangeInclusive r1 = new RangeInclusive(0, 99);
    final RangeInclusive r2 = new RangeInclusive(0, 98);
    final RangeInclusive r3 = new RangeInclusive(1, 99);

    final AreaInclusive a0 = new AreaInclusive(r0, r0);
    final AreaInclusive a1 = new AreaInclusive(r1, r1);
    final AreaInclusive a2 = new AreaInclusive(r0, r2);
    final AreaInclusive a3 = new AreaInclusive(r0, r3);
    final AreaInclusive a4 = new AreaInclusive(r2, r0);
    final AreaInclusive a5 = new AreaInclusive(r3, r0);

    Assert.assertTrue(a0.equals(a0));
    Assert.assertTrue(a0.equals(a1));
    Assert.assertTrue(a1.equals(a0));

    Assert.assertFalse(a0.equals(a2));
    Assert.assertFalse(a0.equals(a3));
    Assert.assertFalse(a0.equals(a4));
    Assert.assertFalse(a0.equals(a5));

    Assert.assertFalse(a0.equals(null));
    Assert.assertFalse(a0.equals(Integer.valueOf(32)));
  }

  @SuppressWarnings("static-method") @Test public void testAreaHashcode()
    throws ConstraintError
  {
    final RangeInclusive r0 = new RangeInclusive(0, 99);
    final RangeInclusive r1 = new RangeInclusive(0, 99);
    final RangeInclusive r2 = new RangeInclusive(0, 98);
    final RangeInclusive r3 = new RangeInclusive(1, 99);

    final AreaInclusive a0 = new AreaInclusive(r0, r0);
    final AreaInclusive a1 = new AreaInclusive(r1, r1);
    final AreaInclusive a2 = new AreaInclusive(r0, r2);
    final AreaInclusive a3 = new AreaInclusive(r0, r3);
    final AreaInclusive a4 = new AreaInclusive(r2, r0);
    final AreaInclusive a5 = new AreaInclusive(r3, r0);

    Assert.assertTrue(a0.hashCode() == a0.hashCode());
    Assert.assertTrue(a0.hashCode() == a1.hashCode());

    Assert.assertFalse(a0.hashCode() == a2.hashCode());
    Assert.assertFalse(a0.hashCode() == a3.hashCode());
    Assert.assertFalse(a0.hashCode() == a4.hashCode());
    Assert.assertFalse(a0.hashCode() == a5.hashCode());
  }

  @SuppressWarnings("static-method") @Test public void testAreaIdentities()
    throws ConstraintError
  {
    final RangeInclusive r0 = new RangeInclusive(0, 99);
    final RangeInclusive r1 = new RangeInclusive(1, 98);

    final AreaInclusive a0 = new AreaInclusive(r0, r1);

    Assert.assertTrue(a0.getRangeX() == r0);
    Assert.assertTrue(a0.getRangeY() == r1);
  }

  @SuppressWarnings("static-method") @Test public void testAreaIncluded()
    throws ConstraintError
  {
    final RangeInclusive r0 = new RangeInclusive(0, 99);
    final RangeInclusive r1 = new RangeInclusive(1, 99);

    final AreaInclusive a0 = new AreaInclusive(r0, r0);
    final AreaInclusive a1 = new AreaInclusive(r0, r1);
    final AreaInclusive a2 = new AreaInclusive(r1, r0);

    Assert.assertTrue(a0.isIncludedIn(a0));
    Assert.assertTrue(a1.isIncludedIn(a0));
    Assert.assertFalse(a0.isIncludedIn(a1));
    Assert.assertTrue(a2.isIncludedIn(a0));
    Assert.assertFalse(a0.isIncludedIn(a2));
  }

  @SuppressWarnings("static-method") @Test public void testAreaToString()
    throws ConstraintError
  {
    final RangeInclusive r0 = new RangeInclusive(0, 99);
    final RangeInclusive r1 = new RangeInclusive(0, 99);
    final RangeInclusive r2 = new RangeInclusive(0, 98);
    final RangeInclusive r3 = new RangeInclusive(1, 99);

    final AreaInclusive a0 = new AreaInclusive(r0, r0);
    final AreaInclusive a1 = new AreaInclusive(r1, r1);
    final AreaInclusive a2 = new AreaInclusive(r0, r2);
    final AreaInclusive a3 = new AreaInclusive(r0, r3);
    final AreaInclusive a4 = new AreaInclusive(r2, r0);
    final AreaInclusive a5 = new AreaInclusive(r3, r0);

    Assert.assertTrue(a0.toString().equals(a0.toString()));
    Assert.assertTrue(a0.toString().equals(a1.toString()));
    Assert.assertTrue(a1.toString().equals(a0.toString()));

    Assert.assertFalse(a0.toString().equals(a2.toString()));
    Assert.assertFalse(a0.toString().equals(a3.toString()));
    Assert.assertFalse(a0.toString().equals(a4.toString()));
    Assert.assertFalse(a0.toString().equals(a5.toString()));
  }
}
