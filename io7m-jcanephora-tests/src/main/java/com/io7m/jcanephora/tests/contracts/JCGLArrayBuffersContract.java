/*
 * Copyright © 2015 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.tests.contracts;

import com.io7m.jcanephora.core.JCGLArrayBufferType;
import com.io7m.jcanephora.core.JCGLBufferUpdateType;
import com.io7m.jcanephora.core.JCGLBufferUpdates;
import com.io7m.jcanephora.core.JCGLExceptionBufferNotBound;
import com.io7m.jcanephora.core.JCGLExceptionDeleted;
import com.io7m.jcanephora.core.JCGLExceptionWrongContext;
import com.io7m.jcanephora.core.JCGLUsageHint;
import com.io7m.jcanephora.core.api.JCGLArrayBuffersType;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jranges.RangeCheckException;
import com.io7m.junsigned.ranges.UnsignedRangeInclusiveL;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Optional;

/**
 * Array buffer contracts.
 */

// CHECKSTYLE_JAVADOC:OFF

public abstract class JCGLArrayBuffersContract extends JCGLContract
{
  @Rule public final ExpectedException expected = ExpectedException.none();

  protected abstract JCGLArrayBuffersType getArrayBuffers(String name);

  protected abstract JCGLUnsharedContextPair<JCGLArrayBuffersType>
  getArrayBuffersUnshared(
    String main,
    String alt);

  protected abstract JCGLSharedContextPair<JCGLArrayBuffersType>
  getArrayBuffersSharedWith(
    String name,
    String shared);

  @Test
  public final void testArrayAllocateNegative()
  {
    final JCGLArrayBuffersType ga = this.getArrayBuffers("main");

    this.expected.expect(RangeCheckException.class);
    ga.arrayBufferAllocate(-1L, JCGLUsageHint.USAGE_STATIC_DRAW);
  }

  @Test
  public final void testArrayAllocateIdentities()
  {
    final JCGLArrayBuffersType ga = this.getArrayBuffers("main");

    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);
    final UnsignedRangeInclusiveL r = a.getRange();
    Assert.assertEquals(0L, r.getLower());
    Assert.assertEquals(99L, r.getUpper());
    Assert.assertEquals(JCGLUsageHint.USAGE_STATIC_DRAW, a.getUsageHint());
    Assert.assertFalse(a.isDeleted());
  }

  @Test
  public final void testArrayReallocateIdentities()
  {
    final JCGLArrayBuffersType ga = this.getArrayBuffers("main");

    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);
    final UnsignedRangeInclusiveL r = a.getRange();
    Assert.assertEquals(0L, r.getLower());
    Assert.assertEquals(99L, r.getUpper());
    Assert.assertEquals(JCGLUsageHint.USAGE_STATIC_DRAW, a.getUsageHint());
    Assert.assertFalse(a.isDeleted());

    ga.arrayBufferReallocate(a);
    Assert.assertEquals(0L, r.getLower());
    Assert.assertEquals(99L, r.getUpper());
    Assert.assertEquals(JCGLUsageHint.USAGE_STATIC_DRAW, a.getUsageHint());
    Assert.assertFalse(a.isDeleted());
  }

  @Test
  public final void testArrayBindIdentities()
  {
    final JCGLArrayBuffersType ga = this.getArrayBuffers("main");

    Assert.assertFalse(ga.arrayBufferGetCurrentlyBound().isPresent());
    Assert.assertFalse(ga.arrayBufferAnyIsBound());

    final JCGLArrayBufferType a0 =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);
    final JCGLArrayBufferType a1 =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);

    Assert.assertTrue(ga.arrayBufferAnyIsBound());
    Assert.assertTrue(ga.arrayBufferIsBound(a1));
    Assert.assertEquals(Optional.of(a1), ga.arrayBufferGetCurrentlyBound());
    ga.arrayBufferBind(a0);
    Assert.assertTrue(ga.arrayBufferAnyIsBound());
    Assert.assertTrue(ga.arrayBufferIsBound(a0));
    Assert.assertEquals(Optional.of(a0), ga.arrayBufferGetCurrentlyBound());
    ga.arrayBufferBind(a1);
    Assert.assertTrue(ga.arrayBufferAnyIsBound());
    Assert.assertTrue(ga.arrayBufferIsBound(a1));
    Assert.assertEquals(Optional.of(a1), ga.arrayBufferGetCurrentlyBound());

    ga.arrayBufferUnbind();
    Assert.assertEquals(Optional.empty(), ga.arrayBufferGetCurrentlyBound());
    Assert.assertFalse(ga.arrayBufferGetCurrentlyBound().isPresent());
    Assert.assertFalse(ga.arrayBufferAnyIsBound());
  }

  @Test
  public final void testArrayBindDeleted()
  {
    final JCGLArrayBuffersType ga = this.getArrayBuffers("main");
    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);
    ga.arrayBufferDelete(a);
    Assert.assertTrue(a.isDeleted());

    this.expected.expect(JCGLExceptionDeleted.class);
    ga.arrayBufferBind(a);
  }

  @Test
  public final void testArrayIsBoundDeleted()
  {
    final JCGLArrayBuffersType ga = this.getArrayBuffers("main");
    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);
    ga.arrayBufferDelete(a);
    Assert.assertTrue(a.isDeleted());

    this.expected.expect(JCGLExceptionDeleted.class);
    ga.arrayBufferIsBound(a);
  }

  @Test
  public final void testArrayBindWrongContext()
  {
    final JCGLUnsharedContextPair<JCGLArrayBuffersType> p =
      this.getArrayBuffersUnshared("main", "alt");
    final JCGLContextType ca = p.getContextA();
    final JCGLContextType cb = p.getContextB();
    final JCGLArrayBuffersType ga = p.getValueA();
    final JCGLArrayBuffersType gb = p.getValueB();

    Assert.assertFalse(ca.contextIsCurrent());
    Assert.assertTrue(cb.contextIsCurrent());

    cb.contextReleaseCurrent();
    ca.contextMakeCurrent();

    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);
    ga.arrayBufferDelete(a);
    Assert.assertTrue(a.isDeleted());

    ca.contextReleaseCurrent();
    cb.contextMakeCurrent();
    this.expected.expect(JCGLExceptionWrongContext.class);
    gb.arrayBufferBind(a);
  }

  @Test
  public final void testArrayBindShared()
  {
    final JCGLSharedContextPair<JCGLArrayBuffersType> p =
      this.getArrayBuffersSharedWith("main", "alt");
    final JCGLContextType ca = p.getMasterContext();
    final JCGLContextType cb = p.getSlaveContext();
    final JCGLArrayBuffersType ga = p.getMasterValue();
    final JCGLArrayBuffersType gb = p.getSlaveValue();

    Assert.assertTrue(ca.contextIsCurrent());
    Assert.assertFalse(cb.contextIsCurrent());

    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);

    ca.contextReleaseCurrent();
    cb.contextMakeCurrent();
    gb.arrayBufferBind(a);
  }

  @Test
  public final void testArrayDeleteDeleted()
  {
    final JCGLArrayBuffersType ga = this.getArrayBuffers("main");
    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);
    ga.arrayBufferDelete(a);
    Assert.assertTrue(a.isDeleted());

    this.expected.expect(JCGLExceptionDeleted.class);
    ga.arrayBufferDelete(a);
  }

  @Test
  public final void testArrayDeleteWrongContext()
  {
    final JCGLUnsharedContextPair<JCGLArrayBuffersType> p =
      this.getArrayBuffersUnshared("main", "alt");
    final JCGLContextType ca = p.getContextA();
    final JCGLContextType cb = p.getContextB();
    final JCGLArrayBuffersType ga = p.getValueA();
    final JCGLArrayBuffersType gb = p.getValueB();

    Assert.assertFalse(ca.contextIsCurrent());
    Assert.assertTrue(cb.contextIsCurrent());

    cb.contextReleaseCurrent();
    ca.contextMakeCurrent();

    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);

    ca.contextReleaseCurrent();
    cb.contextMakeCurrent();
    this.expected.expect(JCGLExceptionWrongContext.class);
    gb.arrayBufferDelete(a);
  }

  @Test
  public final void testArrayDeleteShared()
  {
    final JCGLSharedContextPair<JCGLArrayBuffersType> p =
      this.getArrayBuffersSharedWith("main", "alt");
    final JCGLContextType ca = p.getMasterContext();
    final JCGLContextType cb = p.getSlaveContext();
    final JCGLArrayBuffersType ga = p.getMasterValue();
    final JCGLArrayBuffersType gb = p.getSlaveValue();

    Assert.assertTrue(ca.contextIsCurrent());
    Assert.assertFalse(cb.contextIsCurrent());

    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);

    ca.contextReleaseCurrent();
    cb.contextMakeCurrent();
    gb.arrayBufferDelete(a);
  }

  @Test
  public final void testArrayDeleteUnbinds()
  {
    final JCGLArrayBuffersType ga = this.getArrayBuffers("main");
    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);

    ga.arrayBufferBind(a);
    Assert.assertEquals(Optional.of(a), ga.arrayBufferGetCurrentlyBound());

    ga.arrayBufferDelete(a);
    Assert.assertTrue(a.isDeleted());
    Assert.assertFalse(ga.arrayBufferGetCurrentlyBound().isPresent());
  }

  @Test
  public final void testArrayDeleteNoUnbindOther()
  {
    final JCGLArrayBuffersType ga = this.getArrayBuffers("main");
    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);
    final JCGLArrayBufferType b =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);

    ga.arrayBufferBind(a);
    Assert.assertEquals(Optional.of(a), ga.arrayBufferGetCurrentlyBound());
    ga.arrayBufferDelete(b);
    Assert.assertTrue(b.isDeleted());
    Assert.assertFalse(a.isDeleted());
    Assert.assertEquals(Optional.of(a), ga.arrayBufferGetCurrentlyBound());
  }

  @Test
  public final void testArrayUpdateDeleted()
  {
    final JCGLArrayBuffersType ga = this.getArrayBuffers("main");
    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);

    final JCGLBufferUpdateType<JCGLArrayBufferType> u =
      JCGLBufferUpdates.newUpdateReplacingAll(a);

    ga.arrayBufferDelete(a);

    this.expected.expect(JCGLExceptionDeleted.class);
    ga.arrayBufferUpdate(u);
  }

  @Test
  public final void testArrayUpdateWrongContext()
  {
    final JCGLUnsharedContextPair<JCGLArrayBuffersType> p =
      this.getArrayBuffersUnshared("main", "alt");
    final JCGLContextType ca = p.getContextA();
    final JCGLContextType cb = p.getContextB();
    final JCGLArrayBuffersType ga = p.getValueA();
    final JCGLArrayBuffersType gb = p.getValueB();

    Assert.assertFalse(ca.contextIsCurrent());
    Assert.assertTrue(cb.contextIsCurrent());

    cb.contextReleaseCurrent();
    ca.contextMakeCurrent();

    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);

    final JCGLBufferUpdateType<JCGLArrayBufferType> u =
      JCGLBufferUpdates.newUpdateReplacingAll(a);

    ca.contextReleaseCurrent();
    cb.contextMakeCurrent();
    this.expected.expect(JCGLExceptionWrongContext.class);
    gb.arrayBufferUpdate(u);
  }

  @Test
  public final void testArrayUpdateNotBound()
  {
    final JCGLArrayBuffersType ga = this.getArrayBuffers("main");
    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);

    final JCGLBufferUpdateType<JCGLArrayBufferType> u =
      JCGLBufferUpdates.newUpdateReplacingAll(a);

    ga.arrayBufferUnbind();
    this.expected.expect(JCGLExceptionBufferNotBound.class);
    ga.arrayBufferUpdate(u);
  }

  @Test
  public final void testArrayReallocateNotBound()
  {
    final JCGLArrayBuffersType ga = this.getArrayBuffers("main");
    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);

    ga.arrayBufferUnbind();
    this.expected.expect(JCGLExceptionBufferNotBound.class);
    ga.arrayBufferReallocate(a);
  }

  @Test
  public final void testArrayUpdateShared()
  {
    final JCGLSharedContextPair<JCGLArrayBuffersType> p =
      this.getArrayBuffersSharedWith("main", "alt");
    final JCGLContextType ca = p.getMasterContext();
    final JCGLContextType cb = p.getSlaveContext();
    final JCGLArrayBuffersType ga = p.getMasterValue();
    final JCGLArrayBuffersType gb = p.getSlaveValue();

    Assert.assertTrue(ca.contextIsCurrent());
    Assert.assertFalse(cb.contextIsCurrent());

    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);

    final JCGLBufferUpdateType<JCGLArrayBufferType> u =
      JCGLBufferUpdates.newUpdateReplacingAll(a);

    ca.contextReleaseCurrent();
    cb.contextMakeCurrent();
    gb.arrayBufferBind(a);
    gb.arrayBufferUpdate(u);
  }
}
