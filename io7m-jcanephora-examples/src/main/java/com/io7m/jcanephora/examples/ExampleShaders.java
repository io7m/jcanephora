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
package com.io7m.jcanephora.examples;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.ArrayBuffer;
import com.io7m.jcanephora.ArrayBufferAttribute;
import com.io7m.jcanephora.ArrayBufferAttributeDescriptor;
import com.io7m.jcanephora.ArrayBufferTypeDescriptor;
import com.io7m.jcanephora.ArrayBufferWritableData;
import com.io7m.jcanephora.CursorWritable4f;
import com.io7m.jcanephora.CursorWritableIndex;
import com.io7m.jcanephora.FragmentShader;
import com.io7m.jcanephora.IndexBuffer;
import com.io7m.jcanephora.IndexBufferWritableData;
import com.io7m.jcanephora.JCGLCompileException;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLInterfaceCommon;
import com.io7m.jcanephora.JCGLScalarType;
import com.io7m.jcanephora.Primitives;
import com.io7m.jcanephora.ProgramAttribute;
import com.io7m.jcanephora.ProgramReference;
import com.io7m.jcanephora.ProgramUniform;
import com.io7m.jcanephora.ProjectionMatrix;
import com.io7m.jcanephora.UsageHint;
import com.io7m.jcanephora.VertexShader;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorI2F;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathVirtual;

/**
 * Example program that draws a blended triangle to the screen, with an
 * orthographic projection.
 */

public final class ExampleShaders implements Example
{
  private final JCGLInterfaceCommon           gl;
  private final ArrayBufferTypeDescriptor     array_type;
  private final ArrayBuffer                   array;
  private final ArrayBufferWritableData       array_data;
  private final MatrixM4x4F                   matrix_projection;
  private final MatrixM4x4F                   matrix_modelview;
  private final IndexBuffer                   indices;
  private final IndexBufferWritableData       indices_data;
  private final ExampleConfig                 config;
  private boolean                             has_shut_down;
  private final ProgramReference              shader_program;
  private final VertexShader                  shader_vertex;
  private final FragmentShader                shader_fragment;
  private final Map<String, ProgramAttribute> program_attributes;
  private final Map<String, ProgramUniform>   program_uniforms;

  public ExampleShaders(
    final @Nonnull ExampleConfig config)
    throws ConstraintError,
      JCGLException,
      JCGLCompileException,
      IOException,
      FilesystemError
  {
    this.config = config;
    this.gl = this.config.getGL().getGLCommon();
    this.matrix_modelview = new MatrixM4x4F();
    this.matrix_projection = new MatrixM4x4F();

    /**
     * Create a shader program. Compile vertex and fragment shaders, attach
     * them, and then link the program.
     */

    this.shader_program = this.gl.programCreate("color");
    this.shader_vertex =
      this.gl.vertexShaderCompile(
        "color",
        config.getFilesystem().openFile(
          PathVirtual.ofString("/com/io7m/jcanephora/examples/color.v")));
    this.shader_fragment =
      this.gl.fragmentShaderCompile(
        "color",
        config.getFilesystem().openFile(
          PathVirtual.ofString("/com/io7m/jcanephora/examples/color.f")));

    this.gl.vertexShaderAttach(this.shader_program, this.shader_vertex);
    this.gl.fragmentShaderAttach(this.shader_program, this.shader_fragment);
    this.gl.programLink(this.shader_program);

    /**
     * Obtain references to all of the program's uniform and attribute
     * variables.
     */

    this.program_attributes = new HashMap<String, ProgramAttribute>();
    this.program_uniforms = new HashMap<String, ProgramUniform>();
    this.gl
      .programGetAttributes(this.shader_program, this.program_attributes);
    this.gl.programGetUniforms(this.shader_program, this.program_uniforms);

    /**
     * Allocate an array buffer.
     * 
     * Set up a type descriptor that describes the types of elements within
     * the array. In this case, each element of the array is a series of four
     * floats representing the position of a vertex, followed by a series of
     * four floats representing the color of a vertex.
     * 
     * Then, use this descriptor to allocate an array.
     */

    final ArrayBufferAttributeDescriptor[] ab =
      new ArrayBufferAttributeDescriptor[2];
    ab[0] =
      new ArrayBufferAttributeDescriptor(
        "position",
        JCGLScalarType.TYPE_FLOAT,
        4);
    ab[1] =
      new ArrayBufferAttributeDescriptor(
        "color",
        JCGLScalarType.TYPE_FLOAT,
        4);
    this.array_type = new ArrayBufferTypeDescriptor(ab);
    this.array =
      this.gl.arrayBufferAllocate(
        3,
        this.array_type,
        UsageHint.USAGE_STATIC_DRAW);

    /**
     * Then, allocate a buffer of data that will be populated and uploaded.
     */

    this.array_data = new ArrayBufferWritableData(this.array);

    {
      /**
       * Obtain typed cursors to the parts of the array to be populated. Note
       * that writes to the two cursors can be interleaved. Each cursor can
       * only point to the parts of the array relevant to their attribute.
       */

      final CursorWritable4f pos_cursor =
        this.array_data.getCursor4f("position");
      final CursorWritable4f col_cursor =
        this.array_data.getCursor4f("color");

      pos_cursor.put4f(-100.0f, 100.0f, -1.0f, 1.0f);
      col_cursor.put4f(1.0f, 0.0f, 0.0f, 1.0f);

      pos_cursor.put4f(-100.0f, -100.0f, -1.0f, 1.0f);
      col_cursor.put4f(0.0f, 1.0f, 0.0f, 1.0f);

      pos_cursor.put4f(100.0f, -100.0f, -1.0f, 1.0f);
      col_cursor.put4f(0.0f, 0.0f, 1.0f, 1.0f);
    }

    /**
     * Upload the array data.
     */

    this.gl.arrayBufferBind(this.array);
    this.gl.arrayBufferUpdate(this.array, this.array_data);

    /**
     * Allocate and initialize an index buffer.
     */

    this.indices = this.gl.indexBufferAllocate(this.array, 3);
    this.indices_data = new IndexBufferWritableData(this.indices);

    {
      final CursorWritableIndex ind_cursor = this.indices_data.getCursor();
      ind_cursor.putIndex(0);
      ind_cursor.putIndex(1);
      ind_cursor.putIndex(2);
    }

    this.gl.indexBufferUpdate(this.indices, this.indices_data);
  }

  @Override public void display()
    throws JCGLException,
      JCGLCompileException,
      ConstraintError
  {
    this.gl.colorBufferClear3f(0.3f, 0.3f, 0.15f);

    /**
     * Initialize the projection matrix to an orthographic projection.
     */

    MatrixM4x4F.setIdentity(this.matrix_projection);
    ProjectionMatrix.makeOrthographic(
      this.matrix_projection,
      0,
      640,
      0,
      480,
      1,
      100);

    /**
     * Initialize the modelview matrix, and translate.
     */

    MatrixM4x4F.setIdentity(this.matrix_modelview);
    MatrixM4x4F.translateByVector2FInPlace(
      this.matrix_modelview,
      new VectorI2F(this.config.getWindowSize().getXI() / 2, this.config
        .getWindowSize()
        .getYI() / 2));

    /**
     * Activate shading program, and associate parts of the array buffer with
     * inputs to the shader.
     */

    this.gl.programActivate(this.shader_program);
    {
      /**
       * Get references to the program's uniform variable inputs.
       */

      final ProgramUniform u_proj =
        this.program_uniforms.get("matrix_projection");
      final ProgramUniform u_model =
        this.program_uniforms.get("matrix_modelview");

      /**
       * Upload the matrices to the uniform variable inputs.
       */

      this.gl.programPutUniformMatrix4x4f(u_proj, this.matrix_projection);
      this.gl.programPutUniformMatrix4x4f(u_model, this.matrix_modelview);

      /**
       * Get references to the program's vertex attribute inputs.
       */

      final ProgramAttribute p_pos =
        this.program_attributes.get("vertex_position");
      final ProgramAttribute p_col =
        this.program_attributes.get("vertex_color");

      /**
       * Get references to the array buffer's vertex attributes.
       */

      final ArrayBufferAttribute b_pos = this.array.getAttribute("position");
      final ArrayBufferAttribute b_col = this.array.getAttribute("color");

      /**
       * Bind the array buffer, and associate program vertex attribute inputs
       * with array vertex attributes.
       */

      this.gl.arrayBufferBind(this.array);
      this.gl.arrayBufferBindVertexAttribute(this.array, b_pos, p_pos);
      this.gl.arrayBufferBindVertexAttribute(this.array, b_col, p_col);

      /**
       * Draw primitives, using the array buffer and the given index buffer.
       */

      this.gl.drawElements(Primitives.PRIMITIVE_TRIANGLES, this.indices);
      this.gl.arrayBufferUnbind();
    }
    this.gl.programDeactivate();
  }

  @Override public boolean hasShutDown()
  {
    return this.has_shut_down;
  }

  @Override public void reshape(
    final @Nonnull VectorReadable2I position,
    final @Nonnull VectorReadable2I size)
    throws JCGLException,
      ConstraintError,
      JCGLCompileException
  {
    ProjectionMatrix.makeOrthographic(
      this.matrix_projection,
      0,
      size.getXI(),
      0,
      size.getYI(),
      1,
      100);

    this.gl.viewportSet(position, size);
  }

  @Override public void shutdown()
    throws JCGLException,
      ConstraintError,
      JCGLCompileException
  {
    this.has_shut_down = true;
    this.gl.arrayBufferDelete(this.array);
    this.gl.indexBufferDelete(this.indices);
    this.gl.vertexShaderDelete(this.shader_vertex);
    this.gl.fragmentShaderDelete(this.shader_fragment);
    this.gl.programDelete(this.shader_program);
  }
}
