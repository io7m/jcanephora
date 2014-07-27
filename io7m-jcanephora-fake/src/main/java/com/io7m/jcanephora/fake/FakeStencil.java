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
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionNoStencilBuffer;
import com.io7m.jcanephora.StencilFunction;
import com.io7m.jcanephora.StencilOperation;
import com.io7m.jcanephora.api.JCGLStencilBufferType;
import com.io7m.jlog.LogType;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;

final class FakeStencil implements JCGLStencilBufferType
{
  private StencilFunction        back_function;
  private StencilOperation       back_op_depth_fail;
  private StencilOperation       back_op_pass;
  private StencilOperation       back_op_stencil_fail;
  private int                    current_mask;
  private int                    current_reference;
  private boolean                enabled;
  private final FakeFramebuffers framebuffers;
  private StencilFunction        front_function;
  private StencilOperation       front_op_depth_fail;
  private StencilOperation       front_op_pass;
  private StencilOperation       front_op_stencil_fail;
  private final FakeContext      gl;
  private final LogType          log;

  FakeStencil(
    final FakeContext in_gl,
    final FakeFramebuffers in_framebuffers,
    final LogUsableType in_log)
  {
    this.gl = NullCheck.notNull(in_gl, "FakeContext");
    this.log = NullCheck.notNull(in_log, "Log").with("stencil");
    this.framebuffers = NullCheck.notNull(in_framebuffers, "Framebuffers");

    this.back_function = StencilFunction.STENCIL_ALWAYS;
    this.front_function = StencilFunction.STENCIL_ALWAYS;
    this.current_reference = 0;
    this.current_mask = 0xFFFFFFFF;
    this.back_op_depth_fail = StencilOperation.STENCIL_OP_KEEP;
    this.back_op_pass = StencilOperation.STENCIL_OP_KEEP;
    this.back_op_stencil_fail = StencilOperation.STENCIL_OP_KEEP;
    this.front_op_depth_fail = StencilOperation.STENCIL_OP_KEEP;
    this.front_op_pass = StencilOperation.STENCIL_OP_KEEP;
    this.front_op_stencil_fail = StencilOperation.STENCIL_OP_KEEP;
  }

  void checkStencilBuffer()
    throws JCGLException
  {
    final int bits = this.stencilBufferGetBits();
    if (bits == 0) {
      throw new JCGLExceptionNoStencilBuffer("No stencil buffer available");
    }
  }

  FakeContext getGL()
  {
    return this.gl;
  }

  LogType getLog()
  {
    return this.log;
  }

  @Override public void stencilBufferClear(
    final int stencil)
    throws JCGLException,
      JCGLExceptionNoStencilBuffer
  {
    this.checkStencilBuffer();
  }

  @Override public void stencilBufferDisable()
    throws JCGLException,
      JCGLExceptionNoStencilBuffer
  {
    this.checkStencilBuffer();
    this.enabled = false;
  }

  @Override public void stencilBufferEnable()
    throws JCGLException,
      JCGLExceptionNoStencilBuffer
  {
    this.checkStencilBuffer();
    this.enabled = true;
  }

  @Override public void stencilBufferFunction(
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

    switch (faces) {
      case FACE_BACK:
      {
        this.back_function = function;
        break;
      }
      case FACE_FRONT:
      {
        this.front_function = function;
        break;
      }
      case FACE_FRONT_AND_BACK:
      {
        this.front_function = function;
        this.back_function = function;
        break;
      }
    }

    this.current_reference = reference;
    this.current_mask = mask;
  }

  @Override public int stencilBufferGetBits()
    throws JCGLException
  {
    return this.framebuffers.getBitsStencil();
  }

  @Override public boolean stencilBufferIsEnabled()
    throws JCGLException,
      JCGLExceptionNoStencilBuffer
  {
    this.checkStencilBuffer();
    return this.enabled;
  }

  @Override public void stencilBufferMask(
    final FaceSelection faces,
    final int mask)
    throws JCGLException,
      JCGLExceptionNoStencilBuffer
  {
    NullCheck.notNull(faces, "Faces");

    this.checkStencilBuffer();
    this.current_mask = mask;
  }

  @Override public void stencilBufferOperation(
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

    switch (faces) {
      case FACE_BACK:
      {
        this.back_op_stencil_fail = stencil_fail;
        this.back_op_depth_fail = depth_fail;
        this.back_op_pass = pass;
        break;
      }
      case FACE_FRONT:
      {
        this.front_op_stencil_fail = stencil_fail;
        this.front_op_depth_fail = depth_fail;
        this.front_op_pass = pass;
        break;
      }
      case FACE_FRONT_AND_BACK:
      {
        this.back_op_stencil_fail = stencil_fail;
        this.back_op_depth_fail = depth_fail;
        this.back_op_pass = pass;
        this.front_op_stencil_fail = stencil_fail;
        this.front_op_depth_fail = depth_fail;
        this.front_op_pass = pass;
        break;
      }
    }
  }
}
