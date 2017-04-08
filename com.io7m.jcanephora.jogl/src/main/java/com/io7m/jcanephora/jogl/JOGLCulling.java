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

package com.io7m.jcanephora.jogl;

import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLFaceSelection;
import com.io7m.jcanephora.core.JCGLFaceWindingOrder;
import com.io7m.jcanephora.core.api.JCGLCullingType;
import com.io7m.jnull.NullCheck;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class JOGLCulling implements JCGLCullingType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(JOGLCulling.class);
  }

  private final GL3 gl;
  private boolean enabled;
  private JCGLFaceSelection current_faces;
  private JCGLFaceWindingOrder current_order;

  JOGLCulling(final JOGLContext c)
  {
    final JOGLContext context = NullCheck.notNull(c, "Context");
    this.gl = context.getGL3();
    this.enabled = false;
    this.current_faces = JCGLFaceSelection.FACE_BACK;
    this.current_order = JCGLFaceWindingOrder.FRONT_FACE_COUNTER_CLOCKWISE;

    /*
     * Configure baseline defaults.
     */

    this.gl.glDisable(GL.GL_CULL_FACE);
    this.gl.glCullFace(
      JOGLTypeConversions.faceSelectionToGL(this.current_faces));
    this.gl.glFrontFace(
      JOGLTypeConversions.faceWindingOrderToGL(this.current_order));
    JOGLErrorChecking.checkErrors(this.gl);
  }

  @Override
  public void cullingDisable()
    throws JCGLException
  {
    if (this.enabled) {
      this.gl.glDisable(GL.GL_CULL_FACE);
      this.enabled = false;
    } else {
      LOG.trace("redundant culling disable ignored");
    }
  }

  @Override
  public void cullingEnable(
    final JCGLFaceSelection faces,
    final JCGLFaceWindingOrder order)
    throws JCGLException
  {
    NullCheck.notNull(faces, "Face selection");
    NullCheck.notNull(order, "Face winding order");

    final int fi = JOGLTypeConversions.faceSelectionToGL(faces);
    final int oi = JOGLTypeConversions.faceWindingOrderToGL(order);

    if (!this.enabled) {
      this.gl.glEnable(GL.GL_CULL_FACE);
      this.enabled = true;
    } else {
      LOG.trace("redundant culling enable ignored");
    }
    if (this.current_faces != faces) {
      this.gl.glCullFace(fi);
      this.current_faces = faces;
    } else {
      LOG.trace("redundant culling face selection ignored");
    }
    if (this.current_order != order) {
      this.gl.glFrontFace(oi);
      this.current_order = order;
    } else {
      LOG.trace("redundant culling face order ignored");
    }
  }

  @Override
  public boolean cullingIsEnabled()
    throws JCGLException
  {
    return this.enabled;
  }
}
