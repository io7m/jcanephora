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
import com.io7m.jcanephora.core.JCGLExceptionDeleted;
import com.io7m.jcanephora.core.JCGLUsageHint;
import com.io7m.jcanephora.core.api.JCGLArrayBuffersType;
import com.io7m.jranges.RangeCheckException;
import com.io7m.jranges.RangeInclusiveL;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Optional;

/**
 * Array buffer contracts.
 */

// CHECKSTYLE_JAVADOC:OFF

public abstract class JCGLArrayBuffersContract
{
  @Rule public final ExpectedException expected = ExpectedException.none();

  protected abstract JCGLArrayBuffersType getArrayBuffers();

  @Test public final void testArrayAllocateNegative()
  {
    final JCGLArrayBuffersType ga = this.getArrayBuffers();

    this.expected.expect(RangeCheckException.class);
    ga.arrayBufferAllocate(-1L, JCGLUsageHint.USAGE_STATIC_DRAW);
  }

  @Test public final void testArrayAllocateIdentities()
  {
    final JCGLArrayBuffersType ga = this.getArrayBuffers();

    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);
    final RangeInclusiveL r = a.getRange();
    Assert.assertEquals(0L, r.getLower());
    Assert.assertEquals(99L, r.getUpper());
    Assert.assertEquals(JCGLUsageHint.USAGE_STATIC_DRAW, a.getUsageHint());
    Assert.assertFalse(a.isDeleted());
  }

  @Test public final void testArrayBindIdentities()
  {
    final JCGLArrayBuffersType ga = this.getArrayBuffers();

    Assert.assertFalse(ga.arrayBufferGetCurrentlyBound().isPresent());
    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);
    Assert.assertFalse(ga.arrayBufferGetCurrentlyBound().isPresent());
    ga.arrayBufferBind(a);
    Assert.assertEquals(Optional.of(a), ga.arrayBufferGetCurrentlyBound());
    ga.arrayBufferUnbind();
    Assert.assertFalse(ga.arrayBufferGetCurrentlyBound().isPresent());
  }

  @Test public final void testArrayBindDeleted()
  {
    final JCGLArrayBuffersType ga = this.getArrayBuffers();
    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);
    ga.arrayBufferDelete(a);
    Assert.assertTrue(a.isDeleted());

    this.expected.expect(JCGLExceptionDeleted.class);
    ga.arrayBufferBind(a);
  }

  @Test public final void testArrayDeleteDeleted()
  {
    final JCGLArrayBuffersType ga = this.getArrayBuffers();
    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);
    ga.arrayBufferDelete(a);
    Assert.assertTrue(a.isDeleted());

    this.expected.expect(JCGLExceptionDeleted.class);
    ga.arrayBufferDelete(a);
  }

  @Test public final void testArrayDeleteUnbinds()
  {
    final JCGLArrayBuffersType ga = this.getArrayBuffers();
    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);

    ga.arrayBufferBind(a);
    Assert.assertEquals(Optional.of(a), ga.arrayBufferGetCurrentlyBound());

    ga.arrayBufferDelete(a);
    Assert.assertTrue(a.isDeleted());
    Assert.assertFalse(ga.arrayBufferGetCurrentlyBound().isPresent());
  }

  @Test public final void testArrayDeleteNoUnbindOther()
  {
    final JCGLArrayBuffersType ga = this.getArrayBuffers();
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
}
