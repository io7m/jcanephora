package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.AttachmentColor.AttachmentColorRenderbuffer;
import com.io7m.jcanephora.AttachmentColor.AttachmentColorTexture2DStatic;
import com.io7m.jcanephora.AttachmentColor.AttachmentColorTextureCubeStatic;
import com.io7m.jcanephora.AttachmentColor.AttachmentSharedColorRenderbuffer;
import com.io7m.jcanephora.AttachmentColor.AttachmentSharedColorTexture2DStatic;
import com.io7m.jcanephora.AttachmentColor.AttachmentSharedColorTextureCubeStatic;

public final class AttachmentColorTest
{
  @SuppressWarnings("static-method") @Test public
    void
    testMetaColorRenderbuffer()
      throws ConstraintError
  {
    final Renderbuffer rb1 =
      new Renderbuffer(
        RenderbufferType.RENDERBUFFER_COLOR_RGB_565,
        1,
        128,
        128);
    final Renderbuffer rb2 =
      new Renderbuffer(
        RenderbufferType.RENDERBUFFER_COLOR_RGB_565,
        2,
        128,
        128);

    final AttachmentColorRenderbuffer a0 =
      new AttachmentColorRenderbuffer(rb1);
    final AttachmentColorRenderbuffer a1 =
      new AttachmentColorRenderbuffer(rb1);
    final AttachmentColorRenderbuffer a2 =
      new AttachmentColorRenderbuffer(rb2);

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
    testMetaColorTexture2D()
      throws ConstraintError
  {
    final Texture2DStatic t1 =
      new Texture2DStatic(
        "t1",
        TextureType.TEXTURE_TYPE_RGB_565_2BPP,
        1,
        128,
        128,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    final Texture2DStatic t2 =
      new Texture2DStatic(
        "t2",
        TextureType.TEXTURE_TYPE_RGB_565_2BPP,
        2,
        128,
        128,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);

    final AttachmentColorTexture2DStatic a0 =
      new AttachmentColorTexture2DStatic(t1);
    final AttachmentColorTexture2DStatic a1 =
      new AttachmentColorTexture2DStatic(t1);
    final AttachmentColorTexture2DStatic a2 =
      new AttachmentColorTexture2DStatic(t2);

    Assert.assertTrue(a0.equals(a0));
    Assert.assertTrue(a0.equals(a1));
    Assert.assertFalse(a0.equals(null));
    Assert.assertFalse(a0.equals(Integer.valueOf(23)));
    Assert.assertFalse(a0.equals(a2));

    Assert.assertTrue(a0.hashCode() == a1.hashCode());
    Assert.assertFalse(a0.hashCode() == a2.hashCode());

    Assert.assertTrue(a0.toString().equals(a1.toString()));
    Assert.assertFalse(a0.toString().equals(a2.toString()));

    Assert.assertTrue(a0.getTexture2D() == t1);
    Assert.assertTrue(a1.getTexture2D() == t1);
    Assert.assertTrue(a2.getTexture2D() == t2);

    Assert.assertTrue(a0.getTextureWritable() == t1);
    Assert.assertTrue(a1.getTextureWritable() == t1);
    Assert.assertTrue(a2.getTextureWritable() == t2);
  }

  @SuppressWarnings("static-method") @Test public
    void
    testMetaColorTextureCube()
      throws ConstraintError
  {
    final TextureCubeStatic t1 =
      new TextureCubeStatic(
        "t1",
        TextureType.TEXTURE_TYPE_RGB_565_2BPP,
        1,
        128,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    final TextureCubeStatic t2 =
      new TextureCubeStatic(
        "t2",
        TextureType.TEXTURE_TYPE_RGB_565_2BPP,
        2,
        128,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);

    final AttachmentColorTextureCubeStatic a0 =
      new AttachmentColorTextureCubeStatic(
        t1,
        CubeMapFace.CUBE_MAP_POSITIVE_X);
    final AttachmentColorTextureCubeStatic a1 =
      new AttachmentColorTextureCubeStatic(
        t1,
        CubeMapFace.CUBE_MAP_POSITIVE_X);
    final AttachmentColorTextureCubeStatic a2 =
      new AttachmentColorTextureCubeStatic(
        t2,
        CubeMapFace.CUBE_MAP_POSITIVE_X);

    Assert.assertTrue(a0.equals(a0));
    Assert.assertTrue(a0.equals(a1));
    Assert.assertFalse(a0.equals(null));
    Assert.assertFalse(a0.equals(Integer.valueOf(23)));
    Assert.assertFalse(a0.equals(a2));

    Assert.assertTrue(a0.hashCode() == a1.hashCode());
    Assert.assertFalse(a0.hashCode() == a2.hashCode());

    Assert.assertTrue(a0.toString().equals(a1.toString()));
    Assert.assertFalse(a0.toString().equals(a2.toString()));

    Assert.assertTrue(a0.getTextureCube() == t1);
    Assert.assertTrue(a1.getTextureCube() == t1);
    Assert.assertTrue(a2.getTextureCube() == t2);

    Assert.assertTrue(a0.getTextureWritable() == t1);
    Assert.assertTrue(a1.getTextureWritable() == t1);
    Assert.assertTrue(a2.getTextureWritable() == t2);

    Assert.assertTrue(a0.getFace() == CubeMapFace.CUBE_MAP_POSITIVE_X);
  }

  @SuppressWarnings("static-method") @Test public
    void
    testMetaSharedColorRenderbuffer()
      throws ConstraintError
  {
    final Renderbuffer rb1 =
      new Renderbuffer(
        RenderbufferType.RENDERBUFFER_COLOR_RGB_565,
        1,
        128,
        128);
    final Renderbuffer rb2 =
      new Renderbuffer(
        RenderbufferType.RENDERBUFFER_COLOR_RGB_565,
        2,
        128,
        128);

    final AttachmentSharedColorRenderbuffer a0 =
      new AttachmentSharedColorRenderbuffer(rb1);
    final AttachmentSharedColorRenderbuffer a1 =
      new AttachmentSharedColorRenderbuffer(rb1);
    final AttachmentSharedColorRenderbuffer a2 =
      new AttachmentSharedColorRenderbuffer(rb2);

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
    testMetaSharedColorTexture2D()
      throws ConstraintError
  {
    final Texture2DStatic t1 =
      new Texture2DStatic(
        "t1",
        TextureType.TEXTURE_TYPE_RGB_565_2BPP,
        1,
        128,
        128,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    final Texture2DStatic t2 =
      new Texture2DStatic(
        "t2",
        TextureType.TEXTURE_TYPE_RGB_565_2BPP,
        2,
        128,
        128,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);

    final AttachmentSharedColorTexture2DStatic a0 =
      new AttachmentSharedColorTexture2DStatic(t1);
    final AttachmentSharedColorTexture2DStatic a1 =
      new AttachmentSharedColorTexture2DStatic(t1);
    final AttachmentSharedColorTexture2DStatic a2 =
      new AttachmentSharedColorTexture2DStatic(t2);

    Assert.assertTrue(a0.equals(a0));
    Assert.assertTrue(a0.equals(a1));
    Assert.assertFalse(a0.equals(null));
    Assert.assertFalse(a0.equals(Integer.valueOf(23)));
    Assert.assertFalse(a0.equals(a2));

    Assert.assertTrue(a0.hashCode() == a1.hashCode());
    Assert.assertFalse(a0.hashCode() == a2.hashCode());

    Assert.assertTrue(a0.toString().equals(a1.toString()));
    Assert.assertFalse(a0.toString().equals(a2.toString()));

    Assert.assertTrue(a0.getTexture2D() == t1);
    Assert.assertTrue(a1.getTexture2D() == t1);
    Assert.assertTrue(a2.getTexture2D() == t2);
  }

  @SuppressWarnings("static-method") @Test public
    void
    testMetaSharedColorTextureCube()
      throws ConstraintError
  {
    final TextureCubeStatic t1 =
      new TextureCubeStatic(
        "t1",
        TextureType.TEXTURE_TYPE_RGB_565_2BPP,
        1,
        128,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    final TextureCubeStatic t2 =
      new TextureCubeStatic(
        "t2",
        TextureType.TEXTURE_TYPE_RGB_565_2BPP,
        2,
        128,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);

    final AttachmentSharedColorTextureCubeStatic a0 =
      new AttachmentSharedColorTextureCubeStatic(
        t1,
        CubeMapFace.CUBE_MAP_POSITIVE_X);
    final AttachmentSharedColorTextureCubeStatic a1 =
      new AttachmentSharedColorTextureCubeStatic(
        t1,
        CubeMapFace.CUBE_MAP_POSITIVE_X);
    final AttachmentSharedColorTextureCubeStatic a2 =
      new AttachmentSharedColorTextureCubeStatic(
        t2,
        CubeMapFace.CUBE_MAP_POSITIVE_X);

    Assert.assertTrue(a0.equals(a0));
    Assert.assertTrue(a0.equals(a1));
    Assert.assertFalse(a0.equals(null));
    Assert.assertFalse(a0.equals(Integer.valueOf(23)));
    Assert.assertFalse(a0.equals(a2));

    Assert.assertTrue(a0.hashCode() == a1.hashCode());
    Assert.assertFalse(a0.hashCode() == a2.hashCode());

    Assert.assertTrue(a0.toString().equals(a1.toString()));
    Assert.assertFalse(a0.toString().equals(a2.toString()));

    Assert.assertTrue(a0.getTextureCube() == t1);
    Assert.assertTrue(a1.getTextureCube() == t1);
    Assert.assertTrue(a2.getTextureCube() == t2);

    Assert.assertTrue(a0.getFace() == CubeMapFace.CUBE_MAP_POSITIVE_X);
  }

}
