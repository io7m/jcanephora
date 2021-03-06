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

import com.io7m.jcanephora.core.JCGLFaceSelection;
import com.io7m.jcanephora.core.JCGLFaceWindingOrder;
import com.io7m.jcanephora.core.JCGLImmutableStyleType;
import org.immutables.value.Value;

/**
 * Face culling state.
 */

@JCGLImmutableStyleType
@Value.Immutable
@Value.Modifiable
public interface JCGLCullingStateType
{
  /**
   * @return The faces that will be culled
   */

  @Value.Parameter
  @Value.Default
  default JCGLFaceSelection faceSelection()
  {
    return JCGLFaceSelection.FACE_BACK;
  }

  /**
   * @return The face winding order
   */

  @Value.Parameter
  @Value.Default
  default JCGLFaceWindingOrder faceWindingOrder()
  {
    return JCGLFaceWindingOrder.FRONT_FACE_COUNTER_CLOCKWISE;
  }
}
