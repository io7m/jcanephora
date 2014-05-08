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

package com.io7m.jcanephora.tests;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.AlmostEqualDouble;
import com.io7m.jaux.AlmostEqualDouble.ContextRelative;
import com.io7m.jaux.AlmostEqualFloat;
import com.io7m.jcanephora.FixedPoint;

@SuppressWarnings("static-method") public class FixedPointTest
{
  @Test public void testDoubleSigned()
  {
    for (int b = 2; b < 32; ++b) {
      Assert.assertEquals(
        1.0f,
        FixedPoint.signedNormalizedFixedToDouble(
          b,
          FixedPoint.floatToSignedNormalized(1.0f, b)),
        0.0f);
      Assert.assertEquals(
        0.0f,
        FixedPoint.signedNormalizedFixedToDouble(
          b,
          FixedPoint.floatToSignedNormalized(0.0f, b)),
        0.0f);
      Assert.assertEquals(
        -1.0f,
        FixedPoint.signedNormalizedFixedToDouble(
          b,
          FixedPoint.floatToSignedNormalized(-1.0f, b)),
        0.0f);
    }
  }

  @Test public void testDoubleToSigned16()
  {
    Assert
      .assertEquals(0x7fff, FixedPoint.doubleToSignedNormalized(1.0f, 16));
    Assert.assertEquals(
      -0x7fff,
      FixedPoint.doubleToSignedNormalized(-1.0f, 16));
    Assert.assertEquals(0, FixedPoint.doubleToSignedNormalized(0.0f, 16));
  }

  @Test public void testDoubleToSigned24()
  {
    Assert.assertEquals(
      0x7fffff,
      FixedPoint.doubleToSignedNormalized(1.0f, 24));
    Assert.assertEquals(
      -0x7fffff,
      FixedPoint.doubleToSignedNormalized(-1.0f, 24));
    Assert.assertEquals(0, FixedPoint.doubleToSignedNormalized(0.0f, 24));
  }

  @Test public void testDoubleToSigned8()
  {
    Assert.assertEquals(0x7f, FixedPoint.doubleToSignedNormalized(1.0f, 8));
    Assert.assertEquals(-0x7f, FixedPoint.doubleToSignedNormalized(-1.0f, 8));
    Assert.assertEquals(0, FixedPoint.doubleToSignedNormalized(0.0f, 8));
  }

  @Test public void testDoubleToSignedIdentities()

  {
    final ContextRelative c = new ContextRelative();
    c.setMaxAbsoluteDifference(0.01);

    for (int b = 2; b < 32; ++b) {
      System.out.println("-- Bits: " + b);

      {
        double sum = 0.0;
        for (double d = -1.0; d <= 0.0; d += 0.01) {
          final int i = FixedPoint.doubleToSignedNormalized(d, b);
          final double e = FixedPoint.signedNormalizedFixedToDouble(b, i);

          sum += d;
          System.out.println("d: " + d + " e: " + e + " i: " + i);
        }

        final double r = sum / 51.0;
        System.out.println("Sum      : " + sum);
        System.out.println("Sum / 51 : " + r);
        Assert.assertTrue(AlmostEqualDouble.almostEqual(c, r, -1.0));
      }

      {
        double sum = 0.0;
        for (double d = 0.0; d <= 1.01; d += 0.01) {
          final int i = FixedPoint.doubleToSignedNormalized(d, b);
          final double e = FixedPoint.signedNormalizedFixedToDouble(b, i);

          sum += d;
          System.out.println("d: " + d + " e: " + e + " i: " + i);
        }

        final double r = sum / 51.0;
        System.out.println("Sum      : " + sum);
        System.out.println("Sum / 51 : " + r);
        Assert.assertTrue(AlmostEqualDouble.almostEqual(c, r, 1.0));
      }
    }
  }

  @Test public void testDoubleToSignedIdentitiesExtents()

  {
    for (int b = 2; b < 32; ++b) {
      System.out.println("-- Bits: " + b);
      {
        final int zm1 = FixedPoint.doubleToSignedNormalized(-1.0, b);
        final double em1 = FixedPoint.signedNormalizedFixedToDouble(b, zm1);
        Assert.assertEquals(em1, -1.0, 0.0);
      }

      {
        final int z0 = FixedPoint.doubleToSignedNormalized(0.0, b);
        final double e0 = FixedPoint.signedNormalizedFixedToDouble(b, z0);
        Assert.assertEquals(z0, 0);
        Assert.assertEquals(e0, 0.0, 0.0);
      }

      {
        final int z1 = FixedPoint.doubleToSignedNormalized(1.0, b);
        final double e1 = FixedPoint.signedNormalizedFixedToDouble(b, z1);
        Assert.assertEquals(e1, 1.0, 0.0);
      }
    }
  }

  @Test public void testDoubleToSignedIdentitiesExtentsLong()

  {
    for (int b = 2; b < 64; ++b) {
      System.out.println("-- Bits: " + b);
      {
        final long zm1 = FixedPoint.doubleToSignedNormalizedLong(-1.0, b);
        final double em1 =
          FixedPoint.signedNormalizedFixedToDoubleLong(b, zm1);
        System.out.println("zm1: " + zm1);
        System.out.println("em1: " + em1);
        Assert.assertEquals(em1, -1.0, 0.0);
      }

      {
        final long z0 = FixedPoint.doubleToSignedNormalizedLong(0.0, b);
        final double e0 = FixedPoint.signedNormalizedFixedToDoubleLong(b, z0);
        Assert.assertEquals(z0, 0);
        Assert.assertEquals(e0, 0.0, 0.0);
      }

      {
        final long z1 = FixedPoint.doubleToSignedNormalizedLong(1.0, b);
        final double e1 = FixedPoint.signedNormalizedFixedToDoubleLong(b, z1);
        Assert.assertEquals(e1, 1.0, 0.0);
      }
    }
  }

  @Test public void testDoubleToSignedLongIdentities()

  {
    final ContextRelative c = new ContextRelative();
    c.setMaxAbsoluteDifference(0.01);

    for (int b = 2; b < 64; ++b) {
      System.out.println("-- Bits: " + b);

      {
        double sum = 0.0;
        for (double d = -1.0; d <= 0.0; d += 0.01) {
          final long i = FixedPoint.doubleToSignedNormalizedLong(d, b);
          final double e = FixedPoint.signedNormalizedFixedToDoubleLong(b, i);

          sum += d;
          System.out.println("d: " + d + " e: " + e + " i: " + i);
        }

        final double r = sum / 51.0;
        System.out.println("Sum      : " + sum);
        System.out.println("Sum / 51 : " + r);
        Assert.assertTrue(AlmostEqualDouble.almostEqual(c, r, -1.0));
      }

      {
        double sum = 0.0;
        for (double d = 0.0; d <= 1.01; d += 0.01) {
          final long i = FixedPoint.doubleToSignedNormalizedLong(d, b);
          final double e = FixedPoint.signedNormalizedFixedToDoubleLong(b, i);

          sum += d;
          System.out.println("d: " + d + " e: " + e + " i: " + i);
        }

        final double r = sum / 51.0;
        System.out.println("Sum      : " + sum);
        System.out.println("Sum / 51 : " + r);
        Assert.assertTrue(AlmostEqualDouble.almostEqual(c, r, 1.0));
      }
    }
  }

  @Test public void testDoubleToUnsigned16()

  {
    Assert.assertEquals(
      0xffff,
      FixedPoint.doubleToUnsignedNormalized(1.0f, 16));
    Assert.assertEquals(0, FixedPoint.doubleToUnsignedNormalized(0.0f, 16));
  }

  @Test public void testDoubleToUnsigned24()

  {
    Assert.assertEquals(
      0xffffff,
      FixedPoint.doubleToUnsignedNormalized(1.0f, 24));
    Assert.assertEquals(0, FixedPoint.doubleToUnsignedNormalized(0.0f, 24));
  }

  @Test public void testDoubleToUnsigned8()

  {
    Assert.assertEquals(0xff, FixedPoint.doubleToUnsignedNormalized(1.0f, 8));
    Assert.assertEquals(0, FixedPoint.doubleToUnsignedNormalized(0.0f, 8));
  }

  @Test public void testDoubleToUnsignedIdentities()

  {
    final ContextRelative c = new ContextRelative();
    c.setMaxAbsoluteDifference(0.01);

    for (int b = 2; b < 32; ++b) {
      System.out.println("-- Bits: " + b);
      boolean reached_0 = false;
      boolean reached_05 = false;
      boolean reached_1 = false;

      double sum = 0.0;
      for (double d = 0.0; d <= 1.01; d += 0.01) {
        final int i = FixedPoint.doubleToUnsignedNormalized(d, b);
        final double e = FixedPoint.unsignedNormalizedFixedToDouble(b, i);

        if (e <= 0) {
          reached_0 = true;
        }
        if (e >= 0.5) {
          reached_05 = true;
        }
        if (e >= 1) {
          reached_1 = true;
        }

        sum += d;
        System.out.println("d: " + d + " e: " + e + " i: " + i);
      }

      final double r = sum / 51.0;
      System.out.println("Sum      : " + sum);
      System.out.println("Sum / 51 : " + r);
      Assert.assertTrue(AlmostEqualDouble.almostEqual(c, r, 1.0));
      Assert.assertTrue(reached_0);
      Assert.assertTrue(reached_05);
      Assert.assertTrue(reached_1);
    }
  }

  @Test public void testDoubleToUnsignedIdentitiesExtents()

  {
    for (int b = 2; b < 32; ++b) {
      System.out.println("-- Bits: " + b);
      final int z0 = FixedPoint.doubleToUnsignedNormalized(0.0, b);
      final double e0 = FixedPoint.unsignedNormalizedFixedToDouble(b, z0);
      final int z1 = FixedPoint.doubleToUnsignedNormalized(1.0, b);
      final double e1 = FixedPoint.unsignedNormalizedFixedToDouble(b, z1);
      Assert.assertEquals(z0, 0);
      Assert.assertEquals(e0, 0.0, 0.0);
      Assert.assertEquals(e1, 1.0, 0.0);
    }
  }

  @Test public void testDoubleToUnsignedIdentitiesExtentsLong()

  {
    for (int b = 2; b < 64; ++b) {
      System.out.println("-- Bits: " + b);
      final long z0 = FixedPoint.doubleToUnsignedNormalizedLong(0.0, b);
      final double e0 = FixedPoint.unsignedNormalizedFixedToDoubleLong(b, z0);
      final long z1 = FixedPoint.doubleToUnsignedNormalizedLong(1.0, b);
      final double e1 = FixedPoint.unsignedNormalizedFixedToDoubleLong(b, z1);
      Assert.assertEquals(z0, 0);
      Assert.assertEquals(e0, 0.0, 0.0);
      Assert.assertEquals(e1, 1.0, 0.0);
    }
  }

  @Test public void testDoubleToUnsignedLongIdentities()

  {
    final ContextRelative c = new ContextRelative();
    c.setMaxAbsoluteDifference(0.01);

    for (int b = 2; b < 64; ++b) {
      System.out.println("-- Bits: " + b);

      double sum = 0.0;
      for (double d = 0.0; d <= 1.01; d += 0.01) {
        final long i = FixedPoint.doubleToUnsignedNormalizedLong(d, b);
        final double e = FixedPoint.unsignedNormalizedFixedToDoubleLong(b, i);

        sum += d;
        System.out.println("d: " + d + " e: " + e + " i: " + i);
      }

      final double r = sum / 51.0;
      System.out.println("Sum      : " + sum);
      System.out.println("Sum / 51 : " + r);
      Assert.assertTrue(AlmostEqualDouble.almostEqual(c, r, 1.0));
    }
  }

  @Test public void testDoubleUnsigned()
  {
    for (int b = 2; b < 32; ++b) {
      Assert.assertEquals(
        1.0f,
        FixedPoint.unsignedNormalizedFixedToDouble(
          b,
          FixedPoint.floatToUnsignedNormalized(1.0f, b)),
        0.0f);
      Assert.assertEquals(
        0.0f,
        FixedPoint.unsignedNormalizedFixedToDouble(
          b,
          FixedPoint.floatToUnsignedNormalized(0.0f, b)),
        0.0f);
    }
  }

  @Test public void testFloatSigned()
  {
    for (int b = 2; b < 32; ++b) {
      Assert.assertEquals(
        1.0f,
        FixedPoint.signedNormalizedFixedToFloat(
          b,
          FixedPoint.floatToSignedNormalized(1.0f, b)),
        0.0f);
      Assert.assertEquals(
        0.0f,
        FixedPoint.signedNormalizedFixedToFloat(
          b,
          FixedPoint.floatToSignedNormalized(0.0f, b)),
        0.0f);
      Assert.assertEquals(
        -1.0f,
        FixedPoint.signedNormalizedFixedToFloat(
          b,
          FixedPoint.floatToSignedNormalized(-1.0f, b)),
        0.0f);
    }
  }

  @Test public void testFloatToSigned16()
  {
    Assert.assertEquals(0x7fff, FixedPoint.floatToSignedNormalized(1.0f, 16));
    Assert.assertEquals(
      -0x7fff,
      FixedPoint.floatToSignedNormalized(-1.0f, 16));
    Assert.assertEquals(0, FixedPoint.floatToSignedNormalized(0.0f, 16));
  }

  @Test public void testFloatToSigned24()
  {
    Assert.assertEquals(
      0x7fffff,
      FixedPoint.floatToSignedNormalized(1.0f, 24));
    Assert.assertEquals(
      -0x7fffff,
      FixedPoint.floatToSignedNormalized(-1.0f, 24));
    Assert.assertEquals(0, FixedPoint.floatToSignedNormalized(0.0f, 24));
  }

  @Test public void testFloatToSigned8()
  {
    Assert.assertEquals(0x7f, FixedPoint.floatToSignedNormalized(1.0f, 8));
    Assert.assertEquals(-0x7f, FixedPoint.floatToSignedNormalized(-1.0f, 8));
    Assert.assertEquals(0, FixedPoint.floatToSignedNormalized(0.0f, 8));
  }

  @Test public void testFloatToSignedIdentities()

  {
    final AlmostEqualFloat.ContextRelative c =
      new AlmostEqualFloat.ContextRelative();
    c.setMaxAbsoluteDifference(0.01f);

    for (int b = 2; b < 32; ++b) {
      System.out.println("-- Bits: " + b);

      {
        float sum = 0.0f;
        for (float d = -1.0f; d <= 0.0; d += 0.01) {
          final int i = FixedPoint.floatToSignedNormalized(d, b);
          final float e = FixedPoint.signedNormalizedFixedToFloat(b, i);

          sum += d;
          System.out.println("d: " + d + " e: " + e + " i: " + i);
        }

        final float r = sum / 51.0f;
        System.out.println("Sum      : " + sum);
        System.out.println("Sum / 51 : " + r);
        Assert.assertTrue(AlmostEqualFloat.almostEqual(c, r, -1.0f));
      }

      {
        float sum = 0.0f;
        for (float d = 0.0f; d <= 1.01; d += 0.01) {
          final int i = FixedPoint.floatToSignedNormalized(d, b);
          final float e = FixedPoint.signedNormalizedFixedToFloat(b, i);

          sum += d;
          System.out.println("d: " + d + " e: " + e + " i: " + i);
        }

        final float r = sum / 51.0f;
        System.out.println("Sum      : " + sum);
        System.out.println("Sum / 51 : " + r);
        Assert.assertTrue(AlmostEqualFloat.almostEqual(c, r, 1.0f));
      }
    }
  }

  @Test public void testFloatToSignedIdentitiesExtents()

  {
    for (int b = 2; b < 32; ++b) {
      System.out.println("-- Bits: " + b);
      {
        final int zm1 = FixedPoint.floatToSignedNormalized(-1.0f, b);
        final float em1 = FixedPoint.signedNormalizedFixedToFloat(b, zm1);
        Assert.assertEquals(em1, -1.0, 0.0);
      }

      {
        final int z0 = FixedPoint.floatToSignedNormalized(0.0f, b);
        final float e0 = FixedPoint.signedNormalizedFixedToFloat(b, z0);
        Assert.assertEquals(z0, 0);
        Assert.assertEquals(e0, 0.0, 0.0);
      }

      {
        final int z1 = FixedPoint.floatToSignedNormalized(1.0f, b);
        final float e1 = FixedPoint.signedNormalizedFixedToFloat(b, z1);
        Assert.assertEquals(e1, 1.0, 0.0);
      }
    }
  }

  @Test public void testFloatToSignedIdentitiesExtentsLong()

  {
    for (int b = 2; b < 64; ++b) {
      System.out.println("-- Bits: " + b);
      {
        final long zm1 = FixedPoint.floatToSignedNormalizedLong(-1.0f, b);
        final float em1 = FixedPoint.signedNormalizedFixedToFloatLong(b, zm1);
        System.out.println("zm1: " + zm1);
        System.out.println("em1: " + em1);
        Assert.assertEquals(em1, -1.0, 0.0);
      }

      {
        final long z0 = FixedPoint.floatToSignedNormalizedLong(0.0f, b);
        final float e0 = FixedPoint.signedNormalizedFixedToFloatLong(b, z0);
        Assert.assertEquals(z0, 0);
        Assert.assertEquals(e0, 0.0, 0.0);
      }

      {
        final long z1 = FixedPoint.floatToSignedNormalizedLong(1.0f, b);
        final float e1 = FixedPoint.signedNormalizedFixedToFloatLong(b, z1);
        Assert.assertEquals(e1, 1.0, 0.0);
      }
    }
  }

  @Test public void testFloatToSignedLongIdentities()
  {
    final AlmostEqualFloat.ContextRelative c =
      new AlmostEqualFloat.ContextRelative();
    c.setMaxAbsoluteDifference(0.01f);

    for (int b = 2; b < 64; ++b) {
      System.out.println("-- Bits: " + b);

      {
        float sum = 0.0f;
        for (float d = -1.0f; d <= 0.0; d += 0.01) {
          final long i = FixedPoint.floatToSignedNormalizedLong(d, b);
          final float e = FixedPoint.signedNormalizedFixedToFloatLong(b, i);

          sum += d;
          System.out.println("d: " + d + " e: " + e + " i: " + i);
        }

        final float r = sum / 51.0f;
        System.out.println("Sum      : " + sum);
        System.out.println("Sum / 51 : " + r);
        Assert.assertTrue(AlmostEqualFloat.almostEqual(c, r, -1.0f));
      }

      {
        float sum = 0.0f;
        for (float d = 0.0f; d <= 1.01; d += 0.01) {
          final long i = FixedPoint.floatToSignedNormalizedLong(d, b);
          final float e = FixedPoint.signedNormalizedFixedToFloatLong(b, i);

          sum += d;
          System.out.println("d: " + d + " e: " + e + " i: " + i);
        }

        final float r = sum / 51.0f;
        System.out.println("Sum      : " + sum);
        System.out.println("Sum / 51 : " + r);
        Assert.assertTrue(AlmostEqualFloat.almostEqual(c, r, 1.0f));
      }
    }
  }

  @Test public void testFloatToUnsigned16()
  {
    Assert.assertEquals(
      0xffff,
      FixedPoint.floatToUnsignedNormalized(1.0f, 16));
    Assert.assertEquals(0, FixedPoint.floatToUnsignedNormalized(0.0f, 16));
  }

  @Test public void testFloatToUnsigned24()
  {
    Assert.assertEquals(
      0xffffff,
      FixedPoint.floatToUnsignedNormalized(1.0f, 24));
    Assert.assertEquals(0, FixedPoint.floatToUnsignedNormalized(0.0f, 24));
  }

  @Test public void testFloatToUnsigned8()
  {
    Assert.assertEquals(0xff, FixedPoint.floatToUnsignedNormalized(1.0f, 8));
    Assert.assertEquals(0, FixedPoint.floatToUnsignedNormalized(0.0f, 8));
  }

  @Test public void testFloatToUnsignedIdentities()
  {
    final ContextRelative c = new ContextRelative();
    c.setMaxAbsoluteDifference(0.01);

    for (int b = 2; b < 32; ++b) {
      System.out.println("-- Bits: " + b);
      boolean reached_0 = false;
      boolean reached_05 = false;
      boolean reached_1 = false;

      double sum = 0.0;
      for (float f = 0.0f; f <= 1.01f; f += 0.01f) {
        final int i = FixedPoint.floatToUnsignedNormalized(f, b);
        final float e = FixedPoint.unsignedNormalizedFixedToFloat(b, i);

        if (e <= 0f) {
          reached_0 = true;
        }
        if (e >= 0.5f) {
          reached_05 = true;
        }
        if (e >= 1) {
          reached_1 = true;
        }

        sum += f;
        System.out.println("f: " + f + " e: " + e + " i: " + i);
      }

      final double r = sum / 51.0;
      System.out.println("Sum      : " + sum);
      System.out.println("Sum / 51 : " + r);
      Assert.assertTrue(AlmostEqualDouble.almostEqual(c, r, 1.0));
      Assert.assertTrue(reached_0);
      Assert.assertTrue(reached_05);
      Assert.assertTrue(reached_1);
    }
  }

  @Test public void testFloatToUnsignedIdentitiesExtents()
  {
    for (int b = 2; b < 32; ++b) {
      System.out.println("-- Bits: " + b);
      final int z0 = FixedPoint.floatToUnsignedNormalized(0.0f, b);
      final float e0 = FixedPoint.unsignedNormalizedFixedToFloat(b, z0);
      final int z1 = FixedPoint.floatToUnsignedNormalized(1.0f, b);
      final float e1 = FixedPoint.unsignedNormalizedFixedToFloat(b, z1);
      Assert.assertEquals(z0, 0);
      Assert.assertEquals(e0, 0.0f, 0.0f);
      Assert.assertEquals(e1, 1.0f, 0.0f);
    }
  }

  @Test public void testFloatToUnsignedIdentitiesExtentsLong()
  {
    for (int b = 2; b < 32; ++b) {
      System.out.println("-- Bits: " + b);
      final long z0 = FixedPoint.floatToUnsignedNormalizedLong(0.0f, b);
      final float e0 = FixedPoint.unsignedNormalizedFixedToFloatLong(b, z0);
      final long z1 = FixedPoint.floatToUnsignedNormalizedLong(1.0f, b);
      final float e1 = FixedPoint.unsignedNormalizedFixedToFloatLong(b, z1);
      Assert.assertEquals(z0, 0);
      Assert.assertEquals(e0, 0.0f, 0.0f);
      Assert.assertEquals(e1, 1.0f, 0.0f);
    }
  }

  @Test public void testFloatToUnsignedLongIdentities()
  {
    final ContextRelative c = new ContextRelative();
    c.setMaxAbsoluteDifference(0.01);

    for (int b = 2; b < 64; ++b) {
      System.out.println("-- Bits: " + b);
      boolean reached_0 = false;
      boolean reached_05 = false;
      boolean reached_1 = false;

      double sum = 0.0;
      for (float f = 0.0f; f <= 1.01f; f += 0.01f) {
        final long i = FixedPoint.floatToUnsignedNormalizedLong(f, b);
        final float e = FixedPoint.unsignedNormalizedFixedToFloatLong(b, i);

        if (e <= 0f) {
          reached_0 = true;
        }
        if (e >= 0.5f) {
          reached_05 = true;
        }
        if (e >= 1) {
          reached_1 = true;
        }

        sum += f;
        System.out.println("f: " + f + " e: " + e + " i: " + i);
      }

      final double r = sum / 51.0;
      System.out.println("Sum      : " + sum);
      System.out.println("Sum / 51 : " + r);
      Assert.assertTrue(AlmostEqualDouble.almostEqual(c, r, 1.0));
      Assert.assertTrue(reached_0);
      Assert.assertTrue(reached_05);
      Assert.assertTrue(reached_1);
    }
  }

  @Test public void testFloatUnsigned()
  {
    for (int b = 2; b < 32; ++b) {
      Assert.assertEquals(
        1.0f,
        FixedPoint.unsignedNormalizedFixedToFloat(
          b,
          FixedPoint.floatToUnsignedNormalized(1.0f, b)),
        0.0f);
      Assert.assertEquals(
        0.0f,
        FixedPoint.unsignedNormalizedFixedToFloat(
          b,
          FixedPoint.floatToUnsignedNormalized(0.0f, b)),
        0.0f);
    }
  }

}
