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
import com.io7m.jcanephora.core.JCGLExceptionNoStencilBuffer;
import com.io7m.jcanephora.core.JCGLFaceSelection;
import com.io7m.jcanephora.core.JCGLStencilFunction;
import com.io7m.jcanephora.core.JCGLStencilOperation;
import com.io7m.jcanephora.core.api.JCGLStencilBuffersType;
import com.io7m.jnull.NullCheck;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLDrawable;

final class JOGLStencilBuffers implements JCGLStencilBuffersType
{
  private final JOGLFramebuffers framebuffers;
  private final GL3              gl;
  private       boolean          enabled;

  private int                  front_mask;
  private JCGLStencilOperation front_sfail;
  private JCGLStencilOperation front_dfail;
  private JCGLStencilOperation front_dpass;
  private JCGLStencilFunction  front_func;
  private int                  front_func_ref;
  private int                  front_func_mask;

  private int                  back_mask;
  private JCGLStencilOperation back_sfail;
  private JCGLStencilOperation back_dfail;
  private JCGLStencilOperation back_dpass;
  private JCGLStencilFunction  back_func;
  private int                  back_func_ref;
  private int                  back_func_mask;

  JOGLStencilBuffers(
    final JOGLContext c,
    final JOGLFramebuffers f)
  {
    NullCheck.notNull(c);
    this.framebuffers = NullCheck.notNull(f);
    this.gl = c.getGL3();
    this.enabled = false;

    this.back_mask = 0b11111111_11111111_11111111_11111111;
    this.back_sfail = JCGLStencilOperation.STENCIL_OP_KEEP;
    this.back_dfail = JCGLStencilOperation.STENCIL_OP_KEEP;
    this.back_dpass = JCGLStencilOperation.STENCIL_OP_KEEP;
    this.back_func = JCGLStencilFunction.STENCIL_ALWAYS;
    this.back_func_mask = 0b11111111_11111111_11111111_11111111;
    this.back_func_ref = 0;

    this.front_mask = 0b11111111_11111111_11111111_11111111;
    this.front_sfail = JCGLStencilOperation.STENCIL_OP_KEEP;
    this.front_dfail = JCGLStencilOperation.STENCIL_OP_KEEP;
    this.front_dpass = JCGLStencilOperation.STENCIL_OP_KEEP;
    this.front_func = JCGLStencilFunction.STENCIL_ALWAYS;
    this.front_func_mask = 0b11111111_11111111_11111111_11111111;
    this.front_func_ref = 0;
  }

  private void checkStencilBits()
  {
    if (this.getStencilBits() == 0) {
      throw new JCGLExceptionNoStencilBuffer("No stencil buffer is available");
    }
  }

  private int getStencilBits()
  {
    final JOGLFramebuffer fb = this.framebuffers.getBindDraw();
    if (fb != null) {
      return fb.framebufferGetStencilBits();
    }

    /**
     * Check the capabilities of the OpenGL context for the capabilities
     * of the default framebuffer.
     */

    final GLContext c = this.gl.getContext();
    final GLDrawable drawable = c.getGLDrawable();
    return drawable.getChosenGLCapabilities().getStencilBits();
  }

  @Override public void stencilBufferClear(final int stencil)
    throws JCGLException, JCGLExceptionNoStencilBuffer
  {
    this.checkStencilBits();
    this.gl.glClearStencil(stencil);
    this.gl.glClear(GL.GL_STENCIL_BUFFER_BIT);
  }

  @Override public void stencilBufferDisable()
    throws JCGLException, JCGLExceptionNoStencilBuffer
  {
    this.checkStencilBits();
    if (this.enabled) {
      this.gl.glDisable(GL.GL_STENCIL_TEST);
      this.enabled = false;
    }
  }

  @Override public void stencilBufferEnable()
    throws JCGLException, JCGLExceptionNoStencilBuffer
  {
    this.checkStencilBits();
    if (!this.enabled) {
      this.gl.glEnable(GL.GL_STENCIL_TEST);
      this.enabled = true;
    }
  }

  @Override public void stencilBufferFunction(
    final JCGLFaceSelection faces,
    final JCGLStencilFunction function,
    final int reference,
    final int mask)
    throws JCGLException, JCGLExceptionNoStencilBuffer
  {
    NullCheck.notNull(faces, "Faces");
    NullCheck.notNull(function, "Function");

    this.checkStencilBits();
    final int func = JOGLTypeConversions.stencilFunctionToGL(function);
    final int face = JOGLTypeConversions.faceSelectionToGL(faces);
    this.gl.glStencilFuncSeparate(face, func, reference, mask);

    switch (faces) {
      case FACE_BACK: {
        this.back_func = function;
        this.back_func_ref = reference;
        this.back_func_mask = mask;
        break;
      }
      case FACE_FRONT: {
        this.front_func = function;
        this.front_func_ref = reference;
        this.front_func_mask = mask;
        break;
      }
      case FACE_FRONT_AND_BACK: {
        this.front_func = function;
        this.front_func_ref = reference;
        this.front_func_mask = mask;
        this.back_func = function;
        this.back_func_ref = reference;
        this.back_func_mask = mask;
        break;
      }
    }
  }

  @Override public int stencilBufferGetBits()
    throws JCGLException
  {
    return this.getStencilBits();
  }

  @Override public boolean stencilBufferIsEnabled()
    throws JCGLException, JCGLExceptionNoStencilBuffer
  {
    this.checkStencilBits();
    return this.enabled;
  }

  @Override public void stencilBufferMask(
    final JCGLFaceSelection faces,
    final int mask)
    throws JCGLException, JCGLExceptionNoStencilBuffer
  {
    NullCheck.notNull(faces);

    this.checkStencilBits();
    this.gl.glStencilMaskSeparate(
      JOGLTypeConversions.faceSelectionToGL(faces), mask);

    switch (faces) {
      case FACE_BACK: {
        this.back_mask = mask;
        break;
      }
      case FACE_FRONT: {
        this.front_mask = mask;
        break;
      }
      case FACE_FRONT_AND_BACK: {
        this.back_mask = mask;
        this.front_mask = mask;
        break;
      }
    }
  }

  @Override public void stencilBufferOperation(
    final JCGLFaceSelection faces,
    final JCGLStencilOperation stencil_fail,
    final JCGLStencilOperation depth_fail,
    final JCGLStencilOperation pass)
    throws JCGLException
  {
    NullCheck.notNull(faces, "Face selection");
    NullCheck.notNull(stencil_fail, "Stencil fail operation");
    NullCheck.notNull(depth_fail, "Depth fail operation");
    NullCheck.notNull(pass, "Pass operation");

    this.checkStencilBits();
    final int sfail = JOGLTypeConversions.stencilOperationToGL(stencil_fail);
    final int dfail = JOGLTypeConversions.stencilOperationToGL(depth_fail);
    final int dpass = JOGLTypeConversions.stencilOperationToGL(pass);
    final int gface = JOGLTypeConversions.faceSelectionToGL(faces);
    this.gl.glStencilOpSeparate(gface, sfail, dfail, dpass);

    switch (faces) {
      case FACE_BACK: {
        this.back_sfail = stencil_fail;
        this.back_dfail = depth_fail;
        this.back_dpass = pass;
        break;
      }
      case FACE_FRONT: {
        this.front_sfail = stencil_fail;
        this.front_dfail = depth_fail;
        this.front_dpass = pass;
        break;
      }
      case FACE_FRONT_AND_BACK: {
        this.front_sfail = stencil_fail;
        this.front_dfail = depth_fail;
        this.front_dpass = pass;

        this.back_sfail = stencil_fail;
        this.back_dfail = depth_fail;
        this.back_dpass = pass;
        break;
      }
    }
  }

  @Override public int stencilBufferGetMaskFrontFaces()
  {
    return this.front_mask;
  }

  @Override public int stencilBufferGetMaskBackFaces()
  {
    return this.back_mask;
  }

  @Override
  public JCGLStencilOperation stencilBufferGetOperationStencilFailBack()
  {
    return this.back_sfail;
  }

  @Override public JCGLStencilOperation stencilBufferGetOperationDepthFailBack()
  {
    return this.back_dfail;
  }

  @Override public JCGLStencilOperation stencilBufferGetOperationPassBack()
  {
    return this.back_dpass;
  }

  @Override
  public JCGLStencilOperation stencilBufferGetOperationStencilFailFront()
  {
    return this.front_sfail;
  }

  @Override
  public JCGLStencilOperation stencilBufferGetOperationDepthFailFront()
  {
    return this.front_dfail;
  }

  @Override public JCGLStencilOperation stencilBufferGetOperationPassFront()
  {
    return this.front_dpass;
  }

  @Override public JCGLStencilFunction stencilBufferGetFunctionFront()
  {
    return this.front_func;
  }

  @Override public JCGLStencilFunction stencilBufferGetFunctionBack()
  {
    return this.back_func;
  }

  @Override public int stencilBufferGetFunctionReferenceFront()
  {
    return this.front_func_ref;
  }

  @Override public int stencilBufferGetFunctionMaskFront()
  {
    return this.front_func_mask;
  }

  @Override public int stencilBufferGetFunctionReferenceBack()
  {
    return this.back_func_ref;
  }

  @Override public int stencilBufferGetFunctionMaskBack()
  {
    return this.back_func_mask;
  }
}
