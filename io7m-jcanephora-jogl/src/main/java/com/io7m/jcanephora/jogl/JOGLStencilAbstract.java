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
import javax.media.opengl.GL2ES2;

import com.io7m.jcanephora.FaceSelection;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionNoStencilBuffer;
import com.io7m.jcanephora.StencilFunction;
import com.io7m.jcanephora.StencilOperation;
import com.io7m.jcanephora.api.JCGLStencilBufferType;
import com.io7m.jlog.LogType;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;

abstract class JOGLStencilAbstract implements JCGLStencilBufferType
{
  private final GL      gl;
  private final LogType log;

  JOGLStencilAbstract(
    final GL in_gl,
    final LogUsableType in_log)
  {
    this.gl = NullCheck.notNull(in_gl, "GL");
    this.log = NullCheck.notNull(in_log, "Log").with("stencil");
  }

  protected final void checkStencilBuffer()
    throws JCGLException
  {
    final int bits = this.stencilBufferGetBits();
    if (bits == 0) {
      throw new JCGLExceptionNoStencilBuffer("No stencil buffer available");
    }
  }

  protected final GL getGL()
  {
    return this.gl;
  }

  protected final LogType getLog()
  {
    return this.log;
  }

  @Override public final void stencilBufferClear(
    final int stencil)
    throws JCGLException,
      JCGLExceptionNoStencilBuffer
  {
    this.checkStencilBuffer();
    this.gl.glClearStencil(stencil);
    JOGLErrors.check(this.gl);
  }

  @Override public final void stencilBufferDisable()
    throws JCGLException,
      JCGLExceptionNoStencilBuffer
  {
    this.checkStencilBuffer();
    this.gl.glDisable(GL.GL_STENCIL_TEST);
    JOGLErrors.check(this.gl);
  }

  @Override public final void stencilBufferEnable()
    throws JCGLException,
      JCGLExceptionNoStencilBuffer
  {
    this.checkStencilBuffer();
    this.gl.glEnable(GL.GL_STENCIL_TEST);
    JOGLErrors.check(this.gl);
  }

  @Override public final void stencilBufferFunction(
    final FaceSelection faces,
    final StencilFunction function,
    final int reference,
    final int mask)
    throws JCGLException,
      JCGLExceptionNoStencilBuffer
  {
    NullCheck.notNull(faces, "Faces");
    NullCheck.notNull(function, "Function");

    this.checkStencilBuffer();

    final GL2ES2 g2 = this.gl.getGL2ES2();
    final int func = JOGLTypeConversions.stencilFunctionToGL(function);
    g2.glStencilFuncSeparate(
      JOGLTypeConversions.faceSelectionToGL(faces),
      func,
      reference,
      mask);
    JOGLErrors.check(this.gl);
  }

  @Override public final boolean stencilBufferIsEnabled()
    throws JCGLException,
      JCGLExceptionNoStencilBuffer
  {
    this.checkStencilBuffer();
    final boolean e = this.gl.glIsEnabled(GL.GL_STENCIL_TEST);
    JOGLErrors.check(this.gl);
    return e;
  }

  @Override public final void stencilBufferMask(
    final FaceSelection faces,
    final int mask)
    throws JCGLException,
      JCGLExceptionNoStencilBuffer
  {
    NullCheck.notNull(faces, "Faces");

    this.checkStencilBuffer();

    final GL2ES2 g2 = this.gl.getGL2ES2();
    g2.glStencilMaskSeparate(
      JOGLTypeConversions.faceSelectionToGL(faces),
      mask);
    JOGLErrors.check(this.gl);
  }

  @Override public final void stencilBufferOperation(
    final FaceSelection faces,
    final StencilOperation stencil_fail,
    final StencilOperation depth_fail,
    final StencilOperation pass)
    throws JCGLException
  {
    NullCheck.notNull(faces, "Face selection");
    NullCheck.notNull(stencil_fail, "Stencil fail operation");
    NullCheck.notNull(depth_fail, "Depth fail operation");
    NullCheck.notNull(pass, "Pass operation");

    final GL2ES2 g2 = this.gl.getGL2ES2();
    final int sfail = JOGLTypeConversions.stencilOperationToGL(stencil_fail);
    final int dfail = JOGLTypeConversions.stencilOperationToGL(depth_fail);
    final int dpass = JOGLTypeConversions.stencilOperationToGL(pass);
    g2.glStencilOpSeparate(
      JOGLTypeConversions.faceSelectionToGL(faces),
      sfail,
      dfail,
      dpass);
    JOGLErrors.check(this.gl);
  }
}
