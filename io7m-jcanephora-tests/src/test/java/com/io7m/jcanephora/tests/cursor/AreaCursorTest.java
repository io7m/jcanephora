/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.tests.cursor;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jcanephora.AreaInclusive;
import com.io7m.jcanephora.cursors.AreaCursor;
import com.io7m.jranges.RangeCheckException;
import com.io7m.jranges.RangeInclusiveL;

@SuppressWarnings({ "static-method", "unused" }) public class AreaCursorTest
{
  private static final class TestCursor extends AreaCursor
  {
    protected TestCursor(
      final AreaInclusive in_area_outer,
      final AreaInclusive in_area_inner,
      final long in_element_bytes)
    {
      super(in_area_outer, in_area_inner, in_element_bytes);
    }
  }

  @Test public void testEquals()
  {
    final AreaInclusive area0 =
      new AreaInclusive(new RangeInclusiveL(0, 3), new RangeInclusiveL(0, 1));
    final AreaInclusive area1 =
      new AreaInclusive(new RangeInclusiveL(0, 4), new RangeInclusiveL(0, 1));
    final AreaInclusive area2 =
      new AreaInclusive(new RangeInclusiveL(0, 4), new RangeInclusiveL(0, 1));
    final AreaInclusive area3 =
      new AreaInclusive(new RangeInclusiveL(0, 4), new RangeInclusiveL(0, 2));

    final AreaCursor c0 = new TestCursor(area0, area0, 2);
    final AreaCursor c1 = new TestCursor(area1, area0, 2);
    final AreaCursor c2 = new TestCursor(area2, area0, 2);
    final AreaCursor c3 = new TestCursor(area3, area2, 2);
    final AreaCursor c4 = new TestCursor(area3, area2, 2);
    final AreaCursor c5 = new TestCursor(area3, area2, 3);

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

  @Test public void testFourByte()
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusiveL(0, 3), new RangeInclusiveL(0, 1));
    final AreaCursor c = new TestCursor(area, area, 4);

    Assert.assertTrue(c.isValid());
    Assert.assertEquals(0, c.getByteOffset());
    Assert.assertEquals(4, c.getElementBytes());

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

  @Test public void testHashcode()
  {
    final AreaInclusive area0 =
      new AreaInclusive(new RangeInclusiveL(0, 3), new RangeInclusiveL(0, 1));
    final AreaInclusive area1 =
      new AreaInclusive(new RangeInclusiveL(0, 4), new RangeInclusiveL(0, 1));
    final AreaInclusive area2 =
      new AreaInclusive(new RangeInclusiveL(0, 4), new RangeInclusiveL(0, 1));
    final AreaInclusive area3 =
      new AreaInclusive(new RangeInclusiveL(0, 4), new RangeInclusiveL(0, 2));

    final AreaCursor c0 = new TestCursor(area0, area0, 2);
    final AreaCursor c1 = new TestCursor(area1, area0, 2);
    final AreaCursor c2 = new TestCursor(area2, area0, 2);
    final AreaCursor c3 = new TestCursor(area3, area2, 2);
    final AreaCursor c4 = new TestCursor(area3, area2, 2);
    final AreaCursor c5 = new TestCursor(area3, area2, 3);

    Assert.assertTrue(c0.hashCode() == (c0.hashCode()));
    Assert.assertFalse(c0.hashCode() == (c1.hashCode()));
    Assert.assertTrue(c1.hashCode() == (c2.hashCode()));
    Assert.assertFalse(c2.hashCode() == (c3.hashCode()));

    c4.seekTo(1, 0);
    Assert.assertFalse(c3.hashCode() == (c4.hashCode()));
    Assert.assertFalse(c3.hashCode() == (c5.hashCode()));
  }

  @Test(expected = RangeCheckException.class) public
    void
    testIllegalLowBytes()
  {
    final AreaInclusive area_outer =
      new AreaInclusive(new RangeInclusiveL(0, 6), new RangeInclusiveL(0, 6));
    final AreaInclusive area_inner =
      new AreaInclusive(new RangeInclusiveL(0, 6), new RangeInclusiveL(0, 6));
    new TestCursor(area_outer, area_inner, -1);
  }

  @Test(expected = RangeCheckException.class) public void testIllegalLowX()
  {
    final AreaInclusive area_outer =
      new AreaInclusive(new RangeInclusiveL(-1, 6), new RangeInclusiveL(4, 6));
    final AreaInclusive area_inner =
      new AreaInclusive(new RangeInclusiveL(-1, 6), new RangeInclusiveL(4, 6));
    new TestCursor(area_outer, area_inner, 4);
  }

  @Test(expected = RangeCheckException.class) public void testIllegalLowY()
  {
    final AreaInclusive area_outer =
      new AreaInclusive(new RangeInclusiveL(1, 6), new RangeInclusiveL(-1, 6));
    final AreaInclusive area_inner =
      new AreaInclusive(new RangeInclusiveL(1, 6), new RangeInclusiveL(-1, 6));
    new TestCursor(area_outer, area_inner, 4);
  }

  @Test(expected = RangeCheckException.class) public
    void
    testIllegalNotContained()
  {
    final AreaInclusive area_outer =
      new AreaInclusive(new RangeInclusiveL(0, 9), new RangeInclusiveL(0, 9));
    final AreaInclusive area_inner =
      new AreaInclusive(
        new RangeInclusiveL(0, 20),
        new RangeInclusiveL(0, 20));
    new TestCursor(area_outer, area_inner, 4);
  }

  @Test public void testNonzeroAreaFourBytes()
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusiveL(2, 6), new RangeInclusiveL(4, 6));
    final AreaCursor c = new TestCursor(area, area, 4);
    final long width = area.getRangeX().getInterval();

    Assert.assertEquals(2, c.getElementX());
    Assert.assertEquals(4, c.getElementY());
    Assert.assertEquals((4 * width * 4) + (2 * 4), c.getByteOffset());
  }

  @Test public void testNonzeroAreaOneByte()
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusiveL(2, 6), new RangeInclusiveL(4, 6));
    final AreaCursor c = new TestCursor(area, area, 1);
    final long width = area.getRangeX().getInterval();

    Assert.assertEquals(2, c.getElementX());
    Assert.assertEquals(4, c.getElementY());
    Assert.assertEquals((4 * width) + 2, c.getByteOffset());
  }

  @Test public void testNonzeroAreaThreeBytes()
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusiveL(2, 6), new RangeInclusiveL(4, 6));
    final AreaCursor c = new TestCursor(area, area, 3);
    final long width = area.getRangeX().getInterval();

    Assert.assertEquals(2, c.getElementX());
    Assert.assertEquals(4, c.getElementY());
    Assert.assertEquals((4 * width * 3) + (2 * 3), c.getByteOffset());
  }

  @Test public void testNonzeroAreaTwoBytes()
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusiveL(2, 6), new RangeInclusiveL(4, 6));
    final AreaCursor c = new TestCursor(area, area, 2);
    final long width = area.getRangeX().getInterval();

    Assert.assertEquals(2, c.getElementX());
    Assert.assertEquals(4, c.getElementY());
    Assert.assertEquals((4 * width * 2) + (2 * 2), c.getByteOffset());
  }

  @Test public void testNonzeroAreaTwoBytesAgain()
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusiveL(4, 7), new RangeInclusiveL(4, 7));
    final AreaCursor c = new TestCursor(area, area, 2);
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

  @Test public void testOneByte()
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusiveL(0, 3), new RangeInclusiveL(0, 1));
    final AreaCursor c = new TestCursor(area, area, 1);

    Assert.assertTrue(c.isValid());
    Assert.assertEquals(0, c.getByteOffset());
    Assert.assertEquals(1, c.getElementBytes());

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

  @Test public void testSeek()
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusiveL(0, 3), new RangeInclusiveL(0, 1));
    final AreaCursor c = new TestCursor(area, area, 4);

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

  @Test public void testSeekXOver()
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusiveL(0, 3), new RangeInclusiveL(0, 1));
    final AreaCursor c = new TestCursor(area, area, 4);

    Assert.assertTrue(c.isValid());
    Assert.assertEquals(0, c.getByteOffset());

    c.seekTo(4, 0);

    Assert.assertFalse(c.isValid());
  }

  @Test public void testSeekXUnder()
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusiveL(0, 3), new RangeInclusiveL(0, 1));
    final AreaCursor c = new TestCursor(area, area, 4);

    Assert.assertTrue(c.isValid());
    Assert.assertEquals(0, c.getByteOffset());

    c.seekTo(-1, 0);

    Assert.assertFalse(c.isValid());
  }

  @Test public void testSeekYOver()
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusiveL(0, 3), new RangeInclusiveL(0, 1));
    final AreaCursor c = new TestCursor(area, area, 4);

    Assert.assertTrue(c.isValid());
    Assert.assertEquals(0, c.getByteOffset());

    c.seekTo(0, 4);

    Assert.assertFalse(c.isValid());
  }

  @Test public void testSeekYUnder()
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusiveL(0, 3), new RangeInclusiveL(0, 1));
    final AreaCursor c = new TestCursor(area, area, 4);

    Assert.assertTrue(c.isValid());
    Assert.assertEquals(0, c.getByteOffset());

    c.seekTo(0, -1);

    Assert.assertFalse(c.isValid());
  }

  @Test public void testThreeByte()
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusiveL(0, 3), new RangeInclusiveL(0, 1));
    final AreaCursor c = new TestCursor(area, area, 3);

    Assert.assertTrue(c.isValid());
    Assert.assertEquals(0, c.getByteOffset());
    Assert.assertEquals(3, c.getElementBytes());

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

  @Test public void testToString()
  {
    final AreaInclusive area0 =
      new AreaInclusive(new RangeInclusiveL(0, 3), new RangeInclusiveL(0, 1));
    final AreaInclusive area1 =
      new AreaInclusive(new RangeInclusiveL(0, 4), new RangeInclusiveL(0, 1));
    final AreaInclusive area2 =
      new AreaInclusive(new RangeInclusiveL(0, 4), new RangeInclusiveL(0, 1));
    final AreaInclusive area3 =
      new AreaInclusive(new RangeInclusiveL(0, 4), new RangeInclusiveL(0, 2));

    final AreaCursor c0 = new TestCursor(area0, area0, 2);
    final AreaCursor c1 = new TestCursor(area1, area0, 2);
    final AreaCursor c2 = new TestCursor(area2, area0, 2);
    final AreaCursor c3 = new TestCursor(area3, area2, 2);
    final AreaCursor c4 = new TestCursor(area3, area2, 2);
    final AreaCursor c5 = new TestCursor(area3, area2, 3);

    Assert.assertTrue(c0.toString().equals(c0.toString()));
    Assert.assertFalse(c0.toString().equals(c1.toString()));
    Assert.assertTrue(c1.toString().equals(c2.toString()));
    Assert.assertFalse(c2.toString().equals(c3.toString()));

    c4.seekTo(1, 0);
    Assert.assertFalse(c3.toString().equals(c4.toString()));
    Assert.assertFalse(c3.toString().equals(c5.toString()));
  }

  @Test public void testTwoByte()
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusiveL(0, 3), new RangeInclusiveL(0, 1));
    final AreaCursor c = new TestCursor(area, area, 2);

    Assert.assertTrue(c.isValid());
    Assert.assertEquals(0, c.getByteOffset());
    Assert.assertEquals(2, c.getElementBytes());

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
