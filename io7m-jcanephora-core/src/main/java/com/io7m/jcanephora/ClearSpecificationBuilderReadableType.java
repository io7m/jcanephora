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

import com.io7m.jfunctional.OptionType;
import com.io7m.jtensors.VectorReadable4FType;

/**
 * The type of readable {@link ClearSpecification} builders.
 */

public interface ClearSpecificationBuilderReadableType
{
  /**
   * @return The value to which the depth buffer will be cleared, if any.
   */

  OptionType<Float> getDepthBufferClear();

  /**
   * @return The value to which the stencil buffer will be cleared, if any.
   */

  OptionType<Integer> getStencilBufferClear();

  /**
   * @return The value to which the color buffer(s) will be cleared, if any.
   */

  OptionType<VectorReadable4FType> getColorBufferClear();

  /**
   * @return <code>true</code> if strict checking is enabled.
   */

  boolean getStrictChecking();
}
