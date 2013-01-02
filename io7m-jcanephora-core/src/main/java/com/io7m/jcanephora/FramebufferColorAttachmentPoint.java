/*
 * Copyright © 2012 http://io7m.com
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

package com.io7m.jcanephora;

/**
 * A color attachment point on a framebuffer.
 */

public final class FramebufferColorAttachmentPoint implements
  Comparable<FramebufferColorAttachmentPoint>
{
  private final int index;

  FramebufferColorAttachmentPoint(
    final int index)
  {
    this.index = index;
  }

  @Override public int compareTo(
    final FramebufferColorAttachmentPoint other)
  {
    final Integer ix = Integer.valueOf(this.index);
    final Integer iy = Integer.valueOf(other.index);
    return ix.compareTo(iy);
  }

  @Override public boolean equals(
    final Object obj)
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
    final FramebufferColorAttachmentPoint other =
      (FramebufferColorAttachmentPoint) obj;
    if (this.index != other.index) {
      return false;
    }
    return true;
  }

  /**
   * Retrieve the index of the attachment point. This value will be between 0
   * and some implementation-defined exclusive upper limit (usually 4 or 8 in
   * current implementations).
   */

  public int getIndex()
  {
    return this.index;
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
    builder.append("[FramebufferColorAttachmentPoint ");
    builder.append(this.index);
    builder.append("]");
    return builder.toString();
  }
}
