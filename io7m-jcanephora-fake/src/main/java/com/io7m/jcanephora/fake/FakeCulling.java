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

package com.io7m.jcanephora.fake;

import com.io7m.jcanephora.FaceSelection;
import com.io7m.jcanephora.FaceWindingOrder;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.api.JCGLCullType;
import com.io7m.jlog.LogType;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;

final class FakeCulling implements JCGLCullType
{
  private boolean          culling;
  private FaceSelection    current_faces;
  private FaceWindingOrder current_order;
  private final LogType    log;

  FakeCulling(
    final LogUsableType in_log)
  {
    this.log = NullCheck.notNull(in_log, "Log").with("culling");
    this.culling = false;
    this.current_faces = FaceSelection.FACE_BACK;
    this.current_order = FaceWindingOrder.FRONT_FACE_COUNTER_CLOCKWISE;
  }

  @Override public void cullingDisable()
    throws JCGLExceptionRuntime
  {
    this.culling = false;
  }

  @Override public void cullingEnable(
    final FaceSelection faces,
    final FaceWindingOrder order)
    throws JCGLExceptionRuntime
  {
    NullCheck.notNull(faces, "Face selection");
    NullCheck.notNull(order, "Face winding order");

    this.culling = true;
    this.current_faces = faces;
    this.current_order = order;
  }

  @Override public boolean cullingIsEnabled()
    throws JCGLExceptionRuntime
  {
    return this.culling;
  }
}
