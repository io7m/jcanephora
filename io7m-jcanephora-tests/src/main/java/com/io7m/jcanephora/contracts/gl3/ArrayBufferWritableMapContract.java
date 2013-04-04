package com.io7m.jcanephora.contracts.gl3;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import junit.framework.Assert;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.ArrayBuffer;
import com.io7m.jcanephora.ArrayBufferAttribute;
import com.io7m.jcanephora.ArrayBufferDescriptor;
import com.io7m.jcanephora.ArrayBufferWritableMap;
import com.io7m.jcanephora.CursorWritable2f;
import com.io7m.jcanephora.CursorWritable3f;
import com.io7m.jcanephora.GLArrayBuffers;
import com.io7m.jcanephora.GLArrayBuffersMapped;
import com.io7m.jcanephora.GLErrorCodes;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLScalarType;
import com.io7m.jcanephora.GLUnsupportedException;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.UsageHint;
import com.io7m.jcanephora.contracts.common.TestContract;

public abstract class ArrayBufferWritableMapContract implements TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  abstract GLArrayBuffers getGLArrayBuffers(
    final TestContext tc);

  abstract GLArrayBuffersMapped getGLArrayBuffersMapped(
    final TestContext tc);

  abstract GLErrorCodes getGLErrorCodes(
    TestContext tc);

  /*
   * Cursor3f
   */

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferMapCursor2fNull()
      throws ConstraintError,
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final GLArrayBuffersMapped gm = this.getGLArrayBuffersMapped(tc);

    ArrayBuffer a = null;
    ArrayBufferWritableMap m = null;

    try {
      final ArrayBufferDescriptor d =
        new ArrayBufferDescriptor(
          new ArrayBufferAttribute[] { new ArrayBufferAttribute(
            "position",
            GLScalarType.TYPE_FLOAT,
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
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final GLArrayBuffersMapped gm = this.getGLArrayBuffersMapped(tc);

    ArrayBuffer a = null;
    ArrayBufferWritableMap m = null;

    try {
      final ArrayBufferDescriptor d =
        new ArrayBufferDescriptor(new ArrayBufferAttribute[] {
          new ArrayBufferAttribute("position", GLScalarType.TYPE_FLOAT, 3),
          new ArrayBufferAttribute("shape", GLScalarType.TYPE_INT, 1) });

      a = ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
      m = gm.arrayBufferMapWrite(a);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert m != null;
    final CursorWritable2f c = m.getCursor2f("position");
    c.seekTo(10);
  }

  @SuppressWarnings("boxing") @Test public final
    void
    testArrayBufferMapCursor2fSeekCorrect()
      throws ConstraintError,
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final GLArrayBuffersMapped gm = this.getGLArrayBuffersMapped(tc);

    ArrayBuffer a = null;
    ArrayBufferWritableMap wm = null;

    try {
      final ArrayBufferDescriptor d =
        new ArrayBufferDescriptor(new ArrayBufferAttribute[] {
          new ArrayBufferAttribute("position", GLScalarType.TYPE_FLOAT, 3),
          new ArrayBufferAttribute("normal", GLScalarType.TYPE_FLOAT, 3),
          new ArrayBufferAttribute("uv", GLScalarType.TYPE_FLOAT, 2) });

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

    final ByteBuffer b = gm.arrayBufferMapRead(a);
    final FloatBuffer fb = b.asFloatBuffer();

    Assert.assertEquals(1.0f, fb.get(0));
    Assert.assertEquals(1.0f, fb.get(1));
    Assert.assertEquals(1.0f, fb.get(2));
    Assert.assertEquals(2.0f, fb.get(3));
    Assert.assertEquals(2.0f, fb.get(4));
    Assert.assertEquals(2.0f, fb.get(5));
    Assert.assertEquals(0.0f, fb.get(6));
    Assert.assertEquals(0.0f, fb.get(7));

    Assert.assertEquals(1.0f, fb.get(8));
    Assert.assertEquals(1.0f, fb.get(9));
    Assert.assertEquals(1.0f, fb.get(10));
    Assert.assertEquals(2.0f, fb.get(11));
    Assert.assertEquals(2.0f, fb.get(12));
    Assert.assertEquals(2.0f, fb.get(13));
    Assert.assertEquals(9.0f, fb.get(14));
    Assert.assertEquals(9.0f, fb.get(15));

    Assert.assertEquals(1.0f, fb.get(16));
    Assert.assertEquals(1.0f, fb.get(17));
    Assert.assertEquals(1.0f, fb.get(18));
    Assert.assertEquals(2.0f, fb.get(19));
    Assert.assertEquals(2.0f, fb.get(20));
    Assert.assertEquals(2.0f, fb.get(21));
    Assert.assertEquals(0.0f, fb.get(22));
    Assert.assertEquals(0.0f, fb.get(23));
  }

  @SuppressWarnings("boxing") @Test public final
    void
    testArrayBufferMapCursor2fWriteCorrect()
      throws ConstraintError,
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final GLArrayBuffersMapped gm = this.getGLArrayBuffersMapped(tc);

    ArrayBuffer a = null;
    ArrayBufferWritableMap wm = null;

    try {
      final ArrayBufferDescriptor d =
        new ArrayBufferDescriptor(new ArrayBufferAttribute[] {
          new ArrayBufferAttribute("position", GLScalarType.TYPE_FLOAT, 3),
          new ArrayBufferAttribute("normal", GLScalarType.TYPE_FLOAT, 3),
          new ArrayBufferAttribute("uv", GLScalarType.TYPE_FLOAT, 2) });

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

    final ByteBuffer b = gm.arrayBufferMapRead(a);
    final FloatBuffer fb = b.asFloatBuffer();

    Assert.assertEquals(1.0f, fb.get(0));
    Assert.assertEquals(2.0f, fb.get(1));
    Assert.assertEquals(3.0f, fb.get(2));
    Assert.assertEquals(10.0f, fb.get(3));
    Assert.assertEquals(20.0f, fb.get(4));
    Assert.assertEquals(30.0f, fb.get(5));
    Assert.assertEquals(0.0f, fb.get(6));
    Assert.assertEquals(0.0f, fb.get(7));

    Assert.assertEquals(4.0f, fb.get(8));
    Assert.assertEquals(5.0f, fb.get(9));
    Assert.assertEquals(6.0f, fb.get(10));
    Assert.assertEquals(40.0f, fb.get(11));
    Assert.assertEquals(50.0f, fb.get(12));
    Assert.assertEquals(60.0f, fb.get(13));
    Assert.assertEquals(1.0f, fb.get(14));
    Assert.assertEquals(0.0f, fb.get(15));

    Assert.assertEquals(7.0f, fb.get(16));
    Assert.assertEquals(8.0f, fb.get(17));
    Assert.assertEquals(9.0f, fb.get(18));
    Assert.assertEquals(70.0f, fb.get(19));
    Assert.assertEquals(80.0f, fb.get(20));
    Assert.assertEquals(90.0f, fb.get(21));
    Assert.assertEquals(1.0f, fb.get(22));
    Assert.assertEquals(1.0f, fb.get(23));
  }

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferMapCursor2fWrongType()
      throws ConstraintError,
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final GLArrayBuffersMapped gm = this.getGLArrayBuffersMapped(tc);

    ArrayBuffer a = null;
    ArrayBufferWritableMap m = null;

    try {
      final ArrayBufferDescriptor d =
        new ArrayBufferDescriptor(new ArrayBufferAttribute[] {
          new ArrayBufferAttribute("position", GLScalarType.TYPE_FLOAT, 3),
          new ArrayBufferAttribute("shape", GLScalarType.TYPE_INT, 1) });

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
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final GLArrayBuffersMapped gm = this.getGLArrayBuffersMapped(tc);

    ArrayBuffer a = null;
    ArrayBufferWritableMap m = null;

    try {
      final ArrayBufferDescriptor d =
        new ArrayBufferDescriptor(
          new ArrayBufferAttribute[] { new ArrayBufferAttribute(
            "position",
            GLScalarType.TYPE_FLOAT,
            3) });

      a = ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
      m = gm.arrayBufferMapWrite(a);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert m != null;
    m.getCursor3f(null);
  }

  @SuppressWarnings("boxing") @Test public final
    void
    testArrayBufferMapCursor3fWriteCorrect()
      throws ConstraintError,
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final GLArrayBuffersMapped gm = this.getGLArrayBuffersMapped(tc);

    ArrayBuffer a = null;
    ArrayBufferWritableMap wm = null;

    try {
      final ArrayBufferDescriptor d =
        new ArrayBufferDescriptor(new ArrayBufferAttribute[] {
          new ArrayBufferAttribute("position", GLScalarType.TYPE_FLOAT, 3),
          new ArrayBufferAttribute("normal", GLScalarType.TYPE_FLOAT, 3) });

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

    final ByteBuffer b = gm.arrayBufferMapRead(a);
    final FloatBuffer fb = b.asFloatBuffer();

    Assert.assertEquals(1.0f, fb.get(0));
    Assert.assertEquals(2.0f, fb.get(1));
    Assert.assertEquals(3.0f, fb.get(2));

    Assert.assertEquals(10.0f, fb.get(3));
    Assert.assertEquals(20.0f, fb.get(4));
    Assert.assertEquals(30.0f, fb.get(5));

    Assert.assertEquals(4.0f, fb.get(6));
    Assert.assertEquals(5.0f, fb.get(7));
    Assert.assertEquals(6.0f, fb.get(8));

    Assert.assertEquals(40.0f, fb.get(9));
    Assert.assertEquals(50.0f, fb.get(10));
    Assert.assertEquals(60.0f, fb.get(11));

    Assert.assertEquals(7.0f, fb.get(12));
    Assert.assertEquals(8.0f, fb.get(13));
    Assert.assertEquals(9.0f, fb.get(14));

    Assert.assertEquals(70.0f, fb.get(15));
    Assert.assertEquals(80.0f, fb.get(16));
    Assert.assertEquals(90.0f, fb.get(17));
  }

  @SuppressWarnings("boxing") @Test public final
    void
    testArrayBufferMapCursor3fWriteSeekCorrect()
      throws ConstraintError,
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final GLArrayBuffersMapped gm = this.getGLArrayBuffersMapped(tc);

    ArrayBuffer a = null;
    ArrayBufferWritableMap wm = null;

    try {
      final ArrayBufferDescriptor d =
        new ArrayBufferDescriptor(new ArrayBufferAttribute[] {
          new ArrayBufferAttribute("position", GLScalarType.TYPE_FLOAT, 3),
          new ArrayBufferAttribute("normal", GLScalarType.TYPE_FLOAT, 3) });

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

    final ByteBuffer b = gm.arrayBufferMapRead(a);
    final FloatBuffer fb = b.asFloatBuffer();

    Assert.assertEquals(0.0f, fb.get(0));
    Assert.assertEquals(0.0f, fb.get(1));
    Assert.assertEquals(0.0f, fb.get(2));

    Assert.assertEquals(0.0f, fb.get(3));
    Assert.assertEquals(0.0f, fb.get(4));
    Assert.assertEquals(0.0f, fb.get(5));

    Assert.assertEquals(23.0f, fb.get(6));
    Assert.assertEquals(24.0f, fb.get(7));
    Assert.assertEquals(25.0f, fb.get(8));

    Assert.assertEquals(0.0f, fb.get(9));
    Assert.assertEquals(0.0f, fb.get(10));
    Assert.assertEquals(0.0f, fb.get(11));

    Assert.assertEquals(0.0f, fb.get(12));
    Assert.assertEquals(0.0f, fb.get(13));
    Assert.assertEquals(0.0f, fb.get(14));

    Assert.assertEquals(0.0f, fb.get(15));
    Assert.assertEquals(0.0f, fb.get(16));
    Assert.assertEquals(0.0f, fb.get(17));
  }

  @Test(expected = ConstraintError.class) public final
    void
    testArrayBufferMapCursor3fWrongType()
      throws ConstraintError,
        GLException,
        GLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final GLArrayBuffers ga = this.getGLArrayBuffers(tc);
    final GLArrayBuffersMapped gm = this.getGLArrayBuffersMapped(tc);

    ArrayBuffer a = null;
    ArrayBufferWritableMap m = null;

    try {
      final ArrayBufferDescriptor d =
        new ArrayBufferDescriptor(new ArrayBufferAttribute[] {
          new ArrayBufferAttribute("position", GLScalarType.TYPE_FLOAT, 3),
          new ArrayBufferAttribute("shape", GLScalarType.TYPE_INT, 1) });

      a = ga.arrayBufferAllocate(10, d, UsageHint.USAGE_STATIC_DRAW);
      m = gm.arrayBufferMapWrite(a);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert m != null;
    m.getCursor3f("shape");
  }
}
