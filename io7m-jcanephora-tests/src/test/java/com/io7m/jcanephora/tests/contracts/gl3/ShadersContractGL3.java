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

package com.io7m.jcanephora.tests.contracts.gl3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.io7m.jcanephora.FragmentShaderType;
import com.io7m.jcanephora.FramebufferDrawBufferType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionDeleted;
import com.io7m.jcanephora.JCGLExceptionProgramOutputMappingsError;
import com.io7m.jcanephora.VertexShaderType;
import com.io7m.jcanephora.api.JCGLInterfaceGL3Type;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.TestShaders;
import com.io7m.jcanephora.tests.TestUtilities;
import com.io7m.jcanephora.tests.contracts.ShadersContract;
import com.io7m.jnull.NullCheckException;
import com.io7m.jvvfs.FilesystemType;

@SuppressWarnings({ "unchecked", "null" }) public abstract class ShadersContractGL3 extends
  ShadersContract
{
  public abstract JCGLInterfaceGL3Type getShaders(
    final TestContext context);

  /**
   * Passing a deleted fragment shader fails.
   */

  @Test(expected = JCGLExceptionDeleted.class) public
    void
    testProgramCreateDeletedFragment()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FilesystemType fs = tc.getFilesystem();
    final JCGLInterfaceGL3Type gl = this.getShaders(tc);

    final List<FramebufferDrawBufferType> outs =
      tc.getGLImplementation().getGLCommon().framebufferGetDrawBuffers();

    FragmentShaderType f = null;
    VertexShaderType v = null;
    final Map<String, FramebufferDrawBufferType> outputs =
      new HashMap<String, FramebufferDrawBufferType>();
    outputs.put("out0", outs.get(0));
    outputs.put("out1", outs.get(1));
    outputs.put("out2", outs.get(2));
    outputs.put("out3", outs.get(3));

    try {
      v = TestShaders.getVertexShader(gl, fs, "multi_out");
      f = TestShaders.getFragmentShader(gl, fs, "multi_out");
      gl.fragmentShaderDelete(f);
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    gl.programCreateWithOutputs("name", v, f, outputs);
  }

  /**
   * Passing a deleted vertex shader fails.
   */

  @Test(expected = JCGLExceptionDeleted.class) public
    void
    testProgramCreateDeletedVertex()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FilesystemType fs = tc.getFilesystem();

    final JCGLInterfaceGL3Type gl = this.getShaders(tc);

    final List<FramebufferDrawBufferType> outs =
      tc.getGLImplementation().getGLCommon().framebufferGetDrawBuffers();

    FragmentShaderType f = null;
    VertexShaderType v = null;
    final Map<String, FramebufferDrawBufferType> outputs =
      new HashMap<String, FramebufferDrawBufferType>();
    outputs.put("out0", outs.get(0));
    outputs.put("out1", outs.get(1));
    outputs.put("out2", outs.get(2));
    outputs.put("out3", outs.get(3));

    try {
      v = TestShaders.getVertexShader(gl, fs, "multi_out");
      f = TestShaders.getFragmentShader(gl, fs, "multi_out");
      gl.vertexShaderDelete(v);
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    gl.programCreateWithOutputs("name", v, f, outputs);
  }

  /**
   * Creating a program and mapping multiple outputs works.
   */

  @Test public void testProgramCreateMappings()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FilesystemType fs = tc.getFilesystem();

    final JCGLInterfaceGL3Type gl = this.getShaders(tc);

    final List<FramebufferDrawBufferType> outs =
      tc.getGLImplementation().getGLCommon().framebufferGetDrawBuffers();

    FragmentShaderType f = null;
    VertexShaderType v = null;
    final Map<String, FramebufferDrawBufferType> outputs =
      new HashMap<String, FramebufferDrawBufferType>();

    for (int index = 0; index < 4; ++index) {
      outputs.put("out" + index, outs.get(index));
    }

    v = TestShaders.getVertexShader(gl, fs, "multi_out");
    f = TestShaders.getFragmentShader(gl, fs, "multi_out");

    gl.programCreateWithOutputs("name", v, f, outputs);
  }

  /**
   * Passing null for a fragment shader fails.
   */

  @Test(expected = NullCheckException.class) public
    void
    testProgramCreateOutputsFragmentNull()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FilesystemType fs = tc.getFilesystem();

    final JCGLInterfaceGL3Type gl = this.getShaders(tc);

    VertexShaderType v = null;
    Map<String, FramebufferDrawBufferType> outputs = null;

    try {
      v = TestShaders.getVertexShader(gl, fs, "multi_out");
      outputs = new HashMap<String, FramebufferDrawBufferType>();
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    gl.programCreateWithOutputs(
      "name",
      v,
      (FragmentShaderType) TestUtilities.actuallyNull(),
      outputs);
  }

  /**
   * Passing an empty map fails.
   */

  @Test(expected = JCGLExceptionProgramOutputMappingsError.class) public
    void
    testProgramCreateOutputsMappingEmpty()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FilesystemType fs = tc.getFilesystem();

    final JCGLInterfaceGL3Type gl = this.getShaders(tc);

    FragmentShaderType f = null;
    VertexShaderType v = null;

    v = TestShaders.getVertexShader(gl, fs, "multi_out");
    f = TestShaders.getFragmentShader(gl, fs, "multi_out");

    final Map<String, FramebufferDrawBufferType> outputs =
      new HashMap<String, FramebufferDrawBufferType>();

    gl.programCreateWithOutputs("name", v, f, outputs);
  }

  /**
   * Passing a map with null mappings fails.
   */

  @Test(expected = NullCheckException.class) public
    void
    testProgramCreateOutputsMappingNull()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FilesystemType fs = tc.getFilesystem();

    final JCGLInterfaceGL3Type gl = this.getShaders(tc);

    FragmentShaderType f = null;
    VertexShaderType v = null;
    final Map<String, FramebufferDrawBufferType> outputs =
      new HashMap<String, FramebufferDrawBufferType>();
    outputs.put("out0", null);

    v = TestShaders.getVertexShader(gl, fs, "multi_out");
    f = TestShaders.getFragmentShader(gl, fs, "multi_out");

    gl.programCreateWithOutputs("name", v, f, outputs);
  }

  /**
   * Passing a map with null mappings fails.
   */

  @Test(expected = NullCheckException.class) public
    void
    testProgramCreateOutputsMappingNullKey()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FilesystemType fs = tc.getFilesystem();

    final JCGLInterfaceGL3Type gl = this.getShaders(tc);

    final List<FramebufferDrawBufferType> outs =
      tc.getGLImplementation().getGLCommon().framebufferGetDrawBuffers();

    FragmentShaderType f = null;
    VertexShaderType v = null;
    final Map<String, FramebufferDrawBufferType> outputs =
      new HashMap<String, FramebufferDrawBufferType>();
    outputs.put(null, outs.get(0));

    v = TestShaders.getVertexShader(gl, fs, "multi_out");
    f = TestShaders.getFragmentShader(gl, fs, "multi_out");

    gl.programCreateWithOutputs("name", v, f, outputs);
  }

  /**
   * Passing a map with too many mappings fails.
   */

  @Test(expected = JCGLExceptionProgramOutputMappingsError.class) public
    void
    testProgramCreateOutputsMappingsTooMany()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FilesystemType fs = tc.getFilesystem();

    final JCGLInterfaceGL3Type gl = this.getShaders(tc);

    final List<FramebufferDrawBufferType> outs =
      tc.getGLImplementation().getGLCommon().framebufferGetDrawBuffers();

    FragmentShaderType f = null;
    VertexShaderType v = null;
    final Map<String, FramebufferDrawBufferType> outputs =
      new HashMap<String, FramebufferDrawBufferType>();

    for (int index = 0; index < 1000; ++index) {
      outputs.put("out" + index, outs.get(index % 4));
    }

    v = TestShaders.getVertexShader(gl, fs, "multi_out");
    f = TestShaders.getFragmentShader(gl, fs, "multi_out");

    gl.programCreateWithOutputs("name", v, f, outputs);
  }

  /**
   * Passing null for a program name fails.
   */

  @Test(expected = NullCheckException.class) public
    void
    testProgramCreateOutputsNameNull()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FilesystemType fs = tc.getFilesystem();

    final JCGLInterfaceGL3Type gl = this.getShaders(tc);

    FragmentShaderType f = null;
    VertexShaderType v = null;
    Map<String, FramebufferDrawBufferType> outputs = null;

    outputs = new HashMap<String, FramebufferDrawBufferType>();
    v = TestShaders.getVertexShader(gl, fs, "multi_out");
    f = TestShaders.getFragmentShader(gl, fs, "multi_out");

    gl.programCreateWithOutputs(
      (String) TestUtilities.actuallyNull(),
      v,
      f,
      outputs);
  }

  /**
   * Passing null for outputs fails.
   */

  @Test(expected = NullCheckException.class) public
    void
    testProgramCreateOutputsOutputsNull()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FilesystemType fs = tc.getFilesystem();

    final JCGLInterfaceGL3Type gl = this.getShaders(tc);

    FragmentShaderType f = null;
    VertexShaderType v = null;

    v = TestShaders.getVertexShader(gl, fs, "multi_out");
    f = TestShaders.getFragmentShader(gl, fs, "multi_out");

    gl.programCreateWithOutputs(
      "name",
      v,
      f,
      (Map<String, FramebufferDrawBufferType>) TestUtilities.actuallyNull());
  }

  /**
   * Passing null for a vertex shader fails.
   */

  @Test(expected = NullCheckException.class) public
    void
    testProgramCreateOutputsVertexNull()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final FilesystemType fs = tc.getFilesystem();

    final JCGLInterfaceGL3Type gl = this.getShaders(tc);

    FragmentShaderType f = null;
    Map<String, FramebufferDrawBufferType> outputs = null;

    outputs = new HashMap<String, FramebufferDrawBufferType>();
    f = TestShaders.getFragmentShader(gl, fs, "multi_out");

    gl.programCreateWithOutputs(
      "name",
      (VertexShaderType) TestUtilities.actuallyNull(),
      f,
      outputs);
  }
}
