/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.tests;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jcanephora.cursors.TexturePixelPack;
import com.io7m.jtensors.VectorM3I;
import com.io7m.jtensors.VectorM4I;

@SuppressWarnings("static-method") public class TexturePixelPackTest
{
  @Test public void testPack2_4444()
  {
    {
      final int c =
        TexturePixelPack.pack4444((byte) 0xFF, (byte) 0, (byte) 0, (byte) 0);
      Assert.assertEquals(0xF000, c);
    }

    {
      final int c =
        TexturePixelPack
          .pack4444((byte) 0x0, (byte) 0xFF, (byte) 0, (byte) 0);
      Assert.assertEquals(0xF00, c);
    }

    {
      final int c =
        TexturePixelPack.pack4444(
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0xFF,
          (byte) 0);
      Assert.assertEquals(0xF0, c);
    }

    {
      final int c =
        TexturePixelPack.pack4444(
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0xFF);
      Assert.assertEquals(0x0F, c);
    }

    {
      final int c =
        TexturePixelPack.pack4444(
          (byte) 0xFF,
          (byte) 0xFF,
          (byte) 0xFF,
          (byte) 0xFF);
      Assert.assertEquals(0xffff, c);
    }
  }

  @Test public void testPack2_5551()
  {
    {
      final int c =
        TexturePixelPack.pack5551((byte) 0xFF, (byte) 0, (byte) 0, (byte) 0);
      Assert.assertEquals(0xF800, c);
    }

    {
      final int c =
        TexturePixelPack
          .pack5551((byte) 0x0, (byte) 0xFF, (byte) 0, (byte) 0);
      Assert.assertEquals(0x07C0, c);
    }

    {
      final int c =
        TexturePixelPack.pack5551(
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0xFF,
          (byte) 0);
      Assert.assertEquals(0x3E, c);
    }

    {
      final int c =
        TexturePixelPack.pack5551(
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0xFF);
      Assert.assertEquals(0x01, c);
    }

    {
      final int c =
        TexturePixelPack.pack5551(
          (byte) 0xFF,
          (byte) 0xFF,
          (byte) 0xFF,
          (byte) 0xFF);
      Assert.assertEquals(0xffff, c);
    }
  }

  @Test public void testPack2_565()
  {
    {
      final int c = TexturePixelPack.pack565((byte) 0xFF, (byte) 0, (byte) 0);
      Assert.assertEquals(0xF800, c);
    }

    {
      final int c =
        TexturePixelPack.pack565((byte) 0x00, (byte) 0xFF, (byte) 0);
      Assert.assertEquals(0x7E0, c);
    }

    {
      final int c =
        TexturePixelPack.pack565((byte) 0x00, (byte) 0x0, (byte) 0xFF);
      Assert.assertEquals(0x1F, c);
    }

    {
      final int c =
        TexturePixelPack.pack565((byte) 0xFF, (byte) 0xFF, (byte) 0xFF);
      Assert.assertEquals(0xFFFF, c);
    }
  }

  @Test public void testUnpack2_4444()
  {
    {
      final VectorM4I out = new VectorM4I();
      final int c =
        TexturePixelPack.pack4444((byte) 0xFF, (byte) 0, (byte) 0, (byte) 0);
      TexturePixelPack.unpack4444(c, out);
      Assert.assertEquals(0xF, out.getXI());
      Assert.assertEquals(0x0, out.getYI());
      Assert.assertEquals(0x0, out.getZI());
      Assert.assertEquals(0x0, out.getWI());
    }

    {
      final VectorM4I out = new VectorM4I();
      final int c =
        TexturePixelPack
          .pack4444((byte) 0x0, (byte) 0xFF, (byte) 0, (byte) 0);
      TexturePixelPack.unpack4444(c, out);
      Assert.assertEquals(0x0, out.getXI());
      Assert.assertEquals(0xF, out.getYI());
      Assert.assertEquals(0x0, out.getZI());
      Assert.assertEquals(0x0, out.getWI());
    }

    {
      final VectorM4I out = new VectorM4I();
      final int c =
        TexturePixelPack.pack4444(
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0xFF,
          (byte) 0);
      TexturePixelPack.unpack4444(c, out);
      Assert.assertEquals(0x0, out.getXI());
      Assert.assertEquals(0x0, out.getYI());
      Assert.assertEquals(0xF, out.getZI());
      Assert.assertEquals(0x0, out.getWI());
    }

    {
      final VectorM4I out = new VectorM4I();
      final int c =
        TexturePixelPack.pack4444(
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0xFF);
      TexturePixelPack.unpack4444(c, out);
      Assert.assertEquals(0x0, out.getXI());
      Assert.assertEquals(0x0, out.getYI());
      Assert.assertEquals(0x0, out.getZI());
      Assert.assertEquals(0xF, out.getWI());
    }
  }

  @Test public void testUnpack2_5551()
  {
    {
      final VectorM4I out = new VectorM4I();
      final int c =
        TexturePixelPack.pack5551((byte) 0xFF, (byte) 0, (byte) 0, (byte) 0);
      TexturePixelPack.unpack5551(c, out);
      Assert.assertEquals(31, out.getXI());
      Assert.assertEquals(0x0, out.getYI());
      Assert.assertEquals(0x0, out.getZI());
      Assert.assertEquals(0x0, out.getWI());
    }

    {
      final VectorM4I out = new VectorM4I();
      final int c =
        TexturePixelPack
          .pack5551((byte) 0x0, (byte) 0xFF, (byte) 0, (byte) 0);
      TexturePixelPack.unpack5551(c, out);
      Assert.assertEquals(0x0, out.getXI());
      Assert.assertEquals(31, out.getYI());
      Assert.assertEquals(0x0, out.getZI());
      Assert.assertEquals(0x0, out.getWI());
    }

    {
      final VectorM4I out = new VectorM4I();
      final int c =
        TexturePixelPack.pack5551(
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0xFF,
          (byte) 0);
      TexturePixelPack.unpack5551(c, out);
      Assert.assertEquals(0x0, out.getXI());
      Assert.assertEquals(0x0, out.getYI());
      Assert.assertEquals(31, out.getZI());
      Assert.assertEquals(0x0, out.getWI());
    }

    {
      final VectorM4I out = new VectorM4I();
      final int c =
        TexturePixelPack.pack5551(
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0x0,
          (byte) 0xFF);
      TexturePixelPack.unpack5551(c, out);
      Assert.assertEquals(0x0, out.getXI());
      Assert.assertEquals(0x0, out.getYI());
      Assert.assertEquals(0x0, out.getZI());
      Assert.assertEquals(1, out.getWI());
    }
  }

  @Test public void testUnpack2_565()
  {
    {
      final VectorM3I out = new VectorM3I();
      final int c = TexturePixelPack.pack565((byte) 0xFF, (byte) 0, (byte) 0);
      TexturePixelPack.unpack565(c, out);
      Assert.assertEquals(31, out.getXI());
      Assert.assertEquals(0x0, out.getYI());
      Assert.assertEquals(0x0, out.getZI());
    }

    {
      final VectorM3I out = new VectorM3I();
      final int c = TexturePixelPack.pack565((byte) 0, (byte) 0xFF, (byte) 0);
      TexturePixelPack.unpack565(c, out);
      Assert.assertEquals(0x0, out.getXI());
      Assert.assertEquals(0x3f, out.getYI());
      Assert.assertEquals(0x0, out.getZI());
    }

    {
      final VectorM3I out = new VectorM3I();
      final int c = TexturePixelPack.pack565((byte) 0, (byte) 0, (byte) 0xFF);
      TexturePixelPack.unpack565(c, out);
      Assert.assertEquals(0x0, out.getXI());
      Assert.assertEquals(0x0, out.getYI());
      Assert.assertEquals(31, out.getZI());
    }
  }
}
