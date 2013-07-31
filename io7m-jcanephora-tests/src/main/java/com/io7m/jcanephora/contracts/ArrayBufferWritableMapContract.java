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
import java.nio.FloatBuffer;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.AlmostEqualFloat;
import com.io7m.jaux.AlmostEqualFloat.ContextRelative;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.ArrayBuffer;
import com.io7m.jcanephora.ArrayBufferAttributeDescriptor;
import com.io7m.jcanephora.ArrayBufferTypeDescriptor;
import com.io7m.jcanephora.ArrayBufferWritableMap;
import com.io7m.jcanephora.CursorWritable2f;
import com.io7m.jcanephora.CursorWritable3f;
import com.io7m.jcanephora.JCGLArrayBuffers;
import com.io7m.jcanephora.JCGLArrayBuffersMapped;
import com.io7m.jcanephora.JCGLErrorCodes;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLScalarType;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.UsageHint;

public abstract class ArrayBufferWritableMapContract implements TestContract
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

  /*
   * Cursor3f
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferMapCursor2fNull()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMapped gm = this.getGLArrayBuffersMapped(tc);

    ArrayBuffer a = null;
    ArrayBufferWritableMap m = null;

    try {
      final ArrayBufferTypeDescriptor d =
        new ArrayBufferTypeDescriptor(
          new ArrayBufferAttributeDescriptor[] { new ArrayBufferAttributeDescriptor(
            "position",
            JCGLScalarType.TYPE_FLOAT,
            3) });

      a = ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
      m = gm.arrayBufferMapWrite(a);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert m != null;
    m.getCursor2f(null);
  }

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferMapCursor2fOutOfRange()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMapped gm = this.getGLArrayBuffersMapped(tc);

    ArrayBuffer a = null;
    ArrayBufferWritableMap m = null;

    try {
      final ArrayBufferTypeDescriptor d =
        new ArrayBufferTypeDescriptor(new ArrayBufferAttributeDescriptor[] {
          new ArrayBufferAttributeDescriptor(
            "position",
            JCGLScalarType.TYPE_FLOAT,
            3),
          new ArrayBufferAttributeDescriptor(
            "shape",
            JCGLScalarType.TYPE_INT,
            1) });

      a = ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
      m = gm.arrayBufferMapWrite(a);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert m != null;
    final CursorWritable2f c = m.getCursor2f("position");
    c.seekTo(10);
  }

  @Test public final void testArrayBufferMapCursor2fSeekCorrect()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMapped gm = this.getGLArrayBuffersMapped(tc);

    ArrayBuffer a = null;
    ArrayBufferWritableMap wm = null;

    try {
      final ArrayBufferTypeDescriptor d =
        new ArrayBufferTypeDescriptor(new ArrayBufferAttributeDescriptor[] {
          new ArrayBufferAttributeDescriptor(
            "position",
            JCGLScalarType.TYPE_FLOAT,
            3),
          new ArrayBufferAttributeDescriptor(
            "normal",
            JCGLScalarType.TYPE_FLOAT,
            3),
          new ArrayBufferAttributeDescriptor(
            "uv",
            JCGLScalarType.TYPE_FLOAT,
            2) });

      a = ga.arrayBufferAllocate(3, d, UsageHint.USAGE_STATIC_DRAW);
      wm = gm.arrayBufferMapWrite(a);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert wm != null;

    final CursorWritable3f pp = wm.getCursor3f("position");
    final CursorWritable3f pn = wm.getCursor3f("normal");
    final CursorWritable2f pu = wm.getCursor2f("uv");

    pp.put3f(1.0f, 1.0f, 1.0f);
    pp.put3f(1.0f, 1.0f, 1.0f);
    pp.put3f(1.0f, 1.0f, 1.0f);

    pn.put3f(2.0f, 2.0f, 2.0f);
    pn.put3f(2.0f, 2.0f, 2.0f);
    pn.put3f(2.0f, 2.0f, 2.0f);

    pu.put2f(0.0f, 0.0f);
    pu.put2f(0.0f, 0.0f);
    pu.put2f(0.0f, 0.0f);

    pu.seekTo(1);
    pu.put2f(9.0f, 9.0f);

    gm.arrayBufferUnmap(a);

    final ByteBuffer b = gm.arrayBufferMapReadUntyped(a);
    final FloatBuffer fb = b.asFloatBuffer();
    final ContextRelative rc = new AlmostEqualFloat.ContextRelative();

    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 1.0f, fb.get(0)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 1.0f, fb.get(1)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 1.0f, fb.get(2)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 2.0f, fb.get(3)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 2.0f, fb.get(4)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 2.0f, fb.get(5)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, fb.get(6)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, fb.get(7)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 1.0f, fb.get(8)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 1.0f, fb.get(9)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 1.0f, fb.get(10)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 2.0f, fb.get(11)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 2.0f, fb.get(12)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 2.0f, fb.get(13)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 9.0f, fb.get(14)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 9.0f, fb.get(15)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 1.0f, fb.get(16)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 1.0f, fb.get(17)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 1.0f, fb.get(18)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 2.0f, fb.get(19)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 2.0f, fb.get(20)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 2.0f, fb.get(21)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, fb.get(22)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, fb.get(23)));
  }

  @Test public final void testArrayBufferMapCursor2fWriteCorrect()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMapped gm = this.getGLArrayBuffersMapped(tc);

    ArrayBuffer a = null;
    ArrayBufferWritableMap wm = null;

    try {
      final ArrayBufferTypeDescriptor d =
        new ArrayBufferTypeDescriptor(new ArrayBufferAttributeDescriptor[] {
          new ArrayBufferAttributeDescriptor(
            "position",
            JCGLScalarType.TYPE_FLOAT,
            3),
          new ArrayBufferAttributeDescriptor(
            "normal",
            JCGLScalarType.TYPE_FLOAT,
            3),
          new ArrayBufferAttributeDescriptor(
            "uv",
            JCGLScalarType.TYPE_FLOAT,
            2) });

      a = ga.arrayBufferAllocate(3, d, UsageHint.USAGE_STATIC_DRAW);
      wm = gm.arrayBufferMapWrite(a);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert wm != null;

    final CursorWritable3f pp = wm.getCursor3f("position");
    final CursorWritable3f pn = wm.getCursor3f("normal");
    final CursorWritable2f pu = wm.getCursor2f("uv");

    pp.put3f(1.0f, 2.0f, 3.0f);
    pp.put3f(4.0f, 5.0f, 6.0f);
    pp.put3f(7.0f, 8.0f, 9.0f);

    pn.put3f(10.0f, 20.0f, 30.0f);
    pn.put3f(40.0f, 50.0f, 60.0f);
    pn.put3f(70.0f, 80.0f, 90.0f);

    pu.put2f(0.0f, 0.0f);
    pu.put2f(1.0f, 0.0f);
    pu.put2f(1.0f, 1.0f);

    gm.arrayBufferUnmap(a);

    final ByteBuffer b = gm.arrayBufferMapReadUntyped(a);
    final FloatBuffer fb = b.asFloatBuffer();
    final ContextRelative rc = new AlmostEqualFloat.ContextRelative();

    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 1.0f, fb.get(0)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 2.0f, fb.get(1)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 3.0f, fb.get(2)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 10.0f, fb.get(3)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 20.0f, fb.get(4)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 30.0f, fb.get(5)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, fb.get(6)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, fb.get(7)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 4.0f, fb.get(8)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 5.0f, fb.get(9)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 6.0f, fb.get(10)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 40.0f, fb.get(11)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 50.0f, fb.get(12)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 60.0f, fb.get(13)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 1.0f, fb.get(14)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, fb.get(15)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 7.0f, fb.get(16)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 8.0f, fb.get(17)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 9.0f, fb.get(18)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 70.0f, fb.get(19)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 80.0f, fb.get(20)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 90.0f, fb.get(21)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 1.0f, fb.get(22)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 1.0f, fb.get(23)));
  }

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferMapCursor2fWrongType()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMapped gm = this.getGLArrayBuffersMapped(tc);

    ArrayBuffer a = null;
    ArrayBufferWritableMap m = null;

    try {
      final ArrayBufferTypeDescriptor d =
        new ArrayBufferTypeDescriptor(new ArrayBufferAttributeDescriptor[] {
          new ArrayBufferAttributeDescriptor(
            "position",
            JCGLScalarType.TYPE_FLOAT,
            3),
          new ArrayBufferAttributeDescriptor(
            "shape",
            JCGLScalarType.TYPE_INT,
            1) });

      a = ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
      m = gm.arrayBufferMapWrite(a);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert m != null;
    m.getCursor2f("shape");
  }

  /*
   * Cursor2f
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferMapCursor3fNull()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMapped gm = this.getGLArrayBuffersMapped(tc);

    ArrayBuffer a = null;
    ArrayBufferWritableMap m = null;

    try {
      final ArrayBufferTypeDescriptor d =
        new ArrayBufferTypeDescriptor(
          new ArrayBufferAttributeDescriptor[] { new ArrayBufferAttributeDescriptor(
            "position",
            JCGLScalarType.TYPE_FLOAT,
            3) });

      a = ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
      m = gm.arrayBufferMapWrite(a);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert m != null;
    m.getCursor3f(null);
  }

  @Test public final void testArrayBufferMapCursor3fWriteCorrect()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMapped gm = this.getGLArrayBuffersMapped(tc);

    ArrayBuffer a = null;
    ArrayBufferWritableMap wm = null;

    try {
      final ArrayBufferTypeDescriptor d =
        new ArrayBufferTypeDescriptor(new ArrayBufferAttributeDescriptor[] {
          new ArrayBufferAttributeDescriptor(
            "position",
            JCGLScalarType.TYPE_FLOAT,
            3),
          new ArrayBufferAttributeDescriptor(
            "normal",
            JCGLScalarType.TYPE_FLOAT,
            3) });

      a = ga.arrayBufferAllocate(3, d, UsageHint.USAGE_STATIC_DRAW);
      wm = gm.arrayBufferMapWrite(a);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert wm != null;

    final CursorWritable3f pp = wm.getCursor3f("position");
    final CursorWritable3f pn = wm.getCursor3f("normal");

    pp.put3f(1.0f, 2.0f, 3.0f);
    pp.put3f(4.0f, 5.0f, 6.0f);
    pp.put3f(7.0f, 8.0f, 9.0f);

    pn.put3f(10.0f, 20.0f, 30.0f);
    pn.put3f(40.0f, 50.0f, 60.0f);
    pn.put3f(70.0f, 80.0f, 90.0f);

    gm.arrayBufferUnmap(a);

    final ByteBuffer b = gm.arrayBufferMapReadUntyped(a);
    final FloatBuffer fb = b.asFloatBuffer();
    final ContextRelative rc = new AlmostEqualFloat.ContextRelative();

    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 1.0f, fb.get(0)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 2.0f, fb.get(1)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 3.0f, fb.get(2)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 10.0f, fb.get(3)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 20.0f, fb.get(4)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 30.0f, fb.get(5)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 4.0f, fb.get(6)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 5.0f, fb.get(7)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 6.0f, fb.get(8)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 40.0f, fb.get(9)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 50.0f, fb.get(10)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 60.0f, fb.get(11)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 7.0f, fb.get(12)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 8.0f, fb.get(13)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 9.0f, fb.get(14)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 70.0f, fb.get(15)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 80.0f, fb.get(16)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 90.0f, fb.get(17)));
  }

  @Test public final void testArrayBufferMapCursor3fWriteSeekCorrect()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMapped gm = this.getGLArrayBuffersMapped(tc);

    ArrayBuffer a = null;
    ArrayBufferWritableMap wm = null;

    try {
      final ArrayBufferTypeDescriptor d =
        new ArrayBufferTypeDescriptor(new ArrayBufferAttributeDescriptor[] {
          new ArrayBufferAttributeDescriptor(
            "position",
            JCGLScalarType.TYPE_FLOAT,
            3),
          new ArrayBufferAttributeDescriptor(
            "normal",
            JCGLScalarType.TYPE_FLOAT,
            3) });

      a = ga.arrayBufferAllocate(3, d, UsageHint.USAGE_STATIC_DRAW);
      wm = gm.arrayBufferMapWrite(a);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert wm != null;

    final CursorWritable3f pp = wm.getCursor3f("position");
    final CursorWritable3f pn = wm.getCursor3f("normal");

    pp.put3f(0.0f, 0.0f, 0.0f);
    pp.put3f(0.0f, 0.0f, 0.0f);
    pp.put3f(0.0f, 0.0f, 0.0f);

    pp.seekTo(1);
    pp.put3f(23.0f, 24.0f, 25.0f);

    pn.put3f(0.0f, 0.0f, 0.0f);
    pn.put3f(0.0f, 0.0f, 0.0f);
    pn.put3f(0.0f, 0.0f, 0.0f);

    gm.arrayBufferUnmap(a);

    final ByteBuffer b = gm.arrayBufferMapReadUntyped(a);
    final FloatBuffer fb = b.asFloatBuffer();
    final ContextRelative rc = new AlmostEqualFloat.ContextRelative();

    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, fb.get(0)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, fb.get(1)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, fb.get(2)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, fb.get(3)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, fb.get(4)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, fb.get(5)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 23.0f, fb.get(6)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 24.0f, fb.get(7)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 25.0f, fb.get(8)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, fb.get(9)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, fb.get(10)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, fb.get(11)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, fb.get(12)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, fb.get(13)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, fb.get(14)));

    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, fb.get(15)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, fb.get(16)));
    Assert.assertTrue(AlmostEqualFloat.almostEqual(rc, 0.0f, fb.get(17)));
  }

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferMapCursor3fWrongType()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final JCGLArrayBuffersMapped gm = this.getGLArrayBuffersMapped(tc);

    ArrayBuffer a = null;
    ArrayBufferWritableMap m = null;

    try {
      final ArrayBufferTypeDescriptor d =
        new ArrayBufferTypeDescriptor(new ArrayBufferAttributeDescriptor[] {
          new ArrayBufferAttributeDescriptor(
            "position",
            JCGLScalarType.TYPE_FLOAT,
            3),
          new ArrayBufferAttributeDescriptor(
            "shape",
            JCGLScalarType.TYPE_INT,
            1) });

      a = ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
      m = gm.arrayBufferMapWrite(a);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert m != null;
    m.getCursor3f("shape");
  }
}
