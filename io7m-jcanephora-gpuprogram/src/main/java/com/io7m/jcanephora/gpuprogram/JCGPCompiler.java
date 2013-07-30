/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.gpuprogram;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.FragmentShader;
import com.io7m.jcanephora.JCGLCompileException;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLShaderKindFragment;
import com.io7m.jcanephora.JCGLShaderKindVertex;
import com.io7m.jcanephora.JCGLShaders;
import com.io7m.jcanephora.ProgramAttribute;
import com.io7m.jcanephora.ProgramReference;
import com.io7m.jcanephora.ProgramUniform;
import com.io7m.jcanephora.VertexShader;

/**
 * <p>
 * A compiler that will produce compiled GLSL programs as output.
 * </p>
 * <p>
 * Values of this type cannot be manipulated by multiple threads without
 * explicit synchronization.
 * </p>
 */

@NotThreadSafe public final class JCGPCompiler implements JCGPCompilerAPI
{
  public static @Nonnull JCGPCompiler newCompiler()
  {
    return new JCGPCompiler();
  }

  private JCGPCompiler()
  {

  }

  @Override public @Nonnull
    JCGPProgram
    compileProgram(
      final @Nonnull JCGLShaders gl,
      final @Nonnull String name,
      final @Nonnull JCGPGeneratedSource<JCGLShaderKindVertex> vertex_source,
      final @Nonnull JCGPGeneratedSource<JCGLShaderKindFragment> fragment_source)
      throws ConstraintError,
        JCGLCompileException,
        JCGLException
  {
    Constraints.constrainNotNull(name, "Program name");
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(vertex_source, "Vertex shader source");
    Constraints.constrainNotNull(fragment_source, "Fragment shader source");

    ProgramReference p = null;
    VertexShader v = null;
    FragmentShader f = null;

    try {
      p = gl.programCreate(name);
      v = gl.vertexShaderCompile(name + "-v", vertex_source.getLines());
      f = gl.fragmentShaderCompile(name + "-f", fragment_source.getLines());

      gl.vertexShaderAttach(p, v);
      gl.fragmentShaderAttach(p, f);
      gl.programLink(p);

      final Map<String, ProgramUniform> uniforms =
        new HashMap<String, ProgramUniform>();
      gl.programGetUniforms(p, uniforms);
      final Map<String, ProgramAttribute> attributes =
        new HashMap<String, ProgramAttribute>();
      gl.programGetAttributes(p, attributes);

      return new JCGPProgram(name, p, uniforms, attributes);
    } finally {

      /**
       * Note that shaders can be deleted after being linked into a program,
       * preventing any possibility of leaking them later.
       */

      if (v != null) {
        gl.vertexShaderDelete(v);
      }
      if (f != null) {
        gl.fragmentShaderDelete(f);
      }
    }
  }
}
