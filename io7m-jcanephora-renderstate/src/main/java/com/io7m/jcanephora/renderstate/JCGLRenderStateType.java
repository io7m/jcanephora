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

import com.io7m.jareas.core.AreaInclusiveUnsignedLType;
import com.io7m.jcanephora.core.JCGLImmutableStyleType;
import com.io7m.jcanephora.core.JCGLPolygonMode;
import org.immutables.value.Value;

import java.util.Optional;

/**
 * The type of render states.
 */

@JCGLImmutableStyleType
@Value.Immutable
@Value.Modifiable
public interface JCGLRenderStateType
{
  /**
   * @return The blending state, if blending is enabled
   */

  @Value.Parameter
  @Value.Default
  default Optional<JCGLBlendStateType> getBlendState()
  {
    return Optional.empty();
  }

  /**
   * @return The culling state, if culling is enabled
   */

  @Value.Parameter
  @Value.Default
  default Optional<JCGLCullingStateType> getCullingState()
  {
    return Optional.empty();
  }

  /**
   * @return The color buffer masking state
   */

  @Value.Parameter
  @Value.Default
  default JCGLColorBufferMaskingStateType getColorBufferMaskingState()
  {
    return JCGLColorBufferMaskingStateMutable.create(true, true, true, true);
  }

  /**
   * @return The polygon rendering mode
   */

  @Value.Parameter
  @Value.Default
  default JCGLPolygonMode getPolygonMode()
  {
    return JCGLPolygonMode.POLYGON_FILL;
  }

  /**
   * @return The scissor region, if scissoring is enabled
   */

  @Value.Parameter
  @Value.Default
  default Optional<AreaInclusiveUnsignedLType> getScissor()
  {
    return Optional.empty();
  }

  /**
   * @return The depth buffer state
   */

  @Value.Parameter
  @Value.Default
  default JCGLDepthStateType getDepthState()
  {
    return JCGLDepthStateMutable.create();
  }

  /**
   * @return The stencil buffer state
   */

  @Value.Parameter
  @Value.Default
  default JCGLStencilStateType getStencilState()
  {
    return JCGLStencilStateMutable.create();
  }
}
