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

package com.io7m.jcanephora.jogl;

import javax.media.opengl.GL;

import com.io7m.jcanephora.FaceSelection;
import com.io7m.jcanephora.FaceWindingOrder;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.api.JCGLCullType;
import com.io7m.jnull.NullCheck;

final class JOGLCulling implements JCGLCullType
{
  private static FaceSelection uncachedGetFaceSelection(
    final GL in_gl,
    final JOGLIntegerCacheType in_icache)
  {
    final int faces = in_icache.getInteger(in_gl, GL.GL_CULL_FACE_MODE);
    return JOGLTypeConversions.faceSelectionFromGL(faces);
  }

  private static FaceWindingOrder uncachedGetWindingOrder(
    final GL in_gl,
    final JOGLIntegerCacheType in_icache)
  {
    final int order = in_icache.getInteger(in_gl, GL.GL_FRONT_FACE);
    return JOGLTypeConversions.faceWindingOrderFromGL(order);
  }

  private static boolean uncachedIsEnabled(
    final GL in_gl)
  {
    return in_gl.glIsEnabled(GL.GL_CULL_FACE);
  }

  private final boolean    caching;
  private FaceSelection    current_faces;
  private FaceWindingOrder current_order;
  private boolean          enabled;
  private final GL         gl;

  JOGLCulling(
    final GL in_gl,
    final JOGLIntegerCacheType in_icache,
    final boolean in_caching)
  {
    this.gl = NullCheck.notNull(in_gl, "GL");
    NullCheck.notNull(in_icache, "Integer cache");

    this.caching = in_caching;
    this.current_faces =
      JOGLCulling.uncachedGetFaceSelection(in_gl, in_icache);
    this.current_order =
      JOGLCulling.uncachedGetWindingOrder(in_gl, in_icache);
    this.enabled = JOGLCulling.uncachedIsEnabled(in_gl);
  }

  @Override public void cullingDisable()
    throws JCGLExceptionRuntime
  {
    if (this.ignoreCache() || this.enabled) {
      this.gl.glDisable(GL.GL_CULL_FACE);
    }
    this.enabled = false;
  }

  @Override public void cullingEnable(
    final FaceSelection faces,
    final FaceWindingOrder order)
    throws JCGLExceptionRuntime
  {
    NullCheck.notNull(faces, "Face selection");
    NullCheck.notNull(order, "Face winding order");

    final int fi = JOGLTypeConversions.faceSelectionToGL(faces);
    final int oi = JOGLTypeConversions.faceWindingOrderToGL(order);

    if (this.ignoreCache() || (this.enabled == false)) {
      this.gl.glEnable(GL.GL_CULL_FACE);
    }
    if (this.ignoreCache() || (this.current_faces != faces)) {
      this.gl.glCullFace(fi);
    }
    if (this.ignoreCache() || (this.current_order != order)) {
      this.gl.glFrontFace(oi);
    }

    this.current_faces = faces;
    this.current_order = order;
    this.enabled = true;
  }

  @Override public boolean cullingIsEnabled()
    throws JCGLExceptionRuntime
  {
    if (this.ignoreCache()) {
      return JOGLCulling.uncachedIsEnabled(this.gl);
    }
    return this.enabled;
  }

  private boolean ignoreCache()
  {
    return this.caching == false;
  }
}
