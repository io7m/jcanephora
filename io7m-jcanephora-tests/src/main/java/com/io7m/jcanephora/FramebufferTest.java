package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;

public final class FramebufferTest
{
  @SuppressWarnings("static-method") @Test public void testEquals()
    throws ConstraintError
  {
    final FramebufferReference fr0 = new FramebufferReference(1);
    final FramebufferReference fr4 = new FramebufferReference(4);
    final Framebuffer f0 = new Framebuffer(fr0, 128, 128);
    final Framebuffer f1 = new Framebuffer(fr0, 128, 128);
    final Framebuffer f2 = new Framebuffer(fr0, 127, 128);
    final Framebuffer f3 = new Framebuffer(fr0, 128, 127);
    final Framebuffer f4 = new Framebuffer(fr4, 128, 128);

    Assert.assertTrue(f0.equals(f0));
    Assert.assertTrue(f0.equals(f1));
    Assert.assertTrue(f0.equals(f2));
    Assert.assertTrue(f0.equals(f3));
    Assert.assertFalse(f0.equals(f4));
    Assert.assertFalse(f0.equals(null));
    Assert.assertFalse(f0.equals(Integer.valueOf(23)));
  }

  @SuppressWarnings("static-method") @Test public void testHashCode()
    throws ConstraintError
  {
    final FramebufferReference fr0 = new FramebufferReference(1);
    final FramebufferReference fr4 = new FramebufferReference(4);
    final Framebuffer f0 = new Framebuffer(fr0, 128, 128);
    final Framebuffer f1 = new Framebuffer(fr0, 128, 128);
    final Framebuffer f2 = new Framebuffer(fr0, 127, 128);
    final Framebuffer f3 = new Framebuffer(fr0, 128, 127);
    final Framebuffer f4 = new Framebuffer(fr4, 128, 128);

    Assert.assertTrue(f0.hashCode() == f1.hashCode());
    Assert.assertTrue(f0.hashCode() == f2.hashCode());
    Assert.assertTrue(f0.hashCode() == f3.hashCode());
    Assert.assertTrue(f0.hashCode() != f4.hashCode());
  }

  @SuppressWarnings("static-method") @Test public void testIdentities()
    throws ConstraintError
  {
    final FramebufferReference fr0 = new FramebufferReference(1);
    final Framebuffer f0 = new Framebuffer(fr0, 127, 128);

    Assert.assertEquals(f0.getWidth(), 127);
    Assert.assertEquals(f0.getHeight(), 128);
  }

  @SuppressWarnings("static-method") @Test public void testToString()
    throws ConstraintError
  {
    final FramebufferReference fr0 = new FramebufferReference(1);
    final FramebufferReference fr4 = new FramebufferReference(4);
    final Framebuffer f0 = new Framebuffer(fr0, 128, 128);
    final Framebuffer f1 = new Framebuffer(fr0, 128, 128);
    final Framebuffer f2 = new Framebuffer(fr0, 127, 128);
    final Framebuffer f3 = new Framebuffer(fr0, 128, 127);
    final Framebuffer f4 = new Framebuffer(fr4, 128, 128);

    Assert.assertTrue(f0.toString().equals(f0.toString()));
    Assert.assertTrue(f0.toString().equals(f1.toString()));
    Assert.assertFalse(f0.toString().equals(f2.toString()));
    Assert.assertFalse(f0.toString().equals(f3.toString()));
    Assert.assertFalse(f0.toString().equals(f4.toString()));
  }
}