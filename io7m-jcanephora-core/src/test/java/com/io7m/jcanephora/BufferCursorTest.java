package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;

public class BufferCursorTest
{
  @SuppressWarnings("static-method") @Test public void testHasNextBeyond()
  {
    final BufferCursor c = new BufferCursor(0, 0, 0, 4);
    Assert.assertFalse(c.hasNext());
  }

  @SuppressWarnings("static-method") @Test public void testInit()
  {
    final BufferCursor c = new BufferCursor(0, 0, 7, 4);

    Assert.assertEquals(0, c.getByteOffset());
    Assert.assertTrue(c.hasNext());
  }

  @SuppressWarnings("static-method") @Test public void testNext()
    throws ConstraintError
  {
    final BufferCursor c = new BufferCursor(0, 0, 7, 4);

    for (int index = 0; index < 7; ++index) {
      Assert.assertTrue(c.hasNext());
      c.next();
    }
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testNextBeyond()
      throws ConstraintError
  {
    final BufferCursor c = new BufferCursor(0, 0, 0, 4);
    c.next();
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
