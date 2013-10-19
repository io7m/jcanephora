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

public class AreaCursorTest
{
  @SuppressWarnings("static-method") @Test public void testEquals()
    throws ConstraintError
  {
    final AreaInclusive area0 =
      new AreaInclusive(new RangeInclusive(0, 3), new RangeInclusive(0, 1));
    final AreaInclusive area1 =
      new AreaInclusive(new RangeInclusive(0, 4), new RangeInclusive(0, 1));
    final AreaInclusive area2 =
      new AreaInclusive(new RangeInclusive(0, 4), new RangeInclusive(0, 1));
    final AreaInclusive area3 =
      new AreaInclusive(new RangeInclusive(0, 4), new RangeInclusive(0, 2));

    final AreaCursor c0 = new AreaCursor(area0, area0, 2);
    final AreaCursor c1 = new AreaCursor(area1, area0, 2);
    final AreaCursor c2 = new AreaCursor(area2, area0, 2);
    final AreaCursor c3 = new AreaCursor(area3, area2, 2);
    final AreaCursor c4 = new AreaCursor(area3, area2, 2);
    final AreaCursor c5 = new AreaCursor(area3, area2, 3);

    Assert.assertTrue(c0.equals(c0));
    Assert.assertFalse(c0.equals(null));
    Assert.assertFalse(c0.equals(Integer.valueOf(23)));
    Assert.assertFalse(c0.equals(c1));
    Assert.assertTrue(c1.equals(c2));
    Assert.assertFalse(c2.equals(c3));

    c4.seekTo(1, 0);
    Assert.assertFalse(c3.equals(c4));
    Assert.assertFalse(c3.equals(c5));
  }

  @SuppressWarnings("static-method") @Test public void testFourByte()
    throws ConstraintError
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusive(0, 3), new RangeInclusive(0, 1));
    final AreaCursor c = new AreaCursor(area, area, 4);

    Assert.assertTrue(c.isValid());
    Assert.assertEquals(0, c.getByteOffset());

    for (long y = 0; y <= 1; ++y) {
      for (long x = 0; x <= 3; ++x) {
        Assert.assertEquals(x, c.getElementX());
        Assert.assertEquals(y, c.getElementY());
        Assert.assertEquals((y * (4 * 4)) + (x * 4), c.getByteOffset());
        c.next();
      }
    }

    Assert.assertFalse(c.isValid());
  }

  @SuppressWarnings("static-method") @Test public void testHashcode()
    throws ConstraintError
  {
    final AreaInclusive area0 =
      new AreaInclusive(new RangeInclusive(0, 3), new RangeInclusive(0, 1));
    final AreaInclusive area1 =
      new AreaInclusive(new RangeInclusive(0, 4), new RangeInclusive(0, 1));
    final AreaInclusive area2 =
      new AreaInclusive(new RangeInclusive(0, 4), new RangeInclusive(0, 1));
    final AreaInclusive area3 =
      new AreaInclusive(new RangeInclusive(0, 4), new RangeInclusive(0, 2));

    final AreaCursor c0 = new AreaCursor(area0, area0, 2);
    final AreaCursor c1 = new AreaCursor(area1, area0, 2);
    final AreaCursor c2 = new AreaCursor(area2, area0, 2);
    final AreaCursor c3 = new AreaCursor(area3, area2, 2);
    final AreaCursor c4 = new AreaCursor(area3, area2, 2);
    final AreaCursor c5 = new AreaCursor(area3, area2, 3);

    Assert.assertTrue(c0.hashCode() == (c0.hashCode()));
    Assert.assertFalse(c0.hashCode() == (c1.hashCode()));
    Assert.assertTrue(c1.hashCode() == (c2.hashCode()));
    Assert.assertFalse(c2.hashCode() == (c3.hashCode()));

    c4.seekTo(1, 0);
    Assert.assertFalse(c3.hashCode() == (c4.hashCode()));
    Assert.assertFalse(c3.hashCode() == (c5.hashCode()));
  }

  @SuppressWarnings({ "static-method", "unused" }) @Test(
    expected = IllegalArgumentException.class) public
    void
    testIllegalLowBytes()
      throws ConstraintError
  {
    final AreaInclusive area_outer =
      new AreaInclusive(new RangeInclusive(0, 6), new RangeInclusive(0, 6));
    final AreaInclusive area_inner =
      new AreaInclusive(new RangeInclusive(0, 6), new RangeInclusive(0, 6));
    new AreaCursor(area_outer, area_inner, -1);
  }

  @SuppressWarnings({ "static-method", "unused" }) @Test(
    expected = IllegalArgumentException.class) public void testIllegalLowX()
    throws ConstraintError
  {
    final AreaInclusive area_outer =
      new AreaInclusive(new RangeInclusive(-1, 6), new RangeInclusive(4, 6));
    final AreaInclusive area_inner =
      new AreaInclusive(new RangeInclusive(-1, 6), new RangeInclusive(4, 6));
    new AreaCursor(area_outer, area_inner, 4);
  }

  @SuppressWarnings({ "static-method", "unused" }) @Test(
    expected = IllegalArgumentException.class) public void testIllegalLowY()
    throws ConstraintError
  {
    final AreaInclusive area_outer =
      new AreaInclusive(new RangeInclusive(1, 6), new RangeInclusive(-1, 6));
    final AreaInclusive area_inner =
      new AreaInclusive(new RangeInclusive(1, 6), new RangeInclusive(-1, 6));
    new AreaCursor(area_outer, area_inner, 4);
  }

  @SuppressWarnings("static-method") @Test public
    void
    testNonzeroAreaFourBytes()
      throws ConstraintError
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusive(2, 6), new RangeInclusive(4, 6));
    final AreaCursor c = new AreaCursor(area, area, 4);
    final long width = area.getRangeX().getInterval();

    Assert.assertEquals(2, c.getElementX());
    Assert.assertEquals(4, c.getElementY());
    Assert.assertEquals((4 * width * 4) + (2 * 4), c.getByteOffset());
  }

  @SuppressWarnings("static-method") @Test public
    void
    testNonzeroAreaOneByte()
      throws ConstraintError
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusive(2, 6), new RangeInclusive(4, 6));
    final AreaCursor c = new AreaCursor(area, area, 1);
    final long width = area.getRangeX().getInterval();

    Assert.assertEquals(2, c.getElementX());
    Assert.assertEquals(4, c.getElementY());
    Assert.assertEquals((4 * width) + 2, c.getByteOffset());
  }

  @SuppressWarnings("static-method") @Test public
    void
    testNonzeroAreaThreeBytes()
      throws ConstraintError
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusive(2, 6), new RangeInclusive(4, 6));
    final AreaCursor c = new AreaCursor(area, area, 3);
    final long width = area.getRangeX().getInterval();

    Assert.assertEquals(2, c.getElementX());
    Assert.assertEquals(4, c.getElementY());
    Assert.assertEquals((4 * width * 3) + (2 * 3), c.getByteOffset());
  }

  @SuppressWarnings("static-method") @Test public
    void
    testNonzeroAreaTwoBytes()
      throws ConstraintError
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusive(2, 6), new RangeInclusive(4, 6));
    final AreaCursor c = new AreaCursor(area, area, 2);
    final long width = area.getRangeX().getInterval();

    Assert.assertEquals(2, c.getElementX());
    Assert.assertEquals(4, c.getElementY());
    Assert.assertEquals((4 * width * 2) + (2 * 2), c.getByteOffset());
  }

  @SuppressWarnings("static-method") @Test public
    void
    testNonzeroAreaTwoBytesAgain()
      throws ConstraintError
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusive(4, 7), new RangeInclusive(4, 7));
    final AreaCursor c = new AreaCursor(area, area, 2);
    final long width = area.getRangeX().getInterval();

    Assert.assertEquals(4, c.getElementX());
    Assert.assertEquals(4, c.getElementY());
    Assert.assertEquals((4 * width * 2) + (4 * 2), c.getByteOffset());

    c.next();
    Assert.assertEquals(5, c.getElementX());
    Assert.assertEquals(4, c.getElementY());
    Assert.assertEquals((4 * width * 2) + (5 * 2), c.getByteOffset());

    c.next();
    Assert.assertEquals(6, c.getElementX());
    Assert.assertEquals(4, c.getElementY());
    Assert.assertEquals((4 * width * 2) + (6 * 2), c.getByteOffset());

    c.next();
    Assert.assertEquals(7, c.getElementX());
    Assert.assertEquals(4, c.getElementY());
    Assert.assertEquals((4 * width * 2) + (7 * 2), c.getByteOffset());

    c.next();
    Assert.assertEquals(4, c.getElementX());
    Assert.assertEquals(5, c.getElementY());
    Assert.assertEquals((5 * width * 2) + (4 * 2), c.getByteOffset());

    c.next();
    Assert.assertEquals(5, c.getElementX());
    Assert.assertEquals(5, c.getElementY());
    Assert.assertEquals((5 * width * 2) + (5 * 2), c.getByteOffset());

    c.next();
    Assert.assertEquals(6, c.getElementX());
    Assert.assertEquals(5, c.getElementY());
    Assert.assertEquals((5 * width * 2) + (6 * 2), c.getByteOffset());

    c.next();
    Assert.assertEquals(7, c.getElementX());
    Assert.assertEquals(5, c.getElementY());
    Assert.assertEquals((5 * width * 2) + (7 * 2), c.getByteOffset());

    c.next();
    Assert.assertEquals(4, c.getElementX());
    Assert.assertEquals(6, c.getElementY());
    Assert.assertEquals((6 * width * 2) + (4 * 2), c.getByteOffset());

    c.next();
    Assert.assertEquals(5, c.getElementX());
    Assert.assertEquals(6, c.getElementY());
    Assert.assertEquals((6 * width * 2) + (5 * 2), c.getByteOffset());

    c.next();
    Assert.assertEquals(6, c.getElementX());
    Assert.assertEquals(6, c.getElementY());
    Assert.assertEquals((6 * width * 2) + (6 * 2), c.getByteOffset());

    c.next();
    Assert.assertEquals(7, c.getElementX());
    Assert.assertEquals(6, c.getElementY());
    Assert.assertEquals((6 * width * 2) + (7 * 2), c.getByteOffset());

    c.next();
    Assert.assertEquals(4, c.getElementX());
    Assert.assertEquals(7, c.getElementY());
    Assert.assertEquals((7 * width * 2) + (4 * 2), c.getByteOffset());

    c.next();
    Assert.assertEquals(5, c.getElementX());
    Assert.assertEquals(7, c.getElementY());
    Assert.assertEquals((7 * width * 2) + (5 * 2), c.getByteOffset());

    c.next();
    Assert.assertEquals(6, c.getElementX());
    Assert.assertEquals(7, c.getElementY());
    Assert.assertEquals((7 * width * 2) + (6 * 2), c.getByteOffset());

    c.next();
    Assert.assertEquals(7, c.getElementX());
    Assert.assertEquals(7, c.getElementY());
    Assert.assertEquals((7 * width * 2) + (7 * 2), c.getByteOffset());

    c.next();
    Assert.assertFalse(c.isValid());
  }

  @SuppressWarnings("static-method") @Test public void testOneByte()
    throws ConstraintError
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusive(0, 3), new RangeInclusive(0, 1));
    final AreaCursor c = new AreaCursor(area, area, 1);

    Assert.assertTrue(c.isValid());
    Assert.assertEquals(0, c.getByteOffset());

    for (long y = 0; y <= 1; ++y) {
      for (long x = 0; x <= 3; ++x) {
        Assert.assertEquals(x, c.getElementX());
        Assert.assertEquals(y, c.getElementY());
        Assert.assertEquals((y * 4) + x, c.getByteOffset());
        c.next();
      }
    }

    Assert.assertFalse(c.isValid());
  }

  @SuppressWarnings("static-method") @Test public void testSeek()
    throws ConstraintError
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusive(0, 3), new RangeInclusive(0, 1));
    final AreaCursor c = new AreaCursor(area, area, 4);

    Assert.assertTrue(c.isValid());
    Assert.assertEquals(0, c.getByteOffset());

    for (long y = 0; y <= 1; ++y) {
      for (long x = 0; x <= 3; ++x) {
        c.seekTo(x, y);
        Assert.assertEquals(x, c.getElementX());
        Assert.assertEquals(y, c.getElementY());
        Assert.assertEquals((y * (4 * 4)) + (x * 4), c.getByteOffset());
      }
    }
  }

  @SuppressWarnings("static-method") @Test public void testSeekXOver()
    throws ConstraintError
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusive(0, 3), new RangeInclusive(0, 1));
    final AreaCursor c = new AreaCursor(area, area, 4);

    Assert.assertTrue(c.isValid());
    Assert.assertEquals(0, c.getByteOffset());

    c.seekTo(4, 0);

    Assert.assertFalse(c.isValid());
  }

  @SuppressWarnings("static-method") @Test public void testSeekXUnder()
    throws ConstraintError
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusive(0, 3), new RangeInclusive(0, 1));
    final AreaCursor c = new AreaCursor(area, area, 4);

    Assert.assertTrue(c.isValid());
    Assert.assertEquals(0, c.getByteOffset());

    c.seekTo(-1, 0);

    Assert.assertFalse(c.isValid());
  }

  @SuppressWarnings("static-method") @Test public void testSeekYOver()
    throws ConstraintError
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusive(0, 3), new RangeInclusive(0, 1));
    final AreaCursor c = new AreaCursor(area, area, 4);

    Assert.assertTrue(c.isValid());
    Assert.assertEquals(0, c.getByteOffset());

    c.seekTo(0, 4);

    Assert.assertFalse(c.isValid());
  }

  @SuppressWarnings("static-method") @Test public void testSeekYUnder()
    throws ConstraintError
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusive(0, 3), new RangeInclusive(0, 1));
    final AreaCursor c = new AreaCursor(area, area, 4);

    Assert.assertTrue(c.isValid());
    Assert.assertEquals(0, c.getByteOffset());

    c.seekTo(0, -1);

    Assert.assertFalse(c.isValid());
  }

  @SuppressWarnings("static-method") @Test public void testThreeByte()
    throws ConstraintError
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusive(0, 3), new RangeInclusive(0, 1));
    final AreaCursor c = new AreaCursor(area, area, 3);

    Assert.assertTrue(c.isValid());
    Assert.assertEquals(0, c.getByteOffset());

    for (long y = 0; y <= 1; ++y) {
      for (long x = 0; x <= 3; ++x) {
        Assert.assertEquals(x, c.getElementX());
        Assert.assertEquals(y, c.getElementY());
        Assert.assertEquals((y * (4 * 3)) + (x * 3), c.getByteOffset());
        c.next();
      }
    }

    Assert.assertFalse(c.isValid());
  }

  @SuppressWarnings("static-method") @Test public void testToString()
    throws ConstraintError
  {
    final AreaInclusive area0 =
      new AreaInclusive(new RangeInclusive(0, 3), new RangeInclusive(0, 1));
    final AreaInclusive area1 =
      new AreaInclusive(new RangeInclusive(0, 4), new RangeInclusive(0, 1));
    final AreaInclusive area2 =
      new AreaInclusive(new RangeInclusive(0, 4), new RangeInclusive(0, 1));
    final AreaInclusive area3 =
      new AreaInclusive(new RangeInclusive(0, 4), new RangeInclusive(0, 2));

    final AreaCursor c0 = new AreaCursor(area0, area0, 2);
    final AreaCursor c1 = new AreaCursor(area1, area0, 2);
    final AreaCursor c2 = new AreaCursor(area2, area0, 2);
    final AreaCursor c3 = new AreaCursor(area3, area2, 2);
    final AreaCursor c4 = new AreaCursor(area3, area2, 2);
    final AreaCursor c5 = new AreaCursor(area3, area2, 3);

    Assert.assertTrue(c0.toString().equals(c0.toString()));
    Assert.assertFalse(c0.toString().equals(c1.toString()));
    Assert.assertTrue(c1.toString().equals(c2.toString()));
    Assert.assertFalse(c2.toString().equals(c3.toString()));

    c4.seekTo(1, 0);
    Assert.assertFalse(c3.toString().equals(c4.toString()));
    Assert.assertFalse(c3.toString().equals(c5.toString()));
  }

  @SuppressWarnings("static-method") @Test public void testTwoByte()
    throws ConstraintError
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusive(0, 3), new RangeInclusive(0, 1));
    final AreaCursor c = new AreaCursor(area, area, 2);

    Assert.assertTrue(c.isValid());
    Assert.assertEquals(0, c.getByteOffset());

    for (long y = 0; y <= 1; ++y) {
      for (long x = 0; x <= 3; ++x) {
        Assert.assertEquals(x, c.getElementX());
        Assert.assertEquals(y, c.getElementY());
        Assert.assertEquals((y * (4 * 2)) + (x * 2), c.getByteOffset());
        c.next();
      }
    }

    Assert.assertFalse(c.isValid());
  }

}
