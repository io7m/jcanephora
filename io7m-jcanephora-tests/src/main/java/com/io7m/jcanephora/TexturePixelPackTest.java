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

import com.io7m.jtensors.VectorM3I;
import com.io7m.jtensors.VectorM4I;

public class TexturePixelPackTest
{
  @SuppressWarnings("static-method") @Test public void testPack2_4444()
  {
    {
      final int c =
        TexturePixelPack
          .pack2_4444((byte) 0xFF, (byte) 0, (byte) 0, (byte) 0);
      Assert.assertEquals(0xF000, c);
    }

    {
      final int c =
        TexturePixelPack.pack2_4444(
          (byte) 0x0,
          (byte) 0xFF,
          (byte) 0,
          (byte) 0);
      Assert.assertEquals(0xF00, c);
    }

    {
      final int c =
        TexturePixelPack.pack2_4444(
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0xFF,
          (byte) 0);
      Assert.assertEquals(0xF0, c);
    }

    {
      final int c =
        TexturePixelPack.pack2_4444(
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0xFF);
      Assert.assertEquals(0x0F, c);
    }

    {
      final int c =
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
      final int c =
        TexturePixelPack
          .pack2_5551((byte) 0xFF, (byte) 0, (byte) 0, (byte) 0);
      Assert.assertEquals(0xF800, c);
    }

    {
      final int c =
        TexturePixelPack.pack2_5551(
          (byte) 0x0,
          (byte) 0xFF,
          (byte) 0,
          (byte) 0);
      Assert.assertEquals(0x07C0, c);
    }

    {
      final int c =
        TexturePixelPack.pack2_5551(
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0xFF,
          (byte) 0);
      Assert.assertEquals(0x3E, c);
    }

    {
      final int c =
        TexturePixelPack.pack2_5551(
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0xFF);
      Assert.assertEquals(0x01, c);
    }

    {
      final int c =
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
      final int c =
        TexturePixelPack.pack2_565((byte) 0xFF, (byte) 0, (byte) 0);
      Assert.assertEquals(0xF800, c);
    }

    {
      final int c =
        TexturePixelPack.pack2_565((byte) 0x00, (byte) 0xFF, (byte) 0);
      Assert.assertEquals(0x7E0, c);
    }

    {
      final int c =
        TexturePixelPack.pack2_565((byte) 0x00, (byte) 0x0, (byte) 0xFF);
      Assert.assertEquals(0x1F, c);
    }

    {
      final int c =
        TexturePixelPack.pack2_565((byte) 0xFF, (byte) 0xFF, (byte) 0xFF);
      Assert.assertEquals(0xFFFF, c);
    }
  }

  @SuppressWarnings("static-method") @Test public void testUnpack2_4444()
  {
    {
      final VectorM4I out = new VectorM4I();
      final int c =
        TexturePixelPack
          .pack2_4444((byte) 0xFF, (byte) 0, (byte) 0, (byte) 0);
      TexturePixelPack.unpack2_4444(c, out);
      Assert.assertEquals(0xF, out.x);
      Assert.assertEquals(0x0, out.y);
      Assert.assertEquals(0x0, out.z);
      Assert.assertEquals(0x0, out.w);
    }

    {
      final VectorM4I out = new VectorM4I();
      final int c =
        TexturePixelPack.pack2_4444(
          (byte) 0x0,
          (byte) 0xFF,
          (byte) 0,
          (byte) 0);
      TexturePixelPack.unpack2_4444(c, out);
      Assert.assertEquals(0x0, out.x);
      Assert.assertEquals(0xF, out.y);
      Assert.assertEquals(0x0, out.z);
      Assert.assertEquals(0x0, out.w);
    }

    {
      final VectorM4I out = new VectorM4I();
      final int c =
        TexturePixelPack.pack2_4444(
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0xFF,
          (byte) 0);
      TexturePixelPack.unpack2_4444(c, out);
      Assert.assertEquals(0x0, out.x);
      Assert.assertEquals(0x0, out.y);
      Assert.assertEquals(0xF, out.z);
      Assert.assertEquals(0x0, out.w);
    }

    {
      final VectorM4I out = new VectorM4I();
      final int c =
        TexturePixelPack.pack2_4444(
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0xFF);
      TexturePixelPack.unpack2_4444(c, out);
      Assert.assertEquals(0x0, out.x);
      Assert.assertEquals(0x0, out.y);
      Assert.assertEquals(0x0, out.z);
      Assert.assertEquals(0xF, out.w);
    }
  }

  @SuppressWarnings("static-method") @Test public void testUnpack2_5551()
  {
    {
      final VectorM4I out = new VectorM4I();
      final int c =
        TexturePixelPack
          .pack2_5551((byte) 0xFF, (byte) 0, (byte) 0, (byte) 0);
      TexturePixelPack.unpack2_5551(c, out);
      Assert.assertEquals(31, out.x);
      Assert.assertEquals(0x0, out.y);
      Assert.assertEquals(0x0, out.z);
      Assert.assertEquals(0x0, out.w);
    }

    {
      final VectorM4I out = new VectorM4I();
      final int c =
        TexturePixelPack.pack2_5551(
          (byte) 0x0,
          (byte) 0xFF,
          (byte) 0,
          (byte) 0);
      TexturePixelPack.unpack2_5551(c, out);
      Assert.assertEquals(0x0, out.x);
      Assert.assertEquals(31, out.y);
      Assert.assertEquals(0x0, out.z);
      Assert.assertEquals(0x0, out.w);
    }

    {
      final VectorM4I out = new VectorM4I();
      final int c =
        TexturePixelPack.pack2_5551(
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0xFF,
          (byte) 0);
      TexturePixelPack.unpack2_5551(c, out);
      Assert.assertEquals(0x0, out.x);
      Assert.assertEquals(0x0, out.y);
      Assert.assertEquals(31, out.z);
      Assert.assertEquals(0x0, out.w);
    }

    {
      final VectorM4I out = new VectorM4I();
      final int c =
        TexturePixelPack.pack2_5551(
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0xFF);
      TexturePixelPack.unpack2_5551(c, out);
      Assert.assertEquals(0x0, out.x);
      Assert.assertEquals(0x0, out.y);
      Assert.assertEquals(0x0, out.z);
      Assert.assertEquals(1, out.w);
    }
  }

  @SuppressWarnings("static-method") @Test public void testUnpack2_565()
  {
    {
      final VectorM3I out = new VectorM3I();
      final int c =
        TexturePixelPack.pack2_565((byte) 0xFF, (byte) 0, (byte) 0);
      TexturePixelPack.unpack2_565(c, out);
      Assert.assertEquals(31, out.x);
      Assert.assertEquals(0x0, out.y);
      Assert.assertEquals(0x0, out.z);
    }

    {
      final VectorM3I out = new VectorM3I();
      final int c =
        TexturePixelPack.pack2_565((byte) 0, (byte) 0xFF, (byte) 0);
      TexturePixelPack.unpack2_565(c, out);
      Assert.assertEquals(0x0, out.x);
      Assert.assertEquals(0x3f, out.y);
      Assert.assertEquals(0x0, out.z);
    }

    {
      final VectorM3I out = new VectorM3I();
      final int c =
        TexturePixelPack.pack2_565((byte) 0, (byte) 0, (byte) 0xFF);
      TexturePixelPack.unpack2_565(c, out);
      Assert.assertEquals(0x0, out.x);
      Assert.assertEquals(0x0, out.y);
      Assert.assertEquals(31, out.z);
    }
  }
}
