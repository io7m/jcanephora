package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;

public class BufferCursorTest
{
  @SuppressWarnings({ "static-method", "unused" }) @Test(
    expected = IllegalArgumentException.class) public
    void
    testPrecondition0()
  {
    new BufferCursor(0, -1, 3, 4);
  }

  @SuppressWarnings({ "static-method", "unused" }) @Test(
    expected = IllegalArgumentException.class) public
    void
    testPrecondition1()
  {
    new BufferCursor(0, 0, -1, 4);
  }

  @SuppressWarnings({ "static-method", "unused" }) @Test(
    expected = IllegalArgumentException.class) public
    void
    testPrecondition2()
  {
    new BufferCursor(0, 1, 0, 4);
  }

  @SuppressWarnings({ "static-method", "unused" }) @Test(
    expected = IllegalArgumentException.class) public
    void
    testPrecondition3()
  {
    new BufferCursor(-1, 0, 3, 4);
  }

  @SuppressWarnings({ "static-method", "unused" }) @Test(
    expected = IllegalArgumentException.class) public
    void
    testPrecondition4()
  {
    new BufferCursor(0, 0, 3, -1);
  }

  @SuppressWarnings({ "static-method", "unused" }) @Test(
    expected = IllegalArgumentException.class) public
    void
    testPrecondition5()
  {
    new BufferCursor(4, 0, 3, 4);
  }

  @SuppressWarnings("static-method") @Test public void testCanWrite()
    throws ConstraintError
  {
    final BufferCursor c = new BufferCursor(0, 0, 3, 4);

    Assert.assertEquals(0, c.getByteOffset());
    Assert.assertTrue(c.canWrite());
    c.next();

    Assert.assertEquals(4, c.getByteOffset());
    Assert.assertTrue(c.canWrite());
    c.next();

    Assert.assertEquals(8, c.getByteOffset());
    Assert.assertTrue(c.canWrite());
    c.next();

    Assert.assertEquals(12, c.getByteOffset());
    Assert.assertTrue(c.canWrite());
    Assert.assertFalse(c.hasNext());
  }

  @SuppressWarnings("static-method") @Test public void testCanWritePre()
    throws ConstraintError
  {
    final BufferCursor c = new BufferCursor(0, 2, 3, 4);

    Assert.assertEquals(8, c.getByteOffset());
    Assert.assertTrue(c.canWrite());
    c.next();

    Assert.assertEquals(12, c.getByteOffset());
    Assert.assertTrue(c.canWrite());
    Assert.assertFalse(c.hasNext());

    c.seekTo(0);
    Assert.assertTrue(c.hasNext());
    Assert.assertFalse(c.canWrite());

    c.seekTo(1);
    Assert.assertTrue(c.hasNext());
    Assert.assertFalse(c.canWrite());

    c.seekTo(2);
    Assert.assertTrue(c.hasNext());
    Assert.assertTrue(c.canWrite());

    c.seekTo(3);
    Assert.assertFalse(c.hasNext());
    Assert.assertTrue(c.canWrite());
  }

  @SuppressWarnings("static-method") @Test public void testHasNextBeyond()
  {
    final BufferCursor c = new BufferCursor(0, 0, 0, 4);
    Assert.assertFalse(c.hasNext());
  }

  @SuppressWarnings("static-method") @Test public void testInit0()
    throws ConstraintError
  {
    final BufferCursor c = new BufferCursor(0, 0, 7, 4);

    Assert.assertEquals(0, c.getByteOffset());
    Assert.assertEquals(0, c.getElement());
    Assert.assertTrue(c.hasNext());
    Assert.assertTrue(c.canWrite());
  }

  @SuppressWarnings("static-method") @Test public void testInit1()
    throws ConstraintError
  {
    final BufferCursor c = new BufferCursor(0, 1, 7, 4);

    Assert.assertEquals(4, c.getByteOffset());
    Assert.assertEquals(1, c.getElement());
    Assert.assertTrue(c.hasNext());
    Assert.assertTrue(c.canWrite());
  }

  @SuppressWarnings("static-method") @Test public void testNext()
  {
    final BufferCursor c = new BufferCursor(0, 0, 7, 4);

    for (int index = 0; index < 7; ++index) {
      Assert.assertTrue(c.hasNext());
      c.next();
    }
  }

  @SuppressWarnings("static-method") @Test public void testNextOffsets()
    throws ConstraintError
  {
    final BufferCursor c = new BufferCursor(0, 0, 3, 4);

    Assert.assertEquals(0, c.getByteOffset());
    c.next();
    Assert.assertEquals(4, c.getByteOffset());
    c.next();
    Assert.assertEquals(8, c.getByteOffset());
    c.next();
    Assert.assertEquals(12, c.getByteOffset());

    Assert.assertFalse(c.hasNext());
  }

  @SuppressWarnings("static-method") @Test public void testSeekOffsets()
    throws ConstraintError
  {
    final BufferCursor c = new BufferCursor(0, 0, 3, 4);

    c.seekTo(3);
    Assert.assertEquals(12, c.getByteOffset());

    c.seekTo(1);
    Assert.assertEquals(4, c.getByteOffset());

    c.seekTo(2);
    Assert.assertEquals(8, c.getByteOffset());

    c.seekTo(0);
    Assert.assertEquals(0, c.getByteOffset());
  }
}
