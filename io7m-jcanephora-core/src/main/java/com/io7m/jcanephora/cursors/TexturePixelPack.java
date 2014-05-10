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

package com.io7m.jcanephora.cursors;

import com.io7m.jtensors.VectorM3I;
import com.io7m.jtensors.VectorM4I;
import com.io7m.junreachable.UnreachableCodeException;

/**
 * Functions to pack color values into integers.
 */

public final class TexturePixelPack
{
  /**
   * Pack the given integers in the range [0, 0xff] into a 1010102 format.
   * 
   * @param r
   *          The red channel.
   * @param g
   *          The green channel.
   * @param b
   *          The blue channel.
   * @param a
   *          The alpha channel.
   * @return A packed value.
   */

  public static long pack1010102(
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

  /**
   * Pack the given integers in the range [0, 0xff] into a 4444 format.
   * 
   * @param r
   *          The red channel.
   * @param g
   *          The green channel.
   * @param b
   *          The blue channel.
   * @param a
   *          The alpha channel.
   * @return A packed value.
   */

  public static int pack4444(
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

  /**
   * Pack the given integers in the range [0, 0xff] into a 5551 format.
   * 
   * @param r
   *          The red channel.
   * @param g
   *          The green channel.
   * @param b
   *          The blue channel.
   * @param a
   *          The alpha channel.
   * @return A packed value.
   */

  public static int pack5551(
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

  /**
   * Pack the given integers in the range [0, 0xff] into a 565 format.
   * 
   * @param r
   *          The red channel.
   * @param g
   *          The green channel.
   * @param b
   *          The blue channel.
   * @return A packed value.
   */

  public static int pack565(
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

  /**
   * Unpack the given from a 1010102 format, saving the channels to the given
   * vector.
   * 
   * @param k
   *          The packed value.
   * @param out
   *          The output vector.
   */

  public static void unpack1010102(
    final long k,
    final VectorM4I out)
  {
    out.set4I(
      (int) ((k & 0xffc00000) >> 22),
      (int) ((k & 0x003ff000) >> 12),
      (int) ((k & 0x00000ffc) >> 2),
      (int) (k & 0x00000003));
  }

  /**
   * Unpack the given from a 4444 format, saving the channels to the given
   * vector.
   * 
   * @param rgba
   *          The packed value.
   * @param out
   *          The output vector.
   */

  public static void unpack4444(
    final int rgba,
    final VectorM4I out)
  {
    out.set4I(
      (rgba & 0xF000) >> 12,
      (rgba & 0x0F00) >> 8,
      (rgba & 0x00F0) >> 4,
      rgba & 0x000F);
  }

  /**
   * Unpack the given from a 5551 format, saving the channels to the given
   * vector.
   * 
   * @param rgba
   *          The packed value.
   * @param out
   *          The output vector.
   */

  public static void unpack5551(
    final int rgba,
    final VectorM4I out)
  {
    out.set4I(
      (rgba & 0xF800) >> 11,
      (rgba & 0x07C0) >> 6,
      (rgba & 0x003E) >> 1,
      (rgba & 0x0001));
  }

  /**
   * Unpack the given from a 565 format, saving the channels to the given
   * vector.
   * 
   * @param rgb
   *          The packed value.
   * @param out
   *          The output vector.
   */

  public static void unpack565(
    final int rgb,
    final VectorM3I out)
  {
    out.set3I((rgb & 0xF800) >> 11, (rgb & 0x07E0) >> 5, (rgb & 0x001F));
  }

  private TexturePixelPack()
  {
    throw new UnreachableCodeException();
  }
}
