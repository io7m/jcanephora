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

package com.io7m.jcanephora.fake;

import com.io7m.jcanephora.FramebufferColorAttachmentPointType;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;
import com.io7m.jranges.RangeCheck;

/**
 * A color attachment point on a framebuffer.
 */

final class FakeFramebufferColorAttachmentPoint extends
  FakeObjectPseudoUnshared implements FramebufferColorAttachmentPointType
{
  private final int index;

  FakeFramebufferColorAttachmentPoint(
    final FakeContext in_context,
    final int in_index)
  {
    super(in_context);
    this.index =
      (int) RangeCheck.checkIncludedIn(
        in_index,
        "Attachment point",
        RangeCheck.NATURAL_INTEGER,
        "Valid attachment points");
  }

  @Override public int colorAttachmentPointGetIndex()
  {
    return this.index;
  }

  @Override public int compareTo(
    final @Nullable FramebufferColorAttachmentPointType other)
  {
    final FramebufferColorAttachmentPointType o =
      NullCheck.notNull(other, "Other");

    final Integer ix = Integer.valueOf(this.index);
    final Integer iy = Integer.valueOf(o.colorAttachmentPointGetIndex());
    return ix.compareTo(iy);
  }

  @Override public boolean equals(
    final @Nullable Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    final FakeFramebufferColorAttachmentPoint other =
      (FakeFramebufferColorAttachmentPoint) obj;
    if (this.index != other.index) {
      return false;
    }
    return true;
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + this.index;
    return result;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[FakeFramebufferColorAttachmentPoint ");
    builder.append(this.index);
    builder.append("]");
    final String r = builder.toString();
    assert r != null;
    return r;
  }
}
