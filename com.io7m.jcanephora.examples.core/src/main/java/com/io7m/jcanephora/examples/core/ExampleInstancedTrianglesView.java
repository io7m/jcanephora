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
import com.io7m.jcanephora.core.JCGLScalarType;
import com.io7m.jcanephora.core.JCGLUnsignedType;
import com.io7m.jcanephora.core.JCGLUsageHint;
import com.io7m.jcanephora.core.JCGLVertexShaderType;
import com.io7m.jcanephora.core.JCGLViewMatrices;
import com.io7m.jcanephora.core.api.JCGLArrayBuffersType;
import com.io7m.jcanephora.core.api.JCGLArrayObjectsType;
import com.io7m.jcanephora.core.api.JCGLClearType;
import com.io7m.jcanephora.core.api.JCGLDrawType;
import com.io7m.jcanephora.core.api.JCGLIndexBuffersType;
import com.io7m.jcanephora.core.api.JCGLInterfaceGL33Type;
import com.io7m.jcanephora.core.api.JCGLShadersType;
import com.io7m.jtensors.core.unparameterized.matrices.Matrices4x4D;
import com.io7m.jtensors.core.unparameterized.matrices.Matrix4x4D;
import com.io7m.jtensors.core.unparameterized.vectors.Vector3D;
import com.io7m.jtensors.core.unparameterized.vectors.Vector4D;
import com.io7m.jtensors.core.unparameterized.vectors.Vectors3D;
import com.io7m.jtensors.storage.bytebuffered.MatrixByteBuffered4x4Type;
import com.io7m.jtensors.storage.bytebuffered.MatrixByteBuffered4x4s32;
import com.io7m.mutable.numbers.core.MutableLong;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * An example that renders a single triangle in view space.
 */

public final class ExampleInstancedTrianglesView implements ExampleType
{
  private JCGLClearSpecification clear;
  private JCGLArrayObjectType array_object;
  private JCGLArrayBufferType array_buffer;
  private JCGLIndexBufferType index_buffer;
  private JCGLProgramShaderType program;
  private JCGLArrayBufferType matrices_buffer;
  private JCGLBufferUpdateType<JCGLArrayBufferType> matrices_update;
  private MatrixByteBuffered4x4Type matrices_models;

  /**
   * Construct an example.
   */

  public ExampleInstancedTrianglesView()
  {

  }

  @Override
  public void onInitialize(final JCGLInterfaceGL33Type g)
  {
    final JCGLArrayBuffersType g_ab = g.getArrayBuffers();
    final JCGLArrayObjectsType g_ao = g.getArrayObjects();
    final JCGLIndexBuffersType g_ib = g.getIndexBuffers();
    final JCGLShadersType g_sh = g.getShaders();

    /*
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

    /*
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

    /*
     * Allocate an array buffer to hold three vertices. Each vertex has
     * a single vec3 value representing the position.
     *
     * Note that the allocated array buffer remains bound until it is explicitly
     * unbound.
     */

    final int vertex_size = 3 * 4;
    this.array_buffer =
      g_ab.arrayBufferAllocate(
        (long) vertex_size * 3L, JCGLUsageHint.USAGE_STATIC_DRAW);

    /*
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

    /*
     * Allocate an array buffer to hold three 4x4 matrices.
     */

    final int matrix_size = 16 * 4;
    this.matrices_buffer =
      g_ab.arrayBufferAllocate(
        3L * (long) matrix_size, JCGLUsageHint.USAGE_DYNAMIC_DRAW);

    {
      this.matrices_update =
        JCGLBufferUpdates.newUpdateReplacingAll(this.matrices_buffer);
      final ByteBuffer data = this.matrices_update.getData();

      final MutableLong offset = MutableLong.create();
      this.matrices_models =
        MatrixByteBuffered4x4s32.createWithBase(data, offset, 0);

      /*
       * Populate the matrices, positioning the three triangles in a
       * horizontal line.
       */

      offset.setValue(0L);
      this.matrices_models.setMatrix4x4D(
        Matrices4x4D.ofTranslation(2.0, 0.0, 0.0));

      offset.setValue(offset.value() + matrix_size);
      this.matrices_models.setMatrix4x4D(Matrices4x4D.identity());

      offset.setValue(offset.value() + matrix_size);
      this.matrices_models.setMatrix4x4D(
        Matrices4x4D.ofTranslation(-2.0, 0.0, 0.0));

      g_ab.arrayBufferUpdate(this.matrices_update);
      g_ab.arrayBufferUnbind();
    }

    /*
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

    /*
     * Associate attributes [1, 2, 3, 4] with the created matrix buffer.
     * Matrices are delivered to shaders as four four-element column vectors.
     *
     * A vertex divisor is specified that delivers one matrix to each
     * instance (the attribute pointer steps forward once per instance, as
     * opposed to once per vertex).
     */

    aob.setAttributeFloatingPointWithDivisor(
      1,
      this.matrices_buffer,
      4,
      JCGLScalarType.TYPE_FLOAT,
      matrix_size,
      0L,
      false,
      1);
    aob.setAttributeFloatingPointWithDivisor(
      2,
      this.matrices_buffer,
      4,
      JCGLScalarType.TYPE_FLOAT,
      matrix_size,
      4L * 4L,
      false,
      1);
    aob.setAttributeFloatingPointWithDivisor(
      3,
      this.matrices_buffer,
      4,
      JCGLScalarType.TYPE_FLOAT,
      matrix_size,
      8L * 4L,
      false,
      1);
    aob.setAttributeFloatingPointWithDivisor(
      4,
      this.matrices_buffer,
      4,
      JCGLScalarType.TYPE_FLOAT,
      matrix_size,
      12L * 4L,
      false,
      1);

    /*
     * Create the immutable array object.
     */

    this.array_object = g_ao.arrayObjectAllocate(aob);
    g_ao.arrayObjectUnbind();

    /*
     * Compile a trivial GLSL shader that will display the given triangle.
     */

    try {

      /*
       * Compile a vertex shader. Line separators are required by GLSL
       * and so are manually inserted into the lines of GLSL source code.
       */

      final List<String> vv_lines =
        IOUtils.readLines(
          ExampleInstancedTrianglesView.class.getResourceAsStream(
            "eye_instanced.vert"))
          .stream()
          .map(x -> x + System.lineSeparator())
          .collect(Collectors.toList());

      final JCGLVertexShaderType v =
        g_sh.shaderCompileVertex("eye_instanced.vert", vv_lines);

      /*
       * Compile a fragment shader.
       */

      final List<String> ff_lines =
        IOUtils.readLines(
          ExampleInstancedTrianglesView.class.getResourceAsStream("red.frag"))
          .stream()
          .map(x -> x + System.lineSeparator())
          .collect(Collectors.toList());

      final JCGLFragmentShaderType f =
        g_sh.shaderCompileFragment("red.frag", ff_lines);

      /*
       * Link the shaders into a program.
       */

      this.program =
        g_sh.shaderLinkProgram("simple", v, Optional.empty(), f);

      /*
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

    /*
     * Configure a clearing specification that will clear the color
     * buffer to a dark grey.
     */

    final JCGLClearSpecification.Builder cb =
      JCGLClearSpecification.builder();
    cb.setColorBufferClear(Vector4D.of(0.1, 0.1, 0.1, 1.0));
    this.clear = cb.build();
  }

  @Override
  public void onRender(final JCGLInterfaceGL33Type g)
  {
    final JCGLArrayObjectsType g_ao = g.getArrayObjects();
    final JCGLClearType g_c = g.getClear();
    final JCGLDrawType g_d = g.getDraw();
    final JCGLShadersType g_sh = g.getShaders();

    /*
     * Clear the window.
     */

    g_c.clear(this.clear);

    /*
     * Activate the program, bind the created array object, draw a triangle.
     */

    g_sh.shaderActivateProgram(this.program);
    g_ao.arrayObjectBind(this.array_object);

    /*
     * Construct the view and projection matrices, and use an identity model
     * matrix.
     */

    final Map<String, JCGLProgramUniformType> us = this.program.getUniforms();
    final JCGLProgramUniformType u_view = us.get("m_view");
    final JCGLProgramUniformType u_projection = us.get("m_projection");

    final Matrix4x4D view_matrix =
      JCGLViewMatrices.lookAtRH(
        Vector3D.of(0.0, 0.0, 5.0),
        Vectors3D.zero(),
        Vector3D.of(0.0, 1.0, 0.0));

    final Matrix4x4D proj_matrix =
      JCGLProjectionMatrices.perspectiveProjectionRH(
        0.0001,
        100.0,
        640.0 / 480.0,
        Math.toRadians(90.0));

    g_sh.shaderUniformPutMatrix4x4f(u_view, view_matrix);
    g_sh.shaderUniformPutMatrix4x4f(u_projection, proj_matrix);

    g_d.drawElementsInstanced(JCGLPrimitives.PRIMITIVE_TRIANGLES, 3);
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

    /*
     * Delete everything.
     */

    g_ib.indexBufferDelete(this.index_buffer);
    g_ab.arrayBufferDelete(this.array_buffer);
    g_ao.arrayObjectDelete(this.array_object);
    g_sh.shaderDeleteProgram(this.program);
  }
}
