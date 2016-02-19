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

import com.io7m.jcanephora.core.JCGLImmutableStyleType;
import com.io7m.jcanephora.core.JCGLStencilFunction;
import com.io7m.jcanephora.core.JCGLStencilOperation;
import org.immutables.value.Value;

/**
 * Stencil testing/writing state.
 */

@JCGLImmutableStyleType
@Value.Immutable
@Value.Modifiable
public interface JCGLStencilStateType
{
  /**
   * @return {@code true} if stencilling is enabled
   */

  @Value.Parameter
  @Value.Default
  default boolean getStencilEnabled()
  {
    return false;
  }

  /**
   * Enable or disable strict mode. If strict mode is enabled, then attempting
   * to enable stencil writing/testing will fail with an exception if the
   * currently bound framebuffer does not have a stencil buffer.
   *
   * @return {@code true} iff strict mode is enabled
   */

  @Value.Parameter
  @Value.Default
  default boolean getStencilStrict()
  {
    return false;
  }

  /**
   * @return The stencil function for front faces
   */

  @Value.Parameter
  @Value.Default
  default JCGLStencilFunction getTestFunctionFront()
  {
    return JCGLStencilFunction.STENCIL_ALWAYS;
  }

  /**
   * @return The stencil function for back faces
   */

  @Value.Parameter
  @Value.Default
  default JCGLStencilFunction getTestFunctionBack()
  {
    return JCGLStencilFunction.STENCIL_ALWAYS;
  }

  /**
   * @return The stencil function mask value for back faces
   */

  @Value.Parameter
  @Value.Default
  default int getTestMaskBack()
  {
    return 0b11111111_11111111_11111111_11111111;
  }

  /**
   * @return The stencil function mask value for front faces
   */

  @Value.Parameter
  @Value.Default
  default int getTestMaskFront()
  {
    return 0b11111111_11111111_11111111_11111111;
  }

  /**
   * @return The stencil function reference value for back faces
   */

  @Value.Parameter
  @Value.Default
  default int getTestReferenceBack()
  {
    return 0;
  }

  /**
   * @return The stencil function reference value for front faces
   */

  @Value.Parameter
  @Value.Default
  default int getTestReferenceFront()
  {
    return 0;
  }

  /**
   * @return The operation specified for back faces that fail the stencil test
   */

  @Value.Parameter
  @Value.Default
  default JCGLStencilOperation getOperationStencilFailBack()
  {
    return JCGLStencilOperation.STENCIL_OP_KEEP;
  }

  /**
   * @return The operation specified for back faces that fail the depth test
   */

  @Value.Parameter
  @Value.Default
  default JCGLStencilOperation getOperationDepthFailBack()
  {
    return JCGLStencilOperation.STENCIL_OP_KEEP;
  }

  /**
   * @return The operation specified for back faces that pass all tests
   */

  @Value.Parameter
  @Value.Default
  default JCGLStencilOperation getOperationPassBack()
  {
    return JCGLStencilOperation.STENCIL_OP_KEEP;
  }

  /**
   * @return The operation specified for front faces that fail the stencil test
   */

  @Value.Parameter
  @Value.Default
  default JCGLStencilOperation getOperationStencilFailFront()
  {
    return JCGLStencilOperation.STENCIL_OP_KEEP;
  }

  /**
   * @return The operation specified for front faces that fail the depth test
   */

  @Value.Parameter
  @Value.Default
  default JCGLStencilOperation getOperationDepthFailFront()
  {
    return JCGLStencilOperation.STENCIL_OP_KEEP;
  }

  /**
   * @return The operation specified for front faces that pass all tests
   */

  @Value.Parameter
  @Value.Default
  default JCGLStencilOperation getOperationPassFront()
  {
    return JCGLStencilOperation.STENCIL_OP_KEEP;
  }

  /**
   * @return The write mask value specified for front faces
   */

  @Value.Parameter
  @Value.Default
  default int getWriteMaskFrontFaces()
  {
    return 0b11111111_11111111_11111111_11111111;
  }

  /**
   * @return The write mask value specified for back faces
   */

  @Value.Parameter
  @Value.Default
  default int getWriteMaskBackFaces()
  {
    return 0b11111111_11111111_11111111_11111111;
  }
}
