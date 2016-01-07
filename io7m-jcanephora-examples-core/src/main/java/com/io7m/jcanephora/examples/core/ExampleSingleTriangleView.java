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

package com.io7m.jcanephora.examples.core;

import com.io7m.jcanephora.core.JCGLArrayBufferType;
import com.io7m.jcanephora.core.JCGLArrayObjectBuilderType;
import com.io7m.jcanephora.core.JCGLArrayObjectType;
import com.io7m.jcanephora.core.JCGLBufferUpdateType;
import com.io7m.jcanephora.core.JCGLBufferUpdates;
import com.io7m.jcanephora.core.JCGLClearSpecification;
import com.io7m.jcanephora.core.JCGLFragmentShaderType;
import com.io7m.jcanephora.core.JCGLIndexBufferType;
import com.io7m.jcanephora.core.JCGLPrimitives;
import com.io7m.jcanephora.core.JCGLProgramShaderType;
import com.io7m.jcanephora.core.JCGLProgramUniformType;
import com.io7m.jcanephora.core.JCGLProjectionMatrices;
import com.io7m.jcanephora.core.JCGLProjectionMatricesType;
import com.io7m.jcanephora.core.JCGLScalarType;
import com.io7m.jcanephora.core.JCGLUnsignedType;
import com.io7m.jcanephora.core.JCGLUsageHint;
import com.io7m.jcanephora.core.JCGLVertexShaderType;
import com.io7m.jcanephora.core.JCGLViewMatrices;
import com.io7m.jcanephora.core.JCGLViewMatricesType;
import com.io7m.jcanephora.core.api.JCGLArrayBuffersType;
import com.io7m.jcanephora.core.api.JCGLArrayObjectsType;
import com.io7m.jcanephora.core.api.JCGLClearType;
import com.io7m.jcanephora.core.api.JCGLDrawType;
import com.io7m.jcanephora.core.api.JCGLIndexBuffersType;
import com.io7m.jcanephora.core.api.JCGLInterfaceGL33Type;
import com.io7m.jcanephora.core.api.JCGLShadersType;
import com.io7m.jtensors.Matrix4x4FType;
import com.io7m.jtensors.MatrixDirect4x4FType;
import com.io7m.jtensors.MatrixDirectM4x4F;
import com.io7m.jtensors.MatrixHeapArrayM4x4F;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorI3F;
import com.io7m.jtensors.VectorI4F;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * An example that renders a single triangle in view space.
 */

public final class ExampleSingleTriangleView implements ExampleType
{
  private final JCGLViewMatricesType       view_matrices;
  private final JCGLProjectionMatricesType proj_matrices;
  private final MatrixDirect4x4FType       m_projection;
  private final Matrix4x4FType             m_view;
  private final Matrix4x4FType             m_model;
  private final MatrixDirect4x4FType       m_modelview;
  private       JCGLClearSpecification     clear;
  private       JCGLArrayObjectType        array_object;
  private       JCGLArrayBufferType        array_buffer;
  private       JCGLIndexBufferType        index_buffer;
  private       JCGLProgramShaderType      program;

  /**
   * Construct an example.
   */

  public ExampleSingleTriangleView()
  {
    this.m_view = MatrixHeapArrayM4x4F.newMatrix();
    this.m_model = MatrixHeapArrayM4x4F.newMatrix();
    this.m_projection = MatrixDirectM4x4F.newMatrix();
    this.m_modelview = MatrixDirectM4x4F.newMatrix();
    this.view_matrices = JCGLViewMatrices.newMatrices();
    this.proj_matrices = JCGLProjectionMatrices.newMatrices();
  }

  @Override
  public void onInitialize(final JCGLInterfaceGL33Type g)
  {
    final JCGLArrayBuffersType g_ab = g.getArrayBuffers();
    final JCGLArrayObjectsType g_ao = g.getArrayObjects();
    final JCGLIndexBuffersType g_ib = g.getIndexBuffers();
    final JCGLShadersType g_sh = g.getShaders();

    /**
     * Allocate an index buffer.
     *
     * Note that the index buffer remains bound to the current array object
     * (in this case, the default array object) until it is explicitly unbound.
     */

    this.index_buffer =
      g_ib.indexBufferAllocate(
        3L,
        JCGLUnsignedType.TYPE_UNSIGNED_INT,
        JCGLUsageHint.USAGE_STATIC_DRAW);

    /**
     * Populate the index buffer.
     */

    {
      final JCGLBufferUpdateType<JCGLIndexBufferType> u =
        JCGLBufferUpdates.newUpdateReplacingAll(this.index_buffer);
      final IntBuffer i = u.getData().asIntBuffer();

      i.put(0, 0);
      i.put(1, 1);
      i.put(2, 2);

      g_ib.indexBufferUpdate(u);
      g_ib.indexBufferUnbind();
    }

    /**
     * Allocate an array buffer to hold three vertices. Each vertex has
     * a single vec3 value representing the position.
     *
     * Note that the allocated array buffer remains bound until it is explicitly
     * unbound.
     */

    final int vertex_size = 3 * 4;
    this.array_buffer =
      g_ab.arrayBufferAllocate(
        vertex_size * 3L, JCGLUsageHint.USAGE_STATIC_DRAW);

    /**
     * Populate the array buffer with three triangle vertices.
     */

    {
      final JCGLBufferUpdateType<JCGLArrayBufferType> u =
        JCGLBufferUpdates.newUpdateReplacingAll(this.array_buffer);
      final FloatBuffer d = u.getData().asFloatBuffer();

      d.put(0, -0.5f);
      d.put(1, 0.5f);
      d.put(2, 0.0f);

      d.put(3, -0.5f);
      d.put(4, -0.5f);
      d.put(5, 0.0f);

      d.put(6, 0.5f);
      d.put(7, -0.5f);
      d.put(8, 0.0f);

      g_ab.arrayBufferUpdate(u);
      g_ab.arrayBufferUnbind();
    }

    /**
     * Create a new array object builder. Bind the index buffer to it,
     * and associate vertex attribute 0 with the created array buffer.
     */

    final JCGLArrayObjectBuilderType aob = g_ao.arrayObjectNewBuilder();
    aob.setIndexBuffer(this.index_buffer);
    aob.setAttributeFloatingPoint(
      0,
      this.array_buffer,
      3,
      JCGLScalarType.TYPE_FLOAT,
      vertex_size,
      0L,
      false);

    /**
     * Create the immutable array object.
     */

    this.array_object = g_ao.arrayObjectAllocate(aob);
    g_ao.arrayObjectUnbind();

    /**
     * Compile a trivial GLSL shader that will display the given triangle.
     */

    try {

      /**
       * Compile a vertex shader. Line separators are required by GLSL
       * and so are manually inserted into the lines of GLSL source code.
       */

      final List<String> vv_lines =
        IOUtils.readLines(
          ExampleSingleTriangleView.class.getResourceAsStream("eye.vert"))
          .stream()
          .map(x -> x + System.lineSeparator())
          .collect(Collectors.toList());

      final JCGLVertexShaderType v =
        g_sh.shaderCompileVertex("eye.vert", vv_lines);

      /**
       * Compile a fragment shader.
       */

      final List<String> ff_lines =
        IOUtils.readLines(
          ExampleSingleTriangleView.class.getResourceAsStream("red.frag"))
          .stream()
          .map(x -> x + System.lineSeparator())
          .collect(Collectors.toList());

      final JCGLFragmentShaderType f =
        g_sh.shaderCompileFragment("red.frag", ff_lines);

      /**
       * Link the shaders into a program.
       */

      this.program =
        g_sh.shaderLinkProgram("simple", v, Optional.empty(), f);

      /**
       * The individual shaders can (and should) be deleted, because
       * they are now attached to the linked program. This has the effect
       * that when the linked program is deleted, the shaders are deleted
       * along with it.
       */

      g_sh.shaderDeleteFragment(f);
      g_sh.shaderDeleteVertex(v);

    } catch (final IOException e) {
      throw new UncheckedIOException(e);
    }

    /**
     * Configure a clearing specification that will clear the color
     * buffer to a dark grey.
     */

    final JCGLClearSpecification.Builder cb =
      JCGLClearSpecification.builder();
    cb.setColorBufferClear(new VectorI4F(0.1f, 0.1f, 0.1f, 1.0f));
    this.clear = cb.build();
  }

  @Override
  public void onRender(final JCGLInterfaceGL33Type g)
  {
    final JCGLArrayObjectsType g_ao = g.getArrayObjects();
    final JCGLClearType g_c = g.getClear();
    final JCGLDrawType g_d = g.getDraw();
    final JCGLShadersType g_sh = g.getShaders();

    /**
     * Clear the window.
     */

    g_c.clear(this.clear);

    /**
     * Activate the program, bind the created array object, draw a triangle.
     */

    g_sh.shaderActivateProgram(this.program);
    g_ao.arrayObjectBind(this.array_object);

    /**
     * Construct the view and projection matrices, and use an identity model
     * matrix.
     */

    final Map<String, JCGLProgramUniformType> us = this.program.getUniforms();
    final JCGLProgramUniformType u_modelview = us.get("m_modelview");
    final JCGLProgramUniformType u_projection = us.get("m_projection");

    this.view_matrices.lookAt(
      this.m_view,
      new VectorI3F(0.0f, 0.0f, -5.0f),
      VectorI3F.ZERO,
      new VectorI3F(0.0f, 1.0f, 0.0f));

    this.proj_matrices.makePerspectiveProjection(
      this.m_projection, 0.0001, 100.0, 640.0 / 480.0, Math.toRadians(90.0));

    MatrixM4x4F.setIdentity(this.m_model);
    MatrixM4x4F.multiply(this.m_view, this.m_model, this.m_modelview);

    g_sh.shaderUniformPutMatrix4x4f(u_modelview, this.m_modelview);
    g_sh.shaderUniformPutMatrix4x4f(u_projection, this.m_projection);

    g_d.drawElements(JCGLPrimitives.PRIMITIVE_TRIANGLES);
    g_ao.arrayObjectUnbind();
    g_sh.shaderDeactivateProgram();
  }

  @Override
  public void onFinish(final JCGLInterfaceGL33Type g)
  {
    final JCGLArrayBuffersType g_ab = g.getArrayBuffers();
    final JCGLArrayObjectsType g_ao = g.getArrayObjects();
    final JCGLIndexBuffersType g_ib = g.getIndexBuffers();
    final JCGLShadersType g_sh = g.getShaders();

    /**
     * Delete everything.
     */

    g_ib.indexBufferDelete(this.index_buffer);
    g_ab.arrayBufferDelete(this.array_buffer);
    g_ao.arrayObjectDelete(this.array_object);
    g_sh.shaderDeleteProgram(this.program);
  }
}