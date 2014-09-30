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

package com.io7m.jcanephora.tests;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import nu.xom.ParsingException;

import org.xml.sax.SAXException;

import com.io7m.jcanephora.FragmentShaderType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionProgramCompileError;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.JCGLSLVersion;
import com.io7m.jcanephora.ProgramType;
import com.io7m.jcanephora.VertexShaderType;
import com.io7m.jcanephora.api.JCGLMetaType;
import com.io7m.jcanephora.api.JCGLShadersCommonType;
import com.io7m.jcanephora.api.JCGLShadersParametersType;
import com.io7m.jcanephora.utilities.ShaderUtilities;
import com.io7m.jlog.LogUsableType;
import com.io7m.jparasol.xml.PGLSLMetaXML;
import com.io7m.junreachable.UnreachableCodeException;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.FilesystemType;
import com.io7m.jvvfs.PathVirtual;

@SuppressWarnings("null") public final class TestShaders
{
  public static
    <G extends JCGLShadersCommonType & JCGLMetaType>
    ProgramType
    getEverythingProgram(
      final G gl,
      final FilesystemType fs)
      throws JCGLExceptionProgramCompileError
  {
    return TestShaders.getProgram(gl, fs, "everything");
  }

  public static
    <G extends JCGLShadersCommonType & JCGLMetaType>
    FragmentShaderType
    getFragmentShader(
      final G gl,
      final FilesystemType fs,
      final String name)
  {
    try {
      return TestShaders.getFragmentShaderMayFail(gl, fs, name);
    } catch (final Throwable x) {
      throw new UnreachableCodeException(x);
    }
  }

  public static
    <G extends JCGLShadersCommonType & JCGLMetaType>
    FragmentShaderType
    getFragmentShaderMayFail(
      final G gl,
      final FilesystemType fs,
      final String name)
      throws FilesystemError,
        JCGLExceptionRuntime,
        JCGLExceptionProgramCompileError,
        JCGLException,
        IOException
  {
    final PathVirtual base =
      PathVirtual.ofString("/com/io7m/jcanephora/tests/shaders/glsl");
    final PathVirtual dir = base.appendName(name);

    final JCGLSLVersion version = gl.metaGetSLVersion();

    final String f_name = TestShaders.getFragmentSourceName(version);
    final PathVirtual fn = dir.appendName(f_name);
    return gl.fragmentShaderCompile(
      name,
      ShaderUtilities.readLines(fs.openFile(fn)));
  }

  public static String getFragmentSourceName(
    final JCGLSLVersion version)
  {
    final int maj = version.getVersionMajor() * 100;
    final int min = version.getVersionMinor();

    switch (version.getAPI()) {
      case JCGL_ES:
      {
        final int k = maj + min;
        return "glsl-es-" + k + ".f";
      }
      case JCGL_FULL:
      {
        final int k = maj + min;
        return "glsl-" + k + ".f";
      }
    }

    throw new UnreachableCodeException();
  }

  public static
    <G extends JCGLShadersParametersType & JCGLMetaType>
    PGLSLMetaXML
    getMeta(
      final FilesystemType fs,
      final String name,
      final LogUsableType log)
  {
    try {
      final PathVirtual base =
        PathVirtual.ofString("/com/io7m/jcanephora/tests/shaders/glsl");
      final PathVirtual dir = base.appendName(name);
      final PathVirtual vn = dir.appendName("meta.xml");
      return PGLSLMetaXML.fromStream(fs.openFile(vn), log);
    } catch (final FilesystemError e) {
      throw new UnreachableCodeException(e);
    } catch (final ParsingException e) {
      throw new UnreachableCodeException(e);
    } catch (final IOException e) {
      throw new UnreachableCodeException(e);
    } catch (final SAXException e) {
      throw new UnreachableCodeException(e);
    } catch (final ParserConfigurationException e) {
      throw new UnreachableCodeException(e);
    }
  }

  public static
    <G extends JCGLShadersCommonType & JCGLMetaType>
    ProgramType
    getPositionProgram(
      final G gl,
      final FilesystemType fs)
      throws JCGLExceptionProgramCompileError
  {
    return TestShaders.getProgram(gl, fs, "simple");
  }

  public static
    <G extends JCGLShadersCommonType & JCGLMetaType>
    ProgramType
    getProgram(
      final G gl,
      final FilesystemType fs,
      final String name)
      throws JCGLExceptionProgramCompileError
  {
    try {
      final VertexShaderType v = TestShaders.getVertexShader(gl, fs, name);
      final FragmentShaderType f =
        TestShaders.getFragmentShader(gl, fs, name);
      return gl.programCreateCommon(name, v, f);
    } catch (final JCGLExceptionRuntime e) {
      throw new UnreachableCodeException(e);
    } catch (final JCGLExceptionProgramCompileError e) {
      throw e;
    } catch (final JCGLException e) {
      throw new UnreachableCodeException(e);
    }
  }

  public static
    <G extends JCGLShadersCommonType & JCGLMetaType>
    VertexShaderType
    getVertexShader(
      final G gl,
      final FilesystemType fs,
      final String name)
  {
    try {
      return TestShaders.getVertexShaderMayFail(gl, fs, name);
    } catch (final Throwable x) {
      throw new UnreachableCodeException(x);
    }
  }

  public static
    <G extends JCGLShadersCommonType & JCGLMetaType>
    VertexShaderType
    getVertexShaderMayFail(
      final G gl,
      final FilesystemType fs,
      final String name)
      throws FilesystemError,
        JCGLExceptionRuntime,
        JCGLExceptionProgramCompileError,
        JCGLException,
        IOException
  {
    final PathVirtual base =
      PathVirtual.ofString("/com/io7m/jcanephora/tests/shaders/glsl");
    final PathVirtual dir = base.appendName(name);

    final JCGLSLVersion version = gl.metaGetSLVersion();

    final String v_name = TestShaders.getVertexSourceName(version);
    final PathVirtual vn = dir.appendName(v_name);
    return gl.vertexShaderCompile(
      name,
      ShaderUtilities.readLines(fs.openFile(vn)));
  }

  public static String getVertexSourceName(
    final JCGLSLVersion version)
  {
    final int maj = version.getVersionMajor() * 100;
    final int min = version.getVersionMinor();

    switch (version.getAPI()) {
      case JCGL_ES:
      {
        final int k = maj + min;
        return "glsl-es-" + k + ".v";
      }
      case JCGL_FULL:
      {
        final int k = maj + min;
        return "glsl-" + k + ".v";
      }
    }

    throw new UnreachableCodeException();
  }
}
