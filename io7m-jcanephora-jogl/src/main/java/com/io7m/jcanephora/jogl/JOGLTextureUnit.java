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

package com.io7m.jcanephora.jogl;

import javax.media.opengl.GLContext;

import com.io7m.jcanephora.TextureUnitType;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;
import com.io7m.jranges.RangeCheck;

final class JOGLTextureUnit extends JOGLObjectPseudoUnshared implements
  TextureUnitType
{
  private final int index;

  protected JOGLTextureUnit(
    final GLContext in_context,
    final int in_index)
  {
    super(in_context);
    this.index =
      (int) RangeCheck.checkIncludedIn(
        in_index,
        "Index",
        RangeCheck.NATURAL_INTEGER,
        "Valid indices");
  }

  @Override public int compareTo(
    final @Nullable TextureUnitType other)
  {
    final TextureUnitType o = NullCheck.notNull(other, "Other");
    final Integer ix = Integer.valueOf(this.index);
    final Integer iy = Integer.valueOf(o.unitGetIndex());
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
    final JOGLTextureUnit other = (JOGLTextureUnit) obj;
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
    builder.append("[JOGLTextureUnit ");
    builder.append(this.index);
    builder.append("]");
    final String r = builder.toString();
    assert r != null;
    return r;
  }

  @Override public int unitGetIndex()
  {
    return this.index;
  }
}