package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

public class FramebufferColorAttachmentPointTest
{
  @SuppressWarnings("static-method") @Test public void testCompare()
  {
    final FramebufferColorAttachmentPoint ap0 =
      new FramebufferColorAttachmentPoint(0);
    final FramebufferColorAttachmentPoint ap1 =
      new FramebufferColorAttachmentPoint(1);

    Assert.assertEquals(0, ap0.compareTo(ap0));
    Assert.assertEquals(-1, ap0.compareTo(ap1));
    Assert.assertEquals(1, ap1.compareTo(ap0));
  }

  @SuppressWarnings("static-method") @Test public void testEquals()
  {
    final FramebufferColorAttachmentPoint ap0 =
      new FramebufferColorAttachmentPoint(1);
    final FramebufferColorAttachmentPoint ap1 =
      new FramebufferColorAttachmentPoint(2);
    final FramebufferColorAttachmentPoint ap2 =
      new FramebufferColorAttachmentPoint(1);

    Assert.assertEquals(ap0, ap0);
    Assert.assertEquals(ap0, ap2);
    Assert.assertEquals(ap2, ap0);
    Assert.assertFalse(ap0.equals(ap1));
    Assert.assertFalse(ap0.equals(null));
    Assert.assertFalse(ap0.equals(Integer.valueOf(23)));
  }

  @SuppressWarnings("static-method") @Test public void testHashCode()
  {
    final FramebufferColorAttachmentPoint ap0 =
      new FramebufferColorAttachmentPoint(1);
    final FramebufferColorAttachmentPoint ap1 =
      new FramebufferColorAttachmentPoint(2);

    Assert.assertTrue(ap0.hashCode() == ap0.hashCode());
    Assert.assertTrue(ap0.hashCode() != ap1.hashCode());
  }

  @SuppressWarnings("static-method") @Test public void testIdentities()
  {
    final FramebufferColorAttachmentPoint ap0 =
      new FramebufferColorAttachmentPoint(1);

    Assert.assertEquals(1, ap0.getIndex());
  }

  @SuppressWarnings("static-method") @Test public void testToString()
  {
    final FramebufferColorAttachmentPoint ap0 =
      new FramebufferColorAttachmentPoint(1);
    final FramebufferColorAttachmentPoint ap1 =
      new FramebufferColorAttachmentPoint(2);
    final FramebufferColorAttachmentPoint ap2 =
      new FramebufferColorAttachmentPoint(1);

    Assert.assertEquals(ap0.toString(), ap0.toString());
    Assert.assertEquals(ap0.toString(), ap2.toString());
    Assert.assertEquals(ap2.toString(), ap0.toString());
    Assert.assertFalse(ap0.toString().equals(ap1.toString()));
    Assert.assertFalse(ap0.toString().equals(null));
    Assert.assertFalse(ap0.toString().equals(Integer.valueOf(23)));
  }
}
