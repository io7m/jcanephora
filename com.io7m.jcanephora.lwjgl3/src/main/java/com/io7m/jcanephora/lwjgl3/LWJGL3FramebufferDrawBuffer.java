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

package com.io7m.jcanephora.lwjgl3;

import com.io7m.jcanephora.core.JCGLFramebufferDrawBufferType;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeCheck;
import com.io7m.jranges.Ranges;

final class LWJGL3FramebufferDrawBuffer extends
  LWJGL3ObjectPseudoUnshared implements JCGLFramebufferDrawBufferType
{
  private final int index;

  LWJGL3FramebufferDrawBuffer(
    final LWJGL3Context in_context,
    final int in_index)
  {
    super(in_context);
    this.index =
      RangeCheck.checkIncludedInInteger(
        in_index,
        "Draw buffer",
        Ranges.NATURAL_INTEGER,
        "Valid draw buffers");
  }

  static LWJGL3FramebufferDrawBuffer checkDrawBuffer(
    final LWJGL3Context c,
    final JCGLFramebufferDrawBufferType buffer)
  {
    NullCheck.notNull(c, "Context");
    NullCheck.notNull(buffer, "Framebuffer");
    return (LWJGL3FramebufferDrawBuffer)
      LWJGL3CompatibilityChecks.checkAny(c, buffer);
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder("[FramebufferDrawBuffer ");
    sb.append(this.index);
    sb.append(']');
    return sb.toString();
  }

  @Override
  public boolean equals(final Object o)
  {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }

    final LWJGL3FramebufferDrawBuffer that = (LWJGL3FramebufferDrawBuffer) o;
    return this.index == that.index;
  }

  @Override
  public int hashCode()
  {
    return this.index;
  }

  @Override
  public int compareTo(final JCGLFramebufferDrawBufferType o)
  {
    return Integer.compare(this.index, o.drawBufferGetIndex());
  }

  @Override
  public int drawBufferGetIndex()
  {
    return this.index;
  }
}
