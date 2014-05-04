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

package com.io7m.jcanephora.api;

import java.util.Map;

import com.io7m.jcanephora.FragmentShaderType;
import com.io7m.jcanephora.FramebufferDrawBuffer;
import com.io7m.jcanephora.JCGLExceptionCompileError;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.ProgramType;
import com.io7m.jcanephora.VertexShaderType;

/**
 * The interface to the shading program functionality in OpenGL 3.*.
 */

public interface JCGLShadersGL3Type extends JCGLShadersCommonType
{
  /**
   * <p>
   * Create a new empty program named <code>name</code>, attach <code>v</code>
   * as the vertex shader and <code>f</code> as the fragment shader, bind the
   * named fragment shader outputs to the draw buffers given in
   * <code>outputs</code>, link the program, and return a reference to the
   * linked program.
   * </p>
   * <p>
   * This function is only necessary when using fragment shaders that have
   * multiple outputs; a program that only has a single output is
   * automatically mapped to draw buffer 0.
   * </p>
   * 
   * @param name
   *          The name of the program.
   * @param v
   *          A compiled vertex shader.
   * @param f
   *          A compiled fragment shader.
   * @param outputs
   *          A map associating fragment shader output names with draw
   *          buffers.
   * 
   * @return A reference to the created program.
   * @see JCGLFramebuffersCommonType#framebufferGetDrawBuffers()
   * 
   * @throws JCGLExceptionRuntime
   *           Iff an OpenGL error occurs.
   * @throws JCGLExceptionCompileError
   *           Iff the program fails to link.
   */

  ProgramType programCreateWithOutputs(
    final String name,
    final VertexShaderType v,
    final FragmentShaderType f,
    final Map<String, FramebufferDrawBuffer> outputs)
    throws JCGLExceptionRuntime,
      JCGLExceptionCompileError;
}
