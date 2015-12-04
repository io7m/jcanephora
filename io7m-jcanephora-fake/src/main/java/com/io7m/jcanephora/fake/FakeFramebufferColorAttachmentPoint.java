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

import com.io7m.jcanephora.core.JCGLFramebufferColorAttachmentPointType;
import com.io7m.jranges.RangeCheck;
import com.io7m.jranges.Ranges;

final class FakeFramebufferColorAttachmentPoint extends
  FakeObjectPseudoUnshared implements JCGLFramebufferColorAttachmentPointType
{
  private final int index;

  FakeFramebufferColorAttachmentPoint(
    final FakeContext in_context,
    final int in_index)
  {
    super(in_context);
    this.index =
      RangeCheck.checkIncludedInInteger(
        in_index,
        "Attachment point",
        Ranges.NATURAL_INTEGER,
        "Valid attachment points");
  }

  @Override public String toString()
  {
    final StringBuilder sb =
      new StringBuilder("[FramebufferColorAttachmentPoint ");
    sb.append(this.index);
    sb.append(']');
    return sb.toString();
  }

  @Override public boolean equals(final Object o)
  {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }

    final FakeFramebufferColorAttachmentPoint that =
      (FakeFramebufferColorAttachmentPoint) o;
    return this.index == that.index;
  }

  @Override public int hashCode()
  {
    return this.index;
  }

  @Override public int colorAttachmentPointGetIndex()
  {
    return this.index;
  }

  @Override
  public int compareTo(final JCGLFramebufferColorAttachmentPointType o)
  {
    return Integer.compare(this.index, o.colorAttachmentPointGetIndex());
  }
}
