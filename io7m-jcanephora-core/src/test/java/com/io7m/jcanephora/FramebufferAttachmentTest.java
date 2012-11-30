package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.FramebufferAttachment.ColorAttachment;
import com.io7m.jcanephora.FramebufferAttachment.RenderbufferD24S8Attachment;

public class FramebufferAttachmentTest
{
  @SuppressWarnings("static-method") @Test public void testColorEquals()
    throws ConstraintError
  {
    final Texture2DStatic ta =
      new Texture2DStatic(
        "xyz",
        TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
        1,
        128,
        256);
    final Texture2DStatic tb =
      new Texture2DStatic(
        "xyz",
        TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
        2,
        128,
        256);

    final ColorAttachment tu0 = new ColorAttachment(ta, 1);
    final ColorAttachment tu1 = new ColorAttachment(ta, 2);
    final ColorAttachment tu2 = new ColorAttachment(ta, 1);
    final ColorAttachment tu3 = new ColorAttachment(tb, 1);

    Assert.assertEquals(tu0, tu0);
    Assert.assertEquals(tu0, tu2);
    Assert.assertEquals(tu2, tu0);
    Assert.assertFalse(tu0.equals(tu1));
    Assert.assertFalse(tu0.equals(tu3));
    Assert.assertFalse(tu0.equals(null));
    Assert.assertFalse(tu0.equals(Integer.valueOf(23)));
  }

  @SuppressWarnings("static-method") @Test public void testColorHashCode()
    throws ConstraintError
  {
    final Texture2DStatic ta =
      new Texture2DStatic(
        "xyz",
        TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
        1,
        128,
        256);

    final ColorAttachment tu0 = new ColorAttachment(ta, 1);
    final ColorAttachment tu1 = new ColorAttachment(ta, 2);

    Assert.assertTrue(tu0.hashCode() == tu0.hashCode());
    Assert.assertTrue(tu0.hashCode() != tu1.hashCode());
  }

  @SuppressWarnings("static-method") @Test public void testColorIdentities()
    throws ConstraintError
  {
    final Texture2DStatic ta =
      new Texture2DStatic(
        "xyz",
        TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
        1,
        128,
        256);
    final ColorAttachment tu0 = new ColorAttachment(ta, 1);

    Assert.assertEquals(1, tu0.getIndex());
    Assert.assertSame(ta, tu0.getTexture());
  }

  @SuppressWarnings("static-method") @Test public void testColorToString()
    throws ConstraintError
  {
    final Texture2DStatic ta =
      new Texture2DStatic(
        "xyz",
        TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
        1,
        128,
        256);
    final ColorAttachment tu0 = new ColorAttachment(ta, 1);
    final ColorAttachment tu1 = new ColorAttachment(ta, 2);
    final ColorAttachment tu2 = new ColorAttachment(ta, 1);

    Assert.assertEquals(tu0.toString(), tu0.toString());
    Assert.assertEquals(tu0.toString(), tu2.toString());
    Assert.assertEquals(tu2.toString(), tu0.toString());
    Assert.assertFalse(tu0.toString().equals(tu1.toString()));
    Assert.assertFalse(tu0.toString().equals(null));
    Assert.assertFalse(tu0.toString().equals(Integer.valueOf(23)));
  }

  @SuppressWarnings("static-method") @Test public
    void
    testRenderD24S8Equals()
      throws ConstraintError
  {
    final RenderbufferD24S8 rb0 = new RenderbufferD24S8(1, 128, 256);
    final RenderbufferD24S8 rb1 = new RenderbufferD24S8(2, 128, 256);

    final RenderbufferD24S8Attachment tu0 =
      new RenderbufferD24S8Attachment(rb0);
    final RenderbufferD24S8Attachment tu1 =
      new RenderbufferD24S8Attachment(rb0);
    final RenderbufferD24S8Attachment tu2 =
      new RenderbufferD24S8Attachment(rb1);

    Assert.assertEquals(tu0, tu0);
    Assert.assertEquals(tu0, tu1);
    Assert.assertEquals(tu1, tu0);
    Assert.assertFalse(tu0.equals(tu2));
    Assert.assertFalse(tu0.equals(null));
    Assert.assertFalse(tu0.equals(Integer.valueOf(23)));
  }

  @SuppressWarnings("static-method") @Test public
    void
    testRenderD24S8HashCode()
      throws ConstraintError
  {
    final RenderbufferD24S8 rb0 = new RenderbufferD24S8(1, 128, 256);
    final RenderbufferD24S8 rb1 = new RenderbufferD24S8(2, 128, 256);

    final RenderbufferD24S8Attachment tu0 =
      new RenderbufferD24S8Attachment(rb0);
    final RenderbufferD24S8Attachment tu1 =
      new RenderbufferD24S8Attachment(rb0);
    final RenderbufferD24S8Attachment tu2 =
      new RenderbufferD24S8Attachment(rb1);

    Assert.assertTrue(tu0.hashCode() == tu0.hashCode());
    Assert.assertTrue(tu0.hashCode() == tu1.hashCode());
    Assert.assertTrue(tu1.hashCode() != tu2.hashCode());
  }

  @SuppressWarnings("static-method") @Test public
    void
    testRenderD24S8Identities()
      throws ConstraintError
  {
    final RenderbufferD24S8 rb0 = new RenderbufferD24S8(1, 128, 256);
    final RenderbufferD24S8Attachment tu0 =
      new RenderbufferD24S8Attachment(rb0);

    Assert.assertSame(rb0, tu0.getRenderbuffer());
  }

  @SuppressWarnings("static-method") @Test public
    void
    testRenderD24S8ToString()
      throws ConstraintError
  {
    final RenderbufferD24S8 rb0 = new RenderbufferD24S8(1, 128, 256);
    final RenderbufferD24S8 rb1 = new RenderbufferD24S8(2, 128, 256);
    final RenderbufferD24S8Attachment tu0 =
      new RenderbufferD24S8Attachment(rb0);
    final RenderbufferD24S8Attachment tu1 =
      new RenderbufferD24S8Attachment(rb1);
    final RenderbufferD24S8Attachment tu2 =
      new RenderbufferD24S8Attachment(rb0);

    Assert.assertEquals(tu0.toString(), tu0.toString());
    Assert.assertEquals(tu0.toString(), tu2.toString());
    Assert.assertEquals(tu2.toString(), tu0.toString());
    Assert.assertFalse(tu0.toString().equals(tu1.toString()));
    Assert.assertFalse(tu0.toString().equals(null));
    Assert.assertFalse(tu0.toString().equals(Integer.valueOf(23)));
  }
}
