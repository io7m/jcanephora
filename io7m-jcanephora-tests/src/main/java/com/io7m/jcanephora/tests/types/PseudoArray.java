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

import java.util.Map;

import com.io7m.jcanephora.ArrayAttributeDescriptor;
import com.io7m.jcanephora.ArrayAttributeType;
import com.io7m.jcanephora.ArrayBufferType;
import com.io7m.jcanephora.ArrayBufferUsableType;
import com.io7m.jcanephora.ArrayDescriptor;
import com.io7m.jcanephora.JCGLExceptionAttributeMissing;
import com.io7m.jcanephora.UsageHint;
import com.io7m.jranges.RangeInclusiveL;

public final class PseudoArray implements ArrayBufferType
{
  private boolean               deleted;
  private final ArrayDescriptor desc;
  private final UsageHint       hint;
  private final RangeInclusiveL range;

  public PseudoArray(
    final ArrayDescriptor in_desc,
    final RangeInclusiveL in_range,
    final UsageHint in_hint)
  {
    this.desc = in_desc;
    this.hint = in_hint;
    this.range = in_range;
  }

  @Override public ArrayAttributeType arrayGetAttribute(
    final String name)
    throws JCGLExceptionAttributeMissing
  {
    return new ArrayAttributeType() {
      @Override public ArrayBufferUsableType getArray()
      {
        return PseudoArray.this;
      }

      @Override public ArrayAttributeDescriptor getDescriptor()
      {
        @SuppressWarnings("synthetic-access") final ArrayDescriptor d =
          PseudoArray.this.desc;
        assert d != null;
        final Map<String, ArrayAttributeDescriptor> attrs = d.getAttributes();
        final ArrayAttributeDescriptor aa = attrs.get(name);
        assert aa != null;
        return aa;
      }
    };
  }

  @Override public ArrayDescriptor arrayGetDescriptor()
  {
    return this.desc;
  }

  @Override public UsageHint arrayGetUsageHint()
  {
    return this.hint;
  }

  @Override public long bufferGetElementSizeBytes()
  {
    return this.desc.getElementSizeBytes();
  }

  @Override public RangeInclusiveL bufferGetRange()
  {
    return this.range;
  }

  @Override public int getGLName()
  {
    return 1;
  }

  @Override public long resourceGetSizeBytes()
  {
    return this.range.getInterval() * this.desc.getElementSizeBytes();
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
