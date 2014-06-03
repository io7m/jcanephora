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

package com.io7m.jcanephora.tests.types;

import com.io7m.jcanephora.FragmentShaderType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionProgramCompileError;
import com.io7m.jcanephora.ProgramType;
import com.io7m.jcanephora.VertexShaderType;
import com.io7m.jcanephora.api.JCGLMetaType;
import com.io7m.jcanephora.api.JCGLShadersCommonType;
import com.io7m.jcanephora.tests.TestShaders;
import com.io7m.jlog.LogUsableType;
import com.io7m.jparasol.xml.PGLSLMetaXML;
import com.io7m.junreachable.UnreachableCodeException;
import com.io7m.jvvfs.FilesystemType;

public final class LoadedShader
{
  public static
    <G extends JCGLShadersCommonType & JCGLMetaType>
    LoadedShader
    loadFrom(
      final G gc,
      final FilesystemType fs,
      final String name,
      final LogUsableType log)
  {
    try {
      final PGLSLMetaXML m = TestShaders.getMeta(fs, name, log);
      final VertexShaderType v = TestShaders.getVertexShader(gc, fs, name);
      final FragmentShaderType f =
        TestShaders.getFragmentShader(gc, fs, name);
      final ProgramType p = gc.programCreateCommon(name, v, f);
      return new LoadedShader(p, v, f, m);
    } catch (final JCGLExceptionProgramCompileError e) {
      throw new UnreachableCodeException(e);
    } catch (final JCGLException e) {
      throw new UnreachableCodeException(e);
    }
  }

  public final FragmentShaderType fragment_shader;
  public final PGLSLMetaXML       meta;
  public final ProgramType        program;

  public final VertexShaderType   vertex_shader;

  LoadedShader(
    final ProgramType in_program,
    final VertexShaderType in_vertex,
    final FragmentShaderType in_fragment,
    final PGLSLMetaXML in_meta)
  {
    this.program = in_program;
    this.vertex_shader = in_vertex;
    this.fragment_shader = in_fragment;
    this.meta = in_meta;
  }
}
