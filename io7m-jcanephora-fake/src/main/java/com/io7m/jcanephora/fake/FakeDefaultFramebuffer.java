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

package com.io7m.jcanephora.fake;

import com.io7m.jcanephora.AreaInclusive;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeCheck;

/**
 * The description of the default framebuffer.
 */

public final class FakeDefaultFramebuffer
{
  /**
   * Construct a description of the default framebuffer.
   *
   * @param in_area
   *          The framebuffer area
   * @param in_bits_red
   *          The number of red bits
   * @param in_bits_green
   *          The number of green bits
   * @param in_bits_blue
   *          The number of blue bits
   * @param in_bits_depth
   *          The number of depth bits
   * @param in_bits_stencil
   *          The number of stencil bits
   *
   * @return A new framebuffer.
   */

  public static FakeDefaultFramebuffer newFramebuffer(
    final AreaInclusive in_area,
    final int in_bits_red,
    final int in_bits_green,
    final int in_bits_blue,
    final int in_bits_depth,
    final int in_bits_stencil)
  {
    return new FakeDefaultFramebuffer(
      in_area,
      in_bits_red,
      in_bits_green,
      in_bits_blue,
      in_bits_depth,
      in_bits_stencil);
  }
  private final AreaInclusive area;
  private final int           bits_blue;
  private final int           bits_depth;
  private final int           bits_green;
  private final int           bits_red;

  private final int           bits_stencil;

  /**
   * Construct a description of the default framebuffer.
   *
   * @param in_area
   *          The framebuffer area
   * @param in_bits_red
   *          The number of red bits
   * @param in_bits_green
   *          The number of green bits
   * @param in_bits_blue
   *          The number of blue bits
   * @param in_bits_depth
   *          The number of depth bits
   * @param in_bits_stencil
   *          The number of stencil bits
   */

  private FakeDefaultFramebuffer(
    final AreaInclusive in_area,
    final int in_bits_red,
    final int in_bits_green,
    final int in_bits_blue,
    final int in_bits_depth,
    final int in_bits_stencil)
  {
    this.area = NullCheck.notNull(in_area, "Area");
    this.bits_red =
      (int) RangeCheck.checkIncludedIn(
        in_bits_red,
        "Red bits",
        RangeCheck.NATURAL_INTEGER,
        "Minimum red bits");
    this.bits_green =
      (int) RangeCheck.checkIncludedIn(
        in_bits_green,
        "Green bits",
        RangeCheck.NATURAL_INTEGER,
        "Minimum green bits");
    this.bits_blue =
      (int) RangeCheck.checkIncludedIn(
        in_bits_blue,
        "Blue bits",
        RangeCheck.NATURAL_INTEGER,
        "Minimum blue bits");
    this.bits_depth =
      (int) RangeCheck.checkIncludedIn(
        in_bits_depth,
        "Depth bits",
        RangeCheck.NATURAL_INTEGER,
        "Minimum depth bits");
    this.bits_stencil =
      (int) RangeCheck.checkIncludedIn(
        in_bits_stencil,
        "Stencil bits",
        RangeCheck.NATURAL_INTEGER,
        "Minimum stencil bits");
  }

  /**
   * @return The framebuffer area.
   */

  public AreaInclusive getArea()
  {
    return this.area;
  }

  /**
   * @return The number of bits in the blue channel of the color attachment.
   */

  public int getBitsBlue()
  {
    return this.bits_blue;
  }

  /**
   * @return The number of bits in the depth attachment.
   */
  public int getBitsDepth()
  {
    return this.bits_depth;
  }

  /**
   * @return The number of bits in the green channel of the color attachment.
   */

  public int getBitsGreen()
  {
    return this.bits_green;
  }

  /**
   * @return The number of bits in the red channel of the color attachment.
   */

  public int getBitsRed()
  {
    return this.bits_red;
  }

  /**
   * @return The number of bits in the stencil attachment.
   */

  public int getBitsStencil()
  {
    return this.bits_stencil;
  }
}
