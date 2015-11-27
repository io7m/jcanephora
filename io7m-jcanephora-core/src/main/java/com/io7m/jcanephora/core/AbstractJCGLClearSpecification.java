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

package com.io7m.jcanephora.core;

import com.io7m.jtensors.VectorReadable4FType;
import org.immutables.value.Value;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;

@Value.Immutable @JCGLImmutableStyleType
abstract class AbstractJCGLClearSpecification
{
  /**
   * @return The color to which the color buffer will be cleared, if color
   * buffer clearing is enabled
   */

  abstract Optional<VectorReadable4FType> getColorBufferClear();

  /**
   * @return The depth value to which the depth buffer will be cleared, if depth
   * buffer clearing is enabled
   */

  abstract OptionalDouble getDepthBufferClear();

  /**
   * @return The stencil value to which the stencil buffer will be cleared, if
   * stencil buffer clearing is enabled
   */

  abstract OptionalInt getStencilBufferClear();

  /**
   * @return {@code true} if strict buffer checking is enabled
   */

  @Value.Default boolean getStrictChecking()
  {
    return false;
  }
}
