package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;

public class RenderbufferTest
{
  @SuppressWarnings("static-method") @Test public void testEquals()
    throws ConstraintError
  {
    final Renderbuffer t0 =
      new Renderbuffer(
        RenderbufferType.RENDERBUFFER_COLOR_RGB_565,
        1,
        128,
        256);
    final Renderbuffer t1 =
      new Renderbuffer(
        RenderbufferType.RENDERBUFFER_COLOR_RGB_565,
        2,
        128,
        256);
    final Renderbuffer t2 =
      new Renderbuffer(
        RenderbufferType.RENDERBUFFER_COLOR_RGB_565,
        1,
        128,
        256);

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
    final Renderbuffer t0 =
      new Renderbuffer(
        RenderbufferType.RENDERBUFFER_COLOR_RGB_565,
        1,
        128,
        256);
    final Renderbuffer t1 =
      new Renderbuffer(
        RenderbufferType.RENDERBUFFER_COLOR_RGB_565,
        2,
        128,
        256);
    final Renderbuffer t2 =
      new Renderbuffer(
        RenderbufferType.RENDERBUFFER_COLOR_RGB_565,
        1,
        128,
        256);
    final Renderbuffer t3 =
      new Renderbuffer(
        RenderbufferType.RENDERBUFFER_COLOR_RGB_565,
        2,
        128,
        256);
    final Renderbuffer t4 =
      new Renderbuffer(
        RenderbufferType.RENDERBUFFER_COLOR_RGB_565,
        1,
        128,
        256);

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
    for (final RenderbufferType t : RenderbufferType.values()) {
      final Renderbuffer r = new Renderbuffer(t, 1, 128, 256);

      Assert.assertEquals(1, r.getGLName());
      Assert.assertEquals(128, r.getWidth());
      Assert.assertEquals(256, r.getHeight());
      Assert.assertEquals(t, r.getType());
    }
  }

  @SuppressWarnings("static-method") @Test public void testToString()
    throws ConstraintError
  {
    final Renderbuffer t0 =
      new Renderbuffer(
        RenderbufferType.RENDERBUFFER_COLOR_RGB_565,
        1,
        128,
        256);
    final Renderbuffer t1 =
      new Renderbuffer(
        RenderbufferType.RENDERBUFFER_COLOR_RGB_565,
        2,
        128,
        256);
    final Renderbuffer t2 =
      new Renderbuffer(
        RenderbufferType.RENDERBUFFER_COLOR_RGB_565,
        1,
        128,
        256);

    Assert.assertEquals(t0.toString(), t0.toString());
    Assert.assertEquals(t0.toString(), t2.toString());
    Assert.assertEquals(t2.toString(), t0.toString());
    Assert.assertFalse(t0.toString().equals(t1.toString()));
    Assert.assertFalse(t0.toString().equals(null));
    Assert.assertFalse(t0.toString().equals(Integer.valueOf(23)));
  }
}
