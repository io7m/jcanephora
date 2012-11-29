package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;

public class RenderbufferD24S8Test
{
  @SuppressWarnings("static-method") @Test public void testEquals()
    throws ConstraintError
  {
    final RenderbufferD24S8 t0 = new RenderbufferD24S8(1, 128, 256);
    final RenderbufferD24S8 t1 = new RenderbufferD24S8(2, 128, 256);
    final RenderbufferD24S8 t2 = new RenderbufferD24S8(1, 128, 256);

    Assert.assertEquals(t0, t0);
    Assert.assertEquals(t0, t2);
    Assert.assertEquals(t2, t0);
    Assert.assertFalse(t0.equals(t1));
    Assert.assertFalse(t0.equals(null));
    Assert.assertFalse(t0.equals(Integer.valueOf(23)));
  }

  @SuppressWarnings("static-method") @Test public void testHashCode()
    throws ConstraintError
  {
    final RenderbufferD24S8 t0 = new RenderbufferD24S8(1, 128, 256);
    final RenderbufferD24S8 t1 = new RenderbufferD24S8(2, 128, 256);
    final RenderbufferD24S8 t2 = new RenderbufferD24S8(1, 128, 256);
    final RenderbufferD24S8 t3 = new RenderbufferD24S8(2, 128, 256);
    final RenderbufferD24S8 t4 = new RenderbufferD24S8(1, 128, 256);

    Assert.assertTrue(t0.hashCode() == t0.hashCode());
    Assert.assertTrue(t0.hashCode() == t2.hashCode());
    Assert.assertTrue(t2.hashCode() == t0.hashCode());

    Assert.assertTrue(t0.hashCode() != t1.hashCode());
    Assert.assertTrue(t0.hashCode() != t3.hashCode());
    Assert.assertTrue(t0.hashCode() == t4.hashCode());

    Assert.assertTrue(t3.hashCode() != t4.hashCode());
  }

  @SuppressWarnings("static-method") @Test public void testIdentities()
    throws ConstraintError
  {
    final RenderbufferD24S8 t0 = new RenderbufferD24S8(1, 128, 256);

    Assert.assertEquals(1, t0.getGLName());
    Assert.assertEquals(128, t0.getWidth());
    Assert.assertEquals(256, t0.getHeight());
  }

  @SuppressWarnings("static-method") @Test public void testToString()
    throws ConstraintError
  {
    final RenderbufferD24S8 t0 = new RenderbufferD24S8(1, 128, 256);
    final RenderbufferD24S8 t1 = new RenderbufferD24S8(2, 128, 256);
    final RenderbufferD24S8 t2 = new RenderbufferD24S8(1, 128, 256);

    Assert.assertEquals(t0.toString(), t0.toString());
    Assert.assertEquals(t0.toString(), t2.toString());
    Assert.assertEquals(t2.toString(), t0.toString());
    Assert.assertFalse(t0.toString().equals(t1.toString()));
    Assert.assertFalse(t0.toString().equals(null));
    Assert.assertFalse(t0.toString().equals(Integer.valueOf(23)));
  }
}