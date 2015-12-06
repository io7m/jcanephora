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

package com.io7m.jcanephora.fake;

import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLFaceSelection;
import com.io7m.jcanephora.core.JCGLFaceWindingOrder;
import com.io7m.jcanephora.core.api.JCGLCullingType;
import com.io7m.jnull.NullCheck;

final class FakeCulling implements JCGLCullingType
{
  private boolean              enabled;
  private JCGLFaceSelection    current_faces;
  private JCGLFaceWindingOrder current_order;

  FakeCulling(final FakeContext c)
  {
    final FakeContext context = NullCheck.notNull(c);
    this.enabled = false;
    this.current_faces = JCGLFaceSelection.FACE_BACK;
    this.current_order = JCGLFaceWindingOrder.FRONT_FACE_COUNTER_CLOCKWISE;
  }

  @Override public void cullingDisable()
    throws JCGLException
  {
    if (this.enabled) {
      this.enabled = false;
    }
  }

  @Override public void cullingEnable(
    final JCGLFaceSelection faces,
    final JCGLFaceWindingOrder order)
    throws JCGLException
  {
    NullCheck.notNull(faces, "Face selection");
    NullCheck.notNull(order, "Face winding order");

    if (!this.enabled) {
      this.enabled = true;
    }
    if (this.current_faces != faces) {
      this.current_faces = faces;
    }
    if (this.current_order != order) {
      this.current_order = order;
    }
  }

  @Override public boolean cullingIsEnabled()
    throws JCGLException
  {
    return this.enabled;
  }
}
