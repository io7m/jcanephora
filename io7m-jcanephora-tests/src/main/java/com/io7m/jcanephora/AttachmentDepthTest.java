package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.AttachmentDepth.AttachmentDepthRenderbuffer;
import com.io7m.jcanephora.AttachmentDepth.AttachmentDepthStencilRenderbuffer;
import com.io7m.jcanephora.AttachmentDepth.AttachmentSharedDepthRenderbuffer;
import com.io7m.jcanephora.AttachmentDepth.AttachmentSharedDepthStencilRenderbuffer;

public final class AttachmentDepthTest
{
  @SuppressWarnings("static-method") @Test public
    void
    testMetaDepthRenderbuffer()
      throws ConstraintError
  {
    final Renderbuffer rb1 =
      new Renderbuffer(RenderbufferType.RENDERBUFFER_DEPTH_16, 1, 128, 128);
    final Renderbuffer rb2 =
      new Renderbuffer(RenderbufferType.RENDERBUFFER_DEPTH_16, 2, 128, 128);

    final AttachmentDepthRenderbuffer a0 =
      new AttachmentDepthRenderbuffer(rb1);
    final AttachmentDepthRenderbuffer a1 =
      new AttachmentDepthRenderbuffer(rb1);
    final AttachmentDepthRenderbuffer a2 =
      new AttachmentDepthRenderbuffer(rb2);

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

  @SuppressWarnings("static-method") @Test public
    void
    testMetaDepthStencilRenderbuffer()
      throws ConstraintError
  {
    final Renderbuffer rb1 =
      new Renderbuffer(
        RenderbufferType.RENDERBUFFER_DEPTH_24_STENCIL_8,
        1,
        128,
        128);
    final Renderbuffer rb2 =
      new Renderbuffer(
        RenderbufferType.RENDERBUFFER_DEPTH_24_STENCIL_8,
        2,
        128,
        128);

    final AttachmentDepthStencilRenderbuffer a0 =
      new AttachmentDepthStencilRenderbuffer(rb1);
    final AttachmentDepthStencilRenderbuffer a1 =
      new AttachmentDepthStencilRenderbuffer(rb1);
    final AttachmentDepthStencilRenderbuffer a2 =
      new AttachmentDepthStencilRenderbuffer(rb2);

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

  @SuppressWarnings("static-method") @Test public
    void
    testMetaSharedDepthRenderbuffer()
      throws ConstraintError
  {
    final Renderbuffer rb1 =
      new Renderbuffer(RenderbufferType.RENDERBUFFER_DEPTH_16, 1, 128, 128);
    final Renderbuffer rb2 =
      new Renderbuffer(RenderbufferType.RENDERBUFFER_DEPTH_16, 2, 128, 128);

    final AttachmentSharedDepthRenderbuffer a0 =
      new AttachmentSharedDepthRenderbuffer(rb1);
    final AttachmentSharedDepthRenderbuffer a1 =
      new AttachmentSharedDepthRenderbuffer(rb1);
    final AttachmentSharedDepthRenderbuffer a2 =
      new AttachmentSharedDepthRenderbuffer(rb2);

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
    testMetaSharedDepthStencilRenderbuffer()
      throws ConstraintError
  {
    final Renderbuffer rb1 =
      new Renderbuffer(
        RenderbufferType.RENDERBUFFER_DEPTH_24_STENCIL_8,
        1,
        128,
        128);
    final Renderbuffer rb2 =
      new Renderbuffer(
        RenderbufferType.RENDERBUFFER_DEPTH_24_STENCIL_8,
        2,
        128,
        128);

    final AttachmentSharedDepthStencilRenderbuffer a0 =
      new AttachmentSharedDepthStencilRenderbuffer(rb1);
    final AttachmentSharedDepthStencilRenderbuffer a1 =
      new AttachmentSharedDepthStencilRenderbuffer(rb1);
    final AttachmentSharedDepthStencilRenderbuffer a2 =
      new AttachmentSharedDepthStencilRenderbuffer(rb2);

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
}
