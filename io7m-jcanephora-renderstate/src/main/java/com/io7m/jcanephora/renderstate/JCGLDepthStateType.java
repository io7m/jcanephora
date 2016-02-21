/*
 * Copyright © 2016 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.renderstate;

import com.io7m.jcanephora.core.JCGLDepthFunction;
import com.io7m.jcanephora.core.JCGLImmutableStyleType;
import org.immutables.value.Value;

import java.util.Optional;

/**
 * Depth testing/writing state.
 */

@JCGLImmutableStyleType
@Value.Immutable
@Value.Modifiable
public interface JCGLDepthStateType
{
  /**
   * Enable or disable strict mode. If strict mode is enabled, then attempting
   * to enable depth writing/testing/clamping will fail with an exception if the
   * currently bound framebuffer does not have a depth buffer.
   *
   * @return {@link JCGLDepthStrict#DEPTH_STRICT_ENABLED} iff strict mode is
   * enabled
   */

  @Value.Parameter
  @Value.Default
  default JCGLDepthStrict getDepthStrict()
  {
    return JCGLDepthStrict.DEPTH_STRICT_DISABLED;
  }

  /**
   * @return The depth testing function, if testing is enabled
   */

  @Value.Parameter
  @Value.Default
  default Optional<JCGLDepthFunction> getDepthTest()
  {
    return Optional.empty();
  }

  /**
   * @return {@link JCGLDepthWriting#DEPTH_WRITE_ENABLED} if depth writing is
   * enabled
   */

  @Value.Parameter
  @Value.Default
  default JCGLDepthWriting getDepthWrite()
  {
    return JCGLDepthWriting.DEPTH_WRITE_DISABLED;
  }

  /**
   * @return {@link JCGLDepthClamping#DEPTH_CLAMP_ENABLED} if depth clamping is
   * enabled
   */

  @Value.Parameter
  @Value.Default
  default JCGLDepthClamping getDepthClamp()
  {
    return JCGLDepthClamping.DEPTH_CLAMP_DISABLED;
  }
}
