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
import com.io7m.jcanephora.core.JCGLFragmentShaderUsableType;
import com.io7m.jcanephora.core.JCGLGeometryShaderUsableType;
import com.io7m.jcanephora.core.JCGLProgramAttributeType;
import com.io7m.jcanephora.core.JCGLProgramShaderUsableType;
import com.io7m.jcanephora.core.JCGLProgramUniformType;
import com.io7m.jcanephora.core.JCGLVertexShaderUsableType;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The type of listeners that can be used to control the results of fake shader
 * compilation.
 */

public interface FakeShaderListenerType
{
  /**
   * Called when the vertex shader {@code name} is about to be compiled.
   *
   * @param context The current context
   * @param name    The name
   * @param sources The source code
   *
   * @throws JCGLException If required
   */

  void onCompileVertexShaderStart(
    FakeContext context,
    String name,
    List<String> sources)
    throws JCGLException;

  /**
   * Called when the fragment shader {@code name} is about to be compiled.
   *
   * @param context The current context
   * @param name    The name
   * @param sources The source code
   *
   * @throws JCGLException If required
   */

  void onCompileFragmentShaderStart(
    FakeContext context,
    String name,
    List<String> sources)
    throws JCGLException;

  /**
   * Called when the geometry shader {@code name} is about to be compiled.
   *
   * @param context The current context
   * @param name    The name
   * @param sources The source code
   *
   * @throws JCGLException If required
   */

  void onCompileGeometryShaderStart(
    FakeContext context,
    String name,
    List<String> sources)
    throws JCGLException;

  /**
   * Called when a program is about to be linked.
   *
   * @param context    The current context
   * @param p          The resulting program
   * @param name       The program name
   * @param v          The vertex shader
   * @param g          The optional geometry shader
   * @param f          The fragment shader
   * @param attributes The attributes that the resulting program will have
   * @param uniforms   The uniforms that the resulting program will have
   *
   * @throws JCGLException If required
   */

  void onLinkProgram(
    FakeContext context,
    JCGLProgramShaderUsableType p,
    String name,
    JCGLVertexShaderUsableType v,
    Optional<JCGLGeometryShaderUsableType> g,
    JCGLFragmentShaderUsableType f,
    Map<String, JCGLProgramAttributeType> attributes,
    Map<String, JCGLProgramUniformType> uniforms)
    throws JCGLException;
}
