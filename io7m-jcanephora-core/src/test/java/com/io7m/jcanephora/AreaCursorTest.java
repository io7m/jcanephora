package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;

public class AreaCursorTest
{
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

  @SuppressWarnings("static-method") @Test public void testFourByte()
    throws ConstraintError
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusive(0, 3), new RangeInclusive(0, 1));
    final AreaCursor c = new AreaCursor(area, area, 4);

    Assert.assertTrue(c.canWrite());
    Assert.assertEquals(0, c.getByteOffset());

    for (long y = 0; y <= 1; ++y) {
      for (long x = 0; x <= 3; ++x) {
        Assert.assertEquals(x, c.getElementX());
        Assert.assertEquals(y, c.getElementY());
        Assert.assertEquals((y * (4 * 4)) + (x * 4), c.getByteOffset());
        c.next();
      }
    }

    Assert.assertFalse(c.canWrite());
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
    Assert.assertFalse(c.canWrite());
  }

  @SuppressWarnings("static-method") @Test public void testOneByte()
    throws ConstraintError
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusive(0, 3), new RangeInclusive(0, 1));
    final AreaCursor c = new AreaCursor(area, area, 1);

    Assert.assertTrue(c.canWrite());
    Assert.assertEquals(0, c.getByteOffset());

    for (long y = 0; y <= 1; ++y) {
      for (long x = 0; x <= 3; ++x) {
        Assert.assertEquals(x, c.getElementX());
        Assert.assertEquals(y, c.getElementY());
        Assert.assertEquals((y * 4) + x, c.getByteOffset());
        c.next();
      }
    }

    Assert.assertFalse(c.canWrite());
  }

  @SuppressWarnings("static-method") @Test public void testSeek()
    throws ConstraintError
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusive(0, 3), new RangeInclusive(0, 1));
    final AreaCursor c = new AreaCursor(area, area, 4);

    Assert.assertTrue(c.canWrite());
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

    Assert.assertTrue(c.canWrite());
    Assert.assertEquals(0, c.getByteOffset());

    c.seekTo(4, 0);

    Assert.assertFalse(c.canWrite());
  }

  @SuppressWarnings("static-method") @Test public void testSeekXUnder()
    throws ConstraintError
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusive(0, 3), new RangeInclusive(0, 1));
    final AreaCursor c = new AreaCursor(area, area, 4);

    Assert.assertTrue(c.canWrite());
    Assert.assertEquals(0, c.getByteOffset());

    c.seekTo(-1, 0);

    Assert.assertFalse(c.canWrite());
  }

  @SuppressWarnings("static-method") @Test public void testSeekYOver()
    throws ConstraintError
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusive(0, 3), new RangeInclusive(0, 1));
    final AreaCursor c = new AreaCursor(area, area, 4);

    Assert.assertTrue(c.canWrite());
    Assert.assertEquals(0, c.getByteOffset());

    c.seekTo(0, 4);

    Assert.assertFalse(c.canWrite());
  }

  @SuppressWarnings("static-method") @Test public void testSeekYUnder()
    throws ConstraintError
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusive(0, 3), new RangeInclusive(0, 1));
    final AreaCursor c = new AreaCursor(area, area, 4);

    Assert.assertTrue(c.canWrite());
    Assert.assertEquals(0, c.getByteOffset());

    c.seekTo(0, -1);

    Assert.assertFalse(c.canWrite());
  }

  @SuppressWarnings("static-method") @Test public void testThreeByte()
    throws ConstraintError
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusive(0, 3), new RangeInclusive(0, 1));
    final AreaCursor c = new AreaCursor(area, area, 3);

    Assert.assertTrue(c.canWrite());
    Assert.assertEquals(0, c.getByteOffset());

    for (long y = 0; y <= 1; ++y) {
      for (long x = 0; x <= 3; ++x) {
        Assert.assertEquals(x, c.getElementX());
        Assert.assertEquals(y, c.getElementY());
        Assert.assertEquals((y * (4 * 3)) + (x * 3), c.getByteOffset());
        c.next();
      }
    }

    Assert.assertFalse(c.canWrite());
  }

  @SuppressWarnings("static-method") @Test public void testTwoByte()
    throws ConstraintError
  {
    final AreaInclusive area =
      new AreaInclusive(new RangeInclusive(0, 3), new RangeInclusive(0, 1));
    final AreaCursor c = new AreaCursor(area, area, 2);

    Assert.assertTrue(c.canWrite());
    Assert.assertEquals(0, c.getByteOffset());

    for (long y = 0; y <= 1; ++y) {
      for (long x = 0; x <= 3; ++x) {
        Assert.assertEquals(x, c.getElementX());
        Assert.assertEquals(y, c.getElementY());
        Assert.assertEquals((y * (4 * 2)) + (x * 2), c.getByteOffset());
        c.next();
      }
    }

    Assert.assertFalse(c.canWrite());
  }
}
