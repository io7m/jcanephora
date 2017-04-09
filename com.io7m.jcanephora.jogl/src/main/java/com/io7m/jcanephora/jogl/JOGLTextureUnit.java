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

package com.io7m.jcanephora.jogl;

import com.io7m.jcanephora.core.JCGLTextureUnitType;
import com.io7m.jnull.Nullable;
import com.io7m.jranges.RangeCheck;
import com.io7m.jranges.Ranges;
import com.jogamp.opengl.GLContext;

final class JOGLTextureUnit extends JOGLObjectPseudoUnshared implements
  JCGLTextureUnitType
{
  private final int index;
  private @Nullable JOGLTexture2D bind_2d;
  private @Nullable JOGLTextureCube bind_cube;

  JOGLTextureUnit(
    final GLContext in_context,
    final int in_index)
  {
    super(in_context);
    this.index =
      RangeCheck.checkIncludedInInteger(
        in_index,
        "Index",
        Ranges.NATURAL_INTEGER,
        "Valid indices");
  }

  static JOGLTextureUnit checkTextureUnit(
    final GLContext c,
    final JCGLTextureUnitType u)
  {
    return (JOGLTextureUnit) JOGLCompatibilityChecks.checkAny(c, u);
  }

  JOGLTexture2D getBind2D()
  {
    return this.bind_2d;
  }

  void setBind2D(final JOGLTexture2D t)
  {
    this.bind_2d = t;
  }

  JOGLTextureCube getBindCube()
  {
    return this.bind_cube;
  }

  void setBindCube(final JOGLTextureCube t)
  {
    this.bind_cube = t;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder("[TextureUnit ");
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

    final JOGLTextureUnit that = (JOGLTextureUnit) o;
    return this.index == that.index;
  }

  @Override
  public int hashCode()
  {
    return this.index;
  }

  @Override
  public int index()
  {
    return this.index;
  }

  @Override
  public int compareTo(final JCGLTextureUnitType o)
  {
    return Integer.compareUnsigned(this.index, o.index());
  }
}
