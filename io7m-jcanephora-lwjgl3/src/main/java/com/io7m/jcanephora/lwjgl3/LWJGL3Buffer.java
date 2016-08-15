/*
 * Copyright © 2016 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.lwjgl3;

import com.io7m.jcanephora.core.JCGLBufferUsableType;
import com.io7m.jcanephora.core.JCGLUsageHint;
import com.io7m.jnull.NullCheck;
import com.io7m.junsigned.ranges.UnsignedRangeInclusiveL;

abstract class LWJGL3Buffer extends LWJGL3Referable implements
  JCGLBufferUsableType
{
  private final JCGLUsageHint usage;
  private final UnsignedRangeInclusiveL range;
  private final String image;

  LWJGL3Buffer(
    final LWJGL3Context in_context,
    final int in_id,
    final long in_size,
    final JCGLUsageHint in_usage)
  {
    super(in_context, in_id);
    this.usage = NullCheck.notNull(in_usage);
    this.range = new UnsignedRangeInclusiveL(0L, in_size - 1L);

    {
      final StringBuilder sb = new StringBuilder("[Buffer ");
      sb.append(super.getGLName());
      sb.append(this.range);
      sb.append(" ");
      sb.append(this.usage);
      sb.append(']');
      this.image = sb.toString();
    }
  }

  @Override
  public String toString()
  {
    return this.image;
  }

  @Override
  public final JCGLUsageHint getUsageHint()
  {
    return this.usage;
  }

  @Override
  public final UnsignedRangeInclusiveL getRange()
  {
    return this.range;
  }
}
