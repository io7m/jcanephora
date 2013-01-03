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
