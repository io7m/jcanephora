/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;

public class Texture2DStaticTest
{
  @SuppressWarnings("static-method") @Test public void testEquals()
    throws ConstraintError
  {
    final Texture2DStatic t0 =
      new Texture2DStatic(
        "xyz",
        TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
        1,
        128,
        256,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    final Texture2DStatic t1 =
      new Texture2DStatic(
        "xyz",
        TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
        2,
        128,
        256,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    final Texture2DStatic t2 =
      new Texture2DStatic(
        "xyz",
        TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
        1,
        128,
        256,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

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
    final Texture2DStatic t0 =
      new Texture2DStatic(
        "xyz",
        TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
        1,
        128,
        256,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    final Texture2DStatic t1 =
      new Texture2DStatic(
        "xyz",
        TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
        2,
        128,
        256,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    final Texture2DStatic t2 =
      new Texture2DStatic(
        "xyz",
        TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
        1,
        128,
        256,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    final Texture2DStatic t3 =
      new Texture2DStatic(
        "abc",
        TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
        2,
        128,
        256,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    final Texture2DStatic t4 =
      new Texture2DStatic(
        "abc",
        TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
        1,
        128,
        256,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

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
    for (final TextureType type : TextureType.values()) {
      final Texture2DStatic t0 =
        new Texture2DStatic(
          "xyz",
          type,
          1,
          128,
          256,
          TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
          TextureWrapT.TEXTURE_WRAP_REPEAT,
          TextureFilterMinification.TEXTURE_FILTER_LINEAR,
          TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

      Assert.assertEquals(1, t0.getGLName());
      Assert.assertEquals("xyz", t0.getName());
      Assert.assertEquals(128, t0.getWidth());
      Assert.assertEquals(256, t0.getHeight());
      Assert.assertEquals(0, t0.getRangeX().getLower());
      Assert.assertEquals(127, t0.getRangeX().getUpper());
      Assert.assertEquals(0, t0.getRangeY().getLower());
      Assert.assertEquals(255, t0.getRangeY().getUpper());
      Assert.assertEquals(t0.getRangeX(), t0.getArea().getRangeX());
      Assert.assertEquals(t0.getRangeY(), t0.getArea().getRangeY());
      Assert.assertEquals(type, t0.getType());
      Assert.assertEquals(
        TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE,
        t0.getWrapS());
      Assert.assertEquals(TextureWrapT.TEXTURE_WRAP_REPEAT, t0.getWrapT());
      Assert.assertEquals(
        TextureFilterMinification.TEXTURE_FILTER_LINEAR,
        t0.getMinificationFilter());
      Assert.assertEquals(
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST,
        t0.getMagnificationFilter());
    }
  }

  @SuppressWarnings("static-method") @Test public void testToString()
    throws ConstraintError
  {
    final Texture2DStatic t0 =
      new Texture2DStatic(
        "xyz",
        TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
        1,
        128,
        256,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    final Texture2DStatic t1 =
      new Texture2DStatic(
        "xyz",
        TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
        2,
        128,
        256,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    final Texture2DStatic t2 =
      new Texture2DStatic(
        "xyz",
        TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
        1,
        128,
        256,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_REPEAT,
        TextureFilterMinification.TEXTURE_FILTER_NEAREST,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    Assert.assertEquals(t0.toString(), t0.toString());
    Assert.assertEquals(t0.toString(), t2.toString());
    Assert.assertEquals(t2.toString(), t0.toString());
    Assert.assertFalse(t0.toString().equals(t1.toString()));
    Assert.assertFalse(t0.toString().equals(null));
    Assert.assertFalse(t0.toString().equals(Integer.valueOf(23)));
  }
}
