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

import javax.annotation.Nonnull;

import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jtensors.VectorM3I;
import com.io7m.jtensors.VectorM4I;

/**
 * Functions to pack color values into integers.
 */

final class TexturePixelPack
{
  public static int pack2_4444(
    final int r,
    final int g,
    final int b,
    final int a)
  {
    int c = 0;
    c |= (r << 12) & 0xF000;
    c |= (g << 8) & 0x0F00;
    c |= (b << 4) & 0x00F0;
    c |= a & 0x000F;
    return c;
  }

  public static int pack2_5551(
    final int r,
    final int g,
    final int b,
    final int a)
  {
    int c = 0;
    c |= (r << 11) & 0xF800;
    c |= (g << 6) & 0x07C0;
    c |= (b << 1) & 0x003E;
    c |= a & 0x0001;
    return c;
  }

  public static int pack2_565(
    final int r,
    final int g,
    final int b)
  {
    int c = 0;
    c |= (r << 11) & 0xF800;
    c |= (g << 5) & 0x07E0;
    c |= b & 0x1F;
    return c;
  }

  public static long pack4_1010102(
    final int r,
    final int g,
    final int b,
    final int a)
  {
    long c = 0;
    c |= (r << 22) & 0xffc00000;
    c |= (g << 12) & 0x003ff000;
    c |= (b << 2) & 0x00000ffc;
    c |= a & 0x3;
    return c;
  }

  public static void unpack2_4444(
    final int rgba,
    final @Nonnull VectorM4I out)
  {
    out.x = (rgba & 0xF000) >> 12;
    out.y = (rgba & 0x0F00) >> 8;
    out.z = (rgba & 0x00F0) >> 4;
    out.w = rgba & 0x000F;
  }

  public static void unpack2_5551(
    final int rgba,
    final @Nonnull VectorM4I out)
  {
    out.x = (rgba & 0xF800) >> 11;
    out.y = (rgba & 0x07C0) >> 6;
    out.z = (rgba & 0x003E) >> 1;
    out.w = (rgba & 0x0001);
  }

  public static void unpack2_565(
    final int rgb,
    final @Nonnull VectorM3I out)
  {
    out.x = (rgb & 0xF800) >> 11;
    out.y = (rgb & 0x07E0) >> 5;
    out.z = (rgb & 0x001F);
  }

  public static void unpack4_1010102(
    final long k,
    final @Nonnull VectorM4I out)
  {
    out.x = (int) ((k & 0xffc00000) >> 22);
    out.y = (int) ((k & 0x003ff000) >> 12);
    out.z = (int) ((k & 0x00000ffc) >> 2);
    out.w = (int) (k & 0x00000003);
  }

  private TexturePixelPack()
  {
    throw new UnreachableCodeException();
  }
}
