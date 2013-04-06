/*
 * Copyright © 2012 http://io7m.com
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

import com.io7m.jaux.UnreachableCodeException;

/**
 * Functions to pack color values into integers.
 */

final class TexturePixelPack
{
  public static char pack2_4444(
    final int r,
    final int g,
    final int b,
    final int a)
  {
    final int r_hi4 = (r & 0xF0); // Select 4 high bits
    final int g_hi4 = (g & 0xF0); // Select 4 high bits
    final int b_hi4 = (b & 0xF0); // Select 4 high bits
    final int a_hi4 = (a & 0xF0); // Select 4 high bits

    char c = 0;
    c |= (r_hi4) << 8;
    c |= (g_hi4) << 4;
    c |= (b_hi4);
    c |= (a_hi4) >> 4;
    return c;
  }

  public static char pack2_5551(
    final int r,
    final int g,
    final int b,
    final int a)
  {
    final int r_hi5 = (r & 0xF8); // Select 5 high bits
    final int g_hi5 = (g & 0xF8); // Select 5 high bits
    final int b_hi5 = (b & 0xF8); // Select 5 high bits
    final int a_hi1 = (a & 0x80); // Select high bit

    char c = 0;
    c |= (r_hi5) << 8;
    c |= (g_hi5) << 3;
    c |= (b_hi5) >> 2;
    c |= (a_hi1) >> 7;
    return c;
  }

  public static char pack2_565(
    final int r,
    final int g,
    final int b)
  {
    final int r_hi5 = (r & 0xF8); // Select 5 high bits
    final int g_hi6 = (g & 0xFC); // Select 6 high bits
    final int b_hi5 = (b & 0xF8); // Select 5 high bits

    char c = 0;
    c |= (r_hi5) << 8;
    c |= (g_hi6) << 3;
    c |= (b_hi5) >> 3;
    return c;
  }

  public static char pack2_88(
    final int x,
    final int y)
  {
    final int xb = (x << 8) & 0xFF00;
    final int yb = (y & 0xFF);
    return (char) (xb | yb);
  }

  public static int pack4_8888(
    final int r,
    final int g,
    final int b,
    final int a)
  {
    final int ri = (r << 24) & 0xFF000000;
    final int rg = (g << 16) & 0x00FF0000;
    final int rb = (b << 8) & 0x0000FF00;
    final int ra = a & 0x000000FF;
    return ri | rg | rb | ra;
  }

  public static void unpack2_4444(
    final char rgba,
    final int out[])
  {
    out[0] = ((rgba & 0xF000) >> 8) & 0xff;
    out[1] = ((rgba & 0x0F00) >> 4) & 0xff;
    out[2] = ((rgba & 0x00F0) >> 0) & 0xff;
    out[3] = ((rgba & 0x000F) << 4) & 0xff;
  }

  public static void unpack2_5551(
    final char rgba,
    final int out[])
  {
    out[0] = ((rgba & 0xF800) >> 8) & 0xff;
    out[1] = ((rgba & 0x07C0) >> 3) & 0xff;
    out[2] = ((rgba & 0x003E) << 2) & 0xff;
    out[3] = (rgba & 0x0001) == 1 ? 0xff : 0x00;
  }

  public static void unpack2_565(
    final char rgb,
    final int out[])
  {
    out[0] = ((rgb & 0xF800) >> 8) & 0xFF;
    out[1] = ((rgb & 0x07E0) >> 3) & 0xFF;
    out[2] = ((rgb & 0x001F) << 3) & 0xFF;
  }

  public static void unpack2_88(
    final char rgb,
    final int out[])
  {
    out[0] = ((rgb & 0xff00) >> 8) & 0xff;
    out[1] = rgb & 0xff;
  }

  public static void unpack4_8888(
    final int rgba,
    final int out[])
  {
    out[0] = ((rgba & 0xFF000000) >> 24) & 0xff;
    out[1] = ((rgba & 0x00FF0000) >> 16) & 0xff;
    out[2] = ((rgba & 0x0000FF00) >> 8) & 0xff;
    out[3] = ((rgba & 0x000000FF) >> 0) & 0xff;
  }

  private TexturePixelPack()
  {
    throw new UnreachableCodeException();
  }
}
