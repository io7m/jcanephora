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

import javax.annotation.Nonnull;

import com.io7m.jaux.RangeInclusive;

/**
 * A read-only interface to the {@link Texture2DStatic} type that allows use
 * of the type but not mutation and/or deletion of the contents.
 */

public interface Texture2DStaticUsable extends GLName, GLResourceUsable
{
  /**
   * Retrieve the inclusive area of this texture.
   */

  public @Nonnull AreaInclusive getArea();

  /**
   * Return the height in pixels of the texture.
   */

  public abstract int getHeight();

  /**
   * Retrieve the magnification filter used for the texture.
   */

  public @Nonnull TextureFilter getMagnificationFilter();

  /**
   * Retrieve the minification filter used for the texture.
   */

  public @Nonnull TextureFilter getMinificationFilter();

  /**
   * Retrieve the name of the texture.
   */

  public @Nonnull String getName();

  /**
   * Return the range of valid indices on the X axis.
   */

  public @Nonnull RangeInclusive getRangeX();

  /**
   * Return the range of valid indices on the Y axis.
   */

  public @Nonnull RangeInclusive getRangeY();

  /**
   * Retrieve the type of the texture.
   */

  public @Nonnull TextureType getType();

  /**
   * Retrieve the width in pixels of the texture.
   */

  public abstract int getWidth();

  /**
   * Retrieve the wrapping mode used on the S axis of the texture.
   */

  public @Nonnull TextureWrap getWrapS();

  /**
   * Retrieve the wrapping mode used on the T axis of the texture.
   */

  public @Nonnull TextureWrap getWrapT();
}
