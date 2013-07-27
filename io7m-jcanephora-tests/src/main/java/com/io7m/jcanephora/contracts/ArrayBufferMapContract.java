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
package com.io7m.jcanephora.contracts;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;
import com.io7m.jcanephora.ArrayBuffer;
import com.io7m.jcanephora.ArrayBufferAttributeDescriptor;
import com.io7m.jcanephora.ArrayBufferTypeDescriptor;
import com.io7m.jcanephora.ArrayBufferWritableMap;
import com.io7m.jcanephora.JCGLArrayBuffers;
import com.io7m.jcanephora.JCGLArrayBuffersMapped;
import com.io7m.jcanephora.JCGLErrorCodes;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLScalarType;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.UsageHint;

public abstract class ArrayBufferMapContract implements TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract JCGLArrayBuffers getGLArrayBuffers(
    final TestContext tc);

  public abstract JCGLArrayBuffersMapped getGLArrayBuffersMapped(
    final TestContext tc);

  public abstract JCGLErrorCodes getGLErrorCodes(
    TestContext tc);

  /**
   * Trying to map an out-of-range area in a buffer is an error.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferMapOutOfRange()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMapped gm = this.getGLArrayBuffersMapped(tc);

    final ArrayBufferTypeDescriptor d =
      new ArrayBufferTypeDescriptor(
        new ArrayBufferAttributeDescriptor[] { new ArrayBufferAttributeDescriptor(
          "position",
          JCGLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a =
      ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);

    gm.arrayBufferMapReadUntypedRange(a, new RangeInclusive(11, 11));
  }

  /**
   * Mapping a buffer works.
   */

  @Test public final void testArrayBufferMapped()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMapped gm = this.getGLArrayBuffersMapped(tc);

    final ArrayBufferTypeDescriptor d =
      new ArrayBufferTypeDescriptor(
        new ArrayBufferAttributeDescriptor[] { new ArrayBufferAttributeDescriptor(
          "position",
          JCGLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a =
      ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);

    try {
      try {
        final ArrayBufferWritableMap b = gm.arrayBufferMapWrite(a);

        final ByteBuffer bb = b.getByteBuffer();
        Assert.assertEquals(20, bb.capacity());

        final ShortBuffer s = bb.asShortBuffer();
        for (int index = 0; index < 10; ++index) {
          s.put(index, (short) index);
        }
      } finally {
        gm.arrayBufferUnmap(a);
      }

      try {
        final ByteBuffer b = gm.arrayBufferMapReadUntyped(a);
        final ShortBuffer s = b.asShortBuffer();
        for (int index = 0; index < 10; ++index) {
          Assert.assertEquals(index, s.get(index));
        }
      } finally {
        gm.arrayBufferUnmap(a);
      }
    } finally {
      if (a != null) {
        ga.arrayBufferDelete(a);
      }
    }
  }

  /**
   * A mapped buffer has the correct range.
   */

  @Test public final void testArrayBufferMapRange()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMapped gm = this.getGLArrayBuffersMapped(tc);

    final ArrayBufferTypeDescriptor d =
      new ArrayBufferTypeDescriptor(
        new ArrayBufferAttributeDescriptor[] { new ArrayBufferAttributeDescriptor(
          "position",
          JCGLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a =
      ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);

    try {
      try {
        final ByteBuffer b =
          gm.arrayBufferMapReadUntypedRange(a, new RangeInclusive(5, 8));
        Assert.assertEquals(4 * 2, b.capacity());
      } finally {
        gm.arrayBufferUnmap(a);
      }
    } finally {
      if (a != null) {
        ga.arrayBufferDelete(a);
      }
    }
  }

  /**
   * A mapped buffer has the correct bounds.
   */

  @Test(expected = IndexOutOfBoundsException.class) public final
    void
    testArrayBufferMapReadBounds()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMapped gm = this.getGLArrayBuffersMapped(tc);

    final ArrayBufferTypeDescriptor d =
      new ArrayBufferTypeDescriptor(
        new ArrayBufferAttributeDescriptor[] { new ArrayBufferAttributeDescriptor(
          "position",
          JCGLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a =
      ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);

    try {
      try {
        final ByteBuffer b = gm.arrayBufferMapReadUntyped(a);
        b.get(20);
      } finally {
        gm.arrayBufferUnmap(a);
      }
    } finally {
      if (a != null) {
        ga.arrayBufferDelete(a);
      }
    }
  }

  /**
   * Mapping a deleted buffer read-only fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferMapReadDeleted()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMapped gm = this.getGLArrayBuffersMapped(tc);

    final ArrayBufferTypeDescriptor d =
      new ArrayBufferTypeDescriptor(
        new ArrayBufferAttributeDescriptor[] { new ArrayBufferAttributeDescriptor(
          "position",
          JCGLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a =
      ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);

    ga.arrayBufferDelete(a);
    gm.arrayBufferMapReadUntyped(a);
  }

  /**
   * Mapping a buffer twice fails.
   */

  @Test(expected = JCGLException.class) public final
    void
    testArrayBufferMapReadDouble()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLErrorCodes ge = this.getGLErrorCodes(tc);
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMapped gm = this.getGLArrayBuffersMapped(tc);

    final ArrayBufferTypeDescriptor d =
      new ArrayBufferTypeDescriptor(
        new ArrayBufferAttributeDescriptor[] { new ArrayBufferAttributeDescriptor(
          "position",
          JCGLScalarType.TYPE_SHORT,
          1) });
    ArrayBuffer a = null;

    try {
      a = ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
    } catch (final JCGLException e) {
      Assert.fail(e.getMessage());
    }

    try {
      gm.arrayBufferMapReadUntyped(a);
      gm.arrayBufferMapReadUntyped(a);
    } catch (final JCGLException e) {
      Assert.assertTrue(ge.errorCodeIsInvalidOperation(e.getCode()));
      throw e;
    } finally {
      if (a != null) {
        ga.arrayBufferDelete(a);
      }
    }
  }

  /**
   * Mapping a null buffer fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferMapReadNull()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMapped gm = this.getGLArrayBuffersMapped(tc);

    final ArrayBufferTypeDescriptor d =
      new ArrayBufferTypeDescriptor(
        new ArrayBufferAttributeDescriptor[] { new ArrayBufferAttributeDescriptor(
          "position",
          JCGLScalarType.TYPE_SHORT,
          1) });
    ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
    gm.arrayBufferMapReadUntyped(null);
  }

  /**
   * A mapped buffer has the correct bounds.
   */

  @Test(expected = IndexOutOfBoundsException.class) public final
    void
    testArrayBufferMapWriteBounds()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMapped gm = this.getGLArrayBuffersMapped(tc);

    final ArrayBufferTypeDescriptor d =
      new ArrayBufferTypeDescriptor(
        new ArrayBufferAttributeDescriptor[] { new ArrayBufferAttributeDescriptor(
          "position",
          JCGLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a =
      ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);

    try {
      try {
        final ArrayBufferWritableMap map = gm.arrayBufferMapWrite(a);
        final ByteBuffer b = map.getByteBuffer();
        b.put(20, (byte) 0xff);
      } finally {
        gm.arrayBufferUnmap(a);
      }
    } finally {
      if (a != null) {
        ga.arrayBufferDelete(a);
      }
    }
  }

  /**
   * Mapping a deleted buffer write-only fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferMapWriteDeleted()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMapped gm = this.getGLArrayBuffersMapped(tc);

    final ArrayBufferTypeDescriptor d =
      new ArrayBufferTypeDescriptor(
        new ArrayBufferAttributeDescriptor[] { new ArrayBufferAttributeDescriptor(
          "position",
          JCGLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a =
      ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);

    ga.arrayBufferDelete(a);
    gm.arrayBufferMapWrite(a);
  }

  /**
   * Mapping a null buffer fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferMapWriteNull()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMapped gm = this.getGLArrayBuffersMapped(tc);

    final ArrayBufferTypeDescriptor d =
      new ArrayBufferTypeDescriptor(
        new ArrayBufferAttributeDescriptor[] { new ArrayBufferAttributeDescriptor(
          "position",
          JCGLScalarType.TYPE_SHORT,
          1) });
    ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
    gm.arrayBufferMapWrite(null);
  }

  /**
   * Unmapping a deleted buffer fails.
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferUnmapDeleted()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMapped gm = this.getGLArrayBuffersMapped(tc);

    final ArrayBufferTypeDescriptor d =
      new ArrayBufferTypeDescriptor(
        new ArrayBufferAttributeDescriptor[] { new ArrayBufferAttributeDescriptor(
          "position",
          JCGLScalarType.TYPE_SHORT,
          1) });
    final ArrayBuffer a =
      ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);

    gm.arrayBufferMapWrite(a);
    ga.arrayBufferDelete(a);
    gm.arrayBufferUnmap(a);
  }

  /**
   * Unmapping a buffer twice fails.
   */

  @Test(expected = JCGLException.class) public final
    void
    testArrayBufferUnmapDouble()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLErrorCodes ge = this.getGLErrorCodes(tc);
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMapped gm = this.getGLArrayBuffersMapped(tc);

    final ArrayBufferTypeDescriptor d =
      new ArrayBufferTypeDescriptor(
        new ArrayBufferAttributeDescriptor[] { new ArrayBufferAttributeDescriptor(
          "position",
          JCGLScalarType.TYPE_SHORT,
          1) });
    ArrayBuffer a = null;

    try {
      a = ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
      gm.arrayBufferMapWrite(a);
    } catch (final JCGLException e) {
      Assert.fail(e.getMessage());
    }

    try {
      gm.arrayBufferUnmap(a);
      gm.arrayBufferUnmap(a);
    } catch (final JCGLException e) {
      Assert.assertTrue(ge.errorCodeIsInvalidOperation(e.getCode()));
      throw e;
    } finally {
      if (a != null) {
        ga.arrayBufferDelete(a);
      }
    }
  }
}
