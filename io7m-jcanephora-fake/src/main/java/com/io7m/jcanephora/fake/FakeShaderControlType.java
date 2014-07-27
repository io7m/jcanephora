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

import java.util.Map;

import com.io7m.jcanephora.FragmentShaderType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.ProgramAttributeType;
import com.io7m.jcanephora.ProgramUniformType;
import com.io7m.jcanephora.ProgramUsableType;
import com.io7m.jcanephora.VertexShaderType;

/**
 * <p>
 * Control over shader program arguments.
 * </p>
 */

public interface FakeShaderControlType
{
  /**
   * Called upon an attempt to compile a fragment shader.
   *
   * @param name
   *          The shader name.
   * @param v
   *          The compiled shader.
   * @throws JCGLException
   *           If required.
   */

  void onFragmentShaderCompile(
    final String name,
    final FragmentShaderType v)
    throws JCGLException;

  /**
   * Called upon an attempt to create a program.
   *
   * @param name
   *          The program name.
   * @param program
   *          The created program.
   * @param uniforms
   *          The program uniforms.
   * @param attributes
   *          The program attributes.
   * @throws JCGLException
   *           If required.
   */

  void onProgramCreate(
    final String name,
    final ProgramUsableType program,
    final Map<String, ProgramUniformType> uniforms,
    final Map<String, ProgramAttributeType> attributes)
    throws JCGLException;

  /**
   * Called upon an attempt to compile a vertex shader.
   *
   * @param name
   *          The shader name.
   * @param v
   *          The compiled shader.
   * @throws JCGLException
   *           If required.
   */

  void onVertexShaderCompile(
    final String name,
    final VertexShaderType v)
    throws JCGLException;
}
