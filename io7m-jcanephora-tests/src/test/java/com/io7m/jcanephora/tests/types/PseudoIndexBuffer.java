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

package com.io7m.jcanephora.tests.types;

import com.io7m.jcanephora.IndexBufferType;
import com.io7m.jcanephora.JCGLUnsignedType;
import com.io7m.jcanephora.UsageHint;
import com.io7m.jranges.RangeInclusiveL;

public final class PseudoIndexBuffer implements IndexBufferType
{
  private boolean                deleted;
  private final UsageHint        hint;
  private final RangeInclusiveL  range;
  private final JCGLUnsignedType type;

  public PseudoIndexBuffer(
    final JCGLUnsignedType in_type,
    final RangeInclusiveL in_range,
    final UsageHint in_hint)
  {
    this.type = in_type;
    this.hint = in_hint;
    this.range = in_range;
  }

  @Override public long bufferGetElementSizeBytes()
  {
    return this.type.getSizeBytes();
  }

  @Override public RangeInclusiveL bufferGetRange()
  {
    return this.range;
  }

  @Override public int getGLName()
  {
    return 1;
  }

  @Override public JCGLUnsignedType indexGetType()
  {
    return this.type;
  }

  @Override public UsageHint indexGetUsageHint()
  {
    return this.hint;
  }

  @Override public long resourceGetSizeBytes()
  {
    return this.range.getInterval() * this.type.getSizeBytes();
  }

  @Override public boolean resourceIsDeleted()
  {
    return this.deleted;
  }

  public void setDeleted(
    final boolean d)
  {
    this.deleted = d;
  }
}
