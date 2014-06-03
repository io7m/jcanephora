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

package com.io7m.jcanephora.examples;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.io7m.jcanephora.AreaInclusive;
import com.io7m.jcanephora.ArrayAttributeDescriptor;
import com.io7m.jcanephora.ArrayAttributeType;
import com.io7m.jcanephora.ArrayBufferType;
import com.io7m.jcanephora.ArrayBufferUpdateUnmapped;
import com.io7m.jcanephora.ArrayBufferUpdateUnmappedType;
import com.io7m.jcanephora.ArrayDescriptor;
import com.io7m.jcanephora.ArrayDescriptorBuilderType;
import com.io7m.jcanephora.CursorWritable4fType;
import com.io7m.jcanephora.CursorWritableIndexType;
import com.io7m.jcanephora.FragmentShaderType;
import com.io7m.jcanephora.IndexBufferType;
import com.io7m.jcanephora.IndexBufferUpdateUnmapped;
import com.io7m.jcanephora.IndexBufferUpdateUnmappedType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLScalarType;
import com.io7m.jcanephora.Primitives;
import com.io7m.jcanephora.ProgramAttributeType;
import com.io7m.jcanephora.ProgramType;
import com.io7m.jcanephora.ProgramUniformType;
import com.io7m.jcanephora.ProjectionMatrix;
import com.io7m.jcanephora.UsageHint;
import com.io7m.jcanephora.VertexShaderType;
import com.io7m.jcanephora.api.JCGLInterfaceCommonType;
import com.io7m.jcanephora.utilities.ShaderUtilities;
import com.io7m.jranges.RangeInclusiveL;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorI2F;
import com.io7m.jtensors.VectorReadable2IType;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.FilesystemType;
import com.io7m.jvvfs.PathVirtual;

/**
 * Example program that draws a blended triangle to the screen, with an
 * orthographic projection.
 */

public final class ExampleShaders implements Example
{
  private final ArrayBufferType               array;
  private final ArrayBufferUpdateUnmappedType array_data;
  private final ArrayDescriptor               array_type;
  private final ExampleConfig                 config;
  private final JCGLInterfaceCommonType       gl;
  private boolean                             has_shut_down;
  private final IndexBufferType               indices;
  private final IndexBufferUpdateUnmappedType indices_data;
  private final MatrixM4x4F                   matrix_modelview;
  private final MatrixM4x4F                   matrix_projection;
  private final ProgramType                   program;

  public ExampleShaders(
    final ExampleConfig config1)
    throws JCGLException,
      IOException,
      FilesystemError
  {
    this.config = config1;
    this.gl = this.config.getGL().getGLCommon();
    this.matrix_modelview = new MatrixM4x4F();
    this.matrix_projection = new MatrixM4x4F();

    /**
     * Create a shader program. Compile vertex and fragment shaders, attach
     * them, and then link the program. Note that the vertex and fragment
     * shaders can be deleted after the program is created.
     */

    {
      final VertexShaderType v =
        this.gl.vertexShaderCompile("color", this.readFileLines(PathVirtual
          .ofString("/com/io7m/jcanephora/examples/color.v")));
      final FragmentShaderType f =
        this.gl.fragmentShaderCompile("color", this.readFileLines(PathVirtual
          .ofString("/com/io7m/jcanephora/examples/color.f")));

      this.program = this.gl.programCreateCommon("color", v, f);
      this.gl.vertexShaderDelete(v);
      this.gl.fragmentShaderDelete(f);
    }

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

    final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
    b.addAttribute(ArrayAttributeDescriptor.newAttribute(
      "position",
      JCGLScalarType.TYPE_FLOAT,
      4));
    b.addAttribute(ArrayAttributeDescriptor.newAttribute(
      "color",
      JCGLScalarType.TYPE_FLOAT,
      4));
    this.array_type = b.build();

    this.array =
      this.gl.arrayBufferAllocate(
        3,
        this.array_type,
        UsageHint.USAGE_STATIC_DRAW);

    /**
     * Then, allocate a buffer of data that will be populated and uploaded.
     */

    this.array_data =
      ArrayBufferUpdateUnmapped.newUpdateReplacingAll(this.array);

    {
      /**
       * Obtain typed cursors to the parts of the array to be populated. Note
       * that writes to the two cursors can be interleaved. Each cursor can
       * only point to the parts of the array relevant to their attribute.
       */

      final CursorWritable4fType pos_cursor =
        this.array_data.getCursor4f("position");
      final CursorWritable4fType col_cursor =
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

    this.gl.arrayBufferUpdate(this.array_data);

    /**
     * Allocate and initialize an index buffer.
     */

    this.indices =
      this.gl.indexBufferAllocate(this.array, 3, UsageHint.USAGE_STATIC_DRAW);
    this.indices_data = IndexBufferUpdateUnmapped.newReplacing(this.indices);

    {
      final CursorWritableIndexType ind_cursor =
        this.indices_data.getCursor();
      ind_cursor.putIndex(0);
      ind_cursor.putIndex(1);
      ind_cursor.putIndex(2);
    }

    this.gl.indexBufferUpdate(this.indices_data);
  }

  @Override public void display()
    throws JCGLException
  {
    this.gl.colorBufferClear3f(0.3f, 0.3f, 0.15f);

    /**
     * Initialize the projection matrix to an orthographic projection.
     */

    MatrixM4x4F.setIdentity(this.matrix_projection);
    ProjectionMatrix.makeOrthographicProjection(
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

    this.gl.programActivate(this.program);
    {
      /**
       * Get references to the program's uniform variable inputs.
       */

      final ProgramUniformType u_proj =
        this.program.programGetUniforms().get("matrix_projection");
      assert u_proj != null;

      final ProgramUniformType u_model =
        this.program.programGetUniforms().get("matrix_modelview");
      assert u_model != null;

      /**
       * Upload the matrices to the uniform variable inputs.
       */

      this.gl.programUniformPutMatrix4x4f(u_proj, this.matrix_projection);
      this.gl.programUniformPutMatrix4x4f(u_model, this.matrix_modelview);

      /**
       * Get references to the program's vertex attribute inputs.
       */

      final ProgramAttributeType p_pos =
        this.program.programGetAttributes().get("vertex_position");
      assert p_pos != null;

      final ProgramAttributeType p_col =
        this.program.programGetAttributes().get("vertex_color");
      assert p_col != null;

      /**
       * Get references to the array buffer's vertex attributes.
       */

      final ArrayAttributeType b_pos =
        this.array.arrayGetAttribute("position");
      final ArrayAttributeType b_col = this.array.arrayGetAttribute("color");

      /**
       * Bind the array buffer, and associate program vertex attribute inputs
       * with array vertex attributes.
       */

      this.gl.arrayBufferBind(this.array);
      this.gl.programAttributeArrayAssociate(p_col, b_col);
      this.gl.programAttributeArrayAssociate(p_pos, b_pos);

      /**
       * Draw primitives, using the array buffer and the given index buffer.
       */

      this.gl.drawElements(Primitives.PRIMITIVE_TRIANGLES, this.indices);
      this.gl.programAttributeArrayDisassociate(p_pos);
      this.gl.programAttributeArrayDisassociate(p_col);
      this.gl.arrayBufferUnbind();
    }
    this.gl.programDeactivate();
  }

  @Override public boolean hasShutDown()
  {
    return this.has_shut_down;
  }

  private List<String> readFileLines(
    final PathVirtual p)
    throws FilesystemError,
      IOException
  {
    final FilesystemType fs = this.config.getFilesystem();
    final InputStream s = fs.openFile(p);
    try {
      return ShaderUtilities.readLines(s);
    } finally {
      s.close();
    }
  }

  @Override public void reshape(
    final VectorReadable2IType position,
    final VectorReadable2IType size)
    throws JCGLException
  {
    ProjectionMatrix.makeOrthographicProjection(
      this.matrix_projection,
      0,
      size.getXI(),
      0,
      size.getYI(),
      1,
      100);

    final RangeInclusiveL range_x =
      new RangeInclusiveL(position.getXI(), position.getXI()
        + (size.getXI() - 1));
    final RangeInclusiveL range_y =
      new RangeInclusiveL(position.getYI(), position.getYI()
        + (size.getYI() - 1));

    this.gl.viewportSet(new AreaInclusive(range_x, range_y));
  }

  @Override public void shutdown()
    throws JCGLException
  {
    this.has_shut_down = true;
    this.gl.arrayBufferDelete(this.array);
    this.gl.indexBufferDelete(this.indices);
    this.gl.programDelete(this.program);
  }
}
