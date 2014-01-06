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

package com.io7m.jcanephora.contracts.gl3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.FragmentShader;
import com.io7m.jcanephora.FramebufferDrawBuffer;
import com.io7m.jcanephora.JCGLCompileException;
import com.io7m.jcanephora.JCGLRuntimeException;
import com.io7m.jcanephora.JCGLShadersGL3;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.ShaderUtilities;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.VertexShader;
import com.io7m.jcanephora.contracts.ShadersContract;
import com.io7m.jvvfs.FSCapabilityAll;
import com.io7m.jvvfs.PathVirtual;

public abstract class ShadersContractGL3 extends ShadersContract
{
  public abstract JCGLShadersGL3 getShaders(
    final @Nonnull TestContext context);

  /**
   * Passing a deleted fragment shader fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testProgramCreateDeletedFragment()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final JCGLShadersGL3 gl = this.getShaders(tc);

    final List<FramebufferDrawBuffer> outs =
      tc.getGLImplementation().getGLCommon().framebufferGetDrawBuffers();

    FragmentShader f = null;
    VertexShader v = null;
    final Map<String, FramebufferDrawBuffer> outputs =
      new HashMap<String, FramebufferDrawBuffer>();
    outputs.put("out0", outs.get(0));
    outputs.put("out1", outs.get(1));
    outputs.put("out2", outs.get(2));
    outputs.put("out3", outs.get(3));

    try {
      f =
        gl.fragmentShaderCompile("f", ShaderUtilities.readLines(fs
          .openFile(sp.appendName("multi_out.f"))));
      v =
        gl.vertexShaderCompile(
          "v",
          ShaderUtilities.readLines(fs.openFile(sp.appendName("simple.v"))));
      gl.fragmentShaderDelete(f);
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    gl.programCreateWithOutputs("name", v, f, outputs);
  }

  /**
   * Passing a deleted vertex shader fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testProgramCreateDeletedVertex()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final JCGLShadersGL3 gl = this.getShaders(tc);

    final List<FramebufferDrawBuffer> outs =
      tc.getGLImplementation().getGLCommon().framebufferGetDrawBuffers();

    FragmentShader f = null;
    VertexShader v = null;
    final Map<String, FramebufferDrawBuffer> outputs =
      new HashMap<String, FramebufferDrawBuffer>();
    outputs.put("out0", outs.get(0));
    outputs.put("out1", outs.get(1));
    outputs.put("out2", outs.get(2));
    outputs.put("out3", outs.get(3));

    try {
      f =
        gl.fragmentShaderCompile("f", ShaderUtilities.readLines(fs
          .openFile(sp.appendName("multi_out.f"))));
      v =
        gl.vertexShaderCompile(
          "v",
          ShaderUtilities.readLines(fs.openFile(sp.appendName("simple.v"))));
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
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError,
      JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final JCGLShadersGL3 gl = this.getShaders(tc);

    final List<FramebufferDrawBuffer> outs =
      tc.getGLImplementation().getGLCommon().framebufferGetDrawBuffers();

    FragmentShader f = null;
    VertexShader v = null;
    final Map<String, FramebufferDrawBuffer> outputs =
      new HashMap<String, FramebufferDrawBuffer>();

    for (int index = 0; index < 4; ++index) {
      outputs.put("out" + index, outs.get(index));
    }

    try {
      f =
        gl.fragmentShaderCompile("f", ShaderUtilities.readLines(fs
          .openFile(sp.appendName("multi_out.f"))));
      v =
        gl.vertexShaderCompile(
          "v",
          ShaderUtilities.readLines(fs.openFile(sp.appendName("simple.v"))));
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    gl.programCreateWithOutputs("name", v, f, outputs);
  }

  /**
   * Passing null for a fragment shader fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testProgramCreateOutputsFragmentNull()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final JCGLShadersGL3 gl = this.getShaders(tc);

    VertexShader v = null;
    Map<String, FramebufferDrawBuffer> outputs = null;

    try {
      v =
        gl.vertexShaderCompile(
          "v",
          ShaderUtilities.readLines(fs.openFile(sp.appendName("simple.v"))));
      outputs = new HashMap<String, FramebufferDrawBuffer>();
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    gl.programCreateWithOutputs("name", v, null, outputs);
  }

  /**
   * Passing an empty map fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testProgramCreateOutputsMappingEmpty()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final JCGLShadersGL3 gl = this.getShaders(tc);

    FragmentShader f = null;
    VertexShader v = null;
    Map<String, FramebufferDrawBuffer> outputs =
      new HashMap<String, FramebufferDrawBuffer>();

    try {
      f =
        gl.fragmentShaderCompile("f", ShaderUtilities.readLines(fs
          .openFile(sp.appendName("multi_out.f"))));
      v =
        gl.vertexShaderCompile(
          "v",
          ShaderUtilities.readLines(fs.openFile(sp.appendName("simple.v"))));
      outputs = new HashMap<String, FramebufferDrawBuffer>();
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    gl.programCreateWithOutputs("name", v, f, outputs);
  }

  /**
   * Passing a map with null mappings fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testProgramCreateOutputsMappingNull()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final JCGLShadersGL3 gl = this.getShaders(tc);

    FragmentShader f = null;
    VertexShader v = null;
    Map<String, FramebufferDrawBuffer> outputs =
      new HashMap<String, FramebufferDrawBuffer>();
    outputs.put("out0", null);

    try {
      f =
        gl.fragmentShaderCompile("f", ShaderUtilities.readLines(fs
          .openFile(sp.appendName("multi_out.f"))));
      v =
        gl.vertexShaderCompile(
          "v",
          ShaderUtilities.readLines(fs.openFile(sp.appendName("simple.v"))));
      outputs = new HashMap<String, FramebufferDrawBuffer>();
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    gl.programCreateWithOutputs("name", v, f, outputs);
  }

  /**
   * Passing a map with null mappings fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testProgramCreateOutputsMappingNullKey()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final JCGLShadersGL3 gl = this.getShaders(tc);

    final List<FramebufferDrawBuffer> outs =
      tc.getGLImplementation().getGLCommon().framebufferGetDrawBuffers();

    FragmentShader f = null;
    VertexShader v = null;
    Map<String, FramebufferDrawBuffer> outputs =
      new HashMap<String, FramebufferDrawBuffer>();
    outputs.put(null, outs.get(0));

    try {
      f =
        gl.fragmentShaderCompile("f", ShaderUtilities.readLines(fs
          .openFile(sp.appendName("multi_out.f"))));
      v =
        gl.vertexShaderCompile(
          "v",
          ShaderUtilities.readLines(fs.openFile(sp.appendName("simple.v"))));
      outputs = new HashMap<String, FramebufferDrawBuffer>();
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    gl.programCreateWithOutputs("name", v, f, outputs);
  }

  /**
   * Passing a map with too many mappings fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testProgramCreateOutputsMappingsTooMany()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final JCGLShadersGL3 gl = this.getShaders(tc);

    final List<FramebufferDrawBuffer> outs =
      tc.getGLImplementation().getGLCommon().framebufferGetDrawBuffers();

    FragmentShader f = null;
    VertexShader v = null;
    Map<String, FramebufferDrawBuffer> outputs =
      new HashMap<String, FramebufferDrawBuffer>();

    for (int index = 0; index < 1000; ++index) {
      outputs.put("out" + index, outs.get(index % 4));
    }

    try {
      f =
        gl.fragmentShaderCompile("f", ShaderUtilities.readLines(fs
          .openFile(sp.appendName("multi_out.f"))));
      v =
        gl.vertexShaderCompile(
          "v",
          ShaderUtilities.readLines(fs.openFile(sp.appendName("simple.v"))));
      outputs = new HashMap<String, FramebufferDrawBuffer>();
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    gl.programCreateWithOutputs("name", v, f, outputs);
  }

  /**
   * Passing null for a program name fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testProgramCreateOutputsNameNull()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final JCGLShadersGL3 gl = this.getShaders(tc);

    FragmentShader f = null;
    VertexShader v = null;
    Map<String, FramebufferDrawBuffer> outputs = null;

    try {
      f =
        gl.fragmentShaderCompile("f", ShaderUtilities.readLines(fs
          .openFile(sp.appendName("multi_out.f"))));
      v =
        gl.vertexShaderCompile(
          "v",
          ShaderUtilities.readLines(fs.openFile(sp.appendName("simple.v"))));
      outputs = new HashMap<String, FramebufferDrawBuffer>();
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    gl.programCreateWithOutputs(null, v, f, outputs);
  }

  /**
   * Passing null for outputs fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testProgramCreateOutputsOutputsNull()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final JCGLShadersGL3 gl = this.getShaders(tc);

    FragmentShader f = null;
    VertexShader v = null;

    try {
      f =
        gl.fragmentShaderCompile("f", ShaderUtilities.readLines(fs
          .openFile(sp.appendName("multi_out.f"))));
      v =
        gl.vertexShaderCompile(
          "v",
          ShaderUtilities.readLines(fs.openFile(sp.appendName("simple.v"))));
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    gl.programCreateWithOutputs("name", v, f, null);
  }

  /**
   * Passing null for a vertex shader fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testProgramCreateOutputsVertexNull()
      throws JCGLRuntimeException,
        JCGLUnsupportedException,
        ConstraintError,
        JCGLCompileException
  {
    final TestContext tc = this.newTestContext();
    final FSCapabilityAll fs = tc.getFilesystem();
    final PathVirtual sp = tc.getShaderPath();
    final JCGLShadersGL3 gl = this.getShaders(tc);

    FragmentShader f = null;
    Map<String, FramebufferDrawBuffer> outputs = null;

    try {
      f =
        gl.fragmentShaderCompile("f", ShaderUtilities.readLines(fs
          .openFile(sp.appendName("multi_out.f"))));
      outputs = new HashMap<String, FramebufferDrawBuffer>();
    } catch (final Throwable e) {
      throw new AssertionError(e);
    }

    gl.programCreateWithOutputs("name", null, f, outputs);
  }
}
