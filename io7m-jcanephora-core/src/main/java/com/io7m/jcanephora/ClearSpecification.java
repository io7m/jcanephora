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

package com.io7m.jcanephora;

import com.io7m.jfunctional.Option;
import com.io7m.jfunctional.OptionType;
import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.VectorI4F;
import com.io7m.jtensors.VectorReadable4FType;

/**
 * The type of combined buffer clear specifications.
 */

public final class ClearSpecification
{
  @SuppressWarnings("synthetic-access") private static final class Builder implements
    ClearSpecificationBuilderType
  {
    private OptionType<VectorReadable4FType> color;
    private OptionType<Float>                depth;
    private OptionType<Integer>              stencil;
    private boolean                          strict;

    Builder()
    {
      this.depth = Option.none();
      this.stencil = Option.none();
      this.color = Option.none();
      this.strict = false;
    }

    Builder(
      final ClearSpecification s)
    {
      NullCheck.notNull(s, "Specification");
      this.depth = s.depth;
      this.stencil = s.stencil;
      this.color = s.color;
      this.strict = s.strict;
    }

    @Override public ClearSpecification build()
    {
      return new ClearSpecification(
        this.depth,
        this.stencil,
        this.color,
        this.strict);
    }

    @Override public void disableColorBufferClear()
    {
      this.color = Option.none();
    }

    @Override public void disableDepthBufferClear()
    {
      this.depth = Option.none();
    }

    @Override public void disableStencilBufferClear()
    {
      this.stencil = Option.none();
    }

    @Override public void enableColorBufferClear3f(
      final float r,
      final float g,
      final float b)
    {
      this.color =
        Option.some((VectorReadable4FType) new VectorI4F(r, g, b, 1.0f));
    }

    @Override public void enableColorBufferClear4f(
      final float r,
      final float g,
      final float b,
      final float a)
    {
      this.color =
        Option.some((VectorReadable4FType) new VectorI4F(r, g, b, a));
    }

    @Override public void enableDepthBufferClear(
      final float d)
    {
      final Float r = Float.valueOf(d);
      assert r != null;
      this.depth = Option.some(r);
    }

    @Override public void enableStencilBufferClear(
      final int s)
    {
      final Integer r = Integer.valueOf(s);
      assert r != null;
      this.stencil = Option.some(r);
    }

    @Override public OptionType<VectorReadable4FType> getColorBufferClear()
    {
      return this.color;
    }

    @Override public OptionType<Float> getDepthBufferClear()
    {
      return this.depth;
    }

    @Override public OptionType<Integer> getStencilBufferClear()
    {
      return this.stencil;
    }

    @Override public boolean getStrictChecking()
    {
      return this.strict;
    }

    @Override public void setStrictChecking(
      final boolean enabled)
    {
      this.strict = enabled;
    }
  }

  /**
   * @return A new specification builder
   */

  public static ClearSpecificationBuilderType newBuilder()
  {
    return new Builder();
  }

  /**
   * @param s
   *          An existing specification
   * @return A new specification builder based on the given specification
   */

  public static ClearSpecificationBuilderType newBuilderFrom(
    final ClearSpecification s)
  {
    return new Builder(s);
  }

  private final OptionType<VectorReadable4FType> color;
  private final OptionType<Float>                depth;
  private final OptionType<Integer>              stencil;
  private final boolean                          strict;

  private ClearSpecification(
    final OptionType<Float> in_depth,
    final OptionType<Integer> in_stencil,
    final OptionType<VectorReadable4FType> in_color,
    final boolean in_strict)
  {
    this.depth = NullCheck.notNull(in_depth, "Depth");
    this.stencil = NullCheck.notNull(in_stencil, "Stencil");
    this.color = NullCheck.notNull(in_color, "Color");
    this.strict = in_strict;
  }

  /**
   * @return <code>Some(c)</code> if the color buffer should be cleared to
   *         <code>c</code>
   */

  public OptionType<VectorReadable4FType> getColor()
  {
    return this.color;
  }

  /**
   * @return <code>Some(f)</code> if the depth buffer should be cleared to
   *         <code>f</code>
   */

  public OptionType<Float> getDepth()
  {
    return this.depth;
  }

  /**
   * @return <code>Some(s)</code> if the stencil buffer should be cleared to
   *         <code>s</code>
   */

  public OptionType<Integer> getStencil()
  {
    return this.stencil;
  }

  /**
   * @return <code>true</code> if strict checking should occur
   */

  public boolean isStrict()
  {
    return this.strict;
  }
}
