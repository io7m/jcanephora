package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.AttachmentStencil.AttachmentSharedStencilRenderbuffer;
import com.io7m.jcanephora.AttachmentStencil.AttachmentStencilAsDepthStencil;
import com.io7m.jcanephora.AttachmentStencil.AttachmentStencilRenderbuffer;

public final class AttachmentStencilTest
{
  @SuppressWarnings("static-method") @Test public
    void
    testMetaSharedStencilRenderbuffer()
      throws ConstraintError
  {
    final Renderbuffer rb1 =
      new Renderbuffer(RenderbufferType.RENDERBUFFER_DEPTH_16, 1, 128, 128);
    final Renderbuffer rb2 =
      new Renderbuffer(RenderbufferType.RENDERBUFFER_DEPTH_16, 2, 128, 128);

    final AttachmentSharedStencilRenderbuffer a0 =
      new AttachmentSharedStencilRenderbuffer(rb1);
    final AttachmentSharedStencilRenderbuffer a1 =
      new AttachmentSharedStencilRenderbuffer(rb1);
    final AttachmentSharedStencilRenderbuffer a2 =
      new AttachmentSharedStencilRenderbuffer(rb2);

    Assert.assertTrue(a0.equals(a0));
    Assert.assertTrue(a0.equals(a1));
    Assert.assertFalse(a0.equals(null));
    Assert.assertFalse(a0.equals(Integer.valueOf(23)));
    Assert.assertFalse(a0.equals(a2));

    Assert.assertTrue(a0.hashCode() == a1.hashCode());
    Assert.assertFalse(a0.hashCode() == a2.hashCode());

    Assert.assertTrue(a0.toString().equals(a1.toString()));
    Assert.assertFalse(a0.toString().equals(a2.toString()));

    Assert.assertTrue(a0.getRenderbuffer() == rb1);
    Assert.assertTrue(a1.getRenderbuffer() == rb1);
    Assert.assertTrue(a2.getRenderbuffer() == rb2);
  }

  @SuppressWarnings("static-method") @Test public
    void
    testMetaStencilAsDepthStencil()
  {
    final AttachmentStencilAsDepthStencil a0 =
      new AttachmentStencilAsDepthStencil();
    final AttachmentStencilAsDepthStencil a1 =
      new AttachmentStencilAsDepthStencil();

    Assert.assertTrue(a0.equals(a0));
    Assert.assertTrue(a0.equals(a1));
    Assert.assertFalse(a0.equals(null));
    Assert.assertFalse(a0.equals(Integer.valueOf(23)));

    Assert.assertTrue(a0.hashCode() == a1.hashCode());

    Assert.assertTrue(a0.toString().equals(a1.toString()));
  }

  @SuppressWarnings("static-method") @Test public
    void
    testMetaStencilRenderbuffer()
      throws ConstraintError
  {
    final Renderbuffer rb1 =
      new Renderbuffer(RenderbufferType.RENDERBUFFER_DEPTH_16, 1, 128, 128);
    final Renderbuffer rb2 =
      new Renderbuffer(RenderbufferType.RENDERBUFFER_DEPTH_16, 2, 128, 128);

    final AttachmentStencilRenderbuffer a0 =
      new AttachmentStencilRenderbuffer(rb1);
    final AttachmentStencilRenderbuffer a1 =
      new AttachmentStencilRenderbuffer(rb1);
    final AttachmentStencilRenderbuffer a2 =
      new AttachmentStencilRenderbuffer(rb2);

    Assert.assertTrue(a0.equals(a0));
    Assert.assertTrue(a0.equals(a1));
    Assert.assertFalse(a0.equals(null));
    Assert.assertFalse(a0.equals(Integer.valueOf(23)));
    Assert.assertFalse(a0.equals(a2));

    Assert.assertTrue(a0.hashCode() == a1.hashCode());
    Assert.assertFalse(a0.hashCode() == a2.hashCode());

    Assert.assertTrue(a0.toString().equals(a1.toString()));
    Assert.assertFalse(a0.toString().equals(a2.toString()));

    Assert.assertTrue(a0.getRenderbuffer() == rb1);
    Assert.assertTrue(a1.getRenderbuffer() == rb1);
    Assert.assertTrue(a2.getRenderbuffer() == rb2);

    Assert.assertTrue(a0.getRenderbufferWritable() == rb1);
    Assert.assertTrue(a1.getRenderbufferWritable() == rb1);
    Assert.assertTrue(a2.getRenderbufferWritable() == rb2);
  }
}
