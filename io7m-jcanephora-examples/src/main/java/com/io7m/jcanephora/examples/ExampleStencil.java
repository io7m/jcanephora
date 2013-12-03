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
import java.util.ArrayList;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.ArrayBuffer;
import com.io7m.jcanephora.ArrayBufferAttribute;
import com.io7m.jcanephora.ArrayBufferAttributeDescriptor;
import com.io7m.jcanephora.ArrayBufferTypeDescriptor;
import com.io7m.jcanephora.ArrayBufferWritableData;
import com.io7m.jcanephora.CursorWritable4f;
import com.io7m.jcanephora.CursorWritableIndex;
import com.io7m.jcanephora.FaceSelection;
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
import com.io7m.jcanephora.ShaderUtilities;
import com.io7m.jcanephora.StencilFunction;
import com.io7m.jcanephora.StencilOperation;
import com.io7m.jcanephora.UsageHint;
import com.io7m.jcanephora.VertexShader;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorM2F;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathVirtual;

/**
 * Example program that draws a blended triangle to the screen, with an
 * orthographic projection.
 */

public final class ExampleStencil implements Example
{
  private static final int                STENCIL_MASK = 0xFF;
  private final ArrayBuffer               array;
  private final ArrayBufferWritableData   array_data;
  private final ArrayBufferTypeDescriptor array_type;
  private final ExampleConfig             config;
  private final JCGLInterfaceCommon       gl;
  private boolean                         has_shut_down;
  private final MatrixM4x4F               matrix_modelview;
  private final MatrixM4x4F               matrix_projection;
  private final ProgramReference          program;
  private final IndexBuffer               quad_indices;
  private final IndexBufferWritableData   quad_indices_data;
  private int                             time         = 0;
  private final VectorM2F                 translation  = new VectorM2F();
  private final IndexBuffer               triangle_indices;
  private final IndexBufferWritableData   triangle_indices_data;

  public ExampleStencil(
    final @Nonnull ExampleConfig config)
    throws ConstraintError,
      JCGLException,
      JCGLCompileException,
      IOException,
      FilesystemError
  {
    this.config = config;
    this.matrix_modelview = new MatrixM4x4F();
    this.matrix_projection = new MatrixM4x4F();
    this.gl = this.config.getGL().getGLCommon();

    {
      final VertexShader v =
        this.gl.vertexShaderCompile(
          "v",
          ShaderUtilities.readLines(config.getFilesystem().openFile(
            PathVirtual.ofString("/com/io7m/jcanephora/examples/color.v"))));
      final FragmentShader f =
        this.gl.fragmentShaderCompile(
          "f",
          ShaderUtilities.readLines(config.getFilesystem().openFile(
            PathVirtual.ofString("/com/io7m/jcanephora/examples/color.f"))));
      this.program = this.gl.programCreateCommon("color", v, f);
    }

    final ArrayList<ArrayBufferAttributeDescriptor> abs =
      new ArrayList<ArrayBufferAttributeDescriptor>();
    abs.add(new ArrayBufferAttributeDescriptor(
      "position",
      JCGLScalarType.TYPE_FLOAT,
      4));
    abs.add(new ArrayBufferAttributeDescriptor(
      "color",
      JCGLScalarType.TYPE_FLOAT,
      4));

    this.array_type = new ArrayBufferTypeDescriptor(abs);
    this.array =
      this.gl.arrayBufferAllocate(
        3,
        this.array_type,
        UsageHint.USAGE_STATIC_DRAW);
    this.array_data = new ArrayBufferWritableData(this.array);

    {
      final CursorWritable4f pos_cursor =
        this.array_data.getCursor4f("position");
      final CursorWritable4f col_cursor =
        this.array_data.getCursor4f("color");

      pos_cursor.put4f(-20.0f, 20.0f, -1.0f, 1.0f);
      pos_cursor.put4f(-20.0f, -20.0f, -1.0f, 1.0f);
      pos_cursor.put4f(20.0f, -20.0f, -1.0f, 1.0f);

      col_cursor.put4f(1.0f, 0.0f, 0.0f, 1.0f);
      col_cursor.put4f(0.0f, 1.0f, 0.0f, 1.0f);
      col_cursor.put4f(0.0f, 0.0f, 1.0f, 1.0f);
    }

    this.gl.arrayBufferBind(this.array);
    this.gl.arrayBufferUpdate(this.array_data);

    this.triangle_indices = this.gl.indexBufferAllocate(this.array, 3);
    this.triangle_indices_data =
      new IndexBufferWritableData(this.triangle_indices);

    {
      final CursorWritableIndex ind_cursor =
        this.triangle_indices_data.getCursor();
      ind_cursor.putIndex(0);
      ind_cursor.putIndex(1);
      ind_cursor.putIndex(2);
    }

    this.gl.indexBufferUpdate(this.triangle_indices_data);

    this.quad_indices = this.gl.indexBufferAllocate(this.array, 6);
    this.quad_indices_data = new IndexBufferWritableData(this.quad_indices);

    {
      final CursorWritableIndex ind_cursor =
        this.quad_indices_data.getCursor();
      ind_cursor.putIndex(0);
      ind_cursor.putIndex(1);
      ind_cursor.putIndex(2);

      ind_cursor.putIndex(0);
      ind_cursor.putIndex(2);
      ind_cursor.putIndex(3);
    }

    this.gl.indexBufferUpdate(this.quad_indices_data);
  }

  @Override public void display()
    throws JCGLException,
      JCGLCompileException,
      ConstraintError
  {
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
     * Activate shading program, and associate parts of the array buffer with
     * inputs to the shader.
     */

    this.gl.programActivate(this.program);
    {
      final ProgramUniform u_proj =
        this.program.getUniforms().get("matrix_projection");
      final ProgramUniform u_model =
        this.program.getUniforms().get("matrix_modelview");
      final ProgramAttribute p_pos =
        this.program.getAttributes().get("vertex_position");
      final ProgramAttribute p_col =
        this.program.getAttributes().get("vertex_color");

      final ArrayBufferAttribute b_pos = this.array.getAttribute("position");
      final ArrayBufferAttribute b_col = this.array.getAttribute("color");

      this.gl.programUniformPutMatrix4x4f(u_proj, this.matrix_projection);

      this.gl.arrayBufferBind(this.array);
      this.gl.programAttributeArrayAssociate(p_col, b_col);
      this.gl.programAttributeArrayAssociate(p_pos, b_pos);

      final int width = this.config.getWindowSize().getXI();
      final int height = this.config.getWindowSize().getYI();

      this.gl.colorBufferClear3f(0.2f, 0.1f, 0.3f);

      this.drawIntoStencil(u_model, width, height);
      this.drawTrianglesActual(u_model, width, height);
      this.gl.programAttributeArrayDisassociate(p_pos);
      this.gl.programAttributeArrayDisassociate(p_col);

      this.gl.stencilBufferDisable();
      this.gl.arrayBufferUnbind();
    }
    this.gl.programDeactivate();
  }

  private void drawIntoStencil(
    final ProgramUniform u_model,
    final int width,
    final int height)
    throws ConstraintError,
      JCGLException
  {
    /**
     * Write a set of triangles to the stencil buffer.
     * 
     * Disable writing to the color buffer.
     * 
     * Unconditionally set all bits touched by the drawn triangles to 1.
     */

    this.gl.colorBufferMask(false, false, false, false);
    this.gl.stencilBufferEnable();
    this.gl.stencilBufferClear(0);
    this.gl.stencilBufferFunction(
      FaceSelection.FACE_FRONT,
      StencilFunction.STENCIL_ALWAYS,
      1,
      ExampleStencil.STENCIL_MASK);
    this.gl.stencilBufferOperation(
      FaceSelection.FACE_FRONT,
      StencilOperation.STENCIL_OP_KEEP,
      StencilOperation.STENCIL_OP_KEEP,
      StencilOperation.STENCIL_OP_REPLACE);
    this.gl.stencilBufferMask(
      FaceSelection.FACE_FRONT,
      ExampleStencil.STENCIL_MASK);

    for (int y = 0; y < height; y += 50) {
      for (int x = 0; x < width; x += 50) {
        this.translation.x = x;
        this.translation.y = y;

        MatrixM4x4F.setIdentity(this.matrix_modelview);
        MatrixM4x4F.translateByVector2FInPlace(
          this.matrix_modelview,
          this.translation);

        this.gl.programUniformPutMatrix4x4f(u_model, this.matrix_modelview);
        this.gl.drawElements(
          Primitives.PRIMITIVE_TRIANGLES,
          this.triangle_indices);
      }
    }
  }

  private void drawTrianglesActual(
    final ProgramUniform u_model,
    final int width,
    final int height)
    throws ConstraintError,
      JCGLException
  {
    /**
     * Now draw a large rotating triangle, but only write to bits that were
     * set to 1 by the previous set of triangles.
     * 
     * Disable writing to the stencil buffer.
     */

    this.gl.colorBufferMask(true, true, true, true);
    this.gl.stencilBufferFunction(
      FaceSelection.FACE_FRONT_AND_BACK,
      StencilFunction.STENCIL_EQUAL,
      1,
      ExampleStencil.STENCIL_MASK);
    this.gl.stencilBufferMask(FaceSelection.FACE_FRONT_AND_BACK, 0x00);

    this.time = (this.time + 1) % 100;

    for (int y = -100; y < (height + 100); y += 50) {
      for (int x = -100; x < (width + 100); x += 50) {
        this.translation.x = x + this.time;
        this.translation.y = y + this.time;

        MatrixM4x4F.setIdentity(this.matrix_modelview);
        MatrixM4x4F.translateByVector2FInPlace(
          this.matrix_modelview,
          this.translation);

        this.gl.programUniformPutMatrix4x4f(u_model, this.matrix_modelview);
        this.gl.drawElements(
          Primitives.PRIMITIVE_TRIANGLES,
          this.quad_indices);
      }
    }
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
    ProjectionMatrix.makeOrthographicProjection(
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
    this.gl.indexBufferDelete(this.triangle_indices);
    this.gl.indexBufferDelete(this.quad_indices);
    this.gl.programDelete(this.program);
  }
}
