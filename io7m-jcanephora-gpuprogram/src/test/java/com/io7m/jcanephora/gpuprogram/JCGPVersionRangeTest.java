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

package com.io7m.jcanephora.gpuprogram;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.JCGLApiKindFull;
import com.io7m.jcanephora.JCGLVersionNumber;
import com.io7m.jcanephora.gpuprogram.JCGPVersionRange;

public class JCGPVersionRangeTest
{
  /**
   * Equals.
   */

  @SuppressWarnings({ "static-method" }) @Test public void testEquals()
    throws ConstraintError
  {
    final JCGLVersionNumber vlow0 = new JCGLVersionNumber(1, 0, 0);
    final JCGLVersionNumber vlow1 = new JCGLVersionNumber(1, 0, 1);
    final JCGLVersionNumber vhigh0 = new JCGLVersionNumber(2, 0, 0);
    final JCGLVersionNumber vhigh1 = new JCGLVersionNumber(2, 0, 1);

    final JCGPVersionRange<JCGLApiKindFull> vr0 =
      new JCGPVersionRange<JCGLApiKindFull>(vlow0, vhigh0);
    final JCGPVersionRange<JCGLApiKindFull> vr1 =
      new JCGPVersionRange<JCGLApiKindFull>(vlow1, vhigh0);
    final JCGPVersionRange<JCGLApiKindFull> vr2 =
      new JCGPVersionRange<JCGLApiKindFull>(vlow0, vhigh1);
    final JCGPVersionRange<JCGLApiKindFull> vr3 =
      new JCGPVersionRange<JCGLApiKindFull>(vlow1, vhigh1);

    final JCGPVersionRange<JCGLApiKindFull> vr4 =
      new JCGPVersionRange<JCGLApiKindFull>(vlow0, vhigh0);

    Assert.assertTrue(vr0.equals(vr0));
    Assert.assertFalse(vr0.equals(null));
    Assert.assertFalse(vr0.equals(Integer.valueOf(23)));
    Assert.assertFalse(vr0.equals(vr1));
    Assert.assertFalse(vr0.equals(vr2));
    Assert.assertFalse(vr0.equals(vr3));
    Assert.assertTrue(vr0.equals(vr4));
  }

  /**
   * Hashcode.
   */

  @SuppressWarnings({ "static-method" }) @Test public void testHashcode()
    throws ConstraintError
  {
    final JCGLVersionNumber vlow0 = new JCGLVersionNumber(1, 0, 0);
    final JCGLVersionNumber vlow1 = new JCGLVersionNumber(1, 0, 1);
    final JCGLVersionNumber vhigh0 = new JCGLVersionNumber(2, 0, 0);
    final JCGLVersionNumber vhigh1 = new JCGLVersionNumber(2, 0, 1);

    final JCGPVersionRange<JCGLApiKindFull> vr0 =
      new JCGPVersionRange<JCGLApiKindFull>(vlow0, vhigh0);
    final JCGPVersionRange<JCGLApiKindFull> vr1 =
      new JCGPVersionRange<JCGLApiKindFull>(vlow1, vhigh0);
    final JCGPVersionRange<JCGLApiKindFull> vr2 =
      new JCGPVersionRange<JCGLApiKindFull>(vlow0, vhigh1);
    final JCGPVersionRange<JCGLApiKindFull> vr3 =
      new JCGPVersionRange<JCGLApiKindFull>(vlow1, vhigh1);

    final JCGPVersionRange<JCGLApiKindFull> vr4 =
      new JCGPVersionRange<JCGLApiKindFull>(vlow0, vhigh0);

    Assert.assertTrue(vr0.hashCode() == vr0.hashCode());
    Assert.assertFalse(vr0.hashCode() == vr1.hashCode());
    Assert.assertFalse(vr0.hashCode() == vr2.hashCode());
    Assert.assertFalse(vr0.hashCode() == vr3.hashCode());
    Assert.assertTrue(vr0.hashCode() == vr4.hashCode());
  }

  /**
   * Identities.
   */

  @SuppressWarnings({ "static-method" }) @Test public void testIdentities()
    throws ConstraintError
  {
    final JCGLVersionNumber vlow = new JCGLVersionNumber(1, 0, 0);
    final JCGLVersionNumber vhigh = new JCGLVersionNumber(2, 0, 0);
    final JCGPVersionRange<JCGLApiKindFull> g =
      new JCGPVersionRange<JCGLApiKindFull>(vlow, vhigh);
    Assert.assertEquals(vlow, g.getLowerBound());
    Assert.assertEquals(vhigh, g.getUpperBound());
  }

  /**
   * Inclusion is correct.
   */

  @SuppressWarnings({ "static-method" }) @Test public void testIncludes()
    throws ConstraintError
  {
    final JCGLVersionNumber vlow0 = new JCGLVersionNumber(1, 0, 0);
    final JCGLVersionNumber vlow1 = new JCGLVersionNumber(1, 0, 1);
    final JCGLVersionNumber vhigh0 = new JCGLVersionNumber(2, 0, 0);
    final JCGLVersionNumber vhigh1 = new JCGLVersionNumber(2, 0, 1);

    final JCGPVersionRange<JCGLApiKindFull> vr0 =
      new JCGPVersionRange<JCGLApiKindFull>(vlow0, vhigh0);
    final JCGPVersionRange<JCGLApiKindFull> vr1 =
      new JCGPVersionRange<JCGLApiKindFull>(vlow1, vhigh0);
    final JCGPVersionRange<JCGLApiKindFull> vr2 =
      new JCGPVersionRange<JCGLApiKindFull>(vlow0, vhigh1);
    final JCGPVersionRange<JCGLApiKindFull> vr3 =
      new JCGPVersionRange<JCGLApiKindFull>(vlow1, vhigh1);

    Assert.assertTrue(vr0.includes(vlow0));
    Assert.assertTrue(vr0.includes(vlow1));
    Assert.assertTrue(vr0.includes(vhigh0));
    Assert.assertFalse(vr0.includes(vhigh1));

    Assert.assertFalse(vr1.includes(vlow0));
    Assert.assertTrue(vr1.includes(vlow1));
    Assert.assertTrue(vr1.includes(vhigh0));
    Assert.assertFalse(vr1.includes(vhigh1));

    Assert.assertTrue(vr2.includes(vlow0));
    Assert.assertTrue(vr2.includes(vlow1));
    Assert.assertTrue(vr2.includes(vhigh0));
    Assert.assertTrue(vr2.includes(vhigh1));

    Assert.assertFalse(vr3.includes(vlow0));
    Assert.assertTrue(vr3.includes(vlow1));
    Assert.assertTrue(vr3.includes(vhigh0));
    Assert.assertTrue(vr3.includes(vhigh1));
  }

  /**
   * Passing a higher lower bound than the upper bound fails.
   */

  @SuppressWarnings({ "static-method", "unused" }) @Test(
    expected = ConstraintError.class) public void testIncorrectRange()
    throws ConstraintError
  {
    new JCGPVersionRange<JCGLApiKindFull>(
      new JCGLVersionNumber(2, 0, 0),
      new JCGLVersionNumber(1, 0, 0));
  }

  /**
   * Passing null as the lower bound fails.
   */

  @SuppressWarnings({ "static-method", "unused" }) @Test(
    expected = ConstraintError.class) public void testNullLower()
    throws ConstraintError
  {
    new JCGPVersionRange<JCGLApiKindFull>(null, new JCGLVersionNumber(
      1,
      0,
      0));
  }

  /**
   * Passing null as the upper bound fails.
   */

  @SuppressWarnings({ "static-method", "unused" }) @Test(
    expected = ConstraintError.class) public void testNullUpper()
    throws ConstraintError
  {
    new JCGPVersionRange<JCGLApiKindFull>(
      new JCGLVersionNumber(1, 0, 0),
      null);
  }

  /**
   * toString
   */

  @SuppressWarnings({ "static-method" }) @Test public void testToString()
    throws ConstraintError
  {
    final JCGLVersionNumber vlow0 = new JCGLVersionNumber(1, 0, 0);
    final JCGLVersionNumber vlow1 = new JCGLVersionNumber(1, 0, 1);
    final JCGLVersionNumber vhigh0 = new JCGLVersionNumber(2, 0, 0);
    final JCGLVersionNumber vhigh1 = new JCGLVersionNumber(2, 0, 1);

    final JCGPVersionRange<JCGLApiKindFull> vr0 =
      new JCGPVersionRange<JCGLApiKindFull>(vlow0, vhigh0);
    final JCGPVersionRange<JCGLApiKindFull> vr1 =
      new JCGPVersionRange<JCGLApiKindFull>(vlow1, vhigh0);
    final JCGPVersionRange<JCGLApiKindFull> vr2 =
      new JCGPVersionRange<JCGLApiKindFull>(vlow0, vhigh1);
    final JCGPVersionRange<JCGLApiKindFull> vr3 =
      new JCGPVersionRange<JCGLApiKindFull>(vlow1, vhigh1);

    final JCGPVersionRange<JCGLApiKindFull> vr4 =
      new JCGPVersionRange<JCGLApiKindFull>(vlow0, vhigh0);

    Assert.assertTrue(vr0.toString().equals(vr0.toString()));
    Assert.assertFalse(vr0.toString().equals(vr1.toString()));
    Assert.assertFalse(vr0.toString().equals(vr2.toString()));
    Assert.assertFalse(vr0.toString().equals(vr3.toString()));
    Assert.assertTrue(vr0.toString().equals(vr4.toString()));
  }
}
