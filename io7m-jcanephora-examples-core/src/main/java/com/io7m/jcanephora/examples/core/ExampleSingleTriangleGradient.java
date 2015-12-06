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
import com.io7m.jcanephora.core.JCGLScalarType;
import com.io7m.jcanephora.core.JCGLTexture2DType;
import com.io7m.jcanephora.core.JCGLTexture2DUpdateType;
import com.io7m.jcanephora.core.JCGLTextureFilterMagnification;
import com.io7m.jcanephora.core.JCGLTextureFilterMinification;
import com.io7m.jcanephora.core.JCGLTextureFormat;
import com.io7m.jcanephora.core.JCGLTextureUnitType;
import com.io7m.jcanephora.core.JCGLTextureUpdates;
import com.io7m.jcanephora.core.JCGLTextureWrapS;
import com.io7m.jcanephora.core.JCGLTextureWrapT;
import com.io7m.jcanephora.core.JCGLUnsignedType;
import com.io7m.jcanephora.core.JCGLUsageHint;
import com.io7m.jcanephora.core.JCGLVertexShaderType;
import com.io7m.jcanephora.core.api.JCGLArrayBuffersType;
import com.io7m.jcanephora.core.api.JCGLArrayObjectsType;
import com.io7m.jcanephora.core.api.JCGLClearType;
import com.io7m.jcanephora.core.api.JCGLDrawType;
import com.io7m.jcanephora.core.api.JCGLIndexBuffersType;
import com.io7m.jcanephora.core.api.JCGLInterfaceGL33Type;
import com.io7m.jcanephora.core.api.JCGLShadersType;
import com.io7m.jcanephora.core.api.JCGLTexturesType;
import com.io7m.jcanephora.cursors.JCGLRGB8ByteBuffered;
import com.io7m.jcanephora.cursors.JCGLRGB8Type;
import com.io7m.jpra.runtime.java.JPRACursor2DByteBufferedChecked;
import com.io7m.jpra.runtime.java.JPRACursor2DType;
import com.io7m.jtensors.VectorI4F;
import org.apache.commons.io.IOUtils;
import org.valid4j.Assertive;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * An example that renders a single triangle.
 */

public final class ExampleSingleTriangleGradient implements ExampleType
{
  private JCGLClearSpecification         clear;
  private JCGLArrayObjectType            array_object;
  private JCGLArrayBufferType            array_buffer;
  private JCGLIndexBufferType            index_buffer;
  private JCGLProgramShaderType          program;
  private JCGLTexture2DType              texture;
  private JCGLTexture2DUpdateType        texture_update;
  private JCGLProgramUniformType         texture_uniform;
  private JPRACursor2DType<JCGLRGB8Type> texture_update_cursor;

  /**
   * Construct an example.
   */

  public ExampleSingleTriangleGradient()
  {

  }

  @Override public void onInitialize(final JCGLInterfaceGL33Type g)
  {
    final JCGLArrayBuffersType g_ab = g.getArrayBuffers();
    final JCGLArrayObjectsType g_ao = g.getArrayObjects();
    final JCGLIndexBuffersType g_ib = g.getIndexBuffers();
    final JCGLShadersType g_sh = g.getShaders();
    final JCGLTexturesType g_tex = g.getTextures();

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
     * a single vec3 value representing the position, and a vec2 value holding
     * UV coordinates.
     *
     * Note that the allocated array buffer remains bound until it is explicitly
     * unbound.
     */

    final int vertex_size = (3 * 4) + (2 * 4);
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
      d.put(2, -0.5f);

      d.put(3, 0.0f);
      d.put(4, 1.0f);

      d.put(5, -0.5f);
      d.put(6, -0.5f);
      d.put(7, -0.5f);

      d.put(8, 0.0f);
      d.put(9, 0.0f);

      d.put(10, 0.5f);
      d.put(11, -0.5f);
      d.put(12, -0.5f);

      d.put(13, 1.0f);
      d.put(14, 0.0f);

      g_ab.arrayBufferUpdate(u);
      g_ab.arrayBufferUnbind();
    }

    /**
     * Create a new array object builder. Bind the index buffer to it,
     * and associate vertex attributes 0 and 1 with the created array buffer.
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
    aob.setAttributeFloatingPoint(
      1,
      this.array_buffer,
      2,
      JCGLScalarType.TYPE_FLOAT,
      vertex_size,
      3L * 4L,
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

      final Class<ExampleSingleTriangleGradient> cl =
        ExampleSingleTriangleGradient.class;

      final List<String> vv_lines =
        IOUtils.readLines(
          cl.getResourceAsStream("basic_uv.vert"))
          .stream()
          .map(x -> x + System.lineSeparator())
          .collect(Collectors.toList());

      final JCGLVertexShaderType v =
        g_sh.shaderCompileVertex("basic_uv.vert", vv_lines);

      /**
       * Compile a fragment shader.
       */

      final List<String> ff_lines =
        IOUtils.readLines(
          cl.getResourceAsStream("texture.frag"))
          .stream()
          .map(x -> x + System.lineSeparator())
          .collect(Collectors.toList());

      final JCGLFragmentShaderType f =
        g_sh.shaderCompileFragment("texture.frag", ff_lines);

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

      /**
       * Fetch the uniform for the texture parameter.
       */

      final Map<String, JCGLProgramUniformType> uniforms =
        this.program.getUniforms();
      Assertive.ensure(uniforms.containsKey("t_albedo"));
      this.texture_uniform = uniforms.get("t_albedo");

    } catch (final IOException e) {
      throw new UncheckedIOException(e);
    }

    /**
     * Allocate a texture and an update that can be reused repeatedly.
     */

    final List<JCGLTextureUnitType> units = g_tex.textureGetUnits();
    final JCGLTextureUnitType u0 = units.get(0);
    this.texture = g_tex.texture2DAllocate(
      u0, 64L, 64L,
      JCGLTextureFormat.TEXTURE_FORMAT_RGB_8_3BPP,
      JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
      JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
      JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    g_tex.textureUnitUnbind(u0);
    this.texture_update =
      JCGLTextureUpdates.newUpdateReplacingAll2D(this.texture);
    this.texture_update_cursor =
      JPRACursor2DByteBufferedChecked.newCursor(
        this.texture_update.getData(), 64, 64,
        JCGLRGB8ByteBuffered::newValueWithOffset);

    /**
     * Configure a clearing specification that will clear the color
     * buffer to a dark grey.
     */

    final JCGLClearSpecification.Builder cb =
      JCGLClearSpecification.builder();
    cb.setColorBufferClear(new VectorI4F(0.1f, 0.1f, 0.1f, 1.0f));
    this.clear = cb.build();
  }

  @Override public void onRender(final JCGLInterfaceGL33Type g)
  {
    final JCGLArrayObjectsType g_ao = g.getArrayObjects();
    final JCGLClearType g_c = g.getClear();
    final JCGLDrawType g_d = g.getDraw();
    final JCGLShadersType g_sh = g.getShaders();
    final JCGLTexturesType g_tex = g.getTextures();

    /**
     * Clear the window.
     */

    g_c.clear(this.clear);

    /**
     * Update the texture with a gradient and random data.
     */

    final List<JCGLTextureUnitType> units = g_tex.textureGetUnits();
    final JCGLTextureUnitType u0 = units.get(0);

    {
      final JCGLRGB8Type view = this.texture_update_cursor.getElementView();
      for (int y = 0; y < 64; ++y) {
        for (int x = 0; x < 64; ++x) {
          this.texture_update_cursor.setElementPosition(x, y);
          view.setR(x / 64.0);
          view.setG((64.0 - y) / 64.0);
          view.setB(Math.random());
        }
      }

      g_tex.texture2DUpdate(u0, this.texture_update);
    }

    /**
     * Activate the program, bind the created array object, draw a triangle.
     */

    g_sh.shaderActivateProgram(this.program);
    g_sh.shaderUniformPutTexture2DUnit(this.texture_uniform, u0);
    g_ao.arrayObjectBind(this.array_object);
    g_d.drawElements(JCGLPrimitives.PRIMITIVE_TRIANGLES);
    g_ao.arrayObjectUnbind();
    g_sh.shaderDeactivateProgram();
  }

  @Override public void onFinish(final JCGLInterfaceGL33Type g)
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
