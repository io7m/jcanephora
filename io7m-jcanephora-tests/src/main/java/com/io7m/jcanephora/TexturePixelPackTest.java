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

public class TexturePixelPackTest
{
  @SuppressWarnings("static-method") @Test public void testPack2_4444()
  {
    {
      final char c =
        TexturePixelPack
          .pack2_4444((byte) 0xFF, (byte) 0, (byte) 0, (byte) 0);
      Assert.assertEquals(0xF000, c);
    }

    {
      final char c =
        TexturePixelPack.pack2_4444(
          (byte) 0x0,
          (byte) 0xFF,
          (byte) 0,
          (byte) 0);
      Assert.assertEquals(0xF00, c);
    }

    {
      final char c =
        TexturePixelPack.pack2_4444(
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0xFF,
          (byte) 0);
      Assert.assertEquals(0xF0, c);
    }

    {
      final char c =
        TexturePixelPack.pack2_4444(
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0xFF);
      Assert.assertEquals(0x0F, c);
    }

    {
      final char c =
        TexturePixelPack.pack2_4444(
          (byte) 0xFF,
          (byte) 0xFF,
          (byte) 0xFF,
          (byte) 0xFF);
      Assert.assertEquals(0xffff, c);
    }
  }

  @SuppressWarnings("static-method") @Test public void testPack2_5551()
  {
    {
      final char c =
        TexturePixelPack
          .pack2_5551((byte) 0xFF, (byte) 0, (byte) 0, (byte) 0);
      Assert.assertEquals(0xF800, c);
    }

    {
      final char c =
        TexturePixelPack.pack2_5551(
          (byte) 0x0,
          (byte) 0xFF,
          (byte) 0,
          (byte) 0);
      Assert.assertEquals(0x07C0, c);
    }

    {
      final char c =
        TexturePixelPack.pack2_5551(
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0xFF,
          (byte) 0);
      Assert.assertEquals(0x3E, c);
    }

    {
      final char c =
        TexturePixelPack.pack2_5551(
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0xFF);
      Assert.assertEquals(0x01, c);
    }

    {
      final char c =
        TexturePixelPack.pack2_5551(
          (byte) 0xFF,
          (byte) 0xFF,
          (byte) 0xFF,
          (byte) 0xFF);
      Assert.assertEquals(0xffff, c);
    }
  }

  @SuppressWarnings("static-method") @Test public void testPack2_565()
  {
    {
      final char c =
        TexturePixelPack.pack2_565((byte) 0xFF, (byte) 0, (byte) 0);
      Assert.assertEquals(0xF800, c);
    }

    {
      final char c =
        TexturePixelPack.pack2_565((byte) 0x00, (byte) 0xFF, (byte) 0);
      Assert.assertEquals(0x7E0, c);
    }

    {
      final char c =
        TexturePixelPack.pack2_565((byte) 0x00, (byte) 0x0, (byte) 0xFF);
      Assert.assertEquals(0x1F, c);
    }

    {
      final char c =
        TexturePixelPack.pack2_565((byte) 0xFF, (byte) 0xFF, (byte) 0xFF);
      Assert.assertEquals(0xFFFF, c);
    }
  }

  @SuppressWarnings("static-method") @Test public void testPack2_88()
  {
    {
      final char c = TexturePixelPack.pack2_88((byte) 0xFF, (byte) 0);
      Assert.assertEquals(0xFF00, c);
    }

    {
      final char c = TexturePixelPack.pack2_88((byte) 0x0, (byte) 0xFF);
      Assert.assertEquals(0x00FF, c);
    }
  }

  @SuppressWarnings("static-method") @Test public void testPack4_8888()
  {
    Assert.assertEquals(
      0,
      TexturePixelPack.pack4_8888((byte) 0, (byte) 0, (byte) 0, (byte) 0));

    Assert.assertEquals(
      0xFF000000,
      TexturePixelPack.pack4_8888((byte) 0xFF, (byte) 0, (byte) 0, (byte) 0));

    Assert.assertEquals(
      0x00FF0000,
      TexturePixelPack.pack4_8888((byte) 0, (byte) 0xFF, (byte) 0, (byte) 0));

    Assert.assertEquals(
      0x0000FF00,
      TexturePixelPack.pack4_8888((byte) 0, (byte) 0, (byte) 0xFF, (byte) 0));

    Assert.assertEquals(
      0x000000FF,
      TexturePixelPack.pack4_8888((byte) 0, (byte) 0, (byte) 0, (byte) 0xFF));

    Assert.assertEquals(0x00FF00FF, TexturePixelPack.pack4_8888(
      (byte) 0,
      (byte) 0xFF,
      (byte) 0,
      (byte) 0xFF));

    Assert.assertEquals(0x00FFFFFF, TexturePixelPack.pack4_8888(
      (byte) 0,
      (byte) 0xFF,
      (byte) 0xFF,
      (byte) 0xFF));

    Assert.assertEquals(0xFFFFFFFF, TexturePixelPack.pack4_8888(
      (byte) 0xFF,
      (byte) 0xFF,
      (byte) 0xFF,
      (byte) 0xFF));
  }

  @SuppressWarnings("static-method") @Test public void testUnpack2_4444()
  {
    {
      final int[] out = new int[4];
      final char c =
        TexturePixelPack
          .pack2_4444((byte) 0xFF, (byte) 0, (byte) 0, (byte) 0);
      TexturePixelPack.unpack2_4444(c, out);
      Assert.assertEquals(0xF0, out[0]);
      Assert.assertEquals(0x0, out[1]);
      Assert.assertEquals(0x0, out[2]);
      Assert.assertEquals(0x0, out[3]);
    }

    {
      final int[] out = new int[4];
      final char c =
        TexturePixelPack.pack2_4444(
          (byte) 0x0,
          (byte) 0xFF,
          (byte) 0,
          (byte) 0);
      TexturePixelPack.unpack2_4444(c, out);
      Assert.assertEquals(0x0, out[0]);
      Assert.assertEquals(0xF0, out[1]);
      Assert.assertEquals(0x0, out[2]);
      Assert.assertEquals(0x0, out[3]);
    }

    {
      final int[] out = new int[4];
      final char c =
        TexturePixelPack.pack2_4444(
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0xFF,
          (byte) 0);
      TexturePixelPack.unpack2_4444(c, out);
      Assert.assertEquals(0x0, out[0]);
      Assert.assertEquals(0x0, out[1]);
      Assert.assertEquals(0xF0, out[2]);
      Assert.assertEquals(0x0, out[3]);
    }

    {
      final int[] out = new int[4];
      final char c =
        TexturePixelPack.pack2_4444(
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0xFF);
      TexturePixelPack.unpack2_4444(c, out);
      Assert.assertEquals(0x0, out[0]);
      Assert.assertEquals(0x0, out[1]);
      Assert.assertEquals(0x0, out[2]);
      Assert.assertEquals(0xF0, out[3]);
    }
  }

  @SuppressWarnings("static-method") @Test public void testUnpack2_5551()
  {
    {
      final int[] out = new int[4];
      final char c =
        TexturePixelPack
          .pack2_5551((byte) 0xFF, (byte) 0, (byte) 0, (byte) 0);
      TexturePixelPack.unpack2_5551(c, out);
      Assert.assertEquals(0xF8, out[0]);
      Assert.assertEquals(0x0, out[1]);
      Assert.assertEquals(0x0, out[2]);
      Assert.assertEquals(0x0, out[3]);
    }

    {
      final int[] out = new int[4];
      final char c =
        TexturePixelPack.pack2_5551(
          (byte) 0x0,
          (byte) 0xFF,
          (byte) 0,
          (byte) 0);
      TexturePixelPack.unpack2_5551(c, out);
      Assert.assertEquals(0x0, out[0]);
      Assert.assertEquals(0xF8, out[1]);
      Assert.assertEquals(0x0, out[2]);
      Assert.assertEquals(0x0, out[3]);
    }

    {
      final int[] out = new int[4];
      final char c =
        TexturePixelPack.pack2_5551(
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0xFF,
          (byte) 0);
      TexturePixelPack.unpack2_5551(c, out);
      Assert.assertEquals(0x0, out[0]);
      Assert.assertEquals(0x0, out[1]);
      Assert.assertEquals(0xF8, out[2]);
      Assert.assertEquals(0x0, out[3]);
    }

    {
      final int[] out = new int[4];
      final char c =
        TexturePixelPack.pack2_5551(
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0xFF);
      TexturePixelPack.unpack2_5551(c, out);
      Assert.assertEquals(0x0, out[0]);
      Assert.assertEquals(0x0, out[1]);
      Assert.assertEquals(0x0, out[2]);
      Assert.assertEquals(0xFF, out[3]);
    }
  }

  @SuppressWarnings("static-method") @Test public void testUnpack2_565()
  {
    {
      final int[] out = new int[3];
      final char c =
        TexturePixelPack.pack2_565((byte) 0xFF, (byte) 0, (byte) 0);
      TexturePixelPack.unpack2_565(c, out);
      Assert.assertEquals(0xF8, out[0]);
      Assert.assertEquals(0x0, out[1]);
      Assert.assertEquals(0x0, out[2]);
    }

    {
      final int[] out = new int[3];
      final char c =
        TexturePixelPack.pack2_565((byte) 0, (byte) 0xFF, (byte) 0);
      TexturePixelPack.unpack2_565(c, out);
      Assert.assertEquals(0x0, out[0]);
      Assert.assertEquals(0xFC, out[1]);
      Assert.assertEquals(0x0, out[2]);
    }

    {
      final int[] out = new int[3];
      final char c =
        TexturePixelPack.pack2_565((byte) 0, (byte) 0, (byte) 0xFF);
      TexturePixelPack.unpack2_565(c, out);
      Assert.assertEquals(0x0, out[0]);
      Assert.assertEquals(0x0, out[1]);
      Assert.assertEquals(0xF8, out[2]);
    }
  }

  @SuppressWarnings("static-method") @Test public void testUnpack2_88()
  {
    {
      final int[] out = new int[2];
      final char c = TexturePixelPack.pack2_88((byte) 0xFF, (byte) 0);
      TexturePixelPack.unpack2_88(c, out);
      Assert.assertEquals(0xFF, out[0]);
      Assert.assertEquals(0x0, out[1]);
    }

    {
      final int[] out = new int[2];
      final char c = TexturePixelPack.pack2_88((byte) 0x0, (byte) 0xFF);
      TexturePixelPack.unpack2_88(c, out);
      Assert.assertEquals(0x0, out[0]);
      Assert.assertEquals(0xFF, out[1]);
    }
  }

  @SuppressWarnings("static-method") @Test public void testUnpack4_8888()
  {
    {
      final int[] out = new int[4];
      final int c =
        TexturePixelPack
          .pack4_8888((byte) 0xFF, (byte) 0, (byte) 0, (byte) 0);
      TexturePixelPack.unpack4_8888(c, out);
      Assert.assertEquals(0xFF, out[0]);
      Assert.assertEquals(0x0, out[1]);
      Assert.assertEquals(0x0, out[2]);
      Assert.assertEquals(0x0, out[3]);
    }

    {
      final int[] out = new int[4];
      final int c =
        TexturePixelPack.pack4_8888(
          (byte) 0x0,
          (byte) 0xFF,
          (byte) 0,
          (byte) 0);
      TexturePixelPack.unpack4_8888(c, out);
      Assert.assertEquals(0x0, out[0]);
      Assert.assertEquals(0xFF, out[1]);
      Assert.assertEquals(0x0, out[2]);
      Assert.assertEquals(0x0, out[3]);
    }

    {
      final int[] out = new int[4];
      final int c =
        TexturePixelPack.pack4_8888(
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0xFF,
          (byte) 0);
      TexturePixelPack.unpack4_8888(c, out);
      Assert.assertEquals(0x0, out[0]);
      Assert.assertEquals(0x0, out[1]);
      Assert.assertEquals(0xFF, out[2]);
      Assert.assertEquals(0x0, out[3]);
    }

    {
      final int[] out = new int[4];
      final int c =
        TexturePixelPack.pack4_8888(
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0xFF);
      TexturePixelPack.unpack4_8888(c, out);
      Assert.assertEquals(0x0, out[0]);
      Assert.assertEquals(0x0, out[1]);
      Assert.assertEquals(0x0, out[2]);
      Assert.assertEquals(0xFF, out[3]);
    }
  }
}
