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

package com.io7m.jcanephora.fake;

import com.io7m.jcanephora.core.JCGLBufferUsableType;
import com.io7m.jcanephora.core.JCGLUsageHint;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeInclusiveL;

import java.nio.ByteBuffer;

abstract class FakeBuffer extends FakeObjectShared
  implements JCGLBufferUsableType
{
  private final JCGLUsageHint   usage;
  private final RangeInclusiveL range;
  private final ByteBuffer      data;

  FakeBuffer(
    final FakeContext in_context,
    final int in_id,
    final ByteBuffer in_data,
    final JCGLUsageHint in_usage)
  {
    super(in_context, in_id);
    this.usage = NullCheck.notNull(in_usage);
    this.data = NullCheck.notNull(in_data);
    this.range = new RangeInclusiveL(0L, (long) in_data.capacity() - 1L);
  }

  @Override public final JCGLUsageHint getUsageHint()
  {
    return this.usage;
  }

  @Override public final RangeInclusiveL getRange()
  {
    return this.range;
  }

  public final ByteBuffer getData()
  {
    return this.data;
  }
}
