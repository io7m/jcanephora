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
import com.io7m.jcanephora.core.JCGLUsageHint;
import com.io7m.jcanephora.core.api.JCGLArrayBuffersType;
import com.io7m.jranges.RangeCheckException;
import com.io7m.junsigned.ranges.UnsignedRangeInclusiveL;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.nio.ByteBuffer;

/**
 * Buffer updates contracts.
 */

// CHECKSTYLE_JAVADOC:OFF

public abstract class JCGLBufferUpdatesContract extends JCGLContract
{
  @Rule public final ExpectedException expected = ExpectedException.none();

  protected abstract JCGLArrayBuffersType getArrayBuffers(String name);

  @Test
  public final void testBufferUpdateAllIdentities()
  {
    final JCGLArrayBuffersType ga = this.getArrayBuffers("main");

    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);

    final JCGLBufferUpdateType<JCGLArrayBufferType> u =
      JCGLBufferUpdates.newUpdateReplacingAll(a);
    Assert.assertSame(a, u.getBuffer());

    final UnsignedRangeInclusiveL u_range = u.getDataUpdateRange();
    final ByteBuffer u_data = u.getData();
    Assert.assertEquals(a.getRange(), u_range);
    Assert.assertEquals(u_range.getInterval(), (long) u_data.capacity());
  }

  @Test
  public final void testBufferUpdateOutOfRange()
  {
    final JCGLArrayBuffersType ga = this.getArrayBuffers("main");

    final JCGLArrayBufferType a =
      ga.arrayBufferAllocate(100L, JCGLUsageHint.USAGE_STATIC_DRAW);

    this.expected.expect(RangeCheckException.class);
    JCGLBufferUpdates.newUpdateReplacingRange(
      a, new UnsignedRangeInclusiveL(0L, 200L));
  }
}
