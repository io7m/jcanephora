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

package com.io7m.jcanephora;

import com.io7m.jranges.RangeCheck;
import com.io7m.jranges.RangeCheckException;
import com.io7m.junreachable.UnreachableCodeException;

/**
 * Functions for converting between floating point and signed/unsigned
 * fixed-point as described in the OpenGL standard.
 */

public final class FixedPoint
{
  /**
   * Conversion of a floating point value to a signed normalized fixed-point
   * value, with <code>b</code> bits of precision. See equation 2.6 in the
   * OpenGL 3.1 standard for the derivation of this function.
   * 
   * @return A signed, normalized fixed-point value.
   * @param c
   *          The value.
   * @param b
   *          The number of bits of precision.
   * @throws RangeCheckException
   *           Iff <code>2 <= b < 32 == false</code>.
   */

  public static int doubleToSignedNormalized(
    final double c,
    final int b)
    throws RangeCheckException
  {
    RangeCheck.checkGreater(b, "Bit count", 1, "Minimum bit count");
    RangeCheck.checkLess(b, "Bit count", 32, "Maximum bit count");
    final double cc = Math.max(Math.min(c, 1.0), -1.0);
    return (int) (cc * (Math.pow(2, b - 1) - 1));
  }

  /**
   * Conversion of a floating point value to a signed normalized fixed-point
   * value, with <code>b</code> bits of precision. See equation 2.6 in the
   * OpenGL 3.1 standard for the derivation of this function.
   * 
   * @return A signed, normalized fixed-point value.
   * @param c
   *          The value.
   * @param b
   *          The number of bits of precision.
   * @throws RangeCheckException
   *           Iff <code>2 <= b < 64 == false</code>.
   */

  public static long doubleToSignedNormalizedLong(
    final double c,
    final int b)
    throws RangeCheckException
  {
    RangeCheck.checkGreater(b, "Bit count", 1, "Minimum bit count");
    RangeCheck.checkLess(b, "Bit count", 64, "Maximum bit count");
    final double cc = Math.max(Math.min(c, 1.0), -1.0);
    return (long) (cc * (Math.pow(2, b - 1) - 1));
  }

  /**
   * Conversion of a floating point value to an unsigned normalized
   * fixed-point value, with <code>b</code> bits of precision. See equation
   * 2.4 in the OpenGL 3.1 standard for the derivation of this function.
   * 
   * @return An unsigned, normalized fixed-point value.
   * @param c
   *          The value.
   * @param b
   *          The number of bits of precision.
   * @throws RangeCheckException
   *           Iff <code>2 <= b < 32 == false</code>.
   */

  public static int doubleToUnsignedNormalized(
    final double c,
    final int b)
    throws RangeCheckException
  {
    RangeCheck.checkGreater(b, "Bit count", 1, "Minimum bit count");
    RangeCheck.checkLess(b, "Bit count", 32, "Maximum bit count");
    return (int) (c * (Math.pow(2, b) - 1));
  }

  /**
   * Conversion of a floating point value to an unsigned normalized
   * fixed-point value, with <code>b</code> bits of precision. See equation
   * 2.4 in the OpenGL 3.1 standard for the derivation of this function.
   * 
   * @return An unsigned, normalized fixed-point value.
   * @param c
   *          The value.
   * @param b
   *          The number of bits of precision.
   * @throws RangeCheckException
   *           Iff <code>2 <= b < 64 == false</code>.
   */

  public static long doubleToUnsignedNormalizedLong(
    final double c,
    final int b)
    throws RangeCheckException
  {
    RangeCheck.checkGreater(b, "Bit count", 1, "Minimum bit count");
    RangeCheck.checkLess(b, "Bit count", 64, "Maximum bit count");
    return (long) (c * (Math.pow(2, b) - 1));
  }

  /**
   * Conversion of a floating point value to a signed normalized fixed-point
   * value, with <code>b</code> bits of precision. See equation 2.6 in the
   * OpenGL 3.1 standard for the derivation of this function.
   * 
   * @return A signed, normalized fixed-point value.
   * @param c
   *          The value.
   * @param b
   *          The number of bits of precision.
   * @throws RangeCheckException
   *           Iff <code>2 <= b < 32 == false</code>.
   */

  public static int floatToSignedNormalized(
    final float c,
    final int b)
    throws RangeCheckException
  {
    RangeCheck.checkGreater(b, "Bit count", 1, "Minimum bit count");
    RangeCheck.checkLess(b, "Bit count", 32, "Maximum bit count");
    return (int) (c * (Math.pow(2, b - 1) - 1));
  }

  /**
   * Conversion of a floating point value to a signed normalized fixed-point
   * value, with <code>b</code> bits of precision. See equation 2.6 in the
   * OpenGL 3.1 standard for the derivation of this function.
   * 
   * @return A signed, normalized fixed-point value.
   * @param c
   *          The value.
   * @param b
   *          The number of bits of precision.
   * @throws RangeCheckException
   *           Iff <code>2 <= b < 64 == false</code>.
   */

  public static long floatToSignedNormalizedLong(
    final float c,
    final int b)
    throws RangeCheckException
  {
    RangeCheck.checkGreater(b, "Bit count", 1, "Minimum bit count");
    RangeCheck.checkLess(b, "Bit count", 64, "Maximum bit count");
    return (long) (c * (Math.pow(2, b - 1) - 1));
  }

  /**
   * Conversion of a floating point value to an unsigned normalized
   * fixed-point value, with <code>b</code> bits of precision. See equation
   * 2.4 in the OpenGL 3.1 standard for the derivation of this function.
   * 
   * @return An unsigned, normalized fixed-point value.
   * @param c
   *          The value.
   * @param b
   *          The number of bits of precision.
   * @throws RangeCheckException
   *           Iff <code>2 <= b < 32 == false</code>.
   */

  public static int floatToUnsignedNormalized(
    final float c,
    final int b)
    throws RangeCheckException
  {
    RangeCheck.checkGreater(b, "Bit count", 1, "Minimum bit count");
    RangeCheck.checkLess(b, "Bit count", 32, "Maximum bit count");
    return (int) (c * (Math.pow(2, b) - 1));
  }

  /**
   * Conversion of a floating point value to an unsigned normalized
   * fixed-point value, with <code>b</code> bits of precision. See equation
   * 2.4 in the OpenGL 3.1 standard for the derivation of this function.
   * 
   * @return An unsigned, normalized fixed-point value.
   * @param c
   *          The value.
   * @param b
   *          The number of bits of precision.
   * @throws RangeCheckException
   *           Iff <code>2 <= b < 64 == false</code>.
   */

  public static long floatToUnsignedNormalizedLong(
    final float c,
    final int b)
    throws RangeCheckException
  {
    RangeCheck.checkGreater(b, "Bit count", 1, "Minimum bit count");
    RangeCheck.checkLess(b, "Bit count", 64, "Maximum bit count");
    return (long) (c * (Math.pow(2, b) - 1));
  }

  /**
   * Conversion of an signed normalized fixed point value <code>c</code>,
   * assuming that <code>b</code> bits of <code>c</code> are significant, to
   * floating point. See equation 2.3 in the OpenGL 3.1 standard for the
   * derivation of this function.
   * 
   * @return A double-precision value.
   * @param c
   *          The value.
   * @param b
   *          The number of bits of precision.
   * @throws RangeCheckException
   *           Iff <code>2 <= b < 32 == false</code>.
   */

  public static double signedNormalizedFixedToDouble(
    final int b,
    final int c)
    throws RangeCheckException
  {
    RangeCheck.checkGreater(b, "Bit count", 1, "Minimum bit count");
    RangeCheck.checkLess(b, "Bit count", 32, "Maximum bit count");
    final double fb = b;
    final double fc = c;
    final double r = fc / (Math.pow(2, fb - 1) - 1);
    return Math.max(r, -1.0);
  }

  /**
   * Conversion of an signed normalized fixed point value <code>c</code>,
   * assuming that <code>b</code> bits of <code>c</code> are significant, to
   * floating point. See equation 2.3 in the OpenGL 3.1 standard for the
   * derivation of this function.
   * 
   * @return A double-precision value.
   * @param c
   *          The value.
   * @param b
   *          The number of bits of precision.
   * @throws RangeCheckException
   *           Iff <code>2 <= b < 64 == false</code>.
   */

  public static double signedNormalizedFixedToDoubleLong(
    final int b,
    final long c)
    throws RangeCheckException
  {
    RangeCheck.checkGreater(b, "Bit count", 1, "Minimum bit count");
    RangeCheck.checkLess(b, "Bit count", 64, "Maximum bit count");
    final double fb = b;
    final double fc = c;
    final double r = fc / (Math.pow(2, fb - 1) - 1);
    return Math.max(r, -1.0);
  }

  /**
   * Conversion of an signed normalized fixed point value <code>c</code>,
   * assuming that <code>b</code> bits of <code>c</code> are significant, to
   * floating point. See equation 2.3 in the OpenGL 3.1 standard for the
   * derivation of this function.
   * 
   * @return A single-precision value.
   * @param c
   *          The value.
   * @param b
   *          The number of bits of precision.
   * @throws RangeCheckException
   *           Iff <code>2 <= b < 32 == false</code>.
   */

  public static float signedNormalizedFixedToFloat(
    final int b,
    final int c)
    throws RangeCheckException
  {
    RangeCheck.checkGreater(b, "Bit count", 1, "Minimum bit count");
    RangeCheck.checkLess(b, "Bit count", 32, "Maximum bit count");
    final float fb = b;
    final float fc = c;
    final float r = (float) (fc / (Math.pow(2, fb - 1) - 1));
    return Math.max(r, -1.0f);
  }

  /**
   * Conversion of an signed normalized fixed point value <code>c</code>,
   * assuming that <code>b</code> bits of <code>c</code> are significant, to
   * floating point. See equation 2.3 in the OpenGL 3.1 standard for the
   * derivation of this function.
   * 
   * @return A single-precision value.
   * @param c
   *          The value.
   * @param b
   *          The number of bits of precision.
   * @throws RangeCheckException
   *           Iff <code>2 <= b < 64 == false</code>.
   */

  public static float signedNormalizedFixedToFloatLong(
    final int b,
    final long c)
    throws RangeCheckException
  {
    RangeCheck.checkGreater(b, "Bit count", 1, "Minimum bit count");
    RangeCheck.checkLess(b, "Bit count", 64, "Maximum bit count");
    final float fb = b;
    final float fc = c;
    final float r = (float) (fc / (Math.pow(2, fb - 1) - 1));
    return Math.max(r, -1.0f);
  }

  /**
   * Conversion of an unsigned normalized fixed point value <code>c</code>,
   * assuming that <code>b</code> bits of <code>c</code> are significant, to
   * floating point. See equation 2.1 in the OpenGL 3.1 standard for the
   * derivation of this function.
   * 
   * @return A double-precision value.
   * @param c
   *          The value.
   * @param b
   *          The number of bits of precision.
   * @throws RangeCheckException
   *           Iff <code>2 <= b < 32 == false</code>.
   */

  public static double unsignedNormalizedFixedToDouble(
    final int b,
    final int c)
    throws RangeCheckException
  {
    RangeCheck.checkGreater(b, "Bit count", 1, "Minimum bit count");
    RangeCheck.checkLess(b, "Bit count", 32, "Maximum bit count");
    final double fb = b;
    final double fc = c;
    return fc / (Math.pow(2, fb) - 1);
  }

  /**
   * Conversion of an unsigned normalized fixed point value <code>c</code>,
   * assuming that <code>b</code> bits of <code>c</code> are significant, to
   * floating point. See equation 2.1 in the OpenGL 3.1 standard for the
   * derivation of this function.
   * 
   * @return A double-precision value.
   * @param c
   *          The value.
   * @param b
   *          The number of bits of precision.
   * @throws RangeCheckException
   *           Iff <code>2 <= b < 64 == false</code>.
   */

  public static double unsignedNormalizedFixedToDoubleLong(
    final int b,
    final long c)
    throws RangeCheckException
  {
    RangeCheck.checkGreater(b, "Bit count", 1, "Minimum bit count");
    RangeCheck.checkLess(b, "Bit count", 64, "Maximum bit count");
    final double fb = b;
    final double fc = c;
    return fc / (Math.pow(2, fb) - 1);
  }

  /**
   * Conversion of an unsigned normalized fixed point value <code>c</code>,
   * assuming that <code>b</code> bits of <code>c</code> are significant, to
   * floating point. See equation 2.1 in the OpenGL 3.1 standard for the
   * derivation of this function.
   * 
   * @return A single-precision value.
   * @param c
   *          The value.
   * @param b
   *          The number of bits of precision.
   * @throws RangeCheckException
   *           Iff <code>2 <= b < 32 == false</code>.
   */

  public static float unsignedNormalizedFixedToFloat(
    final int b,
    final int c)
    throws RangeCheckException
  {
    RangeCheck.checkGreater(b, "Bit count", 1, "Minimum bit count");
    RangeCheck.checkLess(b, "Bit count", 32, "Maximum bit count");
    final float fb = b;
    final float fc = c;
    return (float) (fc / (Math.pow(2, fb) - 1));
  }

  /**
   * Conversion of an unsigned normalized fixed point value <code>c</code>,
   * assuming that <code>b</code> bits of <code>c</code> are significant, to
   * floating point. See equation 2.1 in the OpenGL 3.1 standard for the
   * derivation of this function.
   * 
   * @return A single-precision value.
   * @param c
   *          The value.
   * @param b
   *          The number of bits of precision.
   * @throws RangeCheckException
   *           Iff <code>2 <= b < 64 == false</code>.
   */

  public static float unsignedNormalizedFixedToFloatLong(
    final int b,
    final long c)
    throws RangeCheckException
  {
    RangeCheck.checkGreater(b, "Bit count", 1, "Minimum bit count");
    RangeCheck.checkLess(b, "Bit count", 64, "Maximum bit count");
    final float fb = b;
    final float fc = c;
    return (float) (fc / (Math.pow(2, fb) - 1));
  }

  private FixedPoint()
  {
    throw new UnreachableCodeException();
  }
}
