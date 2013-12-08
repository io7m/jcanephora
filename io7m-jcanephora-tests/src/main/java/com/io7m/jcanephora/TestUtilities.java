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

package com.io7m.jcanephora;

import java.io.InputStream;
import java.util.List;

import javax.annotation.Nonnull;

import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jlog.Log;
import com.io7m.jvvfs.FSCapabilityRead;
import com.io7m.jvvfs.PathVirtual;
import com.io7m.parasol.xml.PGLSLMetaXML;

public final class TestUtilities
{
  public static final class LoadedShader
  {
    public final @Nonnull FragmentShader   fragment_shader;
    public final @Nonnull ProgramReference program;
    public final @Nonnull VertexShader     vertex_shader;
    public final @Nonnull PGLSLMetaXML     meta;

    LoadedShader(
      final @Nonnull ProgramReference program,
      final @Nonnull VertexShader vertex_shader,
      final @Nonnull FragmentShader fragment_shader,
      final @Nonnull PGLSLMetaXML meta)
    {
      this.program = program;
      this.vertex_shader = vertex_shader;
      this.fragment_shader = fragment_shader;
      this.meta = meta;
    }
  }

  private static @Nonnull String getVersionName(
    final @Nonnull JCGLMeta g)
    throws JCGLException
  {
    final JCGLSLVersion v = g.metaGetSLVersion();
    final int number = (v.getVersionMajor() * 100) + v.getVersionMinor();

    switch (v.getAPI()) {
      case JCGL_ES:
        return "glsl-es-" + number;
      case JCGL_FULL:
        return "glsl-" + number;
    }

    throw new UnreachableCodeException();
  }

  public static @Nonnull
    <G extends JCGLMeta & JCGLShadersCommon>
    LoadedShader
    loadShader(
      final @Nonnull G g,
      final @Nonnull FSCapabilityRead fs,
      final @Nonnull String name,
      final @Nonnull Log log)
  {
    try {
      final PathVirtual pbase =
        PathVirtual.ofString("/com/io7m/jcanephora/shaders/" + name);
      final PathVirtual pmeta = pbase.appendName("meta.xml");

      final InputStream stream = fs.openFile(pmeta);
      try {
        final PGLSLMetaXML px = PGLSLMetaXML.fromStream(stream, log);
        final String version = TestUtilities.getVersionName(g);
        final PathVirtual pv = pbase.appendName(version + ".v");
        final PathVirtual pf = pbase.appendName(version + ".f");

        final InputStream vstream = fs.openFile(pv);
        try {
          final InputStream fstream = fs.openFile(pf);
          try {
            final List<String> v_lines = ShaderUtilities.readLines(vstream);
            final List<String> f_lines = ShaderUtilities.readLines(fstream);
            final VertexShader v = g.vertexShaderCompile(name, v_lines);
            final FragmentShader f = g.fragmentShaderCompile(name, f_lines);
            final ProgramReference p = g.programCreateCommon(name, v, f);
            return new LoadedShader(p, v, f, px);
          } finally {
            fstream.close();
          }
        } finally {
          vstream.close();
        }
      } finally {
        stream.close();
      }

    } catch (final Throwable x) {
      x.printStackTrace(System.err);
      throw new UnreachableCodeException(x);
    }
  }
}
